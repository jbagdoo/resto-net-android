package ca.usimage.resto;




import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.app.LoaderManager;


import android.os.Bundle;
import android.util.Log;



public class HighListeFragment extends ListeFragment  {


	private static final int RESTO_HIGH_LOADER = 0x03;


    @Override
public void onResume()
{
    super.onResume();
    // call initLoader on Resume avoids a bug which calls onLoadFinished twice
   
    LoaderManager lm = getLoaderManager();

    lm.initLoader(RESTO_HIGH_LOADER, null, this);
  
}
	
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		  String[] projection = { RestoDatabase.ID, RestoDatabase.COL_PROPRIO,RestoDatabase.COL_ETAB,RestoDatabase.COL_ADR,RestoDatabase.COL_DATE_JUGE, RestoDatabase.COL_MONTANT };

	
	    	    switch (id){
		
		    		
		    	case RESTO_HIGH_LOADER:
		    		return new CursorLoader(getActivity(),
		    	            RestoProvider.CONTENT_URI_GROUPBY, projection, null, null,"montant DESC");
//		    		sqlite> select etablissement, adresse, sum(montant)  from resto group by etablissement, adresse order by sum(montant) desc;
		    	default: return null;
		
	}

	}
	

	
	
}
