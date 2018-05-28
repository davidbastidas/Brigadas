package com.applus.vistas.operario.censo;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.applus.R;
import com.applus.controladores.BarrioController;
import com.applus.controladores.DepartamentoController;
import com.applus.controladores.MunicipioController;
import com.applus.modelos.Barrio;
import com.applus.modelos.Censo;
import com.applus.modelos.Departamento;
import com.applus.modelos.Municipio;

import java.util.ArrayList;

public class CensoAdaptadorLista extends BaseAdapter {
	private final Activity actividad;
	private ArrayList<Censo> censos;

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
	String s_departamento="",s_municipio="",s_barrio="";
	
	public CensoAdaptadorLista(Activity actividad, ArrayList<Censo> censos){
		super();
		this.actividad = actividad;
		this.censos = censos;
		departamentos=dep.consultar(0, 0, "", actividad);
		municipios=mun.consultar(0, 0, "", actividad);
		barrios=bar.consultar(0, 0, "", actividad);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		inflater = actividad.getLayoutInflater();
		view = inflater.inflate(R.layout.vista_lista_general1, null, true);
		for (Departamento depart : departamentos) {
			if(depart.getId()==Long.parseLong(censos.get(position).getDepartamento())){
				s_departamento=depart.getNombre();break;
			}
		}
		for (Municipio muni : municipios) {
			if(muni.getId()==Long.parseLong(censos.get(position).getMunicipio())){
				s_municipio=muni.getNombre();break;
			}
		}
		for (Barrio barri : barrios) {
			if(barri.getId()==Long.parseLong(censos.get(position).getBarrio())){
				s_barrio=barri.getNombre();break;
			}
		}
		titulo = (TextView) view.findViewById(R.id.t_numero);
		titulo.setText("Censo. describir tipo");
		
		subtitulo = (TextView) view.findViewById(R.id.t_tipo_id_tipo);
		subtitulo.setText("DEPARTAMENTO. "+s_departamento+" - MUNICIPIO. "+s_municipio);
		
		t_dir_barrio = (TextView) view.findViewById(R.id.t_dir_barrio);
		t_dir_barrio.setText("BARRIO. "+s_barrio);
		
		t_comentario = (TextView) view.findViewById(R.id.t_comentario);
		t_comentario.setText(
				"CODIGO. "+ censos.get(position).getCodigo());
		
		t_fecha_hora = (TextView) view.findViewById(R.id.t_fecha_hora);
		t_fecha_hora.setText(censos.get(position).getFecha());
		
		image_estado= (ImageView) view.findViewById(R.id.image_estado);
		if(censos.get(position).getLast_insert()>0){
			image_estado.setImageResource(R.mipmap.ic_done_all_black_18dp);
		}else{
			image_estado.setImageResource(R.mipmap.ic_done_black_18dp);
		}
		return view;
	}

	public int getCount() {
		return censos.size();
	}

	public Object getItem(int arg0) {
		return censos.get(arg0);
	}

	public long getItemId(int position) {
		return position;
	}
}
