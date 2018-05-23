package com.applus.controladores;

import com.applus.modelos.Novedades;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NovedadController {

	int tamanoConsulta=0;
	long lastInsert;
	public long getLastInsert() {
		return lastInsert;
	}

	public int getTamanoConsulta() {
		return tamanoConsulta;
	}

	public synchronized void insertar(Novedades novedad, Activity activity) {
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {
			ContentValues registro=new ContentValues();
			registro.put("codigo", novedad.getCodigo());
			registro.put("barrio", novedad.getBarrio());
			registro.put("observaciones", novedad.getObservaciones());
			registro.put("fk_usuario", novedad.getFk_usuario());
			registro.put("last_insert", 0);
			registro.put("latitud", novedad.getLatitud());
			registro.put("longitud", novedad.getLongitud());
			registro.put("departamento", novedad.getDepartamento());
			registro.put("municipio", novedad.getMunicipio());
			registro.put("otro", novedad.getOtro());
			registro.put("cliente", novedad.getCliente());
			registro.put("fecha", novedad.getFecha());
			registro.put("hora", novedad.getHora());
			registro.put("fk_cliente", novedad.getFk_cliente());
			registro.put("fk_novedad", novedad.getFk_novedad());
			registro.put("acurracy", novedad.getAcurracy());
			lastInsert=db.insert("novedades", null, registro);
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
	        actualizados = db.update("novedades", registro, where, null);
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
			registros=db.delete("novedades", where, null);
			//db.execSQL("DELETE FROM totalizadores");
		}
		// no cerramos por singleton
		return registros;
	}
	public synchronized ArrayList<Novedades> consultar(int pagina, int limite,String condicion, Activity activity){
		Novedades dataSet;
		ArrayList<Novedades> novedades=new ArrayList<Novedades>();
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
		c = db.rawQuery("SELECT * FROM novedades"+where+" "+limit, null);
		countCursor=db.rawQuery("SELECT count(id) FROM novedades"+where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta=countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		//System.out.println("Tama�o: "+tamanoConsulta);
		if (c.moveToFirst()) {
			do {
				dataSet = new Novedades();
				dataSet.setId(c.getLong(0));
				dataSet.setCodigo(c.getLong(1));
				dataSet.setBarrio(c.getString(2));
				dataSet.setObservaciones(c.getInt(3));
				dataSet.setFk_usuario(c.getInt(4));
				dataSet.setLast_insert(c.getInt(5));
				dataSet.setLatitud(c.getString(6));
				dataSet.setLongitud(c.getString(7));
				dataSet.setDepartamento(c.getString(8));
				dataSet.setMunicipio(c.getString(9));
				dataSet.setOtro(c.getString(10));
				dataSet.setCliente(c.getString(11));
				dataSet.setFecha(c.getString(12));
				dataSet.setHora(c.getString(13));
				dataSet.setFk_cliente(c.getInt(14));
				dataSet.setFk_novedad(c.getInt(15));
				dataSet.setAcurracy(c.getFloat(16));
				
				//System.out.println("Item: " + c.getInt(1) + "Ciente: "+ c.getString(4) + "dir: " + c.getString(5));
				novedades.add(dataSet);
			} while (c.moveToNext());
		}
		c.close();
		countCursor.close();
		// no cerramos por singleton
		return novedades;
	}
	public synchronized int count(String condicion, Activity activity){
		Cursor countCursor = null;
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String where="";
		if(!condicion.equals("")){
			where=" WHERE "+condicion;
		}
		countCursor=db.rawQuery("SELECT count(id) FROM novedades "+where, null);
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
