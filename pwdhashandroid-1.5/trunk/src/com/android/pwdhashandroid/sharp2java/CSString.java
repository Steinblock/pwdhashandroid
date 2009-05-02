// CSString.java
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

package com.android.pwdhashandroid.sharp2java;

/// <summary>
/// Implements some dot.NET String Methods
/// </summary>
public class CSString {
	
	public static boolean isNullOrEmpty(String string) {
		if (string == null)
			return true;
		
		if (string.length() == 0)
			return true;
		
		return false;
	}

	public static String join(String delimeter, String[] items) {
		if (items.length == 0)
			return "";
		
		StringBuilder result = new StringBuilder();
		for (int i = 0; i <= items.length - 1; i++) {
			result.append(items[i]);
			if (i != items.length - 1)
				result.append(delimeter);
		}

		return result.toString();
	}

}
