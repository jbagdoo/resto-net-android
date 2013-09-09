package ca.usimage.resto;




import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.app.LoaderManager;


import android.os.Bundle;
import android.util.Log;



public class RechercheListeFragment extends ListeFragment  {

	private String query;


	private static final int RESTO_SEARCH_LOADER = 0x04;

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		Log.e ("Resto", "RechercheListeFragment Created= ");
		 this.getListView().setItemsCanFocus(false);
		//		  setRetainInstance(true);
		  Bundle arguments = new Bundle();
		   
		    arguments = this.getArguments();
		 query = arguments.getString("query");

	}

	
    @Override
public void onResume()
{
    super.onResume();
    // call initLoader on Resume avoids a bug which calls onLoadFinished twice
   
    LoaderManager lm = getLoaderManager();
	Bundle mBundle = new Bundle();
	mBundle.putString("search_query", query);
    lm.initLoader(RESTO_SEARCH_LOADER, mBundle, this);
    Log.e("RechercheListFragment","onResume");
}
	
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		  String[] projection = { RestoDatabase.ID, RestoDatabase.COL_ETAB, RestoDatabase.COL_DATE_JUGE, RestoDatabase.COL_MONTANT };
          String adresse_clause = "";
                // adrsesse_clause is only added for plusFragment
			    if (null != args.get("adresse")) {
			    	adresse_clause = " and adresse = \"" + args.get("adresse") + "\"";
			    }
	
	    	    switch (id){
		 
		    		
		    	case RESTO_SEARCH_LOADER:
		    		Log.e("onCreateLoader", "search loader");
		    		return new CursorLoader(getActivity(),
		    	            RestoProvider.CONTENT_URI, projection, "etablissement like \"%" + args.getString("search_query") + "%\"" + adresse_clause, null, "etablissement ASC");	
		    				    	
		    	default: return null;

	}

	}
	


	
	
}
