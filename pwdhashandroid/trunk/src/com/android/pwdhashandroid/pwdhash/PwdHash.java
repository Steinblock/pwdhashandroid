package com.android.pwdhashandroid.pwdhash;

import java.security.GeneralSecurityException;

import com.android.pwdhashandroid.sharp2java.ArgumentException;
import com.android.pwdhashandroid.sharp2java.ArgumentNullException;


public class PwdHash {
	
	/// <summary>
	/// Generate a hashed password based on the PwdHash algorithm
	/// </summary>
	/// <param name="siteAddress">
	/// A <see cref="System.String"/> representing the full URL of the site
	/// you would like to use the password for.
	/// </param>
	/// <param name="sitePassword">
	/// A <see cref="System.String"/> with the base password used for this
	/// site
	/// </param>
	/// <returns>
	/// A <see cref="System.String"/> containing the unique, strong password
	/// generated using an MD5 hash and the PwdHash algorithm to ensure a
	/// strong and hard-to-guess password
	/// </returns>
	public static String GenerateHashedPassword(String siteAddress, String sitePassword) throws ArgumentNullException, ArgumentException {
		
		// Validate parameters
		if (siteAddress == null)
			throw new ArgumentNullException("siteAddress",
			                                "Please specify a valid Site Address");
		if (siteAddress == "")
			throw new ArgumentException("Please specify a valid Site Address",
			                            "siteAddress");
		if (sitePassword == null)
			throw new ArgumentNullException("sitePassword",
			                                "Please specify a valid Site Password");
		if (sitePassword == "")
			throw new ArgumentException("Please specify a valid Site Password",
			                            "sitePassword");
		
		// the pwdhash firefox extension behaves this way
		// however, the generator on pwdhash.com allows this passwords
		if (sitePassword.length() <= 4)
			throw new ArgumentException("Your password is too short to protect",
										"sitePassword");
		
		// Get the base domain from the given URI
		String domain = DomainExtractor.ExtractDomain(siteAddress);
		
		// Make sure the new domain isn't empty
		if (domain == "")
			throw new ArgumentException("Please specify a valid Site Address",
			                            "siteAddress");
		
		// Then, use it along with the site password to apply the PwdHash 
		// algorithm.
		PasswordHasher hasher = new PasswordHasher(domain, sitePassword);
		
		String hashedPassword;
		try {
			hashedPassword = hasher.GetHashedPassword();
		} catch (GeneralSecurityException e) {
			throw new Error("Generation of Passwordhash failed", e);
		}
		
		return hashedPassword;
	}
	
}
