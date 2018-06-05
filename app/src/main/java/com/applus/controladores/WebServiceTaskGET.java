package com.applus.controladores;

import com.applus.controladores.interfaces.AsyncResponse;
import com.applus.controladores.interfaces.ClientesInterface;
import com.applus.modelos.SesionSingleton;
import com.applus.vistas.operario.novedad.OnNovedad;

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

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class WebServiceTaskGET extends AsyncTask<String, Void, String> {

	public AsyncResponse delegate=null;
	public OnNovedad delegate_novedad=null;
	String resultadoFinal="";
	String accionJava="";
	public ClientesInterface clienteInterface=null;
	/*
	 * param[0] trae el nombre de la funcion
	 * param[1] trae el nombre del metodo soap
	 * param[2] en adelante trae los parametros a enviar a la funcion
	 * */
	
	private String NAMESPACE = "http://190.156.237.114/gestioneca/sw/index.php?controlador=Site&accion=login";
	private String URL = "http://190.156.237.114/gestioneca/sw/index.php?controlador=Site&accion=login";

	private TextView view;
	SesionSingleton sesion;
	public WebServiceTaskGET() {
		sesion=SesionSingleton.getInstance();
		NAMESPACE = "http://"+sesion.getIP()+"/"+sesion.getPROYECTO()+"/sw/index.php?";
		URL = "http://"+sesion.getIP()+"/"+sesion.getPROYECTO()+"/sw/index.php?";
	}

	@Override
	protected void onPreExecute() {
		//view.setText("Conectando a WebService...");
	}

	@Override
	protected String doInBackground(String... params) {
		String resultado = "-1";
		accionJava=params[0];
		if(accionJava.equals("loginUser")){
			resultado=loginUser(params);
		} else if(accionJava.equals("getTrabajo")){
			resultado=getTrabajo(params);
		} else if(accionJava.equals("getMateriales")){
			resultado=getMateriales(params);
		} else if(accionJava.equals("getEstadoTrabajo")){
			resultado=getEstadoTrabajo(params);
		} else if(accionJava.equals("getDepartamento")){
			resultado=getDepartamento(params);
		} else if(accionJava.equals("getMunicipio")){
			resultado=getMunicipio(params);
		} else if(accionJava.equals("getCliente")){
			resultado=getCliente(params);
		} else if(accionJava.equals("getBarrios")){
			resultado=getBarrios(params);
		} else if(accionJava.equals("getTotalizadorEstadoMedida")){
			resultado=getTotalizadorEstadoMedida(params);
		} else if(accionJava.equals("getTotalizadorObservacion")){
			resultado=getTotalizadorObservacion(params);
		} else if(accionJava.equals("getNovedadTipoNovedad")){
			resultado=getNovedadTipoNovedad(params);
		} else if(accionJava.equals("getNovedadObservacion")){
			resultado=getNovedadObservacion(params);
		} else if(accionJava.equals("getBrigadaAccion")){
			resultado=getBrigadaAccion(params);
		} else if(accionJava.equals("getFormularioCenso")){
			resultado=getFormularioCenso(params);
		} else if(accionJava.equals("getCountClientes")){
			resultado=getCountClientes(params);
		} else if(accionJava.equals("getClientes")){
			resultado=getClientes(params);
		}
		
		return resultado;
	}

	@Override
	protected void onPostExecute(String result) {
		//view.setText("Resultado: " + resultadoFinal);
		if(accionJava.equals("loginUser")){
			delegate.processFinishLogin(result);
		} else if(accionJava.equals("getTrabajo")){
			delegate.onTablaTrabajos(result);
		} else if(accionJava.equals("getMateriales")){
			delegate.onTablaMateriales(result);
		} else if(accionJava.equals("getEstadoTrabajo")){
			delegate.onTablaEstadoTrabajo(result);
		} else if(accionJava.equals("getDepartamento")){
			delegate.onTablaDepartamento(result);
		} else if(accionJava.equals("getMunicipio")){
			delegate.onTablaMunicipio(result);
		} else if(accionJava.equals("getCliente")){
			delegate_novedad.onConsultaCliente(result);
		} else if(accionJava.equals("getBarrios")){
			delegate.onTablaBarrios(result);
		} else if(accionJava.equals("getTotalizadorEstadoMedida")){
			delegate.onTablaTotalizadorEstadoMedida(result);
		} else if(accionJava.equals("getTotalizadorObservacion")){
			delegate.onTablaTotalizadorObservacion(result);
		} else if(accionJava.equals("getNovedadTipoNovedad")){
			delegate.onTablaNovedadTipoNovedad(result);
		} else if(accionJava.equals("getNovedadObservacion")){
			delegate.onTablaNovedadObservacion(result);
		} else if(accionJava.equals("getBrigadaAccion")){
			delegate.onTablaBrigadaAccion(result);
		} else if(accionJava.equals("getFormularioCenso")){
			delegate.onDescargarFormularioCenso(result);
		} else if(accionJava.equals("getCountClientes")){
			clienteInterface.onCountClientes(result);
		} else if(accionJava.equals("getClientes")){
			clienteInterface.onDescargarClientes(result);
		}
		
	}
	public String loginUser(String... params){
		String respuesta="";
		try {
	        HttpParams httpParams = new BasicHttpParams();
	 
	        ConnManagerParams.setTimeout(httpParams, 30000);
	        HttpConnectionParams.setConnectionTimeout(httpParams,30000);
	        HttpConnectionParams.setSoTimeout(httpParams, 30000);
	 
	        HttpClient httpClient = new DefaultHttpClient(httpParams);
	        HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Site&accion=login");
	 
	        // Add your data
 			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
 					2);
 			nameValuePairs.add(new BasicNameValuePair("user", params[1]));
 			nameValuePairs.add(new BasicNameValuePair("pass", params[2]));
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
		return respuesta;
	}
	private String getTrabajo(String[] params) {
		String respuesta="";
		try {
	        HttpParams httpParams = new BasicHttpParams();
	 
	        ConnManagerParams.setTimeout(httpParams, 30000);
	        HttpConnectionParams.setConnectionTimeout(httpParams,30000);
	        HttpConnectionParams.setSoTimeout(httpParams, 30000);
	 
	        HttpClient httpClient = new DefaultHttpClient(httpParams);
	        HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Site&accion=getTrabajos");
	 
	        // Add your data
 			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
 					2);
 			nameValuePairs.add(new BasicNameValuePair("user", params[1]));
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
		return respuesta;
	}
	private String getMateriales(String[] params) {
		String respuesta="";
		try {
	        HttpParams httpParams = new BasicHttpParams();
	 
	        ConnManagerParams.setTimeout(httpParams, 30000);
	        HttpConnectionParams.setConnectionTimeout(httpParams,30000);
	        HttpConnectionParams.setSoTimeout(httpParams, 30000);
	 
	        HttpClient httpClient = new DefaultHttpClient(httpParams);
	        HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Site&accion=getMateriales");
	 
	        // Add your data
 			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
 					2);
 			nameValuePairs.add(new BasicNameValuePair("user", params[1]));
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
		return respuesta;
	}
	private String getEstadoTrabajo(String[] params) {
		String respuesta="";
		try {
	        HttpParams httpParams = new BasicHttpParams();
	 
	        ConnManagerParams.setTimeout(httpParams, 30000);
	        HttpConnectionParams.setConnectionTimeout(httpParams,30000);
	        HttpConnectionParams.setSoTimeout(httpParams, 30000);
	 
	        HttpClient httpClient = new DefaultHttpClient(httpParams);
	        HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Site&accion=getEstadoTrabajo");
	 
	        // Add your data
 			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
 					2);
 			nameValuePairs.add(new BasicNameValuePair("user", params[1]));
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
		return respuesta;
	}
	private String getDepartamento(String[] params) {
		String respuesta="";
		try {
	        HttpParams httpParams = new BasicHttpParams();
	 
	        ConnManagerParams.setTimeout(httpParams, 30000);
	        HttpConnectionParams.setConnectionTimeout(httpParams,30000);
	        HttpConnectionParams.setSoTimeout(httpParams, 30000);
	 
	        HttpClient httpClient = new DefaultHttpClient(httpParams);
	        HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Site&accion=getDepartamento");
	 
	        // Add your data
 			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
 					2);
 			nameValuePairs.add(new BasicNameValuePair("user", params[1]));
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
		return respuesta;
	}
	
	private String getMunicipio(String[] params) {
		String respuesta="";
		try {
	        HttpParams httpParams = new BasicHttpParams();
	 
	        ConnManagerParams.setTimeout(httpParams, 30000);
	        HttpConnectionParams.setConnectionTimeout(httpParams,30000);
	        HttpConnectionParams.setSoTimeout(httpParams, 30000);
	 
	        HttpClient httpClient = new DefaultHttpClient(httpParams);
	        HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Site&accion=getMunicipio");
	 
	        // Add your data
 			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
 					2);
 			nameValuePairs.add(new BasicNameValuePair("user", params[1]));
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
		return respuesta;
	}
	
	private String getCliente(String[] params) {
		String respuesta="";
		try {
	        HttpParams httpParams = new BasicHttpParams();
	 
	        ConnManagerParams.setTimeout(httpParams, 30000);
	        HttpConnectionParams.setConnectionTimeout(httpParams,30000);
	        HttpConnectionParams.setSoTimeout(httpParams, 30000);
	 
	        HttpClient httpClient = new DefaultHttpClient(httpParams);
	        HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Site&accion=getCliente");
	 
	        // Add your data
 			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
 					2);
 			nameValuePairs.add(new BasicNameValuePair("user", params[1]));
 			nameValuePairs.add(new BasicNameValuePair("codigo", params[2]));
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
		return respuesta;
	}
	
	private String getBarrios(String[] params) {
		String respuesta="";
		try {
	        HttpParams httpParams = new BasicHttpParams();
	 
	        ConnManagerParams.setTimeout(httpParams, 30000);
	        HttpConnectionParams.setConnectionTimeout(httpParams,30000);
	        HttpConnectionParams.setSoTimeout(httpParams, 30000);
	 
	        HttpClient httpClient = new DefaultHttpClient(httpParams);
	        HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Site&accion=getBarrios");
	 
	        // Add your data
 			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
 					2);
 			nameValuePairs.add(new BasicNameValuePair("user", params[1]));
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
		return respuesta;
	}
	
	private String getTotalizadorEstadoMedida(String[] params) {
		String respuesta="";
		try {
	        HttpParams httpParams = new BasicHttpParams();
	 
	        ConnManagerParams.setTimeout(httpParams, 30000);
	        HttpConnectionParams.setConnectionTimeout(httpParams,30000);
	        HttpConnectionParams.setSoTimeout(httpParams, 30000);
	 
	        HttpClient httpClient = new DefaultHttpClient(httpParams);
	        HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Site&accion=getTotalizadorEstadoMedida");
	 
	        // Add your data
 			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
 					2);
 			nameValuePairs.add(new BasicNameValuePair("user", params[1]));
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
		return respuesta;
	}
	
	private String getTotalizadorObservacion(String[] params) {
		String respuesta="";
		try {
	        HttpParams httpParams = new BasicHttpParams();
	 
	        ConnManagerParams.setTimeout(httpParams, 30000);
	        HttpConnectionParams.setConnectionTimeout(httpParams,30000);
	        HttpConnectionParams.setSoTimeout(httpParams, 30000);
	 
	        HttpClient httpClient = new DefaultHttpClient(httpParams);
	        HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Site&accion=getTotalizadorObservacion");
	 
	        // Add your data
 			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
 					2);
 			nameValuePairs.add(new BasicNameValuePair("user", params[1]));
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
		return respuesta;
	}
	
	private String getNovedadTipoNovedad(String[] params) {
		String respuesta="";
		try {
	        HttpParams httpParams = new BasicHttpParams();
	 
	        ConnManagerParams.setTimeout(httpParams, 30000);
	        HttpConnectionParams.setConnectionTimeout(httpParams,30000);
	        HttpConnectionParams.setSoTimeout(httpParams, 30000);
	 
	        HttpClient httpClient = new DefaultHttpClient(httpParams);
	        HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Site&accion=getNovedadesTipo");
	 
	        // Add your data
 			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
 					2);
 			nameValuePairs.add(new BasicNameValuePair("user", params[1]));
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
		return respuesta;
	}
	
	private String getNovedadObservacion(String[] params) {
		String respuesta="";
		try {
	        HttpParams httpParams = new BasicHttpParams();
	 
	        ConnManagerParams.setTimeout(httpParams, 30000);
	        HttpConnectionParams.setConnectionTimeout(httpParams,30000);
	        HttpConnectionParams.setSoTimeout(httpParams, 30000);
	 
	        HttpClient httpClient = new DefaultHttpClient(httpParams);
	        HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Site&accion=getNovedadesObservacion");
	 
	        // Add your data
 			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
 					2);
 			nameValuePairs.add(new BasicNameValuePair("user", params[1]));
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
		return respuesta;
	}
	
	private String getBrigadaAccion(String[] params) {
		String respuesta="";
		try {
	        HttpParams httpParams = new BasicHttpParams();
	 
	        ConnManagerParams.setTimeout(httpParams, 30000);
	        HttpConnectionParams.setConnectionTimeout(httpParams,30000);
	        HttpConnectionParams.setSoTimeout(httpParams, 30000);
	 
	        HttpClient httpClient = new DefaultHttpClient(httpParams);
	        HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Site&accion=getBrigadaAccion");
	 
	        // Add your data
 			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
 					2);
 			nameValuePairs.add(new BasicNameValuePair("user", params[1]));
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
		return respuesta;
	}

	private String getFormularioCenso(String[] params) {
		String respuesta="";
		try {
			HttpParams httpParams = new BasicHttpParams();

			ConnManagerParams.setTimeout(httpParams, 30000);
			HttpConnectionParams.setConnectionTimeout(httpParams,30000);
			HttpConnectionParams.setSoTimeout(httpParams, 30000);

			HttpClient httpClient = new DefaultHttpClient(httpParams);
			HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Censo&accion=form_censo");

			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					2);
			nameValuePairs.add(new BasicNameValuePair("user", params[1]));
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
		return respuesta;
	}

	private String getCountClientes(String[] params) {
		String respuesta="";
		try {
			HttpParams httpParams = new BasicHttpParams();

			ConnManagerParams.setTimeout(httpParams, 30000);
			HttpConnectionParams.setConnectionTimeout(httpParams,30000);
			HttpConnectionParams.setSoTimeout(httpParams, 30000);

			HttpClient httpClient = new DefaultHttpClient(httpParams);
			HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Cliente&accion=getCountClientes");

			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					2);
			nameValuePairs.add(new BasicNameValuePair("user", params[1]));
			nameValuePairs.add(new BasicNameValuePair("tabla", params[2]));
			nameValuePairs.add(new BasicNameValuePair("id_campo", params[3]));
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
		return respuesta;
	}

	private String getClientes(String[] params) {
		String respuesta="";
		try {
			HttpParams httpParams = new BasicHttpParams();

			ConnManagerParams.setTimeout(httpParams, 30000);
			HttpConnectionParams.setConnectionTimeout(httpParams,30000);
			HttpConnectionParams.setSoTimeout(httpParams, 30000);

			HttpClient httpClient = new DefaultHttpClient(httpParams);
			HttpPost httpPost = new HttpPost(NAMESPACE+"controlador=Cliente&accion=clientes_paginados");

			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					2);
			nameValuePairs.add(new BasicNameValuePair("user", params[1]));
			nameValuePairs.add(new BasicNameValuePair("pagina", params[2]));
			nameValuePairs.add(new BasicNameValuePair("limit", params[3]));
			nameValuePairs.add(new BasicNameValuePair("tabla", params[4]));
			nameValuePairs.add(new BasicNameValuePair("id_campo", params[5]));
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
		return respuesta;
	}
	
	public String getResultadoFinal() {
		return resultadoFinal;
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