package ca.usimage.resto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

public class RestoDatabase  extends SQLiteOpenHelper {
	 private static String DB_PATH = "/data/data/ca.usimage.resto/databases/";
    private static final String DEBUG_TAG = "RestoDatabase";
    private static final int DB_VERSION = 2;
    
       
    private static final String DB_NAME = "Resto";
    public static final String TABLE_RESTO = "resto";
    public static final String ID = "_id";
    public static final String COL_ETAB = "etablissement";
    public static final String COL_PROPRIO = "proprietaire";
    public static final String COL_MONTANT = "montant";
    public static final String COL_LAT = "latitude";
    public static final String COL_LONG = "longitude";
    public static final String COL_ADR = "adresse";
    public static final String COL_COUNT = "count(*)";
    public static final String COL_DATE_JUGE ="date_jugement";
    
    private static final String SP_KEY_DB_VER = "db_ver";
    private SQLiteDatabase myDataBase; 
    private final Context myContext;
    

    public RestoDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
        initialize();
    }
    
    private void initialize() {
        if (databaseExists()) {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(myContext);
            int dbVersion = prefs.getInt(SP_KEY_DB_VER, 1);
            if (DB_VERSION != dbVersion) {
                File dbFile = myContext.getDatabasePath(DB_NAME);
                if (!dbFile.delete()) {
                    Log.w("RestoDatabase", "Unable to update database");
                }
            }
        }
        if (!databaseExists()) {
            createDatabase();
        }
    }

    
    /**
     * Returns true if database file exists, false otherwise.
     * @return
     */
    private boolean databaseExists() {
        File dbFile = myContext.getDatabasePath(DB_NAME);
        return dbFile.exists();
    }

    /**
     * Creates database by copying it from assets directory.
     */
    public void createDatabase() {
        String parentPath = myContext.getDatabasePath(DB_NAME).getParent();
        String path = myContext.getDatabasePath(DB_NAME).getPath();

        File file = new File(parentPath);
        if (!file.exists()) {
            if (!file.mkdir()) {
                Log.w("RestoDatabase", "Unable to create database directory");
                return;
            }
        }

        InputStream is = null;
        OutputStream os = null;
        try {
            is = myContext.getAssets().open(DB_NAME);
            os = new FileOutputStream(path);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(myContext);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(SP_KEY_DB_VER, DB_VERSION);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    
    @Override
    public void onCreate(SQLiteDatabase db) {
    	 Log.e("resto", "oncreate");

        String createQuery = "CREATE TABLE resto" +
                "(_id integer primary key autoincrement," +
                "id TEXT, proprietaire TEXT,  categorie TEXT , etablissement TEXT,  adresse TEXT, ville TEXT, description TEXT, date_infraction timestamp, date_jugement timestamp, montant INTEGER, latitude DOUBLE, longitude DOUBLE  );";
    
             db.execSQL(createQuery);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  
//    if(newVersion > oldVersion){
//        {
//            Log.v("Database Upgrade", "Database version higher than old.");
//
//            try { 
//            	
//                copyDataBase(); 
//                Log.v("resto", "done copying Database");
//            } catch (IOException e) { 
//                throw new Error("Error upgrading database"); 
//            } 
//            
//        	try {
//
//       	        openDataBase();
//
//       		} catch (SQLException e) {
//
//           		throw new Error("Error opening database");
//
//           	}
//         }

//    }

  }
    
  
   public void createDataBase() throws IOException{

   	boolean dbExist = checkDataBase();
    Log.e("resto", "createDatabase");
   	if(dbExist){
   		//do nothing - database already exist
   	}else{

   		//By calling this method an empty database will be created into the default system path
              //of your application so we are gonna be able to overwrite that database with our database.
       	this.getReadableDatabase();
            

       	try {

   			copyDataBase();

   		} catch (IOException e) {

       		throw new Error("Error copying database");

       	}
    
   	}

   }

   /**
    * Check if the database already exist to avoid re-copying the file each time you open the application.
    * @return true if it exists, false if it doesn't
    */
   private boolean checkDataBase(){

   	SQLiteDatabase checkDB = null;

   	try{
   		String myPath = DB_PATH + DB_NAME;
   		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

   	}catch(SQLiteException e){

   		//database does't exist yet.

   	}

   	if(checkDB != null){

   		checkDB.close();

   	}

   	return checkDB != null ? true : false;
   }

   /**
    * Copies your database from your local assets-folder to the just created empty database in the
    * system folder, from where it can be accessed and handled.
    * This is done by transfering bytestream.
    * */
   private void copyDataBase() throws IOException{

   	//Open your local db as the input stream
   	InputStream myInput = myContext.getAssets().open(DB_NAME);

   	// Path to the just created empty db
   	String outFileName = DB_PATH + DB_NAME;
   
   	//Open the empty db as the output stream
   	OutputStream myOutput = new FileOutputStream(outFileName);

   	//transfer bytes from the inputfile to the outputfile
   	byte[] buffer = new byte[1024];
   	int length;
   	while ((length = myInput.read(buffer))>0){
   	
   		myOutput.write(buffer, 0, length);
   	}

   	//Close the streams
   	myOutput.flush();
   	myOutput.close();
   	myInput.close();

   }

   public void openDataBase() throws SQLException{

   	//Open the database
       String myPath = DB_PATH + DB_NAME;
   	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

   }

   @Override
	public synchronized void close() {

   	    if(myDataBase != null)
   		    myDataBase.close();

   	    super.close();

	}

    
    
   //delete database
   public void db_delete()
   {
	    
         File file = new File(DB_PATH + DB_NAME);
         if(file.exists())
         {
               file.delete();
               System.out.println("delete database file.");
         }
   }
    
    
 }