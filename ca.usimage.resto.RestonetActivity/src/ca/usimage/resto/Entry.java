package ca.usimage.resto;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.net.ParseException;
import android.util.Log;



public class Entry {
	
	
	private long id;
	private String proprietaire;
	private String etablissement;
	private String adresse;
	private String ville;
	private String date_infraction;
	private String categorie;
	private String description;
	private String   date_jugement;
	private String montant;

	public Entry(long id, String proprietaire, String categorie, String etablissement, String adresse, String ville, String description, String date_infraction, String date_jugement, String montant ) {
		super();
		this.id = id;
		this.proprietaire = proprietaire;
		this.categorie = categorie;
		this.etablissement = etablissement;
		this.adresse = adresse;
		this.ville = ville;
		this.description = description;
		this.date_infraction = date_infraction;
		this.date_jugement = date_jugement;
		this.montant = montant;
	}

	public Entry() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProprietaire() {
		return proprietaire;
	}
	public void setProprietaire(String proprietaire) {
		this.proprietaire = proprietaire;
	}
	public String getCategorie() {
		return categorie;
	}
	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}
	public String getEtablissement() {
		return etablissement;
	}
	public void setEtablissement(String etablissement) {
		this.etablissement = etablissement;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	private String convert_date(String date) {
	// convert date from text to timestamp using french locale	
        SimpleDateFormat simpleDateFormat =         new SimpleDateFormat("dd MMMMMMM yyyy", Locale.FRENCH); 
        java.util.Date dateObj = null;
        java.sql.Date sqlDate = null;
        try {
        	dateObj = simpleDateFormat.parse(date);
        	
        	sqlDate = new java.sql.Date(dateObj.getTime());         	
        	} catch (ParseException e) {
        
        	e.printStackTrace();
        	} catch (java.text.ParseException e) {
			
				e.printStackTrace();
			}
            date = sqlDate.toString();
            return date;
	}        
	
    public String getDate_infraction() {
		return convert_date(date_infraction);
	}
	public void setDate_infraction(String date_infraction) {
		this.date_infraction = date_infraction;
	}
	public String getDate_jugement() {
		return convert_date(date_jugement);
	}
	public void setDate_jugement(String date_jugement) {
		this.date_jugement = date_jugement;
	}
	public int getMontant() {
		int valeur = 0;
		// remove trailing $ sign
		montant = montant.replace(" $","");
		try {
			// convert montant string to integer (from ordering purposes)
		    valeur = Integer.parseInt(montant);
		} catch(NumberFormatException nfe) {
		   System.out.println("Could not parse " + nfe);
		}
		return valeur;
	}
	public void setMontant(String montant) {
		this.montant = montant;
	}


	public Entry copy(){
		Entry copy = new Entry();
		copy.proprietaire = proprietaire;
		copy.categorie = categorie;
		copy.description = description;
		copy.etablissement = etablissement;
		copy.adresse = adresse;
		copy.ville = ville;
		copy.date_infraction = date_infraction;
		copy.date_jugement = date_jugement;
		copy.montant = montant;
		
		return copy;
	}

}
