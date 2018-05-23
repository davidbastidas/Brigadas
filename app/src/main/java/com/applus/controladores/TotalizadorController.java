package com.applus.controladores;

import com.applus.modelos.Totalizadores;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TotalizadorController {

	int tamanoConsulta=0;
	long lastInsert;
	public long getLastInsert() {
		return lastInsert;
	}

	public int getTamanoConsulta() {
		return tamanoConsulta;
	}

	public synchronized void insertar(Totalizadores totalizador, Activity activity) {
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {
			ContentValues registro=new ContentValues();
			registro.put("fecha", totalizador.getFecha());
			registro.put("nic", totalizador.getNic());
			registro.put("municipio", totalizador.getFk_municipio());
			registro.put("barrio", totalizador.getFk_barrio());
			registro.put("direccion", totalizador.getDireccion());
			registro.put("ct", totalizador.getCt());
			registro.put("mt", totalizador.getMt());
			registro.put("estado_medida", totalizador.getFk_estado_medida());
			registro.put("observacion", totalizador.getFk_observacion());
			registro.put("fk_usuario", totalizador.getFk_usuario());
			registro.put("last_insert", 0);
			registro.put("latitud", totalizador.getLatitud());
			registro.put("longitud", totalizador.getLongitud());
			registro.put("departamento", totalizador.getFk_departamento());
			registro.put("otro", totalizador.getOtro());
			registro.put("acurracy", totalizador.getAcurracy());
			lastInsert=db.insert("totalizadores", null, registro);
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
	        actualizados = db.update("totalizadores", registro, where, null);
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
			registros=db.delete("totalizadores", where, null);
			//db.execSQL("DELETE FROM totalizadores");
		}
		// no cerramos por singleton
		return registros;
	}
	public synchronized ArrayList<Totalizadores> consultar(int pagina, int limite,String condicion, Activity activity){
		Totalizadores dataSet;
		ArrayList<Totalizadores> totalizadores=new ArrayList<Totalizadores>();
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
		c = db.rawQuery("SELECT * FROM totalizadores"+where+" "+limit, null);
		countCursor=db.rawQuery("SELECT count(id) FROM totalizadores"+where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta=countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		//System.out.println("Tama�o: "+tamanoConsulta);
		if (c.moveToFirst()) {
			do {
				dataSet = new Totalizadores();
				dataSet.setId(c.getLong(0));
				dataSet.setFecha(c.getString(1));
				dataSet.setNic(c.getInt(2));
				dataSet.setFk_municipio(c.getInt(3));
				dataSet.setFk_barrio(c.getInt(4));
				dataSet.setDireccion(c.getString(5));
				dataSet.setCt(c.getString(6));
				dataSet.setMt(c.getString(7));
				dataSet.setFk_estado_medida(c.getInt(8));
				dataSet.setFk_observacion(c.getInt(9));
				dataSet.setFk_usuario(c.getInt(10));
				dataSet.setLast_insert(c.getInt(11));
				dataSet.setLatitud(c.getString(12));
				dataSet.setLongitud(c.getString(13));
				dataSet.setFk_departamento(c.getInt(14));
				dataSet.setOtro(c.getString(15));
				dataSet.setAcurracy(c.getFloat(16));
				
				//System.out.println("Item: " + c.getInt(1) + "Ciente: "+ c.getString(4) + "dir: " + c.getString(5));
				totalizadores.add(dataSet);
			} while (c.moveToNext());
		}
		c.close();
		countCursor.close();
		// no cerramos por singleton
		return totalizadores;
	}
	public synchronized int count(String condicion, Activity activity){
		Cursor countCursor = null;
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String where="";
		if(!condicion.equals("")){
			where=" WHERE "+condicion;
		}
		countCursor=db.rawQuery("SELECT count(id) FROM totalizadores "+where, null);
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
