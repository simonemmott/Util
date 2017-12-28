package com.k2.Util.Version;

public interface Version {
	
	public static Version create() {
		return new SimpleVersion(0,0,0);
	}
	
	public static Version create(int major, int minor, int point) {
		return new SimpleVersion(major, minor, point);
	}
	
	public void increment(Increment inc);
	
	public boolean includes(Version ver);
	
	public int major();
	public int minor();
	public int point();

}
