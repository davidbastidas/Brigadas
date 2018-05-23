package com.applus.modelos;

public class Material {
	long id;
	String nombre,medida;
	float cantidad;
	public String getMedida() {
		return medida;
	}
	public void setMedida(String medida) {
		this.medida = medida;
	}
	public float getCantidad() {
		return cantidad;
	}
	public void setCantidad(float cantidad) {
		this.cantidad = cantidad;
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
	public Material() {
		super();
	}
	public Material(long id, String nombre, String medida, float cantidad) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.medida = medida;
		this.cantidad = cantidad;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return nombre;
	}
	
}
