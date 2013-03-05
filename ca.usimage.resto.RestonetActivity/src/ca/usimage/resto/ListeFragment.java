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



public class ListeFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>  {
	private ListItemSelectListener listeSelectListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listeSelectListener = (ListItemSelectListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " doit implementer ListItemSelectListener");
		}
	}

//	public void onSaveInstanceState (Bundle outState) { 
//		super.onSaveInstanceState(outState); 
//		int scroll = this.getSelectedItemPosition(); 
//		outState.putInt("POS", scroll); } 
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Log.e ("Resto", "pos="+position+" Rowid= "+id);
		listeSelectListener.onItemSelected(position, id);
   	
	}

	
	private static final int RESTO_RECENT_LOADER = 0x01;
	private static final int RESTO_ALPHA_LOADER = 0x02;
	private static final int RESTO_HIGH_LOADER = 0x03;
	private static final int RESTO_SEARCH_LOADER = 0x04;
//	 private static final String AUTHORITY = "ca.usimage.resto.RestoProvider";
//	    private static final String RESTOS_BASE_PATH = "restos";
	
	
	private SimpleCursorAdapter adapter;
	
	public void afficheList(int loader_id, String query) {
		Log.e ("Resto", "loader_id= "+loader_id);
		Bundle mBundle = new Bundle();
		mBundle.putString("search_query", query);
		getLoaderManager().restartLoader(loader_id, mBundle, this);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		  setRetainInstance(true);
		if (savedInstanceState == null) {
	    String[] uiBindFrom = { RestoDatabase.COL_ETAB, RestoDatabase.COL_MONTANT };
	    int[] uiBindTo = { R.id.TextView01, R.id.Montant };
	    // default loader on startup is RECENT_LOADER
	    LoaderManager lm = getLoaderManager();
//        if (lm.getLoader(RESTO_RECENT_LOADER) != null) {
//            lm.initLoader(RESTO_RECENT_LOADER, null, this);
//        }

	    if (adapter == null) {
	    adapter = new SimpleCursorAdapter(
	            getActivity().getApplicationContext(), R.layout.row,
	            null, uiBindFrom, uiBindTo,
	            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	    setListAdapter(adapter);
	    }
	  //  this.setSelection(savedInstanceState.getInt("POS"));
		}
	}
	
	
	
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		  String[] projection = { RestoDatabase.ID, RestoDatabase.COL_ETAB, RestoDatabase.COL_MONTANT };
	    switch (id){
	    	case RESTO_RECENT_LOADER:
        	    return new CursorLoader(getActivity(),
	    	            RestoProvider.CONTENT_URI, projection, null, null, "date_infraction DESC");
	    		
	    	case RESTO_ALPHA_LOADER:
	    		return new CursorLoader(getActivity(),
	    	            RestoProvider.CONTENT_URI_GROUPBY, projection, null, null,"etablissement ASC");
	    		    
	    	case RESTO_HIGH_LOADER:
	    		return new CursorLoader(getActivity(),
	    	            RestoProvider.CONTENT_URI, projection, null, null, "montant DESC");
	    		
	    	case RESTO_SEARCH_LOADER:
	    		return new CursorLoader(getActivity(),
	    	            RestoProvider.CONTENT_URI, projection, "etablissement like \"%" + args.getString("search_query") + "%\"", null, "etablissement ASC");	
	    		
	    	default: return null;
	    	
	    
	    }
	
	}


	
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
	    adapter.swapCursor(cursor);
	    // position cursor at top of list
//	    this.setSelection(0);
	}

	
	public void onLoaderReset(Loader<Cursor> loader) {
	    adapter.swapCursor(null);
	}

	
	
}
