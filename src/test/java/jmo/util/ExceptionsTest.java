package jmo.util;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import jmo.util.Exceptions;

public class ExceptionsTest {
	
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
