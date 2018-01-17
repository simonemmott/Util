package com.k2.Util.tuple;

import javax.persistence.Tuple;


public class Tuple2<A,B> extends AbstractTuple implements Tuple {

	public final A a;
	public final B b;
	
	public int size() { return 2; }
	
	@Override
	public Object[] toArray() {
		Object[] arr = {a,b};
		return arr;
	}

	@SuppressWarnings("unchecked")
	public Tuple2(Object ...values) {
		this.a = (A)values[0];
		this.b = (B)values[1];
	}
	
	public Tuple2(A a, B b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tuple2 other = (Tuple2) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		return true;
	}

	

}
