package com.applus.vistas.operario;

import com.applus.controladores.interfaces.AsyncResponse;
import com.applus.R;
import com.applus.vistas.operario.brigada.NuevoMaterial.OnAddMaterial;

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
public class DialogProgress extends DialogFragment {

	private AsyncResponse callback=null;
	TextView texto;
	public DialogProgress(AsyncResponse callback){
		this.callback=callback;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialogo_general, null);
		texto = (TextView) view.findViewById(R.id.texto);
		texto.setText("�Desea Bajar los datos?");
		builder.setView(view);
		builder.setTitle("Bajar Datos de Tablas");
		builder.setNegativeButton("Cancelar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						//getDialog().dismiss();
					}
				});
		builder.setPositiveButton("Aceptar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						
					}
				});
		builder.setCancelable(true);
		AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                Button buttonOK = ((AlertDialog)dialog).getButton( DialogInterface.BUTTON_POSITIVE );
                buttonOK.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                    	getDialog().dismiss();
                    	callback.onBajarDatosTablas(true);
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
