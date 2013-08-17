package ca.usimage.resto;




import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.app.LoaderManager;
import android.view.View.OnClickListener;


import android.os.Bundle;
import android.util.Log;
import android.view.View;


import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.ListView;
import android.widget.Toast;



public class RecentFragment extends ListeFragment   {

	
	private static final int RESTO_RECENT_LOADER = 0x01;

	
    @Override
public void onResume()
{
    super.onResume();
    // call initLoader on Resume avoids a bug which calls onLoadFinished twice
  
    LoaderManager lm = getLoaderManager();

    lm.initLoader(RESTO_RECENT_LOADER, null, this);
   
}
	
	
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		  String[] projection = { RestoDatabase.ID, RestoDatabase.COL_ETAB, RestoDatabase.COL_DATE_JUGE, RestoDatabase.COL_MONTANT };
	    switch (id){
	    	case RESTO_RECENT_LOADER:
	    		Log.e("onCreateLoader", "recent loader");
        	    return new CursorLoader(getActivity(),
	    	            RestoProvider.CONTENT_URI, projection, null, null, "date_jugement DESC");
	default: return null;
	    	
	    
	    }
	
	}



	
	
}
