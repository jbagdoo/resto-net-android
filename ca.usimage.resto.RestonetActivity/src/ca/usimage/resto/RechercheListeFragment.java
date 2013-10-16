package ca.usimage.resto;




import ca.usimage.resto.R.id;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



public class RechercheListeFragment extends ListeFragment  {

	


	private static final int RESTO_SEARCH_LOADER = 0x04;
	private String query, addr;
	
	private ListItemSelectListener listeSelectListener;
	private ListItemMapListener listeMapListener;
	private Context context;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	
		super.onActivityCreated(savedInstanceState);
	
	
		  Bundle arguments = new Bundle();
		   
		    arguments = this.getArguments();
		 query = arguments.getString("query");
		 addr = arguments.getString("adresse");

	}

	
    @Override
public void onResume()
{
    super.onResume();
    // call initLoader on Resume avoids a bug which calls onLoadFinished twice
   
    LoaderManager lm = getLoaderManager();
	Bundle mBundle = new Bundle();
	mBundle.putString("search_query", query);
	mBundle.putString("where_addr", addr);
    lm.initLoader(RESTO_SEARCH_LOADER, mBundle, this);
   
}
	
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		  String[] projection = { RestoDatabase.ID, RestoDatabase.COL_PROPRIO,RestoDatabase.COL_ETAB, RestoDatabase.COL_DATE_JUGE, RestoDatabase.COL_MONTANT };
          String where_clause = "";
		  String adresse_clause = "";
                // adrsesse_clause is only added for plusFragment
			    if (null != args.get("where_addr")) {
			    	adresse_clause = " and adresse = \"" + args.get("where_addr") + "\"";
			    }
	
	    	    switch (id){
		 
		    		
		    	case RESTO_SEARCH_LOADER:
		    		where_clause = "etablissement like \"%" + args.getString("search_query") + "%\"" + adresse_clause;
		    		return new CursorLoader(getActivity(),
		    	            RestoProvider.CONTENT_URI, projection, where_clause, null, "etablissement ASC");	
		    				    	
		    	default: return null;

	}

	}
	
//	@Override
//	public View onCreateView(LayoutInflater inflater,
//	        ViewGroup container, Bundle savedInstanceState) {
//	
//	    ImageView image = (ImageView) container.findViewById(R.id.noresult);
//		return image;
//	    
//	}
@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
     if ( cursor.getCount() >0) {
	    adapter.swapCursor(cursor);
     } else {
        	ViewGroup container = (ViewGroup) this.getView().getParent();  
			LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    	    View v = inflater.inflate(R.layout.noresult, container, false);
    	    container.addView(v);
     }
	}
	
	
}
