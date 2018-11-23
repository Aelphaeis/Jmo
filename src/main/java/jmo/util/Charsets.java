package jmo.util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class Charsets {
	private static final String DIGITS = "0123456789";
	private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
	private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	private static final List<Character>  B64_CHARSET;
	
	static {
		String charset = DIGITS + LOWERCASE + UPPERCASE + "+/";
		Character[] boxed = Primitives.box(charset.toCharArray());
		B64_CHARSET = Collections.unmodifiableList(Arrays.asList(boxed));
	}
	
	public static final String UTF8 = "utf-8";

	public static String toUtf8(byte[] content) {
		try {
			return new String(content, UTF8);
		} catch (UnsupportedEncodingException e) {
			// JVM implementation must implement this encoding.
			throw new IllegalStateException(e);
		}
	}

	public static byte[] fromUtf8(String content) {
		try {
			return content.getBytes(UTF8);
		} catch (UnsupportedEncodingException e) {
			// JVM implementation must implement this encoding.
			throw new IllegalStateException(e);
		}
	}

	public static String toB64(byte[] content) {
		return Base64.getEncoder().encodeToString(content);
	}

	public static byte[] fromB64(String content) {
		return Base64.getDecoder().decode(content);
	}
	
	public static boolean isB64(String ctnt) {
		return ctnt.codePoints()
				.mapToObj(p -> Character.valueOf((char)p))
				.anyMatch(p -> !B64_CHARSET.contains(p));
	}
	

	public static String toHex(byte[] content) {
		final char[] hexArray = "0123456789abcdef".toCharArray();
		char[] hexChars = new char[content.length * 2];
		int v;
		for (int j = 0; j < content.length; j++) {
			v = content[j] & 0xFF;
			hexChars[j * 2] = hexArray[v / 16];
			hexChars[j * 2 + 1] = hexArray[v % 16];
		}
		return new String(hexChars);
	}

	public static byte[] fromHex(String hex) {
		   int len = hex.length();
		    byte[] data = new byte[len / 2];
		    for (int i = 0; i < len; i += 2) {
		        data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
		                             + Character.digit(hex.charAt(i+1), 16));
		    }
		    return data;
	}

	private Charsets() {
		// utility class
	}
}
