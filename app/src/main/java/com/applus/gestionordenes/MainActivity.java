package com.applus.gestionordenes;

import com.applus.R;
import com.applus.controladores.ConexionController;
import com.applus.controladores.SQLiteController;
import com.applus.modelos.SesionSingleton;
import com.applus.vistas.admin.AdminActivity;

import java.io.File;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	Activity activity;
	EditText T_CONTRASENA, T_USUARIO;
	boolean PASA_LOGIN;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		activity=this;
		T_USUARIO = (EditText) findViewById(R.id.T_USUARIO);
		T_CONTRASENA = (EditText) findViewById(R.id.T_CONTRASENA);
		/*System.out.println(Environment.getExternalStorageDirectory().getName());
		System.out.println(Environment.getExternalStorageDirectory().getParent());
		System.out.println(Environment.getExternalStorageDirectory().getAbsolutePath());
		System.out.println(Environment.getExternalStoragePublicDirectory("PAQUETE/ORDENES").getAbsolutePath());
		*/
		obtenerPreferenciasDeInicio();
		checkPermission();
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed(){
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		usdbh.close();
		finish();
		super.onBackPressed();
	}
	public void login(View view){
		crearCarpetas();
		obtenerPreferenciasDeInicio();
		String usuario=T_USUARIO.getText().toString().trim();
		String pass = T_CONTRASENA.getText().toString().trim();
		

		Intent intentar = null;
		if(usuario.equals("admin") && !pass.equals("")){
			if (pass.length() == 8) {
				String sub = pass.substring(4, 8);
				if (sub.equals("0000")) {
					// resetear la contrase�a del admin a 0000
					resetearContrasenaAdmin(activity);
				} else {
					Toast.makeText(this, "Contrase�a debe ser de 4 Numeros",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				SesionSingleton sesion = SesionSingleton.getInstance();
				if (!sesion.getPASS_ADMIN().equals("")) {
					if (pass.equals(sesion.getPASS_ADMIN())) {
						// mostrar la vista admin
						intentar = new Intent(this, AdminActivity.class);
						startActivity(intentar);
					} else {
						Toast.makeText(this, "Contrase�a incorrecta",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					if (pass.equals("0000")) {
						// mostrar la vista admin
						intentar = new Intent(this, AdminActivity.class);
						startActivity(intentar);
					} else {
						Toast.makeText(this, "Contrase�a incorrecta",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
			
		} else if(!usuario.equals("") && !pass.equals("")){
			//probar logueando por internet
			ConexionController con=new ConexionController();
			con.setActivity(this);
			con.loginUser(usuario, pass);
		} else {
			Toast.makeText(this, "Contrase�a y Usuario incorrecto",
					Toast.LENGTH_SHORT).show();
		}
	}
	private void crearCarpetas() {
		try {
			String rutaBase = Environment.getExternalStorageDirectory().getParent()+"/"
					+ SesionSingleton.getInstance().getRUTA_ALMACENAMIENTO();
			System.out.println("rutaBase: "+rutaBase);
			File dir = new File(rutaBase + "/PAQUETE");
			if (!dir.exists()) {
				dir.mkdirs();
				System.out.println("se creo: "+rutaBase + "/PAQUETE");
			}
			dir = new File(rutaBase + "/PAQUETE/ORDENES");
			if (!dir.exists()) {
				dir.mkdirs();
				System.out.println("se creo: "+rutaBase + "/PAQUETE/ORDENES");
			}
			dir = new File(rutaBase + "/PAQUETE/ORDENES/CARGA");
			if (!dir.exists()) {
				dir.mkdirs();
				System.out.println("se creo: "+rutaBase + "/PAQUETE/ORDENES/CARGA");
			}
			dir = new File(rutaBase + "/PAQUETE/ORDENES/ENVIO");
			if (!dir.exists()) {
				dir.mkdirs();
				System.out.println("se creo: "+rutaBase + "/PAQUETE/ORDENES/ENVIO");
			}
			dir = new File(rutaBase + "/PAQUETE/ORDENES/FOTOS");
			if (!dir.exists()) {
				dir.mkdirs();
				System.out.println("se creo: "+rutaBase + "/PAQUETE/ORDENES/FOTOS");
			}
		} catch (Exception e) {
			// Log.w("creating file error", e.toString());
		}
	}
	private void obtenerPreferenciasDeInicio() {
		PASA_LOGIN = false;
		SharedPreferences preferencias = getSharedPreferences("configuracion",
				Context.MODE_PRIVATE);
		SesionSingleton sesion = SesionSingleton.getInstance();
		Editor editor = preferencias.edit();
		if(preferencias.getString("ip", "").equals("")){
			editor.putString("ip", "181.48.232.226:8087");
		}
		if(preferencias.getString("proyecto", "").equals("")){
			editor.putString("proyecto", "appme");
		}
		if(preferencias.getString("memoria", "").equals("")){
			editor.putString("memoria", "sdcard0");
		}
		if(preferencias.getString("vista", "").equals("")){
			editor.putString("vista", "1");
		}
		if(preferencias.getString("ckinternet", "").equals("")){
			editor.putString("ckinternet", "1");
		}
		if(preferencias.getString("orden_realizado", "").equals("")){
			editor.putString("orden_realizado", "0");
		}
		editor.commit();

		preferencias = getSharedPreferences("configuracion",
				Context.MODE_PRIVATE);
		sesion.setIP(preferencias.getString("ip", ""));
		sesion.setPROYECTO(preferencias.getString("proyecto", ""));
		String almacenamiento = preferencias.getString("memoria", "");
		// si escribo sdcard o sdcard1
		sesion.setRUTA_ALMACENAMIENTO(almacenamiento);
		sesion.setRUTA_GUARDAR_DATOS(Environment.getExternalStorageDirectory().getParent()+"/"+almacenamiento+"/PAQUETE/ORDENES");
		String vista=preferencias.getString("vista", "");
		if (vista.equals("1")) {
			sesion.setMODO_VISTA(1);
		} else if (vista.equals("2")) {
			sesion.setMODO_VISTA(2);
		}
		String internet=preferencias.getString("ckinternet", "");
		if (internet.equals("1")) {
			sesion.setINTERNET(true);
		} else {
			sesion.setINTERNET(false);
		}
		sesion.setPASS_ADMIN(preferencias.getString("passwordAdmin",""));
		System.out.println("passwordServicios= "+preferencias.getString("passwordServicios",""));
		sesion.setPASS_ORDENES(preferencias.getString("passwordServicios",""));
		System.out.println("Usuario Operario= "+preferencias.getString("usuario_operario",""));
		sesion.setEstado_datos(preferencias.getString("estado_datos",""));
		sesion.setEstado_envio(preferencias.getString("estado_envio",""));
		PASA_LOGIN = true;
	}
	public void resetearContrasenaAdmin(Activity activity){
		SharedPreferences preferencias = activity.getSharedPreferences("configuracion",
				Context.MODE_PRIVATE);
		Editor editor = preferencias.edit();
		editor.putString("passwordAdmin", "0000");
		editor.commit();
	}
	public void resetearContrasenaServicios(Activity activity){
		SharedPreferences preferencias = activity.getSharedPreferences("configuracion",
				Context.MODE_PRIVATE);
		Editor editor = preferencias.edit();
		editor.putString("passwordServicios", "0000");
		editor.commit();
	}

	private void checkPermission() {

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			//no es version 6 api 23
		} else {
			int permsRequestCode = 100;
			String[] perms = {
					Manifest.permission.INTERNET,
					Manifest.permission.WRITE_EXTERNAL_STORAGE,
					Manifest.permission.ACCESS_FINE_LOCATION,
					Manifest.permission.CAMERA
			};
			int accessInternetPermission = checkSelfPermission(Manifest.permission.INTERNET);
			int accessWiteExternalPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
			int accessFinePermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
			int cameraPermission = checkSelfPermission(Manifest.permission.CAMERA);

			if (accessInternetPermission == PackageManager.PERMISSION_GRANTED &&
					accessWiteExternalPermission == PackageManager.PERMISSION_GRANTED &&
					accessFinePermission == PackageManager.PERMISSION_GRANTED &&
					cameraPermission == PackageManager.PERMISSION_GRANTED) {
				//se realiza metodo si es necesario...
			} else {
				requestPermissions(perms, permsRequestCode);
			}
		}

		return;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case 100:
				// accion o metodo realizar
				break;
		}
	}
}
