package com.applus.vistas.operario.brigada;

import com.applus.controladores.BarrioController;
import com.applus.modelos.Barrio;
import com.applus.modelos.SesionSingleton;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;

public class TareasLargas extends AsyncTask<Object, Void, String> {

	public InterfaceTareasLargas delegate=null;
	String accionJava="";
	/*
	 * param[0] trae el nombre de la funcion
	 * param[1] trae el nombre del metodo soap
	 * param[2] en adelante trae los parametros a enviar a la funcion
	 * */
	SesionSingleton sesion;
	private String output="";
	private boolean estado=false;
	public TareasLargas() {
		sesion=SesionSingleton.getInstance();
	}

	@Override
	protected void onPreExecute() {
		//view.setText("Conectando a WebService...");
	}

	@Override
	protected String doInBackground(Object... params) {
		String resultado = "-1";
		accionJava=(String) params[0];
		if(accionJava.equals("insertarBarrios")){
			insertarBarrios(params);
		}
		
		return resultado;
	}

	@Override
	protected void onPostExecute(String result) {
		//view.setText("Resultado: " + resultadoFinal);
		if(accionJava.equals("insertarBarrios")){
			delegate.onBarriosInsert(estado, output);
		}
	}
	private void insertarBarrios(Object[] params) {
		JSONObject json_data;
		BarrioController barrio=new BarrioController();
		try {
			json_data = new JSONObject((String)params[1]);
			JSONArray barrios = json_data.getJSONArray("barrios");
			ArrayList<Barrio> values=new ArrayList<Barrio>();
			int size = barrios.length();
			for (int i = 0; i < size; ++i) {
			    JSONObject tr = barrios.getJSONObject(i);
			    values.add(new Barrio(
			    		tr.getLong("id"),
			    		tr.getString("nombre"),
			    		tr.getInt("fk_municipio")));
			}
			if(values.size()>0){
				barrio.eliminar("", (Activity)params[2]);
				barrio.insertar(values, (Activity)params[2]);
			}
			estado=true;
		} catch (Exception e) {
			System.out.println("Exception onTablaBarrios= "+e);
			output="Error Bajando los Barrios\n";
		}
	}
}