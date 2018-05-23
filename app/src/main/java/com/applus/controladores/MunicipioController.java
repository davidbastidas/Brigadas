package com.applus.controladores;

import com.applus.modelos.Municipio;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MunicipioController {

	int tamanoConsulta=0;
	public int getTamanoConsulta() {
		return tamanoConsulta;
	}

	public synchronized void insertar(ArrayList<Municipio> municipios, Activity activity) {
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {
			int tamano = municipios.size();
			if (tamano > 0) {
				for (int i = 0; i < tamano; i++) {
					db.execSQL("INSERT OR IGNORE INTO municipio (" +
							"id," +
							"nombre," +
							"fk_departamento" +
							") "
							+ "VALUES (" +
							""+ municipios.get(i).getId() + ", " +
							"'"+ municipios.get(i).getNombre() + "'," +
							""+ municipios.get(i).getFk_departamento() + "" +
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
	        actualizados = db.update("municipio", registro, where, null);
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
			registros=db.delete("municipio", where, null);
			//db.execSQL("DELETE FROM municipio");
		}
		// no cerramos por singleton
		return registros;
	}
	public synchronized ArrayList<Municipio> consultar(int pagina, int limite,String condicion, Activity activity){
		Municipio dataSet;
		ArrayList<Municipio> municipios=new ArrayList<Municipio>();
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
		c = db.rawQuery("SELECT * FROM municipio"+where+" "+limit, null);
		countCursor=db.rawQuery("SELECT count(id) FROM municipio"+where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta=countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		//System.out.println("Tama�o: "+tamanoConsulta);
		if (c.moveToFirst()) {
			do {
				dataSet = new Municipio();
				dataSet.setId(c.getLong(0));
				dataSet.setNombre(c.getString(1));
				dataSet.setFk_departamento(c.getInt(2));
				
				//System.out.println("Item: " + c.getInt(1) + "Ciente: "+ c.getString(4) + "dir: " + c.getString(5));
				municipios.add(dataSet);
			} while (c.moveToNext());
		}
		c.close();
		countCursor.close();
		// no cerramos por singleton
		return municipios;
	}
	public synchronized int count(String condicion, Activity activity){
		Cursor countCursor = null;
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String where="";
		if(!condicion.equals("")){
			where=" WHERE "+condicion;
		}
		countCursor=db.rawQuery("SELECT count(id) FROM municipio "+where, null);
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
