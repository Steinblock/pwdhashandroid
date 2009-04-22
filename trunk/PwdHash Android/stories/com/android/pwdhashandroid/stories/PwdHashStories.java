package com.android.pwdhashandroid.stories;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.android.pwdhashandroid.PwdHashList;
import com.googlecode.autoandroid.positron.junit4.TestCase;
import static com.googlecode.autoandroid.positron.PositronAPI.Key.BACK;
import static com.googlecode.autoandroid.positron.PositronAPI.Key.SLASH;

public class PwdHashStories extends TestCase {

	@Before
	public void setUp() {
		System.out.println("Empty pwdhashandroid.db sites table");
		
		this.
		
		sql("pwdhashandroid.db", "DELETE FROM sites");
		startActivity("com.android.pwdhashandroid", "com.android.pwdhashandroid.PwdHashList");
		pause();
	}
	
	@Test
	public void shouldAddASite() {	
		System.out.println("Test if creation of a new site is possible");
		
		assertEquals(0, intAt("listView.count"));
		
		menu(PwdHashList.INSERT_ID);
		press("example.com", SLASH, "index.html", BACK);
		
		assertEquals(1, intAt("listView.count"));
		assertEquals("example.com", stringAt("listView.0.0.0.text"));
		assertEquals("http://example.com/index.html", stringAt("listView.0.1.0.text"));
		
	}
	
}
