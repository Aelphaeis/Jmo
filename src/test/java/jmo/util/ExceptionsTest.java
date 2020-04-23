package jmo.util;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jmo.util.Exceptions;

public class ExceptionsTest {
	
	public static List<Throwable> getThrowables(Throwable t) {
		List<Throwable> throwables = new ArrayList<>();
		for (Throwable curr = t; curr != null; curr = curr.getCause()) {
			throwables.add(curr);
		}
		return throwables;
	}

	public static String getFirstLine(Throwable t) {
		return toString(t).split("\n")[0];
	}

	public static String toString(Throwable t) {
		StringWriter sWriter = new StringWriter();
		PrintWriter pWriter = new PrintWriter(sWriter);
		t.printStackTrace(pWriter);
		return sWriter.toString();
	}

	@Test
	public void Exceptions_getCauseInstances_ExceptionExistsInStack() {
		Exception npe = new NullPointerException();
		Exception ise = new IllegalStateException(npe);
		Exception iae = new IllegalArgumentException(ise);
		
		List<? extends Throwable> r = Exceptions
				.getCauseInstances(iae, IllegalStateException.class);
		
		assertEquals(1, r.size());
	}
	
	@Test
	public void Exceptions_getCauseInstances_ExceptionDoesNotExistsInStack() {
		Exception npe = new NullPointerException();
		Exception ise = new IllegalStateException(npe);
		Exception iae = new IllegalArgumentException(ise);
		
		List<? extends Throwable> r = Exceptions
				.getCauseInstances(iae, UnsupportedOperationException.class);
		
		assertEquals(0, r.size());
	}
	
	@Test
	public void Exceptions_getCauseInstancesStrict_ExceptionExistsInStack() {
		Exception npe = new NullPointerException();
		Exception e = new Exception(npe);
		Exception ise = new IllegalStateException(e);
		Exception iae = new IllegalArgumentException(ise);
		
		List<? extends Throwable> r = Exceptions
				.getCauseInstances(iae, Exception.class, true);
		
		assertEquals(1, r.size());
	}
	
	@Test
	public void Exceptions_getCauseInstance_Exception() {
		Exception npe = new NullPointerException();
		Exception ise = new IllegalStateException(npe);
		Exception iae = new IllegalArgumentException(ise);
		
		assertNull(Exceptions.getCauseInstance(iae, 
				UnsupportedOperationException.class));
	}
	
	@Test
	public void Exceptions_getCauseInstanceStrict_Exception() {
		Exception npe = new NullPointerException();
		Exception e = new Exception(npe);
		Exception ise = new IllegalStateException(e);
		Exception iae = new IllegalArgumentException(ise);
		
		assertEquals(e, Exceptions
				.getCauseInstance(iae, Exception.class, true));
		
	}
}
