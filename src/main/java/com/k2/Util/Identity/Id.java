package com.k2.Util.Identity;

import java.io.Serializable;

/**
 * The Id interface formalises methods to return the serializable id of an object.
 * @author simon
 *
 * @param <T> The type of the object implementing the Id interface
 * @param <K> The type of the object used as the key value for the object. This must extend Serializable
 */
public interface Id<T,K extends Serializable> {
	
	/**
	 * The the value of the objects id
	 * @return	The objects id
	 */
	public K getId();
	
	/**
	 * Set the value of the objects id
	 * @param key	The new value for the objects id
	 * @return	The object having its id set
	 */
	public T setId(K key);

}
