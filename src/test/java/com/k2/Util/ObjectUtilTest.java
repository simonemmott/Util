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

public class ObjectUtilTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


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
