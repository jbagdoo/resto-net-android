package ca.usimage.resto;

import java.util.HashMap;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CarteFragment extends MapFragment implements LoaderManager.LoaderCallbacks<Cursor>{
	
	
	 HashMap<String, Integer> extraMarkerInfo = new HashMap<String, Integer>();
	 
	 OnInfoWindowClickListener onInfoWindowClickListener;
	 GoogleMap googleMap;
	 long ROWID;
	 
	  // Container Activity must implement this interface
	    public interface OnMarkerClickListener {
	        public void onMarkerClicked(int rowid);
	    }

	 
	
	private static final int MAP_LOADER = 0x06;
    double LAT = 0,LNG=0;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.e("Carte"," On Attach");
			try {
				 onInfoWindowClickListener = ( OnInfoWindowClickListener) activity;
			
			} catch (ClassCastException e) {
				throw new ClassCastException(activity.toString()
						+ " doit implementer  OnInfoWindowClickListener");
			}
		}
		

	
	
	@Override
public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
	Log.e("Carte"," On Activity Created");	
		Bundle arguments = new Bundle();
		   
	    arguments = this.getArguments();
	  
	   
		ROWID = arguments.getLong("rowid");
	   
		googleMap = this.getMap();
		// default map location to show overall view of Montreal
		LAT = 45.500;
        LNG = -73.600;
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(LAT,LNG)));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
		googleMap.setMyLocationEnabled(true);
		
		googleMap.setOnInfoWindowClickListener(onInfoWindowClickListener);
		googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {
			
			// this is needed to make a infoWindow that does not show the snippet, since the snippet holds the rowid used for details

		
		    private final View v = getActivity().getLayoutInflater().inflate(R.layout.info_window_layout, null);

			@Override
			public View getInfoWindow(Marker arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public View getInfoContents(Marker arg0) {
				// TODO Auto-generated method stub
				
				
				
			     // Getting view from the layout file info_window_layout
            
 
                String nom = arg0.getTitle();
 
                // Getting reference to the TextView to set latitude
                TextView etab = (TextView) v.findViewById(R.id.nom);
 

                etab.setText(nom);

 
                // Returning the view containing InfoWindow contents
                return v;
 
			
			}
			
		}	);
		 
		   
		    


	    
		}
@Override
public void onResume()
{
    super.onResume();
    Log.e("Carte"," On resume");
    // call initLoader on Resume avoids a bug which calls onLoadFinished twice
  
    LoaderManager lm = getLoaderManager();

    lm.initLoader(MAP_LOADER, null, this);
    Log.e("CarteFragment","onResume");
}

public Loader<Cursor> onCreateLoader(int id, Bundle args) {
	  String[] projection = { RestoDatabase.ID, RestoDatabase.COL_LAT, RestoDatabase.COL_LONG, RestoDatabase.COL_ETAB };
  switch (id){
  	case MAP_LOADER:
  		Log.e("onCreateLoader", "map loader");
  	    return new CursorLoader(getActivity(),
  	            RestoProvider.CONTENT_URI, projection, null, null, null);

  	default: return null;
  	
  
  }

}

private void drawMarker(LatLng point, String nom, int resto_row_id){
    // Creating an instance of MarkerOptions
    MarkerOptions markerOptions = new MarkerOptions();

    // Setting latitude and longitude for the marker
    markerOptions.position(point);
    markerOptions.title(nom);
    
    markerOptions.snippet(Integer.toString(resto_row_id));
   
    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.measle));
    
    
    // Adding marker on the Google Map
    Marker marker =  googleMap.addMarker(markerOptions);
    
    // force show infowindow on selected resto
    if (resto_row_id == ROWID) {
     marker.showInfoWindow();
    }
   
    // store id from sqldb into marker extrainfo hashmap which has maker ID as a key
    // this will be used to get resto detail info when marker is clicked
    extraMarkerInfo.put(marker.getId(),resto_row_id);
}

@Override
public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
	
    int locationCount = 0;
    double lat=0;
    double lng=0;
    float zoom=0;
    int id=0;
    String nom;
    Log.e("Carte"," On Load Finished");
    // Number of locations available in the SQLite database table
    locationCount = arg1.getCount();

    // Move the current record pointer to the first row of the table
    arg1.moveToFirst();

    for(int i=0;i<locationCount;i++){

        // Get the latitude
        lat = arg1.getDouble(arg1.getColumnIndex("latitude"));

        // Get the longitude
        lng = arg1.getDouble(arg1.getColumnIndex("longitude"));
        
      
        
        nom = arg1.getString(arg1.getColumnIndex("etablissement"));
        
        id = arg1.getInt(arg1.getColumnIndex("_id"));
        
        if (ROWID == 99999) {
        	// default LAT LNG for Montreal
        	LAT = 45.500;
            LNG = -73.600;
            zoom=10;
        } else
        if (id == ROWID) {
        	LAT = lat;
        	LNG = lng;
        	zoom=14;
        }

        // Get the zoom level
   //     zoom = arg1.getFloat(arg1.getColumnIndex(LocationsDB.FIELD_ZOOM));

        // Creating an instance of LatLng to plot the location in Google Maps
        LatLng location = new LatLng(lat, lng);

        // Drawing the marker in the Google Maps
        drawMarker(location,nom, id);

        // Traverse the pointer to the next row
        arg1.moveToNext();
    }
	
    if(locationCount>0){
        // once all items have been scanned and positioned, zoom on current selected item (at ROWID)
    	Log.e("LAT="+LAT+" ROWID="+ROWID,"LNG"+LNG);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(LAT,LNG)));

    
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
    }
	
}

@Override
public void onLoaderReset(Loader<Cursor> arg0) {
	// TODO Auto-generated method stub
	
}






}
