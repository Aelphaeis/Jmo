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
		
		List<Class<?>> classes = Reflector.getPackageClasses(pkg, loader);
		assertTrue(classes.contains(Reflector.class));
	}
	
	@Test
	public void isInstantiable_various_correct() {
		String [] arr = new String[0];
		assertFalse(Reflector.isInstantiable(null));
		assertFalse(Reflector.isInstantiable(String.class));
		assertFalse(Reflector.isInstantiable(Integer.class));
		assertFalse(Reflector.isInstantiable(arr.getClass()));
		assertFalse(Reflector.isInstantiable(TestInterface.class));
		assertFalse(Reflector.isInstantiable(byte.class));
		assertFalse(Reflector.isInstantiable(TestAbstract.class));
		assertTrue(Reflector.isInstantiable(TestConcrete.class));
		assertFalse(Reflector.isInstantiable(Reflector.class));
		assertTrue(Reflector.isInstantiable(TestConcreteCtor.class));
	}
	
	@Test
	public void initParamCtor_validClass_instance() {
		assertNotNull(Reflector.initParamCtor(TestConcreteCtor.class));
	}
	
	@Test
	public void initParamCtor_privateCtor_null() {
		assertNull(Reflector.initParamCtor(Reflector.class));
	}
	

	public static interface TestInterface { 
		//empty for testing
	}
	public static abstract class TestAbstract implements TestInterface { 
		//empty for testing
	}
	public static class TestConcrete extends TestAbstract {
		//empty for testing
	}
	
	public static class TestConcreteCtor extends TestConcrete { 
		public TestConcreteCtor() { }
	}
}
