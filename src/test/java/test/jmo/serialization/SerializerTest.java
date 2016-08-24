package test.jmo.serialization;

import static org.junit.Assert.*;

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
		System.out.println(xml);
		Person pCopy = (Person) Serializer.deserialize(xml, p.getClass());
		
		
		assertEquals(p.getAge(), pCopy.getAge());
		assertEquals(p.getName(), pCopy.getName());
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
		
		@XmlElement(name="name")
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
}
