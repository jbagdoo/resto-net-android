package ca.usimage.resto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;



public class RestonetActivity extends Activity implements ListItemSelectListener, ListItemMapListener, OnInfoWindowClickListener, ActionBar.TabListener{
	
	 HashMap<String, Integer> extraMarkerInfo = new HashMap<String, Integer>();
    private boolean useLogo = false;
    private boolean showHomeUp = true;
    private static final int RESTO_RECENT_LOADER = 0x01;
	private static final int RESTO_ALPHA_LOADER = 0x02;
	private static final int RESTO_HIGH_LOADER = 0x03;
	private static final int RESTO_SEARCH_LOADER = 0x04;
	private  static final int RESTO_PLUS_LOADER = 0x05;
	private RestoDatabase mDB;
	private SQLiteDatabase sqlDB;

	private int tab_pos;
	private String query = "";
			
    RecentFragment listeFrg = new RecentFragment();
    AlphaListeFragment alphaFrg = new AlphaListeFragment();
    HighListeFragment highFrg = new HighListeFragment();
    RechercheListeFragment rechFrg = new RechercheListeFragment();
    PlusListeFragment plusFrg = new PlusListeFragment();
    MapFragment mapFrg = new CarteFragment();

	private List<Entry> entries;
	 ArrayList<String> etablissements = new ArrayList<String>();
	 ProgressDialog dialog;
	

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  
    	setContentView(R.layout.main);
      	 final ActionBar ab = getActionBar();
        ab.setDisplayUseLogoEnabled(useLogo);
		 ab.setDisplayShowHomeEnabled(true);
      // set up tabs nav

		  ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	   //     ab.setDisplayOptions(0, ActionBar.);
		  FragmentManager fragmentManager = getFragmentManager();
		  
		
		  
  	 	if (savedInstanceState != null){
 		
   		 tab_pos = savedInstanceState.getInt("tabState");
   		 query = savedInstanceState.getString("searchQuery");
   		ab.addTab(ab.newTab().setText(R.string.tab_recente).setTabListener(this),0,false);
         ab.addTab(ab.newTab().setText(R.string.tab_fortes).setTabListener(this),1,false);
         ab.addTab(ab.newTab().setText(R.string.tab_plus).setTabListener(this),2,false);
         ab.addTab(ab.newTab().setText(R.string.tab_alpha).setTabListener(this),3,false);

         if (null == fragmentManager.findFragmentByTag("RECH"))  {
	 	 Log.e("on create","set selection");
         ab.setSelectedNavigationItem(tab_pos);
         } else {

	   	  FragmentTransaction ft = getFragmentManager().beginTransaction();	   
  	    Bundle arguments = new Bundle();
  	    arguments.putString("searchQuery", query);
  	  
  	    rechFrg.setArguments(arguments);
          
		   ft.replace(R.id.listeFragment, rechFrg, "RECH");  
		   ft.addToBackStack(null);
		   ft.commit();
		   
		   
      }
      
   	
  	 	} else {	 		
    ab.addTab(ab.newTab().setText(R.string.tab_recente).setTabListener(this),0,true);
    ab.addTab(ab.newTab().setText(R.string.tab_fortes).setTabListener(this),1,false);
    ab.addTab(ab.newTab().setText(R.string.tab_plus).setTabListener(this),2,false);
    ab.addTab(ab.newTab().setText(R.string.tab_alpha).setTabListener(this),3,false);
	 
   	}
  	
      
    
 	
	  	dialog = new ProgressDialog(RestonetActivity.this);
        dialog.setCancelable(false);
       
