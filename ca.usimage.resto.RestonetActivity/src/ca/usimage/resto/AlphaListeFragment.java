package ca.usimage.resto;




import android.app.ActionBar;
import android.app.Activity;
import android.app.ListFragment;


import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.app.LoaderManager;


import android.os.Bundle;
import android.util.Log;
import android.view.View;


import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.ListView;



public class AlphaListeFragment extends ListeFragment  {


	
	private static final int RESTO_RECENT_LOADER = 0x01;
	private static final int RESTO_ALPHA_LOADER = 0x02;
	private static final int RESTO_HIGH_LOADER = 0x03;
	private static final int RESTO_SEARCH_LOADER = 0x04;


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
		  String[] projection = { RestoDatabase.ID, RestoDatabase.COL_ETAB, RestoDatabase.COL_MONTANT };

	
	    	    switch (id){
		    	case RESTO_RECENT_LOADER:
		    		Log.e("onCreateLoader", "recent loader");
	        	    return new CursorLoader(getActivity(),
		    	            RestoProvider.CONTENT_URI, projection, null, null, "date_infraction DESC");
		    		
		    	case RESTO_ALPHA_LOADER:
		    		Log.e("onCreateLoader", "alpha loader");
		    		return new CursorLoader(getActivity(),
		    	            RestoProvider.CONTENT_URI_GROUPBY, projection, null, null,"etablissement ASC");
		    		    
		    	case RESTO_HIGH_LOADER:
		    		Log.e("onCreateLoader", "high loader");
		    		return new CursorLoader(getActivity(),
		    	            RestoProvider.CONTENT_URI, projection, null, null, "montant DESC");
		    		
		    	case RESTO_SEARCH_LOADER:
		    		return new CursorLoader(getActivity(),
		    	            RestoProvider.CONTENT_URI, projection, "etablissement like \"%" + args.getString("search_query") + "%\"", null, "etablissement ASC");	
		    		
		    		
		    		//  select etablissement, adresse, count(*)  from resto group by etablissement, adresse order by count(*) desc;
		    	default: return null;
		//  select etablissement, adresse, count(*)  from resto group by etablissement, adresse order by count(*) desc;
	}

	}
	

	
	
}
