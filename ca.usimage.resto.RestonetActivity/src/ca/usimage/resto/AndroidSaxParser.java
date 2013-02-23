
package ca.usimage.resto;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

public class AndroidSaxParser extends BaseParser {

	static final String CONTREVENANTS = "contrevenants";
	public AndroidSaxParser(String feedUrl) {
		super(feedUrl);
	}

	public List<Entry> parse() {
		final Entry currentEntry = new Entry();
		RootElement root = new RootElement(CONTREVENANTS);
		final List<Entry> entries = new ArrayList<Entry>();
//		Element channel = root.getChild(CHANNEL);
		Element contrevenant = root.getChild(CONTREVENANT);
		contrevenant.setEndElementListener(new EndElementListener(){
			public void end() {
				entries.add(currentEntry.copy());
			}
		});
		contrevenant.getChild(PROPRIETAIRE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentEntry.setProprietaire(body);
			}
		});
		contrevenant.getChild(CATEGORIE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentEntry.setCategorie(body);
			}
		});
		contrevenant.getChild(ETABLISSEMENT).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentEntry.setEtablissement(body);
			}
		});
		contrevenant.getChild(ADRESSE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentEntry.setAdresse(body);
			}
		});
		contrevenant.getChild(VILLE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentEntry.setVille(body);
			}
		});
		contrevenant.getChild(DESCRIPTION).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentEntry.setDescription(body);
			}
		});
		contrevenant.getChild(DATE_INFRACTION).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentEntry.setDate_infraction(body);
			}
		});
		contrevenant.getChild(DATE_JUGEMENT).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentEntry.setDate_jugement(body);
			}
		});
		contrevenant.getChild(MONTANT).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentEntry.setMontant(body);
			}
		});
//		contrevenant.getChild(DESCRIPTION).setEndTextElementListener(new EndTextElementListener(){
//			public void end(String body) {
//				currentMessage.setDescription(body);
//			}
//		});
//		contrevenant.getChild(PUB_DATE).setEndTextElementListener(new EndTextElementListener(){
//			public void end(String body) {
//				currentMessage.setDate(body);
//			}
//		});
		try {
			Xml.parse(this.getInputStream(), Xml.Encoding.ISO_8859_1, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return entries;
	}


}
