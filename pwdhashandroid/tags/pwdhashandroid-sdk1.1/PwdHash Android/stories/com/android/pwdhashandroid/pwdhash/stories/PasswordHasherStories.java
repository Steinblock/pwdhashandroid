package com.android.pwdhashandroid.pwdhash.stories;

import static org.junit.Assert.*;

import java.security.GeneralSecurityException;
import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;

import com.android.pwdhashandroid.pwdhash.PasswordHasher;
import com.googlecode.autoandroid.positron.junit4.TestCase;

public class PasswordHasherStories extends TestCase {

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
	
	@Ignore // until BasicStories.canAccessAndroidLabraryClasses works 
	@Test
	public void canGenerateHashForExampleDotCom() throws GeneralSecurityException
	{
		System.out.println("Test if hash generation for site example.com works...");
		for (String password: hashTable.keySet() ) {
			String expected_hash = hashTable.get(password);
			String actual_hash = null;
			
			PasswordHasher passwordHasher = new PasswordHasher(site, password);
			actual_hash = passwordHasher.GetHashedPassword();
				
			assertEquals(expected_hash, actual_hash);
		}
	}
}
