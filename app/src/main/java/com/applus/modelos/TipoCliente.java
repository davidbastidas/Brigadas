package com.applus.modelos;

public class TipoCliente {
	String tag;
	String nombre;

	public TipoCliente(String tag, String nombre) {
		this.tag = tag;
		this.nombre = nombre;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public TipoCliente() {
		super();
	}
	@Override
	public String toString() {
		return nombre;
	}
}
