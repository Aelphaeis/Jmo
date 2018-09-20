package jmo.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import jmo.util.Primitives.Parser;

public class PrimitivesTest {

	@Test
	public void parseArray_intArr_equality() {
		Integer[] arr = new Integer[]{1, 2, 3};
		String result = Arrays.toString(arr);
		assertEquals("[1, 2, 3]", result);

		Parser<Integer> pr = p -> Primitives.parseOrDefault(p, 0);
		Integer[] resultArr = Primitives.parseArray(pr, result, new Integer[0]);
		assertArrayEquals(arr, resultArr);
	}
	
	@Test
	public void parseArray_empty_equality() {
		Integer[] arr = new Integer[]{};
		String result = Arrays.toString(arr);
		assertEquals("[]", result);

		Integer[] resultArr = Primitives.parseArray(null, result, new Integer[0]);
		assertArrayEquals(arr, resultArr);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void parseArray_illegalStringNoCasing_iae() {
		Primitives.parseArray(null, "hi", new Integer[0]);
	}
	@Test(expected=IllegalArgumentException.class)
	public void parseArray_illegalStringNoEndCasing_iae() {
		Primitives.parseArray(null, "[hi", new Integer[0]);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void parseArray_illegalStringNoStartCasing_iae() {
		Primitives.parseArray(null, "hi]", new Integer[0]);
	}
}
