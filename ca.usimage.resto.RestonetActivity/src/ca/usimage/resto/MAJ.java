package ca.usimage.resto;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;



public class MAJ extends Service{

	private List<Entry> entries;
	private RestoDatabase mDB;
	private SQLiteDatabase sqlDB;
	private Handler handler;

	
	 @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
		 handler = new Handler();
	     GetCityData task = new GetCityData();
						task.execute(new String[] { "" });
					
		
	    return Service.START_NOT_STICKY;
	  }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	
	

	private class GetCityData extends AsyncTask<String, Integer, String> {
		

		 
		 
	    
		@Override
		protected String doInBackground(String... urls) {
			// get data from web xml file and store in entries list
			NotificationManager mNotifyManager =
			        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			Notification.Builder mBuilder = new Notification.Builder(getApplicationContext());
			mBuilder.setContentTitle("Restonet")
			    
			    .setContentText(getString(R.string.maj_donnees))
			   
			    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.restonet_s))
			    .setSmallIcon(R.drawable.restonet4);
			    

			if (getData()) {
				 mDB = new RestoDatabase(getBaseContext());
				 
				 sqlDB = mDB.getWritableDatabase();
	// before downloading city data, delete the existing sqlite database by forcing a call to onUpgrade by incrementing
	// the database's version
				 
				 int version = sqlDB.getVersion();
				 mDB.onUpgrade(sqlDB, version, version+1);
			
			
			// prepare to store data into sqlite database
//			  dialog.setMax(entries.size());
		
//	
		
			
			
			    ContentValues ajout_resto = new ContentValues();
			    
		
			    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.CANADA_FRENCH);
		        String adresse;


	            int i=0;
	            List<Address> adresse_list;
	            Address location;
	            Double latitude, longitude;
			    	   latitude = 45.5086699;
			    	   longitude = -73.5539925;

		    	for (Entry msg : entries){
		    		  mBuilder.setProgress(entries.size(), i, true);
		    		   mNotifyManager.notify(0, mBuilder.getNotification());
//  get latitude and longitude from address using google geocoder
		    		             if (geocoder.isPresent()) {
		    		            	 adresse = msg.getAdresse() +"," + msg.getVille() + ", QC CANADA";
	    	      	           
	    	     		        try{
	    	     			         adresse_list  = geocoder.getFromLocationName(adresse, 1);

	    	     			       if (adresse_list != null && adresse_list.size() > 0) {
	    	     			         location = adresse_list.get(0);
	    	     			       
	    	     			        latitude = location.getLatitude();
	    	     			        longitude = location.getLongitude();

	    	     			       }
	    	     			         else {  // if adresse is null, postal code is probably bad, remove postal code and try again
	    	     			        	 
	    	     			        	 adresse = msg.getAdresse() +"," + msg.getVille().substring(0, msg.getVille().length()-6) + ", QC CANADA"; 
	    	     			        	try{
	   	    	     			         adresse_list  = geocoder.getFromLocationName(adresse, 1);

	   	    	     			       if (adresse_list != null && adresse_list.size() > 0) {
	   	    	     			         location = adresse_list.get(0);
	 	    	     			        latitude = location.getLatitude();
		    	     			        longitude = location.getLongitude();

	   	    	     			       } else {
	   	    	     			    	   Log.e(" "+msg.getEtablissement()," null geocode");
	   	    	     			       }
	   	    	     			    	   
	    	     			        	 }
		    	     			        catch(IOException e) {
		    	     			         Log.e("Geocoder IOException i="+i+" "+msg.getEtablissement(), e.getMessage()); 
		    	     			         
		    	     			        }
	    	     			         } 	     			      
	    	     			         
	    	     			   //	    	     			       }
	    	     			        }
	    	     			        catch(IOException e) {
	    	     			         Log.e("Gecoder IOException i="+i+" "+msg.getEtablissement(), e.getMessage()); 
	    	     			         
	    	     			        }
	    	     		    
		    		             }
//		    		  }
		    	      			 ajout_resto.put("etablissement", msg.getEtablissement());
		    	      			 ajout_resto.put("proprietaire", msg.getProprietaire());
		    	      			 ajout_resto.put("ville", msg.getVille());
		    	      			 ajout_resto.put("montant", msg.getMontant());
		    	      			 ajout_resto.put("adresse", msg.getAdresse());
		    	      			 ajout_resto.put("categorie", msg.getCategorie());
		    	      			 ajout_resto.put("date_infraction", msg.getDate_infraction());
		    	      			 ajout_resto.put("date_jugement", msg.getDate_jugement());
		    	      			 ajout_resto.put("description", msg.getDescription());
		    	      			 ajout_resto.put("id", msg.getId());
		    	      			 ajout_resto.put("latitude", latitude);
		    	      			 ajout_resto.put("longitude", longitude);
		    	      			 // insert each row into sql database
		    	      		     getContentResolver().insert(RestoProvider.CONTENT_URI, ajout_resto);
		    	              	    		
		    	i++;
		    		 
    		

		    	}
		    	  mNotifyManager.cancel(0);
 		
			
		} else {
			
			handler.post(new Runnable() {
			    public void run() {
			    	Toast toast = Toast.makeText(getApplicationContext(), R.string.no_xml, Toast.LENGTH_LONG);
			        toast.show();
			    }
			 });
			  mNotifyManager.cancel(0);
			

		}
			
		stopSelf();
			return "";
		}
	
		@Override
		protected void onPostExecute(String result) {

		}
	}
	
    
  	private boolean getData() {
  		
   	try{

	    	
	    	RestoParser parser = RestoParserFactory.getParser();
	    	entries = parser.parse();
	    	return true;
	
		
   	} catch (Throwable t){
   		Log.e("NO XML",t.getMessage(),t);
   	
	return false;
   	}
   }
}
