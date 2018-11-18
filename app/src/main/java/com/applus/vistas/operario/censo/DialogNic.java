package com.applus.vistas.operario.censo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.applus.R;
import com.applus.controladores.BarrioController;
import com.applus.controladores.DepartamentoController;
import com.applus.controladores.MunicipioController;
import com.applus.controladores.NicsController;
import com.applus.modelos.Barrio;
import com.applus.modelos.Cliente;
import com.applus.modelos.Departamento;
import com.applus.modelos.Municipio;
import com.applus.modelos.Nics;
import com.applus.modelos.SesionSingleton;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class DialogNic extends DialogFragment {

	Spinner departamento, municipio, barrio, nic;
	Button aceptar;

	Departamento departamentoElegido = null;
	Municipio municipioElegido = null;
	Barrio barrioElegido = null;
	Cliente nicElegido = null;
	long nicBuscado = 0;

	ArrayAdapter<Cliente> nicAdapter = null;
	ArrayAdapter<Municipio> municipioAdapter = null;
	boolean detenerAutoSelectNic = false;
	boolean detenerAutoSelectDepartamento = false;
	boolean detenerAutoSelectMunicipio = false;

	public DialogNic(NicListener mListener, long nicBuscado){
        try{
            this.mNicListener = mListener;
			this.nicBuscado = nicBuscado;
        }catch (Exception e){
            throw new ClassCastException(mListener.toString() + "implementa el dialog listener");
        }
    }

	public interface NicListener{
		public void onAddNic(Cliente cliente);
	}

	NicListener mNicListener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialogo_nic, null);
		//expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
		departamento = (Spinner) view.findViewById(R.id.departamento);
		municipio = (Spinner) view.findViewById(R.id.municipio);
		barrio = (Spinner) view.findViewById(R.id.barrio);
		nic = (Spinner) view.findViewById(R.id.nic);

		aceptar = (Button) view.findViewById(R.id.aceptar);
		aceptar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (nicElegido != null){
					mNicListener.onAddNic(nicElegido);
					dismiss();
				}else{
					Toast.makeText(getActivity(), "No ha seleccionado el NIC", Toast.LENGTH_SHORT).show();
				}

			}
		});

		DepartamentoController dep = new DepartamentoController();
		ArrayList<Departamento> departamentos = dep.consultar(0, 0, "id>0 ORDER BY nombre ", getActivity());
		ArrayAdapter<Departamento> departamentoAdapter = new ArrayAdapter<Departamento>(getActivity(),
				android.R.layout.simple_spinner_item, departamentos);
		departamentoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		departamento.setAdapter(departamentoAdapter);
		departamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				departamentoElegido = (Departamento) parentView.getItemAtPosition(position);

				//llenando los municipios
				MunicipioController mun = new MunicipioController();
				ArrayList<Municipio> municipios = mun.consultar(0, 0, "fk_departamento=" + departamentoElegido.getId() + " ORDER BY nombre ", getActivity());
				municipioAdapter = new ArrayAdapter<Municipio>(getActivity(),
						android.R.layout.simple_spinner_item, municipios);
				municipioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				municipio.setAdapter(municipioAdapter);

				if(detenerAutoSelectDepartamento){
					ArrayList<Departamento> departamentoC = dep.consultar(0, 0, "id=" + SesionSingleton.getInstance().getFkDistrito(), getActivity());

					for (int i = 0; i < departamentoAdapter.getCount(); i++){
						if(departamentoAdapter.getItem(i).getId() == departamentoC.get(0).getId()) {
							departamento.setSelection(departamentoAdapter.getPosition(departamentoAdapter.getItem(i)));
							break;
						}
					}
					detenerAutoSelectDepartamento = false;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				departamentoElegido = null;
			}
		});

		municipio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				municipioElegido = (Municipio) parentView.getItemAtPosition(position);

				//llenando los barrios
				BarrioController barr = new BarrioController();
				ArrayList<Barrio> barrios = barr.consultar(0, 0, "fk_municipio=" + municipioElegido.getId() + " ORDER BY nombre ", getActivity());
				ArrayAdapter<Barrio> barrioAdapter = new ArrayAdapter<Barrio>(getActivity(),
						android.R.layout.simple_spinner_item, barrios);
				barrioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				barrio.setAdapter(barrioAdapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				municipioElegido = null;
			}
		});

		barrio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				barrioElegido = (Barrio) parentView.getItemAtPosition(position);
				//llenando los nics
				NicsController nicsController = new NicsController();
				ArrayList<Nics> nics = nicsController.consultar(0, 0, "fk_barrio=" + barrioElegido.getId() + " GROUP BY nic ", getActivity());
				System.out.println("barrioElegido: " + barrioElegido.getId());
				System.out.println("nics: " + nics.size());

				ArrayList<Cliente> clientes = new ArrayList<Cliente>();
				for (int i = 0; i < nics.size(); i++){
					Cliente cliente = new Cliente();
					System.out.println("NIc: " + nics.get(i).getFkBarrio());
					cliente.setNic(nics.get(i).getNic());
					clientes.add(cliente);
				}
				nicAdapter = new ArrayAdapter<Cliente>(getActivity(),
						android.R.layout.simple_spinner_item, clientes);
				nicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				nic.setAdapter(nicAdapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				barrioElegido = null;
			}
		});

		nic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				nicElegido = (Cliente) parentView.getItemAtPosition(position);

				if(detenerAutoSelectNic){
					if(nicBuscado != 0){
						int positionT = 0;
						int fk_municipio = 0;
						for (int i = 0; i < nicAdapter.getCount(); i++){
							if(nicAdapter.getItem(i).getNic() == nicBuscado) {
								fk_municipio = nicAdapter.getItem(i).getFk_municipio();
								positionT = nicAdapter.getPosition(nicAdapter.getItem(i));
								break;
							}
						}
						for (int i = 0; i < municipioAdapter.getCount(); i++){
							if(municipioAdapter.getItem(i).getId() == fk_municipio) {
								municipio.setSelection(municipioAdapter.getPosition(municipioAdapter.getItem(i)));
								break;
							}
						}
						nic.setSelection(positionT);
					}
					detenerAutoSelectNic = false;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				barrioElegido = null;
			}
		});

		builder.setView(view);
		builder.setTitle("NIC");
		builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss();
			}
		});

		AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                ((AlertDialog)dialog).getButton( AlertDialog.BUTTON_POSITIVE ).setEnabled(true);
            }
        });
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);

		return dialog;
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
