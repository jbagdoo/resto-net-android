package ca.usimage.resto;


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
   

      } 

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, 
          int newVersion) 
      {
      } 
   } 
} 



