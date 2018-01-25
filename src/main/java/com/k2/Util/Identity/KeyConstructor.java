package com.k2.Util.Identity;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.k2.Util.Identity.KeyByMethod;
import com.k2.Util.exceptions.UtilityError;

public interface KeyConstructor {
	public Serializable getKey(String keyStr); 
	public static KeyConstructor keyConstructor(Class<? extends Serializable> cls) {
		
		for (Method m : cls.getDeclaredMethods()) {
			if (		m.isAnnotationPresent(KeySetter.class) && 
					m.getParameterTypes().length == 1 && 
					m.getParameterTypes()[0] == String.class) {
				return new KeyByMethod(cls, m);
			}
		}
		
		try {
			return new KeyByConstructor(cls.getConstructor(String.class));
		} catch (NoSuchMethodException | SecurityException e) {
			throw new UtilityError("Unable to identify suitable key constructor appraoch for class '{}'", cls.getCanonicalName());
		}
	}

}
