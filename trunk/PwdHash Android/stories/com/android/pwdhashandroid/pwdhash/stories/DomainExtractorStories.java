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
		
		// not clear about this one. same result as pwdhash.com, but seems wrong in my eyes (would expect the port to be ignored
		siteTable.put("http://subdomain.example.com:80/index.html", "example.com:80");
		siteTable.put("https://subdomain.example.com:443/index.html", "example.com:443");
	}
	
	@Test
	public void canExtractDomain() throws ArgumentException
	{
		for (String uri: siteTable.keySet() ) {
			String expected_site = siteTable.get(uri);
			String actual_site = null;
			

			actual_site = DomainExtractor.ExtractDomain(uri);

			
			assertEquals(expected_site, actual_site);
		}
	}
}
