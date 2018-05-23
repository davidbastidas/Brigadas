package com.applus.modelos;

import android.os.Parcel;
import android.os.Parcelable;

public class BrigadaParcelable implements Parcelable{

	long id;
	String descargo;
	int festivo;
	String fecha,direccion,hora_inicio,hora_final;
	int grua_hora,canasta_hora,km_adicional,peaje,almuerzo,hotel,fk_municipio,fk_usuario,last_insert;
	String latitud,longitud;
	String fecha_inicio,fecha_final;
	int fk_departamento, fk_barrio, fk_accion;
	float acurracy;
	public BrigadaParcelable() {
		super();
	}
	
	public BrigadaParcelable(Parcel source) {
		super();
		this.id = source.readLong();
		this.descargo = source.readString();
		this.festivo = source.readInt();
		this.fecha = source.readString();
		this.direccion = source.readString();
		this.hora_inicio = source.readString();
		this.hora_final = source.readString();
		this.grua_hora = source.readInt();
		this.canasta_hora = source.readInt();
		this.km_adicional = source.readInt();
		this.peaje = source.readInt();
		this.almuerzo = source.readInt();
		this.hotel = source.readInt();
		this.fk_municipio = source.readInt();
		this.fk_usuario = source.readInt();
		this.last_insert = source.readInt();
		this.latitud = source.readString();
		this.longitud = source.readString();
		this.fecha_inicio = source.readString();
		this.fecha_final = source.readString();
		this.fk_departamento = source.readInt();
		this.fk_barrio = source.readInt();
		this.fk_accion = source.readInt();
		this.acurracy = source.readFloat();
	}
	public static final Creator CREATOR = new Creator() {
		public BrigadaParcelable createFromParcel(Parcel in) {
			return new BrigadaParcelable(in);
		}

		public BrigadaParcelable[] newArray(int size) {
			return new BrigadaParcelable[size];
		}
	};
	@Override
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(descargo);
		dest.writeInt(festivo);
		dest.writeString(fecha);
		dest.writeString(direccion);
		dest.writeString(hora_inicio);
		dest.writeString(hora_final);
		dest.writeInt(grua_hora);
		dest.writeInt(canasta_hora);
		dest.writeInt(km_adicional);
		dest.writeInt(peaje);
		dest.writeInt(almuerzo);
		dest.writeInt(hotel);
		dest.writeInt(fk_municipio);
		dest.writeInt(fk_usuario);
		dest.writeInt(last_insert);
		dest.writeString(latitud);
		dest.writeString(longitud);
		dest.writeString(fecha_inicio);
		dest.writeString(fecha_final);
		dest.writeInt(fk_departamento);
		dest.writeInt(fk_barrio);
		dest.writeInt(fk_accion);
		dest.writeFloat(acurracy);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescargo() {
		return descargo;
	}

	public void setDescargo(String descargo) {
		this.descargo = descargo;
	}

	public int getFestivo() {
		return festivo;
	}

	public void setFestivo(int festivo) {
		this.festivo = festivo;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getHora_inicio() {
		return hora_inicio;
	}

	public void setHora_inicio(String hora_inicio) {
		this.hora_inicio = hora_inicio;
	}

	public String getHora_final() {
		return hora_final;
	}

	public void setHora_final(String hora_final) {
		this.hora_final = hora_final;
	}

	public int getGrua_hora() {
		return grua_hora;
	}

	public void setGrua_hora(int grua_hora) {
		this.grua_hora = grua_hora;
	}

	public int getCanasta_hora() {
		return canasta_hora;
	}

	public void setCanasta_hora(int canasta_hora) {
		this.canasta_hora = canasta_hora;
	}

	public int getKm_adicional() {
		return km_adicional;
	}

	public void setKm_adicional(int km_adicional) {
		this.km_adicional = km_adicional;
	}

	public int getPeaje() {
		return peaje;
	}

	public void setPeaje(int peaje) {
		this.peaje = peaje;
	}

	public int getAlmuerzo() {
		return almuerzo;
	}

	public void setAlmuerzo(int almuerzo) {
		this.almuerzo = almuerzo;
	}

	public int getHotel() {
		return hotel;
	}

	public void setHotel(int hotel) {
		this.hotel = hotel;
	}

	public int getFk_municipio() {
		return fk_municipio;
	}

	public void setFk_municipio(int fk_municipio) {
		this.fk_municipio = fk_municipio;
	}

	public int getFk_usuario() {
		return fk_usuario;
	}

	public void setFk_usuario(int fk_usuario) {
		this.fk_usuario = fk_usuario;
	}

	public int getLast_insert() {
		return last_insert;
	}

	public void setLast_insert(int last_insert) {
		this.last_insert = last_insert;
	}
	public String getLatitud() {
		return latitud;
	}
	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}
	public String getLongitud() {
		return longitud;
	}
	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}
	public String getFecha_inicio() {
		return fecha_inicio;
	}
	public void setFecha_inicio(String fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}
	public String getFecha_final() {
		return fecha_final;
	}
	public void setFecha_final(String fecha_final) {
		this.fecha_final = fecha_final;
	}
	public int getFk_departamento() {
		return fk_departamento;
	}

	public void setFk_departamento(int fk_departamento) {
		this.fk_departamento = fk_departamento;
	}

	public int getFk_barrio() {
		return fk_barrio;
	}

	public void setFk_barrio(int fk_barrio) {
		this.fk_barrio = fk_barrio;
	}

	public int getFk_accion() {
		return fk_accion;
	}

	public void setFk_accion(int fk_accion) {
		this.fk_accion = fk_accion;
	}
	public float getAcurracy() {
		return acurracy;
	}

	public void setAcurracy(float acurracy) {
		this.acurracy = acurracy;
	}

}
