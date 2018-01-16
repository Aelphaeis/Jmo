package jmo.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Exceptions {
	
	/**
	 * <p>Given an exception and a type, searches the nested exceptions of 
	 * the specified exception type and finds the first instance of an 
	 * exception that is assignable to the type specified.</p>
	 * 
	 * <p>If no exception matches the criteria, returns null.</p>
	 * 
	 * @param from
	 * @param type
	 */
	public static <T extends Throwable> T getCauseInstance (
			Throwable from, Class<T> type){
		return getCauseInstance(from, type, false);
	}
	
	/**
	 * 
	 * <p>Given an exception and a type, searches the nested exceptions of 
	 * the specified exception and finds the first instance of an exception
	 * that is assignable to the type specified.</p>
	 * 
	 * <p>If strict is true, it will only return the exact class specified
	 * sub types of that class will be ignored.</p>
	 * 
	 * <p>If no exception matches the criteria, returns null.</p>
	 * 
	 * @param from Throwable who's causes we want to check
	 * @param type Type of exception to return
	 * @param strict only return the exact class specified (no sub types)
	 */
	public static <T extends Throwable> T getCauseInstance (
			Throwable from, Class<T> type, boolean strict){
		List<T> throwables = getCauseInstances(from, type, strict);
		if(throwables.isEmpty()) {
			return null;
		}
		else {
			return throwables.get(throwables.size() - 1);
		}
	}
	
	/**
	 * <p>Given an exception and a type, searches the nested exceptions of 
	 * the specified exception and finds all instances of an exception
	 * that is assignable to the type specified and returns a list.</p>
	 * 
	 * @param from Throwable who's causes we want to check
	 * @param type Type of exception to return
	 */
	public static <T extends Throwable> List<T> getCauseInstances(
			Throwable from, Class<T> type){
		return getCauseInstances(from, type, false);
	}

	/**
	 * <p>Given an exception and a type, searches the nested exceptions of 
	 * the specified exception and finds all instances of an exception
	 * that is assignable to the type specified and returns a list.</p>
	 * 
	 * @param from Throwable who's causes we want to check
	 * @param type Type of exception to return
	 * @param strict only return the exact class specified (no sub types)
	 */
	public static <T extends Throwable> List<T> getCauseInstances(
			Throwable from, Class<T> type, boolean strict){
		List<T> throwables = new ArrayList<>();
		Throwable t = from;
		while((t = t.getCause()) != null) {
			if(strict) {
				if(t.getClass() == type) {
					throwables.add(type.cast(t));
				}
			}
			else {
				if(type.isAssignableFrom(t.getClass())){
					throwables.add(type.cast(t));
				}
			}
		}
		return throwables;
	}
	
	/**
	 * Takes a throwable's stack trace and puts it into a string
	 * 
	 * @param t
	 * @return stack trace
	 */
	public static String toString(Throwable t) {
		StringWriter sWriter = new StringWriter();
		PrintWriter pWriter = new PrintWriter(sWriter);
		t.printStackTrace(pWriter);
		return pWriter.toString();
	}
	
	private Exceptions() { }

}
