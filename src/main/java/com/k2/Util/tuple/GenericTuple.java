package com.k2.Util.tuple;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;

import com.k2.Util.StringUtil;


public class GenericTuple implements Tuple {

	@SuppressWarnings("rawtypes")
	private Map<TupleElement, Object> members = new LinkedHashMap<TupleElement, Object>();
	
	@Override
	public Object[] toArray() {
		Object[] arr = new Object[members.size()];
		return members.values().toArray(arr);
	}

	public GenericTuple(Object ...values) {
		int i=0;
		for (Object obj : values) {
			TupleElement<?> elm = TupleUtil.tupleElement(StringUtil.indexToString(i++), obj.getClass());
			members.put(elm, obj);
		}
	}

	public GenericTuple(String[] aliases, Object ...values) {
		if (aliases.length < values.length) throw new IllegalArgumentException(StringUtil.replaceAll("The number of aliases '{}' must not be less then the number of values '{}'", "{}", aliases.length, values.length));
		for (int i=0; i<values.length; i++) {
			Object obj = values[i];
			TupleElement<?> elm = TupleUtil.tupleElement(aliases[i], obj.getClass());
			members.put(elm, obj);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <X> X get(TupleElement<X> elm) {
		Object obj = members.get(elm);
		if (obj == null) throw new IllegalArgumentException(StringUtil.replaceAll("The given element '{}' is not a member of this tuple", "{}", elm));
		if (elm.getJavaType().isAssignableFrom(obj.getClass())) return (X)obj;
		throw new IllegalArgumentException(StringUtil.replaceAll("The member identified by the given element '{}' is not of the expected type", "{}", elm));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object get(String alias) {
		if (alias == null) throw new IllegalArgumentException("Unable to find member for a null alias");
		for (Entry<TupleElement, Object> entry : members.entrySet()) {
			if (alias.equals(entry.getKey().getAlias())) {
				return entry.getValue();
			}
		}
		throw new IllegalArgumentException(StringUtil.replaceAll("The given alias '{}' is not a member of this tuple", "{}", alias));
	}

	@Override
	public Object get(int i) {
		if (i >= members.size()) throw new IllegalArgumentException(StringUtil.replaceAll("The given index '{}' is greater than the number of member of this tuple '{}'", "{}", i, members.size()));
		return members.values().toArray(new Object[0])[i];
	}

	@SuppressWarnings("unchecked")
	@Override
	public <X> X get(String alias, Class<X> cls) {
		TupleElement<X> elm = TupleUtil.tupleElement(alias, cls);
		Object obj = members.get(elm);
		if (obj == null) throw new IllegalArgumentException(StringUtil.replaceAll("The given alias '{}' and class '{}' is not a member of this tuple", "{}", alias, cls.getCanonicalName()));
		if (elm.getJavaType().isAssignableFrom(obj.getClass())) return (X)obj;
		throw new IllegalArgumentException(StringUtil.replaceAll("The member identified by the given element '{}' is not of the expected type", "{}", elm));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <X> X get(int i, Class<X> cls) {
		Object obj = get(i);
		if (obj == null) return null;
		if (cls.isAssignableFrom(obj.getClass())) return (X)obj;
		throw new IllegalArgumentException(StringUtil.replaceAll("The member at index '{}' is not of the expected type", "{}", i, cls.getCanonicalName()));
	}

	@Override
	public List<TupleElement<?>> getElements() {
		List<TupleElement<?>> list = new ArrayList<TupleElement<?>>();
		for (TupleElement<?> elm : members.keySet()) list.add(elm);
		return list;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((members == null) ? 0 : members.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericTuple other = (GenericTuple) obj;
		if (members == null) {
			if (other.members != null)
				return false;
		} else if (!members.equals(other.members))
			return false;
		return true;
	}
	



	

}
