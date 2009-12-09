package com.android.pwdhashandroid;

import com.android.pwdhashandroid.data.Site;
import com.android.pwdhashandroid.data.SitesController;
import com.android.pwdhashandroid.pwdhash.DomainExtractor;
import com.android.pwdhashandroid.pwdhash.PwdHash;
import com.android.pwdhashandroid.util.ClipboardHelper;
import com.android.pwdhashandroid.util.LauncherBrowser;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class PwdHashList extends ListActivity {

	private SitesController sitesController;
	
    public static final int INSERT_ID = Menu.FIRST;
    public static final int EDIT_ID = Menu.FIRST + 1;
    public static final int DELETE_ID = Menu.FIRST + 2;
    public static final int ABOUT_ID = Menu.FIRST + 3;
    public static final int RENAME_ID = Menu.FIRST + 4;
    public static final int LAUNCH_BROWSER_ID = Menu.FIRST + 5;
    
    private final static String tag = "PwdHashList";
    
    public static ListView listView;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_list);
        
        registerForContextMenu(getListView());
        
        // the ClipBoardHelper class needs a context, so we init it once
        ClipboardHelper.Initialize(this.getApplicationContext());

    }

    public long selectedItemId;
    
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);

		// we have to save the selectedItemId in a global var,
		// because if menu is shown the getSelectedItemId returns INVALID_ROW_ID

    	menu.add(0, EDIT_ID, 0, R.string.menu_edit);
    	menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    	menu.add(0, RENAME_ID, 0, R.string.menu_rename);
    	menu.add(0, LAUNCH_BROWSER_ID, 0, R.string.menu_launch_browser);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		
		// Close the controller
		sitesController.close();

		super.onPause();
	}

	@Override
	protected void onResume() {
        
		// Create a new controller and open it
        sitesController = new SitesController(this);
        sitesController.open();
        
        // Load the Data
        fillData();
        
		super.onResume();
	}
	
	private long getSelectedSiteId()
	{
	
		Cursor c = sitesAdapter.getCursor();
		
		return c.getPosition() > -1 
			? c.getLong(c.getColumnIndexOrThrow(SitesController.KEY_ROWID))
			: c.getPosition();
		
	}
	
	private Site getSelectedSite()
	{
		
		Cursor c = sitesAdapter.getCursor();
		
		if (c.getPosition() < 0)
			return null;
		
		Site s = new Site();
		
		s.id = c.getLong(c.getColumnIndexOrThrow(SitesController.KEY_ROWID));
		s.Name = c.getString(c.getColumnIndexOrThrow(SitesController.KEY_SITENAME));
		s.Domain = c.getString(c.getColumnIndexOrThrow(SitesController.KEY_SITEDOMAIN));
		s.Uri = c.getString(c.getColumnIndexOrThrow(SitesController.KEY_SITEURI));
		s.Password = c.getString(c.getColumnIndexOrThrow(SitesController.KEY_SITEPASS));
		s.Hash = c.getString(c.getColumnIndexOrThrow(SitesController.KEY_SITEHASH));

		return s;

	}	

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        
        menu.add(0, INSERT_ID, 0, R.string.menu_insert).setIcon(android.R.drawable.ic_input_add);
        
