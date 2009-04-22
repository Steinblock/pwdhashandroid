package com.android.pwdhashandroid.stories;

import org.junit.* ;

import android.util.Log;
import static org.junit.Assert.* ;

public class ExampleTest {
 
   @Test
   public void canAccessAndroidLibraryClasses() {
   	
	   	System.out.println("Test if Testrunner is able to access the android library");
	   	
	   	boolean itworks = false;
	   	try {
	   		Log.d("tag", "msg");
	   		itworks = true;
	   	} catch (NoClassDefFoundError e) { }
	   	
	   	assertTrue(itworks);
   }
   
}