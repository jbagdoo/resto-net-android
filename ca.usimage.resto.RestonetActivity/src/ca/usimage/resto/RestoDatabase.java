package ca.usimage.resto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RestoDatabase 
 extends SQLiteOpenHelper {
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

    public RestoDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
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
        Log.w(DEBUG_TAG, "Upgrading database. Existing contents will be lost. ["
                + oldVersion + "]->[" + newVersion + "]");
        db.execSQL("DROP TABLE IF EXISTS resto");
        onCreate(db);
    }
 }