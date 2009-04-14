// HMACMD5.java
// 
// Copyright (C) 2009 Jürgen Steinblock
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

package pwdhash.android.com.sharp2java;

import java.security.GeneralSecurityException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.util.EncodingUtils;

/// <summary>
/// Generates HmacMD5 Hash with java.cryptox functions
/// but works like dot.NET System.Security.Cryptography.HMACMD5 Class
/// </summary>
public class HMACMD5 {
	
	private final String HMAC_MD5_NAME = "HmacMD5";
	
	private SecretKeySpec sk;
	private Mac mac;
	public HMACMD5(byte[] key) throws GeneralSecurityException {
		init(key);
	}
	
	public HMACMD5(String key) throws GeneralSecurityException {
		init(EncodingUtils.getAsciiBytes(key));
	}
	
	private void init(byte[] key) throws GeneralSecurityException {
		sk = new SecretKeySpec(key, HMAC_MD5_NAME);
        mac = Mac.getInstance(HMAC_MD5_NAME);
        mac.init(sk);		
	}
	
	public byte[] ComputeHash(byte[] data) {
        return mac.doFinal(data);
	}

	public byte[] ComputeHash(String data) {
		return ComputeHash(EncodingUtils.getAsciiBytes(data));
	}
}
