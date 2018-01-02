package com.k2.Util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This utility provides static method for handling Long values.
 * 
 * @author simon
 *
 */
public class LongUtil {
	
	/**
	 * This map holds instances of Long TypeConverters indexed by the class that they convert into Long values
	 */
	private static Map<Class<?>, TypeConverter<?,Long>> typeConverters = new HashMap<Class<?>, TypeConverter<?,Long>>();
	/**
	 * This method registers with this static utility an instance of a Long TypeConverter to convert an instance of the convertible class
	 * into a Long value
	 * 
	 * @param converter	The Long TypeConverter instance
	 */
	public static void registerTypeConverter(TypeConverter<?,Long> converter) {
		Class<?> cls = converter.convertClass();
		typeConverters.put(cls, converter);
	}
	/**
	 * This method converts the given object into a Long value
	 * 
	 * Static utility methods convert Strings, Integers, Longs, Floats, Doubles, Dates and Booleans into Long values
	 * If the given object is not one of the above types then a type converter must have been registered with this static utility
	 * in order for the given object to be converted into a Long value
	 * 
	 * If no type converter has been registered for the class of the given object then a null is returned
	 * 
	 * @param v The object to convert into a Long value
	 * @return	The Long representation of the given object or null if the given object is null or the class of the given object
	 * cannot be converted into a Long value
	 */
	public static Long toLong(Object v) {
		if (v==null) { return null; }
		if (String.class.isAssignableFrom(v.getClass())) { return StringUtil.toLong((String)v); }
		if (Integer.class.isAssignableFrom(v.getClass())) { return IntegerUtil.toLong((Integer)v); }
		if (Long.class.isAssignableFrom(v.getClass())) { return LongUtil.toLong((Long)v); }
		if (Float.class.isAssignableFrom(v.getClass())) { return FloatUtil.toLong((Float)v); }
		if (Double.class.isAssignableFrom(v.getClass())) { return DoubleUtil.toLong((Double)v); }
		if (Date.class.isAssignableFrom(v.getClass())) { return DateUtil.toLong((Date)v); }
		if (Boolean.class.isAssignableFrom(v.getClass())) { return BooleanUtil.toLong((Boolean)v); }
		for (Class<?> cls : typeConverters.keySet()) {
			if (cls.isAssignableFrom(v.getClass())) {
				return typeConverters.get(cls).convert(v);
			}
		}
		return null;		
	}
	/**
	 * This method converts the given Long into a Integer value
	 * @param v The Long to convert
	 * @return	A Integer representing the given Long
	 */
	public static Integer toInteger(Long v) {
		if (v == null) { return null; }
		return v.intValue();
	}
	/**
	 * This method converts the given Long into a Long value
	 * @param v The Long to convert
	 * @return	A Long representing the given Long
	 */
	public static Long toLong(Long v) {
		return v;
	}
	/**
	 * This method converts the given Long into a Float value
	 * @param v The Long to convert
	 * @return	A Float representing the given Long
	 */
	public static Float toFloat(Long v) {
		if (v == null) { return null; }
		return new Float(v);
	}
	/**
	 * This method converts the given Long into a Double value
	 * @param v The Long to convert
	 * @return	A Double representing the given Long
	 */
	public static Double toDouble(Long v) {
		if (v == null) { return null; }
		return new Double(v); 	
	}
	/**
	 * This method converts the given Long into a Boolean value
	 * @param v The Long to convert
	 * @return	A Boolean representing the given Long
	 */
	public static Boolean toBoolean(Long v) {
		if (v == null) { return null; }
		if (v.equals(new Long(BooleanUtil.falseAsInt()))) { return false; }
		return true;	
	}
	/**
	 * This method converts the given Long into a Date value
	 * @param v The Long to convert
	 * @return	A Date representing the given Long
	 */
	public static Date toDate(Long v) {
		if (v == null) { return null; }
		return new Date(v.longValue());
	}
	/**
	 * This method converts the given Long into a String value
	 * @param v The Long to convert
	 * @return	A String representing the given Long
	 */
	public static String toString(Long v) {
		if (v == null) { return null; }
		return v.toString();
	}
	/**
	 * This method returns an unbounded random long
	 * @return	An unbounded random long
	 */
	public static Long random() {
		Random rnd = new Random();
		return rnd.nextLong();
	}
	/**
	 * This method return a random Long value within the defined range
	 * @param min	The minimum random value
	 * @param max	The maximum random value
	 * @return	A random Long value within the given range.
	 */
	public static Long random(Long min, Long max) {
		if (min == null || max == null) return random();
		Double d = DoubleUtil.random()*(max-min);
		return min+d.longValue();
	}
	/**
	 * This method converts null values
	 * @param checkValue		The value to check for null
	 * @param valueIfNull	The value to return if the value to check is null
	 * @return	The checkValue if it is not null else the valueIfNull
	 */
	public static Long nvl(Long checkValue, Long valueIfNull) {
		return (checkValue==null) ? valueIfNull : checkValue;
	}


	

}
