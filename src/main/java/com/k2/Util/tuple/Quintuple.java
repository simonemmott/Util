package com.k2.Util.tuple;

import javax.persistence.Tuple;

public class Quintuple<A,B,C,D,E> extends Tuple5<A,B,C,D,E> implements Tuple {
	public Quintuple(Object ...values) {
		super(values);
	}
	
	public Quintuple(A a, B b, C c, D d, E e) {
		super(a,b,c,d,e);
	}


}
