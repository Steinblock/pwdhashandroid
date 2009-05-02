package com.android.pwdhashandroid.pwdhash;

import com.android.pwdhashandroid.sharp2java.ArgumentException;
import com.android.pwdhashandroid.sharp2java.ArgumentNullException;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

public class PwdHashTests extends TestCase {

	private String siteAddress = "example.com";
	private String sitePassword = "geheim";
	private String sitePasswordShort = "abc";
	
	@SmallTest
	public void testGenerateHashedPasswordSiteAddressIsNullThrowsArgumentNullException() {
		
		try {
			PwdHash.GenerateHashedPassword(null, sitePassword);
			
			fail("PwdHash.GenerateHashedPassword should throw ArgumentNullException with siteAddress = null");
		} catch (ArgumentNullException e) { }
		
	}
	
	@SmallTest
	public void testGenerateHashedPasswordSiteAddressIsEmptyThrowsArgumentException() {
		try {
			PwdHash.GenerateHashedPassword("", sitePassword);
			
			fail("PwdHash.GenerateHashedPassword should throw ArgumentException with empty siteAddress");
		} catch (ArgumentException e) { }
	}
		
	@SmallTest
	public void testGenerateHashedPasswordSitePasswordIsNullThrowsArgumentNullException() {
		try {
			PwdHash.GenerateHashedPassword(siteAddress, null);
			
			fail("PwdHash.GenerateHashedPassword should throw ArgumentNullException with sitePassword = null");
		} catch (ArgumentNullException e) { }
	}
	
	@SmallTest
	public void testGenerateHashedPasswordSitePasswordIsEmptyThrowsArgumentException() {
		try {
			PwdHash.GenerateHashedPassword(siteAddress, "");
			
			fail("PwdHash.GenerateHashedPassword should throw ArgumentNullException with empty sitePassword");
		} catch (ArgumentException e) { }
	}
	
	@SmallTest
	public void testGenerateHashedPasswordSitePasswordIsTooShortThrowsArgumentException() {
		try {
			PwdHash.GenerateHashedPassword(siteAddress, sitePasswordShort);
			
			fail("PwdHash.GenerateHashedPassword should throw ArgumentException with sitePassword shorter 5 characters");
		} catch (ArgumentException e) { }
	}

}