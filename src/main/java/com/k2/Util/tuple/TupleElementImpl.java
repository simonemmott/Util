package com.k2.Util.tuple;

import javax.persistence.TupleElement;

import com.k2.Util.StringUtil;

public class TupleElementImpl<X> implements TupleElement<X> {

	private final String alias;
	private final Class<? extends X> type;
	public TupleElementImpl(String alias, Class<? extends X> cls) {
		this.alias = alias;
		this.type = cls;
	}
	
	public TupleElementImpl(Class<? extends X> cls) {
		this.alias = null;
		this.type = cls;
	}
	
	@Override
	public String getAlias() { return alias; }
	@Override
	public Class<? extends X> getJavaType() { return type; }

	@Override
	public String toString() {
		String aliasClause = (StringUtil.isSet(alias)) ? "["+alias+"]" : "[]";
		return type.getCanonicalName()+aliasClause;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		result = prime * result + ((type == null) ? 0 : type.getCanonicalName().hashCode());
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
		TupleElementImpl other = (TupleElementImpl) obj;
		if (alias == null) {
			if (other.alias != null)
				return false;
		} else if (!alias.equals(other.alias))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.getCanonicalName().equals(other.type.getCanonicalName()))
			return false;
		return true;
	}

	
}
