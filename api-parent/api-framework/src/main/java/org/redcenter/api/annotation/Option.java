package org.redcenter.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Option {
	/**
	 * actual value
	 * @return
	 */
	String value() default "";

	/**
	 * display name
	 * @return
	 */
	String key() default "";
}
