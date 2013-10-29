package ca.usimage.resto;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class RechActivity extends Activity  implements ListItemSelectListener, ListItemMapListener {
	
	

	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.recherche);
		// capture intent
				Intent intentRecu = getIntent();
				String query = intentRecu.getStringExtra("query");
				String addr = intentRecu.getStringExtra("adresse");

				  FragmentManager fragmentManager = getFragmentManager();
		          FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		          //add a fragment
		         RechercheListeFragment rechFrg = new RechercheListeFragment();
		         Bundle arguments = new Bundle();
	        	    arguments.putString("query", query);
	        	    arguments.putString("adresse", addr);
	        	  
	        	    rechFrg.setArguments(arguments);
		         // fragment must be tagged to prevent fragment leakage
		         if (null == fragmentManager.findFragmentByTag("RECH")) {
		        	 Log.e("RechActivity", "adding fragment");
		          fragmentTransaction.add(R.id.rechFragment, rechFrg, "RECH");
		          fragmentTransaction.commit();

		         }
	}

	public void afficheDetailFragment (long rowId, Boolean changeTab){
		
		//	
//				//detecter si fragment Detailfragment se trouve dans cette activité		
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
//					 fragmentTransaction.addToBackStack(null);
					 fragmentTransaction.commit();

			
				}					

			}	
			
	
	
	@Override
	public void onItemSelected(int sItem, long rowId) {
		
		afficheDetailFragment(rowId, false);
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
	@Override
	public void onItemMapSelected(long rowId) {

		
		afficheCarteFragment(rowId);
		
	}

}
