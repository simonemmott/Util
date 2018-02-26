package com.k2.Util.classes;

/**
 * The Getter interface defines a generic interface for getting values from a field or getter method. 
 * @author simon
 *
 * @param <E>	The class for which the getter is to be invoked
 * @param <T>	The class of values returned by the call to get()
 */
public interface Getter<E,T> {
	
	/**
	 * This enumeration defines the possible types of getter implementations
	 * @author simon
	 *
	 */
	public enum Type {
		METHOD,
		FIELD
	}
	/**
	 * Allows equality comparisons between getters
	 * @return	the hash code for the getter
	 */
	public int hashCode();
	/**
	 * Allows equality comparsons between getters
	 * @param object	The object to compare to this getter
	 * @return	True if the objects are equal
	 */
	public boolean equals(Object object);
	/**
	 * This method get the value from the underlying field or getter method
	 * @param object		The object from which the value is to be got
	 * @return			The value of the underlying field or returned by the underlying getter method
	 */
	public T get(E object);
	/**
	 * The type of the value returned by the call to get()
	 * @return	The type of the value returned by the call to get()
	 */
	public Class<T> getJavaType();
	/**
	 * @return The class on which the underlying field or method is defined
	 */
	public Class<?> getDeclaringClass();
	/**
	 * 
	 * @return The class on which the getter is to be executed
	 */
	public Class<E> getThroughClass();
	/**
	 * The alias of the field or getter method. The alias of the field is the name by which the field is known in the java code. In the case of
	 * method getter this is the portion of the method name after the initial 'get' with it's first character in lower case
	 * @return	The alias of the getter
	 */
	public String getAlias();
	/**
	 * This method returns the type of the getter, METHOD or FIELD
	 * @return	The type of the getter
	 */
	public Type getType();

}
