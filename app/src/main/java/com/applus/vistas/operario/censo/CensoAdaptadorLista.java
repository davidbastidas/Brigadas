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
	
	public CensoAdaptadorLista(Activity actividad, ArrayList<Censo> censos){
		super();
		this.actividad = actividad;
		this.censos = censos;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		inflater = actividad.getLayoutInflater();
		view = inflater.inflate(R.layout.vista_lista_general1, null, true);

		titulo = (TextView) view.findViewById(R.id.t_numero);
		if(censos.get(position).getCodigo() == 0){
			titulo.setText("Censo nuevo NIC: "+censos.get(position).getNic());
		}else{
			titulo.setText("Censo CODIGO: "+censos.get(position).getCodigo());
		}

		
		subtitulo = (TextView) view.findViewById(R.id.t_tipo_id_tipo);
		subtitulo.setText("");
		
		t_dir_barrio = (TextView) view.findViewById(R.id.t_dir_barrio);
		t_dir_barrio.setText("");
		
		t_comentario = (TextView) view.findViewById(R.id.t_comentario);
		t_comentario.setText("");
		
		t_fecha_hora = (TextView) view.findViewById(R.id.t_fecha_hora);
		t_fecha_hora.setText("Fecha: " + censos.get(position).getFecha());
		
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
