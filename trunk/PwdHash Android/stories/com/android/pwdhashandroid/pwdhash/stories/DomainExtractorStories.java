package com.android.pwdhashandroid.pwdhash.stories;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import com.android.pwdhashandroid.pwdhash.DomainExtractor;
import com.android.pwdhashandroid.sharp2java.ArgumentException;
import com.googlecode.autoandroid.positron.junit4.TestCase;

public class DomainExtractorStories extends TestCase {

	private static final HashMap<String, String> siteTable = new HashMap<String, String>();
	static {
		siteTable.put("example.com", "example.com");
		siteTable.put("subdomain.example.com", "example.com");
		siteTable.put("http://subdomain.example.com", "example.com");
		siteTable.put("https://subdomain.example.com", "example.com");
		
		siteTable.put("example.com/index.html", "example.com");
		siteTable.put("subdomain.example.com/index.html", "example.com");
		siteTable.put("http://subdomain.example.com/index.html", "example.com");
		siteTable.put("https://subdomain.example.com/index.html", "example.com");
		
		siteTable.put("http://subdomain.example.com:80/index.html", "example.com");
		siteTable.put("https://subdomain.example.com:443/index.html", "example.com");
	}
	
	@Test
	public void canExtractDomain()
	{
		for (String uri: siteTable.keySet() ) {
			String expected_site = siteTable.get(uri);
			String actual_site = null;
			
			try {
				actual_site = DomainExtractor.ExtractDomain(uri);
			} catch (ArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			String msg = "" + uri + ": " + actual_site;
			assertEquals(msg, expected_site, actual_site);
		}
	}
}
