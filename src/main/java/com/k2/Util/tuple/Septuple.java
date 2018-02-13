package com.k2.Util.tuple;

import javax.persistence.Tuple;

public class Septuple<A,B,C,D,E,F,G> extends Tuple7<A,B,C,D,E,F,G> implements Tuple {
	public Septuple(Object ...values) {
		super(values);
	}
	
	public Septuple(A a, B b, C c, D d, E e, F f, G g) {
		super(a,b,c,d,e,f,g);
	}

}
