package com.applus.controladores;

import com.applus.modelos.NovedadObservacion;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NovedadObservacionController {

	int tamanoConsulta=0;
	public int getTamanoConsulta() {
		return tamanoConsulta;
	}

	public synchronized void insertar(ArrayList<NovedadObservacion> obs_novedad, Activity activity) {
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {
			int tamano = obs_novedad.size();
			if (tamano > 0) {
				for (int i = 0; i < tamano; i++) {
					db.execSQL("INSERT OR IGNORE INTO novedad_observacion (" +
							"id," +
							"nombre" +
							") "
							+ "VALUES (" +
							""+ obs_novedad.get(i).getId() + ", " +
							"'"+ obs_novedad.get(i).getNombre() + "'" +
							")");
				}
			}
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
	        actualizados = db.update("novedad_observacion", registro, where, null);
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
			registros=db.delete("novedad_observacion", where, null);
			//db.execSQL("DELETE FROM novedad_observacion");
		}
		// no cerramos por singleton
		return registros;
	}
	public synchronized ArrayList<NovedadObservacion> consultar(int pagina, int limite,String condicion, Activity activity){
		NovedadObservacion dataSet;
		ArrayList<NovedadObservacion> obs_novedad=new ArrayList<NovedadObservacion>();
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
		c = db.rawQuery("SELECT * FROM novedad_observacion"+where+" "+limit, null);
		countCursor=db.rawQuery("SELECT count(id) FROM novedad_observacion"+where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta=countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		//System.out.println("Tama�o: "+tamanoConsulta);
		if (c.moveToFirst()) {
			do {
				dataSet = new NovedadObservacion();
				dataSet.setId(c.getLong(0));
				dataSet.setNombre(c.getString(1));
				
				//System.out.println("Item: " + c.getInt(1) + "Ciente: "+ c.getString(4) + "dir: " + c.getString(5));
				obs_novedad.add(dataSet);
			} while (c.moveToNext());
		}
		c.close();
		countCursor.close();
		// no cerramos por singleton
		return obs_novedad;
	}
	public synchronized int count(String condicion, Activity activity){
		Cursor countCursor = null;
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String where="";
		if(!condicion.equals("")){
			where=" WHERE "+condicion;
		}
		countCursor=db.rawQuery("SELECT count(id) FROM novedad_observacion "+where, null);
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
