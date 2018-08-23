package jmo.util;

import static org.junit.Assert.*;
import static java.util.Arrays.asList;
import org.junit.Test;

public class DomainsTest {
	
	@Test
	public void isWhitelisted_wildCard_whitelisted() {
		assertTrue(Domains.isWhitelisted(asList("*"), ""));
	}
	
	@Test
	public void isWhitelisted_exactMatch_whitelisted() {
		assertTrue(Domains.isWhitelisted(asList("jmo.com"),"jmo.com"));
	}
	
	@Test
	public void isWhitelisted_endMatches_blacklisted() {
		assertFalse(Domains.isWhitelisted(asList("jmo.com"),"a.jmo.com"));
	}

	@Test
	public void isWhitelisted_tooShort_blacklisted() {
		assertFalse(Domains.isWhitelisted(asList("jmo.com"),"com"));
	}
	
	@Test
	public void isWhitelisted_notMatching_blacklisted() {
		assertFalse(Domains.isWhitelisted(asList("com.jmo"),"com"));
	}
	
	@Test
	public void isValidDomain_basic_valid() {
		assertTrue(Domains.isValidDomain("jmo.com"));
	}
	
	@Test
	public void isValidDomain_dots_invalid() {
		assertFalse(Domains.isValidDomain("..."));
	}
}
