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

import com.k2.Util.ArrayUtil.CheckType;
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

public class ArrayUtilTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Test
	public void arrayUtilTest() {
		
		Integer[] arr = {1,2,3,4,5,6};
		Integer[] arr2 = {};
		
		assertTrue(ArrayUtil.contains(arr, CheckType.ALL, 1));
		assertTrue(ArrayUtil.contains(arr, CheckType.ALL, 2));
		assertTrue(ArrayUtil.contains(arr, CheckType.ALL, 3));
		assertTrue(ArrayUtil.contains(arr, CheckType.ALL, 4));
		assertTrue(ArrayUtil.contains(arr, CheckType.ALL, 5));
		assertTrue(ArrayUtil.contains(arr, CheckType.ALL, 6));
		assertFalse(ArrayUtil.contains(arr, CheckType.ALL, 0));
		assertFalse(ArrayUtil.contains(arr, CheckType.ALL, 7));

	
		assertTrue(ArrayUtil.contains(arr, CheckType.ALL, 1,2,3));
		assertFalse(ArrayUtil.contains(arr, CheckType.ALL, 1,2,3,7));
		assertTrue(ArrayUtil.contains(arr, CheckType.ANY, 10,20,3,7));
		assertFalse(ArrayUtil.contains(arr, CheckType.ALL, 10,20,30,70));
		
		assertFalse(ArrayUtil.contains(arr2, CheckType.ALL, 1));

		
	}


}
