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
import javax.xml.bind.annotation.XmlType;
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
	 * Given a list (A) of a list (B) of Strings. Writes list A as a CSV to the specified writer
	 * where list B represents a row. Row size is not reinforced. Header should be first entry
	 * inside of the list.
	 *  <br /> 
	 *  If content is null it will be assumed to be an empty list
	 * @param writer Where to write content. Will not be closed 
	 * @param content 
	 */
	public static void writeListsToCSV(Writer writer, List<? extends List<String>> content){
		writeListsToCSV(writer, content, false);
	}
	/**
	 * Given a list (A) of a list (B) of Strings. Writes list A as a CSV to the specified writer
	 * where list B represents a row. Row size is not reinforced. Header should be first entry
	 * inside of the list.
	 *  <br /> 
	 *  If content is null it will be assumed to be an empty list
	 *  
	 * @param writer Where to write content. Will not be closed 
	 * @param content 
	 * @param closeWriter whether or not to close the writer when completed.
	 */
	public static void writeListsToCSV(Writer writer, List<? extends List<String>> content, boolean closeWriter){
		List<? extends List<String>> strings = content == null? new ArrayList<ArrayList<String>>() : content;
		
		PrintWriter pWriter = new PrintWriter(writer);
		for(List<String> row :  strings){
			String[] rowValues = row.toArray(new String[0]);
			for(int i = 0; i < rowValues.length; i ++){
				if(rowValues[i] == null){
					rowValues[i] = "";
				}
			}
			pWriter.println(toString(rowValues));
		}
		pWriter.flush();
		if(closeWriter){
			try {
				writer.close();
			}
			catch (IOException e) {
				throw new IllegalStateException("Unable to close writer", e);
			}
		}
	}

	
	/**
	 * Takes a list of an object annotated annotated with XmlRootElement and XmlElement and writes data
	 * into a CSV format to a writer
	 * 
	 * @param writer Where to write the CSV data
	 * @param iterable Data to write
	 * @param clazz The class of the data
	 * @throws InvocationTargetException
	 */
	public static <T> void writeIterabletoCSV(Writer writer, Iterable<T> iterable, Class<T> clazz) 
			throws InvocationTargetException {
		writeIterabletoCSV(writer, iterable, clazz, false);
	}
	
	/**
	 * Takes a list of an object annotated annotated with XmlRootElement and XmlElement and writes data
	 * into a CSV format to a writer
	 * @param writer Where to write the CSV data
	 * @param iterable Data to write
	 * @param clazz The class of the data
	 * @param closeStream whether or not to close stream upon operation completion
	 * @throws InvocationTargetException
	 */
	public static <T> void writeIterabletoCSV(Writer writer, Iterable<T> iterable, Class<T> clazz, boolean closeWriter) 
			throws InvocationTargetException {
		
		//Determine if property order exists
		XmlType xmlType = clazz.getAnnotation(XmlType.class);
		String [] propOrder = xmlType != null ? xmlType.propOrder() : null;
		
		//Organize all the data
		List<AccessibleObject> accessibles = getAccessibleFieldsAndMethods(clazz);
		accessibles = sortAccessibles(accessibles, propOrder);

		//Print headers and columns
		PrintWriter pWriter = new PrintWriter(writer);
		pWriter.println(toString(propOrder));
		for(T obj : iterable){
			String[] values = new String[accessibles.size()];
			for(int i = 0; i < accessibles.size(); i++){
				AccessibleObject accessor = accessibles.get(i);
				Object value = getValueWithAccessor(obj, accessor);
				values [i] = value == null ? "" : String.valueOf(value);
			}
			String valueText = toString(values);
			pWriter.println(valueText);
		}
		pWriter.flush();
		if(closeWriter){
			try {
				writer.close();
			}
			catch (IOException e) {
				throw new IllegalStateException("Unable to close writer", e);
			}
		}
	}
	
	/**
	 * Given a propOrder sorts the accessibles by name in the order specified. If no order is specified. 
	 * @param accessibles
	 * @param propOrder
	 * @return
	 */
	protected static List<AccessibleObject> sortAccessibles(List<AccessibleObject> accessibles, String [] propOrder){
		if(propOrder != null){
			List<AccessibleObject> orderedAccessibles = new ArrayList<AccessibleObject>();
			for(int i = 0; i < propOrder.length; i++){
				for(int j = 0; j < accessibles.size(); j++){
					String name = accessibles.get(j).getAnnotation(XmlElement.class).name();
					if(name.equals(propOrder[i])){
						orderedAccessibles.add(accessibles.remove(j));
					}
				}
			}
			if(!accessibles.isEmpty()){
				String [] unusedAccessibles = new String[accessibles.size()];
				for(int i = 0; i < accessibles.size(); i ++){
					AccessibleObject obj = accessibles.get(i);
					String objName = obj.getAnnotation(XmlElement.class).name();
					unusedAccessibles[i] = objName;
				}
				String msg = "The following properties name are present but not speified in @XmlType.propOrder : ";
				msg += Arrays.toString(unusedAccessibles);
				throw new IllegalStateException(msg);
			}
			return orderedAccessibles;
		}
		return accessibles;
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
				return ((Field)accessor).get(value);
			}
			catch(IllegalAccessException e){
				//This should never happen, if it does we are in an illegal state
				logger.error("Trying to access inaccessible field", e);
				throw new IllegalStateException(e);
			}
		}
		if(accessor instanceof Method){
			try {
				return ((Method)accessor).invoke(value);
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
	
	/** 
	 * Turn an array of objects into a CSV parsable string
	 * @param a
	 * @return
	 */
	private static String toString(Object[] a) {
        if (a == null || a.length == 0){
        	  return "\"\"";
        }
          
        int iMax = a.length - 1;
        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
        	
        	String element = String.valueOf(a[i]);
        	boolean hasComma = false;
        	
        	if(element.contains(",")){
        		b.append('"');
        		hasComma = true;
        	}
        	
        	element = element.replace("\"", "\"\"");
        	b.append(element);
        	
        	if(hasComma){
        		b.append('"');
        	}
        	
            if (i == iMax){
            	return b.toString();
            }
            
        	b.append(", ");
        }
    }
	
}
