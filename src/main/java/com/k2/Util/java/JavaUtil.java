package com.k2.Util.java;

import java.util.Date;

public class JavaUtil {

	public static String toJavaSource(Object value) {
		return toJavaSource(value.toString());
	}
	
	public static String toJavaSource(String value) {
		StringBuilder sb = new StringBuilder();
		sb.append('"');
		for (char c : value.toCharArray()) {
			sb.append(charToString(c));
		}
		sb.append('"');
		return sb.toString();
	}
	
	public static String toJavaSource(int value) { return String.valueOf(value); }
	public static String toJavaSource(Integer value) { return "Integer.valueOf("+value+")"; }
	
	public static String toJavaSource(long value) { return String.valueOf(value)+"L"; }
	public static String toJavaSource(Long value) { return "Long.valueOf("+value+")"; }
	
	public static String toJavaSource(short value) { return "(short)"+String.valueOf(value); }
	public static String toJavaSource(Short value) { return "Short.valueOf((short)"+value+")"; }
	
	public static String toJavaSource(byte value) { return "(byte)"+String.valueOf(value); }
	public static String toJavaSource(Byte value) { return "Byte.valueOf((byte)"+value+")"; }
	
	private static String charToString(char c) {
		switch (c) {
		case '\n':
			return "\\n";
		case '\t':
			return "\\t";
		case '\b':
			return "\\b";
		case '\f':
			return "\\f";
		case '\r':
			return "\\r";
		case '\"':
			return "\\\"";
		case '\'':
			return "\\'";
		case '\\':
			return "\\\\";
		}
		return String.valueOf(c); 
		
	}
	
	public static String toJavaSource(char value) { return "'"+charToString(value)+"'"; }
	public static String toJavaSource(Character value) { return "Character.valueOf("+toJavaSource(value.charValue())+")"; }
	
	public static String toJavaSource(float value) { return String.valueOf(value)+"f"; }
	public static String toJavaSource(Float value) { return "Float.valueOf("+value+"f)"; }
	
	public static String toJavaSource(double value) { return String.valueOf(value); }
	public static String toJavaSource(Double value) { return "Double.valueOf("+value+")"; }
	
	public static String toJavaSource(boolean value) { return String.valueOf(value); }
	public static String toJavaSource(Boolean value) { return "Boolean.valueOf("+value+")"; }
	
	public static String toJavaSource(Date value) { return "java.util.Date.valueOf("+value.getTime()+")"; }
	
}
