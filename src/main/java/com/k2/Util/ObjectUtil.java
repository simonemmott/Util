package com.k2.Util;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder.Trimspec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.Util.Identity.IdentityUtil;
import com.k2.Util.classes.ClassUtil;
import com.k2.Util.classes.Getter;
import com.k2.Util.exceptions.UtilityError;

/**
 * This class provides static utility method for interacting with objects
 * @author simon
 *
 */
public class ObjectUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	/**
	 * This static method gets the value from the given object identified by the given class and alias
	 * @param obj			The object from which to get the value
	 * @param valueClass		The class of the value to be returned
	 * @param alias			The alias of the field or method that is to return the value
	 * @return				The value supplied by the underlying field or method
	 * 
	 * @param <E> The class of the object from which to get a value
	 * @param <V> The class of the value to get from the object
	 */
	@SuppressWarnings("unchecked")
	public static <E,V> V get(E obj, Class<V> valueClass, String alias) {
		if (obj == null) return null;
		Getter<E,V> g = (Getter<E, V>) ClassUtil.getGetter(obj.getClass(), valueClass, alias);
		if (g == null) return null;
		
		return g.get(obj);
	}
	
	/**
	 * This static method identifies whether a value could be got from the given object with the given class and alias
	 * @param obj			The object from which a value is required
	 * @param valueClass		The class of the value to be got
	 * @param alias			The alias of the field or method that is to supply the value
	 * @return				The value returned by the underlying field or method
	 */
	public static boolean canGet(Object obj, Class<?> valueClass, String alias) {
		if (obj == null) return false;
		return ClassUtil.canGet(obj.getClass(), valueClass, alias);
	}
	
	/**
	 * This static method copies the value of all the fields accessible through the first object to the second object
	 * @param fromObj	The source object
	 * @param toObj		The object whose fields will be updated to match those in the source object
	 * 
	 * @param <T> The class of the object being copied
	 */
	public static <T> void copy(T fromObj, T toObj) {
		if (fromObj == null || toObj == null) return;
		Class<?> cls = fromObj.getClass();
		
		Field[] fields = ClassUtil.getAllFields(cls);
		
		for (Field f : fields) {
			logger.trace("Attempting to set field {}", f.getName());
			if (f.isSynthetic()) logger.trace("Field {} is synthetic", f.getName());
			if (!f.isAccessible()) f.setAccessible(true);
			try {
				f.set(toObj, f.get(fromObj));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.warn("Unable to set field {} on {}({}) to same field on {}({})", 
						f.getName(), 
						toObj.getClass(), 
						IdentityUtil.getId(toObj), 
						fromObj.getClass(), 
						IdentityUtil.getId(fromObj)
					);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T clone(T toClone) {
		try {
			T clone = (T) toClone.getClass().newInstance();
			copy(toClone, clone);
			return clone;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new UtilityError("It is only possible to clone objects that have a zero arg constructor. No zero arg constructor for {} - {}", e, toClone.getClass().getName(), e.getMessage());
		}
	}
	
	private static Field findMatchingField(Field match, Field[] fields) {
		for (Field check : fields) {
			try {
			if (		check.getName().equals(match.getName()) &&
					check.getType().equals(match.getType())) {
				return check;
			}
			} catch (Throwable t) {
				logger.warn("Failed comparing Field {}.{} to Field {}.{}", match.getDeclaringClass().getCanonicalName(), match.getName(), check.getDeclaringClass().getCanonicalName(), check.getName());
			}
		}
		
		return null;
	}

	/**
	 * This static method finds the first matching item in each list or null if none of the list contents match
	 * @param lst1	The first list
	 * @param lst2	The second list
	 * @param <T> The type of objects in the given lists
	 * @return	The first matching object in each list
	 */
	public static <T> T findFirstMatch(List<T> lst1, List<T> lst2) {
		for (int i=0; i<lst1.size(); i++) {
			T t1 = lst1.get(i);
			logger.trace("List1[{}] {} <<<",i, t1);
			for (int j=0; j<lst2.size(); j++) {
				T t2 = lst2.get(j);
				logger.trace("List2[{}] {}{}", j, t2, ((t1.equals(t2))?" match":""));
				if (t1.equals(t2)) return t1;
			}
		}
		return null;
	}
	/**
	 * This class encapsulates a path of fields defining the route to a embedded entity within a root class
	 * @author simon
	 *
	 */
	static class FieldPath {
		String alias;
		FieldPath nextPath;
		FieldPath previousPath;
		/**
		 * Create an instance of the FieldPath extending the given path with the given alias
		 * @param previousPath	The FieldPath instance of the end of the path being extended with the given field alias
		 * @param alias	The alias of the next field in the path
		 */
		FieldPath(FieldPath previousPath, String alias) {
			this.previousPath = previousPath;
			if (previousPath != null) previousPath.nextPath = this;
			this.alias = alias;
		}
		
		/**
		 * This method recurses down the path to the end of the path for all objects and collections identified by fields in the path and populates the
		 * given list of embeddedObjects with the instances of the given class found at the end of the path
		 * @param obj	The object being recursed through or the root object
		 * @param embeddedObjects	The list of embedded objects to be populated
		 * @param cls	The class of the objects to be added to the list of embedded objects
		 * @return	The list of embeddedObjects found for this path in the root object
		 * @throws IllegalArgumentException	If there is a error reading fields in the path
		 * @throws IllegalAccessException	If there is an error reading fields in the path
		 */
		@SuppressWarnings("unchecked")
		<T> List<T> populateEmbeddedObjects(Object obj, List<T> embeddedObjects, Class<T> cls) throws IllegalArgumentException, IllegalAccessException {
			if (obj == null) return embeddedObjects;
			Field f = ClassUtil.getField(obj.getClass(), alias);
			if (!f.isAccessible()) f.setAccessible(true);
			
			if (nextPath == null) {
				if (cls.isAssignableFrom(f.getType())) {
					T t = (T) f.get(obj);
					if (t != null) embeddedObjects.add(t);
					return embeddedObjects;
				} else if (Collection.class.isAssignableFrom(f.getType())) {
					if (cls.isAssignableFrom(ClassUtil.getFieldGenericTypeClass(f, 0))) {
						for (T t : (Collection<T>)f.get(obj)) {
							if (t != null) embeddedObjects.add(t);
						}
					} else {
						throw new UtilityError("The generic type of the final collection field '{}' cannot be cast to {}", f.getName(), cls.getCanonicalName());
					}
					return embeddedObjects;
				} else {
					throw new UtilityError("The type of the final field '{}' cannot be cast to {}", f.getName(), cls.getCanonicalName());
				}
			} else {
				if (Collection.class.isAssignableFrom(f.getType())) {
					for (Object cObj : (Collection<?>)f.get(obj)) {
						nextPath.populateEmbeddedObjects(cObj, embeddedObjects, cls);
					}
					return embeddedObjects;
				}
				if (f.getType().getName().startsWith("java.")) {
					throw new UtilityError("The non final field {}.{} provides a java.* object that is not a collection", obj.getClass().getName(), f.getName());
				}
				nextPath.populateEmbeddedObjects(f.get(obj), embeddedObjects, cls);
				return embeddedObjects;
			}
			
		}
		
		/**
		 * This method recurses through the path avoiding collections to update the object at the end of the path to the given value
		 * @param obj			The object of the current location on the path
		 * @param embeddedObj	The object to set at the end of the path	
		 * @throws IllegalArgumentException	If there is an exception assigning the object at the end of the path
		 * @throws IllegalAccessException	If there is an exception assigning the object at the end of the path
		 */
		@SuppressWarnings("unchecked")
		<T> void putObjectAt(Object obj, T embeddedObj) throws IllegalArgumentException, IllegalAccessException {
			if (obj == null) return;
			Field f = ClassUtil.getField(obj.getClass(), alias);
			if (!f.isAccessible()) f.setAccessible(true);

			if (nextPath == null) {
				if (embeddedObj.getClass().isAssignableFrom(f.getType())) {
					f.set(obj, embeddedObj);
					return;
				} else if (Collection.class.isAssignableFrom(f.getType())) {
					if (embeddedObj.getClass().isAssignableFrom(ClassUtil.getFieldGenericTypeClass(f, 0))) {
						Collection<T> c = (Collection<T>)f.get(obj);
						c.add(embeddedObj);
					} else {
						throw new UtilityError("The generic type of the final collection field '{}' cannot be cast to {}", f.getName(), embeddedObj.getClass().getCanonicalName());
					}
					return;
				}
			} else {
				if (Collection.class.isAssignableFrom(f.getType())) {
					throw new UtilityError("The non final field {}.{} provaides a collection. Unable to put objects in non final collections", obj.getClass().getName(), f.getName());
				}
				if (f.getType().getName().startsWith("java.")) {
					throw new UtilityError("The non final field {}.{} provides a java.* object that is not a collection", obj.getClass().getName(), f.getName());
				}
				nextPath.putObjectAt(f.get(obj), embeddedObj);
				return;
			}
			
		}
		
		/**
		 * This method recurses through the path to identify the location of the given embedded object and removes it from the end of the path
		 * @param obj			The object of the current location on the path
		 * @param embeddedObj	The object to remove from the end of the path
		 * @throws IllegalArgumentException	If there is an exception removing the object from the end of the path
		 * @throws IllegalAccessException	If there is an exception removing the object from the end of the path
		 */
		@SuppressWarnings("unchecked")
		<T> void deleteObjectAt(Object obj, T embeddedObj) throws IllegalArgumentException, IllegalAccessException {
			if (obj == null) return;
			if (embeddedObj == null) return;
			Field f = ClassUtil.getField(obj.getClass(), alias);
			if (!f.isAccessible()) f.setAccessible(true);
			
			if (nextPath == null) {
				if (embeddedObj.getClass().isAssignableFrom(f.getType())) {
					T t = (T) f.get(obj);
					if (embeddedObj.equals(t)) f.set(obj, null);
					return;
				} else if (Collection.class.isAssignableFrom(f.getType())) {
					if (embeddedObj.getClass().isAssignableFrom(ClassUtil.getFieldGenericTypeClass(f, 0))) {
						Collection<T> ts = (Collection<T>)f.get(obj);
						if (ts.contains(embeddedObj)) {
							ts.remove(embeddedObj);
							f.set(obj, ts);
						}
					} else {
						throw new UtilityError("The generic type of the final collection field '{}' cannot be cast to {}", f.getName(), embeddedObj.getClass().getCanonicalName());
					}
					return;
				} else {
					throw new UtilityError("The type of the final field '{}' cannot be cast to {}", f.getName(), embeddedObj.getClass().getCanonicalName());
				}
			} else {
				if (Collection.class.isAssignableFrom(f.getType())) {
					for (Object cObj : (Collection<?>)f.get(obj)) {
						nextPath.deleteObjectAt(cObj, embeddedObj);
					}
					return;
				}
				if (f.getType().getName().startsWith("java.")) {
					throw new UtilityError("The non final field {}.{} provides a java.* object that is not a collection", obj.getClass().getName(), f.getName());
				}
				nextPath.deleteObjectAt(f.get(obj), embeddedObj);
				return;
			}
			
		}
		
		/**
		 * This static factory method converts the given string into a series of path object and returns the root of the path
		 * @param path	A string representing the path through the object tree
		 * @return	The instance of FieldPath representing the root of the resultant path
		 */
		static FieldPath getPath(String path) {
			String[] elements = StringUtil.trim(Trimspec.BOTH, '/', path).split("/");
			FieldPath rootPath = null;
			FieldPath previousPath = null;
			for (String alias : elements) {
				FieldPath fp = new FieldPath(previousPath, alias);
				if (rootPath == null) rootPath = fp;
				previousPath = fp;
			}
			return rootPath;
		}
		
	}
	
	/**
	 * This static method retrieves all the instances of the given class found at the end of the given path of field aliases for the given root object
	 * @param obj	The root object from which to extract the embedded instances of the given class found at the end of the given path of field aliased
	 * @param path	A '/' separated list of field aliases defining the navigation path to follow to locate instances of the given class
	 * @param cls	The class of the objects to extract from the given root object.
	 * @return	The list of instances of the given class found at the end of the given path in the given root object.
	 * 
	 * @param <T> The class of the objects to be listed
	 */
	public static <T> List<T> getObjectsAt(Object obj, String path, Class<T> cls) {

		FieldPath rootPath = FieldPath.getPath(path);

		try {
			return rootPath.populateEmbeddedObjects(obj, new ArrayList<T>(), cls);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new UtilityError("Error populating embedded objects", e);
		}
	}
	
	/**
	 * This static method sets the value of the end of the path to the given object so long as the path does not traverse a collection field
	 * @param obj	The object at the root of the given path
	 * @param path	The path identify the location in the object tree to add the embedded object. The final item in the path can be a collection
	 * 				however collections anywhere on the path other than the end are skipped
	 * @param embeddedObj	The object to add at the end of the path
	 * 
	 * @param <T> The class of the object to out on the object tree
	 */
	public static <T> void putObjectAt(Object obj, String path, T embeddedObj) {

		FieldPath rootPath = FieldPath.getPath(path);
		
		try {
			rootPath.putObjectAt(obj, embeddedObj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new UtilityError("Error putting embedded object {} in {}", e, obj, embeddedObj);
		}

	}

	/**
	 * This static method removes the given object from the end of the given path staring from the given object
	 * @param obj		The root object from which to start the path
	 * @param path		A string representation of the path to the field in which to remove the given embedded object
	 * @param embeddedObj	The object to remove from the end of the path
	 * 
	 * @param <T> The class of the object to remove from the object tree
	 */
	public static <T> void deleteObjectAt(Object obj, String path, T embeddedObj) {

		FieldPath rootPath = FieldPath.getPath(path);
		
		try {
			rootPath.deleteObjectAt(obj, embeddedObj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new UtilityError("Error putting embedded object {} in {}", e, obj, embeddedObj);
		}

	}
	
	/**
	 * Produce a list containing the single item
	 * @param item	The single item in the list
	 * @return	A list containing just the single item
	 * @param <T> The type of the object in the singleton list
	 */
	public static <T> List<T> singletonList(T item) {
		List<T> list = new ArrayList<T>(1);
		list.add(item);
		return list;
	}

	/**
	 * Produce a set containing the single item
	 * @param item	The single item in the set
	 * @return	A set containing just the single item
	 * @param <T> The type of the object in the singleton set
	 */
	public static <T> Set<T> singletonSet(T item) {
		Set<T> set = new HashSet<T>(1);
		set.add(item);
		return set;
	}
	
	public static boolean equivalent(int lhs, int rhs) { logger.debug("(int) "+lhs+" == "+rhs+"?"); return (lhs == rhs); }
	public static boolean equivalent(long lhs, long rhs) { logger.debug("(long) "+lhs+" == "+rhs+"?"); return (lhs == rhs); }
	public static boolean equivalent(boolean lhs, boolean rhs) { logger.debug("(boolean) "+lhs+" == "+rhs+"?"); return (lhs == rhs); }
	public static boolean equivalent(char lhs, char rhs) { logger.debug("(char) "+lhs+" == "+rhs+"?"); return (lhs == rhs); }
	public static boolean equivalent(byte lhs, byte rhs) { logger.debug("(byte) "+lhs+" == "+rhs+"?"); return (lhs == rhs); }
	public static boolean equivalent(short lhs, short rhs) { logger.debug("(short) "+lhs+" == "+rhs+"?"); return (lhs == rhs); }
	public static boolean equivalent(float lhs, float rhs) { logger.debug("(float) "+lhs+" == "+rhs+"?"); return (lhs == rhs); }
	public static boolean equivalent(double lhs, double rhs) { logger.debug("(double) "+lhs+" == "+rhs+"?"); return (lhs == rhs); }
	
	public static boolean equivalent(Integer lhs, Integer rhs) { logger.debug("(Integer) "+lhs+" == "+rhs+"?"); return (lhs==null)?false:lhs.equals(rhs); }
	public static boolean equivalent(Long lhs, Long rhs) { logger.debug("(Long) "+lhs+" == "+rhs+"?"); return (lhs==null)?false:lhs.equals(rhs); }
	public static boolean equivalent(Boolean lhs, Boolean rhs) { logger.debug("(Boolean) "+lhs+" == "+rhs+"?"); return (lhs==null)?false:lhs.equals(rhs); }
	public static boolean equivalent(Character lhs, Character rhs) { logger.debug("(Character) "+lhs+" == "+rhs+"?"); return (lhs==null)?false:lhs.equals(rhs); }
	public static boolean equivalent(Byte lhs, Byte rhs) { logger.debug("(Byte) "+lhs+" == "+rhs+"?"); return (lhs==null)?false:lhs.equals(rhs); }
	public static boolean equivalent(Short lhs, Short rhs) { logger.debug("(Short) "+lhs+" == "+rhs+"?"); return (lhs==null)?false:lhs.equals(rhs); }
	public static boolean equivalent(Float lhs, Float rhs) { logger.debug("(Float) "+lhs+" == "+rhs+"?"); return (lhs==null)?false:lhs.equals(rhs); }
	public static boolean equivalent(Double lhs, Double rhs) { logger.debug("(Double) "+lhs+" == "+rhs+"?"); return (lhs==null)?false:lhs.equals(rhs); }
	public static boolean equivalent(String lhs, String rhs) { logger.debug("(String) "+lhs+" == "+rhs+"?"); return (lhs==null)?false:lhs.equals(rhs); }
	public static boolean equivalent(Date lhs, Date rhs) { logger.debug("(Date) "+lhs+" == "+rhs+"?"); return (lhs==null)?false:lhs.equals(rhs); }
	public static <T> boolean equivalent(Collection<T> lhs, Collection<T> rhs) {
		logger.debug("(Collection<T>) "+lhs+" == "+rhs+"?"); 
		if (lhs == null || rhs == null )
			return false;
		
		if (lhs.size() != rhs.size()) {
			logger.debug("Size mismatch: lhs.size()["+lhs.size()+"] != rhs.size()["+rhs.size()+"]");
			return false;
		}
		
		T[] lhsTa = (T[]) lhs.toArray();
		T[] rhsTa = (T[]) rhs.toArray();

		for (int i = 0; i<lhs.size(); i++) {
			logger.debug("Checking collection index ["+i+"]");
			if (! equivalent(lhsTa[i], rhsTa[i]))
				return false;
		}
		
		return true; 
	}


	public static <T> boolean equivalent(T lhs, T rhs) {
		logger.debug("(T) "+lhs+" == "+rhs+"?"); 
		if ( ! lhs.getClass().equals(rhs.getClass())) {
			logger.debug("Class mismatch: lhs.getClass()[{}].equals(rhs.getClass()[{}])", lhs.getClass().getName(), rhs.getClass().getName());
			return false;
		}
		
		for (Field f : ClassUtil.getAllFields(lhs.getClass())) {
			if (f.isSynthetic()) continue;
			logger.debug("Checking field {}.{}:{}", lhs.getClass().getSimpleName(), f.getName(), f.getType().getSimpleName());
			f.setAccessible(true);
			try {
				if(f.getType() == int.class) {
					if ( ! equivalent(f.getInt(lhs), f.getInt(rhs))) return false;
				} else if (f.getType() == long.class) {
					if ( ! equivalent(f.getLong(lhs), f.getLong(rhs))) return false;
				} else if (f.getType() == char.class) {
					if ( ! equivalent(f.getChar(lhs), f.getChar(rhs))) return false;
				} else if (f.getType() == float.class) {
					if ( ! equivalent(f.getFloat(lhs), f.getFloat(rhs))) return false;
				} else if (f.getType() == double.class) {
					if ( ! equivalent(f.getDouble(lhs), f.getDouble(rhs))) return false;
				} else if (f.getType() == boolean.class) {
					if ( ! equivalent(f.getBoolean(lhs), f.getBoolean(rhs))) return false;
				} else if (f.getType() == short.class) {
					if ( ! equivalent(f.getShort(lhs), f.getShort(rhs))) return false;
				} else if (f.getType() == byte.class) {
					if ( ! equivalent(f.getByte(lhs), f.getByte(rhs))) return false;
				} else if (f.getType() == Integer.class) {
					if ( ! equivalent((Integer)f.get(lhs), (Integer)f.get(rhs))) return false;
				} else if (f.getType() == Long.class) {
					if ( ! equivalent((Long)f.get(lhs), (Long)f.get(rhs))) return false;
				} else if (f.getType() == Float.class) {
					if ( ! equivalent((Float)f.get(lhs), (Float)f.get(rhs))) return false;
				} else if (f.getType() == Double.class) {
					if ( ! equivalent((Double)f.get(lhs), (Double)f.get(rhs))) return false;
				} else if (f.getType() == Byte.class) {
					if ( ! equivalent((Byte)f.get(lhs), (Byte)f.get(rhs))) return false;
				} else if (f.getType() == Short.class) {
					if ( ! equivalent((Short)f.get(lhs), (Short)f.get(rhs))) return false;
				} else if (f.getType() == Character.class) {
					if ( ! equivalent((Character)f.get(lhs), (Character)f.get(rhs))) return false;
				} else if (f.getType() == Boolean.class) {
					if ( ! equivalent((Boolean)f.get(lhs), (Boolean)f.get(rhs))) return false;
				} else if (f.getType() == String.class) {
					if ( ! equivalent((String)f.get(lhs), (String)f.get(rhs))) return false;
				} else if (f.getType() == Date.class) {
					if ( ! equivalent((Date)f.get(lhs), (Date)f.get(rhs))) return false;
				} else if (Collection.class.isAssignableFrom(f.getType())) {
					if ( ! equivalent((Collection<T>)f.get(lhs), (Collection<T>)f.get(rhs))) return false;
				} else if (f.getType().isArray()) {
					logger.debug("Field {}.{} is Array", lhs.getClass().getSimpleName(), f.getName());
					T[] lhsFa = (T[]) f.get(lhs);
					T[] rhsFa = (T[]) f.get(rhs);
					
					if (lhsFa.length != rhsFa.length) {
						logger.debug("Array length mismatch: lhsFa.length[{}] != rhsFa.length[{}]", lhsFa.length, rhsFa.length);
						return false;
					}
					
					for (int i=0; i<lhsFa.length; i++) {
						logger.debug("Checking array index[{}]", i);
						if (lhsFa[i]!=null && rhsFa[i]!=null) {
							if (lhsFa[i].getClass().isPrimitive()) {
								if (lhsFa[i] == rhsFa[i]) {
									logger.debug("Primitive equality failure: {} == {}?", lhsFa[i], rhsFa[i]);
									return false;
								}
							} else {
								if ( ! equivalent(lhsFa[i], rhsFa[i])) {
									logger.debug("Array field value equivalence check failure: {}.{}[{}]", lhs.getClass().getSimpleName(), f.getName(), i);
									return false;
								}
							}
						}
					}
					
				} else {
					if ( ! equivalent(f.get(lhs), f.get(rhs))) {
						logger.debug("Field value equivalence check failure: {}.{}", lhs.getClass().getSimpleName(), f.getName());
						return false;
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error(e.getMessage());
				return false;
			}
		}
		
		logger.debug("{} is equivalent to {}", lhs, rhs);
		return true;
	}

}
