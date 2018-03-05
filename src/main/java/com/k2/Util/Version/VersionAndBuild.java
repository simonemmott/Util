package com.k2.Util.Version;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VersionAndBuild extends SimpleVersion implements Version, BuildNumber {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private int build;

	public VersionAndBuild(int major, int minor, int point, int build) {
		super(major, minor, point);
		this.build = build;
	}

	@Override
	public int buildNumber() {
		return build;
	}
	
}
