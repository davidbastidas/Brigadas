package com.applus.vistas.operario.censo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.applus.R;

public class CensoActivity extends AppCompatActivity {

	FragmentManager fragmentManager;
	Fragment fragment = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_censo);
		fragment = new CensoFormFragment();
		if (fragment != null) {
			iniciarFragment(fragment);
		}
	}
	public void iniciarFragment(Fragment fragment){
		fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.frame_censo_container, fragment).commit();
	}
}
