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

	public static long greatestCommonDivisor(long ... numbers) {
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

	public static long[] factors(long num) {
		return LongStream.range(1, num + 1).filter(p -> num % p == 0).toArray();
	}
}
