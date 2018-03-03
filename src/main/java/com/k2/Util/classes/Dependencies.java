package com.k2.Util.classes;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * The Dependencies class represents a set distinct dependencies
 * 
 * @author simon
 *
 */
public class Dependencies {

	private Set<Dependency> dependencies= new TreeSet<Dependency>();
	
	/**
	 * @param cls	The class to check
	 * @return	True if the given class needs to be included as an import clause
	 */
	public static boolean requiresImport(Class<?> cls) {
		return requiresImport(cls,null);
	}
	
	public static boolean requiresImport(Class<?> cls, Package pack) {
		if (cls == null) 
			return false;
		if (cls.isPrimitive()) 
			return false;
		if (pack != null && cls.getPackage().equals(pack)) 
			return false;
		if (cls.getName().startsWith("java.lang.")) 
			return false;
		return true;
	}
	
	/**
	 * Add the given classes to the maintained set of dependencies 
	 * @param classes	The classes to add to the set of dependencies
	 */
	public void add(Class<?> ... classes) {
		for (Class<?> cls : classes) {
			if (cls != null) {
				dependencies.add(new Dependency(cls));
			}
		}
	}
	
	/**
	 * Add all the dependencies from the given set of dependencies
	 * @param ds		The set of dependencies to add to these dependencies
	 */
	public void addAll(Set<Dependency> ds) {
		dependencies.addAll(ds);
	}
	
	/**
	 * 
	 * @return Get the maintained set of dependencies ordered alphabetically
	 */
	public Set<Dependency> getDependencies() { return dependencies; }
	
}
