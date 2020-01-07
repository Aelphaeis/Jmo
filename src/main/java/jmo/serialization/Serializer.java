package jmo.serialization;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class Serializer {
	private static final Logger logger = LoggerFactory.getLogger(Serializer.class);

	private Serializer() {
		// we don't want anyone instantiating this class.
	}

	/**
	 * Given an object returns an xml representation of the object
	 * 
	 * @param obj
	 *            object to serialize to xml
	 * @return
	 * @throws JAXBException
	 *             if the object cannot be serialized
	 */
	public static <T> String serialize(T obj) throws JAXBException {
		StringWriter sw = new StringWriter();
		serialize(obj, sw);
		return sw.toString();
	}

	/**
	 * Given an document returns a string representation of the object
	 * 
	 * @param obj
	 *            object to serialize to xml
	 * @return
	 * @throws JAXBException
	 *             if the object cannot be serialized
	 */
	public static String serialize(Document obj) {
		StringWriter sw = new StringWriter();
		serialize(obj, sw);
		return sw.toString();
	}

	/**
	 * Given an object and a writer will write the xml object to the writer.
	 * 
	 * @param obj
	 * @param writer
	 * @throws JAXBException
	 *             if the object cannot be serialized.
	 */
	public static <T> void serialize(T obj, Writer writer)
			throws JAXBException {
		/*
		 * Casting Class<? extends Object> to Class<? extends T> is impossible
		 * to guarantee, hence we receive an unchecked warning. We suppress this
		 * because we are in control of both the source and result of the
		 * assignment operation.
		 */
		@SuppressWarnings("unchecked")
		Class<T> objClass = (Class<T>) obj.getClass();
		JAXBContext context = JAXBContext.newInstance(objClass);
		QName qName = new QName(obj.getClass().getSimpleName());
		JAXBElement<T> element = new JAXBElement<>(qName, objClass, obj);

		Marshaller m = context.createMarshaller();

		// This makes things readable.
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.marshal(element, writer);
	}

	/**
	 * Given a document and a writer, will write the contents of the document to
	 * the writer.
	 * 
	 * @param document
	 * @param writer
	 */
	public static void serialize(Document document, Writer writer) {
		final String indent = "{http://xml.apache.org/xslt}indent-amount";
		try {
			TransformerFactory tf = transformerMaker();

			Transformer t = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(writer);

			t.setOutputProperty(indent, "2");
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			t.transform(source, result);
		} catch (TransformerException e) {
			String err = "Unrecoverable error occurred during transformation";
			logger.error(err, e);
			throw new IllegalArgumentException(err);
		}
	}

	/**
	 * Deserializes a string to specified object.
	 * 
	 * @param xml
	 *            string to deserialize
	 * @param clazzs
	 *            class we will deserialize to
	 * @return
	 * @throws JAXBException
	 *             if Object cannot be deserialized
	 */
	public static <T> T deserialize(String xml, Class<T> clazz)
			throws JAXBException {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

			DocumentBuilder dBuilder = dbf.newDocumentBuilder();
			InputSource xmlSource = new InputSource(new StringReader(xml));
			Document doc = dBuilder.parse(xmlSource);

			JAXBContext context = JAXBContext.newInstance(clazz);
			Unmarshaller unmarshaller = context.createUnmarshaller();

			JAXBElement<T> element = unmarshaller.unmarshal(doc, clazz);
			return element.getValue();
		} catch (ParserConfigurationException e) { // This should not occur
			throw new SerializerException("Unable to configure parser", e);
		} catch (IOException e) { // This should not occur ever 
			throw new SerializerException("Unable to read document", e);
		} catch (SAXException e) {
			// The Xml input is not formatted properly.
			throw new IllegalArgumentException("Illegal Xml input", e);
		}
	}

	/**
	 * Given a input stream deserializes the xml into a document
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static Document deserialize(Reader is) throws IOException {
		try {
			TransformerFactory factory = transformerMaker();
			Transformer transformer = factory.newTransformer();
			StreamSource source = new StreamSource(is);
			DOMResult result = new DOMResult();

			transformer.transform(source, result);

			return (Document) result.getNode();
		} catch (TransformerException e) {
			throw new IOException(e);
		}
	}
	
	public static Document deserialize(File file)throws IOException {
		try (FileReader reader = new FileReader(file)){
			return deserialize(reader);
		}
	}
	
	/**
	 * Given a string of xml, deserializes the xml into a document
	 * 
	 * @param xml
	 * @return
	 * @throws IOException
	 */
	public static Document deserialize(String xml) throws IOException {
		return deserialize(new StringReader(xml));
	}
	
	/**
	 * Given an XML annotated class, creates an XSD and returns as a string.
	 * @param cls
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static <T> String toSchema(Class<T> clazz)
			throws JAXBException, IOException {
		StringWriter w = new StringWriter();
		try {
			toSchema(clazz, w);
		}
		catch(IOException e ) {
			//This shouldn't happen with string writer.
			throw new SerializerException(e);
		}
		return w.toString();
	}
	
	/**
	 * Given an XML annotated class, creates an XSD and writes it to a stream
	 * @param cls
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static <T> void toSchema(Class<T> cls, Writer writer)
			throws IOException, JAXBException {
		JAXBContext ctxt = JAXBContext.newInstance(cls);
		SchemaOutputResolver resolver = new SchemaOutputResolver() {
			@Override
			public Result createOutput(String uri, String name)
					throws IOException {
				StreamResult result = new StreamResult(writer);
				result.setSystemId(name);
				return result;
			}
		};
		ctxt.generateSchema(resolver);
	}
	
	/**
	 * Create a transformerFactory with the settings specified in the map.
	 * The factory will always have secure processing set to true.
	 * 
	 * @return
	 */
	public static TransformerFactory transformerMaker() {
		return transformerMaker(null);
	}
	
	/**
	 * Create a transformerFactory with the settings specified in the map.
	 * The factory will always have secure processing set to true.
	 * 
	 * @param o
	 * @return
	 */
	public static TransformerFactory transformerMaker(Map<String, Boolean> o) {
		try {
			Map<String, Boolean> feats = o != null ? o : new HashMap<>();
			TransformerFactory f = TransformerFactory.newInstance();
			for(Map.Entry<String, Boolean> entry : feats.entrySet()) {
				f.setFeature(entry.getKey(), entry.getValue());
			}
			f.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			return f;
		}
		catch(TransformerConfigurationException e) {
			throw new SerializerException(e);
		}
	}
	
	/**
	 * Given a list (A) of a list (B) of Strings. Writes list A as a CSV to the
	 * specified writer where list B represents a row. Row size is not
	 * reinforced. Header should be first entry inside of the list. <br />
	 * If content is null it will be assumed to be an empty list
	 * 
	 * @param writer
	 *            Where to write content. Will not be closed
	 * @param content
	 */
	public static void writeListsToCSV(Writer writer,
			List<? extends List<String>> content) {
		writeListsToCSV(writer, content, false);
	}
	/**
	 * Given a list (A) of a list (B) of Strings. Writes list A as a CSV to the
	 * specified writer where list B represents a row. Row size is not .
	 * reinforced. Header should be first entry inside of the list.<br />
	 * If content is null it will be assumed to be an empty list
	 * 
	 * @param writer
	 *            Where to write content. Will not be closed
	 * @param content
	 * @param closeWriter
	 *            whether or not to close the writer when completed.
	 */
	public static void writeListsToCSV(Writer writer,
			List<? extends List<String>> content, boolean closeWriter) {
		List<? extends List<String>> strings = content == null
				? new ArrayList<>()
				: content;

		PrintWriter pWriter = new PrintWriter(writer);
		for (List<String> row : strings) {
			String[] rowValues = row.toArray(new String[0]);
			for (int i = 0; i < rowValues.length; i++) {
				if (rowValues[i] == null) {
					rowValues[i] = "";
				}
			}
			pWriter.println(toString(rowValues));
		}
		pWriter.flush();
		if (closeWriter) {
			try {
				writer.close();
			} catch (IOException e) {
				throw new IllegalStateException("Unable to close writer", e);
			}
		}
	}

	/**
	 * Takes a list of an object annotated annotated with XmlRootElement and
	 * XmlElement and writes data into a CSV format to a writer
	 * 
	 * @param writer
	 *            Where to write the CSV data
	 * @param iterable
	 *            Data to write
	 * @param clazz
	 *            The class of the data
	 * @throws InvocationTargetException
	 */
	public static <T> void writeIterabletoCSV(Writer writer,
			Iterable<T> iterable, Class<T> clazz)
			throws InvocationTargetException {
		writeIterabletoCSV(writer, iterable, clazz, false);
	}

	/**
	 * Takes a list of an object annotated annotated with XmlRootElement and
	 * XmlElement and writes data into a CSV format to a writer
	 * 
	 * @param w
	 *            Where to write the CSV data
	 * @param it
	 *            Data to write
	 * @param cls
	 *            The class of the data
	 * @param closeStream
	 *            whether or not to close stream upon operation completion
	 * @throws InvocationTargetException
	 */
	public static <T> void writeIterabletoCSV(Writer w, Iterable<T> it,
			Class<T> cls, boolean closeWriter)
			throws InvocationTargetException {
		
		// Determine if property order exists
		XmlType xmlType = cls.getAnnotation(XmlType.class);
		String[] propOrder = xmlType != null ? xmlType.propOrder() : null;

		// Organize all the data
		List<AccessibleObject> accessibles = getAccessibleFieldsAndMethods(cls);
		accessibles = sortAccessibles(accessibles, propOrder);

		// Print headers and columns
		PrintWriter pWriter = new PrintWriter(w);
		pWriter.println(toString(propOrder));
		for (T obj : it) {
			String[] values = new String[accessibles.size()];
			for (int i = 0; i < accessibles.size(); i++) {
				AccessibleObject accessor = accessibles.get(i);
				Object value = getValueWithAccessor(obj, accessor);
				values[i] = value == null ? "" : String.valueOf(value);
			}
			String valueText = toString(values);
			pWriter.println(valueText);
		}
		pWriter.flush();
		if (closeWriter) {
			try {
				w.close();
			} catch (IOException e) {
				throw new IllegalStateException("Unable to close writer", e);
			}
		}
	}

	/**
	 * Given a propOrder sorts the accessible by name in the order specified.
	 * If no order is specified.
	 * 
	 * @param accessibles
	 * @param order
	 * @return
	 */
	static List<AccessibleObject> sortAccessibles(
			List<AccessibleObject> accessibles, String[] order) {
		if (order != null) {
			List<AccessibleObject> orderedAccessibles = new ArrayList<>();
			
			
			for (int i = 0; i < order.length; i++) {
				for (int j = 0; j < accessibles.size(); j++) {
					AccessibleObject obj = accessibles.get(j);
					String name = obj.getAnnotation(XmlElement.class).name();
					if (name.equals(order[i])) {
						orderedAccessibles.add(accessibles.remove(j));
					}
				}
			}
			if (!accessibles.isEmpty()) {
				String[] unusedAccessibles = new String[accessibles.size()];
				for (int i = 0; i < accessibles.size(); i++) {
					AccessibleObject obj = accessibles.get(i);
					String objName = obj.getAnnotation(XmlElement.class).name();
					unusedAccessibles[i] = objName;
				}
				String msg = "The following properties name are present but not specified in @XmlType.propOrder : ";
				msg += Arrays.toString(unusedAccessibles);
				throw new IllegalStateException(msg);
			}
			return orderedAccessibles;
		}
		return accessibles;
	}

	/**
	 * Gets all publicly accessible Objects annotated with XmlElements assuming
	 * the class specified is annotated with XmlRootElement
	 * 
	 * @param clazz
	 * @return list of AccessibleOjects
	 */
	static <T> List<AccessibleObject> getAccessibleFieldsAndMethods(
			Class<T> clazz) {
		List<AccessibleObject> accessibles = new ArrayList<>();

		if (!clazz.isAnnotationPresent(XmlRootElement.class)) {
			String err = "Class must be annotated with " + XmlRootElement.class;
			throw new IllegalArgumentException(err);
		}

		for (AccessibleObject obj : clazz.getFields()) {
			if (obj.isAnnotationPresent(XmlElement.class)) {
				accessibles.add(obj);
			}
		}

		for (AccessibleObject obj : clazz.getMethods()) {
			if (obj.isAnnotationPresent(XmlElement.class)) {
				accessibles.add(obj);
			}
		}

		if (accessibles.isEmpty()) {
			String err = "Class must be annotated with " + XmlElement.class;
			throw new IllegalArgumentException(err);
		}

		return accessibles;
	}

	/**
	 * Return the value of an AccessibleObject as a string if it is a Field or
	 * Method without arguments.
	 * 
	 * @param value
	 *            The object we are trying to extract the value from
	 * @param accessor
	 *            The method or field we will try to extract value from
	 * @throws InvocationTargetException
	 *             If AccessibleObject is method and throws an exception
	 */
	static Object getValueWithAccessor(Object value,
			AccessibleObject accessor) throws InvocationTargetException {
		if (accessor instanceof Field) {
			try {
				return ((Field) accessor).get(value);
			} catch (IllegalAccessException e) {
				// if this happens we are in an illegal state
				throw new SerializerException(e);
			}
		}
		if (accessor instanceof Method) {
			try {
				return ((Method) accessor).invoke(value);
			} catch (IllegalAccessException e) {
				// if this happens we are in an illegal state
				throw new SerializerException(e);
			} 
		}
		throw new IllegalArgumentException("Accessor is not a Field or Method");
	}

	/**
	 * Turn an array of objects into a CSV parsable string
	 * 
	 * @param a
	 * @return
	 */
	private static String toString(Object[] a) {
		if (a == null || a.length == 0) {
			return "\"\"";
		}

		int iMax = a.length - 1;
		StringBuilder b = new StringBuilder();
		for (int i = 0;; i++) {

			String element = String.valueOf(a[i]);
			boolean hasComma = false;

			if (element.contains(",")) {
				b.append('"');
				hasComma = true;
			}

			element = element.replace("\"", "\"\"");
			b.append(element);

			if (hasComma) {
				b.append('"');
			}

			if (i == iMax) {
				return b.toString();
			}

			b.append(", ");
		}
	}
	
	public static class SerializerException extends RuntimeException{

		private static final long serialVersionUID = 1L;

		public SerializerException() {
			super();
		}

		public SerializerException(String message, Throwable cause) {
			super(message, cause);
		}

		public SerializerException(String message) {
			super(message);
		}

		public SerializerException(Throwable cause) {
			super(cause);
		}
	}
}
