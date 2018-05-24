package jmo.serialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class SerializerTest {

	@Test
	public void deserializeStringtoDocument() throws Exception  {
		String xml = buildAnnotatedPersonXml();
		Document doc = Serializer.deserialize(xml);
		Node n = doc.getElementsByTagName("age").item(0);
		assertEquals("1", n.getTextContent());
		
		n = doc.getElementsByTagName("name").item(0);
		assertEquals("name", n.getTextContent());
	}
	
	@Test
	public void serializeDocument() throws Exception  {
		String xml = buildAnnotatedPersonXml();
		Document doc = Serializer.deserialize(xml);
		String result = Serializer.serialize(doc);
		assertTrue(result.contains("<AnnotatedPerson>"));
		assertTrue(result.contains("<name>"));
		assertTrue(result.contains("<age>"));
		assertTrue(result.contains("1"));
	}
	
	public String buildAnnotatedPersonXml() {
		StringBuilder builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
		builder.append("<AnnotatedPerson>\n");
		builder.append("  <age>1</age>\n");
		builder.append("  <name>name</name>\n");
		builder.append("</AnnotatedPerson>\n");
		return builder.toString();
	}
	
	@Test
	public void SerializerWriteListsToCSVTest(){
		List<List<String>> content = new ArrayList<List<String>>(3);
		List<String> A = new ArrayList<String>(3);
		List<String> B = new ArrayList<String>(3);
		List<String> C = new ArrayList<String>(3);
		
		A.add("Person's Name");
		A.add("Person's Age");
		A.add("Person's Sex");
		
		B.add("M1x-745");
		B.add("12053312123 ms");
		B.add("NonBinary");
		
		C.add("Morain, Joseph");
		C.add("25");
		C.add("Male");
		
		content.add(A);
		content.add(B);
		content.add(C);
		
		OutputStream stream = NullOutputStream.INSTANCE;
		Serializer.writeListsToCSV(new PrintWriter(stream), content);
	}
	
	
	@Test
	public void serializationCycleAnotatedTest() throws Exception {
		AnnotatedPerson ap = new AnnotatedPerson();
		ap.setAge(101);
		ap.setName("Joseph2");
		
		String xml = Serializer.serialize(ap);
		AnnotatedPerson apCopy = Serializer.deserialize(xml, ap.getClass());

		assertEquals(ap.getAge(), apCopy.getAge());
		assertEquals(ap.getName(), apCopy.getName());
	}
	
	@Test
	public void serializationCycleNonAnotatedTest() throws JAXBException{
		Person p = new Person();
		p.setAge(99);
		p.setName("Joseph");
		
		String xml = Serializer.serialize(p);
		Person pCopy = (Person) Serializer.deserialize(xml, p.getClass());
		
		
		assertEquals(p.getAge(), pCopy.getAge());
		assertEquals(p.getName(), pCopy.getName());
	}
	
	@Test
	public void writeIterableToCSVSuccessTest() throws InvocationTargetException {
		List<AnnotatedPerson> people = new ArrayList<AnnotatedPerson>();
		people.add(new AnnotatedPerson("A", 1));
		people.add(new AnnotatedPerson("B", 2));
		people.add(new AnnotatedPerson("C", 3));

		OutputStream stream = NullOutputStream.INSTANCE;
		Serializer.writeIterabletoCSV(new PrintWriter(stream), people, AnnotatedPerson.class);
	}
	
	@Test
	public void writeIterableToCSVNullValueSuccessTest() throws InvocationTargetException {
		List<AnnotatedPerson> people = new ArrayList<AnnotatedPerson>();
		people.add(new AnnotatedPerson("A", 1));
		people.add(new AnnotatedPerson("B", 2));
		people.add(new AnnotatedPerson(null, 3));

		OutputStream stream = NullOutputStream.INSTANCE;
		Serializer.writeIterabletoCSV(new PrintWriter(stream), people, AnnotatedPerson.class);
	}

	
	public static class Person {
		
		private String name;
		private int age;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getAccessibleFieldsAndMethods_unannotated_illegalArgument(){
		Serializer.getAccessibleFieldsAndMethods(Person.class);
	}
	
	@Test
	public void getAccessibleFieldsAndMethods_fieldAnnotation_ordered(){
		List<AccessibleObject> aos = Serializer
				.getAccessibleFieldsAndMethods(AnnotatedFieldPerson.class);
		assertEquals(2, aos.size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getAccessibleFieldsAndMethods_unannotatedField_err(){
		Serializer.getAccessibleFieldsAndMethods(UnannotatedFieldPerson.class);
	}
	
	@Test
	public void sortAccessibles_propType_OrderedProps() {
		Class<SortedAnnotatedPerson> clazz = SortedAnnotatedPerson.class;
		XmlType xtype = clazz.getAnnotation(XmlType.class); 
		String[] propOrder = xtype.propOrder();

		List<AccessibleObject> aos = Serializer
				.getAccessibleFieldsAndMethods(SortedAnnotatedPerson.class);
		
		aos = Serializer.sortAccessibles(aos, propOrder);
		assertEquals(2, aos.size());
		
		XmlElement a = aos.get(0).getDeclaredAnnotation(XmlElement.class);
		assertEquals("name", a.name());
		XmlElement b = aos.get(1).getDeclaredAnnotation(XmlElement.class);
		assertEquals("age", b.name());
	}
	
	@Test(expected=IllegalStateException.class)
	public void sortAccessibles_partialElementAnnotation_expect() {
		Class<?> clazz = PartialPropTypeAnnotationedPerson.class;
		XmlType xtype = clazz.getAnnotation(XmlType.class); 
		String[] propOrder = xtype.propOrder();

		List<AccessibleObject> aos = Serializer
				.getAccessibleFieldsAndMethods(clazz);
		Serializer.sortAccessibles(aos, propOrder);
	}
	
	@XmlRootElement
	public static class UnannotatedFieldPerson extends Person { }
	
	@XmlRootElement
	public static class AnnotatedFieldPerson {
		@XmlElement(name="name")
		public String name;
		@XmlElement(name="age")
		public int age;
		
		public int random;
	}
	
	@XmlRootElement
	@XmlType(propOrder= {"age"})
	public static class PartialPropTypeAnnotationedPerson extends Person {
		@XmlElement(name="name")
		@Override
		public String getName() {
			return super.getName();
		}
		
		@XmlElement(name="age")
		@Override
		public int getAge() {
			return super.getAge();
		}
	}
	
	@XmlRootElement
	public static class AnnotatedPerson extends Person {

		public AnnotatedPerson() { }
		
		public AnnotatedPerson(String name, int age){
			this();
			setAge(age);
			setName(name);
		}
		
		@XmlElement(name="name")
		@Override
		public String getName() {
			return super.getName();
		}
		
		@XmlElement(name="age")
		@Override
		public int getAge() {
			return super.getAge();
		}
	}
	
	@XmlRootElement
	@XmlType(propOrder= {"name","age"})
	public static class SortedAnnotatedPerson extends AnnotatedPerson {

		public SortedAnnotatedPerson() { 
			super();
		}
		
		public SortedAnnotatedPerson(String name, int age){
			super(name, age);
		}
	}
	
	public static class NullOutputStream extends OutputStream {

		public static final NullOutputStream INSTANCE = new NullOutputStream();

		@Override
		public void write(int b) {
			// Do nothing
		}

		@Override
		public void write(byte[] b, int offset, int length) {
			// do nothing
		}
		
		private NullOutputStream() {
			//We don't really need people creating instances of this.
		}
	}

}
