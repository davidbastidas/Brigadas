package com.applus.vistas.operario.censo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.applus.R;
import com.applus.controladores.CensoController;
import com.applus.modelos.CensoForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("ValidFragment")
public class DialogNic extends DialogFragment {


	CensoController censoController = new CensoController();

	public DialogNic(NicListener mListener){
        try{
            this.mNicListener = mListener;
        }catch (ClassCastException e){
            throw new ClassCastException(mListener.toString() + "implementa el dialog listener");
        }
    }

	public interface NicListener{
		public void onAddNic(CensoForm item);
	}

	NicListener mNicListener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialogo_nic, null);
		//expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);

		builder.setView(view);
		builder.setTitle("Electrodomesticos");
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
