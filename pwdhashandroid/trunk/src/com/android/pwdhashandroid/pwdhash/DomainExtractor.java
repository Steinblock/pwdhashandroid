//
// DomainExtractor.java
//
// 2009 J�rgen Steinblock
//
// Based on
//
// DomainExtractor.cs
// 
// Copyright (C) 2008 Scott Wegner
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

package com.android.pwdhashandroid.pwdhash;

import android.util.Log;
import java.util.regex.*;

import com.android.pwdhashandroid.sharp2java.ArgumentException;
import com.android.pwdhashandroid.sharp2java.CSString;

/// <summary>
/// A utility for extracting the domain portion for a URI
/// </summary>
public class DomainExtractor {

	// Tag for LogCat
	private final static String tag = "DomainExtractor";
	
	/// <summary>
	/// Extract the domain from the passed URI.
	/// </summary>
	/// <param name="uri">
	/// The <see cref="System.String"/> URI to be parsed.
	/// </param>
	/// <returns>
	/// The <see cref="System.String"/> domain name
	/// </returns>
	public static String ExtractDomain(String uri) throws ArgumentException {

		if (CSString.isNullOrEmpty(uri))
			throw new ArgumentException("Please enter a valid URI", uri);
		
		String host = uri;
		
		Log.d(tag, "extractDomain:");
		Log.d(tag, "initial host:\t" + host);
		
		String s; // the final result
		
		// Begin Chris Zarate's code
		host = host.replace("http://","");
		host = host.replace("https://","");
		Pattern p = Pattern.compile("([^/]+)");
		Matcher m = p.matcher(host);
		
		if (m.find())
			host = m.group();

		
		String[] hosts = host.split("\\.");
		if(hosts.length >= 3) {
			s = hosts[hosts.length-2]+"."+hosts[hosts.length-1];
			for(int i = 0; i < domains.length; i++) {
				if (s == domains[i]) {
					s = hosts[hosts.length-3]+'.'+s;
					break;
				}
			}
		}
		else if (hosts.length < 2) {
			throw new ArgumentException(uri + " is not a valid URI", uri);
		}
		else {
			s = CSString.join(".", hosts);
		}
		// End Chris Zarate's code

		Log.d(tag, "final host:\t" + s);

		return s;
		
	}
	
	// List of domains to treat specially.  This list comes directly from
	// the original pwdhash javascript implementation
	private static String[] domains = {"ab.ca", "ac.ac", "ac.at", 
		"ac.be", "ac.cn", "ac.il", "ac.in", "ac.jp", "ac.kr", "ac.nz", 
		"ac.th", "ac.uk", "ac.za", "adm.br", "adv.br", "agro.pl", "ah.cn", 
		"aid.pl", "alt.za", "am.br", "arq.br", "art.br", "arts.ro", 
		"asn.au", "asso.fr", "asso.mc", "atm.pl", "auto.pl", "bbs.tr", 
		"bc.ca", "bio.br", "biz.pl", "bj.cn", "br.com", "cn.com", "cng.br",
		"cnt.br", "co.ac", "co.at", "co.il", "co.in", "co.jp", "co.kr", 
		"co.nz", "co.th", "co.uk", "co.za", "com.au", "com.br", "com.cn", 
		"com.ec", "com.fr",	"com.hk", "com.mm", "com.mx", "com.pl", 
		"com.ro", "com.ru",	"com.sg", "com.tr", "com.tw", "cq.cn", "cri.nz", 
		"de.com", "ecn.br", "edu.au", "edu.cn", "edu.hk", "edu.mm", 
		"edu.mx", "edu.pl", "edu.tr", "edu.za", "eng.br", "ernet.in", 
		"esp.br", "etc.br", "eti.br", "eu.com", "eu.lv", "fin.ec", 
		"firm.ro", "fm.br", "fot.br", "fst.br", "g12.br", "gb.com",
		"gb.net", "gd.cn", "gen.nz", "gmina.pl", "go.jp", "go.kr", "go.th", 
		"gob.mx", "gov.br", "gov.cn", "gov.ec", "gov.il", "gov.in", 
		"gov.mm", "gov.mx", "gov.sg", "gov.tr", "gov.za", "govt.nz", 
		"gs.cn", "gsm.pl", "gv.ac", "gv.at", "gx.cn", "gz.cn", "hb.cn", 
		"he.cn", "hi.cn", "hk.cn", "hl.cn",	"hn.cn", "hu.com", "idv.tw", 
		"ind.br", "inf.br", "info.pl", "info.ro", "iwi.nz", "jl.cn", 
		"jor.br", "jpn.com", "js.cn", "k12.il", "k12.tr", "lel.br", "ln.cn", 
		"ltd.uk", "mail.pl", "maori.nz", "mb.ca", "me.uk", "med.br",
		"med.ec", "media.pl", "mi.th", "miasta.pl", "mil.br", "mil.ec",
		"mil.nz", "mil.pl", "mil.tr", "mil.za", "mo.cn", "muni.il",	"nb.ca",
		"ne.jp", "ne.kr", "net.au", "net.br", "net.cn",	"net.ec", "net.hk",
		"net.il", "net.in", "net.mm", "net.mx",	"net.nz", "net.pl", 
		"net.ru", "net.sg", "net.th", "net.tr",	"net.tw", "net.za", "nf.ca",
		"ngo.za", "nm.cn", "nm.kr",	"no.com", "nom.br", "nom.pl", "nom.ro",
		"nom.za", "ns.ca",	"nt.ca", "nt.ro", "ntr.br", "nx.cn", "odo.br",
		"on.ca", "or.ac", "or.at", "or.jp", "or.kr", "or.th", "org.au",
		"org.br", "org.cn", "org.ec", "org.hk", "org.il", "org.mm",
		"org.mx", "org.nz", "org.pl", "org.ro", "org.ru", "org.sg",
		"org.tr", "org.tw", "org.uk", "org.za", "pc.pl", "pe.ca", "plc.uk",
		"ppg.br", "presse.fr", "priv.pl", "pro.br",	"psc.br", "psi.br",
		"qc.ca", "qc.com", "qh.cn", "re.kr", "realestate.pl", "rec.br", 
		"rec.ro", "rel.pl", "res.in", "ru.com", "sa.com", "sc.cn",
		"school.nz", "school.za", "se.com", "se.net", "sh.cn", "shop.pl",
		"sk.ca", "sklep.pl", "slg.br", "sn.cn", "sos.pl", "store.ro",
		"targi.pl",	"tj.cn", "tm.fr", "tm.mc", "tm.pl", "tm.ro", "tm.za",
		"tmp.br", "tourism.pl", "travel.pl", "tur.br",	"turystyka.pl", 
		"tv.br", "tw.cn", "uk.co", "uk.com", "uk.net", "us.com", "uy.com", 
		"vet.br", "web.za", "web.com",	"www.ro", "xj.cn", "xz.cn", "yk.ca", 
		"yn.cn", "za.com"};
}
