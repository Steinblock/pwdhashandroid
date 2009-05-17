package com.android.pwdhashandroid.util;

import com.android.pwdhashandroid.PwdHashList;

import android.test.ActivityInstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

// this testclass does not really test the PwdHashEdit Activity
// but we need a context object to launch the Browser
public class LauncherBrowserTests extends ActivityInstrumentationTestCase<PwdHashList> {

	public LauncherBrowserTests() {
		super("com.android.pwdhashandroid", PwdHashList.class);
	}

//	@Override
//	protected void setUp() throws Exception {
//		super.setUp();
//	}
//	
//	@Override
//	protected void tearDown() throws Exception {
//		super.tearDown();
//	}

	
	@SmallTest
	public void testLaunchBrowser() {
		
		// if this doesn't throw a exception we expect everything to be ok
		LauncherBrowser.launchBrowser(getActivity(), "http://www.example.com");
		
	}
			
}