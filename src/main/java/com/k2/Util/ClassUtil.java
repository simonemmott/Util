package com.k2.Util;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.k2.Util.exceptions.UtilityError;

/**
 * The ClassUtil provides static methods for dealing with and finding classes
 * @author simon
 *
 */
public class ClassUtil {
	
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
        		UtilsLogger.warning("The directory '"+directory.getAbsolutePath()+"' does not exist while scanning package '"+packageName+"'");
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
						UtilsLogger.warning("No class defined with name '"+findClassForName+"' while scanning package '"+packageName+"'", e);
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
            
}
