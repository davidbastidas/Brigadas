package com.applus.vistas.operario.censo;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.applus.R;
import com.applus.modelos.Cliente;

public class CensoActivity extends AppCompatActivity implements ActualizarCensoFragment.ActualizarCensoListener{

	CensoActivity listener = null;
	FragmentManager fragmentManager;
	Fragment fragment = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_censo);
		listener = this;
		actualizarCensoFragment();
	}
	public void iniciarFragment(Fragment fragment){
		fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.frame_censo_container, fragment).commit();
	}
	private void nuevoCensoFragment(Cliente cliente){
		fragment = new NuevoCensoFormFragment();
		if (fragment != null) {
			Bundle bundle = new Bundle();
			bundle.putParcelable("cliente", cliente);
			fragment.setArguments(bundle);
			iniciarFragment(fragment);
		}
	}
	private void actualizarCensoFragment(){
		fragment = new ActualizarCensoFragment(listener);
		if (fragment != null) {
			iniciarFragment(fragment);
		}
	}

	@Override
	public void onActualizarCenso(Cliente cliente) {
		nuevoCensoFragment(cliente);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_censo_main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar actions click
		switch (item.getItemId()) {
			case R.id.mnuevocenso:
				nuevoCensoFragment(null);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
