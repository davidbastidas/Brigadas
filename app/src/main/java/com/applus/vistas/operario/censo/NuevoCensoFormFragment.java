package com.applus.vistas.operario.censo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.applus.R;
import com.applus.controladores.CensoController;
import com.applus.controladores.ConexionController;
import com.applus.modelos.Censo;
import com.applus.modelos.CensoForm;
import com.applus.modelos.Cliente;
import com.applus.modelos.SesionSingleton;
import com.applus.vistas.operario.DialogoGPS;
import com.applus.vistas.operario.DialogoGPS.OnGPSIntent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NuevoCensoFormFragment extends Fragment implements
		OnCenso, OnGPSIntent, DialogElectrodomesticos.ElectrodomesticosListener,
		ElectrodomesticosListAdapter.ElectrodomesticoListener{

	ConexionController conexion = new ConexionController();

	Cliente cliente_obj = null;
	NuevoCensoFormFragment listener = null;

	CensoController censoController = new CensoController();

	EditText nombre, direccion, nic;
	ListView lista_electrodomesticos;
	TextView consumo;

	ElectrodomesticosListAdapter electrodomesticosAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		listener = this;
		comenzarLocalizacion();
		View rootView = inflater.inflate(R.layout.fragment_nuevo_censo, container, false);

		nombre = (EditText) rootView.findViewById(R.id.nc_nombre);
		direccion = (EditText) rootView.findViewById(R.id.nc_direccion);
		nic = (EditText) rootView.findViewById(R.id.nc_nic);
		consumo = (TextView) rootView.findViewById(R.id.lb_consumo);

		lista_electrodomesticos = (ListView) rootView.findViewById(R.id.nc_lista_electrodomesticos);
		electrodomesticosAdapter = new ElectrodomesticosListAdapter(
				listener,
				getActivity(),
				new ArrayList<CensoForm>());
		lista_electrodomesticos.setAdapter(electrodomesticosAdapter);

		return rootView;
	}

	//El Fragment ha sido quitado de su Activity y ya no est� disponible
	@Override
	public void onDetach() {
		if (locListener != null) {
			locManager.removeUpdates(locListener);
			locListener = null;
			locManager = null;
		}
		super.onDetach();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		conexion.callback_censo = this;
		conexion.setActivity(getActivity());
		dialogoDinamico("Envio Censo");

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_censo, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.m_guardar_censo:
				guardar();
				return true;
			case R.id.m_electrodomesticos:
				DialogFragment df=new DialogElectrodomesticos(listener);
				df.show(getFragmentManager(), "electrodomesticos");
				return true;
			default:
				break;
		}

		return false;
	}

	public void guardar() {
		String motivo = "";
		boolean pasa = true;
		if (cliente_obj == null) {
            cliente_obj = new Cliente();
            cliente_obj.setCodigo(0);
			cliente_obj.setNombre(nombre.getText().toString());
			cliente_obj.setDireccion(direccion.getText().toString());
			if (nic.getText().toString().equals("")){
				nic.setText("0");
			}
			cliente_obj.setNic(Long.parseLong(nic.getText().toString()));
            cliente_obj.setBarrio("0");
            cliente_obj.setFk_distrito(0);
            cliente_obj.setFk_municipio(0);
            cliente_obj.setId(0);
		}
		if (pasa) {
			iniciarGuardado(LATITUD, LONGITUD, ACURRACY);
		} else {
			Toast.makeText(getActivity(), motivo,
					Toast.LENGTH_SHORT).show();
		}

	}

	Censo censo;
	long last_insert = 0;

	public void iniciarGuardado(String lat, String lon, String acurracy) {
		try {
			if (pasarConPuntoelegido) {
				if (guardadoActivo) {
					System.out.println("guardadoActivo: " + guardadoActivo);
					guardadoActivo = false;
					pasarConPuntoelegido = false;
					//myHandler.removeCallbacks(runnable);
					progressDialog("Guardando...");
					msg = new Message();
					msg.obj = "Guardando en el Telefono...";
					handlerProgreso.sendMessage(msg);
					System.out.println("Guardando en el Telefono...");

                    censoController = new CensoController();
                    censo = new Censo();
                    censo.setLatitud(lat);
                    censo.setLongitud(lon);
                    censo.setAcurracy(Float.parseFloat(acurracy));

                    censo.setCodigo(cliente_obj.getCodigo());
					censo.setNic(cliente_obj.getNic());
                    censo.setBarrio("" + cliente_obj.getFk_barrio());
					ArrayList<CensoForm> electrodomesticos = electrodomesticosAdapter.getAllData();
					Gson gsonBuilder = new GsonBuilder().create();
					String stringJson = gsonBuilder.toJson(electrodomesticos);

					censo.setDatos("{\"nombre\":\""+cliente_obj.getNombre()+"\", \"direccion\":\""+cliente_obj.getDireccion()+"\"}");

					censo.setFormulario(stringJson);

					System.out
							.println("Operario: "
									+ SesionSingleton.getInstance()
									.getFk_id_operario());
                    censo.setFk_usuario(SesionSingleton.getInstance()
							.getFk_id_operario());
                    censo.setLast_insert(0);

                    censo.setDepartamento("" + cliente_obj.getFk_distrito());
                    censo.setMunicipio("" + cliente_obj.getFk_municipio());
                    censo.setCliente(cliente_obj.getNombre());
					String date = new SimpleDateFormat("yyyy-MM-dd")
							.format(new Date());
                    censo.setFecha(date);
					String time = new SimpleDateFormat("HH:mm:ss")
							.format(new Date());
                    censo.setHora(time);
                    censo.setFk_cliente(cliente_obj.getId());

					censoController.insertar(censo, getActivity());
					last_insert = censoController.getLastInsert();
					guardadoActivo = true;
					if (SesionSingleton.getInstance().isINTERNET()) {//si internet
						msg = new Message();
						msg.obj = "Enviando por internet...";
						System.out.println("Enviando por internet...");
						handlerProgreso.sendMessage(msg);
						conexion.enviarCenso(censo);
					} else {
						System.out.println("Limpiando");
						limpiar();
					}
				}
			} else {
				/*myHandler.removeCallbacks(runnable);
				if (locListener != null) {
					locManager.removeUpdates(locListener);
					DialogFragment nt = new DialogoGPS(listener);
	            	nt.show(getFragmentManager(), "gps");
				}*/
				DialogFragment nt = new DialogoGPS(listener, LATITUD, LONGITUD, ACURRACY);
				nt.show(getFragmentManager(), "gps");
			}
		} catch (Exception e) {
			System.err.println("Exception guardando: " + e + " " + e.getStackTrace()[0].getLineNumber());
			//myHandler.removeCallbacks(runnable);
			msg = new Message();
			msg.obj = "Error";
			System.out.println("Exception guardando: " + e + " " + e.getStackTrace()[0].getLineNumber());
			handlerProgreso.sendMessage(msg);
		}
	}

	private void limpiar() {
		last_insert = 0;
		nombre.setText("");
		direccion.setText("");
		nic.setText("");
		electrodomesticosAdapter = new ElectrodomesticosListAdapter(
				listener,
				getActivity(),
				new ArrayList<CensoForm>());
		lista_electrodomesticos.setAdapter(electrodomesticosAdapter);
		consumo.setText("Total consumo 0 Watts");
		cliente_obj = null;
	}

	LocationManager locManager;
	public boolean guardadoActivo = true, pasarConPuntoelegido = false;
	LocationListener locListener;
	public int ESTADO_SERVICE = 0;
	public static final int OUT_OF_SERVICE = 0;
	public static final int TEMPORARILY_UNAVAILABLE = 1;
	public static final int AVAILABLE = 2;
	public String LONGITUD = "0.0", LATITUD = "0.0", ACURRACY = "0";

	private void comenzarLocalizacion() {
		// Obtenemos una referencia al LocationManager
		locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			// Nos registramos para recibir actualizaciones de la posici�n
			locListener = new LocationListener() {
				public void onLocationChanged(Location location) {
					System.out.println("onLocationChanged");
					LATITUD = String.valueOf(location.getLatitude());
					LONGITUD = String.valueOf(location.getLongitude());
					ACURRACY = String.valueOf(location.getAccuracy());
				}

				@Override
				public void onProviderDisabled(String provider) {
					locManager.removeUpdates(locListener);
					System.out.println("onProviderDisabled");
				}

				@Override
				public void onProviderEnabled(String provider) {
				}

				@Override
				public void onStatusChanged(String provider, int status,
											Bundle extras) {
					ESTADO_SERVICE = status;
					mostrarEstadoGPS();
				}
			};
			if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
			//iniciarTareaGPS30Sec();
		} else {
			ActivarGPS();
		}
	}
	/*public void iniciarTareaGPS30Sec(){
		myLooper = Looper.myLooper();
	    myHandler = new Handler(myLooper);
	    runnable = new Runnable() {
	         public void run() {
	        	 if(guardadoActivo){
	        		 System.out.println("cumplio 30 seg");
		        	 iniciarGuardado("0.0","0.0");
	        	 }else{
	        		 System.out.println("ya se inicio el envio");
	        	 }
	         }
	    };
	    myHandler.postDelayed(runnable, 30000);
	}*/
	private void mostrarEstadoGPS(){
		if(ESTADO_SERVICE==OUT_OF_SERVICE){
			Toast.makeText(getActivity(), "Servicio GPS no disponible", Toast.LENGTH_LONG).show();
			System.out.println("OUT_OF_SERVICE");
		} else if(ESTADO_SERVICE==TEMPORARILY_UNAVAILABLE){
			System.out.println("TEMPORARILY_UNAVAILABLE");
			Toast.makeText(getActivity(), "Servicio GPS no disponible", Toast.LENGTH_LONG).show();
		} else if(ESTADO_SERVICE==AVAILABLE){
			System.out.println("AVAILABLE");
			Toast.makeText(getActivity(), "GPS Disponible", Toast.LENGTH_LONG).show();
		}
	}
	private Handler handlerProgreso, handlerDialog;
	ProgressDialog progressDialog;
	AlertDialog.Builder dialog;
	AlertDialog.Builder dialogoServicios;
	Message msg;
	private void progressDialog(String nombreActividad) {
		handlerProgreso=null;progressDialog=null;
		handlerProgreso = new Handler() {
			public void handleMessage(Message msg) {
				progressDialog.setMessage((String) msg.obj);
			}
		};
		progressDialog = ProgressDialog.show(getActivity(), nombreActividad,
				"Espere...", true);
	}

	@Override
	public void onEnviarInternetCenso(String result) {
		JSONObject json_data;
		try {
			json_data = new JSONObject(result);
			if(json_data.getInt("last_insert")>0){
				//ingresar el last_insert al censo
				ContentValues values=new ContentValues();
				values.put("last_insert", json_data.getInt("last_insert"));
				int updt=censoController.actualizar(values, "id="+last_insert, getActivity());
				if(updt>0){
					limpiar();
					if(progressDialog!=null){
						progressDialog.dismiss();
					}
					msg = new Message();
					msg.obj = "ok";
					handlerDialog.sendMessage(msg);
				}
			}else{
				limpiar();
				if(progressDialog!=null){
					progressDialog.dismiss();
				}
				msg = new Message();
				msg.obj = "El censo se guardo en el telefono pero no lo recibio el administrador," +
						" Intenta mas tarde.";
				handlerDialog.sendMessage(msg);
				
			}
		} catch (Exception e) {
			limpiar();
			System.out.println("progressDialog: "+progressDialog);
			if(progressDialog!=null){
				progressDialog.dismiss();
			}
			msg = new Message();
			msg.obj = "Se guardo PERO....Excepcion: "+e;
			handlerDialog.sendMessage(msg);
		}
		
	}
	private void ActivarGPS() {
		Toast.makeText(getActivity(), "Por favor active su GPS", Toast.LENGTH_SHORT)
				.show();
		Intent settingsIntent = new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		this.startActivityForResult(settingsIntent, 0);
	}
	public void dialogoDinamico(String titulo) {
		// dialog=null;handlerDialog=null;
		handlerDialog = new Handler() {
			public void handleMessage(Message msg) {
				String v = (String) msg.obj;
				if ((v.trim()).equals("ok")) {
					dialog.setIcon(R.drawable.confirmacion_icon);
					dialog.setMessage("Enviado!");
					try {
						dialog.show();
					} catch (Exception e) {
					}
				} else {
					dialog.setIcon(R.drawable.info_icon);
					dialog.setMessage(v);
					try {
						dialog.show();
					} catch (Exception e) {
					}
				}
			}
		};
		dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle(titulo);
		dialog.setPositiveButton("Cerrar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
	}
	
	@Override
	public void onGuardarConPuntoElegido(String latitud,String longitud,String acurracy) {
		pasarConPuntoelegido=true;
		iniciarGuardado(latitud,longitud,acurracy);
	}

	@Override
	public void onSeguirIntentandoGPS() {
		pasarConPuntoelegido=false;
		iniciarGuardado(LATITUD,LONGITUD,ACURRACY);
	}

	@Override
	public void onAddElectrodomestico(CensoForm item) {
		item.setCantidad(1);//agregamos +1
		electrodomesticosAdapter.addToList(item);
		//Toast.makeText(getActivity(), item.getNombre()+item.getWatts(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onEventConsumoTotal(int consumoTotal) {
		consumo.setText("Total consumo "+consumoTotal+" Watts");
	}
}
