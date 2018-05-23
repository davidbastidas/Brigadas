package com.applus.modelos;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Calendario {

	public String getHoraActual() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss");
		return df1.format(c.getTime());
	}
	public String getFechaActual() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		return df1.format(c.getTime());
	}
	public Date getCadenaAFecha(int ano,int mes,int dia) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, ano);
        c.set(Calendar.MONTH, mes);
        c.set(Calendar.DAY_OF_MONTH, dia);
        return c.getTime();
    }
}
