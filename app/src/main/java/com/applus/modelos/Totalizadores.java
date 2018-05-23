package com.applus.modelos;

public class Totalizadores {
	long id;
	String fecha;
	long nic;
	String direccion, ct, mt;
	int fk_usuario, last_insert;
	int fk_municipio, fk_barrio, fk_departamento, fk_estado_medida, fk_observacion;
	String otro;
	float acurracy;
	public float getAcurracy() {
		return acurracy;
	}
	public void setAcurracy(float acurracy) {
		this.acurracy = acurracy;
	}
	public String getOtro() {
		return otro;
	}
	public void setOtro(String otro) {
		this.otro = otro;
	}
	public int getFk_barrio() {
		return fk_barrio;
	}
	public void setFk_barrio(int fk_barrio) {
		this.fk_barrio = fk_barrio;
	}
	public int getFk_departamento() {
		return fk_departamento;
	}
	public void setFk_departamento(int fk_departamento) {
		this.fk_departamento = fk_departamento;
	}
	public int getFk_estado_medida() {
		return fk_estado_medida;
	}
	public void setFk_estado_medida(int fk_estado_medida) {
		this.fk_estado_medida = fk_estado_medida;
	}
	public int getFk_observacion() {
		return fk_observacion;
	}
	public void setFk_observacion(int fk_observacion) {
		this.fk_observacion = fk_observacion;
	}
	String latitud, longitud;
	public Totalizadores() {
		super();
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public long getNic() {
		return nic;
	}
	public void setNic(long nic) {
		this.nic = nic;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getCt() {
		return ct;
	}
	public void setCt(String ct) {
		this.ct = ct;
	}
	public String getMt() {
		return mt;
	}
	public void setMt(String mt) {
		this.mt = mt;
	}
	public int getFk_usuario() {
		return fk_usuario;
	}
	public void setFk_usuario(int fk_usuario) {
		this.fk_usuario = fk_usuario;
	}
	public int getLast_insert() {
		return last_insert;
	}
	public void setLast_insert(int last_insert) {
		this.last_insert = last_insert;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getFk_municipio() {
		return fk_municipio;
	}
	public void setFk_municipio(int fk_municipio) {
		this.fk_municipio = fk_municipio;
	}
	public String getLatitud() {
		return latitud;
	}
	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}
	public String getLongitud() {
		return longitud;
	}
	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}
}
