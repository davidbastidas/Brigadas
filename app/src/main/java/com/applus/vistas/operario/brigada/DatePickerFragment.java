package com.applus.vistas.operario.brigada;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
	
public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {
	private Button pick_date;
	public DatePickerFragment(Button pick_date){
		this.pick_date=pick_date;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = sdf.format(c.getTime());
		pick_date.setText(formattedDate);
	}
}