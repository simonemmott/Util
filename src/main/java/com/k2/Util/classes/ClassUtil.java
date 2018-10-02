package com.k2.Util.classes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import javax.tools.JavaFileObject.Kind;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.Util.ObjectUtil;
import com.k2.Util.StringUtil;
import com.k2.Util.exceptions.UtilityError;
import com.k2.Util.tuple.Pair;

/**
 * The ClassUtil provides static methods for dealing with and finding classes
 * @author simon
 *
 */
public class ClassUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	
	/**
	 * This enumeration identifies whether the list of annotations to check must all be present on the class or if any of the
	 * annotations must be present on the class
	 * 
	 * @author simon
	 *
	 */
	public static enum AnnotationCheck {
		/**
		 * All of the defined annotations must be present
		 */
		ALL,
		/**
		 * Any of the defined annotations must be present
		 */
		ANY
	}

	/**
	 * This method returns all the classes defined in the given package
	 * 
	 * If a class file is present in the directories on the class path that match this package but cannot be identified by the 
	 * context class loader a UtilityError is thrown
	 * 
	 * @param packageName The name of the package to scan for classes
	 * @return	An array of classes defined in the package.
	 * @see UtilityError
	 */
    public static Class<?>[] getClasses(String packageName) { return getClasses(packageName, false, AnnotationCheck.ANY); }
    /**
     * This method returns all the classes defined in the given package
     * 
     * If strict is true and a class file is present in the directories on the class path that match this package but cannot be identified by the 
	 * context class loader a UtilityError is thrown
	 * 
	 * If strict is not true then no error will be thrown if a class file exists in the package directories that cannot be loaded from the class loader.
	 * Instead a warning is logged using the UtilityLogger
	 * 
     * @param packageName The name of the package to scan
     * @param strict		True if a UtilityError should be thrown if the class for a class file cannot be loaded from the class loader
     * @return	An array of classes defined in the package.
     * @see UtilityError
     */
    public static Class<?>[] getClasses(String packageName, boolean strict) { return getClasses(packageName, strict, AnnotationCheck.ANY); }
    /**
     * This method returns all the classes defined in a package if they are annotated with the defined annotations
     * 
     * If strict is true and a class file is present in the directories on the class path that match this package but cannot be identified by the 
	 * context class loader a UtilityError is thrown
	 * 
	 * If strict is not true then no error will be thrown if a class file exists in the package directories that cannot be loaded from the class loader.
	 * Instead a warning is logged using the UtilityLogger
	 * 
     * @param packageName	The name of the package to scan
     * @param strict		True if a UtilityError should be thrown if the class for a class file cannot be loaded from the class loader
     * @param annotationCheck	Identifies whether all or any of the defined annotations must be present in the list of classes in the package
     * @param annotationClasses	The list of annotations to check
     * @return	An array of classes defined in the package.
     * @see UtilityError
     */
    @SafeVarargs
	public static Class<?>[] getClasses(String packageName, boolean strict, AnnotationCheck annotationCheck, Class<? extends Annotation> ... annotationClasses) {
    	
    		// Get the context class loader from the current thread
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) throw new UtilityError("Unable to identify the context loader from the current thread");

        // Identify all the directories that provide classes in the given package name
        String path = packageNameToPath(packageName);
        Enumeration<URL> resources;
		try {
			resources = classLoader.getResources(path);
		} catch (IOException e) {
			throw new UtilityError(e);
		}
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {

            URL resource = resources.nextElement();
         	dirs.add(new File(resource.getFile()));
        }
        
        // For each directory in the identified directories for the given package name
        // Identify classes with the given annotations
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName, strict, annotationCheck, annotationClasses));
        }
        return classes.toArray(new Class[classes.size()]);
    }
    /**
     * This method returns all the classes defined in a package if they are annotated with the defined annotations
     * 
     * If strict is true and a class file is present in the directories on the class path that match this package but cannot be identified by the 
	 * context class loader a UtilityError is thrown
	 * 
	 * If strict is not true then no error will be thrown if a class file exists in the package directories that cannot be loaded from the class loader.
	 * Instead a warning is logged using the UtilityLogger
	 * 
     * @param packageName	The name of the package to scan
     * @param annotationClasses	The list of annotations to check
     * @return	An array of classes defined in the package.
     * @see UtilityError
     */
	@SafeVarargs
	public static Class<?>[] getClasses(String packageName, Class<? extends Annotation> ... annotationClasses) {
		return getClasses(packageName, false, AnnotationCheck.ALL, annotationClasses);
	}
	/**
     * This method returns all the classes defined in a package if they are annotated with the defined annotations
     * 
     * If strict is true and a class file is present in the directories on the class path that match this package but cannot be identified by the 
	 * context class loader a UtilityError is thrown
	 * 
	 * If strict is not true then no error will be thrown if a class file exists in the package directories that cannot be loaded from the class loader.
	 * Instead a warning is logged using the UtilityLogger
	 * 
     * @param packageName	The name of the package to scan
     * @param annotationCheck	Identifies whether all or any of the defined annotations must be present in the list of classes in the package
     * @param annotationClasses	The list of annotations to check
     * @return	An array of classes defined in the package.
     * @see UtilityError
	 */
	@SafeVarargs
	public static Class<?>[] getClasses(String packageName, AnnotationCheck annotationCheck, Class<? extends Annotation> ... annotationClasses) {
		return getClasses(packageName, false, annotationCheck, annotationClasses);
	}

    /**
     * This method returns all classes in a particular directory if they are annotated with the defined annotations
     * 
     * If strict is true and a class file is present in the directories on the class path that match this package but cannot be identified by the 
	 * context class loader a UtilityError is thrown
	 * 
	 * If strict is not true then no error will be thrown if a class file exists in the package directories that cannot be loaded from the class loader.
	 * Instead a warning is logged using the UtilityLogger
	 * 
	 * This method is called recursively for sub directories of the given directory
	 * 
     * @param directory	The directory to read for class files
     * @param packageName	The name of the package to scan
     * @param strict		True if a UtilityError should be thrown if the class for a class file cannot be loaded from the class loader
     * @param annotationCheck	Identifies whether all or any of the defined annotations must be present in the list of classes in the package
     * @param annotationClasses	The list of annotations to check
     * @return	An array of classes defined in the package.
     * @see UtilityError
     */
    @SafeVarargs
	private static List<Class<?>> findClasses(File directory, String packageName, boolean strict, AnnotationCheck annotationCheck, Class<? extends Annotation> ... annotationClasses) {
    		// If the given directory does not exist return an empty list of classes
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (!directory.exists()) {
        		logger.warn("The directory '{}' does not exist while scanning package '{}'", directory.getAbsolutePath(), packageName);
            return classes;
        }
        
        // For each file in the directory
        File[] files = directory.listFiles();
        for (File file : files) {
        		// If the file is a directory recursively call findClasses having extended the packageName with the name of the file
            if (file.isDirectory()) {
            		if (file.getName().contains(".")) throw new UtilityError("Package direcory names must not contain a period '.'");
             	classes.addAll(findClasses(file, packageName + "." + file.getName(), strict, annotationCheck, annotationClasses));
            }
            // If the file is a class file
            else if (file.getName().endsWith(".class")) {
            		// Get the class defined by the file
	            	String findClassForName = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
	            	Class<?> cls;
	            	// If the class doesn't exist throw an unchecked error as this is unlikely to happen and shouldn't have to be coded for defensively.
				try {
					cls = Class.forName(findClassForName);
				} catch (ClassNotFoundException e) {
					if (strict) {
						throw new UtilityError("No class defined with name '"+findClassForName+"' while scanning package '"+packageName+"'", e);
					} else {
						logger.warn("No class defined with name '{}' while scanning package '{}'", findClassForName, packageName);
						cls = null;
					}
				}
				// If the class was found
	            	if (cls != null) {
	            		// If checking for annotations
	            		if (annotationClasses.length > 0) {
		            		boolean add;
		            		switch(annotationCheck) {
		            			// If all annotations must be present
							case ALL:
								// Add the class to the list of classes if all annotations are present
								add = true;
								for(Class<? extends Annotation> annotationClass : annotationClasses) {
					            		if (!cls.isAnnotationPresent(annotationClass)) {
					            			add = false;
					            			break;
					            		}
								}
								if (add) classes.add(cls);
								break;
							// If any of the annotations must be present
							case ANY:
								// Add the class to the list of classes if any of the annotations are present
								add = false;
								for(Class<? extends Annotation> annotationClass : annotationClasses) {
					            		if (cls.isAnnotationPresent(annotationClass)) {
					            			add = true;
					            			break;
					            		}
								}
								if (add) classes.add(cls);
								break;
			            	}
		            	} 
	            		// If not checking for annotations
	            		else {
	            			// Add the class to the list of classes
		            		classes.add(cls);
		            	}
	            	}
            }
        }
        return classes;
    }
    /**
     * This method converts a package name into a relative path
     * @param packageName	The package name to convert into a relative path
     * @return	The string representing the relative path
     */
    public static String packageNameToPath(String packageName) {
        return packageName.replace('.', File.separatorChar);
    }
    
    public static Path packageNameToRelativePath(String packageName) {
    	
    		logger.trace("Converting {} to a relative Path", packageName);
    		String start;
    		String[] parts = packageNameToPath(packageName).split(String.valueOf(File.separatorChar));
    		String[] next = new String[parts.length-1];
    		
    		start = parts[0];
    		for (int i=0; i<parts.length-1; i++)
    			next[i] = parts[i+1];
    		
    		return Paths.get(start, next);
    		
    }
    /**
     * This static method lists the super types of the given class in order starting with the given class
     * @param cls	The class for which the list of supertypes is required
     * @return	A List of the classes that are the super types of the given class
     */
    public static List<Class<?>> getSupertypes(Class<?> cls) {
    		if (cls == null) return null;
    		List<Class<?>> h = new ArrayList<Class<?>>();
    		h.add(cls);
    		Class<?> sCls = cls.getSuperclass();
    		while(sCls != null) {
    			h.add(sCls);
    			sCls = sCls.getSuperclass();
    		}
    		return h;
    }
       
    /**
     * This static method examines the super types of the two given classes and returns the nearest matching super type
     * @param cls1	The first class
     * @param cls2	The second class
     * @param <T> The type of the shared supertype
     * @return	The closest supertype of that is the same for both classes
     */
    @SuppressWarnings("unchecked")
	public static <T> Class<T> findMatchingSupertype(Class<? extends T> cls1, Class<? extends T> cls2) {
    		
    		if (cls1 == cls2) return (Class<T>)cls1;
    		List<Class<?>> cls1Hierarchy = getSupertypes(cls1);
    		List<Class<?>> cls2Hierarchy = getSupertypes(cls2);
    		Class<?> cls = ObjectUtil.findFirstMatch(cls1Hierarchy, cls2Hierarchy);
    		return (Class<T>)cls;
    }
    /**
     * This is a cache of all of the methods for each class that has been queried 
     */
	private static Map<Class<?>, Method[]> methodsCache; 
	/**
	 * This method gets the methods cache initializing it if it hasn't yet been instantiated
	 * @return	The methods cache
	 */
	private static Map<Class<?>, Method[]> getMethodsCache() {
		if (methodsCache == null) methodsCache = new HashMap<Class<?>, Method[]>();
		return methodsCache;
	}
	/**
	 * This static method returns an array of the methods callable from the given class
	 * @param cls	The class for which the callable methods are required
	 * @return	An array of the callable methods for the given class
	 */
	public static Method[] getAllMethods(Class<?> cls) {
		List<MethodSignature> signatures = new ArrayList<MethodSignature>();
		if (cls.isInterface()) {
			for (Class<?> iFace : cls.getInterfaces()) {
				for (Method m : getAllMethods(iFace)) {
					MethodSignature ms = MethodSignature.forMethod(m);
					if (!signatures.contains(ms)) signatures.add(ms);
				}
			}
		}
		for (Class<?> c : getSupertypes(cls)) {
			if(!c.equals(Object.class)) {
				for (Method m : getDeclaredMethods(c)) {
					MethodSignature ms = MethodSignature.forMethod(m);
					if (!signatures.contains(ms)) signatures.add(ms);
				}
			}
		}
		Method[] methods = new Method[signatures.size()];
		for (int i=0; i<signatures.size(); i++) {
			methods[i] = signatures.get(i).getMethod();
		}
		return methods;
	}
	/**
	 * Thist statica method returns the methods defined on the given class
	 * @param cls	The class for which the defined method are required
	 * @return	An array of the methods defined on the given class
	 */
	public static Method[] getDeclaredMethods(Class<?> cls) {
		if (cls ==null) return null;
		Method[] methods = getMethodsCache().get(cls);
		if (methods == null) {
			methods = cls.getDeclaredMethods();
			List<Method> out = new ArrayList<Method>(methods.length);
			for (Method m : methods) if (!m.isSynthetic()) {
				if (!m.isAccessible()) m.setAccessible(true);
				out.add(m);
			}
			methods = out.toArray(new Method[out.size()]);
			getMethodsCache().put(cls, methods);
		}
		return methods;
	}
	
	public static Method[] getAnnotatedMethods(Class<?> cls, Class<? extends Annotation> annotation) {
		if (cls ==null) return null;
		Method[] methods = getDeclaredMethods(cls);
		Set<Method> out = new HashSet<Method>(methods.length);
		for (Method m : methods) 
			if (m.isAnnotationPresent(annotation))
				out.add(m);
		
		return out.toArray(new Method[out.size()]);
	}

	public static Method[] getAllAnnotatedMethods(Class<?> cls, Class<? extends Annotation> annotation) {
		if (cls ==null) return null;
		Method[] methods = getAllMethods(cls);
		Set<Method> out = new HashSet<Method>(methods.length);
		for (Method m : methods) 
			if (m.isAnnotationPresent(annotation))
				out.add(m);
		
		return out.toArray(new Method[out.size()]);
	}

    /**
     * The static cache of fields by class
     * 
     * Note this cache excludes synthetic fields
     */
	private static Map<Class<?>, Field[]> fieldsCache; 
	/**
	 * This method returns the static cache of fields by class after initialising it if it has not already been initialised
	 * @return	The static cache of fields by class
	 */
	private static Map<Class<?>, Field[]> getFieldsCache() {
		if (fieldsCache == null) fieldsCache = new HashMap<Class<?>, Field[]>();
		return fieldsCache;
	}
	/**
	 * This static method gets all the fields available through the given class
	 * @param cls	The class for which the available fields is required
	 * @return	An array of the available fields.
	 */
	public static Field[] getAllFields(Class<?> cls) {
		List<Field> fields = new ArrayList<Field>();
		for (Class<?> c : getSupertypes(cls)) {
			fields.addAll(Arrays.asList(getDeclaredFields(c)));
		}
		return fields.toArray(new Field[fields.size()]);
	}

	/**
	 * This method gets the declared fields excluding synthetic fields from the utilities static cache
	 * If the class has not yet had its fields cached by the utility the fields are first extracted using reflection.
	 * 
	 * @param cls	The class for which to get the fields
	 * @return	An array of the declared fields of the class excluding synthetic fields
	 */
	public static Field[] getDeclaredFields(Class<?> cls) {
		if (cls ==null) return null;
		Field[] fields = getFieldsCache().get(cls);
		if (fields == null) {
			fields = cls.getDeclaredFields();
			List<Field> out = new ArrayList<Field>(fields.length);
			for (Field f : fields) if (!f.isSynthetic()&&!f.getName().equals("serialVersionUID")) {
				if (!f.isAccessible()) f.setAccessible(true);
				out.add(f);
			}
			fields = out.toArray(new Field[out.size()]);
			getFieldsCache().put(cls, fields);
		}
		return fields;
	}
	public static Field[] getAnnotatedFields(Class<?> cls, Class<? extends Annotation> annotation) {
		if (cls ==null) return null;
		Field[] fields = getDeclaredFields(cls);
		Set<Field> out = new HashSet<Field>(fields.length);
		for (Field f : fields) 
			if (f.isAnnotationPresent(annotation))
				out.add(f);
		
		return out.toArray(new Field[out.size()]);
	}

	public static Field[] getAllAnnotatedFields(Class<?> cls, Class<? extends Annotation> annotation) {
		if (cls ==null) return null;
		Field[] fields = getAllFields(cls);
		Set<Field> out = new HashSet<Field>(fields.length);
		for (Field f : fields) 
			if (f.isAnnotationPresent(annotation))
				out.add(f);
		
		return out.toArray(new Field[out.size()]);
	}

	/**
	 * This static method extracts the field definition from the given class.
	 * @param cls		The class for which the field is required 
	 * @param alias		The alias (name) of the required field
	 * @return			The Field for the given alias.
	 * 					If there is no field for the given alias in the given class an unchecked UtilityError is thrown
	 */
	public static Field getField(Class<?> cls, String alias) {
		return getField(cls, alias, true);
	}

	public static Field getField(Class<?> cls, String alias, boolean strict) {
		for (Field f : getAllFields(cls)) {
			if (f.getName().equals(alias)) return f;
		}
		if(strict)
			throw new UtilityError("The alias {} does not exists as a field in class {}", alias, cls.getName());
		return null;
	}

	public static Field getField(Class<?> cls, Class<?> type) {
		for (Field f : getAllFields(cls)) {
			if (f.getType().isAssignableFrom(type)) return f;
		}
		return null;
	}

	/**
	 * This method extracts the generic type of a field that is defined with generic type arguements
	 * @param fld	The field from which to extract the generic type argument
	 * @param pos	The 0 based position of the generic type argument to extract
	 * @return		The class of the generic type of the given field at the given position
	 */
	@SuppressWarnings("rawtypes")
	public static Class getFieldGenericTypeClass(Field fld, int pos) {
		if (pos < 0) { 
			logger.warn("Unable to identify generic type for field {}.{} for a negative positional value: {}", 
				fld.getDeclaringClass().getCanonicalName(), 
				fld.getName(),
				pos);
			return null;
		}
		Type fType = fld.getGenericType();
		if (fType instanceof ParameterizedType) {
			ParameterizedType pType = (ParameterizedType)fType;
			Type[] fGenericTypes = pType.getActualTypeArguments();
			if (pos >= fGenericTypes.length) {
				logger.warn("Unable to identify generic type for field {}.{} positional value: {}", 
					fld.getDeclaringClass().getCanonicalName(), 
					fld.getName(),
					pos);
				return null;
			}
			Type gType = fGenericTypes[pos];
			if (gType instanceof Class) {
				return (Class)gType;
			}
			logger.warn("Field {}.{} has an unidentifiable type {} at position {}",
					fld.getDeclaringClass().getCanonicalName(), 
					fld.getName(),
					gType.getTypeName(),
					pos);
		}
		return null;
	}

	public static Class getMethodGenericTypeClass(Method method, int pos) {
		if (pos < 0) { 
			logger.warn("Unable to identify generic type for method {}.{} for a negative positional value: {}", 
					method.getDeclaringClass().getCanonicalName(), 
					method.getName(),
					pos);
			return null;
		}
		Type mType = method.getGenericReturnType();
		if (mType instanceof ParameterizedType) {
			ParameterizedType pType = (ParameterizedType)mType;
			Type[] mGenericTypes = pType.getActualTypeArguments();
			if (pos >= mGenericTypes.length) {
				logger.warn("Unable to identify generic type for method {}.{} positional value: {}", 
						method.getDeclaringClass().getCanonicalName(), 
						method.getName(),
						pos);
				return null;
			}
			Type gType = mGenericTypes[pos];
			if (gType instanceof Class) {
				return (Class)gType;
			}
			logger.warn("method {}.{} has an unidentifiable type {} at position {}",
					method.getDeclaringClass().getCanonicalName(), 
					method.getName(),
					gType.getTypeName(),
					pos);
		}
		return null;
	}

	public static Class getClassGenericTypeClass(Class cls, int pos) {
		if (pos < 0) { 
			logger.warn("Unable to identify generic type for class {} for a negative positional value: {}", 
					cls.getCanonicalName(), 
					pos);
			return null;
		}
		Type cType = cls.getGenericSuperclass();
		if (cType instanceof ParameterizedType) {
			ParameterizedType pType = (ParameterizedType)cType;
			Type[] mGenericTypes = pType.getActualTypeArguments();
			if (pos >= mGenericTypes.length) {
				logger.warn("Unable to identify generic type for class {} positional value: {}", 
						cls.getCanonicalName(), 
						pos);
				return null;
			}
			Type gType = mGenericTypes[pos];
			if (gType instanceof Class) {
				return (Class)gType;
			}
			logger.warn("Class {} has an unidentifiable type {} at position {}",
					cls.getCanonicalName(), 
					gType.getTypeName(),
					pos);
		}
		return null;
	}

	/**
	 * A cache of the Getters indexed by class and alias. Where alias is the field name or the sections of the method name 
	 * following an initial 'get' String with the first letter of the remaining string in lower case
	 */
	private static Map<Pair<Class<?>, String>, Getter<?,?>> getters = new HashMap<Pair<Class<?>, String>, Getter<?,?>>();
	/**
	 * A cache of the Setters indexed by class and alias. Where alias is the field name or the sections of the method name 
	 * following an initial 'set' String with the first letter of the remaining string in lower case
	 */
	private static Map<Pair<Class<?>, String>, Setter<?,?>> setters = new HashMap<Pair<Class<?>, String>, Setter<?,?>>();
	/**
	 * This static method returns an instance of Getter for the given object class, value class and alias
	 * If a suitable MethodGetter can be idenified then the MethodGetter is returned otherwise a FieldGetter is returned
	 * if a suitable field can be identified. If no suitable method or field can be identified a null is returned
	 * @param objectClass	The class of the object for which a getter is required
	 * @param valueClass		The class of the value to be returned by the resultant getter
	 * @param alias			The alias of the field or method whose value is to be returned by the getter
	 * @return				A Getter instance for the given values or null if no suitable getter can be identified from the 
	 * 						given object class
	 * @param <E> The class of the object through which the getter will be invoked
	 * @param <V> The class of the values returned by calls to get()
	 */
	@SuppressWarnings("unchecked")
	public static <E,V> Getter<E,V> getGetter(Class<E> objectClass, Class<V> valueClass, String alias) {
		Pair<Class<?>, String> pair = new Pair<Class<?>, String>(objectClass, alias);
		Getter<E,V> g = (Getter<E, V>) getters.get(pair);
		if (g == null) {
			logger.trace("Getter not yet defined");
			Member m = getGetterMember(objectClass, valueClass, alias);
			if (m != null) {
				if (m instanceof Method) {
					g = new MethodGetter<E,V>(objectClass, valueClass, (Method)m);					
				} else if (m instanceof Field){
					g = new FieldGetter<E,V>(objectClass, valueClass, (Field)m);
				}
			}
			
			if (g != null) {
				logger.trace("Caching {} getter {}.{}", g.getType(), g.getThroughClass(), g.getAlias());
				getters.put(pair, g);
		
			}
		} else {
			if (!valueClass.isAssignableFrom(g.getJavaType())) {
				logger.trace("The cached getter {} does not provide the required type {}", g, valueClass.getName());
				g = null;
			}
		}
		logger.trace("Returning getter {}", g);
		return g;
	}
	/**
	 * This static method returns an instance of Getter for the given object class, value class and alias
	 * If a suitable MethodGetter can be idenified then the MethodGetter is returned otherwise a FieldGetter is returned
	 * if a suitable field can be identified. If no suitable method or field can be identified a null is returned
	 * @param objectClass	The class of the object for which a getter is required
	 * @param valueClass		The class of the value to be returned by the resultant getter
	 * @param alias			The alias of the field or method whose value is to be returned by the getter
	 * @return				A Getter instance for the given values or null if no suitable getter can be identified from the 
	 * 						given object class
	 * @param <E> The class of the object through which the getter will be invoked
	 * @param <V> The class of the values returned by calls to get()
	 */
	@SuppressWarnings("unchecked")
	public static <E,V> Setter<E,V> getSetter(Class<E> objectClass, Class<V> valueClass, String alias) {
		Pair<Class<?>, String> pair = new Pair<Class<?>, String>(objectClass, alias);
		Setter<E,V> s = (Setter<E, V>) setters.get(pair);
		if (s == null) {
			logger.trace("Setter not yet defined");
			Member m = getSetterMember(objectClass, valueClass, alias);
			if (m != null) {
				if (m instanceof Method) {
					s = new MethodSetter<E,V>(objectClass, valueClass, (Method)m);					
				} else if (m instanceof Field){
					s = new FieldSetter<E,V>(objectClass, valueClass, (Field)m);
				}
			}
			
			if (s != null) {
				logger.trace("Caching {} setter {}.{}", s.getType(), s.getThroughClass(), s.getAlias());
				setters.put(pair, s);
		
			}
		} else {
			if (!valueClass.isAssignableFrom(s.getJavaType())) {
				logger.trace("The cached setter {} does not set the required type {}", s, valueClass.getName());
				s = null;
			}
		}
		logger.trace("Returning getter {}", s);
		return s;
	}
	public static <E,V> Setter<E,V> getSetter(Class<E> objectClass, Class<V> valueClass) {
		Member m = getSetterMember(objectClass, valueClass);
		if (m != null) {
			if (m instanceof Method) {
				return new MethodSetter<E,V>(objectClass, valueClass, (Method)m);					
			} else if (m instanceof Field){
				return new FieldSetter<E,V>(objectClass, valueClass, (Field)m);
			}
		}
		
		return null;
	}
	/**
	 * This static method checks to see whether a getter is available for the given value class and alias on the given object class
	 * @param objectClass	The class of the object for which a getter is required
	 * @param valueClass		The class of the value to be returned by the resultant getter
	 * @param alias			The alias of the field or method invoked by the getter
	 * @return				True if a getter can be identified from the given object class for the given value class and alias.
	 * 
	 * @param <V> The class of the object returned by calls to get()
	 */
	public static <V> boolean canGet(Class<?> objectClass, Class<?> valueClass, String alias) {
		return (getGetter(objectClass, valueClass, alias) != null);
	}
	
	public static Member getGetterMember(Class<?> cls, Class<?> type, String alias) {
		for (Method m : getAllMethods(cls)) {
			if (type.isAssignableFrom(m.getReturnType()) 
					&& m.getName().equals("get"+StringUtil.initialUpperCase(alias))
					&& m.getParameterCount() == 0) {
				return m;
			}
		}
		for (Field f : getAllFields(cls)) {
			if (type.isAssignableFrom(f.getType()) && f.getName().equals(alias)) {
				return f;
			}
		}
		return null;
	}

	public static Member getSetterMember(Class<?> cls, Class<?> type) {
		for (Method m : getAllMethods(cls)) {
			if (m.getParameterCount() == 1 && m.getParameters()[0].getType().isAssignableFrom(type)) {
				return m;
			}
		}
		for (Field f : getAllFields(cls)) {
			if (type.isAssignableFrom(f.getType())) {
				return f;
			}
		}
		return null;
	}
	
	public static Member getSetterMember(Class<?> cls, Class<?> type, String alias) {
		for (Method m : getAllMethods(cls)) {
			if (m.getName().equals("set"+StringUtil.initialUpperCase(alias))
					&& m.getParameterCount() == 1
					&& m.getParameters()[0].getType().isAssignableFrom(type)) {
				return m;
			}
		}
		for (Field f : getAllFields(cls)) {
			if (type.isAssignableFrom(f.getType()) && f.getName().equals(alias)) {
				return f;
			}
		}
		return null;
	}
	
	public static Method getMethod(Class<?> cls, String name, Class<?> ... parameterTypes) {
		try {
			return cls.getDeclaredMethod(name, parameterTypes);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new UtilityError("The method signature {} does not match a method in class {}", MethodSignature.forSignature(name, parameterTypes), cls.getName());
		}
	}

	public static Method getMethod(Class<?> cls, MethodSignature methodSignature) {
		try {
			return cls.getDeclaredMethod(methodSignature.getName(), methodSignature.getParameterTypes());
		} catch (NoSuchMethodException | SecurityException e) {
			throw new UtilityError("The method signature {} does not match a method in class {}", methodSignature, cls.getName());
		}
	}


	@SuppressWarnings({ "unchecked", "restriction" })
	public static <T> Class<? extends T> createClassFromString(Class<T> extendsClass, String packageName, String className, String source) {

		System.out.println(source);
		
		String fullClassName = packageName.replace('.', '/')+"/"+className;
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		SimpleJavaFileObject simpleJavaFileObject = new SimpleJavaFileObject(URI.create(fullClassName + ".java"), Kind.SOURCE) {

			@Override
			public CharSequence getCharContent(boolean ignoreEncodingErrors) {
				return source;
			}
			
			@Override
			public OutputStream openOutputStream() throws IOException{
				return byteArrayOutputStream;
			}
		};
		
		@SuppressWarnings({ "rawtypes" })
		JavaFileManager javaFileManager = new ForwardingJavaFileManager(
				ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, null, null)) {
						@Override
						public JavaFileObject getJavaFileForOutput(
								Location location,String className,
								JavaFileObject.Kind kind,
								FileObject sibling)  {
							return simpleJavaFileObject;
						}
		};
		
		ToolProvider.getSystemJavaCompiler().getTask(null, javaFileManager, null, null, null, ObjectUtil.singletonList(simpleJavaFileObject)).call();
		
		byte[] bytes = byteArrayOutputStream.toByteArray();
		
		try {
			Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
			
			f.setAccessible(true);
			
			sun.misc.Unsafe unsafe = (sun.misc.Unsafe) f.get(null);
						
			return (Class<? extends T>) unsafe.defineClass(fullClassName, bytes, 0, bytes.length, extendsClass.getClassLoader(), extendsClass.getProtectionDomain());
			
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new UtilityError("Unable to generate class {}.{} from source \n{}\n", e, packageName, className, source);
		}

	}
	
	public static String getAliasFromMethod(Method method) {
		if (method == null) return "";
		return getAliasFromMethodName(method.getName());
	}

	public static String getAliasFromMethodName(String name) {
		if (name == null || "".equals(name)) return "";
		if (name.startsWith("get")||name.startsWith("set"))
			return StringUtil.initialLowerCase(name.substring(3));
		else
			return StringUtil.initialLowerCase(name);
	}
	
	
	public static String getPackageNameFromCanonicalName(String canonicalName) {
		if (canonicalName == null || "".equals(canonicalName)) return "";
		int lastPeriod = canonicalName.lastIndexOf('.');
		if (lastPeriod == -1) return "";
		return canonicalName.substring(0, lastPeriod);
	}
	
	public static String getBasenameFromCanonicalName(String canonicalName) {
		if (canonicalName == null || "".equals(canonicalName)) return "void";
		int lastPeriod = canonicalName.lastIndexOf('.');
		if (lastPeriod == -1) return canonicalName;
		return canonicalName.substring(lastPeriod+1);
	}
	
	public static String title(Class<?> cls) {
		return StringUtil.splitCamelCase(cls.getSimpleName());
	}
	
	public static String alias(Class<?> cls) {
		return StringUtil.aliasCase(title(cls));
	}

	public static String title(Field f) {
		return StringUtil.splitCamelCase(f.getName());
	}
	
	public static String alias(Field f) {
		return StringUtil.aliasCase(title(f));
	}

	public static String alias(Method m) {
		return getAliasFromMethod(m);
	}

	public static String title(Method m) {
		if (m == null) return "";
		if (m.getName().startsWith("get")||m.getName().startsWith("set"))
			return StringUtil.splitCamelCase(m.getName().substring(3));
		else
			return StringUtil.splitCamelCase(m.getName());
	}
	public static boolean isAnnotationPresent(Member member, Class<? extends Annotation> annotation) {
		if (member instanceof Field)
			return ((Field)member).isAnnotationPresent(annotation);
		else if (member instanceof Method)
			return ((Method)member).isAnnotationPresent(annotation);
		throw new UtilityError("Unsupprted member type {}", member.getClass().getName());
	}
	public static <A extends Annotation> A getAnnotation(Member member, Class<A> annotation) {
		if (member instanceof Field)
			return ((Field)member).getAnnotation(annotation);
		else if (member instanceof Method)
			return ((Method)member).getAnnotation(annotation);
		throw new UtilityError("Unsupprted member type {}", member.getClass().getName());
	}
	public static String alias(Member member) {
		if (member instanceof Field)
			return alias((Field)member);
		else if (member instanceof Method)
			return alias((Method)member);
		throw new UtilityError("Unsupprted member type {}", member.getClass().getName());
	}
	public static String title(Member member) {
		if (member instanceof Field)
			return title((Field)member);
		else if (member instanceof Method)
			return title((Method)member);
		throw new UtilityError("Unsupprted member type {}", member.getClass().getName());
	}
	public static <T,A extends Annotation> A getAnnotation(Class<T> cls, Class<A> annotationClass) {
		A ann = cls.getAnnotation(annotationClass);
		if (ann == null && cls.getSuperclass() != Object.class)
			return getAnnotation(cls.getSuperclass(), annotationClass);
		return ann;
	}
	public static <T,A extends Annotation> boolean isAnnotationPresent(Class<T> cls, Class<A> annotationClass) {
		boolean b = cls.isAnnotationPresent(annotationClass);
		if (( ! b) && cls.getSuperclass() != Object.class)
			return isAnnotationPresent(cls.getSuperclass(), annotationClass);
		return b;
	}
	public static <S> boolean isCollection(Class<S> sourceType, String fieldAlias) {
		return (getGetterMember(sourceType, Collection.class, fieldAlias) != null);
	}
}
