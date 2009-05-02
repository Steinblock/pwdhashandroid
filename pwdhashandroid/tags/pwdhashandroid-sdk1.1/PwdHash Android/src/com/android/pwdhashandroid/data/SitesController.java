package com.android.pwdhashandroid.data;

/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.android.pwdhashandroid.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class SitesController {

	//// DB-Schema:
	// _id - Integer
	// SiteName - String		| Default = SiteDomain
	// SiteDomain - String		| the Domain (example.com)
	// SiteUri - String			| the full Url of the login page (http://www.example.com/login/index.php)
	// SitePass - String		| reserved. PwdHashAndroid will NOT store password / hash until anybody requests
	// SiteHash - String		| reserved. Same as SitePass
	// CreatedOn - DateTime		| Insert into ... date/time
	// ModifiedOn - DateTime	| Update ... date/time
	// AccessedOn - DateTime	| Last time the hash has been generated (maybe with cupcake I add a live folder "Last Sites"
	// UsageCounter - Integer	| Counts the usage (maybe live folder "Most often used")
	
	public static final String KEY_ROWID = "_id";
    public static final String KEY_SITENAME = "SiteName";
    public static final String KEY_SITEDOMAIN = "SiteDomain";
    public static final String KEY_SITEURI = "SiteUri";
    public static final String KEY_SITEPASS = "SitePass";
    public static final String KEY_SITEHASH = "SiteHash";
    public static final String KEY_CREATEDON = "CreatedOn";
    public static final String KEY_MODIFIEDON = "ModifiedOn";
    public static final String KEY_ACCESSEDON = "AccessedOn";
    public static final String KEY_USAGECOUNTER = "UsageCounter";
    
    private static final String TAG = "SitesController";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "CREATE TABLE sites (_id integer primary key autoincrement, "
                    + "SiteName text not null, "
                    + "SiteDomain text not null, "
                    + "SiteUri text not null, "
                    + "SitePass text not null, "
                    + "SiteHash text not null, "
                    + "CreatedOn date not null, "
                    + "ModifiedOn date, "
                    + "AccessedOn date, "
                    + "UsageCounter integer not null);";

    private static final String DATABASE_NAME = "pwdhashandroid.db";
    private static final String DATABASE_TABLE = "sites";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS sites");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public SitesController(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public SitesController open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        mDbHelper.close();
    }

    
    
    public void Save(Site site) {

    	if (site.id == 0) {
    		site.CreatedOn = new Date();
    		site.ModifiedOn = new Date();
    		site.AccessedOn = new Date();
    	} else {
    		site.ModifiedOn = new Date();
    	}
    	
        ContentValues cv = new ContentValues();
        cv.put(KEY_SITENAME, site.Name);
        cv.put(KEY_SITEDOMAIN, site.Domain);
        cv.put(KEY_SITEURI, site.Uri);
        cv.put(KEY_SITEPASS, site.Password);
        cv.put(KEY_SITEHASH, site.Hash);
        cv.put(KEY_CREATEDON, getNow());
        cv.put(KEY_MODIFIEDON, getNow());
        cv.put(KEY_ACCESSEDON, getNow());
        cv.put(KEY_USAGECOUNTER, site.UsageCounter);
        
        if (site.id == 0) {
        	site.id = mDb.insert(DATABASE_TABLE, null, cv);
        	
        	if (site.id <= 0)
        		Log.e(TAG, "Insert of new site record failed");
        	
        } else {
        	boolean result = mDb.update(DATABASE_TABLE, cv, KEY_ROWID + "=" + site.id, null) > 0;
        	
        	if (result == false)
        		Log.e(TAG, "Update of site record with id: " + site.id + " failed");
        }
    	
    }
    
    /**
    * Get at the moment within ISO8601 format.
    * @return
    * Date and time in ISO8601 format.
    * 
    * http://sacoskun.blogspot.com/2008/05/sqlite-datetime-insertion.html
    * 
    */
    private String getNow() {
	    Calendar c = Calendar.getInstance();
	    String format = mCtx.getResources().getString(R.string.DATE_FORMAT_ISO8601);
	    
	    SimpleDateFormat sdf =
	    new SimpleDateFormat(format);
	    return sdf.format(c.getTime());
    } 

    /**
     * Delete the note with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteSite(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
        
    }

    /**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllSites() {

        return querySites(null);

    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchSite(long rowId) throws SQLException {

        Cursor mCursor = querySites(KEY_ROWID + "=" + rowId);
        
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        
        return mCursor;

    }
    
    private Cursor querySites(String whereClause) {

        return mDb.query(true, DATABASE_TABLE, new String[] {
		    			KEY_ROWID,
		    			KEY_SITENAME,
		    			KEY_SITEDOMAIN,
		    			KEY_SITEURI,
		    			KEY_SITEPASS,
		    			KEY_SITEHASH,
		    			KEY_CREATEDON,
		    			KEY_MODIFIEDON,
		    			KEY_ACCESSEDON,
		    			KEY_USAGECOUNTER
	                }, whereClause, null,
	                null, null, null, null);
    	
    }
    

}

