package ca.usimage.resto;

import java.util.ArrayList;
import java.util.List;



import android.app.Activity;
import android.app.ListFragment;

import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;

import android.app.LoaderManager;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import android.widget.ListView;



public class ListeFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>  {
	private ListItemSelectListener listeSelectListener;

	
//	Cursor restos;
//	private List<Entry> entries;
//	 ArrayList<String> etablissements = new ArrayList<String>();
//	 ProgressDialog dialog;

	
	
//	
//	  public ArrayList<String> menuArrayList;
//
//	  Cursor menuListe;
	  
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
	 private static final String AUTHORITY = "ca.usimage.resto.RestoProvider";
	    private static final String RESTOS_BASE_PATH = "restos";
	 public static final Uri CONTENT_URI_GROUPBY = Uri.parse("content://" + AUTHORITY
	            + "/" + RESTOS_BASE_PATH + "/GROUPBY");
	
	private SimpleCursorAdapter adapter;
	
	public void afficheList(int loader_id) {
		Log.e ("Resto", "loader_id= "+loader_id);
		getLoaderManager().restartLoader(loader_id, null, this);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	    String[] uiBindFrom = { RestoDatabase.COL_ETAB };
	    int[] uiBindTo = { R.id.TextView01 };
	//    getLoaderManager().initLoader(1, null, this);
	    adapter = new SimpleCursorAdapter(
	            getActivity().getApplicationContext(), R.layout.row,
	            null, uiBindFrom, uiBindTo,
	            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	    setListAdapter(adapter);	
	}
	
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		  String[] projection = { RestoDatabase.ID, RestoDatabase.COL_ETAB };
	    switch (id){
	    	case RESTO_RECENT_LOADER:
	    	  

	     	    return new CursorLoader(getActivity(),
	    	            RestoProvider.CONTENT_URI, projection, null, null, "date_infraction DESC");
	    		
	    	case RESTO_ALPHA_LOADER:
	    		
	    		return new CursorLoader(getActivity(),
	    	            CONTENT_URI_GROUPBY, projection, null, null,"etablissement ASC");
	    		    
	    	case RESTO_HIGH_LOADER:
	    	   

	    		return new CursorLoader(getActivity(),
	    	            RestoProvider.CONTENT_URI, projection, null, null, "montant DESC");
	    		
	    	case RESTO_SEARCH_LOADER:
	    		return new CursorLoader(getActivity(),
	    	            RestoProvider.CONTENT_URI, projection, null, null, null);	
	    		
	    	default: return null;
	    	
	    
	    }
	
	}


	
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
	    adapter.swapCursor(cursor);
	}

	
	public void onLoaderReset(Loader<Cursor> loader) {
	    adapter.swapCursor(null);
	}

	
	
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//	    DatabaseConnector databaseConnector = 
//	    		new DatabaseConnector(getActivity().getApplicationContext());
//	    databaseConnector.open();
//	    restos = databaseConnector.lireResto();
//	    String[] from = new String[] { "etablissement" };
//	    int[] to = new int[] {R.id.TextView01};
//	    CursorAdapter listAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(), R.layout.row, restos, from, to,0);
//
//		setListAdapter(listAdapter);
////		listAdapter.changeCursor(restos);
//		databaseConnector.close();
//		
	    
//     	ArrayAdapter<String> adapter = 
//     			new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.row,etablissements);
//     		    this.setListAdapter(adapter);
//     		    databaseConnector.open();
//     		    restos = databaseConnector.lireResto();
//     		    restos.moveToFirst();
//     		    int i = restos.getColumnIndex("etablissement");
//     	        while (restos.isAfterLast() == false) {
//     	        	
//     	        	Log.e ("Resto", restos.getString(i));
//     	       etablissements.add(	restos.getString(i) );
//     	       restos.moveToNext();
//     	       }
     	        
     	        
//     	       dialog = new ProgressDialog(getActivity().getApplicationContext());
//     	        dialog.setCancelable(true);
//     	        dialog.setMessage("Mise à jour des données en cours...");
//     	        // set the progress to be horizontal
//     	        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//     	        // reset the bar to the default value of 0
//     	        dialog.setProgress(0);
//     	        
//     	        
//	}

	public void afficheMenu( String nom){
		
		
//		   DatabaseConnector databaseConnector = 
//		            new DatabaseConnector(getActivity().getApplicationContext()); 
//	    databaseConnector.open();
//	    menuListe  = databaseConnector.lireMenu(nom);
//	    String[] from = new String[] { "itemmenu" };
//	    int[] to = new int[] {R.id.itemTextView};
//	    CursorAdapter menuAdapter = new SimpleCursorAdapter(getActivity(), R.layout.menu_list_item, null, from, to);
//
//		setListAdapter(menuAdapter);
//		
//		menuAdapter.changeCursor(menuListe);
//		databaseConnector.close();
	}


	

}
