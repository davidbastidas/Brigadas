package com.applus.controladores;

import com.applus.modelos.BrigadaMaterialParcelable;
import com.applus.modelos.BrigadaParcelable;
import com.applus.modelos.BrigadaTrabajoParcelable;
import com.applus.modelos.Novedades;
import com.applus.modelos.SesionSingleton;
import com.applus.modelos.Totalizadores;
import com.applus.vistas.operario.brigada.OnBrigada;
import com.applus.vistas.operario.novedad.OnNovedad;
import com.applus.vistas.operario.totalizadores.OnTotalizador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class WebServiceTaskSET extends AsyncTask<Object, Void, String> {
	
	public OnBrigada callback=null;
	public OnTotalizador callback_totalizador=null;
	public OnNovedad callback_novedad=null;
	
	String resultadoFinal="";
	String nombreFuncion="";
	long tamanoImagen=0;
	
	private String NAMESPACE = "http://190.156.237.114/gestioneca/sw/index.php?controlador=Brigadas&accion=login";
	private String URL = "http://190.156.237.114/gestioneca/sw/index.php?controlador=Brigadas&accion=login";
	
	private TextView view;
	Activity activity;
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	SesionSingleton sesion;
	public WebServiceTaskSET() {
		sesion=SesionSingleton.getInstance();
		NAMESPACE = "http://"+sesion.getIP()+"/"+sesion.getPROYECTO()+"/sw/index.php?";
		URL = "http://"+sesion.getIP()+"/"+sesion.getPROYECTO()+"/sw/index.php?";
	}
	
	@Override
	protected void onPreExecute() {
		//view.setText("Conectando a WebService...");
	}
	
	@Override
	protected String doInBackground(Object... params) {
		String resultado = "-1";
		nombreFuncion=(String) params[0];
		if(nombreFuncion.equals("enviarBrigada")){
			resultado=enviarBrigada(params);
		}else if(nombreFuncion.equals("enviarTotalizador")){
			resultado=enviarTotalizador(params);
		}else if(nombreFuncion.equals("enviarNovedad")){
			resultado=enviarNovedad(params);
		}else if(nombreFuncion.equals("enviarBrigadaMasiva")){
			resultado=enviarBrigadaMasiva(params);
		}else if(nombreFuncion.equals("enviarTotalizadorMasivo")){
			resultado=enviarTotalizadorMasivo(params);
		}else if(nombreFuncion.equals("enviarNovedadMasivo")){
			resultado=enviarNovedadMasivo(params);
		}
		
		return resultado;
	}

	@Override
	protected void onPostExecute(String result) {
		//view.setText("Resultado: " + resultadoFinal);
		if(nombreFuncion.equals("enviarBrigada")){
			callback.onEnviarInternetBrigada(result);
		}else if(nombreFuncion.equals("enviarTotalizador")){
			callback_totalizador.onEnviarInternetTotalizador(result);
		}else if(nombreFuncion.equals("enviarNovedad")){
			callback_novedad.onEnviarInternetNovedad(result);
		}else if(nombreFuncion.equals("enviarBrigadaMasiva")){
			callback.onEnviarInternetBrigada(result);
		}else if(nombreFuncion.equals("enviarTotalizadorMasivo")){
			callback_totalizador.onEnviarInternetTotalizador(result);
		}else if(nombreFuncion.equals("enviarNovedadMasivo")){
			callback_novedad.onEnviarInternetNovedad(result);
		}
	}
	
	private String enviarBrigada(Object... params){
		String respuesta="";
		BrigadaParcelable brigada= (BrigadaParcelable) params[1];
		ArrayList<BrigadaTrabajoParcelable> britrab= (ArrayList<BrigadaTrabajoParcelable>) params[2];
		ArrayList<BrigadaMaterialParcelable> brimat= (ArrayList<BrigadaMaterialParcelable>) params[3];
		try {
			JSONObject trabajo =null;
			JSONArray trabajos = new JSONArray();
			for (BrigadaTrabajoParcelable bt : britrab) {
				trabajo = new JSONObject();
				trabajo.put("id_trabajo", bt.getFk_trabajo());
				trabajo.put("id_ejecucion", bt.getFk_estado());
				trabajo.put("observaciones", bt.getObservaciones());
				trabajos.put(trabajo);
				trabajo =null;
			}
			JSONObject material =null;
			JSONArray materiales = new JSONArray();
			for (BrigadaMaterialParcelable mt : brimat) {
				material = new JSONObject();
				material.put("id_material", mt.getFk_material());
				material.put("cantidad", mt.getCantidad());
				materiales.put(material);
				material =null;
			}
			
			JSONObject mainObj = new JSONObject();
			mainObj.put("descargo", brigada.getDescargo());
			mainObj.put("festivo", brigada.getFestivo());
			mainObj.put("fecha", brigada.getFecha());
			mainObj.put("fecha_inicio", brigada.getFecha_inicio());
			mainObj.put("hora_inicio", brigada.getHora_inicio());
			System.out.println("t_hora_i.getText(): "+brigada.getHora_inicio());
			mainObj.put("fecha_final", brigada.getFecha_final());
			mainObj.put("hora_final", brigada.getHora_final());
			mainObj.put("municipio", brigada.getFk_municipio());
			mainObj.put("direccion", brigada.getDireccion());
			mainObj.put("grua", brigada.getGrua_hora());
			mainObj.put("canasta", brigada.getCanasta_hora());
			mainObj.put("km_adicional", brigada.getKm_adicional());
			mainObj.put("peaje", brigada.getPeaje());
			mainObj.put("almuerzo", brigada.getAlmuerzo());
			mainObj.put("hotel", brigada.getHotel());
			mainObj.put("latitud", brigada.getLatitud());
			mainObj.put("longitud", brigada.getLongitud());
			mainObj.put("usuario", brigada.getFk_usuario());
			mainObj.put("fk_departamento", brigada.getFk_departamento());
			mainObj.put("fk_barrio", brigada.getFk_barrio());
			mainObj.put("fk_accion", brigada.getFk_accion());
			mainObj.put("acurracy", brigada.getAcurracy());
			mainObj.put("trabajos", trabajos);
			mainObj.put("material", materiales);
			try {
		        HttpParams httpParams = new BasicHttpParams();
		 
		        ConnManagerParams.setTimeout(httpParams, 30000);
		        HttpConnectionParams.setConnectionTimeout(httpParams,30000);
		        HttpConnectionParams.setSoTimeout(httpParams, 30000);
		 
		        HttpClient httpClient = new DefaultHttpClient(httpParams);
		        HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Brigadas&accion=insertar");
		 
		        // Add your data
	 			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
	 					2);
	 			nameValuePairs.add(new BasicNameValuePair("json", mainObj.toString()));
	 			nameValuePairs.add(new BasicNameValuePair("user", ""+sesion.getFk_id_operario()));
		        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse httpResponse = httpClient.execute(httpPost);
		        HttpEntity httpEntity = httpResponse.getEntity();
		 
		        if (httpEntity != null) {
		            InputStream inputStream = httpEntity.getContent();
		            if (inputStream != null) {
		            	respuesta = leerRespuestaHttp(inputStream);
		            }
		        }
		    } catch (ConnectTimeoutException e) {
		    	respuesta = "ConnectTimeoutException: " + e;
		    } catch (Exception e) {
		    	respuesta = "Exception: " + e;
		    }
			
		} catch (Exception e) {
			System.out.println("Exception, setOrdenes= "+e);
			System.out.println(Thread.currentThread().getStackTrace()[1]);
			e.printStackTrace();
		}
		return resultadoFinal=respuesta;
	}
	
	private String enviarBrigadaMasiva(Object... params){
		String respuesta="";
		Activity activity=(Activity) params[1];
		ArrayList<BrigadaParcelable> brigadas=null;
		ArrayList<BrigadaTrabajoParcelable> britrab=null;
		ArrayList<BrigadaMaterialParcelable> brimat=null;
		BrigadaController bricont=new BrigadaController();
		brigadas=bricont.consultar(0, 0, "last_insert=0", activity);
		BrigadaTrabajoController brigtrabcont=new BrigadaTrabajoController();
		britrab=brigtrabcont.consultar(0, 0, "", activity);
		BrigadaMaterialController brigmatcont=new BrigadaMaterialController();
		brimat=brigmatcont.consultar(0, 0, "", activity);
		int cont=0;
		try {
			for (BrigadaParcelable bp : brigadas) {
				JSONObject trabajo =null;
				JSONArray trabajos = new JSONArray();
				for (BrigadaTrabajoParcelable bt : britrab) {
					if(bt.getFk_brigada()==bp.getId()){
						trabajo = new JSONObject();
						trabajo.put("id_trabajo", bt.getFk_trabajo());
						trabajo.put("id_ejecucion", bt.getFk_estado());
						trabajo.put("observaciones", bt.getObservaciones());
						trabajos.put(trabajo);
						trabajo =null;
					}
				}
				JSONObject material =null;
				JSONArray materiales = new JSONArray();
				for (BrigadaMaterialParcelable mt : brimat) {
					if(mt.getFk_brigada()==bp.getId()){
						material = new JSONObject();
						material.put("id_material", mt.getFk_material());
						material.put("cantidad", mt.getCantidad());
						materiales.put(material);
						material =null;
					}
				}
				JSONObject mainObj = new JSONObject();
				mainObj.put("descargo", bp.getDescargo());
				mainObj.put("festivo", bp.getFestivo());
				mainObj.put("fecha", bp.getFecha());
				mainObj.put("fecha_inicio", bp.getFecha_inicio());
				mainObj.put("hora_inicio", bp.getHora_inicio());
				mainObj.put("fecha_final", bp.getFecha_final());
				mainObj.put("hora_final", bp.getHora_final());
				mainObj.put("municipio", bp.getFk_municipio());
				mainObj.put("direccion", bp.getDireccion());
				mainObj.put("grua", bp.getGrua_hora());
				mainObj.put("canasta", bp.getCanasta_hora());
				mainObj.put("km_adicional", bp.getKm_adicional());
				mainObj.put("peaje", bp.getPeaje());
				mainObj.put("almuerzo", bp.getAlmuerzo());
				mainObj.put("hotel", bp.getHotel());
				mainObj.put("latitud", bp.getLatitud());
				mainObj.put("longitud", bp.getLongitud());
				mainObj.put("usuario", bp.getFk_usuario());
				mainObj.put("fk_departamento", bp.getFk_departamento());
				mainObj.put("fk_barrio", bp.getFk_barrio());
				mainObj.put("fk_accion", bp.getFk_accion());
				mainObj.put("acurracy", bp.getAcurracy());
				mainObj.put("trabajos", trabajos);
				mainObj.put("material", materiales);
				try {
			        HttpParams httpParams = new BasicHttpParams();
			 
			        ConnManagerParams.setTimeout(httpParams, 30000);
			        HttpConnectionParams.setConnectionTimeout(httpParams,30000);
			        HttpConnectionParams.setSoTimeout(httpParams, 30000);
			 
			        HttpClient httpClient = new DefaultHttpClient(httpParams);
			        HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Brigadas&accion=insertar");
			 
			        // Add your data
		 			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
		 					2);
		 			nameValuePairs.add(new BasicNameValuePair("json", mainObj.toString()));
		 			nameValuePairs.add(new BasicNameValuePair("user", ""+sesion.getFk_id_operario()));
			        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			        HttpResponse httpResponse = httpClient.execute(httpPost);
			        HttpEntity httpEntity = httpResponse.getEntity();
			 
			        if (httpEntity != null) {
			            InputStream inputStream = httpEntity.getContent();
			            if (inputStream != null) {
			            	respuesta = leerRespuestaHttp(inputStream);
			            	//convertir a json y actualizar
			            	JSONObject json_data = new JSONObject(respuesta);
			            	if(json_data.getInt("last_insert")>0){
			            		ContentValues value=new ContentValues();
				            	value.put("last_insert", json_data.getInt("last_insert"));
				            	bricont.actualizar(value, "id="+bp.getId(), activity);
				            	cont++;
			            	}
			            }
			        }
			    } catch (ConnectTimeoutException e) {
			    	respuesta = "ConnectTimeoutException: " + e;
			    } catch (Exception e) {
			    	respuesta = "Exception: " + e;
			    }
			}
		} catch (Exception e) {
			System.out.println("Exception, setOrdenes= "+e);
			System.out.println(Thread.currentThread().getStackTrace()[1]);
			e.printStackTrace();
		}
		respuesta=cont+" Brigadas Actualizadas";
		return resultadoFinal=respuesta;
	}
	
	
	private String enviarTotalizador(Object... params){
		String respuesta="";
		Totalizadores totalizador= (Totalizadores) params[1];
		try {
			JSONObject mainObj = new JSONObject();
			mainObj.put("fecha", totalizador.getFecha());
			mainObj.put("nic", totalizador.getNic());
			mainObj.put("municipio", totalizador.getFk_municipio());
			mainObj.put("barrio", totalizador.getFk_barrio());
			mainObj.put("direccion", totalizador.getDireccion());
			mainObj.put("ct", totalizador.getCt());
			mainObj.put("mt", totalizador.getMt());
			mainObj.put("estado_medida", totalizador.getFk_estado_medida());
			mainObj.put("observacion", totalizador.getFk_observacion());
			mainObj.put("latitud", totalizador.getLatitud());
			mainObj.put("longitud", totalizador.getLongitud());
			mainObj.put("usuario", totalizador.getFk_usuario());
			mainObj.put("departamento", totalizador.getFk_departamento());
			mainObj.put("otro", totalizador.getOtro());
			mainObj.put("acurracy", totalizador.getAcurracy());
			try {
		        HttpParams httpParams = new BasicHttpParams();
		 
		        ConnManagerParams.setTimeout(httpParams, 30000);
		        HttpConnectionParams.setConnectionTimeout(httpParams,30000);
		        HttpConnectionParams.setSoTimeout(httpParams, 30000);
		 
		        HttpClient httpClient = new DefaultHttpClient(httpParams);
		        HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Totalizador&accion=insertar");
		 
		        // Add your data
	 			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
	 					2);
	 			nameValuePairs.add(new BasicNameValuePair("json", mainObj.toString()));
	 			nameValuePairs.add(new BasicNameValuePair("user", ""+sesion.getFk_id_operario()));
		        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse httpResponse = httpClient.execute(httpPost);
		        HttpEntity httpEntity = httpResponse.getEntity();
		 
		        if (httpEntity != null) {
		            InputStream inputStream = httpEntity.getContent();
		            if (inputStream != null) {
		            	respuesta = leerRespuestaHttp(inputStream);
		            }
		        }
		    } catch (ConnectTimeoutException e) {
		    	respuesta = "ConnectTimeoutException: " + e;
		    } catch (Exception e) {
		    	respuesta = "Exception: " + e;
		    }
			
		} catch (Exception e) {
			System.out.println("Exception, setOrdenes= "+e);
			System.out.println(Thread.currentThread().getStackTrace()[1]);
			e.printStackTrace();
		}
		return resultadoFinal=respuesta;
	}
	
	private String enviarTotalizadorMasivo(Object... params){
		String respuesta="";
		Activity activity=(Activity) params[1];
		ArrayList<Totalizadores> totalizadores=null;
		TotalizadorController tot=new TotalizadorController();
		totalizadores=tot.consultar(0, 0, "last_insert=0", activity);
		int cont=0;
		try {
			for (Totalizadores t : totalizadores) {
				JSONObject mainObj = new JSONObject();
				mainObj.put("fecha", t.getFecha());
				mainObj.put("nic", t.getNic());
				mainObj.put("municipio", t.getFk_municipio());
				mainObj.put("barrio", t.getFk_barrio());
				mainObj.put("direccion", t.getDireccion());
				mainObj.put("ct", t.getCt());
				mainObj.put("mt", t.getMt());
				mainObj.put("estado_medida", t.getFk_estado_medida());
				mainObj.put("observacion", t.getFk_observacion());
				mainObj.put("latitud", t.getLatitud());
				mainObj.put("longitud", t.getLongitud());
				mainObj.put("usuario", t.getFk_usuario());
				mainObj.put("departamento", t.getFk_departamento());
				mainObj.put("otro", t.getOtro());
				mainObj.put("acurracy", t.getAcurracy());
				try {
			        HttpParams httpParams = new BasicHttpParams();
			 
			        ConnManagerParams.setTimeout(httpParams, 30000);
			        HttpConnectionParams.setConnectionTimeout(httpParams,30000);
			        HttpConnectionParams.setSoTimeout(httpParams, 30000);
			 
			        HttpClient httpClient = new DefaultHttpClient(httpParams);
			        HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Totalizador&accion=insertar");
			 
			        // Add your data
		 			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
		 					2);
		 			nameValuePairs.add(new BasicNameValuePair("json", mainObj.toString()));
		 			nameValuePairs.add(new BasicNameValuePair("user", ""+sesion.getFk_id_operario()));
			        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			        HttpResponse httpResponse = httpClient.execute(httpPost);
			        HttpEntity httpEntity = httpResponse.getEntity();
			 
			        if (httpEntity != null) {
			            InputStream inputStream = httpEntity.getContent();
			            if (inputStream != null) {
			            	respuesta = leerRespuestaHttp(inputStream);
			            	//convertir a json y actualizar
			            	JSONObject json_data = new JSONObject(respuesta);
			            	if(json_data.getInt("last_insert")>0){
			            		ContentValues value=new ContentValues();
				            	value.put("last_insert", json_data.getInt("last_insert"));
				            	tot.actualizar(value, "id="+t.getId(), activity);
				            	cont++;
			            	}
			            }
			        }
			    } catch (ConnectTimeoutException e) {
			    	respuesta = "ConnectTimeoutException: " + e;
			    } catch (Exception e) {
			    	respuesta = "Exception: " + e;
			    }
			}
		} catch (Exception e) {
			System.out.println("Exception, setOrdenes= "+e);
			System.out.println(Thread.currentThread().getStackTrace()[1]);
			e.printStackTrace();
		}
		respuesta=cont+" Totalizadores Actualizados";
		return resultadoFinal=respuesta;
	}
	
	private String enviarNovedad(Object... params){
		String respuesta="";
		Novedades novedad= (Novedades) params[1];
		try {
			JSONObject mainObj = new JSONObject();
			mainObj.put("codigo", novedad.getCodigo());
			mainObj.put("fk_tipo", novedad.getFk_novedad());
			mainObj.put("barrio", novedad.getBarrio());
			mainObj.put("observacion", novedad.getObservaciones());
			mainObj.put("latitud", novedad.getLatitud());
			mainObj.put("longitud", novedad.getLongitud());
			mainObj.put("usuario", novedad.getFk_usuario());
			mainObj.put("departamento", novedad.getDepartamento());
			mainObj.put("municipio", novedad.getMunicipio());
			mainObj.put("otro", novedad.getOtro());
			mainObj.put("cliente", novedad.getFk_cliente());
			mainObj.put("fecha", novedad.getFecha());
			mainObj.put("hora", novedad.getHora());
			mainObj.put("acurracy", novedad.getAcurracy());
			try {
		        HttpParams httpParams = new BasicHttpParams();
		 
		        ConnManagerParams.setTimeout(httpParams, 30000);
		        HttpConnectionParams.setConnectionTimeout(httpParams,30000);
		        HttpConnectionParams.setSoTimeout(httpParams, 30000);
		 
		        HttpClient httpClient = new DefaultHttpClient(httpParams);
		        HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Novedad&accion=insertar");
		 
		        // Add your data
	 			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
	 					2);
	 			nameValuePairs.add(new BasicNameValuePair("json", mainObj.toString()));
	 			nameValuePairs.add(new BasicNameValuePair("user", ""+sesion.getFk_id_operario()));
		        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse httpResponse = httpClient.execute(httpPost);
		        HttpEntity httpEntity = httpResponse.getEntity();
		 
		        if (httpEntity != null) {
		            InputStream inputStream = httpEntity.getContent();
		            if (inputStream != null) {
		            	respuesta = leerRespuestaHttp(inputStream);
		            }
		        }
		    } catch (ConnectTimeoutException e) {
		    	respuesta = "ConnectTimeoutException: " + e;
		    } catch (Exception e) {
		    	respuesta = "Exception: " + e;
		    }
			
		} catch (Exception e) {
			System.out.println("Exception, setOrdenes= "+e);
			System.out.println(Thread.currentThread().getStackTrace()[1]);
			e.printStackTrace();
		}
		return resultadoFinal=respuesta;
	}
	
	private String enviarNovedadMasivo(Object... params){
		String respuesta="";
		Activity activity=(Activity) params[1];
		ArrayList<Novedades> novedades= null;
		NovedadController nov=new NovedadController();
		novedades=nov.consultar(0, 0, "last_insert=0", activity);
		int cont=0;
		try {
			for (Novedades n : novedades) {
				JSONObject mainObj = new JSONObject();
				mainObj.put("codigo", n.getCodigo());
				mainObj.put("fk_tipo", n.getFk_novedad());
				mainObj.put("barrio", n.getBarrio());
				mainObj.put("observacion", n.getObservaciones());
				mainObj.put("latitud", n.getLatitud());
				mainObj.put("longitud", n.getLongitud());
				mainObj.put("usuario", n.getFk_usuario());
				mainObj.put("departamento", n.getDepartamento());
				mainObj.put("municipio", n.getMunicipio());
				mainObj.put("otro", n.getOtro());
				mainObj.put("cliente", n.getFk_cliente());
				mainObj.put("fecha", n.getFecha());
				mainObj.put("hora", n.getHora());
				mainObj.put("acurracy", n.getAcurracy());
				try {
			        HttpParams httpParams = new BasicHttpParams();
			 
			        ConnManagerParams.setTimeout(httpParams, 30000);
			        HttpConnectionParams.setConnectionTimeout(httpParams,30000);
			        HttpConnectionParams.setSoTimeout(httpParams, 30000);
			 
			        HttpClient httpClient = new DefaultHttpClient(httpParams);
			        HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Novedad&accion=insertar");
			 
			        // Add your data
		 			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
		 					2);
		 			nameValuePairs.add(new BasicNameValuePair("json", mainObj.toString()));
		 			System.out.println("json masivo: "+mainObj.toString());
		 			nameValuePairs.add(new BasicNameValuePair("user", ""+sesion.getFk_id_operario()));
			        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			        HttpResponse httpResponse = httpClient.execute(httpPost);
			        HttpEntity httpEntity = httpResponse.getEntity();
			 
			        if (httpEntity != null) {
			            InputStream inputStream = httpEntity.getContent();
			            if (inputStream != null) {
			            	respuesta = leerRespuestaHttp(inputStream);
			            	System.out.println("resp servidor novedad: "+respuesta);
			            	//convertir a json y actualizar
			            	JSONObject json_data = new JSONObject(respuesta);
			            	if(json_data.getInt("last_insert")>0){
			            		ContentValues value=new ContentValues();
				            	value.put("last_insert", json_data.getInt("last_insert"));
				            	nov.actualizar(value, "id="+n.getId(), activity);
				            	cont++;
			            	}
			            }
			        }
			    } catch (ConnectTimeoutException e) {
			    	respuesta = "ConnectTimeoutException: " + e;
			    } catch (Exception e) {
			    	respuesta = "Exception: " + e;
			    }
			}
		} catch (Exception e) {
			System.out.println("Exception, setOrdenes= "+e);
			System.out.println(Thread.currentThread().getStackTrace()[1]);
			e.printStackTrace();
		}
		respuesta=cont+" Novedades Actualizadas";
		return resultadoFinal=respuesta;
	}
	
	
	public String leerRespuestaHttp(InputStream is) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(
					new InputStreamReader(is, "iso-8859-1"), 8);
		} catch (UnsupportedEncodingException e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append((line + "\n"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
}