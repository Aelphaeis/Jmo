package jmo.util.naming;

import javax.naming.Name;
import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;

public class MemoryContextTest {

	private MemoryContext context;

	@Before
	public void setup() {
		context = new MemoryContext();
	}

	@Test(expected = NullPointerException.class)
	public void loopup_nullString_exception() throws NamingException {
		context.lookup((String) null);
	}

	@Test(expected = NullPointerException.class)
	public void loopup_nullName_exception() throws NamingException {
		context.lookup((Name) null);
	}
}
