package com.android.pwdhashandroid.pwdhash;

import java.security.GeneralSecurityException;
import java.util.HashMap;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

public class PasswordHasherTests extends TestCase {

	private static final String site = "example.com";
	private static final HashMap<String, String> hashTable = new HashMap<String, String>();
	static {
		hashTable.put("", "2MPb");	// empty password
		hashTable.put("1", "VJh2");
		hashTable.put("geheim", "GyO3YHSs");
		hashTable.put("verylongpasswordverylongpasswordverylongpassword",
							"TY4F8NbLHTb2Uv5w047wcQAAAA");
		hashTable.put("!\"§$%&/()=?`", "XWo1haLW+IKhVV");	// special characters
		hashTable.put("pass with whitespaces", "edziGlweDGbpRwhlfQcak5+");

	}
	
	private String GetHashHelper(String password) throws GeneralSecurityException {
		PasswordHasher passwordHasher = new PasswordHasher(site, password);
		return passwordHasher.GetHashedPassword();	
	}
	
	@SmallTest
	public void testGetHashedPasswordForEmptyPass() throws GeneralSecurityException {
		String pass = "";
		String expected = hashTable.get(pass);
		String actual = GetHashHelper(pass);

		assertEquals(expected, actual);
	}

	@SmallTest
	public void testGetHashedPasswordForShortPass() throws GeneralSecurityException {
		String pass = "1";
		String expected = hashTable.get(pass);
		String actual = GetHashHelper(pass);

		assertEquals(expected, actual);
	}

	@SmallTest
	public void testGetHashedPasswordForUsualPass() throws GeneralSecurityException {
		String pass = "geheim";
		String expected = hashTable.get(pass);
		String actual = GetHashHelper(pass);

		assertEquals(expected, actual);
	}

	@SmallTest
	public void testGetHashedPasswordForLongPass() throws GeneralSecurityException {
		String pass = "verylongpasswordverylongpasswordverylongpassword";
		String expected = hashTable.get(pass);
		String actual = GetHashHelper(pass);

		assertEquals(expected, actual);
	}
	
	@SmallTest
	public void testGetHashedPasswordForPassWithSpecialCharacters() throws GeneralSecurityException {
		String pass = "!\"§$%&/()=?`";
		String expected = hashTable.get(pass);
		String actual = GetHashHelper(pass);

		assertEquals(expected, actual);
	}

	@SmallTest
	public void testGetHashedPasswordForPassWithWhiteSpaces() throws GeneralSecurityException {
		String pass = "pass with whitespaces";
		String expected = hashTable.get(pass);
		String actual = GetHashHelper(pass);

		assertEquals(expected, actual);
	}

}