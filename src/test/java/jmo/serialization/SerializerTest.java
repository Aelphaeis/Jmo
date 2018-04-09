package jmo.serialization;

import static org.junit.Assert.assertEquals;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Test;

import jmo.serialization.Serializer;
import jmo.streams.NullOutputStream;

public class SerializerTest {

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

	
	public static class Person{
		
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
	
	@XmlRootElement
	public static class AnnotatedPerson {
		
		private String name;
		private int age;
		
		public AnnotatedPerson() {
		
		}
		
		public AnnotatedPerson(String name, int age){
			this();
			setAge(age);
			setName(name);
		}
		
		@XmlElement(name="name")
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		@XmlElement(name="age")
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
	}
}
