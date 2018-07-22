package com.applus.modelos;

import android.os.Parcel;
import android.os.Parcelable;

public class Cliente implements Parcelable {

	long id,codigo;
	String nombre,direccion;
	long nic;
	String tipo;
	int censo;
	String tipo_cliente,longitud,latitud,orden_reparto;
	String itinerario;
	int fk_distrito,fk_municipio,fk_barrio;
	String distrito,municipio,barrio;
	String censos;
	String reporte;
	public Cliente(){
		super();
	}

	public Cliente(Parcel source) {
		super();
		this.id = source.readLong();
		this.codigo = source.readLong();
		this.nombre = source.readString();
		this.direccion = source.readString();
		this.nic = source.readLong();
		this.tipo = source.readString();
		this.censo = source.readInt();
		this.tipo_cliente = source.readString();
		this.longitud = source.readString();
		this.latitud = source.readString();
		this.orden_reparto = source.readString();
		this.itinerario = source.readString();
		this.fk_distrito = source.readInt();
		this.fk_municipio = source.readInt();
		this.fk_barrio = source.readInt();
		this.distrito = source.readString();
		this.municipio = source.readString();
		this.barrio = source.readString();
		this.censos = source.readString();
		this.reporte = source.readString();
	}
	public static final Creator CREATOR = new Creator() {
		public Cliente createFromParcel(Parcel in) {
			return new Cliente(in);
		}

		public Cliente[] newArray(int size) {
			return new Cliente[size];
		}
	};
	@Override
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeLong(codigo);
		dest.writeString(nombre);
		dest.writeString(direccion);
		dest.writeLong(nic);
		dest.writeString(tipo);
		dest.writeInt(censo);
		dest.writeString(tipo_cliente);
		dest.writeString(longitud);
		dest.writeString(latitud);
		dest.writeString(orden_reparto);
		dest.writeString(itinerario);
		dest.writeInt(fk_distrito);
		dest.writeInt(fk_municipio);
		dest.writeInt(fk_barrio);
		dest.writeString(distrito);
		dest.writeString(municipio);
		dest.writeString(barrio);
		dest.writeString(censos);
		dest.writeString(reporte);
	}

	public String getReporte() {
		return reporte;
	}

	public void setReporte(String reporte) {
		this.reporte = reporte;
	}

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

	public String getCensos() {
		return censos;
	}

	public void setCensos(String censos) {
		this.censos = censos;
	}

	@Override
	public String toString() {
		return "" + nic;
	}
}
