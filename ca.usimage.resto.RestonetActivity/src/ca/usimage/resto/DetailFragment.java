package ca.usimage.resto;




import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class DetailFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
	
		
		View view=inflater.inflate(R.layout.detailfragment, container, false); 
		return view; 
		}

	
	
	public void afficheEtab(String etab) {		
		TextView Etab  = (TextView) getView().findViewById(R.id.TextViewEtablissement);
		Etab.setText(etab);
				
	}
	
	public void afficheProp(String prop) {		
		TextView Prop  = (TextView) getView().findViewById(R.id.TextViewProprietaire);
		Prop.setText(prop);
				
	}
}
