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
import javax.persistence.criteria.CriteriaBuilder.Trimspec;

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

public class StringTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


	@Test
	public void StringUtilTest() {
		
		logger.trace("StringUtilTest");

		assertEquals(new Integer(123), StringUtil.toInteger("123"));
		assertEquals(new Integer(987), StringUtil.toInteger("987"));
		assertEquals(new Long(123), StringUtil.toLong("123"));
		assertEquals(new Long(987), StringUtil.toLong("987"));
		assertEquals(new Float(123.456), StringUtil.toFloat("123.456"));
		assertEquals(new Float(987.654), StringUtil.toFloat("987.654"));
		assertEquals(new Double(123.456), StringUtil.toDouble("123.456"));
		assertEquals(new Double(987.654), StringUtil.toDouble("987.654"));
		assertEquals(new Date(12345000), StringUtil.toDate("1970-01-01 04:25:45"));
		assertEquals(new Date(987654000), StringUtil.toDate("1970-01-12 11:20:54"));
		assertEquals("Foo", StringUtil.toString("Foo"));
		assertEquals("Bar", StringUtil.toString("Bar"));
		
		assertTrue(StringUtil.toBoolean("true"));
		assertFalse(StringUtil.toBoolean("false"));
		
		
		assertEquals("100", StringUtil.toString(100));
		assertEquals("0", StringUtil.toString(0));
		assertEquals("100", StringUtil.toString(100L));
		assertEquals("0", StringUtil.toString(0L));
		assertEquals("123.456", StringUtil.toString(new Float(123.456)));
		assertEquals("0.0", StringUtil.toString(new Float(0.0)));
		assertEquals("123.456", StringUtil.toString(new Double(123.456)));
		assertEquals("0.0", StringUtil.toString(new Double(0.0)));
		assertEquals("2016-08-30 23:25:59", StringUtil.toString(new Date(1472595959000L)));
		assertEquals("1970-01-01 01:00:00", StringUtil.toString(new Date(0)));
		assertEquals("123.456", StringUtil.toString("123.456"));
		assertEquals("987.654", StringUtil.toString("987.654"));
		assertEquals("true", StringUtil.toString(true));
		assertEquals("false", StringUtil.toString(false));
		
		StringUtil.registerTypeConverter(new TypeConverter<Foo,String>() {

			@Override
			public Class<Foo> convertClass() { return Foo.class; }

			@Override
			public String convert(Object value) {
				if (value instanceof Foo) {
					Foo foo = (Foo)value;
					return foo.description;
				}
				return null;
			}});
		
		
		Foo foo1 = new Foo();
		foo1.description = "foo one";
		Foo foo2 = new Foo();
		foo2.description = "foo two";
		
		assertEquals("foo one", StringUtil.toString(foo1));
		assertEquals("foo two", StringUtil.toString(foo2));
		assertNull(StringUtil.toString((Foo)null));
		
		assertEquals("aSDFGHJ", StringUtil.initialLowerCase("ASDFGHJ"));
		assertEquals("Asdfghj", StringUtil.initialUpperCase("asdfghj"));
		assertFalse(StringUtil.isSet(""));
		assertTrue(StringUtil.isSet("AAA"));
		assertFalse(StringUtil.isSet(null));
		assertEquals("hello", StringUtil.safeWord("hello"));
		assertEquals("hello", StringUtil.safeWord("!@#$%^&*()hello!@#$%^&*()"));
		assertEquals("thisIsMyPhrase", StringUtil.aliasCase("this is my phrase"));
		assertEquals("This Is My Phrase", StringUtil.camelCase("this is my phrase"));
		assertEquals("ThisIsMyPhrase", StringUtil.classCase("this is my phrase"));
		assertEquals("THIS_IS_MY_PHRASE", StringUtil.staticCase("this is my phrase"));
		assertEquals("this-is-my-phrase", StringUtil.kebabCase("this is my phrase"));
		assertEquals("_1thisIsMyPhrase", StringUtil.aliasCase("1this is my phrase"));
		assertEquals("1this Is My Phrase", StringUtil.camelCase("1this is my phrase"));
		assertEquals("_1thisIsMyPhrase", StringUtil.classCase("1this is my phrase"));
		assertEquals("_1THIS_IS_MY_PHRASE", StringUtil.staticCase("1this is my phrase"));
		assertEquals("1this-is-my-phrase", StringUtil.kebabCase("1this is my phrase"));
		StringBuilder sb = new StringBuilder();
		sb.append("replaced");
		assertEquals("This has been replaced properly", StringUtil.replaceAll("This {} been {} properly", "{}", "has", sb));
		assertEquals("This has been replaced properly {}", StringUtil.replaceAll("This {} been {} properly {}", "{}", "has", sb));
		assertEquals("This has been {} properly", StringUtil.replaceAll("This {} been {} properly", "{}", "has"));


	}
	
	@Test
	public void stringToIndexTest() {
		assertEquals(0, StringUtil.stringToIndex("a"));
		assertEquals(25, StringUtil.stringToIndex("z"));
		assertEquals(26, StringUtil.stringToIndex("aa"));
		assertEquals(51, StringUtil.stringToIndex("az"));
		assertEquals(52, StringUtil.stringToIndex("ba"));
		assertEquals(77, StringUtil.stringToIndex("bz"));
		assertEquals(78, StringUtil.stringToIndex("ca"));
		assertEquals(701, StringUtil.stringToIndex("zz"));
		assertEquals(702, StringUtil.stringToIndex("aaa"));
		assertEquals(1377, StringUtil.stringToIndex("azz"));
		assertEquals(1378, StringUtil.stringToIndex("baa"));
	}

	@Test
	public void indexToStringTest() {
		assertEquals("a", StringUtil.indexToString(0));
		assertEquals("z", StringUtil.indexToString(25));
		assertEquals("aa", StringUtil.indexToString(26));
		assertEquals("az", StringUtil.indexToString(51));
		assertEquals("ba", StringUtil.indexToString(52));
		assertEquals("bz", StringUtil.indexToString(77));
		assertEquals("ca", StringUtil.indexToString(78));
		assertEquals("zz", StringUtil.indexToString(701));
		assertEquals("aaa", StringUtil.indexToString(702));
		assertEquals("azz", StringUtil.indexToString(1377));
		assertEquals("baa", StringUtil.indexToString(1378));
	}
	
	@Test
	public void stringIndexTest() {
		for (int i=0; i<100; i++) {
			int rnd = IntegerUtil.random(100000000);
			assertEquals(rnd, StringUtil.stringToIndex(StringUtil.indexToString(rnd)));
		}
	}

	@Test
	public void trimTest() {
		assertEquals("hello world!", StringUtil.trim(Trimspec.BOTH, ' ', "   hello world!   "));
		assertEquals("hello world!   ", StringUtil.trim(Trimspec.LEADING, ' ', "   hello world!   "));
		assertEquals("   hello world!", StringUtil.trim(Trimspec.TRAILING, ' ', "   hello world!   "));
		assertEquals("hello world!", StringUtil.trim(Trimspec.BOTH, '.', "...hello world!..."));
		assertEquals("hello world!...", StringUtil.trim(Trimspec.LEADING, '.', "...hello world!..."));
		assertEquals("...hello world!", StringUtil.trim(Trimspec.TRAILING, '.', "...hello world!..."));
		assertEquals("hello world!", StringUtil.trim(Trimspec.BOTH, 's', "ssshello world!sss"));
		assertEquals("hello world!sss", StringUtil.trim(Trimspec.LEADING, 's', "ssshello world!sss"));
		assertEquals("ssshello world!", StringUtil.trim(Trimspec.TRAILING, 's', "ssshello world!sss"));
		assertEquals("hello world!", StringUtil.trim(Trimspec.BOTH, '\\', "\\\\hello world!\\\\"));
		assertEquals("hello world!\\\\", StringUtil.trim(Trimspec.LEADING, '\\', "\\\\hello world!\\\\"));
		assertEquals("\\\\hello world!", StringUtil.trim(Trimspec.TRAILING, '\\', "\\\\hello world!\\\\"));
	}
	

}
