package jmo.util;

public class Strings {
	public static String padRight(String s, int n, char padding){
		String pad = "";
		for(int i = 0; i < n; i++){
			pad += padding;
		}
		return  s + pad;
		
	}
	public static String padLeft(String s, int n,  char padding) {
		String pad = "";
		for(int i = 0; i < n; i++){
			pad += padding;
		}
		return pad + s;
	}
	
	public static String pad(String s, int n, char padding){
		return padLeft(padRight(s,n, padding),n,padding);
	}
	
	public static boolean isBlank(String value){
		if(value == null){
			return true;
		}
		
		if(value.trim().isEmpty()){
			return true;
		}
		
		return false;
	}
}
