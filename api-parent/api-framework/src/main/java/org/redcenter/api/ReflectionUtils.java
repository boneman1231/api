package org.redcenter.api;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ReflectionUtils {

	private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_MAP;
	private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_MAP;

	static {
		PRIMITIVE_TO_WRAPPER_MAP = new HashMap<Class<?>, Class<?>>(8);
		PRIMITIVE_TO_WRAPPER_MAP.put(boolean.class, Boolean.class);
		PRIMITIVE_TO_WRAPPER_MAP.put(char.class, Character.class);
		PRIMITIVE_TO_WRAPPER_MAP.put(byte.class, Byte.class);
		PRIMITIVE_TO_WRAPPER_MAP.put(short.class, Short.class);
		PRIMITIVE_TO_WRAPPER_MAP.put(int.class, Integer.class);
		PRIMITIVE_TO_WRAPPER_MAP.put(long.class, Long.class);
		PRIMITIVE_TO_WRAPPER_MAP.put(float.class, Float.class);
		PRIMITIVE_TO_WRAPPER_MAP.put(double.class, Double.class);
		// PRIMITIVE_TO_WRAPPER_MAP.put(void.class, Void.class);

		WRAPPER_TO_PRIMITIVE_MAP = new HashMap<Class<?>, Class<?>>(8);
		Set<Entry<Class<?>, Class<?>>> entries = PRIMITIVE_TO_WRAPPER_MAP
				.entrySet();
		for (Entry<Class<?>, Class<?>> entry : entries) {
			WRAPPER_TO_PRIMITIVE_MAP.put(entry.getValue(), entry.getValue());
		}
	}

	public static boolean isWrapperType(Class<?> clazz) {
		return WRAPPER_TO_PRIMITIVE_MAP.containsKey(clazz);
	}

	public static Class<?> getWrapperClass(Class<?> clazz) {
		Class<?> wrapperClass = PRIMITIVE_TO_WRAPPER_MAP.get(clazz);
		if (wrapperClass == null) {
			return clazz;
		} else {
			return wrapperClass;
		}
	}

	public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
		return ((clazz.isPrimitive()) || (isWrapperType(clazz)));
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
