package com.k2.Util.tuple;

import javax.persistence.Tuple;

public class Quadruple<A,B,C,D> extends Tuple4<A,B,C,D> implements Tuple {
	public Quadruple(Object ...values) {
		super(values);
	}
	
	public Quadruple(A a, B b, C c, D d) {
		super(a,b,c,d);
	}


}
