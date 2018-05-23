package com.applus.vistas.operario.brigada;

import java.util.ArrayList;

import com.applus.controladores.EstadoTrabajoController;
import com.applus.controladores.TrabajosController;
import com.applus.R;
import com.applus.modelos.EstadoTrabajo;
import com.applus.modelos.Trabajos;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class NuevoTrabajo extends DialogFragment {

	public OnAddTrabajo callback=null;
	public NuevoTrabajo(OnAddTrabajo callback){
		this.callback=callback;
	}
    public interface OnAddTrabajo {
        public void onSaveTrabajo(int trabajo, int estado_trabajo, String obs);
    }
	AutoCompleteTextView trabajo_realizado, ejecucion_trabajo;
	EditText nt_observaciones;
	Trabajos trabajoFinal = null;
	EstadoTrabajo eTrabajoFinal = null;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.nuevo_trabajo, null);
		trabajo_realizado = (AutoCompleteTextView) view
				.findViewById(R.id.trabajo_realizado);
		ejecucion_trabajo = (AutoCompleteTextView) view
				.findViewById(R.id.ejecucion_trabajo);
		nt_observaciones = (EditText) view.findViewById(R.id.nt_observaciones);

		TrabajosController tb = new TrabajosController();
		ArrayList<Trabajos> trabajos = tb.consultar(0, 0, "", getActivity());
		trabajo_realizado.setAdapter(new AutoCompleteAdapterTrabajos(
				getActivity(), trabajos));
		trabajo_realizado.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				trabajoFinal = (Trabajos) parent.getItemAtPosition(position);
				Toast.makeText(getActivity(),
						"Nombre: " + trabajoFinal.getNombre() + "",
						Toast.LENGTH_SHORT).show();
			}
		});

		EstadoTrabajoController et = new EstadoTrabajoController();
		ArrayList<EstadoTrabajo> esttrab = et.consultar(0, 0, "", getActivity());
		ejecucion_trabajo.setAdapter(new AutoCompleteAdapterEstadoTrabajos(
				getActivity(), esttrab));
		ejecucion_trabajo.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				eTrabajoFinal = (EstadoTrabajo) parent.getItemAtPosition(position);
				Toast.makeText(getActivity(),
						"Nombre: " + eTrabajoFinal.getNombre() + "",
						Toast.LENGTH_SHORT).show();
			}
		});

		builder.setView(view);
		builder.setTitle("Nuevo Trabajo");

		builder.setNegativeButton("Cancelar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						//getDialog().dismiss();
					}
				});
		builder.setPositiveButton("Guardar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						/**/
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
                        if(trabajoFinal!=null  && eTrabajoFinal!=null){
                        	callback.onSaveTrabajo(
    								(int)trabajoFinal.getId(),
    								(int)eTrabajoFinal.getId(),
    								nt_observaciones.getText().toString());
                        	getDialog().dismiss();
                        }else{
                        	Toast.makeText(getActivity(), "Por favor Elige de la LISTA el Trabajo y su Estado",
                					Toast.LENGTH_LONG).show();
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

	private class AutoCompleteAdapterTrabajos extends ArrayAdapter<Trabajos>
			implements Filterable {

		private LayoutInflater mInflater;
		private ArrayList<Trabajos> originalList;
		private ArrayList<Trabajos> trabajosList;
		private TrabajosFilter filter;

		public AutoCompleteAdapterTrabajos(final Context context,
				ArrayList<Trabajos> trabajos) {
			super(context, -1, trabajos);
			mInflater = LayoutInflater.from(context);
			this.trabajosList = new ArrayList<Trabajos>();
			this.trabajosList.addAll(trabajos);
			this.originalList = new ArrayList<Trabajos>();
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
				filter = new TrabajosFilter();
			}
			return filter;
		}

		private class TrabajosFilter extends Filter {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				constraint = constraint.toString().toLowerCase();
				FilterResults result = new FilterResults();
				if (constraint != null && constraint.toString().length() > 0) {
					ArrayList<Trabajos> filteredItems = new ArrayList<Trabajos>();

					for (int i = 0, l = originalList.size(); i < l; i++) {
						Trabajos mar = originalList.get(i);
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
				trabajosList = (ArrayList<Trabajos>) results.values;
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
