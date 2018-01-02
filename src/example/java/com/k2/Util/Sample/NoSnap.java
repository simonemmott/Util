package com.k2.Util.Sample;

import com.k2.Util.Identity.Id;

public class NoSnap implements Id<NoSnap, String> {

	public String aw;

	@Override
	public String getId() { return aw; }

	@Override
	public NoSnap setId(String key) {
		aw = key;
		return this;
	}

}
