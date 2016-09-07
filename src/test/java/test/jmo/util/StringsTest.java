package test.jmo.util;

import static org.junit.Assert.*;

import org.junit.Test;

import jmo.util.Strings;

public class StringsTest {

	@Test
	public void isBlankNullTest() {
		String value = null;
		assertTrue(Strings.isBlank(value));
	}

	@Test
	public void isBlankEmptyTest() {
		String value = "";
		assertTrue(Strings.isBlank(value));
	}
	
	@Test
	public void isBlankSpaceTest() {
		String value = "    ";
		assertTrue(Strings.isBlank(value));
	}
	
	@Test
	public void isBlankValueTest() {
		String value = "value";
		assertFalse(Strings.isBlank(value));
	}
}
