package com.applus.vistas.operario.censo;

import android.annotation.SuppressLint;
import android.app.Activity;
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
public class DialogElectrodomesticos extends DialogFragment {
	ExpandableListView expandableListView;
	ExpandableListAdapter expandableListAdapter;
	List<String> expandableListTitle;
	HashMap<String, List<CensoForm>> expandableListDetail;

	CensoController censoController = new CensoController();

	public DialogElectrodomesticos(ElectrodomesticosListener mListener){
        try{
            this.mListener = mListener;
        }catch (ClassCastException e){
            throw new ClassCastException(mListener.toString() + "implementa el dialog listener");
        }
    }

	public interface ElectrodomesticosListener{
		public void onAddElectrodomestico(CensoForm item);
	}

	ElectrodomesticosListener mListener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialogo_electrodomesticos, null);
		expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
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

		expandableListDetail = ExpandableListDataPump.getData(censoController.consultarFormulario(getActivity()));
		expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
		expandableListAdapter = new CustomExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail);
		expandableListView.setAdapter(expandableListAdapter);
		expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				//expandida
			}
		});

		expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
				//contraida

			}
		});

		expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
										int groupPosition, int childPosition, long id) {
				CensoForm form = expandableListDetail.get(
						expandableListTitle.get(groupPosition)).get(
						childPosition);
				if (form.getTipo().equals("item")){
					//agregar el item y el consumo
					mListener.onAddElectrodomestico(form);
				}else{
					expandableListDetail = ExpandableListDataPump.getData("{\"items\": "+form.getItems() + "}");
					expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
					expandableListAdapter = new CustomExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail);
					expandableListView.setAdapter(expandableListAdapter);
				}
				return false;
			}
		});

		return dialog;
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
