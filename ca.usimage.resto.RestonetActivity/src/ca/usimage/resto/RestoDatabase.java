package ca.usimage.resto;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RestoDatabase  extends SQLiteOpenHelper {
	 private static String DB_PATH = "/data/data/ca.usimage.resto/databases/";
    private static final String DEBUG_TAG = "RestoDatabase";
    private static final int DB_VERSION = 1;
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
    
    private SQLiteDatabase myDataBase; 
    private final Context myContext;
    

    public RestoDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createQuery = "CREATE TABLE resto" +
                "(_id integer primary key autoincrement," +
                "id TEXT, proprietaire TEXT,  categorie TEXT , etablissement TEXT,  adresse TEXT, ville TEXT, description TEXT, date_infraction timestamp, date_jugement timestamp, montant INTEGER, latitude DOUBLE, longitude DOUBLE  );";
    
             db.execSQL(createQuery);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    
        db.execSQL("DROP TABLE IF EXISTS resto");
        onCreate(db);
    }
    
  
   public void createDataBase() throws IOException{

   	boolean dbExist = checkDataBase();

   	if(dbExist){
   		//do nothing - database already exist
   	}else{

   		//By calling this method and empty database will be created into the default system path
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

    
    
    
    
    
 }