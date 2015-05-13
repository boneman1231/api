package org.redcenter.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Api {

	String value() default "";

	// Class<?> type() default String.class;

	String desc() default "";

	/**
	 * only active for parameter annotation
	 * @return
	 */
	Option[] options() default {};
}
