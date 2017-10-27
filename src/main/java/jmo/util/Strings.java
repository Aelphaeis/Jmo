package jmo.util;

public class Strings {
	
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
	
	public static boolean isBlank(String value){
		if(value == null || value.trim().isEmpty()){
			return true;
		}
		return false;
	}
}
