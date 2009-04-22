package com.android.pwdhashandroid.stories;


import org.junit.Before;
import org.junit.Test;

import android.util.Log;

import com.googlecode.autoandroid.positron.junit4.TestCase;

public class PositronExampleTest extends TestCase {

        @Before
        public void setUp() {
        		startActivity("com.android.pwdhashandroid", "com.android.pwdhashandroid.PwdHashList");
                pause();
        }
        
        @Test
        public void shouldLogCat() {
        	Log.d("tag", "msg");
        }
        
}
