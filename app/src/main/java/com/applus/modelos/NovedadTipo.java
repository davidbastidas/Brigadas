package com.applus.modelos;

public class NovedadTipo {
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
	public NovedadTipo(long id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public NovedadTipo() {
		super();
	}
	@Override
	public String toString() {
		return nombre;
	}
}
