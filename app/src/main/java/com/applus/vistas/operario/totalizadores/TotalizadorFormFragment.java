package com.applus.vistas.operario.totalizadores;

import com.applus.controladores.BarrioController;
import com.applus.controladores.ConexionController;
import com.applus.controladores.DepartamentoController;
import com.applus.controladores.MunicipioController;
import com.applus.controladores.TotalizadorController;
import com.applus.controladores.TotalizadorEstadoMedidaController;
import com.applus.controladores.TotalizadorObservacionController;
import com.applus.R;
import com.applus.modelos.Barrio;
import com.applus.modelos.Departamento;
import com.applus.modelos.Municipio;
import com.applus.modelos.SesionSingleton;
import com.applus.modelos.TotalizadorEstadoMedida;
import com.applus.modelos.TotalizadorObservaciones;
import com.applus.modelos.Totalizadores;
import com.applus.vistas.operario.DialogoGPS;
import com.applus.vistas.operario.DialogoGPS.OnGPSIntent;
import com.applus.vistas.operario.brigada.DatePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import android.widget.Toast;

public class TotalizadorFormFragment extends Fragment implements OnTotalizador, OnGPSIntent {

	ConexionController conexion = new ConexionController();
	private Button pick_date;
	private EditText nic, direccion, ct, mt, otro;
	private Spinner estado_medida, observacion;
	private Spinner departamento, municipio, barrio;
	List<Municipio> listm = null;
	List<Barrio> listb = null;
	List<Departamento> lista_departamento = null;
	List<Municipio> lista_municipio = null;
	List<Barrio> lista_barrio = null;
	Departamento departamento_final;
	Municipio municipio_final;
	Barrio barrio_final;

	List<TotalizadorEstadoMedida> lista_estado_medida = null;
	List<TotalizadorObservaciones> lista_observaciones = null;
	List<TotalizadorObservaciones> lista_obstemp = null;
	TotalizadorEstadoMedida estado_medida_final;
	TotalizadorObservaciones observacion_final;

	TotalizadorFormFragment listener = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		comenzarLocalizacion();
		View rootView = inflater.inflate(R.layout.fragment_totalizadores, container, false);
		listener = this;
		pick_date = (Button) rootView.findViewById(R.id.pick_date);

		nic = (EditText) rootView.findViewById(R.id.nic);
		barrio = (Spinner) rootView.findViewById(R.id.barrio);
		departamento = (Spinner) rootView.findViewById(R.id.departamento);
		municipio = (Spinner) rootView.findViewById(R.id.municipio);
		direccion = (EditText) rootView.findViewById(R.id.direccion);
		ct = (EditText) rootView.findViewById(R.id.ct);
		mt = (EditText) rootView.findViewById(R.id.mt);
		estado_medida = (Spinner) rootView.findViewById(R.id.estado_medida);
		observacion = (Spinner) rootView.findViewById(R.id.observacion);
		otro = (EditText) rootView.findViewById(R.id.otro);

