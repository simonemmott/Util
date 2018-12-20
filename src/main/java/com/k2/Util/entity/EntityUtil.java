package com.k2.Util.entity;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.k2.Util.Identity.EntityToKey;
import com.k2.Util.Identity.Id;
import com.k2.Util.Identity.Identified;
import com.k2.Util.Identity.Identity;
import com.k2.Util.Identity.KeyConstructor;
import com.k2.Util.classes.ClassUtil;
import com.k2.Util.exceptions.UtilityError;


public class EntityUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private static Map<Class<?>, EntityCache<?>> entities = new HashMap<Class<?>,EntityCache<?>>();
	
	@SuppressWarnings("unchecked")
	public static <E> EntityCache<E> getEntityCache(Class<E> entityClass) {
		EntityCache<E> ec = (EntityCache<E>) entities.get(entityClass);
		if (ec != null) return ec;
		ec = new EntityCache<E>(entityClass);
		entities.put(entityClass, ec);
		ec.populateFields();
		return ec;
	}

	public static <E> Field getColumnByName(Class<E> entityClass, String name) {
		return getEntityCache(entityClass).getColumnByName(name);
	}

	public static <E> EntityField<E,?> getEntityFieldByName(Class<E> entityClass, String name) {
		return getEntityCache(entityClass).getFieldByName(name);
	}

	public static <E> Field getColumnByAlias(Class<E> entityClass, String alias) {
		return getEntityCache(entityClass).getColumnByAlias(alias);
	}

	public static <E> EntityField<E,?> getEnityFieldByAlias(Class<E> entityClass, String alias) {
		return getEntityCache(entityClass).getFieldByAlias(alias);
	}

	public static <E> EntityLink<E,?> getEnityLinkByAlias(Class<E> entityClass, String alias) {
		return (EntityLink<E, ?>) getEntityCache(entityClass).getFieldByAlias(alias);
	}
	
	public static <E> List<EntityLink<E,?>> getLazyLoadLinks(Class<E> entityClass) {
		List<EntityLink<E,?>> lazyLinks = new ArrayList<EntityLink<E,?>>();
		EntityCache<E> ec = getEntityCache(entityClass);
		
		for (EntityLink<E,?> link : ec.getLinks()) 
			if (link.isLazyFetch()) lazyLinks.add(link);
		
		return lazyLinks;
	}
	
	/**
	 * The static map of id fields for each class.
	 */
	private static Map<Class<?>, Member> idMembersMap = new HashMap<Class<?>, Member>();
	
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
	public static Member getIdMember(Class<?> cls) {
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
	public static Serializable getId(Object obj) {
		
		if (obj == null) return null;
		
		if (Id.class.isAssignableFrom(obj.getClass())) return ((Id<?,?>)obj).getId();
		
		EntityToKey etk = keyGenerators.get(obj.getClass());
		if (etk == null) {
			etk = EntityToKey.forClass(obj.getClass());
			keyGenerators.put(obj.getClass(), etk);
		}
		return (Serializable) etk.getKey(obj);
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

	@SuppressWarnings("unchecked")
	public static <E> Class<?> getKeyClass(Class<E> entityClass) {
		EntityToKey<E,?> etk = keyGenerators.get(entityClass);
		if (etk == null) {
			etk = EntityToKey.forClass(entityClass);
			keyGenerators.put(entityClass, etk);
		}
		return etk.getKeyClass();
	}

	public static Class<?> getBaseEntityClass(Class<?> entityClass) {
		
		List<Class<?>> classes = ClassUtil.getSupertypes(entityClass);
		for (Class<?> cls : Lists.reverse(classes)) {
			if (cls.isAnnotationPresent(Entity.class))
				return cls;
		}
		return entityClass;
	}

	public static boolean isRootEntity(Class<?> cls) {
		if (cls.isAnnotationPresent(RootEntity.class)) 
			return true;
		if (cls.getSuperclass() != Object.class)
			return isRootEntity(cls.getSuperclass());
		return false;
	}
	
	private static Map<Class<?>, DomainEntityMap<?>> domainEntityMaps = new HashMap<Class<?>, DomainEntityMap<?>>();
	
	private static <T> DomainEntityMap<T> getDomainEntityMap(Class<T> cls) {
		DomainEntityMap<T> dem = (DomainEntityMap<T>) domainEntityMaps.get(cls);
		if (dem != null)
			return dem;
		dem = new DomainEntityMap<T>(cls);
		domainEntityMaps.put(cls, dem);
		return dem;
	}

	public static Class<?> getRootEntityType(Class<?> cls) {
		return getDomainEntityMap(cls).getRootEntityType();
	}

	public static Class<?> getParentEntityType(Class<?> cls) {
		return getDomainEntityMap(cls).getParentEntityType();
	}

	public static Object getRootEntity(Object obj) {
		return getDomainEntityMap(obj.getClass()).getRootEntity(obj);
	}
	
	public static Object getParentEntity(Object obj) {
		return getDomainEntityMap(obj.getClass()).getParentEntity(obj);
	}

	public static String getRootPath(Object obj) {
		Object parent = getParentEntity(obj);
		if (parent == null || parent.equals(obj))
			return "";
		else 
			return getRootPath(parent)+"."+getPath(parent, obj);
	}
	
	public static String getParentPath(Object obj) {
		Object parent = getParentEntity(obj);
		if (parent == null || parent.equals(obj))
			return "";
		return getPath(parent, obj);
	}
	
	public static String getPath(Object parent, Object child) {
		for (Field f : ClassUtil.getFields(parent.getClass(), child.getClass())) {
			if (f.isAnnotationPresent(Column.class) || f.isAnnotationPresent(OneToMany.class) || f.isAnnotationPresent(ManyToOne.class)) {
				try {
					if (child.getClass().isAssignableFrom(f.getType())) {
						Object value = f.get(parent);
						if (value != null && value.equals(child))
							return f.getName();
					} else if (Collection.class.isAssignableFrom(f.getType())) {
						Collection collection = (Collection) f.get(parent);
						for (Object value : collection) {
							if (value != null && value.equals(child)) {
								return f.getName()+"["+getId(child)+"]";
							}
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new UtilityError("Unable to get value from field {}.{} for object of type {}", e, f.getDeclaringClass().getName(), f.getName(), parent.getClass().getName());
				} finally {
					
				}
			}
			
		}
		
		
		return null;
	}
	


}
