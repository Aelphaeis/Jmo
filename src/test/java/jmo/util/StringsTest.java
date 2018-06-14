package jmo.util;

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
	
	@Test 
	public void isAnyBlank_emptyString_returnTrue() {
		assertTrue(Strings.isAnyBlank((String)null));
	}
	
	@Test 
	public void isAnyBlank_nonEmptyString_returnFalse() {
		assertFalse(Strings.isAnyBlank("a", "b"));
	}
	
	@Test 
	public void isAnyBlank_secondStringEmpty_returnTrue() {
		assertTrue(Strings.isAnyBlank("a", ""));
	}
	
	@Test 
	public void isAnyBlank_secondStringNull_returnTrue() {
		assertTrue(Strings.isAnyBlank("a", (String)null));
	}
	
	@Test 
	public void isAnyBlank_nullParamArray_returnTrue() {
		assertFalse(Strings.isAnyBlank("a", (String[])null));
	}
	
	@Test 
	public void isAllBlank_singleNonBlankArg_returnFalse() {
		assertFalse(Strings.isAllBlank("a"));
	}
	
	
	@Test 
	public void isAllBlank_singleBlankArg_returnTrue() {
		assertTrue(Strings.isAllBlank(""));
	}
	
	@Test 
	public void isAllBlank_secondArgBlank_returnFalse() {
		assertFalse(Strings.isAllBlank("a", ""));
	}
	
	@Test 
	public void isAllBlank_bothArgBlank_returnTrue() {
		assertTrue(Strings.isAllBlank("", ""));
	}
	
	@Test 
	public void isAllBlank_secondArgNullArr_returnTrue() {
		assertTrue(Strings.isAllBlank("", (String[])null));
	}
	
	@Test 
	public void isAllBlank_firstArgNullSecondArgNotBlank_returnFalse() {
		assertFalse(Strings.isAllBlank(null, "a"));
	}
	
	@Test
	public void remove_emptyString_sameString() {
		String value = "";
		String result = Strings.remove(value, 'c');
		assertEquals("", result);
	}
	
	@Test
	public void remove_chicken_hiken() {
		String value = "chicken";
		String result = Strings.remove(value, 'c');
		assertEquals("hiken", result);
	}
	
	@Test
	public void remove_c_emptyString() {
		String value = "c";
		String result = Strings.remove(value, 'c');
		assertEquals("", result);
	}
	
	@Test
	public void translateCommandline_null_empty() {
		String [] result = Strings.translateCommandline(null);
		assertNotNull(result);
		assertEquals(0, result.length);
	}
	
	@Test
	public void translateCommandline_empty_empty() {
		String [] result = Strings.translateCommandline("");
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	@Test
	public void translateCommandline_nameOnly_nameElement() {
		String [] result = Strings.translateCommandline("name");
		assertNotNull(result);
		assertEquals(1, result.length);
		assertEquals("name", result[0]);
	}
	
	@Test
	public void translateCommandline_twoTokensSingles_Tokenized() {
		String [] result = Strings.translateCommandline("name 'test quote'");
		assertNotNull(result);
		assertEquals(2, result.length);
		assertEquals("name", result[0]);
		assertEquals("test quote", result[1]);
	}
	
	@Test
	public void translateCommandline_twoTokensDoubles_Tokenized() {
		String [] result = Strings.translateCommandline("name \"test quote\"");
		assertNotNull(result);
		assertEquals(2, result.length);
		assertEquals("name", result[0]);
		assertEquals("test quote", result[1]);
	}
	
	@Test
	public void translateCommandline_spaceBeforeQuote_spaceIgnored() {
		String [] result = Strings.translateCommandline(" 'test'");
		assertNotNull(result);
		assertEquals(1, result.length);
		assertEquals("test", result[0]);
	}
	
	@Test
	public void translateCommandline_spaceAfterQuote_spaceIgnored() {
		String [] result = Strings.translateCommandline("'test' ");
		assertNotNull(result);
		assertEquals(1, result.length);
		assertEquals("test", result[0]);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void translateCommandline_singleQuote_exception() {
		Strings.translateCommandline("'");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void translateCommandline_doubleQuote_exception() {
		Strings.translateCommandline("\"");
	}

}

