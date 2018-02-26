package com.k2.Util.classes;

import com.k2.Util.tuple.Pair;

/**
 * The dependency class encapsulates a java dependency, holding the package name and simple class name as an extension of the Pair tuple.
 * @author simon
 *
 */
public class Dependency extends Pair<String, String> implements Comparable<Dependency> {

	/**
	 * Create a dependency for the given package name and simple class name
	 * @param packageName	The package name
	 * @param className		The class name
	 */
	public Dependency(String packageName, String className) {
		super(packageName, className);
	}
	
	/**
	 * Create a dependency for the given class
	 * @param cls	The depends on class
	 */
	public Dependency(Class<?> cls) {
		super(cls.getPackage().getName(), cls.getSimpleName());
	}
	
	/**
	 * 
	 * @return The package name for this dependency
	 */
	public String getPackageName() { return a; }
	/**
	 * 
	 * @return	The simple class name for this dependency
	 */
	public String getClassName() { return b; }
	
	/**
	 * Get the class for this dependency
	 * @return	The class for this dependency
	 * @throws ClassNotFoundException	If the class does not exist
	 */
	public Class<?> getDependencyClass() throws ClassNotFoundException {
		return Class.forName(getPackageName()+"."+getClassName());
	}
	
	/**
	 * 
	 * @return	The java import clause for this dependency
	 */
	public String getImportClause() {
		return "import "+getPackageName()+"."+getClassName()+";";
	}

	/**
	 * Dependencies are comparable using the comparability of the import clause string
	 */
	@Override
	public int compareTo(Dependency o) {
		return getImportClause().compareTo(o.getImportClause());
	}
	
	
}
