package com.applus.vistas.operario.novedad;

import com.applus.controladores.ConexionController;
import com.applus.controladores.NovedadController;
import com.applus.controladores.NovedadObservacionController;
import com.applus.controladores.NovedadTipoController;
import com.applus.R;
import com.applus.modelos.Cliente;
import com.applus.modelos.NovedadObservacion;
import com.applus.modelos.NovedadTipo;
import com.applus.modelos.Novedades;
import com.applus.modelos.SesionSingleton;
import com.applus.vistas.operario.DialogoGPS;
import com.applus.vistas.operario.DialogoGPS.OnGPSIntent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

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
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NovedadFormFragment extends Fragment implements OnNovedad, OnGPSIntent {

	ConexionController conexion = new ConexionController();
	private Spinner tipo_novedad, observacion;
	private TextView cliente, distrito, municipio, barrio;
	private EditText codigo, otro;
	private Button buscar;

	List<NovedadTipo> lista_tipo_novedad = null;
	List<NovedadObservacion> lista_obs_novedad = null;

	NovedadTipo tipo_novedad_final = null;
	NovedadObservacion obs_novedad_final = null;

	Cliente cliente_obj = null;
	NovedadFormFragment listener = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		comenzarLocalizacion();
		View rootView = inflater.inflate(R.layout.fragment_novedad, container, false);
		listener = this;
		tipo_novedad = (Spinner) rootView.findViewById(R.id.novedad);
		observacion = (Spinner) rootView.findViewById(R.id.observacion);
		cliente = (TextView) rootView.findViewById(R.id.cliente);
		distrito = (TextView) rootView.findViewById(R.id.distrito);
		municipio = (TextView) rootView.findViewById(R.id.municipio);
		barrio = (TextView) rootView.findViewById(R.id.barrio);
		codigo = (EditText) rootView.findViewById(R.id.codigo);
		otro = (EditText) rootView.findViewById(R.id.otro);
		buscar = (Button) rootView.findViewById(R.id.buscar);

		NovedadTipoController novtip = new NovedadTipoController();
		lista_tipo_novedad = novtip.consultar(0, 0, "", getActivity());
		ArrayAdapter<NovedadTipo> novtipAdapter = new ArrayAdapter<NovedadTipo>(getActivity(),
				android.R.layout.simple_spinner_item, lista_tipo_novedad);
		novtipAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tipo_novedad.setAdapter(novtipAdapter);
		tipo_novedad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				tipo_novedad_final = (NovedadTipo) parentView.getItemAtPosition(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}
		});

		NovedadObservacionController novobs = new NovedadObservacionController();
		lista_obs_novedad = novobs.consultar(0, 0, "", getActivity());
		ArrayAdapter<NovedadObservacion> novobsAdapter = new ArrayAdapter<NovedadObservacion>(getActivity(),
				android.R.layout.simple_spinner_item, lista_obs_novedad);
		novobsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		observacion.setAdapter(novobsAdapter);
		observacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				obs_novedad_final = (NovedadObservacion) parentView.getItemAtPosition(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}
		});
		// add a click listener to the button
		buscar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					if (Integer.parseInt(codigo.getText().toString()) > 0) {
						progressDialog("Consulta...");
						msg = new Message();
						msg.obj = "Buscando Cliente...";
						handlerProgreso.sendMessage(msg);
						conexion.getCliente("" + SesionSingleton.getInstance().getFk_id_operario(),
								codigo.getText().toString().trim());
					} else {
						Toast.makeText(getActivity(), "Por favor ingresa el CODIGO",
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

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

	@Override
	public void onConsultaCliente(String output) {
		JSONObject json_data;
		try {
			json_data = new JSONObject(output);
			cliente_obj = new Cliente();
			cliente_obj.setId(json_data.getInt("id"));
			cliente_obj.setCodigo(json_data.getInt("codigo"));
			cliente_obj.setNombre(json_data.getString("nombre"));
			cliente_obj.setDireccion(json_data.getString("direccion"));
			cliente_obj.setFk_distrito(json_data.getInt("fk_distrito"));
			cliente_obj.setFk_municipio(json_data.getInt("fk_municipio"));
			cliente_obj.setFk_barrio(json_data.getInt("fk_barrio"));
			cliente_obj.setDistrito(json_data.getString("distrito"));
			cliente_obj.setMunicipio(json_data.getString("municipio"));
			cliente_obj.setBarrio(json_data.getString("barrio"));

			cliente.setText(cliente_obj.getNombre());
			distrito.setText("" + cliente_obj.getDistrito());
			municipio.setText("" + cliente_obj.getMunicipio());
			barrio.setText("" + cliente_obj.getBarrio());
			if (progressDialog != null) {
				progressDialog.dismiss();
			}

		} catch (Exception e) {
			cliente_obj = null;
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			Toast.makeText(getActivity(), "Cliente no Encontrado. Exception: onConsultaCliente- " + e,
					Toast.LENGTH_LONG).show();
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		conexion.callback_novedad = this;
		conexion.setActivity(getActivity());
		dialogoDinamico("Totalizador");

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_novedad, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.m_guardar:
				guardar();
				return true;
			default:
				break;
		}

		return false;
	}

	public void guardar() {
		String motivo = "";
		boolean pasa = true;
		if (tipo_novedad_final == null) {
			pasa = false;
			motivo = "Elija tipo de novedad";
		} else if (obs_novedad_final == null) {
			pasa = false;
			motivo = "Elija la Observacion";
		} else if (cliente_obj == null) {
			if (codigo.getText().toString().length() < 6) {
				pasa = false;
				motivo = "Escriba al menos los 6 digitos del Codigo";
			} else {
				cliente_obj = new Cliente();
				cliente_obj.setCodigo(Long.parseLong(codigo.getText().toString()));
				cliente_obj.setBarrio("0");
				cliente_obj.setFk_distrito(0);
				cliente_obj.setFk_municipio(0);
				cliente_obj.setNombre("NULO");
				cliente_obj.setId(0);
			}
		}
		if (pasa) {
			iniciarGuardado(LATITUD, LONGITUD, ACURRACY);
		} else {
			Toast.makeText(getActivity(), motivo,
					Toast.LENGTH_SHORT).show();
		}

	}

	Novedades novedad;
	NovedadController novedadController;
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

					novedadController = new NovedadController();
					novedad = new Novedades();
					novedad.setLatitud(lat);
					novedad.setLongitud(lon);
					novedad.setAcurracy(Float.parseFloat(acurracy));

					novedad.setCodigo(cliente_obj.getCodigo());

					novedad.setBarrio("" + cliente_obj.getFk_barrio());

					novedad.setObservaciones((int) obs_novedad_final.getId());
					System.out
							.println("Operario: "
									+ SesionSingleton.getInstance()
									.getFk_id_operario());
					novedad.setFk_usuario(SesionSingleton.getInstance()
							.getFk_id_operario());
					novedad.setLast_insert(0);

					novedad.setDepartamento("" + cliente_obj.getFk_distrito());
					novedad.setMunicipio("" + cliente_obj.getFk_municipio());
					novedad.setOtro(otro.getText().toString().trim());
					novedad.setCliente(cliente_obj.getNombre());
					String date = new SimpleDateFormat("yyyy-MM-dd")
							.format(new Date());
					novedad.setFecha(date);
					String time = new SimpleDateFormat("HH:mm:ss")
							.format(new Date());
					novedad.setHora(time);
					novedad.setFk_cliente(cliente_obj.getId());
					novedad.setFk_novedad((int) tipo_novedad_final.getId());

					novedadController.insertar(novedad, getActivity());
					last_insert = novedadController.getLastInsert();
					guardadoActivo = true;
					if (SesionSingleton.getInstance().isINTERNET()) {//si internet
						msg = new Message();
						msg.obj = "Enviando por internet...";
						System.out.println("Enviando por internet...");
						handlerProgreso.sendMessage(msg);
						conexion.enviarNovedad(novedad);
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
		tipo_novedad.setSelection(0);
		codigo.setText("");
		cliente.setText("");
		distrito.setText("");
		municipio.setText("");
		barrio.setText("");
		observacion.setSelection(0);
		otro.setText("");
		last_insert = 0;
	}

	LocationManager locManager;
	public boolean guardadoActivo = true, pasarConPuntoelegido = false;
	LocationListener locListener;
	public int ESTADO_SERVICE = 0;
	public static final int OUT_OF_SERVICE = 0;
	public static final int TEMPORARILY_UNAVAILABLE = 1;
	public static final int AVAILABLE = 2;
	public String LONGITUD = "0.0", LATITUD = "0.0", ACURRACY = "0";

	Looper myLooper = null;
	Handler myHandler = null;
	Runnable runnable = null;

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
	public void onEnviarInternetNovedad(String result) {
		JSONObject json_data;
		try {
			json_data = new JSONObject(result);
			if(json_data.getInt("last_insert")>0){
				//ingresar el last_insert al totalizador
				ContentValues values=new ContentValues();
				values.put("last_insert", json_data.getInt("last_insert"));
				int updt=novedadController.actualizar(values, "id="+last_insert, getActivity());
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
				msg.obj = "La gestion se guardo en el telefono pero no lo recibio el administrador," +
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
}
