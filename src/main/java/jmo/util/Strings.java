package jmo.util;

import java.util.ArrayList;
import java.util.Objects;
import java.util.StringTokenizer;

public final class Strings {

	public static final String EMPTY = "";

	public static String padRight(String s, int n, char padding) {
		StringBuilder builder = new StringBuilder(s.length() + n);
		builder.append(s);
		for (int i = 0; i < n; i++) {
			builder.append(padding);
		}
		return builder.toString();
	}

	public static String padLeft(String s, int n, char padding) {
		StringBuilder builder = new StringBuilder(s.length() + n);
		for (int i = 0; i < n; i++) {
			builder.append(Character.toString(padding));
		}
		return builder.append(s).toString();
	}

	public static String pad(String s, int n, char padding) {
		StringBuilder pad = new StringBuilder(s.length() + n * 2);
		StringBuilder value = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			pad.append(padding);
		}
		return value.append(pad).append(s).append(pad).toString();
	}

	public static boolean equalsAny(final CharSequence string,
			final CharSequence... searchStrings) {
		Objects.requireNonNull(string);
		if (searchStrings.length > 0) {
			for (final CharSequence search : searchStrings) {
				if (string.equals(search)) {
					return true;
				}
			}
		}
		return false;
	}

	public static String sequence(String v, int i) {
		return Sequencer.seq(v, i);
		
	}
	
	public static String sequence(String v, Integer i, Integer k) {
		return Sequencer.seq(v, i, k, null);
	}
	
	public static String sequence(String v, Integer i, Integer k, Integer j) {
		return Sequencer.seq(v, i, k, j);
	}

	/**
	 * Checks to see if an a string is null or empty.
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isBlank(String value) {
		return (value == null || value.trim().isEmpty());
	}

	/**
	 * Checks to see if any the strings given as parameters are null or empty.
	 * 
	 * @param args
	 * @return
	 */
	public static boolean isAnyBlank(String value, String... additional) {
		if (isBlank(value)) {
			return true;
		}

		String[] loopable = additional == null ? new String[0] : additional;

		for (String s : loopable) {
			if (isBlank(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks to see if any the strings given as parameters are null or empty.
	 * 
	 * @param args
	 * @return
	 */
	public static boolean isAllBlank(String value, String... additional) {
		if (!isBlank(value)) {
			return false;
		}

		String[] loopable = additional == null ? new String[0] : additional;

		for (String s : loopable) {
			if (!isBlank(s)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Removes specified character from source string
	 * 
	 * @param source
	 *            String that will have character removed
	 * @param c
	 *            character to remove
	 * @return String with all instances of c removed.
	 */
	public static String remove(String source, char c) {
		int i;
		if ((i = source.indexOf('c')) == -1) {
			return source;
		} else {
			StringBuilder builder = new StringBuilder(source.substring(0, i));
			for (int count = i; count < source.length(); count++) {
				char current = source.charAt(count);
				if (current != c) {
					builder.append(current);
				}
			}
			return builder.toString();
		}
	}

	public static Integer parseOrDefault(String input, Integer defaultValue) {
		return parseOrDefault(Integer::parseInt, input, defaultValue);
	}

	public static Double parseOrDefault(String input, Double defaultValue) {
		return parseOrDefault(Double::parseDouble, input, defaultValue);
	}

	public static Byte parseOrDefault(String input, Byte defaultValue) {
		return parseOrDefault(Byte::parseByte, input, defaultValue);
	}

	public static Long parseOrDefault(String input, Long defaultValue) {
		return parseOrDefault(Long::parseLong, input, defaultValue);
	}

	private static <T> T parseOrDefault(Parser<T> p, String s, T defaultValue) {
		try {
			return p.parse(s);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	@FunctionalInterface
	private interface Parser<T> {
		T parse(String value);
	}

	/**
	 * Crack a command line.
	 * 
	 * @param toProcess
	 *            the command line to process.
	 * @return the command line broken into strings. An empty or null toProcess
	 *         parameter results in a zero sized array.
	 */
	public static String[] translateCommandline(String toProcess) {
		return new CommandTranslator(toProcess).translate();
	}

	private static class CommandTranslator {
		enum ParseState {
			NORMAL, SINGLE, DOUBLE;
		}

		final String input;

		ArrayList<String> result = new ArrayList<>();

		StringTokenizer tok;
		StringBuilder curr;
		ParseState state;
		boolean quoted;

		public CommandTranslator(String i) {
			tok = new StringTokenizer(isBlank(i) ? "" : i, "\"' ", true);
			curr = new StringBuilder();
			state = ParseState.NORMAL;
			quoted = false;
			input = i;
		}

		public String[] translate() {
			return isBlank(input) ? new String[0] : parse();
		}

		String[] parse() {

			while (tok.hasMoreTokens()) {
				String next = tok.nextToken();
				switch (state) {
					case SINGLE :
						handleQuote(next, "'");
						break;
					case DOUBLE :
						handleQuote(next, "\"");
						break;
					default :
						handleNoQuote(next);
						break;
				}
			}
			if (quoted || curr.length() != 0) {
				result.add(curr.toString());
			}
			validateResult();

			return result.toArray(new String[result.size()]);
		}

		void handleQuote(String token, String expect) {
			if (expect.equals(token)) {
				quoted = true;
				state = ParseState.NORMAL;
			} else {
				curr.append(token);
			}
		}

		void handleNoQuote(String token) {
			if ("\'".equals(token)) {
				state = ParseState.SINGLE;
			} else if ("\"".equals(token)) {
				state = ParseState.DOUBLE;
			} else if (" ".equals(token)) {
				if (quoted || curr.length() != 0) {
					result.add(curr.toString());
					curr.setLength(0);
				}
			} else {
				curr.append(token);
			}
			quoted = false;
		}

		void validateResult() {
			if (state != ParseState.NORMAL) {
				String err = "unbalance quotes in " + input;
				throw new IllegalArgumentException(err);
			}
		}
	}

	private static class Sequencer {

		public static String seq(String s, int i) {
			int index = resolveRealIndex(s, i);
			if(index > s.length() - 1) {
				return EMPTY;
			}
			else {
				return String.valueOf(s.charAt(index));
			}
		}

		public static String seq(String s, Integer i, Integer k) {
			int end = resolveRealIndex(s, co(k, s.length()));
			int start = resolveRealIndex(s, co(i, 0));
			
			end = end > s.length() ? s.length() : end;
			if(start > s.length() - 1 || start > end) {
				return EMPTY;
			}
			
			return s.substring(start, end);
		}
		
		public static String seq(String s, Integer i, Integer k, Integer j) {
			int skip = Math.abs(co(j, 1));
			String intermediate = seq(s, i, k);
			StringBuilder builder = new StringBuilder();
			if (co(j, 0) < 0) {
				for (int c = intermediate.length() - 1; c >= 0; c -= skip) {
					builder.append(intermediate.charAt(c));
				}
			} else {
				for (int c = 0; c < intermediate.length(); c += skip) {
					builder.append(intermediate.charAt(c));
				}
			}
			return builder.toString();
		}

		private static int resolveRealIndex(String s, int index) {
			int abs = Math.abs(index);
			int last = s.length();
			if (index < 0) {
				if (abs < last) {
					return (last) - abs;
				}
				return abs;
			} else {
				return index;
			}
		}

		/**
		 * Coalesce
		 * 
		 * @param values
		 * @return
		 */
		@SafeVarargs
		public static <T> T co(T... values) {
			for (T value : values) {
				if (value != null) {
					return value;
				}
			}
			String err = "no non null value found";
			throw new IllegalArgumentException(err);
		}
	}

	private Strings() {
	}
}
