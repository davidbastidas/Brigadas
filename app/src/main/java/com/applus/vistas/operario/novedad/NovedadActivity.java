package com.applus.vistas.operario.novedad;

import com.applus.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class NovedadActivity extends AppCompatActivity {

	FragmentManager fragmentManager;
	Fragment fragment = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_novedad);
		fragment = new NovedadFormFragment();
		if (fragment != null) {
			iniciarFragment(fragment);
		}
	}
	public void iniciarFragment(Fragment fragment){
		fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.frame_novedad_container, fragment).commit();
	}
}
