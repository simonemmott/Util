package com.k2.Util;

/**
 * This interface provides a mechanism to call back to get a value of a particular type
 * @author simon
 *
 * @param <T>	The type of the value to be returned by the call back
 */
public interface CallBack<T> {
	/**
	 * Get the called value
	 * @return	The called value
	 */
	public T get();
	/**
	 * Get the type of the value returned by the call to get()
	 * @return	The class of the value returned by the call to get
	 */
	public Class<T> getJavaType();
	

}
