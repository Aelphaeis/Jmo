package jmo.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ObjFiller {
	
	public static final FillerStrategy<Integer> DEF_INTEGER_FILL_STRATEGY;
	public static final FillerStrategy<String> DEF_STRING_FILL_STRATEGY;
	public static final FillerStrategy<Double> DEF_DOUBLE_FILL_STRATEGY;
	public static final FillerStrategy<Short> DEF_SHORT_FILL_STRATEGY;
	public static final FillerStrategy<Float> DEF_FLOAT_FILL_STRATEGY;
	public static final FillerStrategy<Byte> DEF_BYTE_FILL_STRATEGY;
	public static final FillerStrategy<Long> DEF_LONG_FILL_STRATEGY;
	public static final FillerStrategy<?> DEF_FILL_STRATEGY;
	
	static {
		DEF_INTEGER_FILL_STRATEGY = () -> 0;
		DEF_STRING_FILL_STRATEGY = () -> "";
		DEF_DOUBLE_FILL_STRATEGY = () -> 0D;
		DEF_FLOAT_FILL_STRATEGY = () -> 0F;
		DEF_SHORT_FILL_STRATEGY = () -> 0;
		DEF_LONG_FILL_STRATEGY = () -> 0L;
		DEF_BYTE_FILL_STRATEGY = () -> 0;
		DEF_FILL_STRATEGY = () -> null;
	}
	
	private Map<Class<?>, FillerStrategy<?>> strats;
	
	public ObjFiller() {
		strats = new HashMap<>();
		strats.put(String.class, DEF_STRING_FILL_STRATEGY);
		
		strats.put(int.class, DEF_STRING_FILL_STRATEGY);
		strats.put(Integer.class, DEF_STRING_FILL_STRATEGY);
		
		strats.put(double.class, DEF_DOUBLE_FILL_STRATEGY);
		strats.put(Double.class, DEF_DOUBLE_FILL_STRATEGY);
		
		strats.put(float.class, DEF_FLOAT_FILL_STRATEGY);
		strats.put(Float.class, DEF_FLOAT_FILL_STRATEGY);
		
		strats.put(short.class, DEF_SHORT_FILL_STRATEGY);
		strats.put(Short.class, DEF_SHORT_FILL_STRATEGY);
		
		strats.put(long.class, DEF_LONG_FILL_STRATEGY);
		strats.put(Long.class, DEF_LONG_FILL_STRATEGY);
		
		strats.put(byte.class, DEF_BYTE_FILL_STRATEGY);
		strats.put(Byte.class, DEF_BYTE_FILL_STRATEGY);
	}

	public void fill(Object o) {
		try {
			BeanInfo info = Introspector.getBeanInfo(o.getClass());
			for(PropertyDescriptor d : info.getPropertyDescriptors()) {
				FillerStrategy<?> s = strats.get(d.getPropertyType());
				if(s != null) {
					set(s, d, o);
				}
			}
		}
		catch(NullPointerException e) {
			throw e;
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public <T> void addStrategy(Class<T> clz, FillerStrategy<T> strat){
		strats.put(clz, strat);
	}
	
	@SuppressWarnings("unchecked")
	public <T> FillerStrategy<T> getFillerStrategy(Class<?> cls){
		return (FillerStrategy<T>) strats.getOrDefault(cls, DEF_FILL_STRATEGY);
	}
	
	private static void set(FillerStrategy<?> s, PropertyDescriptor pd, Object o)
			throws IllegalAccessException, InvocationTargetException {
		Object value = s.getFillValue();
		Method writeMethod = pd.getWriteMethod();
		if(writeMethod != null) {
			writeMethod.invoke(o, value);
		}
	}
	
	@FunctionalInterface
	public interface FillerStrategy<T>{
		T getFillValue();
	}
}

