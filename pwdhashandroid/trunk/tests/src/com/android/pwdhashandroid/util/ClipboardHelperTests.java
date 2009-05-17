package com.android.pwdhashandroid.util;

import com.android.pwdhashandroid.PwdHashList;

import android.test.ActivityInstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

// this testclass does not really test the PwdHashEdit Activity
// but we need a context object to access the ClipBoard
public class ClipboardHelperTests extends ActivityInstrumentationTestCase<PwdHashList> {

	public ClipboardHelperTests() {
		super("com.android.pwdhashandroid", PwdHashList.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// Initialize the static ClipboardHelper class;
		ClipboardHelper.Initialize(getActivity());
		
		store();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
		restore();
	}

	private String storedText;
	// helper functions
	private void store() {
		storedText = (String) ClipboardHelper.getText();
	}
	
	private void restore() {
		ClipboardHelper.setText(storedText);
	}
	


	@SmallTest
	public void testSetText() {
		
		store();
		
		// text to use
		String text = "Text generated by ClipboardHelperTests.testSetText()";
					
		// set the text		
		ClipboardHelper.setText(text);
		
		// pull it back
		String result = (String) ClipboardHelper.getText();
		
		// check if result is the same
		assertEquals(text, result);
		
		restore();
	}

	@SmallTest
	public void testGetText() {
		
		// Just try to get the get
		String result = (String) ClipboardHelper.getText();

		assertNotNull(result);
	}
	
	@SmallTest
	public void testHasTextReturnsTrue() {
		
		store();
	
		// text to use
		String text = "Text generated by ClipboardHelperTests.testHasTextReturnsTrue()";
		
		// set the text
		ClipboardHelper.setText(text);
		
		// get the result
		boolean result = ClipboardHelper.hasText();
		
		// check if result is true
		assertTrue("Clipboard contains text. hasText should return true", result);
		
		restore();
		
	}
	
	@SmallTest
	public void testHasTextReturnsFalse() {
		
		store();
		
		// set clipboard-text to null
		ClipboardHelper.setText(null);
		
		// get the result
		boolean result1 = ClipboardHelper.hasText();
		
		// result should be false
		assertFalse("Clipboard contains null. hasText should return false", result1);
		
		// set clipboard-text to empty string
		ClipboardHelper.setText("");
		
		// get the result
		boolean result2 = ClipboardHelper.hasText();
		
		// result should be false
		assertFalse("Clipboard contains empty string. hasText should return false", result2);
		
		restore();
		
	}
		
}