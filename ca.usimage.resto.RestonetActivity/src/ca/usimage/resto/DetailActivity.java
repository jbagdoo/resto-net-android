package ca.usimage.resto;


import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;

public class DetailActivity extends Activity   {
	public long row_id;
	
	

	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.detail);
		// capture intent
				Intent intentRecu = getIntent();
				Long rowId = intentRecu.getLongExtra("rowid", 0);

				
		//Obtenir le fragment
				DetailFragment frg = 
						(DetailFragment)getFragmentManager().findFragmentById(R.id.detailFragment);
				 frg.afficheDetails(rowId);	

	}

}
