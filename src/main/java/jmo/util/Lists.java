package jmo.util;

import java.util.ArrayList;
import java.util.List;

public class Lists {
	public static <T> List<T> seq(List<T> v, int i) {
		int index = resolveIndex(v, i);
		return seq(v, index, index + 1);
	}

	public static <T> List<T> seq(List<T> v, Integer i, Integer k) {
		int end = resolveIndex(v, co(k, v.size()));
		int start = resolveIndex(v, co(i, 0));

		end = end > v.size() ? v.size() : end;
		if (start > v.size() - 1 || start > end) {
			return new ArrayList<>();
		}
		return v.subList(start, end);
	}

	public static <T> List<T> seq(List<T> v, Integer i, Integer k, Integer j) {
		int skip = Math.abs(co(j, 1));
		List<T> temp = seq(v, i, k);
		List<T> list = new ArrayList<>();
		if (co(j, 0) < 0) {
			for (int c = temp.size() - 1; c >= 0; c -= skip) {
				list.add(temp.get(c));
			}
		} else {
			for (int c = 0; c < temp.size(); c += skip) {
				list.add(temp.get(c));
			}
		}
		return list;
	}

	private static <T> int resolveIndex(List<T> v, int i) {
		int abs = Math.abs(i);
		int last = v.size();
		if (i < 0) {
			if (abs < last) {
				return (last) - abs;
			}
			return abs;
		} else {
			return i;
		}
	}

	private static int co(Integer... values) {
		for (Integer value : values) {
			if (value != null) {
				return value;
			}
		}
		String err = "no non null value found";
		throw new IllegalArgumentException(err);
	}

	private Lists() {
		throw new IllegalStateException("Utility class");
	}
}
