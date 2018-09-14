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
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.k2.Util.classes.ClassUtil.AnnotationCheck;
import com.k2.Util.exceptions.FileLockedException;
import com.k2.Util.tuple.Tuple3;
import com.k2.Util.tuple.TupleUtil;

public class ObjectUtilTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private class E {
		public String name;
		public Long id;
		public int number;
		public B b;
		public List<B> bs;
	}
	
	private class B {
		public String name;
		public B(String name) { this.name = name; }
	}

	@Test
	public void equivalentTest() {
		
		E a1 = new E();
		a1.name = "a1";
		a1.id = 100L;
		a1.number = 0;
		a1.b = new B("a1.b");
		a1.bs = new ArrayList<B>();
		a1.bs.add(new B("b1"));
		a1.bs.add(new B("b2"));
		a1.bs.add(new B("b3"));
		
		E a1copy = new E();
		a1copy.name = "a1";
		a1copy.id = 100L;
		a1copy.number = 0;
		a1copy.b = new B("a1.b");
		a1copy.bs = new ArrayList<B>();
		a1copy.bs.add(new B("b1"));
		a1copy.bs.add(new B("b2"));
		a1copy.bs.add(new B("b3"));
		
		E different1 = new E();
		different1.name = "a2";
		different1.id = 100L;
		different1.number = 0;
		different1.b = new B("a1.b");
		different1.bs = new ArrayList<B>();
		different1.bs.add(new B("b1"));
		different1.bs.add(new B("b2"));
		different1.bs.add(new B("b3"));
		
		E differentNull = new E();
		differentNull.name = "a1";
		differentNull.id = null;
		differentNull.number = 0;
		differentNull.b = new B("a1.b");
		differentNull.bs = new ArrayList<B>();
		differentNull.bs.add(new B("b1"));
		differentNull.bs.add(new B("b2"));
		differentNull.bs.add(new B("b3"));
		
		E differentPrimitive = new E();
		differentPrimitive.name = "a1";
		differentPrimitive.id = 100L;
		differentPrimitive.number = 1;
		differentPrimitive.b = new B("a1.b");
		differentPrimitive.bs = new ArrayList<B>();
		differentPrimitive.bs.add(new B("b1"));
		differentPrimitive.bs.add(new B("b2"));
		differentPrimitive.bs.add(new B("b3"));
		
		E differentObject = new E();
		differentObject.name = "a1";
		differentObject.id = 100L;
		differentObject.number = 0;
		differentObject.b = new B("differentObject.b");
		differentObject.bs = new ArrayList<B>();
		differentObject.bs.add(new B("b1"));
		differentObject.bs.add(new B("b2"));
		differentObject.bs.add(new B("b3"));
		
		E differentCollection1 = new E();
		differentCollection1.name = "a1";
		differentCollection1.id = null;
		differentCollection1.number = 0;
		differentCollection1.b = new B("a1.b");
		differentCollection1.bs = new ArrayList<B>();
		differentCollection1.bs.add(new B("b1"));
		differentCollection1.bs.add(new B("b2"));
		differentCollection1.bs.add(new B("b3"));
		differentCollection1.bs.add(new B("b4"));
		
		E differentCollection2 = new E();
		differentCollection2.name = "a1";
		differentCollection2.id = null;
		differentCollection2.number = 0;
		differentCollection2.b = new B("a1.b");
		differentCollection2.bs = new ArrayList<B>();
		differentCollection2.bs.add(new B("b1"));
		differentCollection2.bs.add(new B("b2"));
		
		E differentCollection3 = new E();
		differentCollection3.name = "a1";
		differentCollection3.id = null;
		differentCollection3.number = 0;
		differentCollection3.b = new B("a1.b");
		differentCollection3.bs = new ArrayList<B>();
		differentCollection3.bs.add(new B("b1"));
		differentCollection3.bs.add(new B("b2"));
		differentCollection3.bs.add(new B("b4"));
		
		assertTrue(ObjectUtil.equivalent(a1, a1copy));
		assertFalse(ObjectUtil.equivalent(a1, different1));
		assertFalse(ObjectUtil.equivalent(a1, differentNull));
		assertFalse(ObjectUtil.equivalent(a1, differentPrimitive));
		assertFalse(ObjectUtil.equivalent(a1, differentObject));
		assertFalse(ObjectUtil.equivalent(a1, differentCollection1));
		assertFalse(ObjectUtil.equivalent(a1, differentCollection2));
		assertFalse(ObjectUtil.equivalent(a1, differentCollection3));
		
	}
	
	
	@Test
	public void findFirstMatchTest() {
		
		List<Integer> l1 = new ArrayList<Integer>();
		l1.add(1);
		l1.add(2);
		l1.add(3);
		l1.add(4);
		l1.add(5);
		l1.add(6);

		List<Integer> l2 = new ArrayList<Integer>();
		l2.add(10);
		l2.add(7);
		l2.add(20);
		l2.add(-1);
		l2.add(5);
		l2.add(6);
		
		assertEquals(new Integer(5), ObjectUtil.findFirstMatch(l1, l2));

	}
	
	private class A {
		private Integer id;
		public String name;
		public String getIdAndName() { return id+name; }
		A(int id, String name) {this.id = id; this.name = name;}
		public int getId() { return id; }
	}

	@Test
	public void cloneTest() {
		
		A a = new A(10, "This is an A");
		A a2 = new A(20, "This is another A");
		
		assertEquals(10, a.getId());
		assertEquals(20, a2.getId());
		assertEquals("This is an A", a.name);
		assertEquals("This is another A", a2.name);
		
		ObjectUtil.copy(a, a2);
		
		assertEquals(10, a.getId());
		assertEquals(10, a2.getId());
		assertEquals("This is an A", a.name);
		assertEquals("This is an A", a2.name);

	}
	
	@Test
	public void getTest() {
		A a = new A(1, "hello");
		assertEquals(new Integer(1), ObjectUtil.get(a, Integer.class, "id"));
		assertEquals("hello", ObjectUtil.get(a, String.class, "name"));
		assertEquals("1hello", ObjectUtil.get(a, String.class, "idAndName"));
	}

}
