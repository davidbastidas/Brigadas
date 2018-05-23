package com.applus.controladores.interfaces;

public interface AsyncResponse {
	void processFinishLogin(String output);
	void onTablaTrabajos(String output);
	void onTablaMateriales(String output);
	void onTablaDepartamento(String output);
	void onTablaMunicipio(String output);
	void onTablaEstadoTrabajo(String output);
	void onTablaBarrios(String output);
	void onTablaBrigadaAccion(String output);
	void onTablaTotalizadorEstadoMedida(String output);
	void onTablaTotalizadorObservacion(String output);
	void onTablaNovedadTipoNovedad(String output);
	void onTablaNovedadObservacion(String output);
	void onBajarDatosTablas(boolean respuesta);
}
