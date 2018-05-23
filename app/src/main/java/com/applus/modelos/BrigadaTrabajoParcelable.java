package com.applus.modelos;

import android.os.Parcel;
import android.os.Parcelable;

public class BrigadaTrabajoParcelable implements Parcelable{

	long id;
	int fk_brigada,fk_trabajo,fk_estado;
	String observaciones;
	public BrigadaTrabajoParcelable() {
		super();
	}
	public BrigadaTrabajoParcelable(long id, int fk_brigada, int fk_trabajo,
			int fk_estado, String observaciones) {
		super();
		this.id=id;
		this.fk_brigada=fk_brigada;
		this.fk_trabajo=fk_trabajo;
		this.fk_estado=fk_estado;
		this.observaciones=observaciones;
	}

	public BrigadaTrabajoParcelable(Parcel source) {
		super();
		this.id = source.readLong();
		this.fk_brigada = source.readInt();
		this.fk_trabajo = source.readInt();
		this.fk_estado = source.readInt();
		this.observaciones = source.readString();
	}
	public static final Creator CREATOR = new Creator() {
		public BrigadaTrabajoParcelable createFromParcel(Parcel in) {
			return new BrigadaTrabajoParcelable(in);
		}

		public BrigadaTrabajoParcelable[] newArray(int size) {
			return new BrigadaTrabajoParcelable[size];
		}
	};
	@Override
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeInt(fk_brigada);
		dest.writeInt(fk_trabajo);
		dest.writeInt(fk_estado);
		dest.writeString(observaciones);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getFk_brigada() {
		return fk_brigada;
	}

	public void setFk_brigada(int fk_brigada) {
		this.fk_brigada = fk_brigada;
	}

	public int getFk_trabajo() {
		return fk_trabajo;
	}

	public void setFk_trabajo(int fk_trabajo) {
		this.fk_trabajo = fk_trabajo;
	}

	public int getFk_estado() {
		return fk_estado;
	}

	public void setFk_estado(int fk_estado) {
		this.fk_estado = fk_estado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

}
