package ca.usimage.resto;




import android.app.Activity;
import android.app.ListFragment;


import android.content.Context;
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



public class ListeFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>  {
	private ListItemSelectListener listeSelectListener;
	private ListItemMapListener listeMapListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	

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



	
	public MyCursorAdapter adapter;  // extends simplecursoradaptor


	
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
	     
	        final Cursor c = cursor;
	        int pos;
	        
	        pos = c.getPosition();
	        final Long ID =  getItemId(pos);
	        
            ImageButton mapButton = (ImageButton)view.findViewById(R.id.ImageButton01);
	        mapButton.setOnClickListener(new View.OnClickListener() {

	            @Override
	            public void onClick(View arg0) {
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


		
	    String[] uiBindFrom = { RestoDatabase.COL_ETAB, RestoDatabase.COL_MONTANT, RestoDatabase.COL_DATE_JUGE };
	    int[] uiBindTo = { R.id.TextView01, R.id.Montant, R.id.Date};
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
  
  
}
	
	
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
      return null;
	    	
	    
	   
	
	}


	
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
	    adapter.swapCursor(cursor);
	}

	
	public void onLoaderReset(Loader<Cursor> loader) {
	    adapter.swapCursor(null);
	}

	

	
	
}
