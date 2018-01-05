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

public class UtilTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


	@Test
	public void VersionTest()
    {
		logger.trace("VersionTest");
		
        Version testVer = Version.create(1,2,3);
        assertEquals("v1.2.3", testVer.toString());
        
        testVer.increment(Increment.POINT);
        assertEquals("v1.2.4", testVer.toString());
        
        testVer.increment(Increment.MINOR);
        assertEquals("v1.3.0", testVer.toString());
        
        testVer.increment(Increment.MAJOR);
        assertEquals("v2.0.0", testVer.toString());
        
        Version checkVer = Version.create(2,0,0);
        assertEquals(checkVer, testVer);
        
        Version oldVersion = Version.create(1,2,3);
        Version newVersionPoint = Version.create(2,0,1);
        Version newVersionMinor = Version.create(2,1,0);
        Version newVersionMajor = Version.create(3,0,0);

        assertTrue(testVer.includes(oldVersion));
        assertFalse(testVer.includes(newVersionPoint));
        assertFalse(testVer.includes(newVersionMinor));
        assertFalse(testVer.includes(newVersionMajor));
        
        testVer.increment(Increment.MINOR);
        testVer.increment(Increment.MINOR);
        
        testVer.increment(Increment.POINT);
        testVer.increment(Increment.POINT);
        testVer.increment(Increment.POINT);
        testVer.increment(Increment.POINT);
        
        assertEquals("v2.2.4", testVer.toString());
        
        assertTrue(testVer.includes(oldVersion));
        
        oldVersion.increment(Increment.MAJOR);
        assertEquals("v2.0.0", oldVersion.toString());
        assertTrue(testVer.includes(oldVersion));

        oldVersion.increment(Increment.MINOR);
        assertEquals("v2.1.0", oldVersion.toString());
        assertTrue(testVer.includes(oldVersion));

        oldVersion.increment(Increment.MINOR);
        assertEquals("v2.2.0", oldVersion.toString());
        assertTrue(testVer.includes(oldVersion));

        oldVersion.increment(Increment.POINT);
        oldVersion.increment(Increment.POINT);
        oldVersion.increment(Increment.POINT);
        assertEquals("v2.2.3", oldVersion.toString());
        assertTrue(testVer.includes(oldVersion));

        oldVersion.increment(Increment.POINT);
        assertEquals("v2.2.4", oldVersion.toString());
        assertTrue(testVer.includes(oldVersion));

        oldVersion.increment(Increment.POINT);
        assertEquals("v2.2.5", oldVersion.toString());
        assertFalse(testVer.includes(oldVersion));

        oldVersion.increment(Increment.MINOR);
        assertEquals("v2.3.0", oldVersion.toString());
        assertFalse(testVer.includes(oldVersion));

        oldVersion.increment(Increment.MAJOR);
        assertEquals("v3.0.0", oldVersion.toString());
        assertFalse(testVer.includes(oldVersion));

    }

	@Test
	public void IdentityTest() throws IllegalAccessException {
		
		logger.trace("IdentityTest");
		
		Foo foo = new Foo();
		foo.id = 10000L;
		foo.name = "foo";
		foo.description = "this is a foo";
		
		Bar bar = new Bar();
		bar.id = 20000L;
		bar.name = "bar";
		bar.description = "this is a bar";
		bar.bar = "iron";
		bar.humbug = "a sweet";
		
		Too too = new Too();
		too.id = 30000L;
		too.name = "too";
		too.description = "this is a too";
		
		TooMuch tooMuch = new TooMuch();
		tooMuch.id = 40000L;
		tooMuch.name = "too much";
		tooMuch.description = "this is too much";
		tooMuch.much = "This is way to much";
		
		Snap snap = new Snap();
		snap.aw = "Oh snap!";
		
		NoSnap noSnap = new NoSnap();
		noSnap.aw = "Oh yeah!";
		
		assertEquals(foo.id, IdentityUtil.getId(foo));
		assertEquals(bar.id, IdentityUtil.getId(bar));
		assertEquals(too.id, IdentityUtil.getId(too));
		assertEquals(tooMuch.id, IdentityUtil.getId(tooMuch));
		assertEquals("Snap", IdentityUtil.getId(snap));
		assertEquals(noSnap.aw, IdentityUtil.getId(noSnap));
		
	}

	@Test
	public void BooleanUtilTest() {
		
		logger.trace("BooleanUtilTest");
		
		assertEquals(new Integer(1), BooleanUtil.toInteger(true));
		assertEquals(new Integer(0), BooleanUtil.toInteger(false));
		assertEquals(new Long(1), BooleanUtil.toLong(true));
		assertEquals(new Long(0), BooleanUtil.toLong(false));
		assertEquals(new Float(1.0), BooleanUtil.toFloat(true));
		assertEquals(new Float(0.0), BooleanUtil.toFloat(false));
		assertEquals(new Double(1.0), BooleanUtil.toDouble(true));
		assertEquals(new Double(0.0), BooleanUtil.toDouble(false));
		assertEquals(new Date(), BooleanUtil.toDate(true));
		assertEquals(new Date(0), BooleanUtil.toDate(false));
		assertEquals("true", BooleanUtil.toString(true));
		assertEquals("false", BooleanUtil.toString(false));
		
		assertTrue(BooleanUtil.toBoolean(100));
		assertFalse(BooleanUtil.toBoolean(0));
		assertTrue(BooleanUtil.toBoolean(100L));
		assertFalse(BooleanUtil.toBoolean(0L));
		assertTrue(BooleanUtil.toBoolean(new Float(123.456)));
		assertFalse(BooleanUtil.toBoolean(new Float(0.0)));
		assertTrue(BooleanUtil.toBoolean(new Double(123.456)));
		assertFalse(BooleanUtil.toBoolean(new Double(0.0)));
		assertTrue(BooleanUtil.toBoolean(new Date()));
		assertFalse(BooleanUtil.toBoolean(new Date(0)));
		assertTrue(BooleanUtil.toBoolean("Anything but false"));
		assertTrue(BooleanUtil.toBoolean("true"));
		assertFalse(BooleanUtil.toBoolean("false"));
		assertFalse(BooleanUtil.toBoolean("FALSE"));
		assertFalse(BooleanUtil.toBoolean("False"));
		assertFalse(BooleanUtil.toBoolean(""));
		assertFalse(BooleanUtil.toBoolean("0"));
		assertFalse(BooleanUtil.toBoolean("0.0"));
		
		BooleanUtil.trueAsInt(-1);
		BooleanUtil.falseAsInt(99999999);
		assertEquals(new Integer(-1), BooleanUtil.toInteger(true));
		assertEquals(new Integer(99999999), BooleanUtil.toInteger(false));
		assertTrue(BooleanUtil.toBoolean(100));
		assertFalse(BooleanUtil.toBoolean(99999999));
		
		BooleanUtil.falseAsString("F");
		BooleanUtil.trueAsString("T");
		assertEquals("T", BooleanUtil.toString(true));
		assertEquals("F", BooleanUtil.toString(false));
		assertTrue(BooleanUtil.toBoolean("T"));
		assertFalse(BooleanUtil.toBoolean("F"));
		assertTrue(BooleanUtil.toBoolean("t"));
		assertFalse(BooleanUtil.toBoolean("f"));
		assertTrue(BooleanUtil.toBoolean("false"));
		
		BooleanUtil.restoreDefaults();
		
		BooleanUtil.registerTypeConverter(new TypeConverter<Foo,Boolean>() {

			@Override
			public Class<Foo> convertClass() { return Foo.class; }

			@Override
			public Boolean convert(Object value) {
				if (value instanceof Foo) {
					Foo foo = (Foo)value;
					if (foo.name == null) return false;
					return foo.name.equals(foo.description);
				}
				return null;
			}});
		
		Foo foo = new Foo();
		foo.name = "foo";
		foo.description = "foo";
		assertTrue(BooleanUtil.toBoolean(foo));
		foo.description = "false";
		assertFalse(BooleanUtil.toBoolean(foo));
		
		
	}

	@Test
	public void ClassUtilTest() {
		
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

	@Test
	public void DateUtilTest() {
		
		logger.trace("DateUtilTest");
		
		assertEquals(new Integer(1000), DateUtil.toInteger(new Date(1000)));
		assertEquals(new Integer(0), DateUtil.toInteger(new Date(0)));
		assertEquals(new Long(1200), DateUtil.toLong(new Date(1200)));
		assertEquals(new Long(10), DateUtil.toLong(new Date(10)));
		assertEquals(new Float(3000.0), DateUtil.toFloat(new Date(3000)));
		assertEquals(new Float(0.0), DateUtil.toFloat(new Date(0)));
		assertEquals(new Double(4000.0), DateUtil.toDouble(new Date(4000)));
		assertEquals(new Double(12345.0), DateUtil.toDouble(new Date(12345)));
		assertEquals(new Date(50000), DateUtil.toDate(new Date(50000)));
		assertEquals(new Date(300000), DateUtil.toDate(new Date(300000)));
		assertEquals("1970-01-01 04:25:45", DateUtil.toString(new Date(12345678)));
		assertEquals("1970-01-12 11:20:54", DateUtil.toString(new Date(987654321)));
		
		assertTrue(DateUtil.toBoolean(new Date()));
		assertFalse(DateUtil.toBoolean(new Date(0)));
		
		
		assertEquals(new Date(100), DateUtil.toDate(100L));
		assertEquals(new Date(0), DateUtil.toDate(0L));
		assertEquals(new Date(123), DateUtil.toDate(new Float(123.456)));
		assertEquals(new Date(0), DateUtil.toDate(new Float(0.0)));
		assertEquals(new Date(123), DateUtil.toDate(new Double(123.456)));
		assertEquals(new Date(0), DateUtil.toDate(new Double(0.0)));
		assertEquals(new Date(), DateUtil.toDate(new Date()));
		assertEquals(new Date(0), DateUtil.toDate(new Date(0)));
		assertEquals(new Date(1472595959000L), DateUtil.toDate("2016-08-30 23:25:59"));
		assertEquals(new Date(0), DateUtil.toDate("1970-01-01 01:00:00"));
//		assertEquals(new Date(), DateUtil.toDate(true));
		assertEquals(new Date(0), DateUtil.toDate(false));
		assertNull(DateUtil.toDate("XXXX"));
		
		DateUtil.registerTypeConverter(new TypeConverter<Foo,Date>() {

			@Override
			public Class<Foo> convertClass() { return Foo.class; }

			@Override
			public Date convert(Object value) {
				if (value instanceof Foo) {
					Foo foo = (Foo)value;
					return foo.fooDate;
				}
				return null;
			}});
		
		
		Foo foo1 = new Foo();
		foo1.fooDate = new Date(9999999999999L);
		Foo foo2 = new Foo();
		foo2.fooDate = new Date(111111111111111L);
		
		assertEquals(new Date(9999999999999L), DateUtil.toDate(foo1));
		assertEquals(new Date(111111111111111L), DateUtil.toDate(foo2));
		assertNull(DateUtil.toDate((Foo)null));
		
		DateUtil.defaultDateFormat(DateFormat.DATE);
		assertEquals("1970-01-01", DateUtil.toString(new Date(12345678)));
		assertEquals("1970-01-12", DateUtil.toString(new Date(987654321)));
		
		DateUtil.dateFormatter(new SimpleDateFormat("yyyy/dd/MM"));
		assertEquals("1970/01/01", DateUtil.toString(new Date(12345678)));
		assertEquals("1970/12/01", DateUtil.toString(new Date(987654321)));
		
		DateUtil.dateTimeFormatter(new SimpleDateFormat("yyyy/dd/MM HH:mm:ss"));
		DateUtil.defaultDateFormat(DateFormat.DATE_TIME);
		assertEquals("1970/01/01 04:25:45", DateUtil.toString(new Date(12345678)));
		assertEquals("1970/12/01 11:20:54", DateUtil.toString(new Date(987654321)));
		assertEquals("1970/12/01", DateUtil.toString(new Date(987654321), DateFormat.DATE));
		assertEquals("70 AM AD", DateUtil.toString(new Date(987654321), new SimpleDateFormat("yy a G")));
		assertEquals("1970 AM AD", DateUtil.toString(new Date(987654321), "yyyy a G"));
		
		DateUtil.restoreDefaults();
		assertEquals("1970-01-01 04:25:45", DateUtil.toString(new Date(12345678)));

		
		
	}

	@Test
	public void DoubleUtilTest() {
		
		logger.trace("DoubleUtilTest");
		
		assertEquals(new Integer(123), DoubleUtil.toInteger(new Double(123.456)));
		assertEquals(new Integer(987), DoubleUtil.toInteger(new Double(987.654)));
		assertEquals(new Long(123), DoubleUtil.toLong(new Double(123.456)));
		assertEquals(new Long(987), DoubleUtil.toLong(new Double(987.654)));
		assertEquals(new Float(123.456), DoubleUtil.toFloat(new Double(123.456)));
		assertEquals(new Float(987.654), DoubleUtil.toFloat(new Double(987.654)));
		assertEquals(new Double(123.456), DoubleUtil.toDouble(new Double(123.456)));
		assertEquals(new Double(987.654), DoubleUtil.toDouble(new Double(987.654)));
		assertEquals(new Date(123), DoubleUtil.toDate(new Double(123.456)));
		assertEquals(new Date(987), DoubleUtil.toDate(new Double(987.654)));
		assertEquals("123.456", DoubleUtil.toString(new Double(123.456)));
		assertEquals("987.654", DoubleUtil.toString(new Double(987.654)));
		
		assertTrue(DoubleUtil.toBoolean(new Double(123.456)));
		assertFalse(DoubleUtil.toBoolean(new Double(0.0)));
		
		
		assertEquals(new Double(100), DoubleUtil.toDouble(100));
		assertEquals(new Double(0), DoubleUtil.toDouble(0));
		assertEquals(new Double(100), DoubleUtil.toDouble(100L));
		assertEquals(new Double(0), DoubleUtil.toDouble(0L));
		assertEquals(new Double(new Float(123.456)), DoubleUtil.toDouble(new Float(123.456)));
		assertEquals(new Double(0), DoubleUtil.toDouble(new Float(0.0)));
		assertEquals(new Double(123.456), DoubleUtil.toDouble(new Double(123.456)));
		assertEquals(new Double(0), DoubleUtil.toDouble(new Double(0.0)));
		assertEquals(new Double(1472595959000L), DoubleUtil.toDouble(new Date(1472595959000L)));
		assertEquals(new Double(0), DoubleUtil.toDouble(new Date(0)));
		assertEquals(new Double(123.456), DoubleUtil.toDouble("123.456"));
		assertEquals(new Double(987.654), DoubleUtil.toDouble("987.654"));
		assertEquals(new Double(1), DoubleUtil.toDouble(true));
		assertEquals(new Double(0), DoubleUtil.toDouble(false));
		assertNull(DoubleUtil.toDouble("XXXX"));
		
		DoubleUtil.registerTypeConverter(new TypeConverter<Foo,Double>() {

			@Override
			public Class<Foo> convertClass() { return Foo.class; }

			@Override
			public Double convert(Object value) {
				if (value instanceof Foo) {
					Foo foo = (Foo)value;
					return foo.fooDouble;
				}
				return null;
			}});
		
		
		Foo foo1 = new Foo();
		foo1.fooDouble = new Double(123.456);
		Foo foo2 = new Foo();
		foo2.fooDouble = new Double(987.654);
		
		assertEquals(new Double(123.456), DoubleUtil.toDouble(foo1));
		assertEquals(new Double(987.654), DoubleUtil.toDouble(foo2));
		assertNull(DoubleUtil.toDouble((Foo)null));
		

	}
	

	
	@Test
	public void BuildFileTreeTest() throws IOException, FileLockedException, InterruptedException {
		
		logger.trace("BuildFileTreeTest");
		
		File testingLocation = new File("example"+File.separatorChar+"root");
		FileUtil.deleteCascade(testingLocation);
		
		assertTrue(!testingLocation.exists());
		
		logger.debug("Creating directory example/new");
		testingLocation.mkdir();
		
		assertTrue(testingLocation.exists());
		
		Path p1 = Paths.get("dir1");
		Path p2 = Paths.get("dir2");
		Path p3 = Paths.get("dir2", "childDir1");
		Path p4 = Paths.get("dir2", "childDir2");
		
		logger.debug("Building file tree");
		FileUtil.buildTree(testingLocation, p1, p2, p3, p4);
		
		assertTrue(new File(testingLocation.getAbsolutePath()+File.separatorChar+p1.toString()).exists());
		assertTrue(new File(testingLocation.getAbsolutePath()+File.separatorChar+p2.toString()).exists());
		assertTrue(new File(testingLocation.getAbsolutePath()+File.separatorChar+p3.toString()).exists());
		assertTrue(new File(testingLocation.getAbsolutePath()+File.separatorChar+p4.toString()).exists());
				
	}
	
	@Test
	public void FileUtilTest() throws IOException, FileLockedException, InterruptedException {
		
		logger.trace("FileUtilTest");

		File example = new File("example");
		
		File sampleTxt = FileUtil.fetch(example, "sample.txt");
		
		assertTrue(sampleTxt.exists());
		@SuppressWarnings("resource")
		Scanner sample = new Scanner(sampleTxt);
		while (sample.hasNextLine()) assertEquals("This is a text file", sample.nextLine());
		
		List<File> txtFiles = FileUtil.listForExtension(example, ".txt");
		assertEquals(2, txtFiles.size());
		
		txtFiles = FileUtil.listForExtension(example, "txt");
		assertEquals(2, txtFiles.size());
		
		txtFiles = FileUtil.listForExtension(example, ".json");
		assertEquals(1, txtFiles.size());
		
		txtFiles = FileUtil.listForExtension(example, ".xml");
		assertEquals(1, txtFiles.size());
		
		txtFiles = FileUtil.listForExtension(example, ".xml", ".json");
		assertEquals(2, txtFiles.size());
				
	}
	
	@Test
	public void FileCascadeDeleteTest() throws IOException, FileLockedException, InterruptedException {
		
		logger.trace("FileCascadeDeleteTest");

		File newDir = new File("example/new");
		newDir.mkdirs();
		assertTrue(newDir.exists());
		File newFile = new File("example/new/new.txt");
		newFile.createNewFile();
		assertTrue(newFile.exists());
		File newFile2 = new File("example/new/new.txt");
		newFile2.createNewFile();
		assertTrue(newFile2.exists());
		
		FileUtil.deleteCascade(newDir);
		assertFalse(newFile.exists());
		assertFalse(newFile2.exists());
		assertFalse(newDir.exists());
				
	}
	
	private static class Waiter {}
	
	private static class Check extends Thread {
		public enum Action {
			CHECK,
			END
		}
		
		Action action;
		File file;
		Boolean check;
		Waiter waiter = new Waiter();
		Waiter parentWaiter;

		public void check() {
			action = Action.CHECK;
			synchronized(waiter) { waiter.notify(); }
		}
		public void end() {
			action = Action.END;
			synchronized(waiter) { waiter.notify(); }
		}
		public Check(Waiter waiter, File file) {
			this.parentWaiter = waiter;
			this.file = file;
		}
		@Override
		public void run() {
			while (true) {
				synchronized(waiter) { try {
					waiter.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} }
				if (action == Action.CHECK) {
					action = null;
					check = FileUtil.isLocked(file);
					synchronized(parentWaiter) { parentWaiter.notify(); }
				}
				if (action == Action.END) {
					action = null;
					break;
				}
			}
			
		}
	}
	
	private static class Lock extends Thread {
		public static enum Action{
			LOCK,
			RELEASE,
			END
		}
		public Action action;
		File file;
		FileLock lock = null;
		Waiter waiter = new Waiter();
		Waiter parentWaiter;
		
		public Lock(Waiter waiter, File file) {
			this.parentWaiter = waiter;
			this.file = file;
		}
		
		public void lock() { 
			action = Action.LOCK;
			synchronized(waiter) { waiter.notify(); }
		}
		public void release() {
			action = Action.RELEASE;
			synchronized(waiter) { waiter.notify(); }
		}
		public void end() {
			action = Action.END;
			synchronized(waiter) { waiter.notify(); }
		}
		
		@Override
		public void run() {
			while (true) {
				try {
					synchronized(waiter) { try {
						waiter.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} }
					if (action == Action.LOCK) {
						try {
							lock = FileUtil.lock(file);
						} catch (FileLockedException e) {
							lock = null;
							e.printStackTrace();
						}
						action = null;
						synchronized(parentWaiter) { parentWaiter.notify(); }
					}
					if (action == Action.RELEASE) {
						lock.release();
						action = null;
						synchronized(parentWaiter) { parentWaiter.notify(); }
					}
					if (action == Action.END) {
						action = null;
						lock = null;
						break;
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	public void FileLockingTest() throws IOException, FileLockedException, InterruptedException {
		
		Waiter waiter = new Waiter();
		
		logger.trace("FileLockingTest");

		File example = new File("example");
		
		logger.trace("Fetching file '{}' from directory '{}'", "sample.txt", example.getAbsolutePath());
		File sampleTxt = FileUtil.fetch(example, "sample.txt");
		
		assertTrue(sampleTxt.exists());
		
		logger.trace("Creating locking thread");
		Lock lock = new Lock(waiter, sampleTxt);
		lock.start();
		
		logger.trace("Creating checking thread");
		Check check = new Check(waiter, sampleTxt);
		check.start();
		
		assertNull(check.check);
		assertNull(lock.lock);
		
		logger.trace("Checking file not locked");
		check.check();
		synchronized(waiter) { waiter.wait(); }
		assertFalse(check.check);
		check.check = null;
		assertNull(lock.lock);
		
		logger.trace("Locking file");
		lock.lock();
		synchronized(waiter) { waiter.wait(); }
		assertNotNull(lock.lock);
		assertTrue(lock.lock.isValid());
		
		logger.trace("Checking file is locked");
		check.check();
		synchronized(waiter) { waiter.wait(); }
		assertTrue(check.check);
		check.check = null;
		
		logger.trace("Releasing lock");
		lock.release();
		synchronized(waiter) { waiter.wait(); }
		assertNotNull(lock.lock);
		assertFalse(lock.lock.isValid());
		
		logger.trace("Checking file is not locked");
		check.check();
		synchronized(waiter) { waiter.wait(); }
		assertFalse(check.check);
		check.check = null;
		
		logger.trace("Ending locking thread");
		lock.end();
		
		logger.trace("Ending checking thread");
		check.end();
		
		logger.trace("Waiting for locking thread to end");
		lock.join();
		
		logger.trace("Waiting for checking thread to end");
		check.join();

		logger.trace("Done!");
		
	}
	
	
	@Test
	public void FloatUtilTest() {
		
		logger.trace("FloatUtilTest");

		assertEquals(new Integer(123), FloatUtil.toInteger(new Float(123.456)));
		assertEquals(new Integer(987), FloatUtil.toInteger(new Float(987.654)));
		assertEquals(new Long(123), FloatUtil.toLong(new Float(123.456)));
		assertEquals(new Long(987), FloatUtil.toLong(new Float(987.654)));
		assertEquals(new Float(123.456), FloatUtil.toFloat(new Float(123.456)));
		assertEquals(new Float(987.654), FloatUtil.toFloat(new Float(987.654)));
		assertEquals(new Double(new Float(123.456)), FloatUtil.toDouble(new Float(123.456)));
		assertEquals(new Double(new Float(987.654)), FloatUtil.toDouble(new Float(987.654)));
		assertEquals(new Date(123), FloatUtil.toDate(new Float(123.456)));
		assertEquals(new Date(987), FloatUtil.toDate(new Float(987.654)));
		assertEquals("123.456", FloatUtil.toString(new Float(123.456)));
		assertEquals("987.654", FloatUtil.toString(new Float(987.654)));
		
		assertTrue(FloatUtil.toBoolean(new Float(123.456)));
		assertFalse(FloatUtil.toBoolean(new Float(0.0)));
		
		
		assertEquals(new Float(100), FloatUtil.toFloat(100));
		assertEquals(new Float(0), FloatUtil.toFloat(0));
		assertEquals(new Float(100), FloatUtil.toFloat(100L));
		assertEquals(new Float(0), FloatUtil.toFloat(0L));
		assertEquals(new Float(new Float(123.456)), FloatUtil.toFloat(new Float(123.456)));
		assertEquals(new Float(0), FloatUtil.toFloat(new Float(0.0)));
		assertEquals(new Float(123.456), FloatUtil.toFloat(new Double(123.456)));
		assertEquals(new Float(0), FloatUtil.toFloat(new Double(0.0)));
		assertEquals(new Float(1472595959000L), FloatUtil.toFloat(new Date(1472595959000L)));
		assertEquals(new Float(0), FloatUtil.toFloat(new Date(0)));
		assertEquals(new Float(123.456), FloatUtil.toFloat("123.456"));
		assertEquals(new Float(987.654), FloatUtil.toFloat("987.654"));
		assertEquals(new Float(1), FloatUtil.toFloat(true));
		assertEquals(new Float(0), FloatUtil.toFloat(false));
		assertNull(FloatUtil.toFloat("XXXX"));
		
		FloatUtil.registerTypeConverter(new TypeConverter<Foo,Float>() {

			@Override
			public Class<Foo> convertClass() { return Foo.class; }

			@Override
			public Float convert(Object value) {
				if (value instanceof Foo) {
					Foo foo = (Foo)value;
					return foo.fooFloat;
				}
				return null;
			}});
		
		
		Foo foo1 = new Foo();
		foo1.fooFloat = new Float(123.456);
		Foo foo2 = new Foo();
		foo2.fooFloat = new Float(987.654);
		
		assertEquals(new Float(123.456), FloatUtil.toFloat(foo1));
		assertEquals(new Float(987.654), FloatUtil.toFloat(foo2));
		assertNull(FloatUtil.toFloat((Foo)null));
		

	}
	
	@Test
	public void IntegerUtilTest() {
		
		logger.trace("IntegerUtilTest");

		assertEquals(new Integer(123), IntegerUtil.toInteger(new Integer(123)));
		assertEquals(new Integer(987), IntegerUtil.toInteger(new Integer(987)));
		assertEquals(new Long(123), IntegerUtil.toLong(new Integer(123)));
		assertEquals(new Long(987), IntegerUtil.toLong(new Integer(987)));
		assertEquals(new Float(123.0), IntegerUtil.toFloat(new Integer(123)));
		assertEquals(new Float(987.0), IntegerUtil.toFloat(new Integer(987)));
		assertEquals(new Double(123.0), IntegerUtil.toDouble(new Integer(123)));
		assertEquals(new Double(987.0), IntegerUtil.toDouble(new Integer(987)));
		assertEquals(new Date(123), IntegerUtil.toDate(new Integer(123)));
		assertEquals(new Date(987), IntegerUtil.toDate(new Integer(987)));
		assertEquals("123", IntegerUtil.toString(new Integer(123)));
		assertEquals("987", IntegerUtil.toString(new Integer(987)));
		
		assertTrue(IntegerUtil.toBoolean(new Integer(123)));
		assertFalse(IntegerUtil.toBoolean(new Integer(0)));
		
		
		assertEquals(new Integer(100), IntegerUtil.toInteger(100));
		assertEquals(new Integer(0), IntegerUtil.toInteger(0));
		assertEquals(new Integer(100), IntegerUtil.toInteger(100L));
		assertEquals(new Integer(0), IntegerUtil.toInteger(0L));
		assertEquals(new Integer(123), IntegerUtil.toInteger(new Float(123.456)));
		assertEquals(new Integer(0), IntegerUtil.toInteger(new Float(0.0)));
		assertEquals(new Integer(123), IntegerUtil.toInteger(new Double(123.456)));
		assertEquals(new Integer(0), IntegerUtil.toInteger(new Double(0.0)));
		assertEquals(new Integer(new Long(1472595959000L).intValue()), IntegerUtil.toInteger(new Date(1472595959000L)));
		assertEquals(new Integer(0), IntegerUtil.toInteger(new Date(0)));
		assertEquals(new Integer(123), IntegerUtil.toInteger("123.456"));
		assertEquals(new Integer(987), IntegerUtil.toInteger("987.654"));
		assertEquals(new Integer(1), IntegerUtil.toInteger(true));
		assertEquals(new Integer(0), IntegerUtil.toInteger(false));
		assertNull(IntegerUtil.toInteger("XXXX"));
		
		IntegerUtil.registerTypeConverter(new TypeConverter<Foo,Integer>() {

			@Override
			public Class<Foo> convertClass() { return Foo.class; }

			@Override
			public Integer convert(Object value) {
				if (value instanceof Foo) {
					Foo foo = (Foo)value;
					return foo.fooInteger;
				}
				return null;
			}});
		
		
		Foo foo1 = new Foo();
		foo1.fooInteger = new Integer(123);
		Foo foo2 = new Foo();
		foo2.fooInteger = new Integer(987);
		
		assertEquals(new Integer(123), IntegerUtil.toInteger(foo1));
		assertEquals(new Integer(987), IntegerUtil.toInteger(foo2));
		assertNull(IntegerUtil.toInteger((Foo)null));
		

	}
	
	@Test
	public void LongUtilTest() {
		
		logger.trace("LongUtilTest");

		assertEquals(new Integer(123), LongUtil.toInteger(new Long(123)));
		assertEquals(new Integer(987), LongUtil.toInteger(new Long(987)));
		assertEquals(new Long(123), LongUtil.toLong(new Long(123)));
		assertEquals(new Long(987), LongUtil.toLong(new Long(987)));
		assertEquals(new Float(123.0), LongUtil.toFloat(new Long(123)));
		assertEquals(new Float(987.0), LongUtil.toFloat(new Long(987)));
		assertEquals(new Double(123.0), LongUtil.toDouble(new Long(123)));
		assertEquals(new Double(987.0), LongUtil.toDouble(new Long(987)));
		assertEquals(new Date(123), LongUtil.toDate(new Long(123)));
		assertEquals(new Date(987), LongUtil.toDate(new Long(987)));
		assertEquals("123", LongUtil.toString(new Long(123)));
		assertEquals("987", LongUtil.toString(new Long(987)));
		
		assertTrue(LongUtil.toBoolean(new Long(123)));
		assertFalse(LongUtil.toBoolean(new Long(0)));
		
		
		assertEquals(new Long(100), LongUtil.toLong(100));
		assertEquals(new Long(0), LongUtil.toLong(0));
		assertEquals(new Long(100), LongUtil.toLong(100L));
		assertEquals(new Long(0), LongUtil.toLong(0L));
		assertEquals(new Long(123), LongUtil.toLong(new Float(123.456)));
		assertEquals(new Long(0), LongUtil.toLong(new Float(0.0)));
		assertEquals(new Long(123), LongUtil.toLong(new Double(123.456)));
		assertEquals(new Long(0), LongUtil.toLong(new Double(0.0)));
		assertEquals(new Long(1472595959000L), LongUtil.toLong(new Date(1472595959000L)));
		assertEquals(new Long(0), LongUtil.toLong(new Date(0)));
		assertEquals(new Long(123), LongUtil.toLong("123.456"));
		assertEquals(new Long(987), LongUtil.toLong("987.654"));
		assertEquals(new Long(1), LongUtil.toLong(true));
		assertEquals(new Long(0), LongUtil.toLong(false));
		assertNull(LongUtil.toLong("XXXX"));
		
		LongUtil.registerTypeConverter(new TypeConverter<Foo,Long>() {

			@Override
			public Class<Foo> convertClass() { return Foo.class; }

			@Override
			public Long convert(Object value) {
				if (value instanceof Foo) {
					Foo foo = (Foo)value;
					return foo.fooLong;
				}
				return null;
			}});
		
		
		Foo foo1 = new Foo();
		foo1.fooLong = new Long(123);
		Foo foo2 = new Foo();
		foo2.fooLong = new Long(987);
		
		assertEquals(new Long(123), LongUtil.toLong(foo1));
		assertEquals(new Long(987), LongUtil.toLong(foo2));
		assertNull(LongUtil.toLong((Foo)null));
		

	}
	
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

}
