// ArgumentNullException.java
// 
// Copyright (C) 2009 J�rgen Steinblock
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
/// dot.NET ArgumentNullException for Java
/// </summary>
public class ArgumentNullException extends Error {

	private static final long serialVersionUID = -4968271778721765018L;

	public ArgumentNullException(String argument, String message) {
		super(message);
	}

	
	
}
