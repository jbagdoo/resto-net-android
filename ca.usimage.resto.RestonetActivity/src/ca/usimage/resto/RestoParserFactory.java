package ca.usimage.resto;

import ca.usimage.resto.AndroidSaxParser;




public abstract class RestoParserFactory {

//	static String feedUrl = "http://ville.montreal.qc.ca/pls/portal/portalcon.contrevenants_recherche?p_mot_recherche=,tous.2011";
//	static String feedUrl = "http://depot.ville.montreal.qc.ca/inspection-aliments-contrevenants/data.xml";
	static String feedUrl = "http://donnees.ville.montreal.qc.ca/storage/f/2013-10-08T16%3A11%3A38.814Z/inspection-aliments-contrevenants.xml";
	public static RestoParser getParser() {
		
				return new AndroidSaxParser(feedUrl);
	
		
		}
	}


