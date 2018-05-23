package com.applus.modelos;

public class Municipio {
	long id;
	String nombre;
	int fk_departamento;
	public int getFk_departamento() {
		return fk_departamento;
	}
	public void setFk_departamento(int fk_departamento) {
		this.fk_departamento = fk_departamento;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Municipio(long id, String nombre, int fk_departamento) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.fk_departamento = fk_departamento;
	}
	public Municipio() {
		super();
	}
	@Override
	public String toString() {
		return nombre;
	}
}
