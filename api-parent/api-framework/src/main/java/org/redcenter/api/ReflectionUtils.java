package org.redcenter.api;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtils {
	private static final Map<Class<?>, Class<?>> WRAPPER_TYPES_MAP = getWrapperMap();

	public static boolean isWrapperType(Class<?> clazz) {
		return WRAPPER_TYPES_MAP.values().contains(clazz);
	}

	private static Map<Class<?>, Class<?>> getWrapperMap() {
		Map<Class<?>, Class<?>> map = new HashMap<>();
		map.put(boolean.class, Boolean.class);
		map.put(char.class, Character.class);
		map.put(byte.class, Byte.class);
		map.put(short.class, Short.class);
		map.put(int.class, Integer.class);
		map.put(long.class, Long.class);
		map.put(float.class, Float.class);
		map.put(double.class, Double.class);
		// map.put(void.class, Void.class);
		return map;
	}

	public static Class<?> getWrapperClass(Class<?> clazz) {
		Class<?> wrapperClass = WRAPPER_TYPES_MAP.get(clazz);
		if (wrapperClass == null) {
			return clazz;
		} else {
			return wrapperClass;
		}
	}

	public static <T> T valueOf(Class<T> clazz, String arg) {
		Throwable cause = null;
		T value = null;
		try {
			// set default value 0 for null value with number type
			if ((arg == null || arg.isEmpty())
					&& Number.class.isAssignableFrom(clazz)) {
				arg = "0";
			}

			value = clazz.cast(clazz.getDeclaredMethod("valueOf", String.class)
					.invoke(null, arg));
		} catch (NoSuchMethodException e) {
			cause = e;
		} catch (IllegalAccessException e) {
			cause = e;
		} catch (InvocationTargetException e) {
			cause = e.getTargetException();
		}
		if (cause == null) {
			return value;
		} else {
			throw new IllegalArgumentException(cause);
		}
	}
}
