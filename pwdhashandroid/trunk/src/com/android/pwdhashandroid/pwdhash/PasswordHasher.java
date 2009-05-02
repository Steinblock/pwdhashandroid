//
// PasswordHasher.java
//
// 2009 Jürgen Steinblock
//
// Based on
//
// PasswordHasher.cs
// 
// Copyright (C) 2008 Scott Wegner
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//

package com.android.pwdhashandroid.pwdhash;

import java.security.GeneralSecurityException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

import org.apache.http.util.EncodingUtils;

import com.android.pwdhashandroid.sharp2java.Base64Coder;
import com.android.pwdhashandroid.sharp2java.HMACMD5;

import android.util.Log;


/// <summary>
/// A class to create strong passwords that are always reproducible given
/// a URI from the original domain, and the passphrase.  The algorithm
/// uses a HMAC MD5 hash, along with a pseudo-random function used to 
/// assure password strength, and obfuscate the process.
/// </summary>
public class PasswordHasher {

	// Tag for LogCat
	private final static String tag = "PasswordHasher";
	
	/// <summary>
	/// Domain component of a website to create a password first.  Should
	/// be pre-processed by DomainExtractor to remove extra characters.
	/// </summary>
	protected String Domain;
	
	/// <summary>
	/// User's master site password.  Hashed with the domain to create a 
	/// strong password.
	/// </summary>
	protected String Passphrase;
	
	// used by ApplyConstrains helper functions
	private Queue<Character> extras;

	
	/// <summary>
	/// A prefix used in the password generation process.  This is included
	/// only for compatibility with the original PwdHash plugin.  Note that
	/// other classes access this as well, so it must be public.
	/// </summary>
	public final static String PasswordPrefix = "@@";
	
	/// <summary>
	/// Create a new PasswordHasher.  Initialize with the full domain that
	/// should be hashed.
	/// </summary>
	/// <param name="domain">The <see cref="System.String"/> domain component
	/// of an original URI</param>
	/// <param name="passphrase">The "master password" that is used for
	/// hashing with.</param>
	public PasswordHasher(String domain, String passphrase) {
		Domain = domain;
		Passphrase = passphrase;
	}
	
	/// <summary>
	/// Create a strong password from the given URI and passphrase
	/// </summary>
	/// <returns>
	/// A <see cref="System.String"/> representing the generated password
	/// </returns>
	public String GetHashedPassword() throws GeneralSecurityException {
		// Get initial hash
		String hash = B64HmacMd5(Passphrase, Domain);
		
		// Apply constraints
		int size = Passphrase.length() + PasswordPrefix.length();
		
		//boolean nonalphanumeric = new Regex("\\W").IsMatch(Passphrase);
		boolean nonalphanumeric = Pattern.compile("\\W").matcher(Passphrase).find();
		String result = ApplyConstraints(hash, size, nonalphanumeric);
		
		return result;
	}
	
	/// <summary>
	/// Create a base-64 encoded HMAC MD5 hash from a key and data string
	/// </summary>
	/// <param name="password">
	/// A <see cref="System.String"/> representing the inital password.
	/// This will be used as the "key" for MD5 encryption
	/// </param>
	/// <param name="realm">
	/// A <see cref="System.String"/> representing the initial password.
	/// This will be used as the "data" for MD5 encryption
	/// </param>
	/// <returns>
	/// A <see cref="System.String"/> representing the base-64 encoded hash
	/// function
	/// </returns>
	protected String B64HmacMd5(String password, String realm) throws GeneralSecurityException {

		Log.d(tag, "B64HmacMd5:");
		Log.d(tag, "password:\t" + password);
		Log.d(tag, "realm:\t\t" + realm);

		// Put data in byte arrays to use for MD5		
		byte[] key = EncodingUtils.getAsciiBytes(password);
		byte[] data = EncodingUtils.getAsciiBytes(realm);
		
		// Create our hash
		HMACMD5 hmac = new HMACMD5(key);
		byte[] hashb = hmac.ComputeHash(data);

		// Store it as a base-64 string
		String hash = Base64Coder.encode(hashb);
		
		// Remove trailing "=="'s, which are added for strict adherence
		// to MD5 RFC.
		
//        Pattern p = Pattern.compile("=+$");
//        Matcher m = p.matcher(hash);
//        String newhash = m.replaceAll("");
		
        String newhash = hash.replaceAll("=+$", "");
		
		Log.d(tag, "hash:\t\t" + newhash);

		return newhash;
	}
	