        dialog.setMessage(getString(R.string.maj_donnees));
        // set the progress to be horizontal
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // reset the bar to the default value of 0
        dialog.setProgress(0);
        
      
 
   }
   
   
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
          query = intent.getStringExtra(SearchManager.QUERY);
 
  	   	  FragmentTransaction ft = getFragmentManager().beginTransaction();
	  	     if (null == getFragmentManager().findFragmentByTag("RECH")) {
	  	      Bundle arguments = new Bundle();
	   	    arguments.putString("searchQuery", query);
	   	  
	   	    rechFrg.setArguments(arguments);
	             ft.replace(R.id.listeFragment, rechFrg, "RECH");
	             ft.addToBackStack(null);
	  	   	     ft.commit();
	  	     }else{
	  	    	   RechercheListeFragment rechFrg = (RechercheListeFragment)
	  	    			   getFragmentManager().findFragmentByTag("RECH");
      	 rechFrg.afficheList(RESTO_SEARCH_LOADER, query);
	  	     }
        }
        
    }


	public void onItemSelected(int s, long rowId) {
	
		afficheDetailFragment(rowId, false);
	
	}
	
	
	public void afficheDetailFragment (long rowId, Boolean changeTab){
	
//	
//		//detecter si fragment Detailfragment se trouve dans cette activité		
		  FragmentManager fragmentManager = getFragmentManager();
          FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
          DetailFragment detailFrg = new DetailFragment();
         
        	   
        	    Bundle arguments = new Bundle();
        	    arguments.putLong("rowid", rowId);
        	  
        	    detailFrg.setArguments(arguments);
        	  
          
       
	
		if(null == fragmentManager.findFragmentById(R.id.detailFragment)|| !detailFrg.isInLayout()){//pas de fragment DetailFragment ici

			Intent intention = new Intent(getApplicationContext(), DetailActivity.class);
			intention.putExtra("rowid", rowId);
			startActivity(intention);
		}
		else{//fragment est dans cette activité
			 
			 fragmentTransaction.add(R.id.detailFragment, detailFrg, "DETAIL");
//			 fragmentTransaction.addToBackStack(null);
			 fragmentTransaction.commit();

	
		}					

	}	
	
	public void afficheCarteFragment (long rowId){
		
    int statusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
	    
    if (statusCode == ConnectionResult.SUCCESS) {	

			  FragmentManager fragmentManager = getFragmentManager();
	          FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	          CarteFragment carteFrg = new CarteFragment();
	         
	        	   
	        	    Bundle arguments = new Bundle();
	        	    arguments.putLong("rowid", rowId);
	        	  
	        	    carteFrg.setArguments(arguments);
	
			if(null == fragmentManager.findFragmentById(R.id.carteFragment)|| !carteFrg.isInLayout()){//pas de fragment DetailFragment ici

				Intent intention = new Intent(getApplicationContext(), CarteActivity.class);
				
				intention.putExtra("rowid", rowId);
				startActivity(intention);
			}
			else{//fragment est dans cette activité
				 
				 fragmentTransaction.add(R.id.carteFragment, carteFrg, "Carte");
				 fragmentTransaction.commit();

			}					
         } else {
          	Toast toast = Toast.makeText(getApplicationContext(), R.string.no_google_play, Toast.LENGTH_LONG);
	   		       toast.show();
      }
    }	
	
//	public void afficheCarteFragment (long rowId){
//	
//	   int statusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
//	   if (statusCode == ConnectionResult.SUCCESS) {
//	            //OK
//	     	  FragmentManager fragmentManager = getFragmentManager();
//			 
//		     if (null == fragmentManager.findFragmentByTag("MAP")) {
//		    	  FragmentTransaction fragmentTransaction =
//				           fragmentManager.beginTransaction();
//		    	    Bundle arguments = new Bundle();
//	        	    arguments.putLong("rowid", rowId);
//	        	  
//	        	    mapFrg.setArguments(arguments);
//		     
//		           fragmentTransaction.replace(R.id.listeFragment, mapFrg, "MAP");
//		           fragmentTransaction.addToBackStack(null);
//			       fragmentTransaction.commit();
//		         }
//              } else {
//       	               Toast toast = Toast.makeText(getApplicationContext(), R.string.no_google_play, Toast.LENGTH_LONG);
//  		               toast.show();
//              }
//				
//
//	}	
	
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	 
		switch (item.getItemId()) {
		case R.id.itemMAJ:
			  
				   showDialog();
				   return true;

		case R.id.itemRECH:
            	onSearchRequested(); 
            
                return true;
                
		case R.id.itemMAP:
			afficheCarteFragment(99999);
			 return true;
		}

		return false;
	}

    @Override
    public boolean onSearchRequested() {
       
        return super.onSearchRequested();
    }
    
    
    
    public static boolean haveInternet(Context ctx) {

        NetworkInfo info = (NetworkInfo) ((ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
                

        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            // here is the roaming option you can change it if you want to
            // disable internet while roaming, just return false
            return false;
        }
        return true;
    }
    
   

	private class GetCityData extends AsyncTask<String, Integer, String> {
	    
		@Override
		protected String doInBackground(String... urls) {
			// get data from web xml file and store in entries list
		
			getData();
			
			// prepare to store data into sqlite database
			  dialog.setMax(entries.size());
		
			  // erase table resto before inserting new data... null selection deletes all rows
		
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
		    		 
		    		 publishProgress(i);	    		

		    	}

 		
			return "";
		}
		 @Override
	        protected void onProgressUpdate(Integer...progress) {

	        	dialog.setProgress(progress[0]);
		        
		 }
		@Override
		protected void onPostExecute(String result) {
			if(dialog!=null)
				{
					 dialog.dismiss();
				}
	   

		}
	}

       
   	private void getData() {
   		
    	try{
 
	    	
	    	RestoParser parser = RestoParserFactory.getParser();
	    	entries = parser.parse();
	
		
    	} catch (Throwable t){
    		Log.e("Restonet",t.getMessage(),t);
    	}
    }
   	
//    
   	
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	     int position = tab.getPosition();
		  FragmentManager fragmentManager = getFragmentManager();
	
