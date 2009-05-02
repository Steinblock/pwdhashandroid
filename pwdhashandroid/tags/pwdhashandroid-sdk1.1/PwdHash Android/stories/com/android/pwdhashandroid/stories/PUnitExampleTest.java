package com.android.pwdhashandroid.stories;

import org.junit.Test;
import org.punit.runner.*;

import android.util.Log;

import junit.framework.*;

public class PUnitExampleTest extends TestCase {
 	
	public static void main(String[] args) {
//		new SoloRunner().run(PUnitExampleTest.class);
		
	   AndroidRunner runner = new AndroidRunner(new SoloRunner());
	   runner.run(PUnitExampleTest.class); 
	}
	
	protected void setUp() throws Exception {
		System.out.println("setUp"); //$NON-NLS-1$
	}
	
	protected void tearDown() throws Exception {
		System.out.println("tearDown"); //$NON-NLS-1$
	}
	
	@Test
	public void testA() {
		assertEquals(true, false);
	}

	@Test
	public void testB() {
		assertTrue(true);
	}
	
	@Test
	public void testC() {
		assertTrue(false);
	}
	
    @Test(expected = NullPointerException.class)
    public void testExceptionAnnotation() {
    	System.out.println("test2"); //$NON-NLS-1$
    	throw new NullPointerException();
    }

    @Test
    public void testAndroidLib() {
    	
    	System.out.println("Test if Testrunner is able to access the android library");
    	   	
    	boolean itworks = false;
    	try {
    		Log.d("tag", "msg");
    		itworks = true;
    	} catch (NoClassDefFoundError e) { }
    	
    	assertTrue(itworks);
    }
}