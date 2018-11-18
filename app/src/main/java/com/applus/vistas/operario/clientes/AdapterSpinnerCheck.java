package com.applus.vistas.operario.clientes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.applus.R;
import com.applus.modelos.StateVO;

import java.util.ArrayList;
import java.util.List;

public class AdapterSpinnerCheck extends ArrayAdapter<StateVO> {

    private Context mContext;
    private ArrayList<StateVO> listState;
    private AdapterSpinnerCheck myAdapter;
    private boolean isFromView = false;

    public AdapterSpinnerCheck(Context context, int resource, List<StateVO> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<StateVO>) objects;
        this.myAdapter = this;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_item_check, null);
            holder = new ViewHolder();
            holder.textViewItemSpinner = (TextView) convertView
                    .findViewById(R.id.textViewItemSpinner);
            holder.checkboxItemSpinner = (CheckBox) convertView
                    .findViewById(R.id.checkboxItemSpinner);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textViewItemSpinner.setText(listState.get(position).getTitle());

        // To check weather checked event fire from getview() or user input
        isFromView = true;
        holder.checkboxItemSpinner.setChecked(listState.get(position).isSelected());
        isFromView = false;

        if ((position == 0)) {
            holder.checkboxItemSpinner.setVisibility(View.INVISIBLE);
        } else {
            holder.checkboxItemSpinner.setVisibility(View.VISIBLE);
        }
        holder.checkboxItemSpinner.setTag(position);
        holder.checkboxItemSpinner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();

                if (!isFromView) {
                    listState.get(position).setSelected(isChecked);
                }
            }
        });
        return convertView;
    }

    public ArrayList<StateVO> getListState() {
        return listState;
    }

    private class ViewHolder {
        private TextView textViewItemSpinner;
        private CheckBox checkboxItemSpinner;
    }
}
