package com.firstapp.alarmclock;

import android.app.TimePickerDialog;
import android.content.Context;
//import android.view.View.OnCreateContextMenuListener;
import android.widget.TimePicker;

public class CustomTimePickerDialog extends TimePickerDialog{
	public static final int TIME_PICKER_INTERVAL=5;
    //private boolean mIgnoreEvent=false;
	
    public CustomTimePickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute,
        boolean is24HourView) {
    	super(context, callBack, hourOfDay, minute, is24HourView);
    	
    }
    
    @Override
    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
    	minute = getRoundedMinute(minute);
    	timePicker.setCurrentMinute(minute);
    }

    public static int getRoundedMinute(int minute){
         if(minute % TIME_PICKER_INTERVAL != 0){
            int minuteFloor = minute - (minute % TIME_PICKER_INTERVAL);
            minute = minuteFloor + (minute == minuteFloor + 1 ? TIME_PICKER_INTERVAL : 0);
            if (minute == 60)  minute=0;
         }

        return minute;
    }

    
    private OnTimeSetListener timeSetListener = new OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        	
        }
    };
    	
}
