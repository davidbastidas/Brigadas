package com.applus.modelos;

import android.os.Parcel;
import android.os.Parcelable;

public class ClienteAActualizar implements Parcelable {

	long id,codigo;
	String campo,datoAnterior, datoActual;
	long fkUsuario;
	int esAplicado;
	String fecha;
	String foto;
	String firma;
	int lastInsert;

	public ClienteAActualizar(){
		super();
	}

	public ClienteAActualizar(Parcel source) {
		super();
		this.id = source.readLong();
		this.codigo = source.readLong();
		this.campo = source.readString();
		this.datoAnterior = source.readString();
		this.datoActual = source.readString();
		this.fkUsuario = source.readLong();
		this.esAplicado = source.readInt();
		this.fecha = source.readString();
		this.lastInsert = source.readInt();
		this.foto = source.readString();
		this.firma = source.readString();
	}
	public static final Creator CREATOR = new Creator() {
		public ClienteAActualizar createFromParcel(Parcel in) {
			return new ClienteAActualizar(in);
		}

		public ClienteAActualizar[] newArray(int size) {
			return new ClienteAActualizar[size];
		}
	};
	@Override
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeLong(codigo);
		dest.writeString(campo);
		dest.writeString(datoAnterior);
		dest.writeString(datoActual);
		dest.writeLong(fkUsuario);
		dest.writeInt(esAplicado);
		dest.writeString(fecha);
		dest.writeInt(lastInsert);
		dest.writeString(foto);
		dest.writeString(firma);
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

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getDatoAnterior() {
		return datoAnterior;
	}

	public void setDatoAnterior(String datoAnterior) {
		this.datoAnterior = datoAnterior;
	}

	public String getDatoActual() {
		return datoActual;
	}

	public void setDatoActual(String datoActual) {
		this.datoActual = datoActual;
	}

	public long getFkUsuario() {
		return fkUsuario;
	}

	public void setFkUsuario(long fkUsuario) {
		this.fkUsuario = fkUsuario;
	}

	public int getEsAplicado() {
		return esAplicado;
	}

	public void setEsAplicado(int esAplicado) {
		this.esAplicado = esAplicado;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public int getLastInsert() {
		return lastInsert;
	}

	public void setLastInsert(int lastInsert) {
		this.lastInsert = lastInsert;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

	@Override
	public String toString() {
		return "" + codigo;
	}
}
