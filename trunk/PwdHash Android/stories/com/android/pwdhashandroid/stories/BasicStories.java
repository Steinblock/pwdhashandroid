package com.android.pwdhashandroid.stories;

import static org.junit.Assert.*;

import org.junit.Test;

import com.googlecode.autoandroid.positron.junit4.TestCase;

public class BasicStories extends TestCase {

    @Test
    public void shouldSeePwdHashListActivity() throws InterruptedException {
//                        Your package            		Your activity
            startActivity("com.android.pwdhashandroid", "com.android.pwdhashandroid.PwdHashList");
            pause();
            assertEquals("PwdHashList", stringAt("class.simpleName"));
    }
	
}
