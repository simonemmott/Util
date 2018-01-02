package com.k2.Util.Identity;

/**
 * This interface provides a method to return a human readable value identifying the object. This method does not guarantee to provide a
 * value that can uniquely identify the object.
 * 
 * @author simon
 *
 */
public interface Identified {

	/**
	 * Provide a human readable String that identifies the object implementing the interface.
	 * 
	 * This method does not guarantee to return a value that will uniquely identify the object.
	 * Suitable source valus for his method are 'name', 'title', 'order_number' etc. 
	 * 
	 * @return A human readable value that identifies the object implementing the interface.
	 */
	public String getIdentity();
}
