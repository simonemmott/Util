package com.k2.Util.Identity;

import java.lang.reflect.Field;

import javax.persistence.Id;

import com.k2.Util.classes.ClassUtil;
import com.k2.Util.exceptions.UtilityError;

public class EntityIdClassToKey<E, K> implements EntityToKey<E, K> {

	Class<K> idClass;
	Class<E> entityClass;
	Field[] idFields;
	Field[] keyFields;
	EntityIdClassToKey(Class<E> entityClass, Class<K> idClass) {
		this.idClass = idClass;
		
		idFields = ClassUtil.getAllAnnotatedFields(entityClass, Id.class);
		keyFields = new Field[idFields.length];
		
		for (int i=0; i<idFields.length; i++)
			keyFields[i] = ClassUtil.getField(idClass, idFields[i].getName());

		
	}
	@Override
	public Class<E> getEntityClass() { return entityClass; }

	@Override
	public Class<K> getKeyClass() { return idClass; }

	@Override
	public K getKey(E entity) {
		
		K key;
		try {
			key = idClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new UtilityError("Unable to create new instance of {}. No public zero arg constructor", idClass.getName());
		}
		for (int i=0; i<idFields.length; i++)
			try {
				keyFields[i].set(key, idFields[i].get(entity));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new UtilityError("Unable to populate key field {} in Id class {}", idFields[i].getName(), idClass.getName());
			}
		return key;
	}

}
