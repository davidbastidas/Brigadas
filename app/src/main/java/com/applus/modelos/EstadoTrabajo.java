package com.applus.modelos;

public class EstadoTrabajo {
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
	public EstadoTrabajo() {
		super();
	}
	public EstadoTrabajo(long id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}
	@Override
	public String toString() {
		return nombre;
	}
}
