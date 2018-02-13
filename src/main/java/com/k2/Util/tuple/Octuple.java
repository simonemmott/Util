package com.k2.Util.tuple;

import javax.persistence.Tuple;

public class Octuple<A,B,C,D,E,F,G,H> extends Tuple8<A,B,C,D,E,F,G,H> implements Tuple {
	public Octuple(Object ...values) {
		super(values);
	}
	
	public Octuple(A a, B b, C c, D d, E e, F f, G g, H h) {
		super(a,b,c,d,e,f,g,h);
	}
}
