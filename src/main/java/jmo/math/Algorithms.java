package jmo.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Algorithms {

	public static long lowestCommonMultiple(long... nums) {
		return LongStream.of(nums)
				.mapToObj(Algorithms::primeDecomposition)
				.map(p -> p.stream()
						.collect(Collectors.groupingBy(c -> c,
								Collectors.summingLong(c -> 1L))))
				.reduce(new HashMap<>(), (x, y) -> {
					x.forEach((k, v) -> y.merge(k, v, Math::max));
					return y;
				})
				.entrySet()
				.stream()
				.mapToLong(e -> (long) Math.pow(e.getKey(), e.getValue()))
				.reduce(1, (x, y) -> x * y);
	}

	public static long greatestCommonDivisor(long a, long b) {
		long[] aFactors = factors(a);
		long[] bFactors = factors(b);

		for (int i = aFactors.length - 1; i >= 0; i--) {
			for (int c = bFactors.length - 1; c >= 0; c--) {
				if (aFactors[i] == bFactors[c]) {
					return aFactors[i];
				}
			}
		}
		throw new IllegalStateException();
	}

	public static long greatestCommonDivisor(long... numbers) {
		Map<Long, Long> m = new LinkedHashMap<>();
		for (int i = 0; i < numbers.length; i++) {
			for (long f : factors(numbers[i])) {
				m.computeIfAbsent(f, p -> 0L);
				m.put(f, m.get(f) + 1);
			}
		}

		long largest = 1;
		for (Entry<Long, Long> e : m.entrySet()) {
			if (e.getValue() == numbers.length) {
				largest = e.getKey();
			}
		}
		return largest;
	}

	public static List<Long> primeDecomposition(long num) {
		ArrayList<Long> primes = new ArrayList<>();
		for (int i = 2; i <= num; i++) {
			if (num % i == 0) {
				primes.add(Long.valueOf(i));
				primes.addAll(primeDecomposition(num /= i));
				break;
			}
		}
		return primes;
	}

	public static List<String> longestCommonSubsequences(String a, String b) {
		if (a.length() == 0 || b.length() == 0) {
			return new ArrayList<>();
		}

		char aLast = a.charAt(a.length() - 1);
		char bLast = b.charAt(b.length() - 1);

		String aShort = a.substring(0, a.length() - 1);
		String bShort = b.substring(0, b.length() - 1);

		if (aLast == bLast) {
			List<String> result = longestCommonSubsequences(aShort, bShort);
			if (result.isEmpty()) {
				result.add(String.valueOf(aLast));
				return result;
			}
			else {
				return result.stream()
						.map(p -> p + aLast)
						.collect(Collectors.toList());
			}
		}
		else {
			List<String> l1 = longestCommonSubsequences(a, bShort);
			List<String> l2 = longestCommonSubsequences(aShort, b);
			List<String> result = new ArrayList<>();
			int max = 0;
			for (String s : l1) {
				int len = s.length();
				if (len > max) {
					max = s.length();
					result.clear();
				}
				if (len == max) {
					result.add(s);
				}
			}
			for (String s : l2) {
				int len = s.length();
				if (len > max) {
					max = s.length();
					result.clear();
				}
				if (len == max) {
					result.add(s);
				}
			}
			return result;
		}
	}

	public static long[] factors(long num) {
		return LongStream.range(1, num + 1).filter(p -> num % p == 0).toArray();
	}

	public static List<Integer> digits(long n) {
		List<Integer> d = new ArrayList<>();
		while (n > 0) {
			d.add(Integer.valueOf((int) (n % 10)));
			n /= 10;
		}
		return d;
	}

	public static boolean isPandigital(long num) {
		int count;
		int digits;
		for (count = 0, digits = 1023; num > 0; num /= 10, count++) {
			digits ^= 1 << num % 10;
		}
		return digits == 0 && count == 10;
	}
}
