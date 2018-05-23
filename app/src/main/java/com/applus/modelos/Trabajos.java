package com.applus.modelos;

public class Trabajos {
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
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Trabajos() {
		super();
	}
	public Trabajos(long id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}
	@Override
	public String toString() {
		return nombre;
	}
	
}
