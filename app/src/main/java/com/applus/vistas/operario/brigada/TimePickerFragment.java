package com.applus.vistas.operario.brigada;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;



public class TimePickerFragment extends DialogFragment implements
		TimePickerDialog.OnTimeSetListener {
	private Button pick_time;
	public TimePickerFragment(Button pick_time){
		this.pick_time=pick_time;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
				true);
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		Calendar c = Calendar.getInstance();
		if(hourOfDay==00){
			hourOfDay=24;
		}
		c.set(0, 0, 0, hourOfDay, minute);

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String formattedDate = sdf.format(c.getTime());
		pick_time.setText(formattedDate);
	}
}