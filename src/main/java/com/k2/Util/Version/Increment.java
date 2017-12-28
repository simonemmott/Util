package com.k2.Util.Version;

/**
 * This enumeration defines the various version number ordinals in a standard version number.
 * 
 * @author simon
 *
 */
public enum Increment {
	/**
	 * The major point number. Changes to the major point number indicate significant changes in the implemented logic of the versioned component.
	 * Typically though not necessarily backwards compatibility is not assured over a major version change.
	 */
	MAJOR,
	/**
	 * The minor point number. Changes to the minor point number indicates additional functionality being added without removing or altering existing functionality.
	 * Typically though not necessarily backwards compatibility is expected over a minor version change.
	 */
	MINOR,
	/**
	 * The point version number. Changes to the point version number indicate successive iterations of testing and or development to implement the designed functionality.
	 * Typically the highest point number for a minor version number is the stable release of the functionality associated with the minor version and earlier point numbers 
	 * Should be avoided. Often the final point release of a given minor version is labelled with an additional flag e.g. 'FINAL' Though this functionality is not
	 * supported with the Simple Version implementation
	 */
	POINT;
}
