package com.applus.modelos;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VistaDinamicaDetalleOrdenOS {

	Activity activity;
	View rootView;

	public VistaDinamicaDetalleOrdenOS(View rootView, Activity activity) {
		this.activity = activity;
		this.rootView = rootView;
	}

	public LinearLayout nuevoLayoutHorizontal() {
		LinearLayout layout1 = new LinearLayout(activity);
		layout1.setOrientation(LinearLayout.HORIZONTAL);
		layout1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		layout1.setPadding(3, 3, 3, 3);

		return layout1;
	}
	public TextView nuevoTitulo(String titulo) {
		TextView ttview1 = new TextView(activity);
		ttview1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				1));
		ttview1.setGravity(Gravity.LEFT);
		ttview1.setText(titulo);
		ttview1.setBackgroundColor(Color.BLUE);
		ttview1.setTextColor(Color.WHITE);
		ttview1.setTypeface(null, Typeface.BOLD);
		ttview1.setTextSize(20);
		ttview1.setPadding(3, 3, 3, 3);

		return ttview1;
	}
	public TextView nuevoDetalle(String titulo) {
		TextView dtview1 = new TextView(activity);
		dtview1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				1));
		dtview1.setGravity(Gravity.LEFT);
		dtview1.setText(titulo);
		dtview1.setTextSize(20);
		dtview1.setPadding(3, 3, 3, 3);

		return dtview1;
	}
	public TextView nuevoLinea() {
		TextView linea = new TextView(activity);
		linea.setBackgroundColor(Color.GRAY);
		linea.setText("s");
		linea.setPadding(3, 3, 3, 3);
		linea.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 3,1));

		return linea;
	}
}
