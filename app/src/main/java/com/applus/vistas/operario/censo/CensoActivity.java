package com.applus.vistas.operario.censo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.applus.R;
import com.applus.modelos.Cliente;

public class CensoActivity extends AppCompatActivity implements ActualizarCensoFragment.ActualizarCensoListener, NuevoCensoFormFragment.FirmaListener{

	public static String TAG_NUEVO_CENSO = "NUEVO_CENSO";
	public static String TAG_FIRMA = "FIRMA";
	CensoActivity listener = null;
	FragmentManager fragmentManager;
	Fragment fragment = null;
	Fragment fragmentNuevoCenso = null;
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
		fragmentNuevoCenso = new NuevoCensoFormFragment(listener);
		if (fragment != null) {
			Bundle bundle = new Bundle();
			bundle.putParcelable("cliente", cliente);
			fragmentNuevoCenso.setArguments(bundle);
			fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_censo_container, fragmentNuevoCenso, TAG_NUEVO_CENSO).addToBackStack(null).commit();
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

	@Override
	public void onPedirFirma() {
		//llama al fragment para mostrar la ventana de firma
		fragment = new FirmaFragment(listener);
		fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.add(R.id.frame_censo_container, fragment, TAG_FIRMA).addToBackStack(TAG_FIRMA).hide(fragmentNuevoCenso).commit();
	}

	@Override
	public void onRecibirFirma(String firmaBitMapToString) {
		System.out.println("Firma recibida");
		NuevoCensoFormFragment myFragment = (NuevoCensoFormFragment)getFragmentManager().findFragmentByTag(TAG_NUEVO_CENSO);
		myFragment.setFirmaString(firmaBitMapToString);
		onBackPressed();
		/*fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.frame_censo_container, fragmentNuevoCenso).commit();*/
	}

	@Override
	public void onBackPressed(){
		FragmentManager fm = getFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			fm.popBackStack();
			Fragment fragment = getFragmentManager().findFragmentByTag(TAG_FIRMA);
			if (fragment != null) {
				NuevoCensoFormFragment fa = (NuevoCensoFormFragment) fragmentNuevoCenso;
				fa.validarTitulo();
			}
		} else {
			super.onBackPressed();
		}
	}
}
