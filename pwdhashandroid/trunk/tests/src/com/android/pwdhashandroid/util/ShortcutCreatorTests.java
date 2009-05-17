package com.android.pwdhashandroid.util;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

public class ShortcutCreatorTests extends TestCase {

	@SmallTest
	public void testCreateShortcut() {
		
		ShortcutCreator.CreateShortcut("example.com", null);

	}
	
}