Log.e("tabreselected pos="," "+position);
		  // position cursor at top of list if user retaps a tab
	      switch (position) {
	  	case 0:
             if (null ==  fragmentManager.findFragmentByTag("RECENT")) {
            	
	  	       ft.replace(R.id.listeFragment, listeFrg, "RECENT");
	  	        
	  	     }
	  	       else{
	  	    	   RecentFragment listeFrg = (RecentFragment)
	  	    			   getFragmentManager().findFragmentByTag("RECENT");
	  	  	   if (listeFrg.isVisible()) {
	  		       listeFrg.setSelection(0);
	  	  	   }
	  	     }	
	  			break;
	
	  	case 1:

     if (null == fragmentManager.findFragmentByTag("HIGH")) {
		    	 
		         ft.replace(R.id.listeFragment, highFrg, "HIGH");
		       
		         
		         
		     } else {    
	  	    	   HighListeFragment highFrg = (HighListeFragment)
	  	    			   getFragmentManager().findFragmentByTag("HIGH");
	  	  	   if (highFrg.isVisible()) {
		    	 highFrg.setSelection(0);
	  	  	   }
		     }
	  		 break;
	  
	         
  	case 2: 
	     if (null == fragmentManager.findFragmentByTag("PLUS")) {
	    	 
	         ft.replace(R.id.listeFragment, plusFrg, "PLUS");
	         
	     } else {    
 	    	   PlusListeFragment plusFrg = (PlusListeFragment)
 	    			   getFragmentManager().findFragmentByTag("PLUS");
 	    	   if (plusFrg.isVisible()) {
	         	 plusFrg.setSelection(0);
 	    	   }
	    	
	     }
		 break;
		 
  	case 3: 
	     if (null == fragmentManager.findFragmentByTag("ALPHA")) {
	    	 
	         ft.replace(R.id.listeFragment, alphaFrg, "ALPHA");
	        
	     } else {    
 	    	   AlphaListeFragment alphaFrg = (AlphaListeFragment)
 	    			   getFragmentManager().findFragmentByTag("ALPHA");
 	   	   if (alphaFrg.isVisible()) {
	    	 alphaFrg.setSelection(0);
 	   	   }
	     }
		 break;
	    	  
	      }
	


	}
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
	
		  FragmentManager fragmentManager = getFragmentManager();
          //add a fragment

		
	 	  
         int position = tab.getPosition();
 
         Log.e("tab selected pos="," "+position);
      switch (position) {
  	case 0:
  	 
  	  
  	     if (null == fragmentManager.findFragmentByTag("RECENT")||(!listeFrg.isVisible())) {
           ft.replace(R.id.listeFragment, listeFrg, "RECENT");
         
          }

  			break;

  	case 1:
  		 
  	     if (null == fragmentManager.findFragmentByTag("HIGH") || (!highFrg.isVisible())) {
	           ft.replace(R.id.listeFragment, highFrg, "HIGH");
	        
	     }
  		 break;
 	case 2:
 		 
	     if ((null == fragmentManager.findFragmentByTag("PLUS")) || (!plusFrg.isVisible())) {
	           ft.replace(R.id.listeFragment, plusFrg,"PLUS");    
	     }

 			break;
  	case 3: 
  
	     if (null == fragmentManager.findFragmentByTag("ALPHA")  || (!alphaFrg.isVisible())) {
	           ft.replace(R.id.listeFragment, alphaFrg, "ALPHA");
	         
	     }
  		 break;
      }	




	}
	
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

    
    
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    // getACtionBarr returns null for RECH fragment
		  FragmentManager fragmentManager = getFragmentManager();
	    if (null == fragmentManager.findFragmentByTag("RECH"))  {
	    	 outState.putInt("tabState", getActionBar().getSelectedTab().getPosition());
	    }else{
	    outState.putInt("tabState", 0);
	    }
	    outState.putString("searchQuery", query);
	}
    
	void showDialog() {
	    DialogFragment dataDialog = GetDataDialog.newInstance(
	            R.string.dialog);
	    dataDialog.show(getFragmentManager(), "dialog");
	}

	public void doPositiveClick() {
	
		// first check if internet connection is available
		
		if (haveInternet(this)){
			 dialog.show();
			 
			 mDB = new RestoDatabase(getBaseContext());
			 
			 sqlDB = mDB.getWritableDatabase();
// before downloading city data, delete the existing sqlite database by forcing a call to onUpgrade by incrementing
// the database's version
			 
			 int version = sqlDB.getVersion();
			 mDB.onUpgrade(sqlDB, version, version+1);
			  
		      GetCityData task = new GetCityData();
					task.execute(new String[] { "" });
				
				   } else {
					   Toast toast = Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_LONG);
					   toast.show();
				   }		
	  
	}

	public void doNegativeClick() {
	  
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// when marker infowindow is clicked, show resto details
		int rowid = Integer.parseInt(arg0.getSnippet());
		afficheDetailFragment(rowid, false);
		
		
		
	}

	@Override
	public void onItemMapSelected(long rowId) {
		// displays map when resto map button is clicked with resto  at center
		
		afficheCarteFragment(rowId);
		
		
	}
	
	
	public void onDestroy(){
		super.onDestroy();
		if(dialog!=null)
		{
		dialog.cancel();
		}

		}
	
	
 }
    
