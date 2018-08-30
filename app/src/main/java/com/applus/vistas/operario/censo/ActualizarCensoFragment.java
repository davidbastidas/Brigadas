package com.applus.vistas.operario.censo;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.applus.R;
import com.applus.controladores.ClientesController;
import com.applus.modelos.Cliente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@SuppressLint("ValidFragment")
public class ActualizarCensoFragment extends Fragment{
//1128018
	private ActualizarCensoListener mListener;
	public interface ActualizarCensoListener{
		public void onActualizarCenso(Cliente cliente);
	}
	ClientesController cont = new ClientesController();

	EditText codigo;
	TextView ac_nombre, ac_direccion, ac_nic;
	Button buscar, irActualizarCenso;
	ListView listaCensos;
	ArrayList<LinkedHashMap<String, String>> array = new ArrayList<LinkedHashMap<String, String>>();
	LinkedHashMap<String, String> item = null;

	Cliente clienteEncontrado = null;

	private boolean puedeActualizar = true;

	public ActualizarCensoFragment(ActualizarCensoListener mListener){
		try{
			this.mListener = mListener;
		}catch (ClassCastException e){
			throw new ClassCastException(mListener.toString() + "implementa el listener");
		}
	}

	@Override
	public void onResume() {
		limpiar();
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_actualizar_censo, container, false);

		getActivity().setTitle("Actualizacion de censo");
		codigo = (EditText) rootView.findViewById(R.id.ac_codigo);
		ac_nombre = (TextView) rootView.findViewById(R.id.ac_nombre);
		ac_direccion = (TextView) rootView.findViewById(R.id.ac_direccion);
		ac_nic = (TextView) rootView.findViewById(R.id.ac_nic);
		buscar = (Button) rootView.findViewById(R.id.buscar_codigo);
		buscar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(!codigo.getText().toString().equals("")){
					clienteEncontrado = cont.getClienteCodigo(Long.parseLong(codigo.getText().toString()), getActivity());
					if(clienteEncontrado != null){
						ac_nombre.setText(clienteEncontrado.getNombre());
						ac_direccion.setText(clienteEncontrado.getDireccion());
						ac_nic.setText("" + clienteEncontrado.getNic());
						JSONObject json_data = null;
						try {
							json_data = new JSONObject(clienteEncontrado.getCensos());
							JSONArray censos = json_data.getJSONArray("censos");
							int size = censos.length();
							String estado = "";
							array.clear();
							for (int i = 0; i < size; ++i) {
								JSONObject cen = censos.getJSONObject(i);
								item = new LinkedHashMap<String, String>();
								item.put("fecha", cen.getString("fecha"));
								if(Integer.parseInt(cen.getString("estado")) == 0){
									estado = "No aplicado";
									if(i == 0){
										puedeActualizar = false;
									}
								}else{
									estado = "Aplicado";
									if(i == 0){
										puedeActualizar = true;
									}
								}
								item.put("usuario", "Censador : " + cen.getString("usuario") + " - Estado: " + estado);
								array.add(item);
							}
							if (size == 0){
								puedeActualizar = true;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}else{
						puedeActualizar = false;
                        Toast.makeText(getActivity(), "Codigo no encontrado", Toast.LENGTH_SHORT).show();
                    }
				}else{
					Toast.makeText(getActivity(), "Ingrese un codigo", Toast.LENGTH_SHORT).show();
				}
			}
		});
		irActualizarCenso = (Button) rootView.findViewById(R.id.irActualizarCenso);
		irActualizarCenso.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(puedeActualizar){
					if (clienteEncontrado != null){
						mListener.onActualizarCenso(clienteEncontrado);
					}else{
						Toast.makeText(getActivity(), "Busque primero un cliente", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getActivity(), "No puede actualizar. Por favor verifique el codigo y que no tenga censos por revisar.", Toast.LENGTH_LONG).show();
				}
			}
		});
		listaCensos = (ListView) rootView.findViewById(R.id.lista_censos);
		SimpleAdapter adapter = new SimpleAdapter(
				getActivity(),
				array,
				R.layout.imagen_textview2,
				new String[]{"fecha","usuario"},
				new int[]{R.id.titulo, R.id.subtitulo});
		listaCensos.setAdapter(adapter);

		return rootView;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	private void limpiar() {
		codigo.setText("");
		ac_nombre.setText("");
		ac_direccion.setText("");
		ac_nic.setText("");
		clienteEncontrado = null;
		puedeActualizar = false;
	}
}
