package test.jmo.serialization;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
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

public class SerializerTest {

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
	public void test() throws InvocationTargetException {
		List<AnnotatedPerson> people = new ArrayList<AnnotatedPerson>();
		people.add(new AnnotatedPerson("A", 1));
		people.add(new AnnotatedPerson("B", 2));
		people.add(new AnnotatedPerson("C", 3));

		Serializer.writeIterabletoCSV(new PrintWriter(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				
			}
		}), people, AnnotatedPerson.class);
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
