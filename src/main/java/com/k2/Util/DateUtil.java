package com.k2.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This utility provides static method for handling Date values.
 * 
 * @author simon
 *
 */
public class DateUtil {
	/**
	 * This enumeration identifies the default data formatter
	 * 
	 * @author simon
	 *
	 */
	public static enum DateFormat{
		/**
		 * The default date format is date only
		 */
		DATE,
		/**
		 * The default date format is date and time
		 */
		DATE_TIME
	}
	public static void restoreDefaults() {
		defaultFormat = DateFormat.DATE_TIME;
		dateFormatter = null;
		datetimeFormatter = null;
	}
	/**
	 * Defines the current default date formatter
	 * Defaults to DATE_TIME
	 * @see DateFormat
	 */
	private static DateFormat defaultFormat = DateFormat.DATE_TIME;
	/**
	 * The date formatter
	 */
	private static SimpleDateFormat dateFormatter;
	/**
	 * This method returns the current date formatter
	 * 
	 * Defaults to "yyyy-MM-dd"
	 * 
	 * @return	The current date formatter
	 */
	public static SimpleDateFormat dateFormatter() {
		if (dateFormatter == null) { dateFormatter = new SimpleDateFormat("yyyy-MM-dd"); }
		return dateFormatter;
	}
	/**
	 * This method set the current date formatter to the given formatter
	 * 
	 * This formatter will be used if the utility is defined to format dates into date only format
	 * 
	 * @param newDateFormatter	The new date formatter
	 */
	public static void dateFormatter(SimpleDateFormat newDateFormatter) {
		dateFormatter = newDateFormatter;
	}
	/**
	 * The date time formatter
	 */
	private static SimpleDateFormat datetimeFormatter;
	/**
	 * This method returns the current date time formatter
	 * 
	 * Defaults to "yyyy-MM-dd HH:mm:SS"
	 * 
	 * @return The current date time formatter
	 */
	public static SimpleDateFormat dateTimeFormatter() {
		if (datetimeFormatter == null) { datetimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); }
		return datetimeFormatter;
	}
	/**
	 * This method set the current date time formatter to the given formatter
	 * 
	 * This formatter will be used if the utility is defined to format date into date time format
	 * 
	 * @param newDatetimeFormatter	The new date time formatter
	 */
	public static void dateTimeFormatter(SimpleDateFormat newDatetimeFormatter) {
		datetimeFormatter = newDatetimeFormatter;
	}
	/**
	 * This method returns the current default date formatter according to the default date format
	 * 
	 * @return The default date formatter
	 */
	public static SimpleDateFormat defaultDateFormatter() {
		switch(defaultFormat) {
		case DATE:
			return dateFormatter();
		case DATE_TIME:
			return dateTimeFormatter();
		}
		return null;
	}
	/**
	 * This method returns the current data formatter for the given date format
	 * @param format		The date format required
	 * @return	The date formatter for the given date format
	 */
	public static SimpleDateFormat dateFormatter(DateFormat format) {
		switch(format) {
		case DATE:
			return dateFormatter();
		case DATE_TIME:
			return dateTimeFormatter();
		}
		return null;
	}
	/**
	 * This method defines whethe the default date format should be date only or date and time
	 * 
	 * Defaults to date and time
	 * 
	 * @param newDefaultFormat The new default format to use
	 */
	public static void defaultDateFormatter(DateFormat newDefaultFormat) {
		defaultFormat = newDefaultFormat;
	}
	/**
	 * This map holds instances of Date TypeConverters indexed by the class that they convert into Date values
	 */
	private static Map<Class<?>, TypeConverter<?,Date>> typeConverters = new HashMap<Class<?>, TypeConverter<?,Date>>();
	/**
	 * This method registers with this static utility an instance of a date TypeConverter to convert an instance of the convertible class
	 * into a date value
	 * 
	 * @param converter	The date TypeConverter instance
	 */
	public static void registerTypeConverter(TypeConverter<?,Date> converter) {
		Class<?> cls = converter.convertClass();
		typeConverters.put(cls, converter);
	}
	/**
	 * This method converts the given object into a Date value
	 * 
	 * Static utility methods convert Strings, Integers, Longs, Floats, Doubles, Dates and Booleans into Date values
	 * If the given object is not one of the above types then a type converter must have been registered with this static utility
	 * in order for the given object to be converted into a Date value
	 * 
	 * If no type converter has been registered for the class of the given object then a null is returned
	 * 
	 * @param v The object to convert into a Date value
	 * @return	The Date representation of the given object or null if the given object is null or the class of the given object
	 * cannot be converted into a date value
	 */
	public static Date toDate(Object v) {
		if (v==null) { return null; }
		if (String.class.isAssignableFrom(v.getClass())) { return StringUtil.toDate((String)v); }
		if (Integer.class.isAssignableFrom(v.getClass())) { return IntegerUtil.toDate((Integer)v); }
		if (Long.class.isAssignableFrom(v.getClass())) { return LongUtil.toDate((Long)v); }
		if (Float.class.isAssignableFrom(v.getClass())) { return FloatUtil.toDate((Float)v); }
		if (Double.class.isAssignableFrom(v.getClass())) { return DoubleUtil.toDate((Double)v); }
		if (Date.class.isAssignableFrom(v.getClass())) { return (Date)v; }
		if (Boolean.class.isAssignableFrom(v.getClass())) { return BooleanUtil.toDate((Boolean)v); }
		for (Class<?> cls : typeConverters.keySet()) {
			if (cls.isAssignableFrom(v.getClass())) {
				return typeConverters.get(cls).convert(v);
			}
		}
		return null;		
	}
	/**
	 * This method converts the given date value into an Integer
	 * @param v	The date to convert
	 * @return	An integer value representing the given date
	 */
	public static Integer toInteger(Date v) {
		if (v == null) { return null; }
		return new Long(v.getTime()).intValue();
	}
	/**
	 * This method converts the given date value into a Long
	 * @param v	The date to convert
	 * @return	A Long representing the given date
	 */
	public static Long toLong(Date v) {
		if (v == null) { return null; }
		return v.getTime();
	}
	/**
	 * This method converts the given date into a Float
	 * @param v	The date to convert
	 * @return	A Float representing the given date
	 */
	public static Float toFloat(Date v) {
		if (v == null) { return null; }
		return new Float(v.getTime());
	}
	/**
	 * This method converts a date into a Double
	 * @param v	The date to convert
	 * @return	A Double value representing the given date
	 */
	public static Double toDouble(Date v) {
		if (v == null) { return null; }
		return new Double(v.getTime()); 	
	}
	/**
	 * This method converts the given date value into a Boolean
	 * @param v	The date to convert
	 * @return	A Boolean value representing the given date
	 */
	public static Boolean toBoolean(Date v) {
		if (v == null) { return null; }
		if (v.getTime() == BooleanUtil.falseAsInt()) { return false; }
		return true;	
	}
	/**
	 * This method converts the given date into a Date
	 * @param v	The date to convert
	 * @return	A date representing the given date
	 */
	public static Date toDate(Date v) {
		return v;
	}
	/**
	 * This method converts the given date into a String using the current default formatter
	 * @param v	The date to convert
	 * @return	A String representing the date according to the current default formatter
	 */
	public static String toString(Date v) {
		return toString(v, defaultDateFormatter());
	}
	/**
	 * This method converts a given date into a String using the current formatter for the given format
	 * @param v	The date to convert
	 * @param format		The format of the required output
	 * @return	A String representing the given date in the given format
	 */
	public static String toString(Date v, DateFormat format) {
		switch (format) {
		case DATE:
			return toString(v, dateFormatter());
		case DATE_TIME:
			return toString(v, dateTimeFormatter());
		}
		return toString(v);
	}
	/**
	 * This method converts the given date into a String using the given formatter
	 * @param v	The date to convert
	 * @param dateFormatter	The formatter to convert the date
	 * @return	A string representing the given date in the given format
	 */
	public static String toString(Date v, SimpleDateFormat dateFormatter) {
		if (v == null) { return null; }
		return dateFormatter.format(v);
	}
	/**
	 * This method converts a date into a string in the given format
	 * @param v	The date to convert
	 * @param format		A string defining the desired format of the date
	 * @return	A string representing the date in the defined format
	 */
	public static String toString(Date v, String format) {
		if (v == null) { return null; }
		return new SimpleDateFormat(format).format(v);
	}
	/**
	 * This method returns a random date
	 * @return	A random date
	 */
	public static Date random() {
		return new Date(LongUtil.random());
	}
	/**
	 * This method returns a random date between within the defined range.
	 * @param from	The start of the date range.
	 * @param to		The end of the date range
	 * @return	A random date within the range.
	 */
	public static Date random(Date from, Date to) {
		if (from == null || to == null) return random();
		long fromL = from.getTime();
		long toL = to.getTime();
		Double d = DoubleUtil.random();
		Double outputD = fromL+d*(toL-fromL);
		return new Date(outputD.longValue());
	}
	/**
	 * This method converts null values
	 * @param checkValue		The value to check for null
	 * @param valueIfNull	The value to return if the value to check is null
	 * @return	The checkValue if it is not null else the valueIfNull
	 */
	public static Date nvl(Date checkValue, Date valueIfNull) {
		return (checkValue==null) ? valueIfNull : checkValue;
	}



}
