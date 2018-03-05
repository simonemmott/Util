package com.k2.Util.Version;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticVersion extends VersionAndBuild implements Version, BuildNumber {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public StaticVersion(int major, int minor, int point, int build) {
		super(major, minor, point, build);
	}

	@Override
	public void increment(Increment inc) {
		logger.warn("Static versions cannot be incremented. Nothing done!");
	}

}
