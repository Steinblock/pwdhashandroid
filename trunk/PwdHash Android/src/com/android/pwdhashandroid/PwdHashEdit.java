// PwdHashAndroid.java
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

package com.android.pwdhashandroid;

import com.android.pwdhashandroid.data.Site;
import com.android.pwdhashandroid.data.SitesController;
import com.android.pwdhashandroid.pwdhash.DomainExtractor;
import com.android.pwdhashandroid.pwdhash.PwdHash;
import com.android.pwdhashandroid.sharp2java.ArgumentException;
import com.android.pwdhashandroid.util.ClipboardHelper;
import com.android.pwdhashandroid.util.LauncherBrowser;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PwdHashEdit extends Activity {
	
	// Tag for LogCat
	private final static String tag = "PwdHashAndroid";
	
	private EditText editTextSiteDomain;
	private EditText editTextSitePassword;
	private Button buttonCopyClip;
	private Button buttonCopyAndOpen;
	private Button buttonCreateShortcut;
	
	private SitesController sitesController;
	private Long mRowId;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        sitesController = new SitesController(this);
        sitesController.open();
        setContentView(R.layout.layout_edit);
        
        initControls();
        
        mRowId = savedInstanceState != null ? savedInstanceState.getLong(SitesController.KEY_ROWID) : null;
        
        if (mRowId == null) {
        	Bundle extras = getIntent().getExtras();            
        	mRowId = extras != null ? extras.getLong(SitesController.KEY_ROWID) : null;
        }
        
        populateFields();
        
        
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SitesController.KEY_ROWID, mRowId);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }
    
    private void saveState() {
    	
    	try {
			String siteUri = editTextSiteDomain.getText().toString();
			String siteDomain = DomainExtractor.ExtractDomain(siteUri);
//			String sitePassword = editTextSitePassword.getText().toString();
	
	        Site site = new Site();
	        
	        if (mRowId != null)
	        	site.id = mRowId;
	        
	        site.Name = siteDomain;
	        site.Domain = siteDomain;
	        site.Uri = siteUri;
	        
	        sitesController.Save(site);
    	} catch (ArgumentException ex) {
    		Log.e(tag, ex.toString());
    	}
        
    }
    
    private void initControls() {
    	// Initialize the ClipBoarHelperClass
    	ClipboardHelper.Initialize(this);
    	
    	editTextSiteDomain = (EditText)findViewById(R.id.editTextSiteDomain);
    	editTextSitePassword = (EditText)findViewById(R.id.editTextSitePassword);
    	
    	editTextSiteDomain.setText("http://");
//    	editTextSiteDomain.setText("http://www.example.com/login.php?user=test&pass=bla");
//    	editTextSitePassword.setText("geheim");
    	
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
    
    private void populateFields() {
        if (mRowId != null) {
            Cursor site = sitesController.fetchSite(mRowId);
            startManagingCursor(site);
            editTextSiteDomain.setText(site.getString(
    	            site.getColumnIndexOrThrow(SitesController.KEY_SITEURI)));
//            mBodyText.setText(note.getString(
//                    note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
        }
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