//        if (getSelectedSiteId() != -1)
//        {
//        	menu.add(0, EDIT_ID, 0, R.string.menu_edit).setIcon(android.R.drawable.ic_menu_edit);
//        	menu.add(0, DELETE_ID, 0, R.string.menu_delete).setIcon(android.R.drawable.ic_input_delete);
//        }
        
        menu.add(0, ABOUT_ID, 0, R.string.menu_about).setIcon(android.R.drawable.ic_dialog_info);
        
        return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onMenuItemSelected(int, android.view.MenuItem)
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
	        case INSERT_ID:
	        	showEditDialog(-1, false);
	            return true;
	        case EDIT_ID:
	        	showEditDialog(getSelectedSiteId(), false);
	            return true;
	        case DELETE_ID:
	        	sitesController.deleteSite(getSelectedSiteId());
	            fillData();
	            return true;
	        case RENAME_ID:
	        	Toast.makeText(this.getApplicationContext(), "TODO: Rename", Toast.LENGTH_SHORT).show();
	        	return true;
	        case LAUNCH_BROWSER_ID:
	        	
	        	if (selectedItemId != AdapterView.INVALID_ROW_ID)
	        	{
	        		
	        		Site s = getSelectedSite();
	        		
	        		if (s != null)
	        		{
	        			LauncherBrowser.launchBrowser(this, s.Uri);
	        		}
		            
	        	}
	        	return true;
	        	
	        case ABOUT_ID:
	        	showAboutBox();
	        	return true;
        }
       
        return super.onMenuItemSelected(featureId, item);
	}

	
	private SimpleCursorAdapter sitesAdapter;
	
	/*
	 *  Fetch all rows from the database and bind to the cursor to the layout
	 */
	private void fillData() {
        Cursor sitesCursor = sitesController.fetchAllSites();
        startManagingCursor(sitesCursor);
        
        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{SitesController.KEY_SITENAME, SitesController.KEY_SITEURI};
        
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.textSiteName, R.id.textSiteUri};
        
        // Now create a simple cursor adapter and set it to display
        sitesAdapter = new SimpleCursorAdapter(this, R.layout.layout_row, sitesCursor, from, to);
        setListAdapter(sitesAdapter);

    }
	
	/*
	 * Show a little about box with some copyleft info
	 */
    private void showAboutBox()
    {
    	new AlertDialog.Builder(this)
			.setTitle(R.string.about_title)
    		.setMessage(R.string.about_message)
    	    .setCancelable(true)
    	    .setIcon(android.R.drawable.ic_dialog_info)
    	    .setPositiveButton(R.string.about_positive_button, new DialogInterface.OnClickListener() {
    	    	public void onClick(DialogInterface dialog, int id) {
    	    		dialog.cancel();
    	        }
    	     })
    	     .create()
    	     .show();
    }

    /*
     * Show an Dialog with layout_edit_dialog.xml as layout to add or update a site
     */
    private void showEditDialog(long currentItemId, Boolean rename)
    {    	
    	
        // This example shows how to add a custom layout to an AlertDialog
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.layout_edit_dialog , null);
        final EditText text = (EditText)textEntryView.findViewById(R.id.edit_dialog_SiteDomain);
        final Long selectedItemId = currentItemId;
        
        String siteUri = null;
        @SuppressWarnings("unused")
		String siteDomain = null;
        String title = null;
        
        
        if (selectedItemId >= 0) {
        	
        	Site s = getSelectedSite();
            siteUri = s.Uri;
            siteDomain = s.Domain;
            title = s.Name;
        }
        else {
        	siteUri = "http://";
        	siteDomain = "";
        	title = getString(R.string.edit_dialog_title);        
        }
        
        text.setText(siteUri);
        
        new AlertDialog.Builder(PwdHashList.this)
            .setIcon(R.drawable.icon)
            .setTitle(title)
            .setView(textEntryView)
            .setPositiveButton(R.string.edit_dialog_positive_button, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	String result = text.getText().toString();
                	long id = selectedItemId;
                	
                	if (result.equals("http://"))
                		dialog.cancel();
                	
                	UpdateSite(id, result);                
                }
            })
            .setNegativeButton(R.string.edit_dialog_negative_button, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	dialog.cancel();
                }
            })
            .create()
            .show();
    }
        
    /*
     * Is called from showEditDialog after PositveButton is clicked
     */
    private void UpdateSite(long id, String siteUri)
    {
    	String siteDomain = TryExtractDomain(siteUri);
    	
    	if (siteDomain == null)
    		return;
    	
        Site site = new Site();
        
      	site.id = id;
        
        site.Name = siteDomain;
        site.Domain = siteDomain;
        site.Uri = siteUri;
        
        sitesController.Save(site);
        fillData();

    }
    
    @SuppressWarnings("unused")
	private void RenameSite(long id, String siteName) throws Exception
    {
    	throw new Exception("not implemented");
    }
    
    /*
     * Show a dialog where user enters his password. Directly calls TryAction on positive click
     */
    private void showPasswordDialog(String Domain)
    {
    	
    	LayoutInflater factory = LayoutInflater.from(this);
    	final View textEntryView = factory.inflate(R.layout.layout_edit_dialog , null);
    	final EditText text = (EditText)textEntryView.findViewById(R.id.edit_dialog_SiteDomain);
    	final String siteDomain = Domain; 
    	
        new AlertDialog.Builder(PwdHashList.this)
        .setIcon(R.drawable.icon)
        .setTitle(getString(R.string.password_dialog_title) + " " + siteDomain)
        .setView(textEntryView)
        .setPositiveButton(R.string.password_dialog_positive_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	
            	String sitePassword = text.getText().toString();
            	String sitePwdHash = TryGetPwdHash(siteDomain, sitePassword, true);
            	
            	if (sitePwdHash != null)
            		TrySetClipboardText(sitePwdHash);            	
            }
        })
        .setNegativeButton(R.string.password_dialog_negative_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                /* User clicked cancel so do some stuff */
            	dialog.cancel();
            }
        })
        .create()
        .show();
    }
    
    /* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        Cursor site = sitesController.fetchSite(id);
        startManagingCursor(site);
//        String siteUri = site.getString(site.getColumnIndexOrThrow(SitesController.KEY_SITEURI));
        String siteDomain = site.getString(site.getColumnIndexOrThrow(SitesController.KEY_SITEDOMAIN));
        
        showPasswordDialog(siteDomain);

	}

	/*
	 * Here happens the "real" action --> Generate PwdHash passed in Domain/Password
	 */
    private String TryGetPwdHash(String siteDomain, String sitePassword, boolean showToast) {
        
    	try {
    		String domain = siteDomain;
    		String pwdhash = PwdHash.GenerateHashedPassword(domain, sitePassword);
    		
    		if (showToast) {
    			StringBuilder text = new StringBuilder();
    			
				text.append("Site Domain:\t");
				text.append(domain).append("\n");
				
				text.append("PasswordHash:\t");
				text.append(pwdhash).append("\n");
				
				Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
				toast.show();
    		}
    		
    		return pwdhash;
    		
    	} catch (Throwable t) {
    		Log.e(tag, t.getStackTrace().toString());
			Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();
			
			return null;
    	}
    
    }
    
    private String TryExtractDomain(String siteUri)
    {
    	try
    	{
        	return DomainExtractor.ExtractDomain(siteUri);
    	} catch (Throwable t) {
    		Log.e(tag, t.getStackTrace().toString());
    		Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();
    		
    		return null;
    	}
    }
    
    private void TrySetClipboardText(String text)
    {
    	try
    	{
    		ClipboardHelper.setText(text);
    	} catch (Throwable t) {
    		Log.e(tag, t.getStackTrace().toString());
    		Toast.makeText(this, "Sorry! Could not copy content to Clipboard.", Toast.LENGTH_LONG);
    	}	
    }
        
}
