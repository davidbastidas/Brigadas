package com.applus.modelos;

public class Censo {
	long id;
	long codigo;
	String barrio;
	String json;
	int fk_usuario;
	int last_insert;
	String latitud,longitud;
	String municipio, departamento;
	String fecha, hora;
	String cliente;
	long fk_cliente;
	float acurracy;
	public float getAcurracy() {
		return acurracy;
	}
	public void setAcurracy(float acurracy) {
		this.acurracy = acurracy;
	}
	public long getFk_cliente() {
		return fk_cliente;
	}
	public void setFk_cliente(long fk_cliente) {
		this.fk_cliente = fk_cliente;
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
	public Censo() {
		super();
	}
	public long getCodigo() {
		return codigo;
	}
	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
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

	public String getBarrio() {
		return barrio;
	}

	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
}