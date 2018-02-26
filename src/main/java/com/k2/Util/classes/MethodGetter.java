package com.k2.Util.classes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.k2.Util.StringUtil;
import com.k2.Util.classes.Getter.Type;
import com.k2.Util.exceptions.UtilityError;

/**
 * This class implements the getter interface for an underlying getter method.
 * @author simon
 *
 * @param <E>	The type of the class on which the getter will be executed
 * @param <T>	The type of the variable returned by the call to get
 */
public class MethodGetter<E,T> implements Getter<E,T> {

	Method method;
	Class<E> objectClass;
	
	/**
	 * Create an instance of the getter interface for the given object class, return type and method
	 * @param objectClass	The class of the object on which this method getter will operate
	 * @param returnType		The type of value returned by the call to get
	 * @param method			The underlying method that returns the value
	 */
	public MethodGetter(Class<E> objectClass, Class<T> returnType, Method method) {
		if (!method.getReturnType().isAssignableFrom(returnType)) throw new UtilityError("Class mismatch for field. Field {}.{} doesn't supply a {}", method.getDeclaringClass().getName(), method.getName(), returnType.getName());
		if (!method.getDeclaringClass().isAssignableFrom(objectClass)) throw new UtilityError("Class mismatch for method. Method {}.{} isn't a method in {}", method.getDeclaringClass().getName(), method.getName(), objectClass.getName());
		if (!method.isAccessible()) method.setAccessible(true);
		this.method = method;
		this.objectClass = objectClass;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((method == null) ? 0 : method.hashCode());
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
		MethodGetter<?,?> other = (MethodGetter<?,?>) obj;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
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
			return (T) method.invoke(object);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			throw new UtilityError("Unable to get value from method {}.{}", e, method.getDeclaringClass().getName(), method.getName());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getJavaType() {
		return (Class<T>) method.getReturnType();
	}

	@Override
	public String getAlias() {
		return StringUtil.initialLowerCase(method.getName().substring(3));
	}

	@Override
	public Type getType() {
		return Type.METHOD;
	}

	@Override
	public Class<E> getThroughClass() {
		return objectClass;
	}

	@Override
	public Class<?> getDeclaringClass() {
		return method.getDeclaringClass();
	}

}
