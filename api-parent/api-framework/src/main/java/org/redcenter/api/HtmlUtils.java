package org.redcenter.api;

public class HtmlUtils {

//	public static void main(String[] s){
//		System.out.println(getInputType(Double.class));
//	}
	
	public static String getInputType(Class<?> typeClass) {
		typeClass = ReflectionUtils.getWrapperClass(typeClass);		
		if (String.class == typeClass) {
			return "text";
		} else if (Number.class.isAssignableFrom(typeClass)) {
			return "number";
		} else {
			return typeClass.getSimpleName();
		}
	}
}
