package com.k2.Util.Identity;

import java.lang.reflect.Field;

import com.k2.Util.exceptions.UtilityError;

public class EntityFieldToKey<E, K> implements EntityToKey<E, K> {

	Field idField;
	Class<E> entityClass;
	EntityFieldToKey(Class<E> entityClass, Field idField) {
		this.idField = idField;
	}
	@Override
	public Class<E> getEntityClass() { return entityClass; }

	@SuppressWarnings("unchecked")
	@Override
	public Class<K> getKeyClass() { return (Class<K>) idField.getType(); }

	@SuppressWarnings("unchecked")
	@Override
	public K getKey(E entity) {
		try {
			return (K) idField.get(entity);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new UtilityError("Unable to get key from field {}:{} - {}",e, entityClass.getName(), idField.getName(), e.getMessage());
		}
	}

}
