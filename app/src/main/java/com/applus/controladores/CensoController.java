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
		// Si hemos abierto correctamente la base de datos
		if (db != null) {
			registros=db.delete(tableName, where, null);
			//db.execSQL("DELETE FROM "+tableName);
		}
		// no cerramos por singleton
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
		c = db.rawQuery("SELECT * FROM " + tableName + " " + where+" "+limit, null);
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
				dataSet.setBarrio(c.getString(2));
				dataSet.setFk_usuario(c.getInt(4));
				dataSet.setLast_insert(c.getInt(5));
				dataSet.setLatitud(c.getString(6));
				dataSet.setLongitud(c.getString(7));
				dataSet.setDepartamento(c.getString(8));
				dataSet.setMunicipio(c.getString(9));
				dataSet.setCliente(c.getString(11));
				dataSet.setFecha(c.getString(12));
				dataSet.setHora(c.getString(13));
				dataSet.setFk_cliente(c.getInt(14));
				dataSet.setAcurracy(c.getFloat(16));
				
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
}
