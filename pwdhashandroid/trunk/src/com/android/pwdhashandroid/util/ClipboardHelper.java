package com.android.pwdhashandroid.util;

import android.content.Context;
import android.text.ClipboardManager;

public class ClipboardHelper  {

	public static void setText(CharSequence text) {
		if (clipboardManager == null)
			throw new NullPointerException("Class has not been initialized");
		
		clipboardManager.setText(text);
	}
	
	public static CharSequence getText() {
		if (clipboardManager == null)
			throw new NullPointerException("Class has not been initialized");
		
		return clipboardManager.getText();
	}
	
	public static boolean hasText() {
		if (clipboardManager == null)
			throw new NullPointerException("Class has not been initialized");
		
		return clipboardManager.hasText();
	}
	
	private static ClipboardManager clipboardManager;
	public static void Initialize(Context context) {
		if (clipboardManager == null)
			clipboardManager = (ClipboardManager)context.
									getSystemService(Context.CLIPBOARD_SERVICE);
	}
	
}
