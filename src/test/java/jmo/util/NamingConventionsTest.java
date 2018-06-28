package jmo.util;

import static org.junit.Assert.*;

import org.junit.Test;
import static jmo.util.NamingConventions.*;

public class NamingConventionsTest {

	@Test
	public void camelCase_allLowerCase_properCase() {
		assertEquals("Test", camelCase("test"));
	}

	@Test
	public void camelCase_allUpperCase_properCase() {
		assertEquals("Test", camelCase("TEST"));
	}

	@Test
	public void toCamelCaseFromSnakeCase_snakeCase_camelCase() {
		String s = "hello_world";
		assertEquals("HelloWorld", toCamelCaseFromSnakeCase(s));
	}
	@Test
	public void toScreamingSnakeCaseFromCamelCase_camcelcase_screamingSnake() {
		String s = "HelloWorld!";
		assertEquals("HELLO_WORLD!", toScreamingSnakeCaseFromCamelCase(s));
	}
}
