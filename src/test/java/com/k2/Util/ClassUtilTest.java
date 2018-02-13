package com.k2.Util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.Util.ClassUtil.AnnotationCheck;
import com.k2.Util.DateUtil.DateFormat;
import com.k2.Util.Identity.IdentityUtil;
import com.k2.Util.Sample.Bar;
import com.k2.Util.Sample.Foo;
import com.k2.Util.Sample.NoSnap;
import com.k2.Util.Sample.Snap;
import com.k2.Util.Sample.TestAnnotation1;
import com.k2.Util.Sample.TestAnnotation2;
import com.k2.Util.Sample.Too;
import com.k2.Util.Sample.TooMuch;
import com.k2.Util.Version.Increment;
import com.k2.Util.Version.Version;
import com.k2.Util.Version.VersionExample;
import com.k2.Util.exceptions.FileLockedException;
import com.k2.Util.tuple.Tuple3;
import com.k2.Util.tuple.TupleUtil;

public class ClassUtilTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Test
	public void classUtilTest() {
		
		logger.trace("ClassUtilTest");
		
		assertEquals("com/k2/Util", ClassUtil.packageNameToPath("com.k2.Util"));
		
		Class<?>[] classes = ClassUtil.getClasses("com.k2.Util.Sample");
		
		assertEquals(8, classes.length);
		
		List<Class<?>> classesList = Arrays.asList(classes);
		assertTrue(classesList.contains(Bar.class));
		assertTrue(classesList.contains(Foo.class));
		assertTrue(classesList.contains(NoSnap.class));
		assertTrue(classesList.contains(Snap.class));
		assertTrue(classesList.contains(TestAnnotation1.class));
		assertTrue(classesList.contains(TestAnnotation2.class));
		assertTrue(classesList.contains(Too.class));
		assertTrue(classesList.contains(TooMuch.class));
		
		classesList = Arrays.asList(ClassUtil.getClasses("com.k2.Util"));
		assertTrue(classesList.contains(VersionExample.class));
		assertTrue(classesList.contains(FileLockedException.class));
		
		classesList = Arrays.asList(ClassUtil.getClasses("com.k2.Util", TestAnnotation1.class));
		
		assertEquals(2, classesList.size());
		assertTrue(classesList.contains(Foo.class));
		assertTrue(classesList.contains(Bar.class));
		
		classesList = Arrays.asList(ClassUtil.getClasses("com.k2.Util", TestAnnotation1.class, TestAnnotation2.class));
		
		assertEquals(1, classesList.size());
		assertTrue(classesList.contains(Foo.class));

		classesList = Arrays.asList(ClassUtil.getClasses("com.k2.Util", AnnotationCheck.ANY, TestAnnotation1.class, TestAnnotation2.class));
		
		assertEquals(3, classesList.size());
		assertTrue(classesList.contains(Bar.class));
		assertTrue(classesList.contains(Foo.class));
		assertTrue(classesList.contains(Too.class));

		
		
	}

	private class A {
		int a;
		A(int a) { this.a=a; }
	}
	
	private class AB extends A {
		int b;
		AB(int a, int b) { super(a); this.b=b; }
	}
	
	private class ABC extends AB {
		int c;
		AB ab;
		AB getAAndB() { return ab; }
		ABC(int a, int b, int c) { super(a,b); this.c=c; }
	}

	private class AD extends A {
		int d;
		AD(int a, int d) { super(a); this.d=d; }
	}
	
	@Test
	public void canGetTest() {
		assertTrue(ClassUtil.canGet(ABC.class, int.class, "a"));
		assertTrue(ClassUtil.canGet(ABC.class, int.class, "b"));
		assertTrue(ClassUtil.canGet(ABC.class, int.class, "c"));
		assertTrue(ClassUtil.canGet(ABC.class, AB.class, "ab"));
		assertTrue(ClassUtil.canGet(ABC.class, A.class, "ab"));
		assertTrue(ClassUtil.canGet(ABC.class, AB.class, "aAndB"));
		assertTrue(ClassUtil.canGet(ABC.class, A.class, "aAndB"));
		assertFalse(ClassUtil.canGet(ABC.class, String.class, "c"));
		assertFalse(ClassUtil.canGet(ABC.class, int.class, "d"));
	}
	
	@Test
	public void getSupertypesTest() {
		
		List<Class<?>> l = ClassUtil.getSupertypes(ABC.class);
		for (int i=0; i<l.size(); i++) {
			logger.trace("ABC supertype[{}] {}", i, l.get(i).getSimpleName());
		}
		assertEquals(4, l.size());

	}

	@Test
	public void findMatchingSupertypeTest() {
		
		Class<?> cls = ClassUtil.findMatchingSupertype(ABC.class, AD.class);
		assertEquals(A.class, cls);

	}

}
