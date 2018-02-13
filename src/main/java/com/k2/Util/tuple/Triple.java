package com.k2.Util.tuple;

import javax.persistence.Tuple;

public class Triple<A,B,C> extends Tuple3<A,B,C> implements Tuple {
	public Triple(Object ...values) {
		super(values);
	}
	
	public Triple(A a, B b, C c) {
		super(a,b,c);
	}


}
