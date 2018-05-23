package com.applus.vistas.operario.brigada;

import com.applus.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

@SuppressLint("ValidFragment")
public class FormFechaHora extends DialogFragment {

	private OnFechaHora callback=null;
	public FormFechaHora(OnFechaHora callback){
		this.callback=callback;
	}
    public interface OnFechaHora {
        public void onSetFechaHora(String fecha, String hora);
    }
	Button fecha, hora;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.fecha_hora, null);
		fecha = (Button) view.findViewById(R.id.fecha);
		hora = (Button) view.findViewById(R.id.hora);
		// add a click listener to the button
		fecha.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	DialogFragment picker = new DatePickerFragment(fecha);
            	picker.show(getFragmentManager(), "datePicker");
            }
        });
		hora.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	DialogFragment picker = new TimePickerFragment(hora);
            	picker.show(getFragmentManager(), "timePicker");
            }
        });
		
		builder.setView(view);
		builder.setTitle("Fecha y Hora");
		builder.setNegativeButton("Cancelar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						getDialog().dismiss();
					}
				});
		builder.setPositiveButton("Guardar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						System.out.println("callback: "+callback);
						
						callback.onSetFechaHora(
								fecha.getText().toString(),
								hora.getText().toString());
					}
				});
		return builder.create();
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
