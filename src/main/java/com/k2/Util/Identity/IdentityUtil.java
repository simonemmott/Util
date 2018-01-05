package com.k2.Util.Identity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

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
	
	/**
	 * The static map of id fields for each class.
	 */
	private static Map<Class<?>, Field> idFieldsMap = new HashMap<Class<?>, Field>();
	/**
	 * The static map of identity fields for each class.
	 */
	private static Map<Class<?>, Field> identityFieldsMap = new HashMap<Class<?>, Field>();
	
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
	private static Field getIdField(Class<?> cls) {
		if (cls == null) return null;
		Field id = idFieldsMap.get(cls);
		if (id != null) return id;
		
		for (Field f : cls.getDeclaredFields()) {
			if (f.isAnnotationPresent(javax.persistence.Id.class)) {
				idFieldsMap.put(cls, f);
				return f;
			}
		}
		
		id = getIdField(cls.getSuperclass());
		if (id != null) return id;

		for (Field f : cls.getDeclaredFields()) {
			if (Serializable.class.isAssignableFrom(f.getType()) && f.getName().equalsIgnoreCase("id")) {
				idFieldsMap.put(cls, f);
				return f;
			}
		}
		
		return null;


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
	private static Field getIdentityField(Class<?> cls) {
		if (cls == null) return null;
		Field identity = identityFieldsMap.get(cls);
		if (identity != null) return identity;
		
		for (Field f : cls.getDeclaredFields()) {
			if (f.isAnnotationPresent(Identity.class)) {
				identityFieldsMap.put(cls, f);
				return f;
			}
		}
		
		identity = getIdentityField(cls.getSuperclass());
		if (identity != null) return identity;

		for (Field f : cls.getDeclaredFields()) {
			if (Serializable.class.isAssignableFrom(f.getType()) && f.getName().equalsIgnoreCase("identity")) {
				identityFieldsMap.put(cls, f);
				return f;
			}
		}
		for (Field f : cls.getDeclaredFields()) {
			if (String.class.isAssignableFrom(f.getType()) && f.getName().equalsIgnoreCase("alias")) {
				identityFieldsMap.put(cls, f);
				return f;
			}
		}
		for (Field f : cls.getDeclaredFields()) {
			if (String.class.isAssignableFrom(f.getType()) && f.getName().equalsIgnoreCase("name")) {
				identityFieldsMap.put(cls, f);
				return f;
			}
		}
		for (Field f : cls.getDeclaredFields()) {
			if (String.class.isAssignableFrom(f.getType()) && f.getName().equalsIgnoreCase("title")) {
				identityFieldsMap.put(cls, f);
				return f;
			}
		}
		
		return null;


	}

	/**
	 * This method gets the serializable id value from an object.
	 * 
	 * If no identity field is identifiable in the class or its super classes then the given default value is returned
	 * 
	 * @param obj	The object for which to extract the id value
	 * @param The default value to return if a suitable id value cannot be found
	 * @return	The id value for the object. If no id field can be identified then the objects simple class name is returned
	 * @throws IllegalAccessException	If the object is enforcing Java language access control, and the underlying field is inaccessible
	 */
	public static String getIdentity(Object obj, String defaultVal) throws IllegalAccessException {
		
		if (obj == null) return null;
		
		if (Identified.class.isAssignableFrom(obj.getClass())) return ((Identified)obj).getIdentity();
		
		Field identity = getIdentityField(obj.getClass());
		
		if (identity == null) return getId(obj, defaultVal).toString();
		
		return identity.get(obj).toString();
		
	}
	/**
	 * This method gets the serializable id value from an object.
	 * 
	 * If no id field is identifiable in the class or its super classes then the name of the class is returned
	 * 
	 * @param obj	The object for which to extract the id value
	 * @param The default value to return if a suitable id value cannot be found
	 * @return	The id value for the object. If no id field can be identified then the objects simple class name is returned
	 * @throws IllegalAccessException	If the object is enforcing Java language access control, and the underlying field is inaccessible
	 */
	public static Serializable getId(Object obj, Serializable defaultVal) throws IllegalAccessException {
		
		if (obj == null) return null;
		
		if (Id.class.isAssignableFrom(obj.getClass())) return ((Id<?,?>)obj).getId();
		
		Field id = getIdField(obj.getClass());
		
		if (id == null) return defaultVal;
		
		return (Serializable) id.get(obj);
		
	}
	
	/**
	 * This method gets the serializable id value from an object.
	 * 
	 * If no id field is identifiable in the class or its super classes then the name of the class is returned
	 * 
	 * @param obj	The object for which to extract the id value
	 * @return	The id value for the object. If no id field can be identified then the objects simple class name is returned
	 * @throws IllegalAccessException	If the object is enforcing Java language access control, and the underlying field is inaccessible
	 */
	public static Serializable getId(Object obj) throws IllegalAccessException {
		return getId(obj, obj.getClass().getSimpleName());
	}
	

}
