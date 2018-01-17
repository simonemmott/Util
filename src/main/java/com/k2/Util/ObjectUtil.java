package com.k2.Util;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.Util.Identity.IdentityUtil;

public class ObjectUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	public static <T> void copy(T fromObj, T toObj) {
		if (fromObj == null || toObj == null) return;
		Class<?> cls = fromObj.getClass();
		
		Field[] fields = ClassUtil.getDeclaredFields(cls);
		
		for (Field f : fields) {
			logger.trace("Attempting to set field {}", f.getName());
			if (f.isSynthetic()) logger.trace("Field {} is synthetic", f.getName());
			if (!f.isAccessible()) f.setAccessible(true);
			try {
				f.set(toObj, f.get(fromObj));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.warn("Unable to set field {} on {}({}) to same field on {}({})", 
						f.getName(), 
						toObj.getClass(), 
						IdentityUtil.getId(toObj), 
						fromObj.getClass(), 
						IdentityUtil.getId(fromObj)
					);
			}
		}
		
		
		
	}
	
	private static Field findMatchingField(Field match, Field[] fields) {
		for (Field check : fields) {
			try {
			if (		check.getName().equals(match.getName()) &&
					check.getType().equals(match.getType())) {
				return check;
			}
			} catch (Throwable t) {
				logger.warn("Failed comparing Field {}.{} to Field {}.{}", match.getDeclaringClass().getCanonicalName(), match.getName(), check.getDeclaringClass().getCanonicalName(), check.getName());
			}
		}
		
		return null;
	}

	/**
	 * This static method finds the first matching item in each list or null if none of the list contents match
	 * @param lst1	The first list
	 * @param lst2	The second list
	 * @param <T> The type of objects in the given lists
	 * @return	The first matching object in each list
	 */
	public static <T> T findFirstMatch(List<T> lst1, List<T> lst2) {
		for (int i=0; i<lst1.size(); i++) {
			T t1 = lst1.get(i);
			logger.trace("List1[{}] {} <<<",i, t1);
			for (int j=0; j<lst2.size(); j++) {
				T t2 = lst2.get(j);
				logger.trace("List2[{}] {}{}", j, t2, ((t1.equals(t2))?" match":""));
				if (t1.equals(t2)) return t1;
			}
		}
		return null;
	}
}
