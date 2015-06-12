package jmo.util;

import java.util.HashMap;
import java.util.Map;

//TODO Document warning suppressions 
//TODO Write test cases for this class
public class Singleton {

	private static final Singleton instance = new Singleton();

	@SuppressWarnings("rawtypes")
	private Map<Class, Object> mapHolder = new HashMap<Class, Object>();

	private Singleton() {
	}

	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<T> classOf)
			throws InstantiationException, IllegalAccessException {

		synchronized (instance) {

			if (!instance.mapHolder.containsKey(classOf)) {
				T obj = classOf.newInstance();
				instance.mapHolder.put(classOf, obj);
			}
			return (T) instance.mapHolder.get(classOf);
		}
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}
