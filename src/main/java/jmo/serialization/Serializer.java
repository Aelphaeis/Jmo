package jmo.serialization;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

//TODO Allow for multiple classes to be inserted into serializer.
public final class Serializer {
	private static final Logger logger =  LoggerFactory.getLogger(Serializer.class);
	
	private Serializer() {
		// we don't want anyone instantiating this class.
	}

	/**
	 * Given an object returns an xml representation of the object
	 * @param obj object to serialize to xml
	 * @return
	 * @throws JAXBException if the object cannot be serialized
	 */
	public static <T> String serialize(T obj) throws JAXBException{
		StringWriter stringWriter = new StringWriter();
		serialize(obj, stringWriter);
		return stringWriter.toString();
	}
	
	/**
	 * Given an object and a writer will write the xml object to the writer.
	 * @param obj
	 * @param writer
	 * @throws JAXBException if the object cannot be serialized.
	 */
	public static <T> void serialize(T obj, Writer writer) throws JAXBException{
		/*
		 * Casting Class<? extends Object> to Class<? extends T> is impossible
		 * to guarantee, hence we receive an unchecked warning. We suppress this
		 * because we are in control of both the source and result of the
		 * assignment operation.
		 */
		@SuppressWarnings("unchecked")
		Class<T> objClass = (Class<T>) obj.getClass();
		JAXBContext context = JAXBContext.newInstance(objClass);
		QName qualifiedName = new QName(obj.getClass().getSimpleName());
		JAXBElement<T> element = new JAXBElement<T>(qualifiedName, objClass, obj);

		Marshaller m = context.createMarshaller();

		//This makes things readable.
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.marshal(element, writer);
	}
	
	/**
	 * Deserializes a string to specified object.
	 * @param xml string to deserialize
	 * @param clazzs class we will deserialize to
	 * @return
	 * @throws JAXBException if Object cannot be deserialized
	 */
	public static <T> T deserialize(String xml, Class<T> clazz) throws JAXBException{
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputSource xmlSource = new InputSource(new StringReader(xml));
			Document doc = dBuilder.parse(xmlSource);
			
			JAXBContext context = JAXBContext.newInstance(clazz);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			JAXBElement<T> element = unmarshaller.unmarshal(doc, clazz);
			return element.getValue();
		} 
		catch (ParserConfigurationException e) {
			//This should not occur ever
			logger.error(e.getMessage(), e);
			throw new IllegalStateException("Unable to configure parser", e);
		} 
		catch(IOException e){
			//This should not occur ever
			logger.error(e.getMessage(), e);
			throw new IllegalStateException("Unable to read document", e);
		}
		catch (SAXException e) {
			//The Xml input is not formatted properly.
			throw new IllegalArgumentException("Illegal Xml input", e);
		}
	}
	
	/**
	 * Takes a list of an object annotated annotated with XmlRootElement and XmlElement and writes data
	 * into a CSV format to a writer
	 * @param writer Where to write the CSV data
	 * @param iterable Data to write
	 * @param clazz The class of the data
	 * @throws InvocationTargetException
	 */
	public static <T> void writeIterabletoCSV(Writer writer, Iterable<T> iterable, Class<T> clazz) 
			throws InvocationTargetException {
		
		PrintWriter pWriter = new PrintWriter(writer);
		List<AccessibleObject> accessibles = getAccessibleFieldsAndMethods(clazz);
		String[] headers = new String[accessibles.size()];
		
		for(int i = 0; i < headers.length; i++){
			headers[i] = accessibles.get(i).getAnnotation(XmlElement.class).name();
		}

		String headerText = Arrays.toString(headers);
		headerText = headerText.substring(1, headerText.length() - 1);
		
		pWriter.println(headerText);
	
		for(T obj : iterable){
			String[] values = new String[accessibles.size()];
			for(int i = 0; i < accessibles.size(); i++){
				AccessibleObject accessor = accessibles.get(i);
				values [i] = getValueWithAccessor(obj, accessor).toString();
			}
			String valueText = Arrays.toString(values);
			valueText = valueText.substring(1, valueText.length() - 1);
			pWriter.println(valueText);
		}
		pWriter.flush();
	}
	
	/**
	 * Gets all publicly accessible Objects annotated with XmlElements assuming the class specified
	 * is annotated with XmlRootElement
	 * @param clazz
	 * @return list of AccessibleOjects
	 */
	protected static <T> List<AccessibleObject> getAccessibleFieldsAndMethods(Class<T> clazz){
		List<AccessibleObject> accessibles = new ArrayList<AccessibleObject>();
		
		if(!clazz.isAnnotationPresent(XmlRootElement.class)){
			throw new IllegalArgumentException("Class must be annotated with " + XmlRootElement.class);
		}
		
		for(AccessibleObject obj : clazz.getFields()){
			if(obj.isAnnotationPresent(XmlElement.class)){
				accessibles.add(obj);
			}
		}
		
		for(AccessibleObject obj : clazz.getMethods()){
			if(obj.isAnnotationPresent(XmlElement.class)){
				accessibles.add(obj);
			}
		}
		
		if(accessibles.isEmpty()){
			throw new IllegalArgumentException("Class must be annotated with " + XmlElement.class);
		}
		
		return accessibles;
	}
	
	/**
	 * Return the value of an AccessibleObject as a string if it is a Field or Method without arguments.
	 * @param value The object we are trying to extract the value from
	 * @param accessor The method or field we will try to extract value from
	 * @throws InvocationTargetException If AccessibleObject is method and throws an exception
	 */
	protected static Object getValueWithAccessor(Object value, AccessibleObject accessor) 
			throws InvocationTargetException{
		if(accessor instanceof Field){
			try{ 
				return ((Field)accessor).get(value).toString();
			}
			catch(IllegalAccessException e){
				//This should never happen, if it does we are in an illegal state
				logger.error("Trying to access inaccessible field", e);
				throw new IllegalStateException(e);
			}
		}
		if(accessor instanceof Method){
			try {
				return ((Method)accessor).invoke(value).toString();
			} 
			catch (IllegalAccessException e) {
				//this should never happen if it does we are in an illegal state
				logger.error("Trying to access inaccessible field", e);
				throw new IllegalStateException(e);
			} 
			catch (InvocationTargetException e) {
				//This could happen, unlikely, but possible.
				logger.error("The method we called threw an exception", e);
				throw e;
			}
		}
		throw new IllegalArgumentException("Accessor is not a Field or Method");
	}
}
