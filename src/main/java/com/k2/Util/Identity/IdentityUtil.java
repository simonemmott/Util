package com.k2.Util.Identity;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.IdClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.k2.Util.classes.ClassUtil;
import com.k2.Util.entity.EntityUtil;
import com.k2.Util.exceptions.UtilityError;

/**
 * The identity utility provides static methods for handling id values for objects.
 * 
 * To reduce the overhead of reflection a static map is held of the id fields for each class. This map is populated for automatically
 * when the identify value is got or set for an object.
 * 
 * @author simon
 *
 */
public class IdentityUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * The static map of identity fields for each class.
	 */
	private static Map<Class<?>, Member> identityMembersMap = new HashMap<Class<?>, Member>();
	
	/**
	 * This internal method extracts from class the field holding the id value.
	 * 
	 * Identity fields are identified by the javax.persistence.Id annotation. If the annotation is not
	 * present then a field named 'id' irrespective of case that holds a serializable value is used.
	 * 
	 * The if the class does not define an id field then the classes super class is checked and
	 * the id field returned from it if it can be found.
	 * 
	 * @param cls	The class from which to extract the identify field.
	 * @return	The field holding the id value. If not suitable field is can be identified then null is returned.
	 */
	static Member getIdMember(Class<?> cls) {
		return EntityUtil.getIdMember(cls);
	}

	/**
	 * This internal method extracts from class the field holding the identity value.
	 * 
	 * Identity fields are identified by the Identity annotation. If the annotation is not
	 * present then a Serializable field named 'identity' or a String field 'alias' or 'name' or 'title'
	 * irrespective of case  is used.
	 * If multiple fields match the above criteria then the first field in the above order is returned
	 * 
	 * The if the class does not define an identity field then the classes super class is checked and
	 * the identity field returned from it if it can be found.
	 * 
	 * @param cls	The class from which to extract the identify field.
	 * @return	The field holding the identity value. If not suitable field is can be identified then null is returned.
	 */
	private static Member getIdentityMember(Class<?> cls) {
		if (cls == null) return null;
		Member identity = identityMembersMap.get(cls);
		if (identity != null) return identity;
		
		for (Method m : cls.getDeclaredMethods()) {
			if (m.isAnnotationPresent(Identity.class)) {
				identityMembersMap.put(cls, m);
				return m;
			}
		}
		
		for (Field f : cls.getDeclaredFields()) {
			if (f.isAnnotationPresent(Identity.class)) {
				identityMembersMap.put(cls, f);
				return f;
			}
		}
		
		identity = getIdentityMember(cls.getSuperclass());
		if (identity != null) return identity;

		String[] identityAliases = {"identity", "alias", "name", "title"};
		for (String alias : identityAliases ) {
			Member m = ClassUtil.getGetterMember(cls, String.class, alias);
			if (m != null) {
				identityMembersMap.put(cls, m);
				return m;
			}
		}
		
		return null;


	}
	
	public static String getIdentity(Object obj) {
		return getIdentity(obj, obj.getClass().getSimpleName());
	}
	/**
	 * This method gets the serializable id value from an object.
	 * 
	 * If no identity field is identifiable in the class or its super classes then the given default value is returned
	 * 
	 * @param obj	The object for which to extract the id value
	 * @param defaultVal The default value to return if a suitable id value cannot be found
	 * @return	The id value for the object. If no id field can be identified then the objects simple class name is returned
	 */
	public static String getIdentity(Object obj, String defaultVal) {
		
		if (obj == null) return null;
		
		if (Identified.class.isAssignableFrom(obj.getClass())) return ((Identified)obj).getIdentity();
		
		Member identity = getIdentityMember(obj.getClass());
		
		if (identity != null) {
			if (identity instanceof Method) {
				Method m = (Method)identity;
				try {
					return (String) m.invoke(obj);
				} catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
					logger.error("Unable to execute method {}.{}", e, obj.getClass().getName(), m.getName());
				}
			} else if (identity instanceof Field) {
				Field f = (Field)identity;
				try {
					return (String) f.get(obj);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					logger.error("Unable to get value frpm field {}.{}", e, obj.getClass().getName(), f.getName());
				}
			}
		} 
		
		try {
			return getId(obj).toString();
		} catch (Throwable e) {
			return defaultVal;
		}
		
	}
	
	public static Serializable getId(Object obj) {
		return EntityUtil.getId(obj);
	}
	
	/**
	 * Convert the given key into a string
	 * @param key	The key to express as a String
	 * @return	The string representation of the given key
	 */
	public static String toString(Serializable key) {
		return EntityUtil.toString(key);
	}
	/**
	 * This static method converts the given string representation of a key into an instance of the given key class and 
	 * @param keyCls		The class of the key that is to be generatated from the string representation of the key
	 * @param keyStr		The string representation of the key
	 * @return			An instance of the key class generated from the string representation of the key
	 */
	public static Serializable toKey(Class<? extends Serializable> keyCls, String keyStr) {
		return EntityUtil.toKey(keyCls, keyStr);
	}
	/**
	 * This method returns the Serializable class of the Id field of the given class
	 * @param cls	The class for which the class of the Id field is required
	 * @return		The class that is used to represent the Id of the given class
	 */
	public static Class<? extends Serializable> getIdFieldType(Class<?> cls) {
		return EntityUtil.getIdFieldType(cls);
	}

	public static <E> Class<?> getKeyClass(Class<E> entityClass) {
		return EntityUtil.getKeyClass(entityClass);
	}

	public static Class<?> getBaseEntityClass(Class<?> entityClass) {
		return EntityUtil.getBaseEntityClass(entityClass);
	}
	

}
