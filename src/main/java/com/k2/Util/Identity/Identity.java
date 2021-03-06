package com.k2.Util.Identity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Identity annotation identifies the field to be used as the classes identity value  which can be used as an alternative to 
 * implementing the Identified interface.
 * @author simon
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Identity {
	
	/**
	 * The value to use if the field value is null
	 * @return	The value to use if the field value is null
	 */
	public String valueIfNull() default "";
	
}
