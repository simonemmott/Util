package com.k2.Util;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.jws.WebParam.Mode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This utility provides static method for handling Double values.
 * 
 * @author simon
 *
 */
public class DoubleUtil {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * This map holds instances of Double TypeConverters indexed by the class that they convert into Double values
	 */
	private static Map<Class<?>, TypeConverter<?,Double>> typeConverters = new HashMap<Class<?>, TypeConverter<?,Double>>();
	/**
	 * This method registers with this static utility an instance of a Double TypeConverter to convert an instance of the convertible class
	 * into a Double value
	 * 
	 * @param converter	The Double TypeConverter instance
	 */
	public static void registerTypeConverter(TypeConverter<?,Double> converter) {
		Class<?> cls = converter.convertClass();
		typeConverters.put(cls, converter);
	}
	/**
	 * This method converts the given object into a Double value
	 * 
	 * Static utility methods convert Strings, Integers, Longs, Floats, Doubles, Dates and Booleans into Double values
	 * If the given object is not one of the above types then a type converter must have been registered with this static utility
	 * in order for the given object to be converted into a Double value
	 * 
	 * If no type converter has been registered for the class of the given object then a null is returned
	 * 
	 * @param v The object to convert into a Double value
	 * @return	The Double representation of the given object or null if the given object is null or the class of the given object
	 * cannot be converted into a Double value
	 */
	public static Double toDouble(Object v) {
		if (v==null) { return null; }
		if (String.class.isAssignableFrom(v.getClass())) { return StringUtil.toDouble((String)v); }
		if (Integer.class.isAssignableFrom(v.getClass())) { return IntegerUtil.toDouble((Integer)v); }
		if (Long.class.isAssignableFrom(v.getClass())) { return LongUtil.toDouble((Long)v); }
		if (Float.class.isAssignableFrom(v.getClass())) { return FloatUtil.toDouble((Float)v); }
		if (Double.class.isAssignableFrom(v.getClass())) { return (Double)v; }
		if (Date.class.isAssignableFrom(v.getClass())) { return DateUtil.toDouble((Date)v); }
		if (Boolean.class.isAssignableFrom(v.getClass())) { return BooleanUtil.toDouble((Boolean)v); }
		for (Class<?> cls : typeConverters.keySet()) {
			if (cls.isAssignableFrom(v.getClass())) {
				return typeConverters.get(cls).convert(v);
			}
		}
		return null;		
	}
	/**
	 * This method converts the given Double value into an Integer
	 * @param v	The Double value to convert
	 * @return	An Integer value representing the given Double value
	 */
	public static Integer toInteger(Double v) {
		if (v == null) { return null; }
		return v.intValue();
	}
	/**
	 * This method converts the given Double value into a Long
	 * @param v	The Double value to convert
	 * @return	A Long value representing the given Double value
	 */
	public static Long toLong(Double v) {
		if (v == null) { return null; }
		return v.longValue();
	}
	/**
	 * This method converts the given Double value into a Float
	 * @param v	The Double value to convert
	 * @return	A Float value representing the given Double value
	 */
	public static Float toFloat(Double v) {
		if (v == null) { return null; }
		return new Float(v);
	}
	/**
	 * This method converts the given Double value into a Double
	 * @param v	The Double value to convert
	 * @return	A Double value representing the given Double value
	 */
	public static Double toDouble(Double v) {
		return v; 	
	}
	/**
	 * This method converts the given Double value into a Boolean
	 * @param v	The Double value to convert
	 * @return	A Boolean value representing the given Double value
	 */
	public static Boolean toBoolean(Double v) {
		if (v == null) { return null; }
		if (v.equals(new Double(BooleanUtil.falseAsInt()))) { return false; }
		return true;	
	}
	/**
	 * This method converts the given Double value into a Date
	 * @param v	The Double value to convert
	 * @return	A Date value representing the given Double value
	 */
	public static Date toDate(Double v) {
		if (v == null) { return null; }
		return new Date(v.longValue());
	}
	/**
	 * This method converts the given Double value into a String
	 * @param v	The Double value to convert
	 * @return	A String value representing the given Double value
	 */
	public static String toString(Double v) {
		if (v == null) { return null; }
		return v.toString();
	}
	/**
	 * This method returns a random Double value
	 * @return	A random double value between 0 and 1
	 */
	public static Double random() {
		Random rnd = new Random();
		return rnd.nextDouble();
	}
	/**
	 * This method return a random Double value within the defined range
	 * @param min	The minimum random value
	 * @param max	The maximum random value
	 * @return	A random double value within the given range.
	 */
	public static Double random(Double min, Double max) {
		if (min == null || max == null) return random();
		return min+random()*(max-min);
	}
	/**
	 * This method converts null values
	 * @param checkValue		The value to check for null
	 * @param valueIfNull	The value to return if the value to check is null
	 * @return	The checkValue if it is not null else the valueIfNull
	 */
	public static Double nvl(Double checkValue, Double valueIfNull) {
		return (checkValue==null) ? valueIfNull : checkValue;
	}

	/**
	 * This static method rounds the given double to the number of decimal places specified by the precision rounding half up
	 * @param unrounded	The un-rounded double value
	 * @param precision	The number of decimal places to round to
	 * @return			The given decimal number rounded to the given precision
	 */
	public static Double round(Double unrounded, int precision) {
		return round(unrounded, precision, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * This static method rounds the given double to the number of decimal places specified by the given precision using the
	 * given rounding mode.
	 * 
	 * @see BigDecimal
	 * 
	 * @param unrounded		The un-rounded doouble value
	 * @param precision		The number of decimal places to round to
	 * @param roundingMode	The rounding e.g. BigDecimal.ROUND_HALF_UP
	 * @return				The given decimal number rounded to the given precision
	 */
	public static Double round(Double unrounded, int precision, int roundingMode) {
		BigDecimal db = new BigDecimal(unrounded);
		BigDecimal rounded = db.setScale(precision, roundingMode);
		return rounded.doubleValue();
	}

}
