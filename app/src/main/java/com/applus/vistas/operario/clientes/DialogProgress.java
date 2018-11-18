package com.applus.vistas.operario.clientes;

import com.applus.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class DialogProgress extends DialogFragment {

	ProgressBar barra;
	AlertDialog.Builder builder = null;

	public interface NoticeDialogListener{
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}
	NoticeDialogListener mListener;
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			mListener = (NoticeDialogListener) activity;
		}catch (ClassCastException e){
			throw new ClassCastException(activity.toString() + "implementa el dialog listener");
		}
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialogo_progress, null);
        barra = (ProgressBar) view.findViewById(R.id.barra);
        barra.setIndeterminate(true);
		builder.setView(view);
		builder.setTitle("Bajando datos");
		AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                ((AlertDialog)dialog).getButton( AlertDialog.BUTTON_POSITIVE ).setEnabled(false);
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
