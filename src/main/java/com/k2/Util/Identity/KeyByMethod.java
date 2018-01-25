package com.k2.Util.Identity;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.k2.Util.exceptions.UtilityError;

public class KeyByMethod implements KeyConstructor{
	Class<?> cls;
	Method setter;
	KeyByMethod(Class<?> cls, Method setter) {
		this.cls = cls;
		this.setter = setter;
	}
	@Override
	public Serializable getKey(String keyStr) {
		Serializable key;
		try {
			key = (Serializable) cls.newInstance();
			setter.invoke(key, keyStr);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new UtilityError("Unable to generate key of type '{}' for '{}'", e, cls.getCanonicalName(), keyStr);
		}
		return key;
	}
}

