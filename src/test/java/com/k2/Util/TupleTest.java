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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

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

public class TupleTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


	@Test
	public void basicTest()
    {
		Tuple3<Integer, String, Boolean> tuple = new Tuple3<>(1, "string", true);
		
		assertEquals(new Integer(1), tuple.a);
		assertEquals("string", tuple.b);
		assertTrue(tuple.c);
	}
	
	@Test
	public void tupleIndexTest() {
		
		Tuple tuple = TupleUtil.tuple(1, "string", true);
		
		assertEquals(new Integer(1), tuple.get(0));
		assertEquals("string", tuple.get(1));
		assertTrue((Boolean)tuple.get(2));

		assertEquals(new Integer(1), tuple.get(0, Integer.class));
		assertEquals("string", tuple.get(1, String.class));
		assertTrue(tuple.get(2, Boolean.class));

	}
	
	@Test
	public void tupleAliasTest() {
		
		Tuple tuple = TupleUtil.tuple(1, "string", true);
		
		assertEquals(new Integer(1), tuple.get("a"));
		assertEquals("string", tuple.get("b"));
		assertTrue((Boolean)tuple.get("c"));

		assertEquals(new Integer(1), tuple.get("a", Integer.class));
		assertEquals("string", tuple.get("b", String.class));
		assertTrue(tuple.get("c", Boolean.class));
		
	}
	
	@Test
	public void tupleElementTest() {
		
		Tuple tuple = TupleUtil.tuple(1, "string", true);
		
		assertEquals(new Integer(1), tuple.get(TupleUtil.tupleElement("a", Integer.class)));
		assertEquals("string", tuple.get(TupleUtil.tupleElement("b", String.class)));
		assertTrue(tuple.get(TupleUtil.tupleElement("c", Boolean.class)));

		assertEquals(new Integer(1), tuple.get(TupleUtil.tupleElement(Integer.class)));
		assertEquals("string", tuple.get(TupleUtil.tupleElement(String.class)));
		assertTrue(tuple.get(TupleUtil.tupleElement(Boolean.class)));

	}

	@Test
	public void aliasedTupleAliasTest() {

		String[] aliases = {"one", "two", "three"};
		Tuple tuple = TupleUtil.aliasedTuple(aliases, 1, "string", true);
		
		assertEquals(new Integer(1), tuple.get("one"));
		assertEquals("string", tuple.get("two"));
		assertTrue((Boolean)tuple.get("three"));

		assertEquals(new Integer(1), tuple.get("one", Integer.class));
		assertEquals("string", tuple.get("two", String.class));
		assertTrue(tuple.get("three", Boolean.class));
		
	}
	
	@Test
	public void aliasedTupleElementTest() {
		
		String[] aliases = {"one", "two", "three"};
		Tuple tuple = TupleUtil.aliasedTuple(aliases, 1, "string", true);
		
		assertEquals(new Integer(1), tuple.get(TupleUtil.tupleElement("one", Integer.class)));
		assertEquals("string", tuple.get(TupleUtil.tupleElement("two", String.class)));
		assertTrue(tuple.get(TupleUtil.tupleElement("three", Boolean.class)));

		assertEquals(new Integer(1), tuple.get(TupleUtil.tupleElement(Integer.class)));
		assertEquals("string", tuple.get(TupleUtil.tupleElement(String.class)));
		assertTrue(tuple.get(TupleUtil.tupleElement(Boolean.class)));

	}

	@Test
	public void largeTupleElementTest() {
		
		Tuple tuple = TupleUtil.tuple(1, "string2", true, 4, "string5", false, 7, "string8", true, 10, "string11", false, 13, "string14", true, 16, "string17", false, 19, "string20", true, 22, "string23", true, 25, "string26");
		
		assertEquals(new Integer(25), tuple.get(TupleUtil.tupleElement("y", Integer.class)));
		assertEquals("string26", tuple.get(TupleUtil.tupleElement("z", String.class)));
		assertTrue(tuple.get(TupleUtil.tupleElement("x", Boolean.class)));
		
		List<TupleElement<?>> list = tuple.getElements();
		
		assertEquals(new Integer(7), tuple.get(list.get(6)));
		assertEquals("string5", tuple.get(list.get(4)));
		assertFalse((Boolean)tuple.get(list.get(17)));
		

	}

	@Test
	public void largeAliasedTupleElementTest() {
		
		String[] aliases = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
		Tuple tuple = TupleUtil.aliasedTuple(aliases, 1, "string2", true, 4, "string5", false, 7, "strin8", true, 10, "string11", false, 13, "string14", true, 16, "string17", false, 19, "string20", true, 22, "string23", true, 25, "string26");
		
		assertEquals(new Integer(1), tuple.get(TupleUtil.tupleElement("a", Integer.class)));
		assertEquals("string2", tuple.get(TupleUtil.tupleElement("b", String.class)));
		assertTrue(tuple.get(TupleUtil.tupleElement("c", Boolean.class)));

	}

}
