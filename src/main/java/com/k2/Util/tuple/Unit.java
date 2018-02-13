package com.k2.Util.tuple;

import javax.persistence.Tuple;

public class Unit<A> extends Tuple1<A> implements Tuple {
	public Unit(Object ...values) {
		super(values);
	}
	
	public Unit(A a) {
		super(a);
	}


}
