package org.redcenter.api;

import java.util.Date;

public class HtmlUtils {

	public static String getHtmlType(Class<?> typeClass) {
		typeClass = ReflectionUtils.getWrapperClass(typeClass);		
		if (String.class == typeClass) {
			return "text";
		} else if (Number.class.isAssignableFrom(typeClass)) {
			return "number";
		} else if (Date.class.isAssignableFrom(typeClass)) {
			return "date";
		} else {
			return typeClass.getSimpleName();
		}
	}
}
