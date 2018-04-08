package com.k2.Util.classes;

import com.k2.Util.StringUtil;
import com.k2.Util.tuple.Pair;

/**
 * The dependency class encapsulates a java dependency, holding the package name and simple class name as an extension of the Pair tuple.
 * @author simon
 *
 */
public class Dependency extends Pair<String, String> implements Comparable<Dependency> {
	
	public static Dependency forClass(Class<?> cls) { return new Dependency(cls); }
	public static Dependency fromString(String name) { 
		return new Dependency(ClassUtil.getPackageNameFromCanonicalName(name), ClassUtil.getBasenameFromCanonicalName(name)); 
	}
	public static Dependency fromString(String packageName, String className) { 
		return new Dependency(packageName, className); 
	}

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
		super((cls==null)?"":(cls.getPackage()==null)?"":cls.getPackage().getName(), (cls==null)?"":cls.getSimpleName());
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
	 * @return Get the canonical name of the dependency
	 */
	public String getName() { return (StringUtil.isSet(a))?a+"."+b:b; }
	
	/**
	 * Get the class for this dependency
	 * @return	The class for this dependency
	 * @throws ClassNotFoundException	If the class does not exist
	 */
	public Class<?> getDependencyClass() throws ClassNotFoundException {
		return Class.forName(getName());
	}
	
	/**
	 * 
	 * @return	The java import clause for this dependency
	 */
	public String getImportClause() {
		return "import "+getName()+";";
	}

	/**
	 * Dependencies are comparable using the comparability of the import clause string
	 */
	@Override
	public int compareTo(Dependency o) {
		return getImportClause().compareTo(o.getImportClause());
	}
	
	
}
