package jmo.util;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import jmo.util.Reflector;

public class ReflectorTest {

	@Test
	public void getClassForPackageSuccessTest() {
		Package pkg = Reflector.class.getPackage();
		assertNotNull(pkg);
		
		List<Class<?>> classes = Reflector.getClassesForPackage(pkg);
		assertTrue(classes.contains(Reflector.class));
	}

}
