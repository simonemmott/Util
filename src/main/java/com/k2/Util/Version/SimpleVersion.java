package com.k2.Util.Version;

/**
 * This class is a simple implementation of the Version Interface
 * This is the implementation of the Interface that is retured by calls to the static create(...) methods of the interface
 * 
 * @author simon
 *
 */
public class SimpleVersion implements Version {
	
	private int major = 0;
	private int minor = 0;
	private int point = 0;
	
	/**
	 * Create an instance of the SimpleVersion setting the major, minor and point version numbers.
	 * @param major	The major version number
	 * @param minor	The minor version number
	 * @param point The point version number
	 */
	public SimpleVersion(int major, int minor, int point) {
		this.major = major;
		this.minor = minor;
		this.point = point;
	}
	
	@Override
	public int major() { return major; }
	@Override
	public int minor() { return minor; }
	@Override
	public int point() { return point; }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + major;
		result = prime * result + minor;
		result = prime * result + point;
		return result;
	}

	/**
	 * Check for version equality based on the major, minor and point version numbers only.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if ( ! (obj instanceof Version))
			return false;
		Version other = (Version) obj;
		if (major != other.major())
			return false;
		if (minor != other.minor())
			return false;
		if (point != other.point())
			return false;
		return true;
	}

	/**
	 * Return a sensible rendering of the version as a String, e.g. "v1.2.3"
	 */
	@Override
	public String toString() {
		return "v" + major + "." + minor + "." + point;
	}

	/**
	 * Increment the version by one
	 */
	@Override
	public void increment(Increment inc) {
		switch(inc) {
		case MAJOR:
			major++;
			minor=0;
			point=0;
			break;
		case MINOR:
			minor++;
			point=0;
			break;
		case POINT:
			point++;
			break;
		default:
			break;
		}
		
	}

	@Override
	public boolean includes(Version ver) {
		if (ver.major() < major) return true;
		if (ver.major() > major) return false;
		if (ver.minor() < minor) return true;
		if (ver.minor() > minor) return false;
		if (ver.point() < point) return true;
		if (ver.point() > point) return false;
		if (equals(ver)) return true;
		return false;
	}
	
	


}
