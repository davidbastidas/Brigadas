package com.applus.modelos;

import android.os.Parcel;
import android.os.Parcelable;

public class ImagenesParcelable implements Parcelable{

	String ruta,nombre;
	int id,estado;
	String fecha;
	long no_orden;
	
	public ImagenesParcelable(){
	}
	public ImagenesParcelable(int id,String ruta,String nombre,int estado,String fecha,long no_orden){
		super();
		this.id = id;
		this.ruta = ruta;
		this.nombre=nombre;
		this.estado = estado;
		this.fecha = fecha;
		this.no_orden = no_orden;
	}
	public ImagenesParcelable(Parcel source) {
		this.id = source.readInt();
		this.ruta = source.readString();
		this.nombre = source.readString();
		this.estado = source.readInt();
		this.fecha = source.readString();
		this.no_orden = source.readLong();
	}
	public static final Creator CREATOR = new Creator() {
		public ImagenesParcelable createFromParcel(Parcel in) {
			return new ImagenesParcelable(in);
		}

		public ImagenesParcelable[] newArray(int size) {
			return new ImagenesParcelable[size];
		}
	};
	@Override
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(ruta);
		dest.writeString(nombre);
		dest.writeInt(estado);
		dest.writeString(fecha);
		dest.writeLong(no_orden);
	}
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public long getNo_orden() {
		return no_orden;
	}
	public void setNo_orden(long no_orden) {
		this.no_orden = no_orden;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
}
