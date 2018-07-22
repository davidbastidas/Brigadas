package com.applus.controladores;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.applus.modelos.Cliente;

import java.util.ArrayList;

public class ClientesController {

	private static String tableName = "clientes";
	int tamanoConsulta=0;
	long lastInsert;
	public long getLastInsert() {
		return lastInsert;
	}

	public int getTamanoConsulta() {
		return tamanoConsulta;
	}

	public synchronized void insertar(Cliente censo, Activity activity) {
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {
			ContentValues registro=new ContentValues();
			registro.put("codigo", censo.getCodigo());
			registro.put("nombre", censo.getNombre());
			registro.put("direccion", censo.getDireccion());
			registro.put("nic", censo.getNic());
			registro.put("tipo", censo.getTipo());
			registro.put("censo", censo.getCenso());
			registro.put("tipo_cliente", censo.getTipo_cliente());
			registro.put("latitud", censo.getLatitud());
			registro.put("longitud", censo.getLongitud());
			registro.put("orden_reparto", censo.getOrden_reparto());
			registro.put("itinerario", censo.getItinerario());
			registro.put("fk_barrio", censo.getFk_barrio());
			registro.put("censos", censo.getCensos());
			registro.put("reporte", censo.getReporte());
			lastInsert=db.insert(tableName, null, registro);
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
	public synchronized ArrayList<Cliente> consultar(int pagina, int limite,String condicion, Activity activity){
		Cliente dataSet;
		ArrayList<Cliente> cliente =new ArrayList<Cliente>();
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
				dataSet = new Cliente();
				dataSet.setId(c.getLong(0));
				dataSet.setNombre(c.getString(2));
				dataSet.setCodigo(c.getLong(1));
				dataSet.setDireccion(c.getString(3));
				dataSet.setNic(c.getLong(4));
				dataSet.setTipo(c.getString(5));
				dataSet.setCenso(c.getInt(6));
				dataSet.setTipo_cliente(c.getString(7));
				dataSet.setLatitud(c.getString(8));
				dataSet.setLongitud(c.getString(9));
				dataSet.setOrden_reparto(c.getString(10));
				dataSet.setItinerario(c.getString(11));
				dataSet.setFk_barrio(c.getInt(12));
				dataSet.setCensos(c.getString(13));
				dataSet.setReporte(c.getString(14));
				
				//System.out.println("Item: " + c.getInt(1) + "Ciente: "+ c.getString(4) + "dir: " + c.getString(5));
				cliente.add(dataSet);
			} while (c.moveToNext());
		}
		c.close();
		countCursor.close();
		// no cerramos por singleton
		return cliente;
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

	public synchronized void eliminarTodo(Activity activity){
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
			db.execSQL("DELETE FROM "+tableName);
		}
	}

	public synchronized Cliente getClienteCodigo(long codigo, Activity activity){
		Cliente dataSet = null;
		Cursor c = null;
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();

		c = db.rawQuery("SELECT * FROM " + tableName + " WHERE codigo = " + codigo, null);
		if (c.moveToFirst()) {
			do {
				dataSet = new Cliente();
				dataSet.setId(c.getLong(0));
				dataSet.setNombre(c.getString(2));
				dataSet.setCodigo(c.getLong(1));
				dataSet.setDireccion(c.getString(3));
				dataSet.setNic(c.getLong(4));
				dataSet.setTipo(c.getString(5));
				dataSet.setCenso(c.getInt(6));
				dataSet.setTipo_cliente(c.getString(7));
				dataSet.setLatitud(c.getString(8));
				dataSet.setLongitud(c.getString(9));
				dataSet.setOrden_reparto(c.getString(10));
				dataSet.setItinerario(c.getString(11));
				dataSet.setFk_barrio(c.getInt(12));
				dataSet.setCensos(c.getString(13));
				dataSet.setReporte(c.getString(14));
			} while (c.moveToNext());
		}
		c.close();

		return dataSet;
	}

	public synchronized void insertarClienteActualizar(Cliente cliente, Activity activity) {
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {
			ContentValues registro=new ContentValues();
			registro.put("codigo", cliente.getCodigo());
			registro.put("nombre", cliente.getNombre());
			registro.put("direccion", cliente.getDireccion());
			registro.put("nic", cliente.getNic());
			registro.put("tipo_cliente", cliente.getTipo_cliente());
			registro.put("reporte", cliente.getReporte());
			registro.put("last_insert", 0);
			lastInsert=db.insert("cliente_actualizar", null, registro);
		}
	}

	public synchronized ArrayList<Cliente> getClientesAActualizar(Activity activity){
		Cliente dataSet;
		ArrayList<Cliente> cliente =new ArrayList<Cliente>();
		Cursor c= null;
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		c = db.rawQuery("SELECT * FROM cliente_actualizar WHERE last_insert = 0", null);

		if (c.moveToFirst()) {
			do {
				dataSet = new Cliente();
				dataSet.setId(c.getLong(0));
				dataSet.setCodigo(c.getLong(1));
				dataSet.setNombre(c.getString(2));
				dataSet.setDireccion(c.getString(3));
				dataSet.setNic(c.getLong(4));
				dataSet.setTipo_cliente(c.getString(5));
				dataSet.setReporte(c.getString(6));
				cliente.add(dataSet);
			} while (c.moveToNext());
		}
		c.close();
		// no cerramos por singleton
		return cliente;
	}

	public synchronized int countAActualizar(String condicion, Activity activity){
		Cursor countCursor = null;
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String where="";
		if(!condicion.equals("")){
			where=" WHERE "+condicion;
		}
		countCursor=db.rawQuery("SELECT count(id) FROM cliente_actualizar " + where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta=countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		countCursor.close();
		// no cerramos por singleton
		return tamanoConsulta;
	}

	public synchronized int actualizarClienteActualizar(ContentValues registro,String where, Activity activity){
		int actualizados = 0;
		// Abrimos la base de datos 'db_ordenes' en modo escritura
		SQLiteController usdbh = SQLiteController.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {
			actualizados = db.update("cliente_actualizar", registro, where, null);
		}
		// no cerramos por singleton
		return actualizados;
	}
}
