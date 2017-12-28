package com.k2.Util.Version;

/**
 * The Version interface defines a 3 part version number, comprised of major, minor and point version numbers.
 * 
 * @author simon
 *
 */
public interface Version {
	
	/**
	 * Create the default initial version "v0.0.0"
	 * 
	 * @return An implementation of the Version interface with the version number v0.0.0
	 * 
	 */
	public static Version create() {
		return new SimpleVersion(0,0,0);
	}
	
	/**
	 * Create a version for the given major, minor and point version numbers
	 * @param major	The major number of the version
	 * @param minor	The minor number of the version
	 * @param point	The point number of the version
	 * @return An implementation of the Version interface with the given version numbers
	 */
	public static Version create(int major, int minor, int point) {
		return new SimpleVersion(major, minor, point);
	}
	
	/**
	 * Increment the version, changing the defined number component
	 * 
	 * If the point number is to be changed then just the point number is incremented
	 * If the minor number is to be changed then the minor version number is incremented and the point
	 * number is set to zero
	 * If the major number is to be changed then the major version number is incremeted and the 
	 * point and minor numbers are set to zero.
	 * 
	 * @param inc Change the major, minor or point version number
	 */
	public void increment(Increment inc);
	
	/**
	 * Check whether the given version is this or an earlier version.
	 * @param ver	The version to compare to this version
	 * @return	True if this version is on or after the given version
	 */
	public boolean includes(Version ver);
	
	/**
	 * Get the major version number
	 * @return	The major version number
	 */
	public int major();
	/**
	 * Get the minor version number
	 * @return	The minor version number
	 */
	public int minor();
	/**
	 * Get the point version number
	 * @return	The point version number.
	 */
	public int point();

}
