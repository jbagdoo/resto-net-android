package ca.usimage.resto;




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



public class PlusListeFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>  {
	private ListItemSelectListener listeSelectListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.e ("onAttach", "PlusFragment Attached ");

		try {
			listeSelectListener = (ListItemSelectListener) activity;
		
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " doit implementer ListItemSelectListener");
		}
	}


	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	
		listeSelectListener.onItemSelected(position, id);
   	
	}

	
	private static final int RESTO_RECENT_LOADER = 0x01;
	private static final int RESTO_ALPHA_LOADER = 0x02;
	private static final int RESTO_HIGH_LOADER = 0x03;
	private static final int RESTO_SEARCH_LOADER = 0x04;
	private  static final int RESTO_PLUS_LOADER = 0x05;

	
	private SimpleCursorAdapter adapter;
	
	public void afficheList(int loader_id, String query) {
		Log.e ("afficheList", "loader_id= "+loader_id);
		Bundle mBundle = new Bundle();
		mBundle.putString("search_query", query);
		getLoaderManager().restartLoader(loader_id, mBundle, this);
	}
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Log.e ("OnCreate", "PlusFragment Created ");

		 

	    String[] uiBindFrom = { RestoDatabase.COL_ETAB,  RestoDatabase.COL_ADR, RestoDatabase.COL_COUNT };
	    int[] uiBindTo = { R.id.TextView01, R.id.Adresse, R.id.Count };
	    adapter = new SimpleCursorAdapter(
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


	
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		Log.e("recent", "cursor about to be swapped");
	    adapter.swapCursor(cursor);
	    // position cursor at top of list
//	     this.setSelection(0);
	}

	
	public void onLoaderReset(Loader<Cursor> loader) {
	    adapter.swapCursor(null);
	}

	
	
}
