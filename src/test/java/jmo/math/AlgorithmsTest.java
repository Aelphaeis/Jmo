package jmo.math;

import org.junit.Test;

public class AlgorithmsTest {

	@Test
	public void lowestCommonMultiple_2Numbers_FirstNumHighest() {
		System.out.println(Algorithms.primeDecomposition(3));
	}

	@Test
	public void primeComposition__() {
		System.out.println(Algorithms.factors(45));
	}

//	List all pandigital numbers within a range. A pandigital number that has every digit, it cannot start with 0.
//
//	1023456789 is the smallest pandigital.
//	9876543210 is the biggest pandigital.

	public void pandigital(int start, int end) {
		for (int i = start; i < end; i++) {
			int currNum = i;
			if (isPandigital(currNum)) {
				System.out.println(currNum);
			}
		}
	}

	public boolean isPandigital(int i) {
		boolean[] digitArray = new boolean[10];
		if (i % 100000000 == 0) {
			return false;
		}
		for (int j = 0; j < 10; j++) {
			int digit = i % 10;
			digitArray[digit] = true;
			i /= 10;
		}
		for (int k = 0; k < 10; k++) {
			if (digitArray[k] == false) {
				return false;
			}
		}
		return true;
	}
}
