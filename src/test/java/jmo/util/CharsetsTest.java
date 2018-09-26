package jmo.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CharsetsTest {

	private static final byte[] VALID = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
			12, 13, 14, 15};
	@Test
	public void test() {
		assertEquals("000102030405060708090a0b0c0d0e0f", Charsets.toHex(VALID));
	}

}
