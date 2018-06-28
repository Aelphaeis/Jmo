package jmo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NamingConventions {

	/**
	 * Capitalizes the first letter and makes all subsequent letters lower case
	 * <br/>
	 * 
	 * camelCase("TEST"); //Test<br/>
	 * camelCase("test"); //Test<br/>
	 * @param s
	 * @return proper cased string
	 */
	public static String camelCase(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}
	
	/**
	 * Takes a snake case String and turns it into camel case <br/>
	 * 
	 * toCamelCaseFromSnakeCase("hello_world");// HelloWorld
	 * @param s
	 * @return
	 */
	public static String toCamelCaseFromSnakeCase(String s) {
		String[] parts = s.split("_");
		StringBuilder builder = new StringBuilder();
		for (String part : parts) {
			builder.append(camelCase(part));
		}
		return builder.toString();
	}
	
	/**
	 * Takes camel case and turns it into screaming snake case
	 * 
	 * 
	 * @param s
	 * @return
	 */
	public static String toScreamingSnakeCaseFromCamelCase(String s) {
		List<String> content = new ArrayList<>();
		char[] cArr = s.toCharArray();
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < cArr.length; i++) {
			if(Character.isUpperCase(cArr[i])) {
				content.add(builder.toString());
				builder.setLength(0);
			}
			builder.append(Character.toUpperCase(cArr[i]));
		}
		content.remove("");
		content.add(builder.toString());
		return content.stream().collect(Collectors.joining("_"));
	}
	
	public static String toScreamingSnakeCaseFromCamelCase2(String s) {
		char[] cArr = s.toCharArray();
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < cArr.length; i++) {
			if(Character.isUpperCase(cArr[i]) &&  i != 0) {
				builder.append("_");
			}
			builder.append(Character.toUpperCase(cArr[i]));
		}
		return builder.toString();
	}
	
	public static String toScreamingSnakeCaseFromCamelCase3(String s) {
		char[] cArr = s.toCharArray();
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < cArr.length; i++) {
			if(Character.isUpperCase(cArr[i])) {
				builder.append("_");
			}
			builder.append(Character.toUpperCase(cArr[i]));
		}
		if(cArr.length > 0 && Character.isUpperCase(cArr[0])) {
			return builder.substring(1);
		}
		else {
			return builder.toString();
		}
	}
	
	
	public static String toScreamingSnakeCaseFromCamelCase4(String s) {
		StringBuilder builder = new StringBuilder();
		for(char c : s.toCharArray()) {
			if(Character.isUpperCase(c)) {
				builder.append("_");
			}
			builder.append(Character.toUpperCase(c));
		}
		if(s.length() > 0 && Character.isUpperCase(s.charAt(0))) {
			return builder.substring(1);
		}
		else {
			return builder.toString();
		}
	}
	
	private NamingConventions() {
	}
}
