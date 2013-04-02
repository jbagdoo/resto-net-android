package ca.usimage.resto;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class DetailActivity extends Activity   {
	public long row_id;
	
	

	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.detail);
		// capture intent
				Intent intentRecu = getIntent();
				Long rowId = intentRecu.getLongExtra("rowid", 0);

				  FragmentManager fragmentManager = getFragmentManager();
		          FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		          //add a fragment
		         DetailFragment detailFrg = new DetailFragment();
		         Bundle arguments = new Bundle();
	        	    arguments.putLong("rowid", rowId);
	        	  
	        	    detailFrg.setArguments(arguments);
		         // fragment must be tagged to prevent fragment leakage
		         if (null == fragmentManager.findFragmentByTag("Detail")) {
		        	 Log.e("DetailActivity", "adding fragment");
		          fragmentTransaction.add(R.id.detailFragment, detailFrg, "Detail");
		          fragmentTransaction.commit();

		         }
	}

}
