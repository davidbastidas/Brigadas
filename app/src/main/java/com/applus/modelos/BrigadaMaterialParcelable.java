package com.applus.modelos;

import android.os.Parcel;
import android.os.Parcelable;

public class BrigadaMaterialParcelable implements Parcelable{

	long id;
	int fk_brigada,fk_material;
	float cantidad;

	public BrigadaMaterialParcelable() {
		super();
	}
	public BrigadaMaterialParcelable(long id, int fk_brigada,int fk_material, float cantidad) {
		super();
		this.id=id;
		this.fk_brigada=fk_brigada;
		this.fk_material=fk_material;
		this.cantidad=cantidad;
	}

	public BrigadaMaterialParcelable(Parcel source) {
		super();
		this.id = source.readLong();
		this.fk_brigada = source.readInt();
		this.fk_material = source.readInt();
		this.cantidad = source.readFloat();
	}
	public static final Creator CREATOR = new Creator() {
		public BrigadaMaterialParcelable createFromParcel(Parcel in) {
			return new BrigadaMaterialParcelable(in);
		}

		public BrigadaMaterialParcelable[] newArray(int size) {
			return new BrigadaMaterialParcelable[size];
		}
	};
	@Override
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeInt(fk_brigada);
		dest.writeInt(fk_material);
		dest.writeFloat(cantidad);
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

	public int getFk_material() {
		return fk_material;
	}

	public void setFk_material(int fk_material) {
		this.fk_material = fk_material;
	}

	public float getCantidad() {
		return cantidad;
	}

	public void setCantidad(float cantidad) {
		this.cantidad = cantidad;
	}
}
