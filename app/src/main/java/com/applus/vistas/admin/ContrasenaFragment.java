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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ContrasenaFragment extends Fragment {

	
	EditText T_CAMBIO_CONTRASENA;
	RadioButton R_CAMBIO_OPERARIO,R_CAMBIO_ADMIN;
	
	//variables del toast dinamico
	LayoutInflater inflater;
	View layoutToast;
	TextView textToast;
	Toast toastAlerta;
	////////////////////////
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
	    View rootView = inflater.inflate(R.layout.fragment_contrasena, container, false);
	    cargarToastAlerta();
	    T_CAMBIO_CONTRASENA = (EditText) rootView.findViewById(R.id.T_CAMBIO_CONTRASENA);
	    R_CAMBIO_OPERARIO = (RadioButton) rootView.findViewById(R.id.R_CAMBIO_OPERARIO);
	    R_CAMBIO_ADMIN = (RadioButton) rootView.findViewById(R.id.R_CAMBIO_ADMIN);
	    Button button = (Button) rootView.findViewById(R.id.B_GUARDAR_PASS);
	    button.setOnClickListener(new OnClickListener()
	    {
		    public void onClick(View v)
		    {
		    	guardar();
		    }
	    });
        return rootView;
    }
	public void guardar(){
		if(!T_CAMBIO_CONTRASENA.getText().toString().trim().equals("")){
			SharedPreferences preferencias = getActivity().getSharedPreferences("configuracion",
					Context.MODE_PRIVATE);
			Editor editor = preferencias.edit();
			if(R_CAMBIO_OPERARIO.isChecked()){
				editor.putString("passwordServicios", T_CAMBIO_CONTRASENA.getText().toString());
				Toast.makeText(getActivity().getApplicationContext(), "Se cambio la contrase�a de OPERARIO",
						Toast.LENGTH_SHORT).show();
			}else if(R_CAMBIO_ADMIN.isChecked()){
				editor.putString("passwordAdmin", T_CAMBIO_CONTRASENA.getText().toString());
				Toast.makeText(getActivity().getApplicationContext(), "Se cambio la contrase�a de ADMIN",
						Toast.LENGTH_SHORT).show();
			}
			editor.commit();
		}else{
			mostrarToastAlerta("Escriba la contrase�a");
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
}
