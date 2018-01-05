package com.k2.Util;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This utility provides static method for handling Integer values.
 * 
 * @author simon
 *
 */
public class IntegerUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * This map holds instances of Integer TypeConverters indexed by the class that they convert into Integer values
	 */
	private static Map<Class<?>, TypeConverter<?,Integer>> typeConverters = new HashMap<Class<?>, TypeConverter<?,Integer>>();
	/**
	 * This method registers with this static utility an instance of a Integer TypeConverter to convert an instance of the convertible class
	 * into a Integer value
	 * 
	 * @param converter	The Integer TypeConverter instance
	 */
	public static void registerTypeConverter(TypeConverter<?,Integer> converter) {
		Class<?> cls = converter.convertClass();
		typeConverters.put(cls, converter);
	}
	/**
	 * This method converts the given object into a Integer value
	 * 
	 * Static utility methods convert Strings, Integers, Longs, Floats, Doubles, Dates and Booleans into Integer values
	 * If the given object is not one of the above types then a type converter must have been registered with this static utility
	 * in order for the given object to be converted into a Integer value
	 * 
	 * If no type converter has been registered for the class of the given object then a null is returned
	 * 
	 * @param v The object to convert into a Integer value
	 * @return	The Integer representation of the given object or null if the given object is null or the class of the given object
	 * cannot be converted into a Integer value
	 */
	public static Integer toInteger(Object v) {
		if (v==null) { return null; }
		if (String.class.isAssignableFrom(v.getClass())) { return StringUtil.toInteger((String)v); }
		if (Integer.class.isAssignableFrom(v.getClass())) { return (Integer)v; }
		if (Long.class.isAssignableFrom(v.getClass())) { return LongUtil.toInteger((Long)v); }
		if (Float.class.isAssignableFrom(v.getClass())) { return FloatUtil.toInteger((Float)v); }
		if (Double.class.isAssignableFrom(v.getClass())) { return DoubleUtil.toInteger((Double)v); }
		if (Date.class.isAssignableFrom(v.getClass())) { return DateUtil.toInteger((Date)v); }
		if (Boolean.class.isAssignableFrom(v.getClass())) { return BooleanUtil.toInteger((Boolean)v); }
		for (Class<?> cls : typeConverters.keySet()) {
			if (cls.isAssignableFrom(v.getClass())) {
				return typeConverters.get(cls).convert(v);
			}
		}
		return null;		
	}
	/**
	 * This method converts the given Integer into a Long value
	 * @param v The Integer to convert
	 * @return	A Long representing the given Integer
	 */
	public static Long toLong(Integer v) {
		if (v == null) { return null; }
		return new Long(v);
	}
	/**
	 * This method converts the given Integer into a Float value
	 * @param v The Integer to convert
	 * @return	A Float representing the given Integer
	 */
	public static Float toFloat(Integer v) {
		if (v == null) { return null; }
		return new Float(v);
	}
	/**
	 * This method converts the given Integer into a Double value
	 * @param v The Integer to convert
	 * @return	A Double representing the given Integer
	 */
	public static Double toDouble(Integer v) {
		if (v == null) { return null; }
		return new Double(v); 	
	}
	/**
	 * This method converts the given Integer into a Boolean value
	 * @param v The Integer to convert
	 * @return	A Boolean representing the given Integer
	 */
	public static Boolean toBoolean(Integer v) {
		if (v == null) { return null; }
		if (v.equals(BooleanUtil.falseAsInt())) { return false; }
		return true;	
	}
	/**
	 * This method converts the given Integer into a Date value
	 * @param v The Integer to convert
	 * @return	A Date representing the given Integer
	 */
	public static Date toDate(Integer v) {
		if (v == null) { return null; }
		return new Date(v);
	}
	/**
	 * This method converts the given Integer into a String value
	 * @param v The Integer to convert
	 * @return	A String representing the given Integer
	 */
	public static String toString(Integer v) {
		if (v == null) { return null; }
		return v.toString();
	}
	/**
	 * This method returns an unbounded random integer
	 * @return	An unbounded random integer
	 */
	public static Integer random() {
		Random rnd = new Random();
		return rnd.nextInt();
	}
	/**
	 * This method returns a random integer between 0 and the given maximum inclusive
	 * @param max	The maximum random number
	 * @return	A random integer between 0 and max inclusive
	 */
	public static Integer random(Integer max) {
		Random rnd = new Random();
		if (max != null) return rnd.nextInt(max +1);
		return rnd.nextInt();
	}
	/**
	 * This method returns a random integer within the specified range inclusive
	 * @param min	The minimum value for the random integer
	 * @param max	The maximum value for the random integer
	 * @return	A random integer within the given range.
	 */
	public static Integer random(Integer min, Integer max) {
		Random rnd = new Random();
		if (min == null && max == null) return rnd.nextInt();
		if (min == null) return rnd.nextInt(max+1);
		if (max == null) return min + rnd.nextInt();
		return min + rnd.nextInt(max - min +1);
	}
	/**
	 * This method converts null values
	 * @param checkValue		The value to check for null
	 * @param valueIfNull	The value to return if the value to check is null
	 * @return	The checkValue if it is not null else the valueIfNull
	 */
	public static Integer nvl(Integer checkValue, Integer valueIfNull) {
		return (checkValue==null) ? valueIfNull : checkValue;
	}


	

}
