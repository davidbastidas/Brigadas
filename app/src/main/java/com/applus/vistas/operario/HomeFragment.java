package com.applus.vistas.operario;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applus.R;
import com.applus.controladores.BrigadaAccionController;
import com.applus.controladores.CensoController;
import com.applus.controladores.ClientesController;
import com.applus.controladores.ConexionController;
import com.applus.controladores.DepartamentoController;
import com.applus.controladores.EstadoTrabajoController;
import com.applus.controladores.MaterialController;
import com.applus.controladores.MunicipioController;
import com.applus.controladores.NovedadController;
import com.applus.controladores.NovedadObservacionController;
import com.applus.controladores.NovedadTipoController;
import com.applus.controladores.TotalizadorController;
import com.applus.controladores.TotalizadorEstadoMedidaController;
import com.applus.controladores.TotalizadorObservacionController;
import com.applus.controladores.TrabajosController;
import com.applus.controladores.interfaces.AsyncResponse;
import com.applus.modelos.BrigadaAccion;
import com.applus.modelos.Departamento;
import com.applus.modelos.EstadoTrabajo;
import com.applus.modelos.Material;
import com.applus.modelos.Municipio;
import com.applus.modelos.NovedadObservacion;
import com.applus.modelos.NovedadTipo;
import com.applus.modelos.SesionSingleton;
import com.applus.modelos.TotalizadorEstadoMedida;
import com.applus.modelos.TotalizadorObservaciones;
import com.applus.modelos.Trabajos;
import com.applus.vistas.operario.brigada.InterfaceTareasLargas;
import com.applus.vistas.operario.brigada.OnBrigada;
import com.applus.vistas.operario.brigada.TareasLargas;
import com.applus.vistas.operario.censo.OnCenso;
import com.applus.vistas.operario.clientes.OnCliente;
import com.applus.vistas.operario.novedad.OnNovedad;
import com.applus.vistas.operario.totalizadores.OnTotalizador;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment implements AsyncResponse, InterfaceTareasLargas, OnBrigada, OnTotalizador, OnNovedad, OnCenso, OnCliente {
	
	private TextView estado,T_CONS_PROCESO,envio, acercade;
	ConexionController conexion;
	Activity activity;
	SesionSingleton se=SesionSingleton.getInstance();
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
	    T_CONS_PROCESO = (TextView) rootView.findViewById(R.id.T_CONS_PROCESO);
	    estado = (TextView) rootView.findViewById(R.id.estado);
	    envio = (TextView) rootView.findViewById(R.id.envio);
		acercade = (TextView) rootView.findViewById(R.id.acercade);
	    estado.setText(se.getEstado_datos());
	    envio.setText(se.getEstado_envio());
		acercade.setText(se.getAcercade());
	    conexion=new ConexionController();
		conexion.callback_get=this;
		conexion.callback=this;
		conexion.callback_totalizador=this;
		conexion.callback_novedad=this;
		conexion.callback_censo = this;
		conexion.callback_cliente = this;
		conexion.setActivity(activity);
	    mostrarReporte();
		acercaDe();
        return rootView;
    }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		activity=getActivity();
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.home, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.datostablas:
			DialogFragment nt = new DialogDatosTablas(this);
        	nt.show(getFragmentManager(), "nuevo_dialogo");
			return true;
		case R.id.sincronizar:
			progressDialog("Enviando");
			msg = new Message();
			msg.obj = "Espere...";
			handlerProgreso.sendMessage(msg);
			conexion.enviarBrigadaMasiva();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	private void mostrarReporte() {
		TotalizadorController tot = new TotalizadorController();
		NovedadController nov = new NovedadController();
		CensoController cen = new CensoController();
		ClientesController cliente = new ClientesController();
		T_CONS_PROCESO.setText(
				"Totalizadores Realizados= "+tot.count("", activity)+"\n"+
				"Totalizadores Enviados= "+tot.count("last_insert>0", activity)+"\n"+
				"Novedades Realizados= "+nov.count("", activity)+"\n"+
				"Novedades Enviados= "+nov.count("last_insert>0", activity)+"\n"+
				"Censos Realizados= "+cen.count("", activity)+"\n"+
				"Censos Enviados= "+cen.count("last_insert>0", activity) +"\n"+
				"Clientes Actualizados= "+cliente.countAActualizar("", activity)+"\n"+
				"Clientes Actualizados Enviados= "+cliente.countAActualizar("last_insert>0", activity)
				);
	}

	private void acercaDe(){
		acercade.setText(
			"GeoDato Version 1.3 2018-11-06 08:00:00\n" +
			"VERSION DE ZONA: " + se.getVersionClientes() + "\n" +
			"BARRIOS DESCARGADOS: "
		);
	}
	////procesos masivos
	
	@Override
	public void onEnviarInternetBrigada(String result) {
		conexion.enviarTotalizadorMasivo();
	}
	@Override
	public void onEnviarInternetTotalizador(String result) {
		conexion.enviarNovedadMasivo();
	}
	@Override
	public void onEnviarInternetNovedad(String result) {
		conexion.enviarCensoMasivo();
	}
	@Override
	public void onEnviarInternetCenso(String result) {
		conexion.enviarClientesAActualizar();
	}
	@Override
	public void onEnviarInternetCliente(String result) {
		//termino censo masivo
		mostrarReporte();
		envio.setText("");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		envio.setText("Ultimo Envio: "+currentDateandTime);

		//*/*/*/*//*/
		SharedPreferences preferencias = getActivity().getSharedPreferences(
				"configuracion", Context.MODE_PRIVATE);
		Editor editor = preferencias.edit();
		editor.putString("estado_envio", envio.getText().toString());
		editor.commit();

		se.setEstado_envio(envio.getText().toString());
		/*/*///*/*//*/

		progressDialog.dismiss();
	}
	@Override
	public void onConsultaCliente(String output) {
		// TODO Auto-generated method stub
		
	}
	/////finnnn
	@Override
	public void processFinishLogin(String output) {
		// no se hace nada
	}
	@Override
	public void onBajarDatosTablas(boolean respuesta) {
		if(respuesta){
			progressDialog("Conectando");
			msg = new Message();
			msg.obj = "Espere...";
			handlerProgreso.sendMessage(msg);
			estado.setText("Bajando Trabajos...");
			conexion.getTrabajos(""+SesionSingleton.getInstance().getFk_id_operario());
		}
	}
	@Override
	public void onTablaTrabajos(String output) {
		JSONObject json_data;
		TrabajosController trab=new TrabajosController();
		try {
			json_data = new JSONObject(output);
			JSONArray trabajos = json_data.getJSONArray("trabajos");
			ArrayList<Trabajos> values=new ArrayList<Trabajos>();
			int size = trabajos.length();
			for (int i = 0; i < size; ++i) {
			    JSONObject tr = trabajos.getJSONObject(i);
			    values.add(new Trabajos(tr.getLong("id"), tr.getString("nombre")));
			}
			if(values.size()>0){
				trab.eliminar("", activity);
				trab.insertar(values, activity);
			}
			estado.setText(estado.getText()+"OK.\n"+"Bajando los Materiales...");
			conexion.getMateriales(""+SesionSingleton.getInstance().getFk_id_operario());
		} catch (Exception e) {
			System.out.println("Exception onTablaTrabajos= "+e);
			estado.setText(estado.getText()+"ERROR.\n"+"Bajando los Materiales...");
			conexion.getMateriales(""+SesionSingleton.getInstance().getFk_id_operario());
		}
	}
	@Override
	public void onTablaMateriales(String output) {
		JSONObject json_data;
		MaterialController mat=new MaterialController();
		try {
			json_data = new JSONObject(output);
			JSONArray materiales = json_data.getJSONArray("materiales");
			ArrayList<Material> values=new ArrayList<Material>();
			int size = materiales.length();
			for (int i = 0; i < size; ++i) {
			    JSONObject tr = materiales.getJSONObject(i);
			    values.add(new Material(
			    		tr.getLong("id"),
			    		tr.getString("nombre"),
			    		tr.getString("medida"),
			    		0));
			}
			if(values.size()>0){
				mat.eliminar("", activity);
				mat.insertar(values, activity);
			}
			estado.setText(estado.getText()+"OK.\n"+"Bajando los Estado Trabajos...");
			conexion.getEstadoTrabajo(""+SesionSingleton.getInstance().getFk_id_operario());
		} catch (Exception e) {
			System.out.println("Exception onTablaMateriales= "+e);
			estado.setText(estado.getText()+"ERROR.\n"+"Bajando los Estado Trabajos...");
			conexion.getEstadoTrabajo(""+SesionSingleton.getInstance().getFk_id_operario());
		}
	}
	@Override
	public void onTablaEstadoTrabajo(String output) {
		JSONObject json_data;
		EstadoTrabajoController estadot=new EstadoTrabajoController();
		try {
			json_data = new JSONObject(output);
			JSONArray estados_trabajos = json_data.getJSONArray("estado_trabajo");
			ArrayList<EstadoTrabajo> values=new ArrayList<EstadoTrabajo>();
			int size = estados_trabajos.length();
			for (int i = 0; i < size; ++i) {
			    JSONObject tr = estados_trabajos.getJSONObject(i);
			    values.add(new EstadoTrabajo(
			    		tr.getLong("id"),
			    		tr.getString("nombre")));
			}
			if(values.size()>0){
				estadot.eliminar("", activity);
				estadot.insertar(values, activity);
			}
			estado.setText(estado.getText()+"OK.\n"+"Bajando los Departamentos...");
			conexion.getDepartamento(""+SesionSingleton.getInstance().getFk_id_operario());
		} catch (Exception e) {
			System.out.println("Exception onTablaEstadoTrabajo= "+e);
			estado.setText(estado.getText()+"ERROR.\n"+"Bajando los Departamentos...");
			conexion.getDepartamento(""+SesionSingleton.getInstance().getFk_id_operario());
		}
		
	}
	
	@Override
	public void onTablaDepartamento(String output) {
		JSONObject json_data;
		DepartamentoController dep=new DepartamentoController();
		try {
			json_data = new JSONObject(output);
			JSONArray departamentos = json_data.getJSONArray("departamento");
			ArrayList<Departamento> values=new ArrayList<Departamento>();
			int size = departamentos.length();
			for (int i = 0; i < size; ++i) {
			    JSONObject tr = departamentos.getJSONObject(i);
			    values.add(new Departamento(
			    		tr.getLong("id"),
			    		tr.getString("nombre"),
						tr.getString("abreviatura"),
						tr.getString("ruta_archivo"),
						tr.getString("version"),
						tr.getString("ruta_archivo_nic")
				));
			    if(tr.getLong("id") == SesionSingleton.getInstance().getFkDistrito()){
					SesionSingleton.getInstance().setUrlClientes(tr.getString("ruta_archivo"));
					SesionSingleton.getInstance().setUrlNics(tr.getString("ruta_archivo_nic"));
				}
			}
			if(values.size()>0){
				dep.eliminar("", activity);
				dep.insertar(values, activity);
			}
			estado.setText(estado.getText()+"OK.\n"+"Bajando los Municipios...");
			conexion.getMunicipio(""+SesionSingleton.getInstance().getFk_id_operario());
		} catch (Exception e) {
			System.out.println("Exception onTablaDepartamento= "+e);
			estado.setText(estado.getText()+"ERROR.\n"+"Bajando los Municipios...");
			conexion.getMunicipio(""+SesionSingleton.getInstance().getFk_id_operario());
		}
		
	}

	@Override
	public void onTablaMunicipio(String output) {
		JSONObject json_data;
		MunicipioController muni=new MunicipioController();
		try {
			json_data = new JSONObject(output);
			JSONArray municipios = json_data.getJSONArray("municipios");
			ArrayList<Municipio> values=new ArrayList<Municipio>();
			int size = municipios.length();
			for (int i = 0; i < size; ++i) {
			    JSONObject tr = municipios.getJSONObject(i);
			    values.add(new Municipio(
			    		tr.getLong("id"),
			    		tr.getString("nombre"),
			    		tr.getInt("fk_departamento")));
			}
			if(values.size()>0){
				muni.eliminar("", activity);
				muni.insertar(values, activity);
			}
			estado.setText(estado.getText()+"OK.\n"+"Bajando los Barrios...");
			conexion.getBarrios(""+SesionSingleton.getInstance().getFk_id_operario());
		} catch (Exception e) {
			System.out.println("Exception onTablaMunicipio= "+e);
			estado.setText(estado.getText()+"ERROR.\n"+"Bajando los Barrios...");
			conexion.getBarrios(""+SesionSingleton.getInstance().getFk_id_operario());
		}
		
	}
	@Override
	public void onTablaBarrios(String output) {
		TareasLargas tarea = new TareasLargas();
		tarea.delegate=this;
		tarea.execute(new Object[] {"insertarBarrios",output,activity});
	}
	@Override
	public void onBarriosInsert(boolean esta, String output) {
		try {
			if(!esta){
				estado.setText(estado.getText()+output);
			}
			estado.setText(estado.getText()+"OK.\n"+"Bajando los Estado de Medida de Totalizadores...");
			conexion.getTotalizadorEstadoMedida(""+SesionSingleton.getInstance().getFk_id_operario());
		} catch (Exception e) {
			estado.setText(estado.getText()+"ERROR.\n"+"Bajando los Estado de Medida de Totalizadores...");
			conexion.getTotalizadorEstadoMedida(""+SesionSingleton.getInstance().getFk_id_operario());
		}
		
	}
	@Override
	public void onTablaTotalizadorEstadoMedida(String output) {
		JSONObject json_data;
		TotalizadorEstadoMedidaController estado_medida=new TotalizadorEstadoMedidaController();
		try {
			json_data = new JSONObject(output);
			JSONArray estados = json_data.getJSONArray("estado_medidas");
			ArrayList<TotalizadorEstadoMedida> values=new ArrayList<TotalizadorEstadoMedida>();
			int size = estados.length();
			for (int i = 0; i < size; ++i) {
			    JSONObject tr = estados.getJSONObject(i);
			    values.add(new TotalizadorEstadoMedida(
			    		tr.getLong("id"),
			    		tr.getString("nombre")));
			}
			if(values.size()>0){
				estado_medida.eliminar("", activity);
				estado_medida.insertar(values, activity);
			}
			estado.setText(estado.getText()+"OK.\n"+"Bajando las Observaciones de Totalizadores...");
			conexion.getTotalizadorObservacion(""+SesionSingleton.getInstance().getFk_id_operario());
		} catch (Exception e) {
			System.out.println("Exception onTablaTotalizadorEstadoMedida= "+e);
			estado.setText(estado.getText()+"ERROR.\n"+"Bajando las Observaciones de Totalizadores...");
			conexion.getTotalizadorObservacion(""+SesionSingleton.getInstance().getFk_id_operario());
		}
		
	}
	@Override
	public void onTablaTotalizadorObservacion(String output) {
		JSONObject json_data;
		TotalizadorObservacionController observacion=new TotalizadorObservacionController();
		try {
			json_data = new JSONObject(output);
			JSONArray observaciones = json_data.getJSONArray("observaciones");
			ArrayList<TotalizadorObservaciones> values=new ArrayList<TotalizadorObservaciones>();
			int size = observaciones.length();
			for (int i = 0; i < size; ++i) {
			    JSONObject tr = observaciones.getJSONObject(i);
			    values.add(new TotalizadorObservaciones(
			    		tr.getLong("id"),
			    		tr.getString("nombre"),
			    		tr.getInt("fk_estado_medida")));
			}
			if(values.size()>0){
				observacion.eliminar("", activity);
				observacion.insertar(values, activity);
			}
			estado.setText(estado.getText()+"OK.\n"+"Bajando los Tipos de Novedad...");
			conexion.getNovedadTipoNovedad(""+SesionSingleton.getInstance().getFk_id_operario());
		} catch (Exception e) {
			System.out.println("Exception onTablaTotalizadorObservacion= "+e);
			estado.setText(estado.getText()+"ERROR.\n"+"Bajando los Tipos de Novedad...");
			conexion.getNovedadTipoNovedad(""+SesionSingleton.getInstance().getFk_id_operario());
		}
		
	}
	@Override
	public void onTablaNovedadTipoNovedad(String output) {
		JSONObject json_data;
		NovedadTipoController novedad=new NovedadTipoController();
		try {
			json_data = new JSONObject(output);
			JSONArray novedades = json_data.getJSONArray("novedades");
			ArrayList<NovedadTipo> values=new ArrayList<NovedadTipo>();
			int size = novedades.length();
			for (int i = 0; i < size; ++i) {
			    JSONObject tr = novedades.getJSONObject(i);
			    values.add(new NovedadTipo(
			    		tr.getLong("id"),
			    		tr.getString("nombre")));
			}
			if(values.size()>0){
				novedad.eliminar("", activity);
				novedad.insertar(values, activity);
			}
			estado.setText(estado.getText()+"OK.\n"+"Bajando las Observaciones de Novedad...");
			conexion.getNovedadObservacion(""+SesionSingleton.getInstance().getFk_id_operario());
		} catch (Exception e) {
			System.out.println("Exception onTablaNovedadTipoNovedad= "+e);
			estado.setText(estado.getText()+"ERROR.\n"+"Bajando las Observaciones de Novedad...");
			conexion.getNovedadObservacion(""+SesionSingleton.getInstance().getFk_id_operario());
		}
		
	}
	@Override
	public void onTablaNovedadObservacion(String output) {
		JSONObject json_data;
		NovedadObservacionController novedad_obs=new NovedadObservacionController();
		try {
			json_data = new JSONObject(output);
			JSONArray observaciones = json_data.getJSONArray("observaciones");
			ArrayList<NovedadObservacion> values=new ArrayList<NovedadObservacion>();
			int size = observaciones.length();
			for (int i = 0; i < size; ++i) {
			    JSONObject tr = observaciones.getJSONObject(i);
			    values.add(new NovedadObservacion(
			    		tr.getLong("id"),
			    		tr.getString("nombre")));
			}
			if(values.size()>0){
				novedad_obs.eliminar("", activity);
				novedad_obs.insertar(values, activity);
			}
			estado.setText(estado.getText()+"OK.\n"+"Bajando las Acciones de Brigadas...");
			conexion.getBrigadaAccion(""+SesionSingleton.getInstance().getFk_id_operario());
		} catch (Exception e) {
			System.out.println("Exception onTablaNovedadObservacion= "+e);
			estado.setText(estado.getText()+"ERROR.\n"+"Bajando las Acciones de Brigadas...");
			conexion.getBrigadaAccion(""+SesionSingleton.getInstance().getFk_id_operario());
		}
		
	}
	@Override
	public void onTablaBrigadaAccion(String output) {
		JSONObject json_data;
		BrigadaAccionController acciones=new BrigadaAccionController();
		try {
			json_data = new JSONObject(output);
			JSONArray obs_accion = json_data.getJSONArray("acciones");
			ArrayList<BrigadaAccion> values=new ArrayList<BrigadaAccion>();
			int size = obs_accion.length();
			for (int i = 0; i < size; ++i) {
			    JSONObject tr = obs_accion.getJSONObject(i);
			    values.add(new BrigadaAccion(
			    		tr.getLong("id"),
			    		tr.getString("nombre")));
			}
			if(values.size()>0){
				acciones.eliminar("", activity);
				acciones.insertar(values, activity);
			}

			estado.setText(estado.getText()+"OK.\n"+"Bajando el formulario de censos...");
			conexion.getFormularioCenso(""+SesionSingleton.getInstance().getFk_id_operario());
		} catch (Exception e) {
			System.out.println("Exception onTablaBrigadaAccion= "+e);
			estado.setText(estado.getText()+"ERROR.\n"+"Bajando el formulario de censos...");
			conexion.getFormularioCenso(""+SesionSingleton.getInstance().getFk_id_operario());
		}
	}
	@Override
	public void onDescargarFormularioCenso(String output) {
		JSONObject json_data;
		CensoController acciones=new CensoController();
		try {
			json_data = new JSONObject(output);
			JSONArray obs_accion = json_data.getJSONArray("items");

			acciones.eliminarFormulario(activity);
			acciones.insertarFormulario(output, activity);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDateandTime = sdf.format(new Date());
			estado.setText(estado.getText()+"OK.\n"+"Ultima Actualizacion: "+currentDateandTime);
		} catch (Exception e) {
			System.out.println("Exception onTablaBrigadaAccion= "+e);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDateandTime = sdf.format(new Date());
			estado.setText(estado.getText()+"ERROR.\n"+"Ultima Actualizacion: "+currentDateandTime);
		}
		//*/*/*/*//*/
		SharedPreferences preferencias = getActivity().getSharedPreferences(
				"configuracion", Context.MODE_PRIVATE);
		Editor editor = preferencias.edit();
		editor.putString("estado_datos", estado.getText().toString());
		editor.commit();
		se.setEstado_datos(estado.getText().toString());
		/*/*///*/*//*/
		progressDialog.dismiss();
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
		progressDialog = ProgressDialog.show(activity, nombreActividad,
				"Espere...", true);
	}
}
