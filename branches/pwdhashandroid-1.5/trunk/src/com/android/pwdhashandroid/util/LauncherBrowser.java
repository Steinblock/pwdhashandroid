package com.android.pwdhashandroid.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class LauncherBrowser {

	public static void launchBrowser(Context context, String uri) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		Uri u = Uri.parse(uri);
		i.setData(u);
		context.startActivity(i);
	}
}
