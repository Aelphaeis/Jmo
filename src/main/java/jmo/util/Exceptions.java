package jmo.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Exceptions {

	public static <T extends Throwable> boolean hasInstance(Throwable t,
			Class<T> type) {
		return hasInstance(t, type, false);
	}

	public static <T extends Throwable> boolean hasInstance(Throwable t,
			Class<T> type, boolean strict) {
		return !getInstances(t, type, strict).isEmpty();
	}

	/**
	 * <p>
	 * Given an exception and a type, searches the the exceptions (inclusive of
	 * the one passed in) of the specified exception and finds all instances of
	 * an exception that is assignable to the type specified and returns a list.
	 * </p>
	 * 
	 * <p>
	 * The order of the list is from earliest occurrence in the trace to last
	 * occurrence in the trace
	 * </p>
	 * 
	 * @param from Throwable who's causes we want to check
	 * @param type Type of exception to return
	 */
	public static <T extends Throwable> List<T> getInstances(Throwable t,
			Class<T> type) {
		return getInstances(t, type, false);
	}

	/**
	 * <p>
	 * Given an exception and a type, searches the exceptions (inclusive of the
	 * one passed in) of the specified exception and finds all instances of an
	 * exception that is assignable to the type specified and returns a list.
	 * </p>
	 * 
	 * <p>
	 * The order of the list is from earliest occurrence in the trace to last
	 * occurrence in the trace
	 * </p>
	 * 
	 * @param from   Throwable who we want to check
	 * @param type   Type of exception to return
	 * @param strict only return the exact class specified (no sub types)
	 */
	public static <T extends Throwable> List<T> getInstances(Throwable t,
			Class<T> type, boolean strict) {
		if (t == null) {
			return Collections.emptyList();
		}
		List<T> throwables = new ArrayList<>();
		Throwable throwable = t;
		do {
			if (strict) {
				if (throwable.getClass() == type) {

					throwables.add(type.cast(throwable));
				}
			}
			else {
				if (type.isAssignableFrom(throwable.getClass())) {
					throwables.add(type.cast(throwable));
				}
			}
			throwable = throwable.getCause();
		} while (throwable != null);
		return throwables;
	}

	/**
	 * <p>
	 * Given an exception and a type, searches the nested exceptions of the
	 * specified exception type and finds the first instance of an exception
	 * that is assignable to the type specified.
	 * </p>
	 * 
	 * <p>
	 * If no exception matches the criteria, returns null.
	 * </p>
	 * 
	 * @param from
	 * @param type
	 */
	public static <T extends Throwable> T getCauseInstance(Throwable from,
			Class<T> type) {
		return getCauseInstance(from, type, false);
	}

	/**
	 * 
	 * <p>
	 * Given an exception and a type, searches the nested exceptions of the
	 * specified exception and finds the first instance of an exception that is
	 * assignable to the type specified.
	 * </p>
	 * 
	 * <p>
	 * If strict is true, it will only return the exact class specified sub
	 * types of that class will be ignored.
	 * </p>
	 * 
	 * <p>
	 * If no exception matches the criteria, returns null.
	 * </p>
	 * 
	 * @param from   Throwable who's causes we want to check
	 * @param type   Type of exception to return
	 * @param strict only return the exact class specified (no sub types)
	 */
	public static <T extends Throwable> T getCauseInstance(Throwable from,
			Class<T> type, boolean strict) {
		List<T> throwables = getCauseInstances(from, type, strict);
		if (throwables.isEmpty()) {
			return null;
		}
		else {
			return throwables.get(throwables.size() - 1);
		}
	}

	/**
	 * <p>
	 * Given an exception and a type, searches the nested exceptions of the
	 * specified exception and finds all instances of an exception that is
	 * assignable to the type specified and returns a list.
	 * </p>
	 * 
	 * @param from Throwable who's causes we want to check
	 * @param type Type of exception to return
	 */
	public static <T extends Throwable> List<T> getCauseInstances(
			Throwable from, Class<T> type) {
		return getCauseInstances(from, type, false);
	}

	/**
	 * <p>
	 * Given an exception and a type, searches the nested exceptions of the
	 * specified exception and finds all instances of an exception that is
	 * assignable to the type specified and returns a list.
	 * </p>
	 * 
	 * @param from   Throwable who's causes we want to check
	 * @param type   Type of exception to return
	 * @param strict only return the exact class specified (no sub types)
	 */
	public static <T extends Throwable> List<T> getCauseInstances(
			Throwable from, Class<T> type, boolean strict) {
		return getInstances(from.getCause(), type, strict);
	}

	/**
	 * Get list of nested exceptions. The first exception is the outer must
	 * exception and the last exception is the innermost exception
	 * 
	 * @param t
	 * @return
	 */
	public static List<Throwable> getThrowables(Throwable t) {
		List<Throwable> throwables = new ArrayList<>();
		for (Throwable curr = t; curr != null; curr = curr.getCause()) {
			throwables.add(curr);
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
		return sWriter.toString();
	}

	@FunctionalInterface
	public static interface CheckedSupplier<T> {

		public static <T> Supplier<T> uncheck(CheckedSupplier<T> s) {
			return () -> {
				try {
					return s.get();
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			};
		}

		public T get() throws Exception;
	}

	/**
	 * Represents a function that accepts one argument and produces a result.
	 *
	 *
	 * @param <T> the type of the input to the function
	 * @param <R> the type of the result of the function
	 */
	@FunctionalInterface
	public static interface CheckedFunction<T, R> {

		/**
		 * <p>
		 * Takes a function wraps it in another function.
		 * 
		 * The wrapping function catches all sub types of exception and wraps
		 * them in an exception of type {@code AutomationException}
		 * </p>
		 * 
		 * @param <T>     the input argument
		 * @param <R>     The element type of the new stream
		 * @param checked a non-interfering, state-less, function to apply to
		 *                each element of a stream
		 * @return the wrapped function
		 * 
		 * @throws RuntimeException if the function passed in throws any
		 *                          exception
		 */
		public static <T, R> Function<T, R> uncheck(CheckedFunction<T, R> f) {
			return t -> {
				try {
					return f.apply(t);
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			};
		}

		public R apply(T t) throws Exception;
	}

	@FunctionalInterface
	public interface CheckedConsumer<T> {

		public static <T> Consumer<T> uncheck(CheckedConsumer<T> consumer) {
			return c -> {
				try {
					consumer.accept(c);
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			};
		}

		/**
		 * Performs this operation on the given argument.
		 *
		 * @param t the input argument
		 */
		void accept(T t) throws Exception;
	}

	private Exceptions() {}
}
