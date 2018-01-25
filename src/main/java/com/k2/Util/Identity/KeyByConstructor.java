package com.k2.Util.Identity;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.k2.Util.exceptions.UtilityError;

public class KeyByConstructor implements KeyConstructor{
	Constructor<? extends Serializable> constructor;
	KeyByConstructor(Constructor<? extends Serializable> constructor) {
		this.constructor = constructor;
	}
	@Override
	public Serializable getKey(String keyStr) {
		Serializable key;
		try {
			key = constructor.newInstance(keyStr);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new UtilityError("Unable to generate key of type '{}' for '{}'", e, constructor.getDeclaringClass().getCanonicalName(), keyStr);
		}
		return key;
	}
}
