package jmo.util;

import java.util.regex.Pattern;

public class Domains {
	private static final String TLD_RGX = "(\\.[A-Za-z]{2,})";
	public static final String DOMAIN_RGX = "[\\w\\-]+(\\.[\\w\\-]+)*" + TLD_RGX + "$";
	private static final Pattern DOMAIN_PATTERN = Pattern.compile(DOMAIN_RGX);
	private static final String DOMAIN_SPLIT_REGEX = "\\.";
	private static final String WC = "*"; // wildcard

	public static boolean isValidDomain(String domain) {
		return DOMAIN_PATTERN.matcher(domain).matches();
	}

	public static boolean isWhitelisted(Iterable<String> whitelist, String d) {
		for (String entry : whitelist) {
			if (isWhitelisted(entry, d)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isWhitelisted(String entry, String input) {
		String[] wlparts = entry.split(DOMAIN_SPLIT_REGEX);
		String[] parts = input.split(DOMAIN_SPLIT_REGEX);

		reverse(wlparts);
		reverse(parts);

		for (int i = 0; i < wlparts.length; i++) {
			if (wlparts[i].equals(WC)) {
				return true;
			} else if (i == parts.length) {
				return false;
			} else if (!wlparts[i].equalsIgnoreCase(parts[i])) {
				return false;
			}

		}
		return parts.length == wlparts.length;
	}

	private static <T> T[] reverse(T[] objs) {
		for (int i = 0; i < objs.length / 2; i++) {
			T temp = objs[i];
			objs[i] = objs[objs.length - i - 1];
			objs[objs.length - i - 1] = temp;
		}
		return objs;
	}

	private Domains() {
	}
}
