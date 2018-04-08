package com.k2.Util.classes;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.k2.Util.StringUtil;

/**
 * The Dependencies class represents a set distinct dependencies
 * 
 * @author simon
 *
 */
public class Dependencies {
	
	private String forName;
	private String packageName;
	private Package pack;
	public Dependencies() {}
	private Dependencies(String forName) { 
		this.forName = forName; 
		this.packageName = ClassUtil.getPackageNameFromCanonicalName(forName);
		if (StringUtil.isSet(packageName))
			pack = Package.getPackage(packageName);
	}
	public static Dependencies forName(String name) { return new Dependencies(name); }

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
	
	public boolean requiresImport(String name) {
		if (name == null) 
			return false;
		if (		name.equals("int")||
				name.equals("long")||
				name.equals("boolean")||
				name.equals("float")||
				name.equals("double")||
				name.equals("char")||
				name.equals("short")||
				name.equals("byte")|| 
				name.equals("void")) 
			return false;
		if (name.startsWith("java.lang.")) 
			return false;
		if (packageName != null && packageName.equals(ClassUtil.getPackageNameFromCanonicalName(name))) 
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
				if (requiresImport(cls,pack))
					dependencies.add(new Dependency(cls));
			}
		}
	}
	
	/**
	 * Add all the dependencies from the given set of dependencies
	 * @param ds		The set of dependencies to add to these dependencies
	 */
	public void addAll(Set<Dependency> ds) {
		for (Dependency d : ds) 
			add(d);
//		dependencies.addAll(ds);
	}
	
	public void add(Dependency d) {
		if (requiresImport(d.getName()))
			dependencies.add(d);
	}
	
	/**
	 * 
	 * @return Get the maintained set of dependencies ordered alphabetically
	 */
	public Set<Dependency> getDependencies() { return dependencies; }
	
}
