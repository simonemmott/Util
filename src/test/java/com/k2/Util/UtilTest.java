package com.k2.Util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.k2.Util.Version.Increment;
import com.k2.Util.Version.Version;

public class UtilTest {

	@Test
	public void VersionTest()
    {
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
}
