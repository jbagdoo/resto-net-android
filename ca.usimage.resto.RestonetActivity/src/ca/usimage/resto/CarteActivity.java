package ca.usimage.resto;


import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

public class CarteActivity extends Activity  implements OnInfoWindowClickListener, OnMarkerClickListener{
	public long rowId;
	
	
		
    CarteFragment carteFrg = new CarteFragment();
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.carte);
		  FragmentManager fragmentManager = getFragmentManager();
          FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	  
		// capture intent
				Intent intentRecu = getIntent();
				
				 rowId = intentRecu.getLongExtra("rowid", 0);
				
	     

		          //add a fragment
		
		         Bundle arguments = new Bundle();
	        	    arguments.putLong("rowid", rowId);
	        	  
	        	    carteFrg.setArguments(arguments);
		         // fragment must be tagged to prevent fragment leakage
	          if (null == fragmentManager.findFragmentByTag("DETAIL")) {
	        	   if (null == fragmentManager.findFragmentByTag("Carte")) {	
		          fragmentTransaction.add(R.id.carteFragment, carteFrg, "Carte");
		         
		          fragmentTransaction.commit();
	        	   }
		         
	         } else {
	        	 fragmentTransaction.hide(fragmentManager.findFragmentByTag("Carte"));
	        	  fragmentTransaction.commit();
	         }
	}

	
	public void afficheDetailFragment (long rowId, Boolean changeTab){

	//	
//			//detecter si fragment Detailfragment se trouve dans cette activité		
			  FragmentManager fragmentManager = getFragmentManager();
	          FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	
	          DetailFragment detailFrg = new DetailFragment();
	         
	        	   
	        	    Bundle arguments = new Bundle();
	        	    arguments.putLong("rowid", rowId);
	        	  
	        	    detailFrg.setArguments(arguments);

	        	        fragmentTransaction.hide(fragmentManager.findFragmentByTag("Carte"));

	        	  
		         // fragment must be tagged to prevent fragment leakage
	        	    if (null == fragmentManager.findFragmentByTag("DETAIL")) {

		
		          fragmentTransaction.add(R.id.carteFragment, detailFrg, "DETAIL");
		          fragmentTransaction.addToBackStack(null);
		          fragmentTransaction.commit();

		         }
				

		}	
		



	@Override
	public void onInfoWindowClick(Marker arg0) {
	
		// when marker infowindow is clicked, show resto details
		rowId = Integer.parseInt(arg0.getSnippet());
		afficheDetailFragment(rowId, false);
		
		
	}


	@Override
	public boolean onMarkerClick(Marker arg0) {
		// update the current rowid if another marker is clicked
		rowId = Integer.parseInt(arg0.getSnippet());
		 Bundle arguments = new Bundle();
 	     arguments = carteFrg.getArguments();
 	     arguments.putLong("rowid", rowId);   	  

		return false;
	}

}
