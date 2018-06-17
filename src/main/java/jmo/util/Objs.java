package jmo.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Objs {

	public static <T> boolean equalGetterValues(T a, T b)
			throws IntrospectionException {
		try {
			if (a == b) {
				return true;
			} else if (a != null && b != null) {
				return equalGetterValuesInternal(a, b);
			} else {
				return false;
			}

		} catch (IntrospectionException e) {
			// Only possible if A is not subclass of Object. Think about it...
			throw new IllegalStateException(e);
		}
	}
	/**
	 * Checks to see if getters of objects return equal values.
	 * 
	 * @param a
	 *            must not be null
	 * @param b
	 *            must not be null
	 * @return true or false?
	 * @throws IntrospectionException
	 *             if type is not a subclass of object.
	 */
	protected static <T> boolean equalGetterValuesInternal(T a, T b)
			throws IntrospectionException {

		Object nullObj = new Object();
		BeanInfo info = Introspector.getBeanInfo(a.getClass(), Object.class);
		for (PropertyDescriptor descriptors : info.getPropertyDescriptors()) {
			Method readMethod = descriptors.getReadMethod();
			Object valA = readMethod(readMethod, a, nullObj);
			Object valB = readMethod(readMethod, b, nullObj);

			if (!valA.equals(valB)) {
				return false;
			}
		}
		return true;
	}
	protected static Object readMethod(Method m, Object t, Object def) {
		try {
			if (m != null) {
				Object value = m.invoke(t);
				return value == null ? def : value;
			}
			return def;
		} catch (IllegalAccessException e) {
			//This doesn't really happen but assume equal if it does.
			return def;
		} catch (InvocationTargetException e) {
			// To be honest, you deserve runtime exceptions if you make it here.
			String err = "Read methods shouldn't throw exceptions. Check Args";
			throw new IllegalArgumentException(err);
		}
	}

	/**
	 * Coalesce
	 * 
	 * @param values
	 * @return
	 */
	@SafeVarargs
	public static <T> T co(T... values) {
		for (T value : values) {
			if (value != null) {
				return value;
			}
		}
		return null;
	}

	private Objs() {
		// utility class
	}

}
