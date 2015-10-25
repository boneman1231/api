package org.redcenter.api.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Api {

	/**
	 * display name
	 * @return
	 */
	String value() default "";

	/**
	 * type to convert for UI component 
	 * @return
	 */
	Class<?> type() default String.class;

	/**
	 * tooltip
	 * @return
	 */
	String desc() default "";

	/**
	 * only active for parameter annotation
	 * 
	 * @return
	 */
	Option[] options() default {};
}
