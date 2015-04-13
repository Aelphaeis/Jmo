package jmo.serialization;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

public final class Serializer{
	
	private Serializer(){
		//we don't want anyone instantiating this class.
	}
	public static <T> String serialize(T obj) throws Exception{
		/* Casting Class<? extends Object> to Class<? extends T> is impossible
		 * to guarantee, hence we receive an unchecked warning.
		 * We suppress this because we are in control of both the source and
		 * result of the assignment operation.
		 */
		@SuppressWarnings("unchecked") 
		Class<T> objClass = (Class<T>) obj.getClass();
		JAXBContext context = JAXBContext.newInstance(objClass);
		QName qualifiedName =  new QName(obj.getClass().getName());
		JAXBElement<T> element = new JAXBElement<T>(qualifiedName, objClass, obj );
		
		StringWriter stringWriter = new StringWriter();
	    Marshaller m = context.createMarshaller();
	   
	    //This makes things readable.
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    
	    m.marshal(element, stringWriter);
	    return stringWriter.toString();
	}
}
