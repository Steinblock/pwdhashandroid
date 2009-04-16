package com.android.pwdhashandroid;

import com.android.pwdhashandroid.data.SitesController;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class PwdHashList extends ListActivity {

	private SitesController sitesController;
	
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
	
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list);
        
        sitesController = new SitesController(this);
        sitesController.open();
        fillData();
    }

    private void fillData() {
        Cursor sitesCursor = sitesController.fetchAllSites();
        startManagingCursor(sitesCursor);
        
        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{SitesController.KEY_SITENAME, SitesController.KEY_SITEURI};
        
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.textSiteName, R.id.textSiteUri};
        
        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter sites = 
        	    new SimpleCursorAdapter(this, R.layout.layout_row2, sitesCursor, from, to);
        setListAdapter(sites);
        
    }

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
        return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onMenuItemSelected(int, android.view.MenuItem)
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
        case INSERT_ID:
            createSite();
            return true;
        case DELETE_ID:
        	sitesController.deleteSite(getListView().getSelectedItemId());
            fillData();
            return true;
        }
       
        return super.onMenuItemSelected(featureId, item);
	}
    
    private void createSite() {
        Intent i = new Intent(this, PwdHashEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    /* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, PwdHashEdit.class);
        i.putExtra(SitesController.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, 
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
        
        // TODO: Set Position
    }
    
}
