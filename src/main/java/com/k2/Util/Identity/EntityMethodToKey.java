package com.k2.Util.Identity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.k2.Util.exceptions.UtilityError;

public class EntityMethodToKey<E, K> implements EntityToKey<E, K> {

	Method idMethod;
	Class<E> entityClass;
	EntityMethodToKey(Class<E> entityClass, Method idMethod) {
		this.idMethod = idMethod;
	}
	@Override
	public Class<E> getEntityClass() { return entityClass; }

	@SuppressWarnings("unchecked")
	@Override
	public Class<K> getKeyClass() { return (Class<K>) idMethod.getReturnType(); }

	@SuppressWarnings("unchecked")
	@Override
	public K getKey(E entity) {
		try {
			return (K) idMethod.invoke(entity);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			throw new UtilityError("Unable to get key from method {}::{}() - {}", e, entityClass.getName(), idMethod.getName(), e.getMessage());
		}
	}

}
