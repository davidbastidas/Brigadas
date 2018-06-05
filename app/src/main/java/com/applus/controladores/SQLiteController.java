package com.applus.controladores;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteController extends SQLiteOpenHelper {

	private static SQLiteController mInstance;
	private static SQLiteDatabase myWritableDb;

	private SQLiteController(Context context) {
		super(context, "db_applus", null, 1);//se hizo cambio de bis
	}

	/**
     * Get default instance of the class to keep it a singleton
     *
     * @param context
     * the application context
     */
	public static SQLiteController getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new SQLiteController(context);
		}
		return mInstance;
	}

	/**
     * Returns a writable database instance in order not to open and close many
     * SQLiteDatabase objects simultaneously
     *
     * @return a writable instance to SQLiteDatabase
     */
	public SQLiteDatabase getMyWritableDatabase() {
		if ((myWritableDb == null) || (!myWritableDb.isOpen())) {
			myWritableDb = this.getWritableDatabase();
		}

		return myWritableDb;
	}

	@Override
    public void close() {
        super.close();
        if (myWritableDb != null) {
            myWritableDb.close();
            myWritableDb = null;
        }
    }
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Se ejecuta la sentencia SQL de todas las tablas necesarias en la base
		// de datos
		db.execSQL("create table brigada(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" descargo INTEGER," +
				" festivo INT," +
				" direccion TEXT," +
				" fecha_inicio DATE," +
				" hora_inicio TIME," +
				" fecha_final DATE," +
				" hora_final TIME," +
				" grua_hora INT," +
				" canasta_hora INT," +
				" km_adicional INT," +
				" peaje INT," +
				" almuerzo INT," +
				" hotel INT," +
				" fk_municipio INT," +
				" fk_usuario INT," +
				" last_insert INT," +
				" latitud TEXT," +
				" longitud TEXT," +
				" fecha DATE," +
				" fk_departamento INT," +
				" fk_barrio INT," +
				" fk_accion INT," +
				" acurracy VARCHAR(45)" +
				")");
		
		db.execSQL("create table brigada_material(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" fk_brigada INT," +
				" fk_material INT," +
				" cantidad DECIMAL(20,2)" +
				")");
		db.execSQL("create table brigada_trabajo(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" fk_brigada INT," +
				" fk_trabajo INT," +
				" fk_estado INT," +
				" observaciones TEXT" +
				")");
		db.execSQL("create table brigada_accion(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nombre TEXT" +
				")");
		db.execSQL("create table departamento(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nombre TEXT" +
				")");
		db.execSQL("create table estado_trabajo(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nombre TEXT" +
				")");
		db.execSQL("create table material(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nombre TEXT," +
				" medida VARCHAR(45)," +
				" cantidad DECIMAL(20,2)" +
				")");
		db.execSQL("create table municipio(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nombre TEXT," +
				" fk_departamento INT" +
				")");
		db.execSQL("create table barrio(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nombre TEXT," +
				" fk_municipio INT" +
				")");
		db.execSQL("create table novedades(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" codigo INTEGER," +
				" barrio TEXT," +
				" observaciones INT," +
				" fk_usuario INT," +
				" last_insert INT," +
				" latitud TEXT," +
				" longitud TEXT," +
				" departamento TEXT," +
				" municipio TEXT," +
				" otro TEXT," +
				" cliente TEXT," +
				" fecha DATE," +
				" hora TIME," +
				" fk_cliente INT," +
				" fk_novedad INT," +
				" acurracy VARCHAR(45)" +
				")");
		db.execSQL("create table novedad_tipo(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nombre TEXT" +
				")");
		db.execSQL("create table novedad_observacion(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nombre TEXT" +
				")");
		db.execSQL("create table totalizadores(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" fecha DATE," +
				" nic INTEGER," +
				" municipio INT," +
				" barrio INT," +
				" direccion VARCHAR(150)," +
				" ct VARCHAR(150)," +
				" mt VARCHAR(150)," +
				" estado_medida INT," +
				" observacion INT," +
				" fk_usuario INT," +
				" last_insert INT," +
				" latitud TEXT," +
				" longitud TEXT," +
				" departamento INT," +
				" otro TEXT," +
				" acurracy VARCHAR(45)" +
				")");
		db.execSQL("create table totalizador_estado_medida(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nombre TEXT" +
				")");
		db.execSQL("create table totalizador_observaciones(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nombre TEXT," +
				" fk_estado_medida INT" +
				")");
		db.execSQL("create table trabajos(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nombre TEXT" +
				")");
		db.execSQL("create table usuario(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nombre TEXT," +
				" fk_id int," +
				" nickname TEXT," +
				" tipo int" +
				")");
		db.execSQL("create table imagenes(id INTEGER PRIMARY KEY AUTOINCREMENT, ruta VARCHAR(255), nombre VARCHAR(150), no_orden BIGINT, estado INT, fecha DATE)");
		db.execSQL("create table censos(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" codigo INTEGER," +
				" nic INTEGER," +
				" formulario TEXT," +
				" datos TEXT," +
				" barrio TEXT," +
				" fk_usuario INT," +
				" last_insert INT," +
				" latitud TEXT," +
				" longitud TEXT," +
				" departamento TEXT," +
				" municipio TEXT," +
				" cliente TEXT," +
				" fecha DATE," +
				" hora TIME," +
				" fk_cliente INT," +
				" acurracy VARCHAR(45)" +
				")");

		db.execSQL("create table censo_formulario(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" formulario TEXT" +
				")");

		db.execSQL("create table clientes(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" codigo INTEGER," +
				" nombre TEXT," +
				" direccion TEXT," +
				" nic INTEGER," +
				" tipo TEXT," +
				" censo INTEGER," +
				" tipo_cliente TEXT," +
				" latitud TEXT," +
				" longitud TEXT," +
				" orden_reparto INTEGER," +
				" itinerario TEXT" +
				")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnterior,
			int versionNueva) {
	}
}
