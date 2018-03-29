package com.k2.Util;

public class ArrayUtil {

	public enum CheckType {
		ANY,
		ALL;
	}
	
	public static <X> boolean contains(X[] arr, CheckType checkType, X ... values ) {
		boolean matched = false;
		for (X value : values) {
			matched = false;
			for (X item : arr) {
				if (item.equals(value)) {
					matched = true;
					break;
				}
			}
			if (checkType == CheckType.ANY && matched)
				return true;
			if (checkType == CheckType.ALL && ! matched)
				return false;
		}
		
		
		return matched;
	}
	
}
