package com.android.pwdhashandroid;

import com.android.pwdhashandroid.pwdhash.DomainExtractor;
import com.android.pwdhashandroid.pwdhash.PwdHash;
import com.android.pwdhashandroid.util.ClipboardHelper;
import com.android.pwdhashandroid.util.LauncherBrowser;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PwdHashAndroid extends Activity {
	
	// Tag for LogCat
	private final static String tag = "PwdHashAndroid";
	
	private EditText editTextSiteDomain;
	private EditText editTextSitePassword;
	private Button buttonCopyClip;
	private Button buttonCopyAndOpen;
	private Button buttonCreateShortcut;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        init();
    }
    
    private void init() {
    	// Initialize the ClipBoarHelperClass
    	ClipboardHelper.Initialize(this);
    	
    	editTextSiteDomain = (EditText)findViewById(R.id.editTextSiteDomain);
    	editTextSitePassword = (EditText)findViewById(R.id.editTextSitePassword);
    	
    	editTextSiteDomain.setText("http://www.example.com/login.php?user=test&pass=bla");
    	editTextSitePassword.setText("geheim");
    	
        buttonCopyClip = (Button)findViewById(R.id.buttonCopyClip);
        buttonCopyAndOpen = (Button)findViewById(R.id.buttonCopyAndOpen);
        buttonCreateShortcut = (Button)findViewById(R.id.buttonCreateShortcut);
        
        buttonCopyClip.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v) {

        		String siteDomain = editTextSiteDomain.getText().toString();
        		String sitePassword = editTextSitePassword.getText().toString();
					
        		TryAction(siteDomain, sitePassword, false, true);
        	}
        });
        
        buttonCopyAndOpen.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v) {
        	    
        		String siteDomain = editTextSiteDomain.getText().toString();
        		String sitePassword = editTextSitePassword.getText().toString();
					
        		TryAction(siteDomain, sitePassword, true, true);
        	}
        });
        
        buttonCreateShortcut.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v) {
        	    
        	}
        });
    }
    
    private void TryAction(String siteDomain, String sitePassword, boolean startBrowser, boolean showToast) {
    
    	try {
    		String domain = DomainExtractor.ExtractDomain(siteDomain);
    		String pwdhash = PwdHash.GenerateHashedPassword(domain, sitePassword);
    		
    		ClipboardHelper.setText(pwdhash);
    		
    		if (showToast) {
    			StringBuilder text = new StringBuilder();
    			
				text.append("Site Domain:\t");
				text.append(domain).append("\n");
				
				text.append("PasswordHash:\t");
				text.append(pwdhash).append("\n");
				
				Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
				toast.show();
    		}
    		
    		if (startBrowser)
    			LauncherBrowser.launchBrowser(this, siteDomain);
    		
    	} catch (Exception e) {
    		Log.e(tag, e.getStackTrace().toString());
			Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
			toast.show();
    	}
    
    }
   
}