package jmo.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Primitives {

	public static <T> T[] parseArray(Parser<T> p, String in, T[] out) {
		if (!isArrayString(in)) {
			String err = "String is not an output from Arrays.toString";
			throw new IllegalArgumentException(err);
		}

		if (isArrayStringEmpty(in)) {
			return Arrays.asList().toArray(out);
		}

		String content = in.substring(1, in.length() - 1);
		String[] ctntArr = content.split(",");
		return Arrays.asList(ctntArr).stream()
				.map(String::trim)
				.map(p::parse)
				.collect(Collectors.toList())
				.toArray(out);
	}

	public static int parseOrDefault(String input, int defaultValue) {
		return parseOrDefault(Integer::parseInt, input, defaultValue);
	}

	public static double parseOrDefault(String input, double defaultValue) {
		return parseOrDefault(Double::parseDouble, input, defaultValue);
	}

	public static byte parseOrDefault(String input, byte defaultValue) {
		return parseOrDefault(Byte::parseByte, input, defaultValue);
	}

	public static long parseOrDefault(String input, long defaultValue) {
		return parseOrDefault(Long::parseLong, input, defaultValue);
	}

	public static short parseOrDefault(String input, short defaultValue) {
		return parseOrDefault(Short::parseShort, input, defaultValue);
	}

	public static float parseOrDefault(String input, float defaultvalue) {
		return parseOrDefault(Float::parseFloat, input, defaultvalue);
	}

	public static boolean parseOrDefault(String input, boolean defaultvalue) {
		return parseOrDefault(Boolean::parseBoolean, input, defaultvalue);
	}

	public static <T> T parseOrDefault(Parser<T> p, String s, T defaultValue) {
		try {
			return p.parse(s);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	@FunctionalInterface
	public interface Parser<T> {
		T parse(String value);
	}

	private static boolean isArrayStringEmpty(String in) {
		return in.length() == 2;
	}

	private static boolean isArrayString(String in) {
		return in.startsWith("[") && in.endsWith("]");
	}
}