	/// <summary>
	/// Augment the generated password to adhere to some constraints.
	/// Specifically, it should have at least one upper-case, one lower-case
	/// and one numeral character.  Also, if specified, it should contain
	/// at least one symbol.  Otherwise, it shouldn't contain any.  Then,
	/// trim it to be the specified length, and add some pseudo-entropy to
	/// make it harder to guess.
	/// </summary>
	/// <param name="hash">
	/// A <see cref="System.String"/> representing the initial password hash
	/// </param>
	/// <param name="size">
	/// A <see cref="System.Int32"/> containing the final size of the
	/// password
	/// </param>
	/// <param name="nonalphanumeric">
	/// A <see cref="System.Boolean"/> determining whether or not the final
	/// password should contain symbols
	/// </param>
	/// <returns>
	/// A <see cref="System.String"/> containing the final string, with
	/// constraints applied.
	/// </returns>
	protected String ApplyConstraints(String hash, int size, boolean nonalphanumeric)
	{
		Log.d(tag, "ApplyConstraints:");

		int startingSize = size - 4; // Leave room for extra characters
		String result = hash.substring(0, startingSize);
//		extras = new CSQueue(hash.substring(startingSize).toCharArray());
		extras = CreateQueue(hash.substring(startingSize).toCharArray());
		
		Pattern matchSymbol = Pattern.compile("\\W");
		//Regex matchSymbol = new Regex("\\W");

		
		Log.d(tag, "startingSize:\t" + startingSize);
		Log.d(tag, "result (start):\t" + result);
		Log.d(tag, "extras (start):\t" + new String(extras.toString()));
		Log.d(tag, "nonalphanumeric:\t" + nonalphanumeric);
		
		// Add the extra characters
		Log.d(tag, "A-1Z:\t\t\t");
		
		result += (Pattern.compile("[A-Z]").matcher(result).find() ? nextExtraChar()
			                                          : nextBetween('A', 26));
		Log.d(tag, result);
		
		Log.d(tag, "a-z:\t\t\t");
		result += (Pattern.compile("[a-z]").matcher(result).find() ? nextExtraChar()
			                                          : nextBetween('a', 26));
		Log.d(tag, result);
		
		Log.d(tag, "0-9:\t\t\t");
		result += (Pattern.compile("[0-9]").matcher(result).find() ? nextExtraChar()
			                                          : nextBetween('0', 10));
		Log.d(tag, result);

		Log.d(tag, "\\W:\t\t\t");
		result += (Pattern.compile("\\W").matcher(result).find() && nonalphanumeric ? 
		                                                nextExtraChar()
			                                          : '+');
		Log.d(tag, result);
		
		while(matchSymbol.matcher(result).find() && !nonalphanumeric) {
			//Log.d(tag, "Replace '" + matchSymbol.Match(result) + "':\t\t");
			Log.d(tag, "Replace '" + matchSymbol.matcher(result).group() + "':\t\t");

			result = result.replaceFirst("\\W", Character.toString(nextBetween('A', 26)));	// 
			Log.d(tag, result);
		}

		// Rotate the result to make it harder to guess the inserted locations
		Log.d(tag, "Rotate ");
		char[] rotateArr = result.toCharArray();
		rotateArr = rotate(rotateArr, nextExtra());
		result = new String(rotateArr);
		Log.d(tag, result);
		
		return result;
	}
	
	
	/* 
	 * These functions are used in the ApplyConstraints method.  They are
	 * provided as a convenience, and to make the code more readable.  They
	 * also exist in the original pwdhash javascript code.
	 */
	
	// Get the next extra character as an int if one exists-- otherwise 0	
	private int nextExtra() {
		if (extras.size() != 0)
			return (int)(extras.remove());
		else
			return 0;
	}
	
	// Get the next extra as a character
	private char nextExtraChar() {
		return (char)(nextExtra());
	}
	
	// Rotate the character array a given number of times
	private char[] rotate(char[] arr, int amount) {
//		Log.d(tag, amount + "x:\t");
	
		Queue<Character> q = CreateQueue(arr);
		
		
		while (amount -- != 0) {
			q.add(q.remove());
			Log.d(tag, q.toString());
		}
		
		Character[] chars = (Character[]) q.toArray(new Character[0]);		
		return CharacterToCharArray(chars);
		
//		return new char[10]; 
		
		
//		CSQueue q = new CSQueue(arr);
//		while (amount-- != 0) {
//			q.put(q.get());
//			Log.d(tag, new String(q.toArray()));
//		}
//
//		return q.toArray();
	}
	
	// Return a integer within the given bounds
	private int between(int min, int interval, int offset) {
		return min + offset % interval;
	}
	
	// Get the next extra character within the given bounds
	private char nextBetween(char baseChar, int interval) {
		return (char)(between((int)(baseChar), interval, nextExtra()));
	}
	
	private char[] CharacterToCharArray(Character[] chars) {
		char[] chars2 = new char[chars.length];
		
		for(int i = 0; i < chars.length; i++) {
			chars2[i] = chars[i];
		}
		
		return chars2;
	}
	
	private Queue<Character> CreateQueue(char[] chars) {
		Queue<Character> q = new LinkedList<Character>();
		
		for(int i = 0; i < chars.length; i++) {
			q.add(chars[i]);
		}
		
		return q;
	}
		
}
