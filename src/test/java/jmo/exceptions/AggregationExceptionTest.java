package jmo.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class AggregationExceptionTest {
	
	@Test
	public void ctor_empty_noException() {
		assertNotNull(new AggregationException());
	}

	@Test
	public void ctor_msg_noException() {
		assertNotNull(new AggregationException("msg"));
	}

	@Test
	public void ctor_exceptionList_init() {
		assertNotNull(new AggregationException("msg", new Exception()));
	}

	@Test
	public void ctor_nullList_init() {
		List<Exception> list = null;
		assertNotNull(new AggregationException(list));
	}

	@Test
	public void getExceptions_noExceptions_emptyList() {
		List<Exception> list = null;
		assertEquals(0, new AggregationException(list).getExceptions().size());
	}

	@Test
	public void getExceptions_npe_npeReturned() {
		List<Exception> list = Arrays.asList(new NullPointerException());
		AggregationException ae = new AggregationException(list);
		assertEquals(1, ae.getExceptions(NullPointerException.class).size());
	}
}
