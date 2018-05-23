package com.applus.vistas.admin;

import com.applus.R;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ConfiguracionFragment extends Fragment {
	
	private EditText T_IP, T_PROYECTO, T_MEMORIA;
	private RadioButton R_VTOTAL, R_VLIMITADA;
	private CheckBox CK_INTERNET;

	
	//variables del toast dinamico
	LayoutInflater inflater;
	View layoutToast;
	TextView textToast;
	Toast toastAlerta;
	////////////////////////
	
	String orden_realizado="";
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_configuracion, container, false);
        cargarToastAlerta();
		T_IP = (EditText) rootView.findViewById(R.id.T_IP);
		T_PROYECTO = (EditText) rootView.findViewById(R.id.T_PROYECTO);
		T_MEMORIA = (EditText) rootView.findViewById(R.id.T_MEMORIA);
		R_VTOTAL = (RadioButton) rootView.findViewById(R.id.R_VTOTAL);
		R_VLIMITADA = (RadioButton) rootView.findViewById(R.id.R_VLIMITADA);
		CK_INTERNET = (CheckBox) rootView.findViewById(R.id.CK_INTERNET);
        SharedPreferences prefe = getActivity().getSharedPreferences("configuracion",	Context.MODE_PRIVATE);
        if(prefe!=null){
        	 orden_realizado=prefe.getString("orden_realizado", "");
             mostrarConfiguracion(
             		""+prefe.getString("ip", ""),
             		""+prefe.getString("proyecto", ""),
             		""+prefe.getString("memoria", ""),
             		""+prefe.getString("vista", ""),
             		""+prefe.getString("ckinternet", "")
     				);
        }else{
        	System.out.println("conf NULA");
        }
       
        return rootView;
    }
	
	public void mostrarConfiguracion(
			String ip, String proyecto, String memoria,
			String vista,
			String ckinternet) {
		//s10 y s11 son el formato de alturas y la validacion de lecturas
		//s12 maximo y s13 minimo
		T_IP.setText(ip);
		T_PROYECTO.setText(proyecto);
		T_MEMORIA.setText(memoria);
		if (vista.equals("1")) {
			R_VTOTAL.setChecked(true);
		} else if (vista.equals("2")){
			R_VLIMITADA.setChecked(true);
		}
		if (ckinternet.equals("0") || ckinternet.equals("")) {
			CK_INTERNET.setChecked(false);
		} else {
			CK_INTERNET.setChecked(true);
		}
	}
	
	public void guardarConfiguracion() {
		boolean pasa=true;
		String campoError="";
		if(T_IP.getText().toString().trim().equals("")){
			pasa=false;
			campoError="Escriba la IP del Servidor";
		} else if(T_PROYECTO.getText().toString().trim().equals("")){
			pasa=false;
			campoError="Escriba el PROYECTO";
		} else if(T_MEMORIA.getText().toString().trim().equals("")){
			pasa=false;
			campoError="Escriba el almacenamiento";
		}
		if(!pasa){
			mostrarToastAlerta(campoError);
		}
		if (pasa) {
			SharedPreferences preferencias = getActivity().getSharedPreferences(
					"configuracion", Context.MODE_PRIVATE);
			Editor editor = preferencias.edit();
			editor.putString("ip", T_IP.getText().toString());
			editor.putString("proyecto", T_PROYECTO.getText().toString());
			editor.putString("memoria", T_MEMORIA.getText().toString().trim());
			if (R_VTOTAL.isChecked()) {
				editor.putString("vista", "1");
			} else if (R_VLIMITADA.isChecked()){
				editor.putString("vista", "2");
			}
			if (CK_INTERNET.isChecked()) {
				editor.putString("ckinternet", "1");
			} else {
				editor.putString("ckinternet", "0");
			}
			if (orden_realizado.equals("")) {
				editor.putString("orden_realizado", "0");
			} else {
				editor.putString("orden_realizado", orden_realizado);
			}
			editor.commit();
			Toast.makeText(getActivity(), "Se guardo la configuracion",
					Toast.LENGTH_SHORT).show();
		}
	}
	public void cargarToastAlerta(){
		inflater = getActivity().getLayoutInflater();
		layoutToast = inflater.inflate(R.layout.mensaje1,
				(ViewGroup) getActivity().findViewById(R.id.toast_layout_root));
		ImageView image = (ImageView) layoutToast.findViewById(R.id.image);
		image.setImageResource(R.mipmap.alerta);
		textToast = (TextView) layoutToast.findViewById(R.id.text);
	}
	public void mostrarToastAlerta(String mensaje){
		textToast.setText(mensaje);
		if (toastAlerta==null) {
			toastAlerta = new Toast(getActivity().getApplicationContext());
			toastAlerta.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toastAlerta.setDuration(Toast.LENGTH_SHORT);
			toastAlerta.setView(layoutToast);
		}
		toastAlerta.show();
	}
     
    //El Fragment ha sido quitado de su Activity y ya no est� disponible
    @Override
    public void onDetach() {
        super.onDetach();
        guardarConfiguracion();
    }
}
