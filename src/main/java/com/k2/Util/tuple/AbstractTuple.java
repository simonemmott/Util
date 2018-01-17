package com.k2.Util.tuple;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;

import com.k2.Util.StringUtil;

/**
 * This abstract class provides the abstractable methods of the Tuple interface
 * 
 * @author simon
 *
 */
public abstract class AbstractTuple implements Tuple {

	/**
	 * This abstract method allows implementing classes to define the number of members in the implemented tuple
	 * 
	 * @return	The number of members in the Tuple
	 */
	abstract int size();
	
	/**
	 * The array of aliases used to identify the members of this Tuple
	 */
	private String[] aliases = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	
	/**
	 * This method allows the aliases to to modified for the created Tuple
	 * @param aliases	The new set of aliases
	 */
	void setAliases(String ...aliases) {
		this.aliases = aliases;
	}
	
	/**
	 * This method identifies whether the given alias is in the set of aliases
	 * 
	 * @param alias	The alias to check for
	 * @return	True if the given alias is in the set of aliases for this tuple
	 */
	private boolean isAlias(String alias) {
		for (int i=0; i<size(); i++) {
			if (aliases[i].equals(alias)) return true;
		}
		return false;
	}
	
	/**
	 * This method returns the index of the given alias
	 * 	
	 * @param alias	The alias for which the index is required
	 * @return	The index of the given alias or -1 if the alias is not in the set of aliases
	 */
	private int getAliasIndex(String alias) {
		for (int i=0; i<size(); i++) {
			if (aliases[i].equals(alias)) return i;
		}
		return -1;
	}
	

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object obj);

	/**
	 * Get the value of the member matching the given TupleElement
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <X> X get(TupleElement<X> elm) {
		if (elm == null) return null;
		int i = 0;
		for (Object obj : toArray()) {
			String alias = aliases[i++];
			if (elm.getJavaType().equals(obj.getClass())) {
				if (elm.getAlias() != null) {
					if (elm.getAlias().equals(alias)) {
						return (X)obj;
					}
				} else {
					return (X)obj;
				}
			}
		}
		throw new IllegalArgumentException(StringUtil.replaceAll("No member found in this tuple for alias '{}' and class '{}'", "{}", elm.getAlias(), elm.getClass().getCanonicalName()));
	}

	/**
	 * Get the value of the member for the given alias
	 */
	@Override
	public Object get(String alias) {
		if (alias == null || !isAlias(alias)) throw new IllegalArgumentException(StringUtil.replaceAll("The given alias '{}' is not a member of this tuple", "{}", alias));
		return get(getAliasIndex(alias));
	}

	/**
	 * Get the value of the member for the given index
	 */
	@Override
	public Object get(int i) {
		if (i >= size()) throw new IllegalArgumentException(StringUtil.replaceAll("There are only {} members of this tuple, requested member at {}", "{}", size(), i));
		return toArray()[i];
	}

	/**
	 * Get the value of the member for the given alias and class
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <X> X get(String alias, Class<X> cls) {
		Object obj = get(alias);
		if (obj == null) return null;
		if (obj.getClass().isAssignableFrom(cls)) { 
			return (X)obj;
		} else {
			throw new IllegalArgumentException(StringUtil.replaceAll("The member with alias '{}' cannot be cast to {}", "{}", alias, cls.getCanonicalName()));
		}
	}

	/**
	 * Get the value of he member for the given index and class
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <X> X get(int i, Class<X> cls) {
		if (i >= size()) throw new IllegalArgumentException(StringUtil.replaceAll("There are only {} members of this tuple, requested member at {}", "{}", size(), i));
		Object obj = get(i);
		if (obj == null) return null;
		if (obj.getClass().isAssignableFrom(cls)) { 
			return (X)obj;
		} else {
			throw new IllegalArgumentException(StringUtil.replaceAll("The member at {} cannot be cast to {}", "{}", i, cls.getCanonicalName()));
		}
	}

	/**
	 * Get the list of TupleElements in this Tuple
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<TupleElement<?>> getElements() {
		List<TupleElement<?>> list = new ArrayList<TupleElement<?>>(size());
		int i = 0;
		for (Object obj : toArray()) {
			if (obj == null) {
				list.add(new TupleElementImpl(aliases[i++], Object.class));
			} else {
				list.add(new TupleElementImpl(aliases[i++], obj.getClass()));
			}
		}
		return list;
	}

}
