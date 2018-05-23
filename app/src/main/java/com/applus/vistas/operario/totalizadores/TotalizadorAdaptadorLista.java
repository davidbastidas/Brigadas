package com.applus.vistas.operario.totalizadores;


import com.applus.controladores.BarrioController;
import com.applus.controladores.DepartamentoController;
import com.applus.controladores.MunicipioController;
import com.applus.controladores.TotalizadorEstadoMedidaController;
import com.applus.R;
import com.applus.modelos.Barrio;
import com.applus.modelos.Departamento;
import com.applus.modelos.Municipio;
import com.applus.modelos.TotalizadorEstadoMedida;
import com.applus.modelos.Totalizadores;

import java.util.ArrayList;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TotalizadorAdaptadorLista extends BaseAdapter {
	private final Activity actividad;
	private ArrayList<Totalizadores> totalizadores;

	View view;
	LayoutInflater inflater;
	TextView titulo,subtitulo,t_dir_barrio,t_comentario,t_fecha_hora;
	ImageView imageView,image_estado;
	DepartamentoController dep=new DepartamentoController();
	ArrayList<Departamento> departamentos;
	MunicipioController mun=new MunicipioController();
	ArrayList<Municipio> municipios;
	BarrioController bar=new BarrioController();
	ArrayList<Barrio> barrios;
	String s_departamento="",s_municipio="",s_barrio="",s_estado="";
	TotalizadorEstadoMedidaController tot=new TotalizadorEstadoMedidaController();
	ArrayList<TotalizadorEstadoMedida> estados;
	
	public TotalizadorAdaptadorLista(Activity actividad, ArrayList<Totalizadores> totalizadores) {
		super();
		this.actividad = actividad;
		this.totalizadores = totalizadores;
		departamentos=dep.consultar(0, 0, "", actividad);
		municipios=mun.consultar(0, 0, "", actividad);
		barrios=bar.consultar(0, 0, "", actividad);
		estados=tot.consultar(0, 0, "", actividad);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		inflater = actividad.getLayoutInflater();
		view = inflater.inflate(R.layout.vista_lista_general1, null, true);
		
		for (Departamento depart : departamentos) {
			if(depart.getId()==totalizadores.get(position).getFk_departamento()){
				s_departamento=depart.getNombre();break;
			}
		}
		for (Municipio muni : municipios) {
			if(muni.getId()==totalizadores.get(position).getFk_municipio()){
				s_municipio=muni.getNombre();break;
			}
		}
		for (Barrio barri : barrios) {
			if(barri.getId()==totalizadores.get(position).getFk_barrio()){
				s_barrio=barri.getNombre();break;
			}
		}
		for (TotalizadorEstadoMedida est : estados) {
			if(est.getId()==totalizadores.get(position).getFk_estado_medida()){
				s_estado=est.getNombre();break;
			}
		}
		titulo = (TextView) view.findViewById(R.id.t_numero);
		titulo.setText("NIC. "+totalizadores.get(position).getNic());
		
		subtitulo = (TextView) view.findViewById(R.id.t_tipo_id_tipo);
		subtitulo.setText("DEPARTAMENTO. "+s_departamento+" - MUNICIPIO. "+s_municipio);
		
		t_dir_barrio = (TextView) view.findViewById(R.id.t_dir_barrio);
		t_dir_barrio.setText("BARRIO. "+s_barrio);
		
		t_comentario = (TextView) view.findViewById(R.id.t_comentario);
		t_comentario.setText(
				"Estado Medida: "+s_estado);
		
		t_fecha_hora = (TextView) view.findViewById(R.id.t_fecha_hora);
		t_fecha_hora.setText(totalizadores.get(position).getFecha());
		
		image_estado= (ImageView) view.findViewById(R.id.image_estado);
		if(totalizadores.get(position).getLast_insert()>0){
			image_estado.setImageResource(R.mipmap.ic_done_all_black_18dp);
		}else{
			image_estado.setImageResource(R.mipmap.ic_done_black_18dp);
		}
		return view;
	}

	public int getCount() {
		return totalizadores.size();
	}

	public Object getItem(int arg0) {
		return totalizadores.get(arg0);
	}

	public long getItemId(int position) {
		return position;
	}
}
