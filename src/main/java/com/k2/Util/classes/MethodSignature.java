package com.k2.Util.classes;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import com.k2.Util.exceptions.UtilityError;

/**
 * The method signature encapsulates the signature of a method and provides hashcode and equality methods that return true if the java method 
 * signature of one method is that same as another method signature.
 * 
 * @author simon
 *
 */
public class MethodSignature implements Comparable<MethodSignature>{

	
	private final String name;
	private final Class<?>[] parameterTypes;
	private final Parameter[] parameters;
	private final Method meth;
	
	private MethodSignature(Method meth) {
		this.name = meth.getName();
		this.parameterTypes = meth.getParameterTypes();
		this.parameters = meth.getParameters();
		this.meth = meth;
	}
	
	private MethodSignature(String name, Class<?> ... parameterTypes) {
		this.name = name;
		this.parameterTypes = parameterTypes;
		this.meth = null;
		this.parameters = null;
	}
	
	/**
	 * Create a method signature for a given method.
	 * @param meth	The method from which the signature is required
	 * @return The method signature for the given method
	 */
	public static MethodSignature forMethod(Method meth) { return new MethodSignature(meth); }

	/**
	 * Create a method signature for the given method and array of parameter types
	 * @param name				The name of the method
	 * @param parameterTypes		The types of the methods parameters
	 * @return The method signature for the given method name and parameter types
	 */
	public static MethodSignature forSignature(String name, Class<?> ... parameterTypes) { return new MethodSignature(name, parameterTypes); }
	
	/**
	 * Get the name of the method
	 * @return	The method name
	 */
	public String getName() { return name; }

	/**
	 * Get the types of the methods parameters
	 * @return	The methods parameter types
	 */
	public Class<?>[] getParameterTypes() { return parameterTypes; }

	/**
	 * Get the method for this signature if it was created from a method
	 * @return	The method used to create this signature
	 */
	public Method getMethod() { return meth; }

	/**
	 * Get the method for this signature as defined by the given class
	 * @param cls	The class from which the method is required
	 * @return		The method object for this method signature
	 */
	public Method getMethod(Class<?> cls) { 
		if (meth != null && meth.getDeclaringClass().equals(cls)) return meth;
		return ClassUtil.getMethod(cls, this); 
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Arrays.hashCode(parameterTypes);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return equals(obj, true);
	}
	
	public boolean equals(Object obj, boolean strict) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MethodSignature other = (MethodSignature) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parameterTypes.length != other.parameterTypes.length) return false;
		for (int i=0; i<parameterTypes.length; i++) {
			if (strict) {
				if (parameterTypes[i] != other.parameterTypes[i]) return false;
			} else {
				if (!parameterTypes[i].isAssignableFrom(other.parameterTypes[i])) return false;
			}
		}
		return true;
	}
	
	/**
	 * Get the dependnecies implied by this methods signature
	 * @return	The set of dependencies identified by this method signature
	 */
	public Set<Dependency> getDependencies() {
		if (meth == null) throw new UtilityError("You can only get the dependencies from a MethodSignature that was instantiated from a method.");
		Set<Dependency> dependencies= new TreeSet<Dependency>();
		if (Dependencies.requiresImport(meth.getReturnType())) 
			dependencies.add(new Dependency(meth.getReturnType()));
		for (Class<?> type : parameterTypes) 
			if (Dependencies.requiresImport(type)) 
				dependencies.add(new Dependency(meth.getReturnType()));
		return dependencies;
	}
	
	/**
	 * 
	 * @return True if this method has parameters
	 */
	public boolean hasParameters() { return (parameterTypes.length > 0); }
	
	/**
	 * 
	 * @return True if this method returns a value
	 */
	public boolean returnsValue() { 
		if (meth != null) return (meth.getReturnType() != void.class);
		return false; 
	}
	
	/**
	 * 
	 * @return	If this method returns a value then the Simple name of the returned value
	 */
	public String getReturnsClause() {
		if (meth == null) return "";
		return meth.getReturnType().getSimpleName();
	}
	
	/**
	 * Get an comma separated string of the parameter names of this method
	 * @return	A comma separated string of the parameter names of this method
	 */
	public String getParameterNamesClause() {
		if (parameters == null) return "...";
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<parameters.length; i++) {
			sb.append(parameters[i].getName());
			if (i < parameters.length - 1)
				sb.append(", ");
		}
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append("(");
		for (int i=0; i<parameterTypes.length; i++) {
			sb.append(parameterTypes[i].getSimpleName());
			if (i < parameterTypes.length - 1)
				sb.append(", ");
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * 
	 * @return The java source representation of this method signature without the return clause
	 */
	public String getMethodSignature() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append("(");
		for (int i=0; i<parameterTypes.length; i++) {
			sb.append(parameterTypes[i].getSimpleName()).append(" ").append(parameters[i].getName());
			if (i < parameterTypes.length - 1)
				sb.append(", ");
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * Method signatures are comparable by the comparable string from 'getMethodSignature()'
	 */
	@Override
	public int compareTo(MethodSignature o) {
		return getMethodSignature().compareTo(o.getMethodSignature());
	}

	
	
	
	

}
