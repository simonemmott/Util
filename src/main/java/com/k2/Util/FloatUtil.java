package com.k2.Util;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This utility provides static method for handling Float values.
 * 
 * @author simon
 *
 */
public class FloatUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	/**
	 * This map holds instances of Float TypeConverters indexed by the class that they convert into Float values
	 */
	private static Map<Class<?>, TypeConverter<?,Float>> typeConverters = new HashMap<Class<?>, TypeConverter<?,Float>>();
	/**
	 * This method registers with this static utility an instance of a Float TypeConverter to convert an instance of the convertible class
	 * into a Float value
	 * 
	 * @param converter	The Float TypeConverter instance
	 */
	public static void registerTypeConverter(TypeConverter<?,Float> converter) {
		Class<?> cls = converter.convertClass();
		typeConverters.put(cls, converter);
	}
	/**
	 * This method converts the given object into a Float value
	 * 
	 * Static utility methods convert Strings, Integers, Longs, Floats, Doubles, Dates and Booleans into Float values
	 * If the given object is not one of the above types then a type converter must have been registered with this static utility
	 * in order for the given object to be converted into a Float value
	 * 
	 * If no type converter has been registered for the class of the given object then a null is returned
	 * 
	 * @param v The object to convert into a Float value
	 * @return	The Float representation of the given object or null if the given object is null or the class of the given object
	 * cannot be converted into a Float value
	 */
	public static Float toFloat(Object v) {
		if (v==null) { return null; }
		if (String.class.isAssignableFrom(v.getClass())) { return StringUtil.toFloat((String)v); }
		if (Integer.class.isAssignableFrom(v.getClass())) { return IntegerUtil.toFloat((Integer)v); }
		if (Long.class.isAssignableFrom(v.getClass())) { return LongUtil.toFloat((Long)v); }
		if (Float.class.isAssignableFrom(v.getClass())) { return (Float)v; }
		if (Double.class.isAssignableFrom(v.getClass())) { return DoubleUtil.toFloat((Double)v); }
		if (Date.class.isAssignableFrom(v.getClass())) { return DateUtil.toFloat((Date)v); }
		if (Boolean.class.isAssignableFrom(v.getClass())) { return BooleanUtil.toFloat((Boolean)v); }
		for (Class<?> cls : typeConverters.keySet()) {
			if (cls.isAssignableFrom(v.getClass())) {
				return typeConverters.get(cls).convert(v);
			}
		}
		return null;		
	}
	/**
	 * This method converts the given Float into an Integer
	 * @param v	The float to convert
	 * @return	An Integer representing the given Float
	 */
	public static Integer toInteger(Float v) {
		if (v == null) { return null; }
		return v.intValue();
	}
	/**
	 * This method converts the given Float into a Long
	 * @param v	The float to convert
	 * @return	A Long representing the given Float
	 */
	public static Long toLong(Float v) {
		if (v == null) { return null; }
		return v.longValue();
	}
	/**
	 * This method converts the given Float into a FLoat
	 * @param v	The float to convert
	 * @return	A Float representing the given Float
	 */
	public static Float toFloat(Float v) {
		if (v == null) { return null; }
		return v;
	}
	/**
	 * This method converts the given Float into a Double
	 * @param v	The float to convert
	 * @return	A Double representing the given Float
	 */
	public static Double toDouble(Float v) {
		if (v == null) { return null; }
		return new Double(v); 	
	}
	/**
	 * This method converts the given Float into a Boolean
	 * @param v	The float to convert
	 * @return	A Boolean representing the given Float
	 */
	public static Boolean toBoolean(Float v) {
		if (v == null) { return null; }
		if (v.equals(new Float(BooleanUtil.falseAsInt()))) { return false; }
		return true;	
	}
	/**
	 * This method converts the given Float into a Date
	 * @param v	The float to convert
	 * @return	A Date representing the given Float
	 */
	public static Date toDate(Float v) {
		if (v == null) { return null; }
		return new Date(v.longValue());
	}
	/**
	 * This method converts the given Float into a String
	 * @param v	The float to convert
	 * @return	A String representing the given Float
	 */
	public static String toString(Float v) {
		if (v == null) { return null; }
		return v.toString();
	}
	/**
	 * This method returns a random Float value
	 * @return	A random float value between 0 and 1
	 */
	public static Float random() {
		Random rnd = new Random();
		return rnd.nextFloat();
	}
	/**
	 * This method return a random Float value within the defined range
	 * @param min	The minimum random value
	 * @param max	The maximum random value
	 * @return	A random Float value within the given range.
	 */
	public static Float random(Float min, Float max) {
		if (min == null || max == null) return random();
		return min+random()*(max-min);
	}
	/**
	 * This method converts null values
	 * @param checkValue		The value to check for null
	 * @param valueIfNull	The value to return if the value to check is null
	 * @return	The checkValue if it is not null else the valueIfNull
	 */
	public static Float nvl(Float checkValue, Float valueIfNull) {
		return (checkValue==null) ? valueIfNull : checkValue;
	}



}
