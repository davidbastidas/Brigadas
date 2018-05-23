package com.applus.controladores;

import com.applus.modelos.BrigadaParcelable;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BrigadaController {

	int tamanoConsulta=0;
	long lastInsert;
	public long getLastInsert() {
		return lastInsert;
	}

	public int getTamanoConsulta() {
		return tamanoConsulta;
	}

	public synchronized void insertar(BrigadaParcelable brigada, Activity activity) {
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {
			ContentValues registro=new ContentValues();
			registro.put("descargo", brigada.getDescargo());
			registro.put("festivo", brigada.getFestivo());
			registro.put("fecha_inicio", brigada.getFecha_inicio());
			registro.put("direccion", brigada.getDireccion());
			registro.put("fecha_final", brigada.getFecha_final());
			registro.put("hora_inicio", brigada.getHora_inicio());
			registro.put("hora_final", brigada.getHora_final());
			registro.put("grua_hora", brigada.getGrua_hora());
			registro.put("canasta_hora", brigada.getCanasta_hora());
			registro.put("km_adicional", brigada.getKm_adicional());
			registro.put("peaje", brigada.getPeaje());
			registro.put("almuerzo", brigada.getAlmuerzo());
			registro.put("hotel", brigada.getHotel());
			registro.put("fk_municipio", brigada.getFk_municipio());
			registro.put("fk_usuario", brigada.getFk_usuario());
			registro.put("last_insert", 0);
			registro.put("latitud", brigada.getLatitud());
			registro.put("longitud", brigada.getLongitud());
			registro.put("fecha", brigada.getFecha());
			registro.put("fk_departamento", brigada.getFk_departamento());
			registro.put("fk_barrio", brigada.getFk_barrio());
			registro.put("fk_accion", brigada.getFk_accion());
			registro.put("acurracy", brigada.getAcurracy());
			lastInsert=db.insert("brigada", null, registro);
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
	        actualizados = db.update("brigada", registro, where, null);
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
			registros=db.delete("brigada", where, null);
			//db.execSQL("DELETE FROM brigada");
		}
		// no cerramos por singleton
		return registros;
	}
	public synchronized ArrayList<BrigadaParcelable> consultar(int pagina, int limite,String condicion, Activity activity){
		BrigadaParcelable dataSet;
		ArrayList<BrigadaParcelable> brigadaParcelable=new ArrayList<BrigadaParcelable>();
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
		c = db.rawQuery("SELECT * FROM brigada"+where+" "+limit, null);
		countCursor=db.rawQuery("SELECT count(id) FROM brigada"+where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta=countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		//System.out.println("Tama�o: "+tamanoConsulta);
		if (c.moveToFirst()) {
			do {
				dataSet = new BrigadaParcelable();
				dataSet.setId(c.getLong(0));
				dataSet.setDescargo(c.getString(1));
				dataSet.setFestivo(c.getInt(2));
				dataSet.setDireccion(c.getString(3));
				dataSet.setFecha_inicio(c.getString(4));
				dataSet.setHora_inicio(c.getString(5));
				dataSet.setFecha_final(c.getString(6));
				dataSet.setHora_final(c.getString(7));
				dataSet.setGrua_hora(c.getInt(8));
				dataSet.setCanasta_hora(c.getInt(9));
				dataSet.setKm_adicional(c.getInt(10));
				dataSet.setPeaje(c.getInt(11));
				dataSet.setAlmuerzo(c.getInt(12));
				dataSet.setHotel(c.getInt(13));
				dataSet.setFk_municipio(c.getInt(14));
				dataSet.setFk_usuario(c.getInt(15));
				dataSet.setLast_insert(c.getInt(16));
				dataSet.setLatitud(c.getString(17));
				dataSet.setLongitud(c.getString(18));
				dataSet.setFecha(c.getString(19));
				dataSet.setFk_departamento(c.getInt(20));
				dataSet.setFk_barrio(c.getInt(21));
				dataSet.setFk_accion(c.getInt(22));
				dataSet.setAcurracy(c.getFloat(23));
				
				//System.out.println("Item: " + c.getInt(1) + "Ciente: "+ c.getString(4) + "dir: " + c.getString(5));
				brigadaParcelable.add(dataSet);
			} while (c.moveToNext());
		}
		c.close();
		countCursor.close();
		// no cerramos por singleton
		return brigadaParcelable;
	}
	public synchronized int count(String condicion, Activity activity){
		Cursor countCursor = null;
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String where="";
		if(!condicion.equals("")){
			where=" WHERE "+condicion;
		}
		countCursor=db.rawQuery("SELECT count(id) FROM brigada "+where, null);
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
