package jmo.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Singleton {

	private static final Singleton instance = new Singleton();

	private Map<Class<?>, Object> mapHolder = new HashMap<>();

	private Singleton() {
	}

	public static <T> T getInstance(Class<T> c){
		synchronized (instance) {
			Map<Class<?>, Object> m = instance.mapHolder;
			return c.cast(m.computeIfAbsent(c, new SingletonInit<>()));
		}
	}
	
	static class SingletonInit<T> implements Function<Class<T>, Object> {
		@Override
		public Object apply(Class<T> t) {
			try {
				return t.newInstance();
			}
			catch(InstantiationException | IllegalAccessException e) {
				String err = "Unable to create singleton";
				throw new SingletonInitException(err, e);
			}
		}
	}
	
	public static class SingletonInitException extends RuntimeException{

		private static final long serialVersionUID = 1L;

		public SingletonInitException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
