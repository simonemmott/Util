package com.k2.Util.Identity;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.IdClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.Util.classes.ClassUtil;
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
	 * The static map of id fields for each class.
	 */
	private static Map<Class<?>, Member> idMembersMap = new HashMap<Class<?>, Member>();
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
	private static Member getIdMember(Class<?> cls) {
		if (cls == null) return null;
		Member id = idMembersMap.get(cls);
		if (id != null) return id;
		
		for (Field f : ClassUtil.getDeclaredFields(cls)) {
			if (f.isAnnotationPresent(javax.persistence.Id.class) || f.isAnnotationPresent(javax.persistence.EmbeddedId.class)) {
				if (f.getType() instanceof Serializable) {
					idMembersMap.put(cls, f);
					return f;
				} else {
					throw new UtilityError("The annotated id field {}.{} is not Serializable", cls.getName(), f.getName());
				}
			}
		}
		
		for (Method m : ClassUtil.getDeclaredMethods(cls)) {
			if (m.isAnnotationPresent(javax.persistence.Id.class) || m.isAnnotationPresent(javax.persistence.EmbeddedId.class)) {
				if (m.getReturnType() instanceof Serializable) {
					if (m.getParameterCount() == 0) {
						idMembersMap.put(cls, m);
						return m;
					} else {
						throw new UtilityError("The annotated id method {}.{} is not a zero arg method", cls.getName(), m.getName());
					}
				} else {
					throw new UtilityError("The annotated id method {}.{} does not return a Serializable value", cls.getName(), m.getName());
				}
			}
		}
		
		id = getIdMember(cls.getSuperclass());
		if (id != null) return id;

		for (Field f : cls.getDeclaredFields()) {
			if (Serializable.class.isAssignableFrom(f.getType()) && f.getName().equalsIgnoreCase("id")) {
				idMembersMap.put(cls, f);
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
		
		return getId(obj, defaultVal).toString();
		
	}
	
	@SuppressWarnings("rawtypes")
	private static Map<Class, EntityToKey> keyGenerators = new HashMap<Class, EntityToKey>();
	/**
	 * This method gets the serializable id value from an object.
	 * 
	 * If no id field is identifiable in the class or its super classes then the name of the class is returned
	 * 
	 * @param obj	The object for which to extract the id value
	 * @param defaultVal The default value to return if a suitable id value cannot be found
	 * @return	The id value for the object. If no id field can be identified then the objects simple class name is returned
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Serializable getId(Object obj, Serializable defaultVal) {
		
		if (obj == null) return null;
		
		if (Id.class.isAssignableFrom(obj.getClass())) return ((Id<?,?>)obj).getId();
		
		EntityToKey etk = keyGenerators.get(obj.getClass());
		if (etk == null) {
			etk = EntityToKey.forClass(obj.getClass());
			keyGenerators.put(obj.getClass(), etk);
		}
		return (Serializable) etk.getKey(obj);
/*		
		Member id = getIdMember(obj.getClass());
		
		if (id == null) return defaultVal;
		
		if (id instanceof Field) {
			Field f = (Field)id;
			if (!f.isAccessible()) f.setAccessible(true);
			try {
				return (Serializable) f.get(obj);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error("Unable to get value from id field {}.{}", e, obj.getClass().getName(), f.getName());
			}
		} else if (id instanceof Method) {
			Method m = (Method)id;
			if (!m.isAccessible()) m.setAccessible(true);
			try {
				return (Serializable) m.invoke(obj);
			} catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
				logger.error("Unable to invoke the id method {}.{}", e, obj.getClass().getName(), m.getName());
				e.printStackTrace();
			}
		}
		
		return defaultVal;
*/		
	}
	
	/**
	 * This method gets the serializable id value from an object.
	 * 
	 * If no id field is identifiable in the class or its super classes then the name of the class is returned
	 * 
	 * @param obj	The object for which to extract the id value
	 * @return	The id value for the object. If no id field can be identified then the objects simple class name is returned
	 */
	public static Serializable getId(Object obj) {
		return getId(obj, obj.getClass().getSimpleName());
	}
	/**
	 * Convert the given key into a string
	 * @param key	The key to express as a String
	 * @return	The string representation of the given key
	 */
	public static String toString(Serializable key) {
		return key.toString();
	}
	/**
	 * A map of key constructors for indexed by the class of the keys that they construct
	 */
	private static Map<Class<? extends Serializable>, KeyConstructor> keyConstructors = new HashMap<Class<? extends Serializable>, KeyConstructor>();
	/**
	 * This static method converts the given string representation of a key into an instance of the given key class and 
	 * @param keyCls		The class of the key that is to be generatated from the string representation of the key
	 * @param keyStr		The string representation of the key
	 * @return			An instance of the key class generated from the string representation of the key
	 */
	public static Serializable toKey(Class<? extends Serializable> keyCls, String keyStr) {
		KeyConstructor keyConstructor = keyConstructors.get(keyCls);
		if (keyConstructor == null) {
			keyConstructor = KeyConstructor.keyConstructor(keyCls);
			keyConstructors.put(keyCls, keyConstructor);
		}
		return keyConstructor.getKey(keyStr);
		
	}
	/**
	 * This method returns the Serializable class of the Id field of the given class
	 * @param cls	The class for which the class of the Id field is required
	 * @return		The class that is used to represent the Id of the given class
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends Serializable> getIdFieldType(Class<?> cls) {
		Member id = getIdMember(cls);
		if (id == null) return null;
		if (id instanceof Field) {
			Field idField = (Field)id;
			return (Class<? extends Serializable>) idField.getType();
		} else if (id instanceof Method) {
			Method idMethod = (Method)id;
			return (Class<? extends Serializable>) idMethod.getReturnType();
		}
		return null;
	}
	

}
