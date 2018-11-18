package com.applus.modelos;

public class Departamento {
	long id;
	String nombre;
	String abreviatura, url, version, urlNics, versionNics;
	public long getId() {
		return id;
	}

	public String getUrlNics() {
		return urlNics;
	}

	public void setUrlNics(String urlNics) {
		this.urlNics = urlNics;
	}

	public String getVersionNics() {
		return versionNics;
	}

	public void setVersionNics(String versionNics) {
		this.versionNics = versionNics;
	}

	public void setId(long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public Departamento(long id, String nombre, String abreviatura, String url, String version, String urlNics) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.abreviatura = abreviatura;
		this.url = url;
		this.version = version;
		this.urlNics = urlNics;
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

	public String getAbreviatura() {
		return abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
