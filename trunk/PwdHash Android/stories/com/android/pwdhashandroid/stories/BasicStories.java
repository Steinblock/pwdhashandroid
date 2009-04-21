package com.android.pwdhashandroid.stories;

import static org.junit.Assert.*;

import org.junit.Test;

import android.util.Log;

import com.googlecode.autoandroid.positron.junit4.TestCase;

public class BasicStories extends TestCase {

	
    @Test
    public void shouldSeePwdHashListActivity() throws InterruptedException {
//                        Your package            		Your activity
            startActivity("com.android.pwdhashandroid", "com.android.pwdhashandroid.PwdHashList");
            pause();
            assertEquals("PwdHashList", stringAt("class.simpleName"));
    }
    
    @Test
    public void shouldSeePwdHashEditActivity() throws InterruptedException {
//                        Your package            		Your activity
            startActivity("com.android.pwdhashandroid", "com.android.pwdhashandroid.PwdHashEdit");
            pause();
            assertEquals("PwdHashEdit", stringAt("class.simpleName"));

    }
    
    @Test
    public void canAccessAndroidLibraryClasses() {
    	
    	boolean itworks = false;
    	try {
    		Log.d("tag", "msg");
    		itworks = true;
    	} catch (NoClassDefFoundError e) { }
    	
    	assertTrue(itworks);
    }
	
}
