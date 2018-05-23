package com.applus.modelos;

public class Usuario {

	long id;
	String nombre;
	int fk_id;
	String nickname;
	int tipo;
	public Usuario() {
		super();
	}
	public Usuario(long id, String nombre, int fk_id, String nickname,int tipo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.fk_id = fk_id;
		this.nickname = nickname;
		this.tipo = tipo;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
	public int getFk_id() {
		return fk_id;
	}
	public void setFk_id(int fk_id) {
		this.fk_id = fk_id;
	}
}
