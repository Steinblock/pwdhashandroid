package com.android.pwdhashandroid.data;

import java.util.Date;

// pojo
public class Site {

	public long id = 0;
	public String Name;
	public String Domain;
	public String Uri;
	public String Password = "";
	public String Hash = "";
	public Date CreatedOn;
	public Date ModifiedOn;
	public Date AccessedOn;
	public int UsageCounter;

}
