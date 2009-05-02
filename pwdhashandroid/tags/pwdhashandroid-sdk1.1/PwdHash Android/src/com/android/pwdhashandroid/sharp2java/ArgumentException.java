// ArgumentException.java
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
/// dot.NET ArgumentException for Java
/// </summary>
public class ArgumentException extends Exception {

	private static final long serialVersionUID = 5753524364881271418L;

	public ArgumentException(String message, String argument) {
		super(message + ": " + argument);
	}

}