		// add a click listener to the button
		pick_date.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DialogFragment picker = new DatePickerFragment(pick_date);
				picker.show(getFragmentManager(), "datePicker");
			}
		});
		DepartamentoController dep = new DepartamentoController();
		lista_departamento = dep.consultar(0, 0, "", getActivity());
		ArrayAdapter<Departamento> departamentoAdapter = new ArrayAdapter<Departamento>(getActivity(),
				android.R.layout.simple_spinner_item, lista_departamento);
		departamentoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		departamento.setAdapter(departamentoAdapter);
		departamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				departamento_final = (Departamento) parentView.getItemAtPosition(position);
				lista_municipio.clear();
				for (Municipio i : listm) {
					if (i.getFk_departamento() == departamento_final.getId()) {
						lista_municipio.add(i);
					}
				}
				if (lista_municipio.size() == 0) {
					municipio_final = null;
				}
				ArrayAdapter<Municipio> municipioAdapter = new ArrayAdapter<Municipio>(getActivity(),
						android.R.layout.simple_spinner_item, lista_municipio);
				municipio.setAdapter(municipioAdapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}
		});
		MunicipioController muni = new MunicipioController();
		listm = muni.consultar(0, 0, "", getActivity());
		lista_municipio = new ArrayList<Municipio>();
		ArrayAdapter<Municipio> municipioAdapter = new ArrayAdapter<Municipio>(getActivity(),
				android.R.layout.simple_spinner_item, lista_municipio);
		municipioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		municipio.setAdapter(municipioAdapter);
		municipio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				municipio_final = (Municipio) parentView.getItemAtPosition(position);
				lista_barrio.clear();
				for (Barrio i : listb) {
					if (i.getFk_municipio() == municipio_final.getId()) {
						lista_barrio.add(i);
					}
				}
				if (lista_barrio.size() == 0) {
					barrio_final = null;
				}
				ArrayAdapter<Barrio> barrioAdapter = new ArrayAdapter<Barrio>(getActivity(),
						android.R.layout.simple_spinner_item, lista_barrio);
				barrio.setAdapter(barrioAdapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});

		BarrioController barr = new BarrioController();
		listb = barr.consultar(0, 0, "", getActivity());
		lista_barrio = new ArrayList<Barrio>();
		ArrayAdapter<Barrio> barrioAdapter = new ArrayAdapter<Barrio>(getActivity(),
				android.R.layout.simple_spinner_item, lista_barrio);
		barrioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		barrio.setAdapter(municipioAdapter);
		barrio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				barrio_final = (Barrio) parentView.getItemAtPosition(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});

		TotalizadorEstadoMedidaController totestmed = new TotalizadorEstadoMedidaController();
		lista_estado_medida = totestmed.consultar(0, 0, "", getActivity());
		ArrayAdapter<TotalizadorEstadoMedida> totestmedAdapter = new ArrayAdapter<TotalizadorEstadoMedida>(getActivity(),
				android.R.layout.simple_spinner_item, lista_estado_medida);
		totestmedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		estado_medida.setAdapter(totestmedAdapter);
		estado_medida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				estado_medida_final = (TotalizadorEstadoMedida) parentView.getItemAtPosition(position);
				lista_observaciones.clear();
				for (TotalizadorObservaciones i : lista_obstemp) {
					if (i.getFk_estado_medida() == estado_medida_final.getId()) {
						lista_observaciones.add(i);
					}
				}
				if (lista_observaciones.size() == 0) {
					observacion_final = null;
				}
				ArrayAdapter<TotalizadorObservaciones> totobsAdapter = new ArrayAdapter<TotalizadorObservaciones>(getActivity(),
						android.R.layout.simple_spinner_item, lista_observaciones);
				observacion.setAdapter(totobsAdapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});
		TotalizadorObservacionController totobs = new TotalizadorObservacionController();
		lista_obstemp = totobs.consultar(0, 0, "", getActivity());
		lista_observaciones = new ArrayList<TotalizadorObservaciones>();
		ArrayAdapter<TotalizadorObservaciones> totobsAdapter = new ArrayAdapter<TotalizadorObservaciones>(getActivity(),
				android.R.layout.simple_spinner_item, lista_observaciones);
		totobsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		observacion.setAdapter(totobsAdapter);
		observacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				observacion_final = (TotalizadorObservaciones) parentView.getItemAtPosition(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
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

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		conexion.callback_totalizador = this;
		conexion.setActivity(getActivity());
		dialogoDinamico("Totalizador");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_totalizador, menu);
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
		if (!isFecha(pick_date.getText().toString())) {
			pasa = false;
			motivo = "Elija la fecha";
		} else if (nic.getText().toString().trim().equals("")) {
			pasa = false;
			motivo = "Ingrese el NIC";
		} else if (municipio_final == null) {
			pasa = false;
			motivo = "Elija el Municipio";
		} else if (barrio_final == null) {
			pasa = false;
			motivo = "Ingrese el barrio";
		} else if (direccion.getText().toString().trim().equals("")) {
			pasa = false;
			motivo = "Ingrese la direccion";
		} else if (ct.getText().toString().trim().equals("")) {
			pasa = false;
			motivo = "Ingrese el CT";
		} else if (mt.getText().toString().trim().equals("")) {
			pasa = false;
			motivo = "Ingrese el MT";
		} else if (estado_medida_final == null) {
			pasa = false;
			motivo = "Ingrese el Estado de la Medida";
		} else if (observacion_final == null) {
			pasa = false;
			motivo = "Ingrese la Observacion";
		}
		if (pasa) {
			iniciarGuardado(LATITUD, LONGITUD, ACURRACY);
		} else {
			Toast.makeText(getActivity(), motivo,
					Toast.LENGTH_SHORT).show();
		}

	}

	Totalizadores totalizador;
	TotalizadorController totalizController;
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

					//insertar la brigada
					totalizController = new TotalizadorController();
					totalizador = new Totalizadores();
					totalizador.setLatitud(lat);
					totalizador.setLongitud(lon);
					totalizador.setAcurracy(Float.parseFloat(acurracy));

					totalizador.setFecha(pick_date.getText().toString());
					if (nic.getText().toString().trim().equals("")) {
						totalizador.setNic(0);
					} else {
						totalizador.setNic(Integer.parseInt(nic.getText()
								.toString()));
					}
					totalizador.setFk_municipio((int) municipio_final.getId());
					totalizador.setFk_barrio((int) barrio_final.getId());
					totalizador.setFk_departamento((int) departamento_final
							.getId());

					totalizador.setDireccion(direccion.getText().toString());
					totalizador.setCt(ct.getText().toString());
					totalizador.setMt(mt.getText().toString());
					totalizador.setFk_estado_medida((int) estado_medida_final
							.getId());
					totalizador.setFk_observacion((int) observacion_final
							.getId());
					totalizador.setOtro(otro.getText().toString().trim());

					totalizador.setFk_usuario(SesionSingleton.getInstance()
							.getFk_id_operario());
					totalizador.setLast_insert(0);

					totalizController.insertar(totalizador, getActivity());
					last_insert = totalizController.getLastInsert();
					guardadoActivo = true;
					if (SesionSingleton.getInstance().isINTERNET()) {//si internet
						msg = new Message();
						msg.obj = "Enviando por internet...";
						System.out.println("Enviando por internet...");
						handlerProgreso.sendMessage(msg);
						conexion.enviarTotalizador(totalizador);
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
		pick_date.setText("Calendario");

		nic.setText("");
		departamento.setSelection(0);
		departamento_final = (Departamento) departamento.getItemAtPosition(0);
		lista_municipio.clear();
		for (Municipio i : listm) {
			if (i.getFk_departamento() == departamento_final.getId()) {
				lista_municipio.add(i);
			}
		}
		ArrayAdapter<Municipio> municipioAdapter = new ArrayAdapter<Municipio>(getActivity(),
				android.R.layout.simple_spinner_item, lista_municipio);
		municipio.setAdapter(municipioAdapter);
		municipio_final = (Municipio) municipio.getItemAtPosition(0);
		lista_barrio.clear();
		for (Barrio i : listb) {
			if (i.getFk_municipio() == municipio_final.getId()) {
				lista_barrio.add(i);
			}
		}
		ArrayAdapter<Barrio> barrioAdapter = new ArrayAdapter<Barrio>(getActivity(),
				android.R.layout.simple_spinner_item, lista_barrio);
		barrio.setAdapter(barrioAdapter);
		barrio_final = (Barrio) barrio.getItemAtPosition(0);
		direccion.setText("");

		ct.setText("");
		mt.setText("");

		estado_medida.setSelection(0);
		estado_medida_final = (TotalizadorEstadoMedida) estado_medida.getItemAtPosition(0);
		lista_observaciones.clear();
		for (TotalizadorObservaciones i : lista_obstemp) {
			if (i.getFk_estado_medida() == estado_medida_final.getId()) {
				lista_observaciones.add(i);
			}
		}
		ArrayAdapter<TotalizadorObservaciones> observAdapter = new ArrayAdapter<TotalizadorObservaciones>(getActivity(),
				android.R.layout.simple_spinner_item, lista_observaciones);
		observacion.setAdapter(observAdapter);
		otro.setText("");

		last_insert = 0;
	}

	private boolean isFecha(String fecha) {
		try {
			SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
			formatoFecha.setLenient(false);
			formatoFecha.parse(fecha);
		} catch (ParseException e) {
			return false;
		}
		return true;
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
	public void onEnviarInternetTotalizador(String result) {
		JSONObject json_data;
		try {
			json_data = new JSONObject(result);
			if(json_data.getInt("last_insert")>0){
				//ingresar el last_insert al totalizador
				ContentValues values=new ContentValues();
				values.put("last_insert", json_data.getInt("last_insert"));
				int updt=totalizController.actualizar(values, "id="+last_insert, getActivity());
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
