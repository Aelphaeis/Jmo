package jmo.serialization;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
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
	 * Gien an object and a writer will write the xml object to the writer.
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
	 * De-serializes a string to specified object.
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
	
}
