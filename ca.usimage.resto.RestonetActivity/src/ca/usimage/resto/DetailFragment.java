package ca.usimage.resto;




import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
	
	
	private static final int RESTO_DETAILS_LOADER = 0x05;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
	
		
		View view=inflater.inflate(R.layout.detailfragment, container, false); 
		return view; 
		}

	public void afficheDetails(Long rowId) {	
		Bundle mBundle = new Bundle();
		mBundle.putLong("rowid", rowId);
		getLoaderManager().restartLoader(RESTO_DETAILS_LOADER, mBundle, this);
				
	}



	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
	
		    switch (id){
		    	case RESTO_DETAILS_LOADER:
	        	    return new CursorLoader(getActivity(),
		    	            RestoProvider.CONTENT_URI, null, "_id= "+ args.getLong("rowid"), null, null);
		    	default: return null;
		    }
	}



	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		String etabText, prop, adresseText, categorieText, Date_infraText, villeText, Date_jugeText, DescText, MontantText;
		cursor.moveToFirst();
		
		int etabIndex = cursor.getColumnIndex("etablissement");
		etabText = cursor.getString(etabIndex);
		TextView Etab  = (TextView) getView().findViewById(R.id.TextViewEtablissement);
		Etab.setText(etabText);
		
		int propIndex = cursor.getColumnIndex("proprietaire");
		prop = cursor.getString(propIndex);
		TextView Prop  = (TextView) getView().findViewById(R.id.TextViewProprietaire);
		Prop.setText(prop);
		
		int adresseIndex = cursor.getColumnIndex("adresse");
		adresseText = cursor.getString(adresseIndex);
		TextView Adresse  = (TextView) getView().findViewById(R.id.TextViewAdresse);
		Adresse.setText(adresseText);
		
		int villeIndex = cursor.getColumnIndex("ville");
		villeText = cursor.getString(villeIndex);
		TextView Ville  = (TextView) getView().findViewById(R.id.TextViewVille);
		Ville.setText(villeText);
		
		int categorieIndex = cursor.getColumnIndex("categorie");
		categorieText = cursor.getString(categorieIndex);
		TextView Categorie  = (TextView) getView().findViewById(R.id.TextViewCategorie);
		Categorie.setText(categorieText);
		
		int dateinfraIndex = cursor.getColumnIndex("date_infraction");
		Date_infraText = cursor.getString(dateinfraIndex);
		TextView Date_Infra  = (TextView) getView().findViewById(R.id.TextViewDate_Infraction);
		Date_Infra.setText(Date_infraText);
		
		int datejugeIndex = cursor.getColumnIndex("date_jugement");
		Date_jugeText = cursor.getString(datejugeIndex);
		TextView Date_Juge  = (TextView) getView().findViewById(R.id.TextViewDate_Jugement);
		Date_Juge.setText(Date_jugeText);
		
		int montantIndex = cursor.getColumnIndex("montant");
		MontantText = cursor.getString(montantIndex);
		TextView Montant  = (TextView) getView().findViewById(R.id.TextViewMontant);
		Montant.setText(MontantText);
		
		int descIndex = cursor.getColumnIndex("description");
		DescText = cursor.getString(descIndex);
		TextView Desc  = (TextView) getView().findViewById(R.id.TextViewDescription);
		Desc.setText(DescText);
	}



	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}
}
