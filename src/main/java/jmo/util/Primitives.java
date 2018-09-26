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
		return Arrays.asList(ctntArr).stream().map(String::trim).map(p::parse)
				.collect(Collectors.toList()).toArray(out);
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

	public static byte[] unbox(Byte[] content) {
		byte[] result = new byte[content.length];
		for (int i = 0; i < content.length; i++) {
			result[i] = content[i];
		}
		return result;
	}

	public static Byte[] box(byte[] content) {
		Byte[] result = new Byte[content.length];
		for (int i = 0; i < content.length; i++) {
			result[i] = content[i];
		}
		return result;
	}

	public static int[] unbox(Integer[] content) {
		int[] result = new int[content.length];
		for (int i = 0; i < content.length; i++) {
			result[i] = content[i];
		}
		return result;
	}

	public static Integer[] box(int[] content) {
		Integer[] result = new Integer[content.length];
		for (int i = 0; i < content.length; i++) {
			result[i] = content[i];
		}
		return result;
	}
	
	public static double[] unbox(Double[] content) {
		double[] result = new double[content.length];
		for (int i = 0; i < content.length; i++) {
			result[i] = content[i];
		}
		return result;
	}

	public static Double[] box(double[] content) {
		Double[] result = new Double[content.length];
		for (int i = 0; i < content.length; i++) {
			result[i] = content[i];
		}
		return result;
	}
	
	public static float[] unbox(Float[] content) {
		float[] result = new float[content.length];
		for (int i = 0; i < content.length; i++) {
			result[i] = content[i];
		}
		return result;
	}

	public static Float[] box(float[] content) {
		Float[] result = new Float[content.length];
		for (int i = 0; i < content.length; i++) {
			result[i] = content[i];
		}
		return result;
	}

	public static char[] unbox(Character[] content) {
		char[] result = new char[content.length];
		for (int i = 0; i < content.length; i++) {
			result[i] = content[i];
		}
		return result;
	}
	
	public static Character[] box(char[] content) {
		Character[] result = new Character[content.length];
		for (int i = 0; i < content.length; i++) {
			result[i] = content[i];
		}
		return result;
	}
	
	public static short[] unbox(Short[] content) {
		short[] result = new short[content.length];
		for (int i = 0; i < content.length; i++) {
			result[i] = content[i];
		}
		return result;
	}
	
	public static Short[] box(short[] content) {
		Short[] result = new Short[content.length];
		for (int i = 0; i < content.length; i++) {
			result[i] = content[i];
		}
		return result;
	}
	
	public static boolean[] unbox(Boolean[] content) {
		boolean[] result = new boolean[content.length];
		for (int i = 0; i < content.length; i++) {
			result[i] = content[i];
		}
		return result;
	}

	public static Boolean[] box(boolean[] content) {
		Boolean[] result = new Boolean[content.length];
		for (int i = 0; i < content.length; i++) {
			result[i] = content[i];
		}
		return result;
	}

	public static long[] unbox(Long[] content) {
		long[] result = new long[content.length];
		for (int i = 0; i < content.length; i++) {
			result[i] = content[i];
		}
		return result;
	}

	public static Long[] box(long[] content) {
		Long[] result = new Long[content.length];
		for (int i = 0; i < content.length; i++) {
			result[i] = content[i];
		}
		return result;
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
