package ca.usimage.resto;




import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.app.LoaderManager;


import android.os.Bundle;
import android.util.Log;
import android.widget.CursorAdapter;



public class PlusListeFragment extends ListeFragment  {



	private  static final int RESTO_PLUS_LOADER = 0x05;

	
	
	
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

	    String[] uiBindFrom = { RestoDatabase.COL_ETAB,  RestoDatabase.COL_ADR, RestoDatabase.COL_COUNT };
	    int[] uiBindTo = { R.id.Etablissement, R.id.Adresse, R.id.Count };
	    adapter = new MyCursorAdapter(
	            getActivity(), R.layout.row_plus,
	            null, uiBindFrom, uiBindTo,
	            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	    setListAdapter(adapter);
	    
		}

    @Override
public void onResume()
{
    super.onResume();
    // call initLoader on Resume avoids a bug which calls onLoadFinished twice
  
    LoaderManager lm = getLoaderManager();

    lm.initLoader(RESTO_PLUS_LOADER, null, this);
    Log.e("PlusFragment","onResume");
}
	
	
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		  String[] projection = { RestoDatabase.ID, RestoDatabase.COL_ETAB, RestoDatabase.COL_MONTANT, RestoDatabase.COL_ADR, "count(*)" };
	    switch (id){
	
	    		
	    	case RESTO_PLUS_LOADER:
	    		Log.e("onCreateLoader", "plus loader");
	    		
	    		return new CursorLoader(getActivity(),
	    	            RestoProvider.CONTENT_URI_GROUPBY_PLUS, projection, null, null, "count(*) DESC");
	    		//  select etablissement, adresse, count(*)  from resto group by etablissement, adresse order by count(*) desc;
	    	default: return null;
	    	
	    
	    }
	
	}

	
	
}
