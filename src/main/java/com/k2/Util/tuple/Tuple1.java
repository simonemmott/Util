package com.k2.Util.tuple;

import javax.persistence.Tuple;


public class Tuple1<A> extends AbstractTuple implements Tuple {

	public final A a;
	
	public int size() { return 1; }
	
	@Override
	public Object[] toArray() {
		Object[] arr = {a};
		return arr;
	}

	@SuppressWarnings("unchecked")
	public Tuple1(Object ...values) {
		this.a = (A)values[0];
	}
	
	public Tuple1(A a) {
		this.a = a;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
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
		Tuple1 other = (Tuple1) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		return true;
	}

	

}
