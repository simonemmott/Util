package com.k2.Util.nullable;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.Util.classes.ClassUtil;
import com.k2.Util.classes.ClassUtil.AnnotationCheck;
import com.k2.Util.exceptions.UtilityError;

/**
 * The Null utility provides methods for handling null values
 * 
 * The Null class maintains a static map of Instances of objects that represent a Null value but are not actually null. Thus preventing calls to the methods of the 
 * 'null' value from throwing a NPE
 * 
 * @author simon
 *
 */
public class Null {
	
	public enum RegisterType {
		IGNORE,
		WARNING,
		FAIL
	}

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private static Map<Class<?>, Object> nulls = new HashMap<Class<?>, Object>();
	
	/**
	 * This method cases the Null utility to generate a 'null' value for each of the given classes
	 * @param rType	How to handle existing 'null' values when registering null values a given class
	 * @param cls	The class that should have a null instance registered with the Null utility
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <C> C registerClass(RegisterType rType, Class<C> cls) {
		if (!nulls.containsKey(cls)) {
			C nll = null;
			try {
				nll = cls.newInstance();
				nulls.put(cls, nll);
			} catch (InstantiationException | IllegalAccessException e) {
				switch(rType) {
				case FAIL:
					throw new UtilityError("Unable to create instance of {}, no public zero arg consructor", e, cls.getName());
				case WARNING:
					logger.warn("Unable to create instance of {}, no public zero arg consructor", e, cls.getName());
					break;
				default:
					throw new UtilityError("Unable to create instance of {}, no public zero arg consructor", e, cls.getName());
				}	
			}
			return nll;
		} else {
			switch(rType) {
			case FAIL:
				throw new UtilityError("The class {} has already been registered. Using regisered null value", cls.getName());
			case WARNING:
				logger.warn("The class {} has already been registered. Using regisered null value", cls.getName());
				break;
			default:
				logger.warn("The class {} has already been registered. Using regisered null value", cls.getName());
				break;
			}
			return (C) nulls.get(cls);
		}
	}
	
	/**
	 * Create and register null instances for each of the given classes
	 * @param classes	The classes for which to register null instances
	 */
	public static void registerClasses(Class<?> ... classes) {
		for (Class<?> cls : classes) {
			registerClass(null, cls);
		}
	}
	
	/**
	 * Create and register null instances for each of the given classes using the given RegisterType
	 * @param rType		The registration type
	 * @param classes	The classes for which to register a null value
	 */
	public static void registerClasses(RegisterType rType, Class<?> ... classes) {
		for (Class<?> cls : classes) {
			registerClass(rType, cls);
		}
	}
	
	/**
	 * Create and register null instances for all the Entity classes in the list of package names
	 * @param packageNames	A list of package names to scan for classes for which to register null values
	 */
	public static void registerEntities(String ... packageNames) {
		for (String packageName : packageNames) {
			registerClasses(null, ClassUtil.getClasses(packageName, AnnotationCheck.ANY, Entity.class, Embeddable.class));
		}
	}

	/**
	 * Create and register null instances for all the Entity classes in the list of given packages
	 * @param rType			The registration type under which to register the entity classes
	 * @param packageNames	The package names to scan for entity classes
	 */
	public static void registerEntities(RegisterType rType, String ... packageNames) {
		for (String packageName : packageNames) {
			registerClasses(rType, ClassUtil.getClasses(packageName, AnnotationCheck.ANY, Entity.class, Embeddable.class));
		}
	}

	/**
	 * Create an register null instances for all the classes in the list of given package names
	 * @param packageNames	The list of package names to scan for classes
	 */
	public static void registerAll(String ... packageNames) {
		for (String packageName : packageNames) {
			registerClasses(null, ClassUtil.getClasses(packageName));
		}
	}

	/**
	 * Create an register null instances for all the classes in the list of given package names
	 * @param rType			The registration type under which to register the entity classes
	 * @param packageNames	The list of package names to scan for classes
	 */
	public static void registerAll(RegisterType rType, String ... packageNames) {
		for (String packageName : packageNames) {
			registerClasses(rType, ClassUtil.getClasses(packageName));
		}
	}

	/**
	 * Check whether the given object is null or represents a null value
	 * @param obj	The object to check for null
	 * @return		True if the object == null or if the object == the null instance
	 */
	public static boolean isNull(Object obj) {
		if (obj == null) return true;
		if (!nulls.containsKey(obj.getClass()))
			throw new UtilityError("Unable to ascertain whether the instance of class {} is null. The class has not been registered.", obj.getClass().getName());
		return (obj.equals(nulls.get(obj.getClass())));
	}
	
	/**
	 * Get the null instance for the given class
	 * @param cls	The class for which the null instance is required
	 * @return		The null instance for the given class
	 */
	@SuppressWarnings("unchecked")
	public static <C> C getNull(Class<C> cls) {
		C nll = (C) nulls.get(cls);
		if (nll == null) {
			nll = registerClass(RegisterType.WARNING, cls);
			if (nll == null)
				throw new UtilityError("Unable to get the null value for class {}. The class has not been registered.", cls.getName());
		}
		return nll;
	}

}
