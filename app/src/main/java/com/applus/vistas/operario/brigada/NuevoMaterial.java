package com.applus.vistas.operario.brigada;

import com.applus.controladores.MaterialController;
import com.applus.R;
import com.applus.modelos.EstadoTrabajo;
import com.applus.modelos.Material;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class NuevoMaterial extends DialogFragment {

	private OnAddMaterial callback=null;
	public NuevoMaterial(OnAddMaterial callback){
		this.callback=callback;
	}
    public interface OnAddMaterial {
        public void onSaveMaterial(int id, float cantidad);
    }
	AutoCompleteTextView n_material;
	EditText n_cantidad;
	Material materialFinal = null;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.nuevo_material, null);
		n_material = (AutoCompleteTextView) view.findViewById(R.id.n_material);
		n_cantidad = (EditText) view.findViewById(R.id.n_cantidad);

		MaterialController mat = new MaterialController();
		ArrayList<Material> material = mat.consultar(0, 0, "", getActivity());
		n_material.setAdapter(new AutoCompleteAdapterMaterial(
				getActivity(), material));
		n_material.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				materialFinal = (Material) parent.getItemAtPosition(position);
				Toast.makeText(getActivity(),
						"Nombre: " + materialFinal.getNombre() + "",
						Toast.LENGTH_SHORT).show();
			}
		});
		builder.setView(view);
		builder.setTitle("Nuevo Material");
		builder.setNegativeButton("Cancelar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						//getDialog().dismiss();
					}
				});
		builder.setPositiveButton("Guardar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						
					}
				});
		builder.setCancelable(true);
		AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                final DialogInterface d = dialog;
                Button buttonOK = ((AlertDialog)dialog).getButton( DialogInterface.BUTTON_POSITIVE );
                buttonOK.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                    	if(n_cantidad.getText().toString().equals("")){
                    		Toast.makeText(getActivity(), "Por favor digite la cantidad",
                					Toast.LENGTH_LONG).show();
                    	}else{
                    		if(Float.parseFloat(n_cantidad.getText().toString())>0){
                    			if(materialFinal!=null){
                                	float cantidad=0;
            						cantidad=Float.parseFloat(n_cantidad.getText().toString());
            						callback.onSaveMaterial(
            								(int)materialFinal.getId(),
            								cantidad);
                                	getDialog().dismiss();
                                }else{
                                	Toast.makeText(getActivity(), "Por favor Elige de la LISTA el MATERIAL",
                        					Toast.LENGTH_LONG).show();
                                }
                        	}else{
                        		Toast.makeText(getActivity(), "Por favor la cantidad debe ser mayor a CERO",
                    					Toast.LENGTH_LONG).show();
                        	}
                    		
                    	}
                    }
                });
            }
        });
		return dialog;
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	private class AutoCompleteAdapterMaterial extends ArrayAdapter<Material>
			implements Filterable {

		private LayoutInflater mInflater;
		private ArrayList<Material> originalList;
		private ArrayList<Material> trabajosList;
		private MaterialFilter filter;

		public AutoCompleteAdapterMaterial(final Context context,
				ArrayList<Material> trabajos) {
			super(context, -1, trabajos);
			mInflater = LayoutInflater.from(context);
			this.trabajosList = new ArrayList<Material>();
			this.trabajosList.addAll(trabajos);
			this.originalList = new ArrayList<Material>();
			this.originalList.addAll(trabajos);
		}

		@Override
		public View getView(final int position, final View convertView,
				final ViewGroup parent) {
			final TextView tv;
			if (convertView != null) {
				tv = (TextView) convertView;
			} else {
				tv = (TextView) mInflater.inflate(
						android.R.layout.simple_dropdown_item_1line, parent,
						false);
			}

			tv.setText(getItem(position).getNombre());
			return tv;
		}

		@Override
		public Filter getFilter() {
			if (filter == null) {
				filter = new MaterialFilter();
			}
			return filter;
		}

		private class MaterialFilter extends Filter {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				constraint = constraint.toString().toLowerCase();
				FilterResults result = new FilterResults();
				if (constraint != null && constraint.toString().length() > 0) {
					ArrayList<Material> filteredItems = new ArrayList<Material>();

					for (int i = 0, l = originalList.size(); i < l; i++) {
						Material mar = originalList.get(i);
						if (mar.toString().toLowerCase().contains(constraint))
							filteredItems.add(mar);
					}
					result.count = filteredItems.size();
					result.values = filteredItems;
				} else {
					synchronized (this) {
						result.values = originalList;
						result.count = originalList.size();
					}
				}
				return result;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				trabajosList = (ArrayList<Material>) results.values;
				if (trabajosList != null) {
					notifyDataSetChanged();
					System.out.println("Tama�o de trabajos: "
							+ trabajosList.size());
					clear();
					for (int i = 0, l = trabajosList.size(); i < l; i++)
						add(trabajosList.get(i));
					notifyDataSetInvalidated();
				}
			}
		}
	}

	private class AutoCompleteAdapterEstadoTrabajos extends ArrayAdapter<EstadoTrabajo>
			implements Filterable {

		private LayoutInflater mInflater;
		private ArrayList<EstadoTrabajo> originalList;
		private ArrayList<EstadoTrabajo> trabajosList;
		private EstadoTrabajosFilter filter;

		public AutoCompleteAdapterEstadoTrabajos(final Context context,
				ArrayList<EstadoTrabajo> trabajos) {
			super(context, -1, trabajos);
			mInflater = LayoutInflater.from(context);
			this.trabajosList = new ArrayList<EstadoTrabajo>();
			this.trabajosList.addAll(trabajos);
			this.originalList = new ArrayList<EstadoTrabajo>();
			this.originalList.addAll(trabajos);
		}

		@Override
		public View getView(final int position, final View convertView,
				final ViewGroup parent) {
			final TextView tv;
			if (convertView != null) {
				tv = (TextView) convertView;
			} else {
				tv = (TextView) mInflater.inflate(
						android.R.layout.simple_dropdown_item_1line, parent,
						false);
			}

			tv.setText(getItem(position).getNombre());
			return tv;
		}

		@Override
		public Filter getFilter() {
			if (filter == null) {
				filter = new EstadoTrabajosFilter();
			}
			return filter;
		}

		private class EstadoTrabajosFilter extends Filter {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				constraint = constraint.toString().toLowerCase();
				FilterResults result = new FilterResults();
				if (constraint != null && constraint.toString().length() > 0) {
					ArrayList<EstadoTrabajo> filteredItems = new ArrayList<EstadoTrabajo>();

					for (int i = 0, l = originalList.size(); i < l; i++) {
						EstadoTrabajo mar = originalList.get(i);
						if (mar.toString().toLowerCase().contains(constraint))
							filteredItems.add(mar);
					}
					result.count = filteredItems.size();
					result.values = filteredItems;
				} else {
					synchronized (this) {
						result.values = originalList;
						result.count = originalList.size();
					}
				}
				return result;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				trabajosList = (ArrayList<EstadoTrabajo>) results.values;
				if (trabajosList != null) {
					notifyDataSetChanged();
					System.out.println("Tama�o de estado trabajos: "
							+ trabajosList.size());
					clear();
					for (int i = 0, l = trabajosList.size(); i < l; i++)
						add(trabajosList.get(i));
					notifyDataSetInvalidated();
				}
			}
		}
	}

}
