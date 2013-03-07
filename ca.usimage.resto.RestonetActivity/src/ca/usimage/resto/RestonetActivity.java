package ca.usimage.resto;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;





public class RestonetActivity extends Activity implements ListItemSelectListener, ActionBar.TabListener{
	
	
    private boolean useLogo = false;
    private boolean showHomeUp = true;
    private static final int RESTO_RECENT_LOADER = 0x01;
	private static final int RESTO_ALPHA_LOADER = 0x02;
	private static final int RESTO_HIGH_LOADER = 0x03;
	private static final int RESTO_SEARCH_LOADER = 0x04;
	private int tab_pos;
	

	private List<Entry> entries;
	 ArrayList<String> etablissements = new ArrayList<String>();
	 ProgressDialog dialog;
	
//	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  ;
    	setContentView(R.layout.liste);
    	 final ActionBar ab = getActionBar();
   
     
      // set defaults for logo & home up
//      ab.setDisplayHomeAsUpEnabled(showHomeUp);
     ab.setDisplayUseLogoEnabled(useLogo);

		
		 ab.setDisplayShowHomeEnabled(true);
      // set up tabs nav
		 
	
	   
        
         
     
   	
  	 	if (savedInstanceState != null){
   		 tab_pos = savedInstanceState.getInt("tabState");
   		 ab.addTab(ab.newTab().setText(R.string.tab_recente).setTabListener(this),0,false);
         ab.addTab(ab.newTab().setText(R.string.tab_alpha).setTabListener(this),1,false);
         ab.addTab(ab.newTab().setText(R.string.tab_fortes).setTabListener(this),2,false);
   	
   		
   		 
   	} else { ab.addTab(ab.newTab().setText(R.string.tab_recente).setTabListener(this),0,true);
    ab.addTab(ab.newTab().setText(R.string.tab_alpha).setTabListener(this),1,false);
    ab.addTab(ab.newTab().setText(R.string.tab_fortes).setTabListener(this),2,false);
   	}
  	
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ab.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        ab.setSelectedNavigationItem(tab_pos);
 	
	  	dialog = new ProgressDialog(RestonetActivity.this);
        dialog.setCancelable(true);
        dialog.setMessage("Mise � jour des donn�es en cours...");
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
          String query = intent.getStringExtra(SearchManager.QUERY);
          Log.e("restonet",query);
 		 ListeFragment listeFrg = (ListeFragment)getFragmentManager().findFragmentById(R.id.listeFragment);
			 listeFrg.afficheList(RESTO_SEARCH_LOADER, query);
        }
    }


	public void onItemSelected(int s, long rowId) {
	
		afficheDetailFragment(rowId, false);
	
	}
	
	public void afficheDetailFragment (long rowId, Boolean changeTab){

//	
//		//detecter si fragment Detailfragment se trouve dans cette activit�		
	DetailFragment  frg = 
				(DetailFragment)getFragmentManager().findFragmentById(R.id.detailFragment);
		if(frg ==null || !frg.isInLayout()){//pas de fragment DetailFragment ici
			if (changeTab) {
	//			frg.effacerFragment();
			} else {
			Intent intention = new Intent(getApplicationContext(), DetailActivity.class);
			intention.putExtra("rowid", rowId);
			startActivity(intention);
		}
		}else{//fragment est dans cette activit�
			frg.afficheDetails(rowId);
	
		}					

	}	
	
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	 final ActionBar ab = getActionBar();
		switch (item.getItemId()) {
		case R.id.itemMAJ:
			  
				   showDialog();
				   return true;

		case R.id.itemRECH:
            	onSearchRequested(); 
            	//force alpha order tab to be selected after a search
            	ab.setSelectedNavigationItem(1);
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
			  getContentResolver().delete(RestoProvider.CONTENT_URI, null, null);
			  
			    ContentValues ajout_resto = new ContentValues();

	            int i=0;
		    	for (Entry msg : entries){
	    	      	
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
		
		  // position cursor at top of list if user retaps a tab
		 ListeFragment listeFrg = (ListeFragment)getFragmentManager().findFragmentById(R.id.listeFragment);
		 listeFrg.setSelection(0);	
		
	}
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
        ListeFragment listeFrg = (ListeFragment)getFragmentManager().findFragmentById(R.id.listeFragment);
        
        int position = tab.getPosition();
        Log.e ("resto", "pos= "+position);
        switch (position) {
    	case 0:
    	
    		 listeFrg.afficheList(RESTO_RECENT_LOADER, null);
    			break;
    	case 1: 
    		 listeFrg.afficheList(RESTO_ALPHA_LOADER, null);
    		 break;
    	case 2:
    		 listeFrg.afficheList(RESTO_HIGH_LOADER, null);
    		 break;
        }
       
		
		
	}
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    outState.putInt("tabState", getActionBar().getSelectedTab().getPosition());
	}
    
	void showDialog() {
	    DialogFragment dataDialog = GetDataDialog.newInstance(
	            R.string.dialog);
	    dataDialog.show(getFragmentManager(), "dialog");
	}

	public void doPositiveClick() {
		 if (haveInternet(this)){
			 dialog.show();
		      GetCityData task = new GetCityData();
					task.execute(new String[] { "" });
				
				   } else {
					   Toast toast = Toast.makeText(getApplicationContext(), "Veuillez activer un acc�s � internet", Toast.LENGTH_LONG);
					   toast.show();
				   }		
	    Log.i("FragmentAlertDialog", "Positive click!");
	}

	public void doNegativeClick() {
	    // Do stuff here.
	    Log.i("FragmentAlertDialog", "Negative click!");
	}
	
	
 }
    
