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
	
	@Test
	public void padLeftCorrect() {
		String value = Strings.EMPTY;
		value = Strings.padLeft(value, 3, 'p');
		assertEquals("ppp", value);
		
		value = Strings.EMPTY;
		value = Strings.padLeft(value, 0, 'p');
		assertEquals("", value);
	
		value = "val";
		value = Strings.padLeft(value, 3, 'p');
		assertEquals("pppval", value);
		
		value = "val";
		value = Strings.padLeft(value, 0, 'p');
		assertEquals("val", value);
	}
	
	@Test
	public void padRightCorrect() {
		String value = Strings.EMPTY;
		value = Strings.padRight(value, 3, 'p');
		assertEquals("ppp", value);
		
		value = Strings.EMPTY;
		value = Strings.padRight(value, 0, 'p');
		assertEquals("", value);
	
		value = "val";
		value = Strings.padRight(value, 3, 'p');
		assertEquals("valppp", value);
		
		value = "val";
		value = Strings.padRight(value, 0, 'p');
		assertEquals("val", value);
	}
	
	
	@Test
	public void padCorrect() {
		String value = Strings.EMPTY;
		value = Strings.pad(value, 3, 'p');
		assertEquals("pppppp", value);
		
		value = Strings.EMPTY;
		value = Strings.pad(value, 0, 'p');
		assertEquals("", value);
	
		value = "val";
		value = Strings.pad(value, 3, 'p');
		assertEquals("pppvalppp", value);
		
		value = "val";
		value = Strings.pad(value, 0, 'p');
		assertEquals("val", value);
	}
}
