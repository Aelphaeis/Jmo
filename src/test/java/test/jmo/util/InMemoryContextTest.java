package test.jmo.util;

import static org.junit.Assert.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.Test;

public class InMemoryContextTest {

	@Test
	public void test() throws NamingException {
		InitialContext ic = new InitialContext();
		Context ctx = (Context) ic.lookup("java:comp/env");
	}

}
