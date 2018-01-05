package com.k2.Util;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This utility provides static method for handling Boolean values.
 * 
 * @author simon
 *
 */
public class BooleanUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static String FALSE_STRING = "false";
	private static String TRUE_STRING = "true";
	private static int FALSE_INT = 0;
	private static int TRUE_INT = 1;
	/**
	 * This method restores the default settings of the BooleanUtil
	 */
	public static void restoreDefaults() {
		FALSE_STRING = "false";
		TRUE_STRING = "true";
		FALSE_INT = 0;
		TRUE_INT = 1;
	}
	/**
	 * This method defines the value that is used to represent a true value as a String
	 * 
	 * The default value is 'true'
	 * 
	 * @param value	The String representation of the value true
	 */
	public static void trueAsString(String value) { TRUE_STRING = value; }
	/**
	 * The method returns the current value that is used to represent a true value as a String
	 * @return	The current value that is used to represent true as a String
	 */
	public static String trueAsString() { return TRUE_STRING; }
	/**
	 * This method defines the value that is used to represent a false value as a String
	 * 
	 * The default value is 'false'
	 * 
	 * @param value The String representation of the value false
	 */
	public static void falseAsString(String value) { FALSE_STRING = value; }
	/**
	 * This method returns the current value that is used to represent the value false as a String
	 * @return The current value that is used to represent false as a String
	 */
	public static String falseAsString() { return FALSE_STRING; }
	/**
	 * Convert the given boolean value into an integer
	 * @param v	The boolean value to convert
	 * @return	The given boolean value as an integer
	 */
	/**
	 * This method defines the value that is used to represent a true value as an integer
	 * 
	 * The default value is '1'
	 * 
	 * @param value	The integer representation of the value true
	 */
	public static void trueAsInt(int value) { TRUE_INT = value; }
	/**
	 * The method returns the current value that is used to represent a true value as an integer
	 * @return	The current value that is used to represent true as a integer
	 */
	public static Integer trueAsInt() { return TRUE_INT; }
	/**
	 * This method defines the value that is used to represent a false value as an integer
	 * 
	 * The default value is '0'
	 * 
	 * @param value The integer representation of the value false
	 */
	public static void falseAsInt(int value) { FALSE_INT = value; }
	/**
	 * This method returns the current value that is used to represent the value false as an integer
	 * @return The current value that is used to represent false as an integer
	 */
	public static Integer falseAsInt() { return FALSE_INT; }
	/**
	 * Convert the given boolean value into an integer
	 * @param v	The boolean value to convert
	 * @return	The given boolean value as an integer
	 */
	public static Integer toInteger(Boolean v) {
		if (v == null) { return null; }
		if (v) { return TRUE_INT; } else { return FALSE_INT; }
	}
	/**
	 * Convert the given boolean value to a Long value
	 * @param v	The boolean value to convert
	 * @return	A long representing the given boolean value
	 */
	public static Long toLong(Boolean v) {
		if (v == null) { return null; }
		if (v) { return new Long(TRUE_INT); } else { return new Long(FALSE_INT); }
	}
	/**
	 * Convert the given boolean value into a Float
	 * @param v	The boolean value to convert
	 * @return	The float value representing the given boolean value
	 */
	public static Float toFloat(Boolean v) {
		if (v == null) { return null; }
		if (v) { return new Float(TRUE_INT); } else { return new Float(FALSE_INT); }
	}
	/**
	 * Convert the given boolean value into a Double
	 * @param v	The boolean value to convert
	 * @return	The Double representing the given boolean value
	 */
	public static Double toDouble(Boolean v) {
		if (v == null) { return null; }
		if (v) { return new Double(TRUE_INT); } else { return new Double(FALSE_INT); }		
	}
	/**
	 * This map holds instances of Boolean TypeConverters indexed by the class that they convert into Boolean values
	 */
	private static Map<Class<?>, TypeConverter<?,Boolean>> typeConverters = new HashMap<Class<?>, TypeConverter<?,Boolean>>();
	/**
	 * This method registers with this static utility an instance of a boolean TypeConverter to convert an instance of the convertible class
	 * into a boolean value
	 * 
	 * @param converter	The boolean TypeConverter instance
	 */
	public static void registerTypeConverter(TypeConverter<?,Boolean> converter) {
		Class<?> cls = converter.convertClass();
		typeConverters.put(cls, converter);
	}
	/**
	 * This method converts the given object into a Boolean value
	 * 
	 * Static utility methods convert Strings, Integers, Longs, Floats, Doubles, Dates and Booleans into Boolean values
	 * If the given object is not one of the above types then a type converter must have been registered with this static utility
	 * in order for the given object to be converted into a boolean value
	 * 
	 * If no type converter has been registered for the class of the given object then a null is returned
	 * 
	 * @param v The object to convert into a Boolean value
	 * @return	The boolean representation of the given object or null if the given object is null or the class of the given object
	 * cannot be converted into a boolean value
	 */
	public static Boolean toBoolean(Object v) {
		if (v==null) { return null; }
		if (String.class.isAssignableFrom(v.getClass())) { return StringUtil.toBoolean((String)v); }
		if (Integer.class.isAssignableFrom(v.getClass())) { return IntegerUtil.toBoolean((Integer)v); }
		if (Long.class.isAssignableFrom(v.getClass())) { return LongUtil.toBoolean((Long)v); }
		if (Float.class.isAssignableFrom(v.getClass())) { return FloatUtil.toBoolean((Float)v); }
		if (Double.class.isAssignableFrom(v.getClass())) { return DoubleUtil.toBoolean((Double)v); }
		if (Date.class.isAssignableFrom(v.getClass())) { return DateUtil.toBoolean((Date)v); }
		if (Boolean.class.isAssignableFrom(v.getClass())) { return (Boolean)v; }
		for (Class<?> cls : typeConverters.keySet()) {
			if (cls.isAssignableFrom(v.getClass())) {
				return typeConverters.get(cls).convert(v);
			}
		}
		return null;		
	}
	/**
	 * This method converts the given boolean value into a Data
	 * @param v	The boolean value to convert
	 * @return	The date representing the given boolean value. If the given boolean value is false then the epoch is returned. 
	 * If the given boolean value is true then the system current date is returned. If the given boolean value is null then null
	 * is returned.
	 */
	public static Date toDate(Boolean v) {
		if (v == null) { return null; }
		if (v) { return new Date(); } else { return new Date(FALSE_INT); }
	}
	/**
	 * This method converts the given boolean value into a String
	 * @param v	The boolean value to convert
	 * @return	A string representing the given boolean value
	 */
	public static String toString(Boolean v) {
		if (v == null) { return null; }
		if (v) { return TRUE_STRING; } else { return FALSE_STRING; }
	}
	/**
	 * This method returns a random Boolean value
	 * @return	A random Boolean value
	 */
	public static Boolean randomBoolean() {
		Random rnd = new Random();
		return (rnd.nextInt(2) == 0);
	}
	/**
	 * This method converts null values
	 * @param checkValue		The value to check for null
	 * @param valueIfNull	The value to return if the value to check is null
	 * @return	The checkValue if it is not null else the valueIfNull
	 */
	public static Boolean nvl(Boolean checkValue, Boolean valueIfNull) {
		return (checkValue==null) ? valueIfNull : checkValue;
	}



}
