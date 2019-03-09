package jmo.exceptions;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * The class {@code AggregationException} represents that multiple exceptions
 * have occurred.
 * 
 * @author Aelphaeis
 *
 */
public class AggregationException extends Exception {
	private static final long serialVersionUID = 1L;
	private final Collection<Throwable> exceptions;

	/**
	 * Creates an aggregate exception with no exceptions or message. If
	 * {@link AggregationException#getExceptions()} is called, an empty
	 * unmodifiable collection will be returned.
	 */
	public AggregationException() {
		this(null, Collections.emptyList());
	}

	/**
	 * Creates an aggregate exception with a message. If
	 * {@link AggregationException#getExceptions()} is called an empty
	 * unmodifiable collection will be returned.
	 * 
	 * 
	 * @param message
	 * @param ex
	 */
	public AggregationException(String msg, Throwable... ex) {
		this(msg, Arrays.asList(ex));
	}
	/**
	 * Takes a collection of exceptions. The exceptions can be retrieved with
	 * {@link AggregationException#getExceptions()}. if a null collection is
	 * passed, it's replaced with an empty collection
	 * 
	 * @param exceptions
	 */
	public AggregationException(Collection<? extends Throwable> ex) {
		this(null, ex);
	}

	/**
	 * Takes a message and a collection of exceptions. The exceptions can be
	 * retrieved with {@link AggregationException#getExceptions()} If a null
	 * collection is passed, it's replaced with an empty collection.
	 * 
	 * @param message
	 * @param exceptions
	 */
	public AggregationException(String msg, Collection<? extends Throwable> e) {
		super(msg);
		
		if (e == null) {
			this.exceptions = Collections.emptyList();
		} else {
			this.exceptions = Collections.unmodifiableCollection(e);
		}
		
		this.exceptions.forEach(this::addSuppressed);
	}

	/**
	 * Returns an unmodifiable collection that contains the exceptions passed
	 * into this exception. This cannot return null. If no exceptions were
	 * passed into this an empty collection will be returned.
	 * 
	 * @return
	 */
	public Collection<Throwable> getExceptions() {
		return exceptions;
	}

	/**
	 * Returns an unmodifiable collection that contains the exceptions passed
	 * into this exception. This cannot return null. If no exceptions were
	 * passed into this an empty collection will be returned.
	 * 
	 * @return
	 */
	public <T> Collection<T> getExceptions(Class<T> type) {
		return exceptions.stream().map(type::cast)
				.collect(collectingAndThen(toList(), 
						Collections::unmodifiableList));
	}
}
