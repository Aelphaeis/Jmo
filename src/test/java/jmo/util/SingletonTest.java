package jmo.util;

import static org.junit.Assert.*;

import org.junit.Test;

import jmo.util.Singleton.SingletonInitException;

public class SingletonTest {
	
	@Test
	public void test() {
		assertEquals(1, Singleton.getInstance(MySingleton.class).getValue());
		assertEquals(1, Singleton.getInstance(MySingleton.class).getValue());
	}
	
	@Test(expected=SingletonInitException.class)
	public void test2() {
		assertEquals(1, Singleton.getInstance(PrivCtor.class).getValue());
	}
	
	
	public static class MySingleton {
		private static int integer = 1;
		
		private final int value;
		public MySingleton() {
			this.value = integer;;
			integer++;
		}
		public int getValue() {
			return value;
		}
	}
	

	public static class PrivCtor {
		private static int integer = 1;
		
		private final int value;
		private PrivCtor() {
			this.value = integer;;
			integer++;
		}
		public int getValue() {
			return value;
		}
	}
}
