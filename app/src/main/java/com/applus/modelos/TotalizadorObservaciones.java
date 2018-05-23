package com.applus.modelos;

public class TotalizadorObservaciones {
	long id;
	String nombre;
	int fk_estado_medida;
	
	public TotalizadorObservaciones(long id, String nombre, int fk_estado_medida) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.fk_estado_medida = fk_estado_medida;
	}
	public int getFk_estado_medida() {
		return fk_estado_medida;
	}
	public void setFk_estado_medida(int fk_estado_medida) {
		this.fk_estado_medida = fk_estado_medida;
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
	public TotalizadorObservaciones() {
		super();
	}
	@Override
	public String toString() {
		return nombre;
	}
}
