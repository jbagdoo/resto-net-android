package ca.usimage.resto;




import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.app.LoaderManager;


import android.os.Bundle;
import android.util.Log;



public class AlphaListeFragment extends ListeFragment  {


	private static final int RESTO_ALPHA_LOADER = 0x02;


    @Override
public void onResume()
{
    super.onResume();
    // call initLoader on Resume avoids a bug which calls onLoadFinished twice
   
    LoaderManager lm = getLoaderManager();

    lm.initLoader(RESTO_ALPHA_LOADER, null, this);
    Log.e("AlphaListFragment","onResume");
}
	
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		  String[] projection = { RestoDatabase.ID, RestoDatabase.COL_ETAB, RestoDatabase.COL_DATE_JUGE, RestoDatabase.COL_MONTANT };

	
	    	    switch (id){
		
		    		
		    	case RESTO_ALPHA_LOADER:
		    		Log.e("onCreateLoader", "alpha loader");
		    		return new CursorLoader(getActivity(),
		    	            RestoProvider.CONTENT_URI_GROUPBY, projection, null, null,"etablissement ASC");
	
		    	default: return null;
		
	}

	}
	

	
	
}


