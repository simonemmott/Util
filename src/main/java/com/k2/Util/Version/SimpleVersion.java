package com.k2.Util.Version;

public class SimpleVersion implements Version {
	
	public int major = 0;
	public int minor = 0;
	public int point = 0;
	
	public SimpleVersion(int major, int minor, int point) {
		this.major = major;
		this.minor = minor;
		this.point = point;
	}
	
	public int major() { return major; }
	public int minor() { return minor; }
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleVersion other = (SimpleVersion) obj;
		if (major != other.major)
			return false;
		if (minor != other.minor)
			return false;
		if (point != other.point)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "v" + major + "." + minor + "." + point;
	}

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
