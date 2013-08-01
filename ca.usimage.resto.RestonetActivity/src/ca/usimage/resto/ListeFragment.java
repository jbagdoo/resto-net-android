package ca.usimage.resto;




import android.app.Activity;
import android.app.ListFragment;


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



public class ListeFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>  {
	private ListItemSelectListener listeSelectListener;
	private ListItemMapListener listeMapListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.e ("onAttach", "ListeFragment Attached ");

		try {
			listeSelectListener = (ListItemSelectListener) activity;
		
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " doit implementer ListItemSelectListener");
		}
		try {
			listeMapListener = (ListItemMapListener) activity;
		
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " doit implementer ListMapListener");
		}
	}


	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Log.e ("Resto", "pos="+position+" Rowid= "+id);
		listeSelectListener.onItemSelected(position, id);
   	
	}
	
	private MyCursorAdapter adapter;  // extends simplecursoradaptor

	
	private static final int RESTO_RECENT_LOADER = 0x01;
	private static final int RESTO_ALPHA_LOADER = 0x02;
	private static final int RESTO_HIGH_LOADER = 0x03;
	private static final int RESTO_SEARCH_LOADER = 0x04;

	
	
	
	public void afficheList(int loader_id, String query) {
		Log.e ("afficheList", "loader_id= "+loader_id);
		Bundle mBundle = new Bundle();
		mBundle.putString("search_query", query);
		getLoaderManager().restartLoader(loader_id, mBundle, this);
	}
	
	private class OnItemClickListener implements OnClickListener{       
	    private int mPosition;
	    private long mId;
	    OnItemClickListener(int position, long id){
	        mPosition = position;
	        mId = id;
	    }
	    @Override
	    public void onClick(View arg0) {
	    	// this links the click event to the main activity via the interface  ListItemSelectListener
	    	// to call the details fragment
	    	listeSelectListener.onItemSelected(mPosition, mId);
       
	    }       
	}
	
	
	public class MyCursorAdapter extends SimpleCursorAdapter implements OnClickListener{
	
		public MyCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flag) {
	        super(context, layout, c, from, to, flag);
	    }
 
	    @Override
	    public void bindView(View view, Context context, Cursor cursor) {
	        super.bindView(view, context, cursor);
	        final Context t = context;
	        final Cursor c = cursor;
	        int pos;
	        
	        pos = c.getPosition();
	        final Long ID =  getItemId(pos);
	        
            ImageButton mapButton = (ImageButton)view.findViewById(R.id.ImageButton01);
	        mapButton.setOnClickListener(new View.OnClickListener() {
	        	 private String nom = c.getString(c.getColumnIndex(RestoDatabase.COL_ETAB));

	            @Override
	            public void onClick(View arg0) {

	               Toast.makeText(t,
	                "Resto Name: " + nom, Toast.LENGTH_SHORT).show();
	               listeMapListener.onItemMapSelected(ID);

	            }
	        });
	        
	        // this allows a list item to be clickable, separately from the map button, to call up the details fragment

	        view.setOnClickListener(new OnItemClickListener( pos, ID));

	    }
   
		@Override
		public void onClick(View v) {
			
		}   
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Log.e ("ListeFragment", "ListeFragment Created= ");
//		  setRetainInstance(true);
			
		

		
	
		
		
		
		
	    String[] uiBindFrom = { RestoDatabase.COL_ETAB, RestoDatabase.COL_MONTANT };
	    int[] uiBindTo = { R.id.TextView01, R.id.Montant };
	    adapter = new MyCursorAdapter(
	            getActivity(), R.layout.row,
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

    lm.initLoader(RESTO_RECENT_LOADER, null, this);
    Log.e("ListFragment","onResume");
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
	    	
	    
	    }
	
	}


	
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
	    adapter.swapCursor(cursor);
	}

	
	public void onLoaderReset(Loader<Cursor> loader) {
	    adapter.swapCursor(null);
	}

	

	
	
}
