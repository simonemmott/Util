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
import com.k2.Util.classes.ClassUtil;
import com.k2.Util.classes.ClassUtil.AnnotationCheck;
import com.k2.Util.exceptions.FileLockedException;
import com.k2.Util.java.JavaUtil;
import com.k2.Util.tuple.Tuple3;
import com.k2.Util.tuple.TupleUtil;

public class JavaUtilTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Test
	public void classUtilTest() {
		
		assertEquals("true", JavaUtil.toJavaSource(true));
		assertEquals("false", JavaUtil.toJavaSource(false));
		assertEquals("Boolean.valueOf(true)", JavaUtil.toJavaSource(Boolean.valueOf(true)));
		assertEquals("Boolean.valueOf(false)", JavaUtil.toJavaSource(Boolean.valueOf(false)));
		
		assertEquals("10", JavaUtil.toJavaSource(10));
		assertEquals("Integer.valueOf(10)", JavaUtil.toJavaSource(Integer.valueOf(10)));
		
		assertEquals("10L", JavaUtil.toJavaSource(10L));
		assertEquals("Long.valueOf(10)", JavaUtil.toJavaSource(Long.valueOf(10)));
		
		assertEquals("(short)10", JavaUtil.toJavaSource((short)10));
		assertEquals("Short.valueOf((short)10)", JavaUtil.toJavaSource(Short.valueOf((short)10)));
		
		assertEquals("(byte)10", JavaUtil.toJavaSource((byte)10));
		assertEquals("Byte.valueOf((byte)10)", JavaUtil.toJavaSource(Byte.valueOf((byte)10)));
		
		assertEquals("1.234f", JavaUtil.toJavaSource(1.234f));
		assertEquals("Float.valueOf(1.234f)", JavaUtil.toJavaSource(Float.valueOf(1.234f)));
		
		assertEquals("1.234", JavaUtil.toJavaSource(1.234));
		assertEquals("Double.valueOf(1.234)", JavaUtil.toJavaSource(Double.valueOf(1.234)));
		
		assertEquals("'\\b'", JavaUtil.toJavaSource('\b'));
		assertEquals("Character.valueOf('\\b')", JavaUtil.toJavaSource(Character.valueOf('\b')));
		assertEquals("'\\t'", JavaUtil.toJavaSource('\t'));
		assertEquals("Character.valueOf('\\t')", JavaUtil.toJavaSource(Character.valueOf('\t')));
		assertEquals("'\\n'", JavaUtil.toJavaSource('\n'));
		assertEquals("Character.valueOf('\\n')", JavaUtil.toJavaSource(Character.valueOf('\n')));
		assertEquals("'\\r'", JavaUtil.toJavaSource('\r'));
		assertEquals("Character.valueOf('\\r')", JavaUtil.toJavaSource(Character.valueOf('\r')));
		assertEquals("'\\f'", JavaUtil.toJavaSource('\f'));
		assertEquals("Character.valueOf('\\f')", JavaUtil.toJavaSource(Character.valueOf('\f')));
		assertEquals("'\\\"'", JavaUtil.toJavaSource('\"'));
		assertEquals("Character.valueOf('\\\"')", JavaUtil.toJavaSource(Character.valueOf('\"')));
		assertEquals("'\\''", JavaUtil.toJavaSource('\''));
		assertEquals("Character.valueOf('\\'')", JavaUtil.toJavaSource(Character.valueOf('\'')));
		assertEquals("'\\\\'", JavaUtil.toJavaSource('\\'));
		assertEquals("Character.valueOf('\\\\')", JavaUtil.toJavaSource(Character.valueOf('\\')));
		assertEquals("'c'", JavaUtil.toJavaSource('c'));
		assertEquals("Character.valueOf('c')", JavaUtil.toJavaSource(Character.valueOf('c')));
		
		assertEquals("\"Hello World!\"", JavaUtil.toJavaSource("Hello World!"));
		assertEquals("\"Hello \\\"World!\\\"\"", JavaUtil.toJavaSource("Hello \"World!\""));
		assertEquals("\"Hello \\\"World! \\b \\t \\n \\r \\f \\\" \\' \\\\ \"", JavaUtil.toJavaSource("Hello \"World! \b \t \n \r \f \" \' \\ "));
		
	}


}
