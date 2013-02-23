package ca.usimage.resto;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class DetailActivity extends Activity{
	public long row_id;
	
	
//	   DatabaseConnector databaseConnector = 
//	            new DatabaseConnector(DetailActivity.this); 
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.detail);
		// capture intent
				Intent intentRecu = getIntent();
				String etab = intentRecu.getStringExtra("etab");
			String prop = intentRecu.getStringExtra("prop");

				
		//Obtenir le fragment
				DetailFragment frg = 
						(DetailFragment)getFragmentManager().findFragmentById(R.id.detailFragment);
				
		        frg.afficheEtab(etab);	
				frg.afficheProp(prop);
//				frg.afficheCote(cote);
	


		
	}
}
