package com.k2.Util.tuple;

import javax.persistence.Tuple;


public class Tuple3<A,B,C> extends AbstractTuple implements Tuple {

	public final A a;
	public final B b;
	public final C c;
	
	public int size() { return 3; }
	
	@Override
	public Object[] toArray() {
		Object[] arr = {a,b,c};
		return arr;
	}

	@SuppressWarnings("unchecked")
	public Tuple3(Object ...values) {
		this.a = (A)values[0];
		this.b = (B)values[1];
		this.c = (C)values[2];
	}
	
	public Tuple3(A a, B b, C c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		result = prime * result + ((c == null) ? 0 : c.hashCode());
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
		Tuple3 other = (Tuple3) obj;
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
		if (c == null) {
			if (other.c != null)
				return false;
		} else if (!c.equals(other.c))
			return false;
		return true;
	}

	

}
