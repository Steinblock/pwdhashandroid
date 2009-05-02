package com.android.pwdhashandroid.stories;

import static org.junit.Assert.*;

import org.junit.Test;

import android.util.Log;

import com.googlecode.autoandroid.positron.junit4.TestCase;

public class BasicStories extends TestCase {

	
    @Test
    public void shouldSeePwdHashListActivity() throws InterruptedException {
    		System.out.println("Test if activity PwdHashList starts");
    	
//                        Your package            		Your activity
            startActivity("com.android.pwdhashandroid", "com.android.pwdhashandroid.PwdHashList");
            pause();
            assertEquals("PwdHashList", stringAt("class.simpleName"));
    }
    
    @Test
    public void shouldSeePwdHashEditActivity() throws InterruptedException {
    		System.out.println("Test if activity PwdHashEdit starts");
    	
//                        Your package            		Your activity
            startActivity("com.android.pwdhashandroid", "com.android.pwdhashandroid.PwdHashEdit");
            pause();
            assertEquals("PwdHashEdit", stringAt("class.simpleName"));

    }
    
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
