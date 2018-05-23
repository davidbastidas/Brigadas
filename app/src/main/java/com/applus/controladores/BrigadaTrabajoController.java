package com.applus.controladores;

import com.applus.modelos.BrigadaTrabajoParcelable;
import com.applus.modelos.Trabajos;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BrigadaTrabajoController {

	int tamanoConsulta=0;
	public int getTamanoConsulta() {
		return tamanoConsulta;
	}

	public synchronized void insertar(ArrayList<BrigadaTrabajoParcelable> b_trabajo, Activity activity) {
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {
			int tamano = b_trabajo.size();
			if (tamano > 0) {
				for (int i = 0; i < tamano; i++) {
					db.execSQL("INSERT OR IGNORE INTO brigada_trabajo (" +
							"fk_brigada," +
							"fk_trabajo," +
							"fk_estado," +
							"observaciones" +
							") "
							+ "VALUES (" +
							""+ b_trabajo.get(i).getFk_brigada() + "," +
							""+ b_trabajo.get(i).getFk_trabajo() + "," +
							""+ b_trabajo.get(i).getFk_estado() + "," +
							"'"+ b_trabajo.get(i).getObservaciones() + "'" +
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
	        actualizados = db.update("brigada_trabajo", registro, where, null);
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
			registros=db.delete("brigada_trabajo", where, null);
			//db.execSQL("DELETE FROM brigada_trabajo");
		}
		// no cerramos por singleton
		return registros;
	}
	public synchronized ArrayList<BrigadaTrabajoParcelable> consultar(int pagina, int limite,String condicion, Activity activity){
		BrigadaTrabajoParcelable dataSet;
		ArrayList<BrigadaTrabajoParcelable> trabajos=new ArrayList<BrigadaTrabajoParcelable>();
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
		c = db.rawQuery("SELECT * FROM brigada_trabajo"+where+" "+limit, null);
		countCursor=db.rawQuery("SELECT count(id) FROM brigada_trabajo"+where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta=countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		//System.out.println("Tama�o: "+tamanoConsulta);
		if (c.moveToFirst()) {
			do {
				dataSet = new BrigadaTrabajoParcelable();
				dataSet.setId(c.getLong(0));
				dataSet.setFk_brigada(c.getInt(1));
				dataSet.setFk_trabajo(c.getInt(2));
				dataSet.setFk_estado(c.getInt(3));
				dataSet.setObservaciones(c.getString(4));
				trabajos.add(dataSet);
			} while (c.moveToNext());
		}
		c.close();
		countCursor.close();
		// no cerramos por singleton
		return trabajos;
	}
	public synchronized int count(String condicion, Activity activity){
		Cursor countCursor = null;
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String where="";
		if(!condicion.equals("")){
			where=" WHERE "+condicion;
		}
		countCursor=db.rawQuery("SELECT count(id) FROM brigada_trabajo "+where, null);
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
