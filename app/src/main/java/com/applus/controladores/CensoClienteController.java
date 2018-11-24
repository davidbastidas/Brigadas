package com.applus.controladores;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.applus.modelos.Censo;
import com.applus.modelos.CensoCliente;

import java.util.ArrayList;

public class CensoClienteController {

	private static String tableName = "censos_clientes";
	int tamanoConsulta=0;
	long lastInsert;
	public long getLastInsert() {
		return lastInsert;
	}

	public int getTamanoConsulta() {
		return tamanoConsulta;
	}

	public synchronized void insertar(CensoCliente censo, Activity activity) {
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {
			ContentValues registro=new ContentValues();
			registro.put("cliente_id", censo.getClienteId());
			registro.put("fecha", censo.getFecha());
			registro.put("estado", censo.getEstado());
			registro.put("usuario", censo.getUsuario());
			registro.put("electrodomesticos", "");
			registro.put("aprobado", censo.getAprobado());
			lastInsert = db.insert(tableName, null, registro);
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
	public synchronized void eliminarTodo(Activity activity){
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
			db.execSQL("DELETE FROM "+tableName);
		}
	}
	public synchronized ArrayList<CensoCliente> consultar(int pagina, int limite,String condicion, Activity activity){
		CensoCliente dataSet;
		ArrayList<CensoCliente> censos =new ArrayList<CensoCliente>();
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
		countCursor = db.rawQuery("SELECT count(id) FROM " + tableName + " " + where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta = countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		//System.out.println("Tama�o: "+tamanoConsulta);
		if (c.moveToFirst()) {
			do {
				dataSet = new CensoCliente();
				dataSet.setId(c.getLong(0));
				dataSet.setClienteId(c.getLong(1));
				dataSet.setFecha(c.getString(2));
				dataSet.setEstado(c.getString(3));
				dataSet.setUsuario(c.getString(4));
				dataSet.setAprobado(c.getInt(5));
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
