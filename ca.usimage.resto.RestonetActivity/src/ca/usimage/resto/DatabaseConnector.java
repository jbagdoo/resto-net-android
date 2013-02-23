package ca.usimage.resto;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DatabaseConnector 
{
  
   private static final String DATABASE_NAME = "Resto";
   private SQLiteDatabase database; 
   private DatabaseOpenHelper databaseOpenHelper; 

  
   public DatabaseConnector(Context context) 
   {
     
      databaseOpenHelper = 
         new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
   } 

   
   public void open() throws SQLException 
   {
     
      database = databaseOpenHelper.getWritableDatabase();
   } 

  
   public void close() 
   {
      if (database != null)
         database.close(); 
   } 
   
   public void clear() 
   {
      if (database != null)
         database.delete("resto", null, null); 
   } 

   
   @SuppressLint("NewApi") public void ajoutResto(Entry etablissement) 
   {
      ContentValues ajout_resto = new ContentValues();
  //      open(); 
    	
    			 ajout_resto.put("etablissement", etablissement.getEtablissement());
    			 ajout_resto.put("proprietaire", etablissement.getProprietaire());
    			 ajout_resto.put("ville", etablissement.getVille());
    			 ajout_resto.put("montant", etablissement.getMontant());
    			 ajout_resto.put("adresse", etablissement.getAdresse());
    			 ajout_resto.put("categorie", etablissement.getCategorie());
    			 ajout_resto.put("date_infraction", etablissement.getDate_infraction());
    			 ajout_resto.put("date_jugement", etablissement.getDate_jugement());
    			 ajout_resto.put("description", etablissement.getDescription());
    			 ajout_resto.put("id", etablissement.getId());
    			 database.insert("resto", null, ajout_resto);
    		 
             
 //      close(); 
   } 

  
   public void ecrireCritique(long id,  String nom, String itemmenu, int cote, 
		      String commentaire) 
   {
      ContentValues modif_resto = new ContentValues();
//      modif_resto.put("nom", nom);
//      modif_resto.put("itemmenu,", itemmenu);
      modif_resto.put("cote", cote);
      modif_resto.put("commentaire", commentaire);
      open(); 
      database.update("resto", modif_resto, "_id=" + id, null);
      close(); 
   } 

  
   public Cursor lireResto() 
   {
	   
      return  database.rawQuery("SELECT _id, etablissement FROM resto ORDER BY date_infraction DESC", null);
      
   } 
   
   public Cursor rechercheResto(String requete) 
   {
	   return database.query("resto",null, "etablissement like \"%" + requete + "%\"", null, null, null, "etablissement");   
  
      
   } 
   
   public Cursor insertInfrac(Entry infrac) 
   {
	   
      return  database.rawQuery("SELECT DISTINCT nom FROM resto", null);
      
   } 
   
   public long getRowId (String nom_resto)
   {
	   Cursor IDs = database.query("resto",new String[] {"_id"}, "nom = \"" + nom_resto + "\"", null, null, null, null);
	   IDs.moveToFirst();
	   int IDIndex = IDs.getColumnIndex("_id");
	   Long FirstID = IDs.getLong(IDIndex);
	   return FirstID;
   }
   
   

   public Cursor lireMenu(String nom_resto) 
   {
	   
	     return database.query("resto",null, "nom = \"" + nom_resto + "\"", null, null, null, "nom");
      
      
   } 
   
   public Cursor lireDetails(long rowId) 
   {
	   
      return  database.query("resto",null, "_id=" + rowId, null, null, null, null);
      
   } 
   
   
   private class DatabaseOpenHelper extends SQLiteOpenHelper 
   {
     
      public DatabaseOpenHelper(Context context, String name,
         CursorFactory factory, int version) 
      {
         super(context, name, factory, version);
      } 

   
      @Override
      public void onCreate(SQLiteDatabase db) 
      {
   
         String createQuery = "CREATE TABLE resto" +
            "(_id integer primary key autoincrement," +
            "id TEXT, proprietaire TEXT,  categorie TEXT , etablissement TEXT,  adresse TEXT, ville TEXT, description TEXT, date_infraction timestamp, date_jugement timestamp, montant TEXT  );";
            
      
                  
         db.execSQL(createQuery);
      } 

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, 
          int newVersion) 
      {
      } 
   } 
} 



