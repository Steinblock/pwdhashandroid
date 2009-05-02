package com.android.pwdhashandroid.pwdhash;

import junit.framework.TestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.android.pwdhashandroid.sharp2java.ArgumentException;

public class DomainExtractorTests extends TestCase {
	
	private String siteName = "example.com";
	
	@SmallTest
	public void testExtractDomainSiteOnly() {
		String uri = "example.com";
		assertEquals("Extract domain from " + uri, siteName, 
				DomainExtractor.ExtractDomain(uri));
	}
	
	@SmallTest
	public void testExtractDomainSiteWithSubdomain() {
		String uri = "www.example.com";
		assertEquals("Extract domain from " + uri, siteName, 
				DomainExtractor.ExtractDomain(uri));
	}
	
	@SmallTest
	public void testExtractDomainSiteWithProtocolHttp() {
		String uri = "http://example.com";
		assertEquals("Extract domain from " + uri, siteName, 
				DomainExtractor.ExtractDomain(uri));
	}
	
	@SmallTest
	public void testExtractDomainSiteWithProtocolHttpsAndSubdomain() {
		String uri = "https://www.example.com";
		assertEquals("Extract domain from " + uri, siteName, 
				DomainExtractor.ExtractDomain(uri));
	}
	
	@SmallTest
	public void testExtractDomainSiteWithSlash() {
		String uri = "http://www.example.com/";
		assertEquals("Extract domain from " + uri, siteName, 
				DomainExtractor.ExtractDomain(uri));
	}
	
	@SmallTest
	public void testExtractDomainSiteWithFileName() {
		String uri = "http://www.example.com/index.php";
		assertEquals("Extract domain from " + uri, siteName, 
				DomainExtractor.ExtractDomain(uri));
	}

	@SmallTest
	public void testExtractDomainSiteWithFolderAndFileName() {
		String uri = "http://www.example.com/login/login.aspx";
		assertEquals("Extract domain from " + uri, siteName, 
				DomainExtractor.ExtractDomain(uri));
	}
	
	@SmallTest
	public void testExtractDomainSiteWithPortHTTP() {
		// pwdhash.com generates example.com:80 which is wrong from my pov
		String uri = "http://www.example.com:80/index.html";
		assertEquals("Extract domain from " + uri, siteName, 
				DomainExtractor.ExtractDomain(uri));
	}
	
	@SmallTest
	public void testExtractDomainSiteWithPortHTTPS() {
		// pwdhash.com generates example.com:443 which is wrong from my pov
		String uri = "http://www.example.com:443/";
		assertEquals("Extract domain from " + uri, siteName, 
				DomainExtractor.ExtractDomain(uri));
	}

	@SmallTest
	public void testExtractDomainSiteWithPortSpecial() {
		// pwdhash.com generates example.com:8443 which is wrong from my pov
		String uri = "http://www.example.com:8443/";
		assertEquals("Extract domain from " + uri, siteName, 
				DomainExtractor.ExtractDomain(uri));
	}
	
	@SmallTest	//(expected = ArgumentException.class)
	public void testExtractDomainWithEmptyStringThrowsArgumentException() {
		// since SmallTest lags the expected attribute and I don't know any alternative:
		try {
			DomainExtractor.ExtractDomain("");
			
			fail("DomainExtractor.ExtractDomain should throw a ArgumentException with empty domain");
		} catch (ArgumentException e) {
			// Everything ok
		} catch (Throwable t) {
			fail("DomainExtractor.ExtractDomain should throw a ArgumentException with empty domain");
		}
	}

	@SmallTest	//(expected = ArgumentException.class)
	public void testExtractDomainWithNullStringThrowsArgumentException() {
		// since SmallTest lags the expected attribute and I don't know any alternative:
		try {
			DomainExtractor.ExtractDomain(null);
			
			fail("DomainExtractor.ExtractDomain should throw a ArgumentException with empty domain");
		} catch (ArgumentException e) {
			// Everything ok
		} catch (Throwable t) {
			fail("DomainExtractor.ExtractDomain should throw a ArgumentException with empty domain");
		}
	}
	
}
