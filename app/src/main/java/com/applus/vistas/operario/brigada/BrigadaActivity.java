package com.applus.vistas.operario.brigada;

import com.applus.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

public class BrigadaActivity extends Activity {

	FragmentManager fragmentManager;
	Fragment fragment = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_brigada);
		fragment = new BrigadaFormFragment();
		if (fragment != null) {
			iniciarFragment(fragment);
		}
	}
	/*
	public void iniciarGuardado(){
		if (guardadoActivo) {
			guardadoActivo=false;
			
			dialogoDinamico("Servicio", "Enviado con Exito!");
			conexionCon.setActivity(this);
			//iniciar guardado
			
			msg = new Message();
			msg.obj = "Guardando en el Telefono...";
			handlerProgreso.sendMessage(msg);
			if(guardarOrden(this.os)){
				
				guardarImagenes(listaFotos);
				if(sesion.isINTERNET()){//si internet
					msg = new Message();
					msg.obj = "Enviando por internet...";
					handlerProgreso.sendMessage(msg);
					//conexionCon.setOrdenes(ordenEnviar,obsOrden);
				}
				
			}else{
				Toast.makeText(this, "No se puede guardar la Orden",
						Toast.LENGTH_LONG).show();
			}
			progressDialog.dismiss();
		}
	}
	private boolean guardarOrden(OrdenesParcelable orden1) {
		boolean resp=false;
		BridagaController oc = new BridagaController();
		ContentValues registro = new ContentValues();
		registro.put("anomalia",orden1.getAnomalia());
		registro.put("tipo_cliente_cod",orden1.getTipoClienteCod());
		registro.put("fk_irregularidad",orden1.getFk_irregularidad());
		registro.put("total_censo",orden1.getTotalCenso());
		registro.put("meses_pago",orden1.getMesesFinanciacion());
		registro.put("tipo_pago",orden1.getIndicePago());
		registro.put("indice_anomalia",orden1.getIndiceAnomalia());
		registro.put("observaciones",orden1.getObservacion());
		//registro.put("lectura_medidor",orden1.getLectura_med());*
		
		registro.put("latitud",orden1.getLatitud());
		registro.put("longitud",orden1.getLongitud());
		registro.put("fecha",cal.getFechaActual());
		registro.put("hora",cal.getHoraActual());
		registro.put("estado","2");
		
		int actualiza = oc.actualizar(registro, "no_orden="+ orden1.getNo_orden(), this);
		if(actualiza==1){
			//guardar el array observaciones
			oc.insertarObservacionOrdenPaso(obsOrden, this);
			//guardar el array manoObra
			ManoObraController m=new ManoObraController();
			m.insertarObraOrden(manoObra, this, ""+orden1.getNo_orden());
			resp=true;
		}
		return resp;
	}*/

	public void iniciarFragment(Fragment fragment){
		fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.frame_brigada_container, fragment).commit();
	}
}
