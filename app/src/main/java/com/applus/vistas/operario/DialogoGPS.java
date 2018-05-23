package com.applus.vistas.operario;

import com.applus.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class DialogoGPS extends DialogFragment {

	private OnGPSIntent callback=null;
	String latitud,longitud,acurracy;
	public DialogoGPS(OnGPSIntent callback,String latitud,String longitud,String acurracy){
		this.callback=callback;
		this.latitud=latitud;
		this.longitud=longitud;
		this.acurracy=acurracy;
	}
    public interface OnGPSIntent {
        public void onGuardarConPuntoElegido(String latitud, String longitud, String acurracy);
        public void onSeguirIntentandoGPS();
    }
	TextView gps_texto;
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.gps_intentar, null);
		gps_texto = (TextView) view.findViewById(R.id.gps_texto);
		gps_texto.setText(
				"�Desea guardar estas Corrdenadas?\nLatitud: "+latitud+
				"\nLongitud: "+longitud+
				"\nPrecisi�n: "+acurracy+" Metros");
		builder.setView(view);
		builder.setTitle("Punto GPS");
		builder.setNegativeButton("Guardar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						/**/
					}
				});
		builder.setPositiveButton("Intentar con Otro",
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
                    	callback.onSeguirIntentandoGPS();
                    	getDialog().dismiss();
                    }
                });
                Button buttonNegative = ((AlertDialog)dialog).getButton( DialogInterface.BUTTON_NEGATIVE );
                buttonNegative.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                    	callback.onGuardarConPuntoElegido(latitud,longitud,acurracy);
						getDialog().dismiss();
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
}
