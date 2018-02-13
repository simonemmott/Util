package com.k2.Util.tuple;

import javax.persistence.Tuple;

public class Pair<A,B> extends Tuple2<A,B> implements Tuple {
	public Pair(Object ...values) {
		super(values);
	}
	
	public Pair(A a, B b) {
		super(a,b);
	}

}
