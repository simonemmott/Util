package com.k2.Util.tuple;

import javax.persistence.Tuple;

public class Sextuple<A,B,C,D,E,F> extends Tuple6<A,B,C,D,E,F> implements Tuple {
	public Sextuple(Object ...values) {
		super(values);
	}
	
	public Sextuple(A a, B b, C c, D d, E e, F f) {
		super(a,b,c,d,e,f);
	}


}
