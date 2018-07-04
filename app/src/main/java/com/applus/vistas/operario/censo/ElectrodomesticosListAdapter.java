package com.applus.vistas.operario.censo;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.applus.R;
import com.applus.controladores.BarrioController;
import com.applus.controladores.DepartamentoController;
import com.applus.controladores.MunicipioController;
import com.applus.modelos.Barrio;
import com.applus.modelos.Censo;
import com.applus.modelos.CensoForm;
import com.applus.modelos.Departamento;
import com.applus.modelos.Municipio;

import java.util.ArrayList;

public class ElectrodomesticosListAdapter extends BaseAdapter {

    public interface ElectrodomesticoListener{
        public void onEventConsumoTotal(float consumo);
    }
    ElectrodomesticoListener mlistener;
	private final Activity actividad;
	private ArrayList<CensoForm> items;

	View view;
	LayoutInflater inflater;
	TextView nombre, descripcion, cantidad;
	Button remove, add;

	float totalCenso = 0;

	public ElectrodomesticosListAdapter(ElectrodomesticoListener listener, Activity actividad, ArrayList<CensoForm> items){
		super();
		this.actividad = actividad;
		this.items = items;
        this.mlistener = listener;
	}

	public void addToList(CensoForm item){
		boolean nuevo = true;
		int size = items.size();
		for (int i = 0; i<size; i++){
			if(items.get(i).getId() == item.getId()){
				nuevo = false;
				break;
			}
		}
		if (nuevo){
		    float consumo = Float.parseFloat(item.getWatts()) * item.getCantidad();
		    item.setConsumo(consumo);
            totalCenso += item.getConsumo();
            mlistener.onEventConsumoTotal(totalCenso);
			items.add(item);
		}else{
			Toast.makeText(actividad, "El item ya se agrego, aumente la cantidad desde la lista.", Toast.LENGTH_SHORT).show();
		}

		notifyDataSetChanged();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		inflater = actividad.getLayoutInflater();
		view = inflater.inflate(R.layout.electrodomesticos_item, null, true);

		nombre = (TextView) view.findViewById(R.id.el_nombre);
		nombre.setText(items.get(position).getNombre());

		descripcion = (TextView) view.findViewById(R.id.el_descripcion);
		descripcion.setText("Consumo = "+items.get(position).getConsumo());

		cantidad = (TextView) view.findViewById(R.id.el_cantidad);
		cantidad.setText("+ (" + items.get(position).getCantidad() + ")");

		remove = (Button) view.findViewById(R.id.el_remove);
		remove.setTag(position);
		remove.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int positionRemove = (int) v.getTag();
				ArrayList<CensoForm> items2 = new ArrayList<>(items);
				int cantidad = items2.get(positionRemove).getCantidad() -1;

                float consumo = Float.parseFloat(items2.get(positionRemove).getWatts()) * cantidad;
                if(consumo > 0){
                    items2.get(positionRemove).setConsumo(consumo);
                }
                totalCenso = totalCenso - Float.parseFloat(items2.get(positionRemove).getWatts());

				if(cantidad == 0){
					items2.remove(positionRemove);
				}else{
					items2.get(positionRemove).setCantidad(cantidad);
				}
				items = items2;
				notifyDataSetChanged();//refresca la nueva lista
                mlistener.onEventConsumoTotal(totalCenso);
			}
		});

		add = (Button) view.findViewById(R.id.el_add);
		add.setTag(position);
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int positionAdd = (int) v.getTag();
				ArrayList<CensoForm> items2 = new ArrayList<>(items);
				int cantidad = items2.get(positionAdd).getCantidad() + 1;
				items2.get(positionAdd).setCantidad(cantidad);

                float consumo = Float.parseFloat(items2.get(positionAdd).getWatts()) * items2.get(positionAdd).getCantidad();
                items2.get(positionAdd).setConsumo(consumo);
                totalCenso += Float.parseFloat(items2.get(positionAdd).getWatts());

				items = items2;
				notifyDataSetChanged();//refresca la nueva lista
                mlistener.onEventConsumoTotal(totalCenso);
			}
		});

		return view;
	}

	public int getCount() {
		return items.size();
	}

	public Object getItem(int arg0) {
		return items.get(arg0);
	}

	public long getItemId(int position) {
		return position;
	}
	public ArrayList<CensoForm> getAllData(){
        return items;
    }
}
