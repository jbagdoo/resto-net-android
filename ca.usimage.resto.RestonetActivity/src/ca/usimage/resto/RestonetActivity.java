package ca.usimage.resto;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;










import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;

import android.database.Cursor;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import android.widget.ArrayAdapter;
import android.widget.Toast;





public class RestonetActivity extends Activity implements ListItemSelectListener, ActionBar.TabListener{
	Cursor restos;
	
    private boolean useLogo = false;
    private boolean showHomeUp = true;
    private static final int RESTO_RECENT_LOADER = 0x01;
	private static final int RESTO_ALPHA_LOADER = 0x02;
	private static final int RESTO_HIGH_LOADER = 0x03;
	private static final int RESTO_SEARCH_LOADER = 0x04;

	private List<Entry> entries;
	 ArrayList<String> etablissements = new ArrayList<String>();
	 ProgressDialog dialog;
	    DatabaseConnector databaseConnector = 
     new DatabaseConnector(RestonetActivity.this); 
//	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    setContentView(R.layout.main);
    	setContentView(R.layout.liste);
    	
    	
        final ActionBar ab = getActionBar();
     
      // set defaults for logo & home up
      ab.setDisplayHomeAsUpEnabled(showHomeUp);
     ab.setDisplayUseLogoEnabled(useLogo);
		ab.setNavigationMode(2);
		ab.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

      // set up tabs nav
      
          ab.addTab(ab.newTab().setText(R.string.tab_recente).setTabListener(this),0,true);
          ab.addTab(ab.newTab().setText(R.string.tab_alpha).setTabListener(this),1,false);
          ab.addTab(ab.newTab().setText(R.string.tab_fortes).setTabListener(this),2,false);
      
	
//		ab.removeAllTabs();
 
  //      handleIntent(getIntent());
//     	ArrayAdapter<String> adapter = 
//     			new ArrayAdapter<String>(this, R.layout.row,etablissements);
//     		    this.setListAdapter(adapter);
//     		    databaseConnector.open();
//     		    restos = databaseConnector.lireResto();
//     		    restos.moveToFirst();
//     		    int i = restos.getColumnIndex("etablissement");
//     	        while (restos.isAfterLast() == false) {
//     	        	
//     	        	Log.e ("Resto", restos.getString(i));
//     	       etablissements.add(	restos.getString(i) );
//     	       restos.moveToNext();
//      
//        }
//       
//        
//        
//
//
//          
	  	dialog = new ProgressDialog(RestonetActivity.this);
        dialog.setCancelable(true);
        dialog.setMessage("Mise à jour des données en cours...");
        // set the progress to be horizontal
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // reset the bar to the default value of 0
        dialog.setProgress(0);
//        
//     

   }
//    @Override
//    protected void onNewIntent(Intent intent) {
//        setIntent(intent);
//        handleIntent(intent);
//    }
//
//    private void handleIntent(Intent intent) {
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//          String query = intent.getStringExtra(SearchManager.QUERY);
//          Log.e("restonet",query);
//          restos = databaseConnector.rechercheResto(query);
//  	    restos.moveToFirst();
//  	  etablissements.clear();
//  	    int i = restos.getColumnIndex("etablissement");
//          while (restos.isAfterLast() == false) {
//          	
//          	Log.e ("Resto", "Résultat= "+restos.getString(i));
//         etablissements.add(	restos.getString(i) );
//         restos.moveToNext();
//          }
//         
//        }
//    }

//	
	public void onItemSelected(int s, long rowId) {
	
		afficheDetailFragment(rowId, false);
	
	}
	
	public void afficheDetailFragment (long rowId, Boolean changeTab){
		Cursor details;
		String etabText, prop;
	

		 databaseConnector.open();
		 details = databaseConnector.lireDetails(rowId);
		 details.moveToFirst();
			int etabIndex = details.getColumnIndex("etablissement");
			int propIndex = details.getColumnIndex("proprietaire");

			
			etabText = details.getString(etabIndex);
			prop = details.getString(propIndex);
		 	
      	Log.e ("Resto", "etab= "+etabText+" prop= "+prop);

//		 row_id = rowId;
//		
//	
//		//detecter si fragment Detailfragment se trouve dans cette activité		
	DetailFragment  frg = 
				(DetailFragment)getFragmentManager().findFragmentById(R.id.detailFragment);
		if(frg ==null || !frg.isInLayout()){//pas de fragment DetailFragment ici
			if (changeTab) {
	//			frg.effacerFragment();
			} else {
			Intent intention = new Intent(getApplicationContext(), DetailActivity.class);
			intention.putExtra("etab", etabText);
			intention.putExtra("prop", prop);
////			intention.putExtra("cote", cote);
////			intention.putExtra("rowId", rowId);
////
			startActivity(intention);
		}
		}else{//fragment est dans cette activité
////			frg.afficheResto(nomResto);
////			frg.afficheNote(noteText);
////			frg.afficheCote(cote);
////			
		}					
//		databaseConnector.close();
	}	
	
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
    
    public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.itemMAJ:
			   if (haveInternet(this)){
			  dialog.show();
		      GetCityData task = new GetCityData();
				task.execute(new String[] { "" });
			
			   } else {
				   Toast toast = Toast.makeText(getApplicationContext(), "Veuillez activer un accès à internet", Toast.LENGTH_LONG);
				   toast.show();
			   }		return true;
			   
		case R.id.itemRECH:
            	onSearchRequested(); 
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
			getData();
			  dialog.setMax(entries.size());
			  

			    databaseConnector.open();
			    databaseConnector.clear();
			  	
	            int i=0;
		    	for (Entry msg : entries){
		    		databaseConnector.ajoutResto(msg);
		    	i++;
		    		 
		    		 publishProgress(i);	    		

		    	}
		    	databaseConnector.close();
 		
			return "";
		}
		 @Override
	        protected void onProgressUpdate(Integer...progress) {

	        	dialog.setProgress(progress[0]);
		        
		 }
		@Override
		protected void onPostExecute(String result) {

	    dialog.dismiss();

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
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
        ListeFragment listeFrg = (ListeFragment)getFragmentManager().findFragmentById(R.id.listeFragment);
        int position = tab.getPosition();
        switch (position) {
    	case 0:
    	
    		 listeFrg.afficheList(RESTO_RECENT_LOADER);
    			break;
    	case 1: 
    		 listeFrg.afficheList(RESTO_ALPHA_LOADER);
    		 break;
    	case 2:
    		 listeFrg.afficheList(RESTO_HIGH_LOADER);
    		 break;
        }
       
		
		
	}
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

    
 }
    
