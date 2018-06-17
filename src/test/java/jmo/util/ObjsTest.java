package jmo.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ObjsTest {

	String string;

	@Test
	public void equalGetterValues_equalObjs_equality() throws Exception {
		Person a = new Person();
		a.setAge(10);;
		a.setName("person");

		Person b = new Person();
		b.setAge(10);
		b.setName("person");

		assertTrue(Objs.equalGetterValues(a, b));
	}
	@Test
	public void equalGetterValues_null_equal() throws Exception {
		assertTrue(Objs.equalGetterValues(null, null));
	}

	@Test
	public void equalGetterValues_nullandObj_notequal() throws Exception {
		Person a = new Person();
		a.setAge(10);;
		a.setName("person");

		assertFalse(Objs.equalGetterValues(a, null));
		assertFalse(Objs.equalGetterValues(null, a));

	}
	@Test
	public void equalGetterValues_diffValues_notEqual() throws Exception {
		Person a = new Person();
		a.setAge(10);;
		a.setName("notPerson");

		Person b = new Person();
		b.setAge(10);
		b.setName("person");

		assertFalse(Objs.equalGetterValues(a, b));
	}

	@Test(expected = IllegalArgumentException.class)
	public void equalGetterValues_iae_iae() throws Exception {
		Person a = new Person() {
			@Override
			public String getName() {
				throw new RuntimeException();
			}
		};
		assertFalse(Objs.equalGetterValues(a, new Person()));
	}

	public static class Person {
		private String getterless;
		private String string;
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
		public String getString() {
			return string;
		}
		public void setString(String string) {
			this.string = string;
		}
		@Override
		public String toString() {
			// no reason for this, just didn't like the warning
			return Objs.co(getterless, super.toString());
		}
		public void setGetterless(String getterless) {
			this.getterless = getterless;
		}
	}
}
