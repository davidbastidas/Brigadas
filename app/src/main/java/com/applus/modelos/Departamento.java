package com.applus.modelos;

public class Departamento {
	long id;
	String nombre;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public Departamento(long id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Departamento() {
		super();
	}
	@Override
	public String toString() {
		return nombre;
	}
}
