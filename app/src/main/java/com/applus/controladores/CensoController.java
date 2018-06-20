package com.applus.controladores;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.applus.modelos.Censo;

import java.util.ArrayList;

public class CensoController {

	private static String tableName = "censos";
	int tamanoConsulta=0;
	long lastInsert;
	public long getLastInsert() {
		return lastInsert;
	}

	public int getTamanoConsulta() {
		return tamanoConsulta;
	}

	public synchronized void insertar(Censo censo, Activity activity) {
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {
			ContentValues registro=new ContentValues();
			registro.put("codigo", censo.getCodigo());
			registro.put("nic", censo.getNic());
			registro.put("barrio", censo.getBarrio());
			registro.put("fk_usuario", censo.getFk_usuario());
			registro.put("last_insert", 0);
			registro.put("latitud", censo.getLatitud());
			registro.put("longitud", censo.getLongitud());
			registro.put("departamento", censo.getDepartamento());
			registro.put("municipio", censo.getMunicipio());
			registro.put("cliente", censo.getCliente());
			registro.put("fecha", censo.getFecha());
			registro.put("hora", censo.getHora());
			registro.put("fk_cliente", censo.getFk_cliente());
			registro.put("acurracy", censo.getAcurracy());
			registro.put("formulario", censo.getFormulario());
			registro.put("datos", censo.getDatos());
			registro.put("firma", censo.getFirma());
			registro.put("tipo", censo.getTipoCliente());
			lastInsert=db.insert(tableName, null, registro);
			//Cerramos la base de datos
			// no cerramos por singleton
		}
	}
	public synchronized int actualizar(ContentValues registro,String where, Activity activity){
		int actualizados = 0;
		// Abrimos la base de datos 'db_ordenes' en modo escritura
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {
	        actualizados = db.update(tableName, registro, where, null);
		}
		// no cerramos por singleton
		return actualizados;
	}
	public synchronized int eliminar(String where, Activity activity){
		int registros=0;
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();

		if (db != null) {
			registros=db.delete(tableName, where, null);
		}
		return registros;
	}
	public synchronized ArrayList<Censo> consultar(int pagina, int limite,String condicion, Activity activity){
        Censo dataSet;
		ArrayList<Censo> censos =new ArrayList<Censo>();
		Cursor c= null,countCursor = null;
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String limit="";
		if(limite!=0){
			limit=" LIMIT "+pagina+","+limite;
		}
		String where="";
		if(!condicion.equals("")){
			where=" WHERE "+condicion;
		}
		c = db.rawQuery("SELECT * FROM " + tableName + " " + where+" ORDER BY id DESC "+limit, null);
		countCursor=db.rawQuery("SELECT count(id) FROM " + tableName + " " + where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta=countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		//System.out.println("Tama�o: "+tamanoConsulta);
		if (c.moveToFirst()) {
			do {
				dataSet = new Censo();
				dataSet.setId(c.getLong(0));
				dataSet.setCodigo(c.getLong(1));
				dataSet.setNic(c.getLong(2));
				dataSet.setFormulario(c.getString(3));
				dataSet.setDatos(c.getString(4));
				dataSet.setBarrio(c.getString(5));
				dataSet.setFk_usuario(c.getInt(6));
				dataSet.setLast_insert(c.getInt(7));
				dataSet.setLatitud(c.getString(8));
				dataSet.setLongitud(c.getString(9));
				dataSet.setDepartamento(c.getString(10));
				dataSet.setMunicipio(c.getString(11));
				dataSet.setCliente(c.getString(12));
				dataSet.setFecha(c.getString(13));
				dataSet.setHora(c.getString(14));
				dataSet.setFk_cliente(c.getInt(15));
				dataSet.setAcurracy(c.getFloat(16));
				dataSet.setFirma(c.getString(17));
				dataSet.setTipoCliente(c.getString(18));
				
				//System.out.println("Item: " + c.getInt(1) + "Ciente: "+ c.getString(4) + "dir: " + c.getString(5));
				censos.add(dataSet);
			} while (c.moveToNext());
		}
		c.close();
		countCursor.close();
		// no cerramos por singleton
		return censos;
	}
	public synchronized int count(String condicion, Activity activity){
		Cursor countCursor = null;
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String where="";
		if(!condicion.equals("")){
			where=" WHERE "+condicion;
		}
		countCursor=db.rawQuery("SELECT count(id) FROM " + tableName + " " + where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta=countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		countCursor.close();
		// no cerramos por singleton
		return tamanoConsulta;
	}

	/*acciones para el formulario*/
	public void eliminarFormulario(Activity activity){
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
			db.execSQL("DELETE FROM censo_formulario");
		}
	}

	public synchronized void insertarFormulario(String form, Activity activity) {
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();

		if (db != null) {
			ContentValues registro=new ContentValues();
			registro.put("formulario", form);
			db.insert("censo_formulario", null, registro);
		}
	}

	public String consultarFormulario(Activity activity){
		String formulario = "";
		Cursor c= null;
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();

		c = db.rawQuery("SELECT * FROM censo_formulario", null);

		if (c.moveToFirst()) {
			do {
				formulario = c.getString(1);
			} while (c.moveToNext());
		}
		c.close();
		return formulario;
	}
}
