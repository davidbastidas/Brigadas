package com.applus.controladores;
import com.applus.controladores.interfaces.AsyncResponse;
import com.applus.R;
import com.applus.modelos.BrigadaMaterialParcelable;
import com.applus.modelos.BrigadaParcelable;
import com.applus.modelos.BrigadaTrabajoParcelable;
import com.applus.modelos.Censo;
import com.applus.modelos.Novedades;
import com.applus.modelos.SesionSingleton;
import com.applus.modelos.Totalizadores;
import com.applus.modelos.Usuario;
import com.applus.vistas.operario.OperarioActivity;
import com.applus.vistas.operario.brigada.OnBrigada;
import com.applus.vistas.operario.censo.OnCenso;
import com.applus.vistas.operario.novedad.OnNovedad;
import com.applus.vistas.operario.totalizadores.OnTotalizador;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class ConexionController implements AsyncResponse,OnBrigada,OnTotalizador,OnNovedad, OnCenso	{
	
	public AsyncResponse callback_get=null;
	public OnBrigada callback=null;
	public OnTotalizador callback_totalizador=null;
	public OnNovedad callback_novedad=null;
	public OnCenso callback_censo=null;
	Activity activity;
	SesionSingleton sesion;
	String user="";
	public ConexionController(){
		sesion=SesionSingleton.getInstance();
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	public void loginUser(String user, String pass){
		progressDialog("Conectando");
		msg = new Message();
		msg.obj = "Espere...";
		handlerProgreso.sendMessage(msg);
		String accionJava = "loginUser";
		WebServiceTaskGET asyncTask = new WebServiceTaskGET();
		asyncTask.delegate = this;
		this.user=user;
		asyncTask.execute(new String[] {
        		accionJava,
            	user,
            	pass
		});
	}
	public void processFinishLogin(String output){
		//this you will received result fired from async class of onPostExecute(result) method.	
		System.out.println("processFinishLogin= "+output);
		SesionSingleton se=SesionSingleton.getInstance();
		UsuarioController usu=new UsuarioController();
		try {
			JSONObject json_data = new JSONObject(output);
			if(json_data.getInt("fk_id")>0){
				se.setPasaLogin(true);
				se.setTipo_usuario(json_data.getInt("tipo"));
				if(usu.count("", activity)>0){
					ContentValues registro=new ContentValues();
					registro.put("nombre", json_data.getString("nombre"));
					registro.put("fk_id", json_data.getInt("fk_id"));
					registro.put("nickname", json_data.getString("nickname"));
					usu.actualizar(registro, "id=1", activity);
					se.setFk_id_operario(json_data.getInt("fk_id"));
					System.out.println("fk_id: "+se.getFk_id_operario());
					se.setNombreOperario(json_data.getString("nombre"));
				}else{
					usu.insertar(new Usuario(1, 
									json_data.getString("nombre"), 
									json_data.getInt("fk_id"),
									json_data.getString("nickname"),
									json_data.getInt("tipo")
									), activity);
					se.setFk_id_operario(json_data.getInt("fk_id"));
					System.out.println("fk_id: "+se.getFk_id_operario());
					se.setNombreOperario(json_data.getString("nombre"));
				}
				System.out.println("FK USUARIO:"+json_data.getInt("fk_id") );
				progressDialog.dismiss();
				Toast.makeText(activity, "Login OK",
						Toast.LENGTH_SHORT).show();
				Intent intentar = new Intent(activity, OperarioActivity.class);
				activity.startActivity(intentar);
			} else {
				Usuario usuario=usu.consultar(0, 0, "id=1", activity);
				if(usuario!=null){
					if(usuario.getNickname().equals(this.user)){
						System.out.println("FK USUARIO:"+usuario.getFk_id());
						se.setFk_id_operario(usuario.getFk_id());
						se.setNombreOperario(usuario.getNombre());
						se.setTipo_usuario(usuario.getTipo());
						se.setPasaLogin(true);
						progressDialog.dismiss();
						Toast.makeText(activity, "Verifica tu conexion, no estas Online",
								Toast.LENGTH_LONG).show();
						Intent intentar = new Intent(activity, OperarioActivity.class);
						activity.startActivity(intentar);
					}
				}else{
					se.setPasaLogin(false);
					Toast.makeText(activity, "No se ha encontrado este Usuario en el telefono, Intenta de nuevo",
							Toast.LENGTH_LONG).show();
				}
				progressDialog.dismiss();
			}
		} catch (Exception e) {
			System.out.println("Exception: "+e);
			Toast.makeText(activity, "Verifica tu conexion, o la Configuracion", Toast.LENGTH_LONG).show();
			
			Usuario usuario=usu.consultar(0, 0, "id=1", activity);
			System.out.println("usuario: "+usuario);
			if(usuario!=null){
				System.out.println("usuario.getNickname(): "+usuario.getNickname());
				System.out.println("this.user: "+this.user);
				if(usuario.getNickname().equals(this.user)){
					System.out.println("FK USUARIO:"+usuario.getFk_id());
					se.setFk_id_operario(usuario.getFk_id());
					se.setNombreOperario(usuario.getNombre());
					se.setTipo_usuario(usuario.getTipo());
					se.setPasaLogin(true);
					progressDialog.dismiss();
					Toast.makeText(activity, "Verifica tu conexion, no estas Online",
							Toast.LENGTH_LONG).show();
					Intent intentar = new Intent(activity, OperarioActivity.class);
					activity.startActivity(intentar);
				}else{
					Toast.makeText(activity, "No se ha encontrado este Usuario en el telefono, Intenta de nuevo",
							Toast.LENGTH_LONG).show();
				}
			}else{
				se.setPasaLogin(false);
				Toast.makeText(activity, "Debes conectar a internet para Logueo.",
						Toast.LENGTH_SHORT).show();
			}
			progressDialog.dismiss();
		}
    }
	public void enviarBrigada(BrigadaParcelable brigada,
							  ArrayList<BrigadaTrabajoParcelable> britrab,
							  ArrayList<BrigadaMaterialParcelable> brimat){
		String accionJava = "enviarBrigada";
		WebServiceTaskSET asyncTask = new WebServiceTaskSET();
		asyncTask.callback = this;
		asyncTask.execute(new Object[] {
        		accionJava,
        		brigada,
        		britrab,
        		brimat
		});
	}
	public void enviarBrigadaMasiva(){
			String accionJava = "enviarBrigadaMasiva";
			WebServiceTaskSET asyncTask = new WebServiceTaskSET();
			asyncTask.callback = this;
			asyncTask.execute(new Object[] {
			accionJava,
			activity
			});
	}
	@Override
	public void onEnviarInternetBrigada(String result) {
		System.out.println("onEnviarInternetBrigada= "+result);
		callback.onEnviarInternetBrigada(result);
	}
	public void enviarTotalizador(Totalizadores totalizador){
				String accionJava = "enviarTotalizador";
				WebServiceTaskSET asyncTask = new WebServiceTaskSET();
				asyncTask.callback_totalizador = this;
				asyncTask.execute(new Object[] {
					accionJava,
					totalizador
				});
	}
	public void enviarTotalizadorMasivo(){
		String accionJava = "enviarTotalizadorMasivo";
		WebServiceTaskSET asyncTask = new WebServiceTaskSET();
		asyncTask.callback_totalizador = this;
		asyncTask.execute(new Object[] {
			accionJava,
			activity
		});
}
	@Override
	public void onEnviarInternetTotalizador(String result) {
		System.out.println("onEnviarInternetTotalizador= "+result);
		callback_totalizador.onEnviarInternetTotalizador(result);
	}
	
	public void enviarNovedad(Novedades novedad){
			String accionJava = "enviarNovedad";
			WebServiceTaskSET asyncTask = new WebServiceTaskSET();
			asyncTask.callback_novedad = this;
			asyncTask.execute(new Object[] {
				accionJava,
				novedad
			});
	}
	
	public void enviarNovedadMasivo(){
		String accionJava = "enviarNovedadMasivo";
		WebServiceTaskSET asyncTask = new WebServiceTaskSET();
		asyncTask.callback_novedad = this;
		asyncTask.execute(new Object[] {
			accionJava,
			activity
		});
	}

	public void enviarCenso(Censo censo){
		/*String accionJava = "enviarNovedad";
		WebServiceTaskSET asyncTask = new WebServiceTaskSET();
		asyncTask.callback_novedad = this;
		asyncTask.execute(new Object[] {
				accionJava,
				novedad
		});*/
	}
	
	@Override
	public void onEnviarInternetNovedad(String result) {
		System.out.println("onEnviarInternetNovedad= "+result);
		callback_novedad.onEnviarInternetNovedad(result);
	}
	
	public void getTrabajos(String user){
		String accionJava = "getTrabajo";
		WebServiceTaskGET asyncTask = new WebServiceTaskGET();
		asyncTask.delegate = this;
		this.user=user;
		asyncTask.execute(new String[] {
        		accionJava,
            	user
		});
	}
	
	@Override
	public void onTablaTrabajos(String output) {
		System.out.println("onTablaTrabajos= "+output);
		callback_get.onTablaTrabajos(output);
	}
	
	public void getMateriales(String user){
		String accionJava = "getMateriales";
		WebServiceTaskGET asyncTask = new WebServiceTaskGET();
		asyncTask.delegate = this;
		this.user=user;
		asyncTask.execute(new String[] {
        		accionJava,
            	user
		});
	}
	
	@Override
	public void onTablaMateriales(String output) {
		System.out.println("onTablaMateriales= "+output);
		callback_get.onTablaMateriales(output);
	}
	
	public void getEstadoTrabajo(String user){
		String accionJava = "getEstadoTrabajo";
		WebServiceTaskGET asyncTask = new WebServiceTaskGET();
		asyncTask.delegate = this;
		this.user=user;
		asyncTask.execute(new String[] {
        		accionJava,
            	user
		});
	}
	
	@Override
	public void onTablaEstadoTrabajo(String output) {
		System.out.println("onTablaEstadoTrabajo= "+output);
		callback_get.onTablaEstadoTrabajo(output);
	}
	
	public void getDepartamento(String user){
		String accionJava = "getDepartamento";
		WebServiceTaskGET asyncTask = new WebServiceTaskGET();
		asyncTask.delegate = this;
		this.user=user;
		asyncTask.execute(new String[] {
        		accionJava,
            	user
		});
	}
	
	@Override
	public void onTablaDepartamento(String output) {
		System.out.println("onTablaDepartamento= "+output);
		callback_get.onTablaDepartamento(output);
	}
	
	public void getMunicipio(String user){
		String accionJava = "getMunicipio";
		WebServiceTaskGET asyncTask = new WebServiceTaskGET();
		asyncTask.delegate = this;
		this.user=user;
		asyncTask.execute(new String[] {
        		accionJava,
            	user
		});
	}
	
	@Override
	public void onTablaMunicipio(String output) {
		System.out.println("onTablaMunicipio= "+output);
		callback_get.onTablaMunicipio(output);
	}
	
	public void getCliente(String user,String codigo){
		String accionJava = "getCliente";
		WebServiceTaskGET asyncTask = new WebServiceTaskGET();
		asyncTask.delegate_novedad = this;
		this.user=user;
		asyncTask.execute(new String[] {
        		accionJava,
            	user,
            	codigo
		});
	}
	
	@Override
	public void onConsultaCliente(String output) {
		System.out.println("onTablaMunicipio= "+output);
		callback_novedad.onConsultaCliente(output);
	}
	
	public void getBarrios(String user){
		String accionJava = "getBarrios";
		WebServiceTaskGET asyncTask = new WebServiceTaskGET();
		asyncTask.delegate = this;
		this.user=user;
		asyncTask.execute(new String[] {
        		accionJava,
            	user
		});
	}
	
	@Override
	public void onTablaBarrios(String output) {
		System.out.println("onTablaBarrios= "+output);
		callback_get.onTablaBarrios(output);
	}
	
	public void getBrigadaAccion(String user){
		String accionJava = "getBrigadaAccion";
		WebServiceTaskGET asyncTask = new WebServiceTaskGET();
		asyncTask.delegate = this;
		this.user=user;
		asyncTask.execute(new String[] {
        		accionJava,
            	user
		});
	}
	
	@Override
	public void onTablaBrigadaAccion(String output) {
		System.out.println("onTablaBrigadaAccion= "+output);
		callback_get.onTablaBrigadaAccion(output);
	}
	
	public void getTotalizadorEstadoMedida(String user){
		String accionJava = "getTotalizadorEstadoMedida";
		WebServiceTaskGET asyncTask = new WebServiceTaskGET();
		asyncTask.delegate = this;
		this.user=user;
		asyncTask.execute(new String[] {
        		accionJava,
            	user
		});
	}
	
	@Override
	public void onTablaTotalizadorEstadoMedida(String output) {
		System.out.println("onTablaTotalizadorEstadoMedida= "+output);
		callback_get.onTablaTotalizadorEstadoMedida(output);
	}
	
	public void getTotalizadorObservacion(String user){
		String accionJava = "getTotalizadorObservacion";
		WebServiceTaskGET asyncTask = new WebServiceTaskGET();
		asyncTask.delegate = this;
		this.user=user;
		asyncTask.execute(new String[] {
        		accionJava,
            	user
		});
	}
	
	@Override
	public void onTablaTotalizadorObservacion(String output) {
		System.out.println("onTablaTotalizadorObservacion= "+output);
		callback_get.onTablaTotalizadorObservacion(output);
	}
	
	public void getNovedadTipoNovedad(String user){
		String accionJava = "getNovedadTipoNovedad";
		WebServiceTaskGET asyncTask = new WebServiceTaskGET();
		asyncTask.delegate = this;
		this.user=user;
		asyncTask.execute(new String[] {
        		accionJava,
            	user
		});
	}
	
	@Override
	public void onTablaNovedadTipoNovedad(String output) {
		System.out.println("onTablaNovedadTipoNovedad= "+output);
		callback_get.onTablaNovedadTipoNovedad(output);
	}
	
	public void getNovedadObservacion(String user){
		String accionJava = "getNovedadObservacion";
		WebServiceTaskGET asyncTask = new WebServiceTaskGET();
		asyncTask.delegate = this;
		this.user=user;
		asyncTask.execute(new String[] {
        		accionJava,
            	user
		});
	}
	
	@Override
	public void onTablaNovedadObservacion(String output) {
		System.out.println("onTablaNovedadObservacion= "+output);
		callback_get.onTablaNovedadObservacion(output);
	}
	
	//************************//////
	public String FormatDate(String fecha) {
		//String originalString = "2010-07-14 09:00:02";
		Date date;
		String newString = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fecha);
			newString = new SimpleDateFormat("yyyy-MM-dd").format(date); // yyyy-MM-dd
		} catch (ParseException e) {
			System.out.println("ParseException= "+e);
		}
		
        return newString;
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
	public void dialogoDinamico(String titulo, final String mensaje) {
		dialog=null;handlerDialog=null;
		handlerDialog = new Handler() {
			public void handleMessage(Message msg) {
				String v = (String) msg.obj;
				if ((v.trim()).equals("ok")) {
					dialog.setIcon(R.drawable.confirmacion_icon);
					dialog.setMessage(mensaje);
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
		dialog = new AlertDialog.Builder(activity);
		dialog.setTitle(titulo);
		dialog.setPositiveButton("Cerrar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						
					}
				});
		dialog.create();
	}
	@Override
	public void onBajarDatosTablas(boolean respuesta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEnviarInternetCenso(String result) {

	}
}
