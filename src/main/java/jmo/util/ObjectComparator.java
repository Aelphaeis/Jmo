package jmo.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ObjectComparator<T> {

	protected static final Checker<Object> CHECKER = new DefaultChecker();
	public static <R> ComparatorBuilder<R> builder(Class<R> type) {
		return new ComparatorBuilder<>(type);
	}
	
	protected static Collection<Method> resolveReadable(Class<?> clazz) {
		List<Method> methods = new ArrayList<>();
		BeanInfo info = resolveBeanInfo(clazz);
		for (PropertyDescriptor desc : info.getPropertyDescriptors()) {
			methods.add(desc.getReadMethod());
		}
		return methods;
	}

	protected static BeanInfo resolveBeanInfo(Class<?> clazz) {
		try {
			return Introspector.getBeanInfo(clazz, Object.class);
		} catch (IntrospectionException e) {
			// Only possible if A is not subclass of Object.
			throw new IllegalArgumentException(e);
		}
	}

	protected static Object readValue(Method m, Object obj) {
		try {
			return m.invoke(obj);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (InvocationTargetException e) {
			// To be honest, you deserve runtime exceptions if you make it here.
			String err = "Read methods shouldn't throw exceptions. Check Args";
			throw new IllegalArgumentException(err, e);
		}
	}
	private Checker<Object> defaultChecker;
	private final Class<T> comparisonClass;
	private final Collection<Method> readMethods;
	private final Map<Class<?>, Checker<?>> equalityOverrides;

	public ObjectComparator(Class<T> clz) {
		this(clz, new HashMap<>());

	}

	public ObjectComparator(Class<T> clz, Map<Class<?>, Checker<?>> override) {
		defaultChecker = CHECKER;
		this.comparisonClass = clz;
		Collection<Method> readable = resolveReadable(clz);
		this.readMethods = Collections.unmodifiableCollection(readable);
		this.equalityOverrides = Collections.unmodifiableMap(override);
	}

	public boolean isPropertiesEqual(T a, T b) {
		if (a == b) {
			return true;
		}

		if (a == null || b == null) {
			return false;
		}

		for (Method m : getReadMethods()) {
			Object propA = readValue(m, a);
			Object propB = readValue(m, b);

			Class<?> type = m.getReturnType();
			Checker<?> checker = equalityOverrides.getOrDefault(type,
					defaultChecker);

			if (!checker.isEqualRaw(propA, propB)) {
				return false;
			}
		}
		return true;
	}
	
	public Checker<?> getChecker(Class<?> type){
		return equalityOverrides.getOrDefault(type, defaultChecker);
	}

	public Class<T> getComparisonClass() {
		return comparisonClass;
	}

	public Collection<Method> getReadMethods() {
		return readMethods;
	}

	public static interface Checker<T> {
		boolean isEqual(T a, T b);
		default boolean isEqualRaw(Object a, Object b) {
			Class<T> cls = getCheckedClass();
			return isEqual(cls.cast(a), cls.cast(b));
		}
		Class<T> getCheckedClass();
	}

	public static class DefaultChecker implements Checker<Object> {

		@Override
		public boolean isEqual(Object a, Object b) {
			return a == b || Objects.nonNull(a) && a.equals(b);
		}

		@Override
		public Class<Object> getCheckedClass() {
			return Object.class;
		}
	}

	public static class ComparatorBuilder<X> {
		private Class<X> cls;
		private Checker<Object> defaultChecker;
		private Map<Class<?>, Checker<?>> checkerOverride;

		public ComparatorBuilder(Class<X> cls) {
			this.cls = cls;
			defaultChecker = CHECKER;
			checkerOverride = new HashMap<>();
		}

		public ComparatorBuilder<X> defaultChecker(Checker<Object> checker) {
			Objects.requireNonNull(checker);
			defaultChecker = checker;
			return this;
		}

		public <Y> ComparatorBuilder<X> override(Checker<Y> checker) {
			Objects.requireNonNull(checker);
			checkerOverride.put(checker.getCheckedClass(), checker);
			return this;
		}

		public ObjectComparator<X> build() {
			ObjectComparator<X> oc = new ObjectComparator<>(cls);
			oc.defaultChecker = defaultChecker;
			return oc;
		}
	}
}
