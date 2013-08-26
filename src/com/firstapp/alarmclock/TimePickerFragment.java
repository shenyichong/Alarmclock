package com.firstapp.alarmclock;



import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment
						implements CustomTimePickerDialog.OnTimeSetListener {
	
	OnAlarmSetListener mCallback;
	 
	public interface OnAlarmSetListener {
        public void onAlarmTimeSet();
    }
	@Override
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnAlarmSetListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAlarmSetListener.onAlarmTimeSet()");
        }
    }
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		
		int minute=MainActivity.Minute;
		int hour=MainActivity.Hour;
		
		// Create a new instance of TimePickerDialog and return it
		return new CustomTimePickerDialog(getActivity(), this, hour, minute,
		DateFormat.is24HourFormat(getActivity()));
	}
	
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	// Do something with the time chosen by the user
		TextView activity_edittext = (TextView)getActivity().findViewById(R.id.ringtime_blank);
		activity_edittext.setText(String.valueOf(hourOfDay)+":"+ String.valueOf(minute));
		MainActivity.Minute=minute;MainActivity.Hour=hourOfDay;
		mCallback.onAlarmTimeSet();
	}
}
