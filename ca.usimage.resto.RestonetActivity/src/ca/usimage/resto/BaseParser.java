package ca.usimage.resto;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public abstract class BaseParser  implements RestoParser {

	// names of the XML tags
	static final String CONTREVENANT = "contrevenant";
	static final String PROPRIETAIRE = "proprietaire";
	static final String CATEGORIE = "categorie";
	static final String ETABLISSEMENT = "etablissement";
	static final String ADRESSE = "adresse";
	static final String VILLE = "ville";
	static final String DESCRIPTION = "description";
	static final String DATE_INFRACTION = "date_infraction";
	static final String DATE_JUGEMENT = "date_jugement";
	static final String MONTANT = "montant";
	
	
	
	private final URL feedUrl;

	protected BaseParser(String feedUrl){
		try {
			this.feedUrl = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	protected InputStream getInputStream() {
		try {
			URLConnection urlc = feedUrl.openConnection();
			urlc.addRequestProperty("User-Agent", "firefox");
			return urlc.getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
