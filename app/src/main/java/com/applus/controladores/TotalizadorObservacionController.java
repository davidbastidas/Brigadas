package com.applus.controladores;

import com.applus.modelos.TotalizadorObservaciones;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TotalizadorObservacionController {

	int tamanoConsulta=0;
	public int getTamanoConsulta() {
		return tamanoConsulta;
	}

	public synchronized void insertar(ArrayList<TotalizadorObservaciones> obs, Activity activity) {
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {
			int tamano = obs.size();
			if (tamano > 0) {
				for (int i = 0; i < tamano; i++) {
					db.execSQL("INSERT OR IGNORE INTO totalizador_observaciones (" +
							"id," +
							"nombre," +
							"fk_estado_medida" +
							") "
							+ "VALUES (" +
							""+ obs.get(i).getId() + ", " +
							"'"+ obs.get(i).getNombre() + "'," +
							""+ obs.get(i).getFk_estado_medida() + "" +
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
	        actualizados = db.update("totalizador_observaciones", registro, where, null);
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
			registros=db.delete("totalizador_observaciones", where, null);
			//db.execSQL("DELETE FROM totalizador_observaciones");
		}
		// no cerramos por singleton
		return registros;
	}
	public synchronized ArrayList<TotalizadorObservaciones> consultar(int pagina, int limite,String condicion, Activity activity){
		TotalizadorObservaciones dataSet;
		ArrayList<TotalizadorObservaciones> observacions=new ArrayList<TotalizadorObservaciones>();
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
		c = db.rawQuery("SELECT * FROM totalizador_observaciones"+where+" "+limit, null);
		countCursor=db.rawQuery("SELECT count(id) FROM totalizador_observaciones"+where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta=countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		//System.out.println("Tama�o: "+tamanoConsulta);
		if (c.moveToFirst()) {
			do {
				dataSet = new TotalizadorObservaciones();
				dataSet.setId(c.getLong(0));
				dataSet.setNombre(c.getString(1));
				dataSet.setFk_estado_medida(c.getInt(2));
				
				//System.out.println("Item: " + c.getInt(1) + "Ciente: "+ c.getString(4) + "dir: " + c.getString(5));
				observacions.add(dataSet);
			} while (c.moveToNext());
		}
		c.close();
		countCursor.close();
		// no cerramos por singleton
		return observacions;
	}
	public synchronized int count(String condicion, Activity activity){
		Cursor countCursor = null;
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String where="";
		if(!condicion.equals("")){
			where=" WHERE "+condicion;
		}
		countCursor=db.rawQuery("SELECT count(id) FROM totalizador_observaciones "+where, null);
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
