package com.k2.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.k2.Util.DateUtil.DateFormat;

/**
 * This utility provides static method for handling String values.
 * 
 * @author simon
 *
 */
public class StringUtil {
	/**
	 * The set of characters from which to build random strings
	 */
	private static String SALT_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMONPQRSTUVWXYZ";
	/**
	 * Get the set of characters from which to build random strings
	 * 
	 * @return	The set of characters from which to build random strings
	 */
	public static String saltChars() { return SALT_CHARS; }
	/**
	 * Set the characters from which to build random strings
	 * 
	 * @param newSaltChars	The new set of characters from which to build random strings
	 */
	public static void saltChars(String newSaltChars) {
		SALT_CHARS = newSaltChars;
	}
	/**
	 * The set of safe characters
	 */
	private static String SAFE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMONPQRSTUVWXYZ0123456789";
	/**
	 * Get the set of safe characters
	 * 
	 * @return	The set of safe characters
	 */
	public static String safeChars() { return SAFE_CHARS; }
	/**
	 * Set the safe characters
	 * 
	 * @param newSafeChars	The new set of safe characters
	 */
	public static void safeChars(String newSafeChars) {
		SAFE_CHARS = newSafeChars;
	}
	/**
	 * The set of very safe characters
	 */
	private static String VERY_SAFE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMONPQRSTUVWXYZ";
	/**
	 * Get the set of safe characters
	 * 
	 * @return	The set of very safe characters
	 */
	public static String verySafeChars() { return VERY_SAFE_CHARS; }
	/**
	 * Set the very safe characters
	 * 
	 * @param newVerySafeChars	The new set of safe characters
	 */
	public static void verySafeChars(String newVerySafeChars) {
		VERY_SAFE_CHARS = newVerySafeChars;
	}
	private static String DIGITS = "0123456789";
	
