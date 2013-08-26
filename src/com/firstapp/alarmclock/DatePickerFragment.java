package com.firstapp.alarmclock;


import com.firstapp.alarmclock.R;
import com.firstapp.alarmclock.R.id;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;


public class DatePickerFragment extends DialogFragment
				implements DatePickerDialog.OnDateSetListener {
	
		OnAlarmSetListener mCallback;
	 
		public interface OnAlarmSetListener {
	        public void onAlarmDateSet();
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
	                    + " must implement OnAlarmSetListener.onAlarmDateSet()");
	        }
	    }
		
	
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
	
			int year=MainActivity.Year;
			int month=MainActivity.Month;
			int day=MainActivity.Day;
			
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}
	
		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user
			TextView activity_edittext = (TextView)getActivity().findViewById(R.id.ringdate_blank);
			activity_edittext.setText(String.valueOf(month + 1) + "/" + String.valueOf(day) + "/" + String.valueOf(year));
			MainActivity.Year=year;MainActivity.Month=month;MainActivity.Day=day;
			
			mCallback.onAlarmDateSet();
		}
}	
	
