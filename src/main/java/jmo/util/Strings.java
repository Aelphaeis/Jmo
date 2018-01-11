package jmo.util;

public final class Strings {
	
	public static final String EMPTY = "";
	
	public static String padRight(String s, int n, char padding){
		StringBuilder builder = new StringBuilder(s);
		for(int i = 0; i < n; i++){
			builder.append(padding);
		}
		return builder.toString();
	}
	
	public static String padLeft(String s, int n,  char padding) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < n; i++){
			builder.append(Character.toString(padding));
		}
		return builder.append(s).toString();
	}
	
	public static String pad(String s, int n, char padding){
		StringBuilder pad = new StringBuilder();
		StringBuilder value = new StringBuilder();
		for(int i = 0; i < n; i++){
			pad.append(Character.toString(padding));
		}
		return value.append(pad).append(s).append(pad).toString();
	}
	
	
	/**
	 * Checks to see if an a string is null or empty.
	 * @param value
	 * @return
	 */
	public static boolean isBlank(String value){
		return (value == null || value.trim().isEmpty());
	}
	
	/**
	 * Checks to see if any the strings given as parameters are 
	 * null or empty.
	 * @param args
	 * @return
	 */
	public static boolean isAnyBlank(String value, String ...additional) {
		if(isBlank(value)) {
			return true;
		}
		
		String[] arrToIterate = additional == null ? new String[0]: additional;
		
		for(String s : arrToIterate) {
			if(isBlank(s)) {
				return true; 
			}
		}
		return false;
	}
	

	/**
	 * Checks to see if any the strings given as parameters are 
	 * null or empty.
	 * @param args
	 * @return
	 */
	public static boolean isAllBlank(String value, String ...additional) {
		if(!isBlank(value)) {
			return false;
		}
		
		String[] arrToIterate = additional == null ? new String[0]: additional;
		
		for(String s : arrToIterate) {
			if(!isBlank(s)) {
				return false; 
			}
		}
		return true;
	}
	
	/**
	 * Removes specified character from source string
	 * @param source String that will have character removed
	 * @param c character to remove
	 * @return String with all instances of c removed.
	 */
	public static String remove(String source, char c) {
		int index;
		if((index = source.indexOf('c')) == -1)
			return source;
		else {
			StringBuilder builder = new StringBuilder(source.substring(0, index));
			for(int i = index; i < source.length(); i++) {
				char current = source.charAt(i);
				if(current != c) {
					builder.append(current);
				}
			}
			return builder.toString();
		}
	}
	
	private Strings() { }

}
