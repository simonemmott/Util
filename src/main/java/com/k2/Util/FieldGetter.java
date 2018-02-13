package com.k2.Util;

import java.lang.reflect.Field;

import com.k2.Util.exceptions.UtilityError;

/**
 * This class implements the Getter interface for a field value
 * @author simon
 *
 * @param <E>	The type of object that includes the field
 * @param <T>	The type of value held in the field
 */
public class FieldGetter<E,T> implements Getter<E,T> {

	Field field;
	Class<E> objectClass;
	
	/**
	 * Create an instance of FieldGetter for the given object class, field class and field
	 * @param objectClass	The class through which the getter will be invoked
	 * @param fieldClass		The class of the objects identified by the field
	 * @param field			The field from which the getter will draw its value
	 */
	public FieldGetter(Class<E> objectClass, Class<T> fieldClass, Field field) {
		if (!field.getType().isAssignableFrom(fieldClass)) throw new UtilityError("Class mismatch for field. Field {}.{} doesn't supply a {}", field.getDeclaringClass().getName(), field.getName(), fieldClass.getName());
		if (!field.getDeclaringClass().isAssignableFrom(objectClass)) throw new UtilityError("Class mismatch for field. Field {}.{} isn't a field in {}", field.getDeclaringClass().getName(), field.getName(), objectClass.getName());
		if (!field.isAccessible()) field.setAccessible(true);
		
		this.field = field;
		this.objectClass = objectClass;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		result = prime * result + ((objectClass == null) ? 0 : objectClass.getName().hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FieldGetter<?,?> other = (FieldGetter<?,?>) obj;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		if (objectClass == null) {
			if (other.objectClass != null)
				return false;
		} else if (!objectClass.getName().equals(other.objectClass.getName()))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return getType()+":"+getThroughClass().getName()+"."+getAlias();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(E object) {
		try {
			return (T) field.get(object);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new UtilityError("Unable to get value from field {}.{}", e, field.getDeclaringClass().getName(), field.getName());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getJavaType() {
		return (Class<T>) field.getType();
	}

	@Override
	public String getAlias() {
		return field.getName();
	}

	@Override
	public Type getType() {
		return Type.FIELD;
	}

	@Override
	public Class<E> getThroughClass() {
		return objectClass;
	}

	@Override
	public Class<?> getDeclaringClass() {
		return field.getDeclaringClass();
	}

}
