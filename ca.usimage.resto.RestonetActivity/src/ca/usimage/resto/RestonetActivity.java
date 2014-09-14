package ca.usimage.resto;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



public class RestonetActivity extends Activity implements ListItemSelectListener, ListItemMapListener, OnInfoWindowClickListener, ActionBar.TabListener{
	
	 HashMap<String, Integer> extraMarkerInfo = new HashMap<String, Integer>();
    private boolean useLogo = false;
 
	private int tab_pos;
	private String query = "";
			
    RecentFragment listeFrg = new RecentFragment();
    AlphaListeFragment alphaFrg = new AlphaListeFragment();
    HighListeFragment highFrg = new HighListeFragment();
    RechercheListeFragment rechFrg = new RechercheListeFragment();
    PlusListeFragment plusFrg = new PlusListeFragment();
    MapFragment mapFrg = new CarteFragment();


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

		  ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
				  
  	 	if (savedInstanceState != null){
 		
   		 tab_pos = savedInstanceState.getInt("tabState");

   	     ab.addTab(ab.newTab().setText(R.string.tab_recente).setTabListener(this),0,false);
         ab.addTab(ab.newTab().setText(R.string.tab_fortes).setTabListener(this),1,false);
         ab.addTab(ab.newTab().setText(R.string.tab_plus).setTabListener(this),2,false);
         ab.addTab(ab.newTab().setText(R.string.tab_alpha).setTabListener(this),3,false);

         ab.setSelectedNavigationItem(tab_pos);

      
   	
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
    	  FragmentManager fragmentManager = getFragmentManager();
          FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
  	  
//	  	     if (null == getFragmentManager().findFragmentByTag("RECH")) {
	  	      Bundle arguments = new Bundle();
	   	    arguments.putString("searchQuery", query);
	   	  
	   		if(null == fragmentManager.findFragmentById(R.id.rechFragment)|| !rechFrg.isInLayout()){//pas de fragment RechFragment ici

				Intent intention = new Intent(getApplicationContext(), RechActivity.class);
				
				intention.putExtra("query", query);
				startActivity(intention);
			}
			else{//fragment est dans cette activité
				 
				 fragmentTransaction.add(R.id.rechFragment, rechFrg, "RECH");
				 fragmentTransaction.commit();

			}	
	   	    
        }
        
    }


	public void onItemSelected(int s, long rowId) {
		  Cursor c;
		  FragmentManager fragmentManager = getFragmentManager();
	 	   PlusListeFragment plusfrg = (PlusListeFragment)
	    			   fragmentManager.findFragmentByTag("PLUS");
		//  only for plusFragment, we want to show all items with equal name and adresse and call the search fragment
		// otherwise just show detail fragment using rowid  
		 if ((null != fragmentManager.findFragmentByTag("PLUS"))) {
		
			    c = plusfrg.adapter.getCursor();
			    c.moveToPosition(s);
			  
			    String addr = c.getString(c.getColumnIndex("adresse"));
			    String nom = c.getString(c.getColumnIndex("etablissement"));
				Intent intention = new Intent(getApplicationContext(), RechActivity.class);
				
				intention.putExtra("query", nom);
				intention.putExtra("adresse", addr);
			
				startActivity(intention);
	          
	     }else {
        		afficheDetailFragment(rowId, false);
	           }
	}
	
	
	public  void afficheDetailFragment (long rowId, Boolean changeTab){

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
			 fragmentTransaction.commit();

	
		}					
	 
	}	
	
	public void afficheCarteFragment (long rowId){
		
    int statusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
	if (isMyServiceRunning()) {
		// don't allow map to be shown while MAJ is running
		
		   Toast toast = Toast.makeText(getApplicationContext(), R.string.no_map, Toast.LENGTH_LONG);
		   toast.show();
	   }	else
	   {    
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
			  
				   showDialog();
				   return true;

		case R.id.itemRECH:
            	onSearchRequested(); 
            
                return true;
                
		case R.id.itemMAP:
			afficheCarteFragment(99999);
			 return true;
			 
		case R.id.itemABOUT:
			showAbout();
			 return true;
		
	case R.id.itemQC:
		if (!isPackageInstalled("ca.usimage.restoqc", this)){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id=ca.usimage.restoqc"));
		startActivity(intent);
		}else
		{
			//if (isProcessRunning("ca.usimage.resto")) {
				
				Intent i = new Intent();
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.setAction("android.intent.action.VIEW");
				i.setComponent(ComponentName.unflattenFromString("ca.usimage.restoqc/ca.usimage.restoqc.RestonetActivity"));
				startActivity(i);

		}
	
		 return true;
	}

		return false;
	}


private boolean isPackageInstalled(String packagename, Context context) {
    PackageManager pm = context.getPackageManager();
    try {
        pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
        return true;
    } catch (NameNotFoundException e) {
        return false;
    }
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

            return false;
        }
        return true;
    }
    
   

   	
//    
   	
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	     int position = tab.getPosition();
		  FragmentManager fragmentManager = getFragmentManager();
	
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
 
      switch (position) {
  	case 0:
  	 
  	  
  	     if (null == fragmentManager.findFragmentByTag("RECENT")) {
           ft.replace(R.id.listeFragment, listeFrg, "RECENT");
         
          }

  			break;

  	case 1:
  		 
  	     if (null == fragmentManager.findFragmentByTag("HIGH") ) {
	           ft.replace(R.id.listeFragment, highFrg, "HIGH");
	        
	     }
  		 break;
 	case 2:
 		 
	     if ((null == fragmentManager.findFragmentByTag("PLUS"))) {
	           ft.replace(R.id.listeFragment, plusFrg,"PLUS");    
	     }

 			break;
  	case 3: 
  
	     if (null == fragmentManager.findFragmentByTag("ALPHA") ) {
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
             // save selected tab before orientation changes
	    	 outState.putInt("tabState", getActionBar().getSelectedTab().getPosition());

	}
    
	void showDialog() {
	    DialogFragment dataDialog = GetDataDialog.newInstance(
	            R.string.dialog);
	    dataDialog.show(getFragmentManager(), "dialog");
	}
	
	private boolean isMyServiceRunning() {
		
	  
		
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (MAJ.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}

	public void doPositiveClick() {
	
		// first check if internet connection is available
		
		if (haveInternet(this)){
		 
			if (!isMyServiceRunning()) {
			 
//			 mDB = new RestoDatabase(getBaseContext());
//			 
//			 sqlDB = mDB.getWritableDatabase();
//// before downloading city data, delete the existing sqlite database by forcing a call to onUpgrade by incrementing
//// the database's version
//			 
//			 int version = sqlDB.getVersion();
//			 mDB.onUpgrade(sqlDB, version, version+1);
			 startService(new Intent(this, MAJ.class));

//		      GetCityData task = new GetCityData();
//					task.execute(new String[] { "" });
			} else
			{
				   Toast toast = Toast.makeText(getApplicationContext(), R.string.maj_deja, Toast.LENGTH_LONG);
				   toast.show();
			   }		
				
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


	 protected void showAbout() {
	        // Inflate the about message contents
	        View messageView = getLayoutInflater().inflate(R.layout.about, null, false);
	 
	        // When linking text, force to always use default color. This works
	        // around a pressed color state bug.
	        TextView textView = (TextView) messageView.findViewById(R.id.about_credits);
	        int defaultColor = textView.getTextColors().getDefaultColor();
	        textView.setTextColor(defaultColor);
	 
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setIcon(R.drawable.restonet4);
	        builder.setTitle(R.string.app_name);
	        builder.setView(messageView);
	        builder.create();
	        builder.show();
	    }
	

 }
    
