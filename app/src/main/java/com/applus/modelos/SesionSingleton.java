package com.applus.modelos;


public class SesionSingleton {
	private static SesionSingleton mInstance;

	public static SesionSingleton getInstance() {
		if (mInstance == null) {
			mInstance = new SesionSingleton();
		}
		return mInstance;
	}
	private String IP;
	private String PROYECTO;
	
	private String RUTA_ALMACENAMIENTO;
	private String RUTA_GUARDAR_DATOS;
	private int MODO_VISTA;//1 limitada, 2 total
	private boolean INTERNET;
	
	private String PASS_ADMIN;
	private String PASS_ORDENES;
	private String nombre_operario;
	private int tipo_usuario;
	public int getTipo_usuario() {
		return tipo_usuario;
	}
	public void setTipo_usuario(int tipo_usuario) {
		this.tipo_usuario = tipo_usuario;
	}
	private int fk_id_operario;
	
	private int conteoFotos;
	private boolean pasaLogin;
	
	private String estado_datos;
	private String estado_envio;
	
	public String getEstado_datos() {
		return estado_datos;
	}
	public void setEstado_datos(String estado_datos) {
		this.estado_datos = estado_datos;
	}
	public String getEstado_envio() {
		return estado_envio;
	}
	public void setEstado_envio(String estado_envio) {
		this.estado_envio = estado_envio;
	}
	public int getFk_id_operario() {
		return fk_id_operario;
	}
	public void setFk_id_operario(int fk_id_operario) {
		this.fk_id_operario = fk_id_operario;
	}
	public String getNombreOperario() {
		return nombre_operario;
	}
	public void setNombreOperario(String nombre_operario) {
		this.nombre_operario = nombre_operario;
	}
	public boolean isPasaLogin() {
		return pasaLogin;
	}
	public void setPasaLogin(boolean pasaLogin) {
		this.pasaLogin = pasaLogin;
	}
	public int getConteoFotos() {
		return conteoFotos;
	}
	public void setConteoFotos(int conteoFotos) {
		this.conteoFotos = conteoFotos;
	}
	
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public String getRUTA_ALMACENAMIENTO() {
		return RUTA_ALMACENAMIENTO;
	}
	public void setRUTA_ALMACENAMIENTO(String rUTA_ALMACENAMIENTO) {
		RUTA_ALMACENAMIENTO = rUTA_ALMACENAMIENTO;
	}
	public String getRUTA_GUARDAR_DATOS() {
		return RUTA_GUARDAR_DATOS;
	}
	public void setRUTA_GUARDAR_DATOS(String rUTA_GUARDAR_DATOS) {
		RUTA_GUARDAR_DATOS = rUTA_GUARDAR_DATOS;
	}
	public int getMODO_VISTA() {
		return MODO_VISTA;
	}
	public void setMODO_VISTA(int mODO_VISTA) {
		MODO_VISTA = mODO_VISTA;
	}
	public boolean isINTERNET() {
		return INTERNET;
	}
	public void setINTERNET(boolean iNTERNET) {
		INTERNET = iNTERNET;
	}
	public String getPASS_ADMIN() {
		return PASS_ADMIN;
	}
	public void setPASS_ADMIN(String pASS_ADMIN) {
		PASS_ADMIN = pASS_ADMIN;
	}
	public String getPASS_ORDENES() {
		return PASS_ORDENES;
	}
	public void setPASS_ORDENES(String pASS_ORDENES) {
		PASS_ORDENES = pASS_ORDENES;
	}
	public String getPROYECTO() {
		return PROYECTO;
	}
	public void setPROYECTO(String pROYECTO) {
		PROYECTO = pROYECTO;
	}
	
}
