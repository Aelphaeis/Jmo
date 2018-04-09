package jmo.util;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import jmo.util.Reflector;

public class ReflectorTest {

	@Test
	public void getClassForPackageSuccessTest() {
		ClassLoader loader = Reflector.class.getClassLoader();
		Package pkg = Reflector.class.getPackage();
		assertNotNull(loader);
		assertNotNull(pkg);
		
		List<Class<?>> classes = Reflector.getClassesForPackage(pkg, loader);
		assertTrue(classes.contains(Reflector.class));
	}

}
