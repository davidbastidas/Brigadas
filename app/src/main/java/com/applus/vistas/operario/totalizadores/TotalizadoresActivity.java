package com.applus.vistas.operario.totalizadores;

import com.applus.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

public class TotalizadoresActivity extends Activity {

	FragmentManager fragmentManager;
	Fragment fragment = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_totalizadores);
		fragment = new TotalizadorFormFragment();
		if (fragment != null) {
			iniciarFragment(fragment);
		}
	}
	public void iniciarFragment(Fragment fragment){
		fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.frame_totalizadores_container, fragment).commit();
	}
}
