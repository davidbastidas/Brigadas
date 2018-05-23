package com.applus.vistas.operario.brigada;

import com.applus.controladores.BarrioController;
import com.applus.controladores.BrigadaAccionController;
import com.applus.controladores.BrigadaController;
import com.applus.controladores.BrigadaMaterialController;
import com.applus.controladores.BrigadaTrabajoController;
import com.applus.controladores.ConexionController;
import com.applus.controladores.DepartamentoController;
import com.applus.controladores.MunicipioController;
import com.applus.R;
import com.applus.modelos.Barrio;
import com.applus.modelos.BrigadaAccion;
import com.applus.modelos.BrigadaMaterialParcelable;
import com.applus.modelos.BrigadaParcelable;
import com.applus.modelos.BrigadaTrabajoParcelable;
import com.applus.modelos.Departamento;
import com.applus.modelos.Municipio;
import com.applus.modelos.SesionSingleton;
import com.applus.vistas.operario.DialogoGPS;
import com.applus.vistas.operario.DialogoGPS.OnGPSIntent;
import com.applus.vistas.operario.brigada.FormFechaHora.OnFechaHora;
import com.applus.vistas.operario.brigada.NuevoMaterial.OnAddMaterial;
import com.applus.vistas.operario.brigada.NuevoTrabajo.OnAddTrabajo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BrigadaFormFragment extends Fragment
		implements OnAddTrabajo, OnAddMaterial, OnBrigada, OnFechaHora, OnGPSIntent {

	ConexionController conexion = new ConexionController();
	private Button pick_hi, pick_hf;
	private EditText descargo, direccion, grua_hora, canasta_hora;
	private EditText km_adicional, peaje, almuerzo, hotel;
	private CheckBox festivo;
	Spinner departamento, municipio, barrio, accion;
	List<Municipio> listm = null;
	List<Departamento> lista_departamento = null;
	List<Municipio> lista_municipio = null;
	Departamento departamento_final;
	Municipio municipio_final;

	List<Barrio> listb = null;
	List<Barrio> lista_barrio = null;
	Barrio barrio_final;

	List<BrigadaAccion> lista_accion = null;
	BrigadaAccion accion_final;

	TextView t_fecha_i, t_hora_i, t_fecha_f, t_hora_f;
	BrigadaFormFragment listener = null;
	boolean fechaPress_i = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		comenzarLocalizacion();
		View rootView = inflater.inflate(R.layout.fragment_brigada, container, false);
		pick_hi = (Button) rootView.findViewById(R.id.pick_hi);
		pick_hf = (Button) rootView.findViewById(R.id.pick_hf);

		accion = (Spinner) rootView.findViewById(R.id.accion);
		barrio = (Spinner) rootView.findViewById(R.id.barrio);
		descargo = (EditText) rootView.findViewById(R.id.descargo);
		departamento = (Spinner) rootView.findViewById(R.id.departamento);
		municipio = (Spinner) rootView.findViewById(R.id.municipio);
		direccion = (EditText) rootView.findViewById(R.id.direccion);
		grua_hora = (EditText) rootView.findViewById(R.id.grua_hora);
		canasta_hora = (EditText) rootView.findViewById(R.id.canasta_hora);
		km_adicional = (EditText) rootView.findViewById(R.id.km_adicional);
		peaje = (EditText) rootView.findViewById(R.id.peaje);
		almuerzo = (EditText) rootView.findViewById(R.id.almuerzo);
		hotel = (EditText) rootView.findViewById(R.id.hotel);

		festivo = (CheckBox) rootView.findViewById(R.id.festivo);

		listener = this;
		pick_hi.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				fechaPress_i = true;
				DialogFragment nt = new FormFechaHora(listener);
				nt.show(getFragmentManager(), "fecha_hora");
			}
		});
		pick_hf.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				fechaPress_i = false;
				DialogFragment nt = new FormFechaHora(listener);
				nt.show(getFragmentManager(), "fecha_hora");
			}
		});
		t_fecha_i = (TextView) rootView.findViewById(R.id.t_fecha_i);
		t_hora_i = (TextView) rootView.findViewById(R.id.t_hora_i);
		t_fecha_f = (TextView) rootView.findViewById(R.id.t_fecha_f);
		t_hora_f = (TextView) rootView.findViewById(R.id.t_hora_f);

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
		BrigadaAccionController briacc = new BrigadaAccionController();
		lista_accion = briacc.consultar(0, 0, "", getActivity());
		ArrayAdapter<BrigadaAccion> accionAdapter = new ArrayAdapter<BrigadaAccion>(getActivity(),
				android.R.layout.simple_spinner_item, lista_accion);
		accionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		accion.setAdapter(accionAdapter);
		accion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				accion_final = (BrigadaAccion) parentView.getItemAtPosition(position);
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
		conexion.callback = this;
		conexion.setActivity(getActivity());
		dialogoDinamico("Brigada");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_brigada, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.m_nuevo_trabajo:
				DialogFragment nt = new NuevoTrabajo(this);
				nt.show(getFragmentManager(), "nuevo_trabajo");
				return true;
			case R.id.m_nuevo_material:
				DialogFragment nm = new NuevoMaterial(this);
				nm.show(getFragmentManager(), "nuevo_material");
				return true;
			case R.id.m_guardar:
				guardar();
				return true;
			default:
				break;
		}

		return false;
	}

	ArrayList<BrigadaTrabajoParcelable> b_trabajos = new ArrayList<BrigadaTrabajoParcelable>();

	@Override
	public void onSaveTrabajo(int trabajo, int estado_trabajo, String obs) {
		b_trabajos.add(new BrigadaTrabajoParcelable(0, 0, trabajo, estado_trabajo, obs));
	}

	ArrayList<BrigadaMaterialParcelable> b_materiales = new ArrayList<BrigadaMaterialParcelable>();

	@Override
	public void onSaveMaterial(int id, float cantidad) {
		b_materiales.add(new BrigadaMaterialParcelable(0, 0, id, cantidad));
	}

	@Override
	public void onSetFechaHora(String fecha, String hora) {
		// TODO Auto-generated method stub
		if (fechaPress_i) {
			fechaPress_i = false;
			t_fecha_i.setText(fecha);
			t_hora_i.setText(hora);
		} else {
			t_fecha_f.setText(fecha);
			t_hora_f.setText(hora);
		}
	}

	public void guardar() {
		String motivo = "";
		boolean pasa = true;
		if (descargo.getText().toString().trim().equals("")) {
			pasa = false;
			motivo = "Ingrese el descargo";
		} else if (!isFecha(t_fecha_i.getText().toString())) {
			pasa = false;
			motivo = "Ingrese la Fecha Inicial";
		} else if (!isHora(t_hora_i.getText().toString())) {
			pasa = false;
			motivo = "Ingrese la Hora de Inicio";
		} else if (!isFecha(t_fecha_f.getText().toString())) {
			pasa = false;
			motivo = "Ingrese la Fecha Final";
		} else if (!isHora(t_hora_f.getText().toString())) {
			pasa = false;
			motivo = "Ingrese la Hora de Final";
		} else if (municipio_final == null) {
			pasa = false;
			motivo = "Elija el Municipio";
		} else if (direccion.getText().toString().trim().equals("")) {
			pasa = false;
			motivo = "Ingrese la direccion";
		} else if (barrio_final == null) {
			pasa = false;
			motivo = "Ingrese el barrio";
		}
		if (pasa) {
			iniciarGuardado(LATITUD, LONGITUD, ACURRACY);
		} else {
			Toast.makeText(getActivity(), motivo,
					Toast.LENGTH_SHORT).show();
		}

	}

	BrigadaParcelable brigada;
	BrigadaController brigController;
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
					brigController = new BrigadaController();
					brigada = new BrigadaParcelable();
					brigada.setLatitud(lat);
					brigada.setLongitud(lon);
					brigada.setAcurracy(Float.parseFloat(acurracy));

					brigada.setDescargo(descargo.getText().toString());
					if (festivo.isChecked()) {
						brigada.setFestivo(1);
					} else {
						brigada.setFestivo(0);
					}
					String date = new SimpleDateFormat("yyyy-MM-dd")
							.format(new Date());
					brigada.setFecha(date);
					brigada.setFecha_inicio(t_fecha_i.getText().toString());
					brigada.setHora_inicio(t_hora_i.getText().toString());

					brigada.setFecha_final(t_fecha_f.getText().toString());
					brigada.setHora_final(t_hora_f.getText().toString());
					brigada.setDireccion(direccion.getText().toString());
					if (grua_hora.getText().toString().trim().equals("")) {
						brigada.setGrua_hora(0);
					} else {
						brigada.setGrua_hora(Integer.parseInt(grua_hora
								.getText().toString()));
					}
					if (canasta_hora.getText().toString().trim().equals("")) {
						brigada.setCanasta_hora(0);
					} else {
						brigada.setCanasta_hora(Integer.parseInt(canasta_hora
								.getText().toString()));
					}
					if (km_adicional.getText().toString().trim().equals("")) {
						brigada.setKm_adicional(0);
					} else {
						brigada.setKm_adicional(Integer.parseInt(km_adicional
								.getText().toString()));
					}
					if (peaje.getText().toString().trim().equals("")) {
						brigada.setPeaje(0);
					} else {
						brigada.setPeaje(Integer.parseInt(peaje.getText()
								.toString()));
					}
					if (almuerzo.getText().toString().trim().equals("")) {
						brigada.setAlmuerzo(0);
					} else {
						brigada.setAlmuerzo(Integer.parseInt(almuerzo.getText()
								.toString()));
					}
					if (hotel.getText().toString().trim().equals("")) {
						brigada.setHotel(0);
					} else {
						brigada.setHotel(Integer.parseInt(hotel.getText()
								.toString()));
					}
					brigada.setFk_municipio((int) municipio_final.getId());
					brigada.setFk_barrio((int) barrio_final.getId());
					brigada.setFk_usuario(SesionSingleton.getInstance()
							.getFk_id_operario());
					brigada.setFk_accion((int) accion_final.getId());
					brigada.setFk_departamento((int) departamento_final.getId());
					brigada.setLast_insert(0);

					brigController.insertar(brigada, getActivity());
					last_insert = brigController.getLastInsert();
					if (last_insert > 0) {
						//agregar el fkbrigada
						for (int i = 0; i < b_trabajos.size(); i++) {
							b_trabajos.get(i).setFk_brigada((int) last_insert);
						}
						for (int i = 0; i < b_materiales.size(); i++) {
							b_materiales.get(i)
									.setFk_brigada((int) last_insert);
						}
						//insertar trabajos
						BrigadaTrabajoController britra = new BrigadaTrabajoController();
						britra.insertar(b_trabajos, getActivity());
						//insertar materiales
						BrigadaMaterialController brimat = new BrigadaMaterialController();
						brimat.insertar(b_materiales, getActivity());

						guardadoActivo = true;
						if (SesionSingleton.getInstance().isINTERNET()) {//si internet
							msg = new Message();
							msg.obj = "Enviando por internet...";
							System.out.println("Enviando por internet...");
							handlerProgreso.sendMessage(msg);
							conexion.enviarBrigada(brigada, b_trabajos,
									b_materiales);
						} else {
							System.out.println("Limpiando");
							limpiar();
						}
					} else {
						System.out.println("No se guardo");
						Toast.makeText(getActivity(), "No se guardo",
								Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				/*myHandler.removeCallbacks(runnable);
				if (locListener != null) {
					locManager.removeUpdates(locListener);
					DialogFragment nt = new DialogoGPS(listener);
	            	nt.show(getFragmentManager(), "gps");
				}
				*/
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
		t_fecha_i.setText("0000-00-00");
		t_hora_i.setText("00:00");
		t_fecha_f.setText("0000-00-00");
		t_hora_f.setText("00:00");
		//pick_hi.setText("HH:II");
		//pick_hf.setText("HH:FF");

		descargo.setText("");
		accion.setSelection(0);
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
		grua_hora.setText("");
		canasta_hora.setText("");
		km_adicional.setText("");
		peaje.setText("");
		almuerzo.setText("");
		hotel.setText("");

		festivo.setChecked(false);
		b_trabajos.clear();
		b_materiales.clear();
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

	private boolean isHora(String hora) {
		try {
			SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm", Locale.getDefault());
			formatoHora.setLenient(false);
			formatoHora.parse(hora);
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
		        	 iniciarGuardado("0.0","0.0","0");
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
	public void onEnviarInternetBrigada(String result) {
		JSONObject json_data;
		try {
			json_data = new JSONObject(result);
			if(json_data.getInt("last_insert")>0){
				//ingresar el last_insert a la brigada
				ContentValues values=new ContentValues();
				values.put("last_insert", json_data.getInt("last_insert"));
				int updt=brigController.actualizar(values, "id="+last_insert, getActivity());
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