	/**
	 * This map holds instances of String TypeConverters indexed by the class that they convert into String values
	 */
	private static Map<Class<?>, TypeConverter<?,String>> typeConverters = new HashMap<Class<?>, TypeConverter<?,String>>();
	/**
	 * This method registers with this static utility an instance of a String TypeConverter to convert an instance of the convertible class
	 * into a String value
	 * 
	 * @param converter	The String TypeConverter instance
	 */
	public static void registerTypeConverter(TypeConverter<?,String> converter) {
		Class<?> cls = converter.convertClass();
		typeConverters.put(cls, converter);
	}
	/**
	 * This method converts the given object into a String value
	 * 
	 * Static utility methods convert Strings, Integers, Longs, Floats, Doubles, Dates and Booleans into String values
	 * If the given object is not one of the above types then a type converter must have been registered with this static utility
	 * in order for the given object to be converted into a String value
	 * 
	 * If no type converter has been registered for the class of the given object then a null is returned
	 * 
	 * @param v The object to convert into a String value
	 * @return	The String representation of the given object or null if the given object is null or the class of the given object
	 * cannot be converted into a String value
	 */
	public static String toString(Object v) {
		if (v==null) { return null; }
		if (String.class.isAssignableFrom(v.getClass())) { return (String)v; }
		if (Integer.class.isAssignableFrom(v.getClass())) { return IntegerUtil.toString((Integer)v); }
		if (Long.class.isAssignableFrom(v.getClass())) { return LongUtil.toString((Long)v); }
		if (Float.class.isAssignableFrom(v.getClass())) { return FloatUtil.toString((Float)v); }
		if (Double.class.isAssignableFrom(v.getClass())) { return DoubleUtil.toString((Double)v); }
		if (Date.class.isAssignableFrom(v.getClass())) { return DateUtil.toString((Date)v); }
		if (Boolean.class.isAssignableFrom(v.getClass())) { return BooleanUtil.toString((Boolean)v); }
		for (Class<?> cls : typeConverters.keySet()) {
			if (cls.isAssignableFrom(v.getClass())) {
				return typeConverters.get(cls).convert(v);
			}
		}
		return null;		
	}
	/**
	 * This method converts the given string into a Integer value
	 * @param v	The String to convert
	 * @return	A Integer representing the given String
	 */
	public static Integer toInteger(String v) {
		if (!StringUtil.isSet(v)) { return null; }
		try {
			Double d = Double.parseDouble(v);
			return d.intValue();
		} catch (Throwable e) {
			UtilsLogger.warning("Unable to convert '"+v+"' into an Integer.");
			return null;
		}
	}
	/**
	 * This method converts the given string into a Long value
	 * @param v	The String to convert
	 * @return	A Long representing the given String
	 */
	public static Long toLong(String v) {
		if (!StringUtil.isSet(v)) { return null; }
		try {
			Double d = Double.parseDouble(v);
			return d.longValue();
		} catch (Throwable e) {
			UtilsLogger.warning("Unable to convert '"+v+"' into a Long.");
			return null;
		}
	}
	/**
	 * This method converts the given string into a Float value
	 * @param v	The String to convert
	 * @return	A Float representing the given String
	 */
	public static Float toFloat(String v) {
		if (!StringUtil.isSet(v)) { return null; }
		try {
			return Float.parseFloat(v);
		} catch (Throwable e) {
			UtilsLogger.warning("Unable to convert '"+v+"' into a Float.");
			return null;
		}
	}
	/**
	 * This method converts the given string into a Double value
	 * @param v	The String to convert
	 * @return	A Double representing the given String
	 */
	public static Double toDouble(String v) {
		if (!StringUtil.isSet(v)) { return null; }
		try {
			return Double.parseDouble(v);
		} catch (Throwable e) {
			UtilsLogger.warning("Unable to convert '"+v+"' into a Double.");
			return null;
		}
	}
	/**
	 * This method converts the given string into a Boolean value
	 * @param v	The String to convert
	 * @return	A Boolean representing the given String
	 */
	public static Boolean toBoolean(String v) {
		if (v == null) { return null; }
		if (v.equals("")) { return false; }
		if (v.equalsIgnoreCase(BooleanUtil.trueAsString())) { return true; }
		if (v.equalsIgnoreCase(BooleanUtil.falseAsString())) { return false; }
		try {
			if (new Float(BooleanUtil.falseAsInt()).equals(StringUtil.toFloat(v))) { return false; }
		} catch (NumberFormatException e) {}
		return true;	
	}
	/**
	 * This method converts the given string into a Date value
	 * @param v	The String to convert
	 * @return	A Date representing the given String
	 */
	public static Date toDate(String v) {
		if (!StringUtil.isSet(v)) { return null; }
		try {
			return DateUtil.defaultDateFormatter().parse(v);
		} catch (Throwable e) {
			UtilsLogger.warning("Unable to convert '"+v+"' into a Date using the default date formatter with format '"+DateUtil.defaultDateFormatter().toPattern()+"'");
			return null;
		}
	}
	/**
	 * This method converts the given string into a Date value
	 * @param v	The String to convert
	 * @param format		The requested data format
	 * @return	A Date representing the given String in the requested format
	 */
	public static Date toDate(String v, DateFormat format) {
		if (!StringUtil.isSet(v)) { return null; }
		try {
			return DateUtil.dateFormatter(format).parse(v);
		} catch (Throwable e) {
			UtilsLogger.warning("Unable to convert '"+v+"' into a Date using the default date formatter with format '"+DateUtil.dateFormatter(format).toPattern()+"'");
			return null;
		}
	}
	/**
	 * This method converts the given string into a Date value
	 * @param v	The String to convert
	 * @param formatter		The date formatter to convert the Date
	 * @return	A Date representing the given String in the requested format
	 */
	public static Date toDate(String v, SimpleDateFormat formatter) {
		if (!StringUtil.isSet(v)) { return null; }
		try {
			return formatter.parse(v);
		} catch (Throwable e) {
			UtilsLogger.warning("Unable to convert '"+v+"' into a Date using the default date formatter with format '"+formatter.toPattern()+"'");
			return null;
		}
	}
	/**
	 * This method converts the given string into a Date value
	 * @param v	The String to convert
	 * @param format		A String representing the format of the required date
	 * @return	A Date representing the given String in the requested format
	 */
	public static Date toDate(String v, String format) {
		if (!StringUtil.isSet(v)) { return null; }
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(v);
		} catch (Throwable e) {
			UtilsLogger.warning("Unable to convert '"+v+"' into a Date using the default date formatter with format '"+sdf.toPattern()+"'");
			return null;
		}
	}
	/**
	 * This method converts the given string into a String value
	 * @param v	The String to convert
	 * @return	A String representing the given String
	 */
	public static String toString(String v) {
		return v;
	}
	/**
	 * This method generates a random String of the given length
	 * @param length		The desired length of the random string
	 * @return	A random String of the given length
	 */
	public static String random(int length) {
        return random(length, SALT_CHARS);
	}
	/**
	 * This method generates a String of within the length range specified
	 * @param minLength	The minimum length of the String
	 * @param maxLength	The maximum length of the String
	 * @return	The random string within the given length range
	 */
	public static String random(int minLength, int maxLength) {
        return random(minLength, maxLength, SALT_CHARS);
	}
	/**
	 * This method generates a random String of the given length from the given set of characters
	 * @param length		The desired length of the random string
	 * @param saltChars	The set of characters from which to build the random String
	 * @return	The random string of the desired length
	 */
	public static String random(Integer length, String saltChars) {
		if (length == null) length = IntegerUtil.random();
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * saltChars.length());
            salt.append(saltChars.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
	}
	/**
	 * This method returns a random String in the given length range from the given set of characters
	 * @param minLength	The minimum length of the string
	 * @param maxLength	The maximum length of the string
	 * @param saltChars	The set of characters from which to build the string
	 * @return	The random string of the desired length
	 */
	public static String random(Integer minLength, Integer maxLength, String saltChars) {
		if (minLength == null && maxLength == null) return random(IntegerUtil.random(), saltChars);
		if (minLength == null) return random(1, maxLength, saltChars);
		if (maxLength == null) return random(minLength, IntegerUtil.random(), saltChars);
        return random(IntegerUtil.random(minLength, maxLength), saltChars);
	}
	/**
	 * This method returns the given String with a lower case initial character
	 * @param input	the String
	 * @return	The string with its initial character in lower case
	 */
	public static String initialLowerCase(String input) {
		if (input == null) return null;
		if (input.length() == 0) return input;
		String c = input.substring(0, 1).toLowerCase();
		return c+input.substring(1);
	}
	/**
	 * The method returns the given String with its initial character in upper case
	 * @param input	The String
	 * @return	The String with its initial character in upper case
	 */
	public static String initialUpperCase(String input) {
		if (input == null) return null;
		if (input.length() == 0) return input;
		String c = input.substring(0, 1).toUpperCase();
		return c+input.substring(1);
	}
	/**
	 * This method checks whether the string is not null and has no white space charaters
	 * @param value	The string to check
	 * @return	True if the string is no null and has non-whitespace characters
	 */
	public static boolean isSet(String value) {
		if (value==null) { return false; }
		if ("".equals(value.trim())) { return false; }
		return true;
	}
	/**
	 * This method splits a String into words on any whitespace characters
	 * @param value	The string to split into words
	 * @return	An array of words
	 */
	public static String[] words(String value) {
		return value.split("\\s+");
	}
	/**
	 * This method ensures that only safe characters exist in the word.
	 * 
	 * If the word only contains unsafe characters then an 6 character random string is returned
	 * 
	 * @param word	The word to make safe
	 * @return	The word with unsafe characters removed
	 */
	public static String safeWord(String word) {
		String output = "";
		for (char c : word.toCharArray()) {
			if (SAFE_CHARS.indexOf(c) != -1) output = output+c;
		}
		if ("".equals(output)) output = random(6);
		return output;
	}
	/**
	 * This method converts a String into alias case.
	 * 
	 * Alias case has a lower case initial character for the first word and an upper case initial character
	 * for all subsequent words and no spaces
	 * @param value	The string to convert to alias case
	 * @return	The string in alias case
	 */
	public static String aliasCase(String value) {
		String[] words = words(value);
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (String word : words) {
			String safeWord = safeWord(word);
			if (first) {
				if (DIGITS.indexOf(safeWord.toCharArray()[0]) != -1) sb.append('_');
				sb.append(initialLowerCase(safeWord.toLowerCase()));
				first = false;
			} else {
				sb.append(initialUpperCase(safeWord.toLowerCase()));
			}
		}
		return sb.toString();
	}
	/**
	 * This method converts a String to camel case
	 * 
	 * Camel case has an upper case initial character for each word in the input string and separates the words with a single white space
	 * @param value	The String to convert to Camel case
	 * @return	The String in Camel case
	 */
	public static String camelCase(String value) {
		String[] words = words(value);
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String word : words) {
			if (!first) {
				sb.append(' ');
			}
			first = false;
			sb.append(initialUpperCase(word.toLowerCase()));
		}
		return sb.toString();
	}
	/**
	 * This method converts a string to class case.
	 * 
	 * Class case has an upper case initial character for each word and no spaces
	 * @param value	The string to convert to class case
	 * @return	The string in class case
	 */
	public static String classCase(String value) {
		String[] words = words(value);
		StringBuilder sb = new StringBuilder();
		for (String word : words) {
			String safeWord = safeWord(word);
			if (DIGITS.indexOf(safeWord.toCharArray()[0]) != -1) sb.append('_');
			sb.append(initialUpperCase(safeWord.toLowerCase()));
		}
		return sb.toString();
	}
	/**
	 * This method converts a string to static case
	 * 
	 * Static case is all in upper case with words separated by a single underscore '_'
	 * @param value	The string to convert to static case
	 * @return	The string in static case
	 */
	public static String staticCase(String value) {
		String[] words = words(value);
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String word : words) {
			String safeWord = safeWord(word);
			if (!first) {
				sb.append('_');
			}
			first = false;
			if (DIGITS.indexOf(safeWord.toCharArray()[0]) != -1) sb.append('_');
			sb.append(safeWord.toUpperCase());
		}
		return sb.toString();
	}
	/**
	 * This method converts a string to kebab case
	 * 
	 * Kebab case is all in lower case with words separated by a hyphen '-'
	 * @param value	The string to convert to kebab case
	 * @return	The string in kebab case
	 */
	public static String kebabCase(String value) {
		String[] words = words(value);
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String word : words) {
			if (!first) {
				sb.append('-');
			}
			first = false;
			sb.append(word.toLowerCase());
		}
		return sb.toString();
	}
	/**
	 * This method converts null values
	 * @param checkValue		The value to check for null
	 * @param valueIfNull	The value to return if the value to check is null
	 * @return	The checkValue if it is not null else the valueIfNull
	 */
	public static String nvl(String checkValue, String valueIfNull) {
		return (checkValue==null) ? valueIfNull : checkValue;
	}


}
