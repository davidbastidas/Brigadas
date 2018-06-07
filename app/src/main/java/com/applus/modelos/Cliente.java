package com.applus.modelos;

public class Cliente {

	long id,codigo;
	String nombre,direccion;
	long nic;
	String tipo;
	int censo;
	String tipo_cliente,longitud,latitud,orden_reparto;
	String itinerario;
	int fk_distrito,fk_municipio,fk_barrio;
	String distrito,municipio,barrio;

	public String getDistrito() {
		return distrito;
	}
	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public String getBarrio() {
		return barrio;
	}
	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCodigo() {
		return codigo;
	}
	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public long getNic() {
		return nic;
	}
	public void setNic(long nic) {
		this.nic = nic;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public int getCenso() {
		return censo;
	}
	public void setCenso(int censo) {
		this.censo = censo;
	}
	public String getTipo_cliente() {
		return tipo_cliente;
	}
	public void setTipo_cliente(String tipo_cliente) {
		this.tipo_cliente = tipo_cliente;
	}
	public String getLongitud() {
		return longitud;
	}
	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}
	public String getLatitud() {
		return latitud;
	}
	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}
	public String getOrden_reparto() {
		return orden_reparto;
	}
	public void setOrden_reparto(String orden_reparto) {
		this.orden_reparto = orden_reparto;
	}
	public String getItinerario() {
		return itinerario;
	}
	public void setItinerario(String itinerario) {
		this.itinerario = itinerario;
	}
	public int getFk_distrito() {
		return fk_distrito;
	}
	public void setFk_distrito(int fk_distrito) {
		this.fk_distrito = fk_distrito;
	}
	public int getFk_municipio() {
		return fk_municipio;
	}
	public void setFk_municipio(int fk_municipio) {
		this.fk_municipio = fk_municipio;
	}
	public int getFk_barrio() {
		return fk_barrio;
	}
	public void setFk_barrio(int fk_barrio) {
		this.fk_barrio = fk_barrio;
	}

	@Override
	public String toString() {
		return "" + nic;
	}
}
