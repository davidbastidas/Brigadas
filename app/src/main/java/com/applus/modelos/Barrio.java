package com.applus.modelos;

public class Barrio {
	long id;
	String nombre;
	int fk_municipio;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Barrio(long id, String nombre, int fk_municipio) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.fk_municipio = fk_municipio;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getFk_municipio() {
		return fk_municipio;
	}
	public void setFk_municipio(int fk_municipio) {
		this.fk_municipio = fk_municipio;
	}
	public Barrio() {
		super();
	}
	@Override
	public String toString() {
		return nombre;
	}
}
