package jmo.util;

import java.util.HashMap;
import java.util.Map;

//TODO Document warning suppressions 
//TODO Write test cases for this class
public class Singleton {

	private static final Singleton instance = new Singleton();

	private Map<Class<?>, Object> mapHolder = new HashMap<>();

	private Singleton() {
	}

	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<T> classOf)
			throws InstantiationException, IllegalAccessException {

		T obj = null;
		synchronized (instance) {
			if (!instance.mapHolder.containsKey(classOf)) {
				obj = classOf.newInstance();
				instance.mapHolder.put(classOf, obj);
			}
			obj = (T) instance.mapHolder.get(classOf);
			return obj;
		}
	}
}
