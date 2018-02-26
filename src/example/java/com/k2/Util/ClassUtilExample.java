package com.k2.Util;

import com.k2.Util.Sample.TestAnnotation1;
import com.k2.Util.Sample.TestAnnotation2;
import com.k2.Util.classes.ClassUtil;
import com.k2.Util.classes.ClassUtil.AnnotationCheck;

public class ClassUtilExample {
	
	public static void main(String[] args) {
		
		Class<?>[] classes = ClassUtil.getClasses("com.k2.Util");
		for (Class<?> cls : classes) {
			System.out.println("Found: "+cls);
		}

		System.out.println("BREAK!");
		
		classes = ClassUtil.getClasses("com.k2.Util", AnnotationCheck.ANY, TestAnnotation1.class, TestAnnotation2.class);
		for (Class<?> cls : classes) {
			System.out.println("Found: "+cls);
		}
}

}
