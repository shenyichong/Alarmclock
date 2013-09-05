package com.firstapp.alarmclock;

import android.app.TimePickerDialog;
import android.content.Context;
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
    	super.onTimeChanged(timePicker, hourOfDay, minute);
//        if (!mIgnoreEvent){
//            minute = getRoundedMinute(minute);
//            mIgnoreEvent=true;
//            timePicker.setCurrentMinute(minute);
//            mIgnoreEvent=false;
//        }
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

//    private TimePicker.OnTimeChangedListener mStartTimeChangedListener =
//    	    new TimePicker.OnTimeChangedListener() {
//
//    	    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//    	        updateDisplay(view,hourOfDay, minute);          
//    	    }
//    	};
//    private TimePicker.OnTimeChangedListener mNullTimeChangedListener =
//		    new TimePicker.OnTimeChangedListener() {
//
//		    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//
//		    }
//		};
//
//	private void updateDisplay(TimePicker timePicker,  int hourOfDay, int minute) { 
//
//	    // do calculation of next time 
//	    int nextMinute = 0;     
//	    if (minute >= 45 && minute <= 59)
//	        nextMinute = 45;
//	    else if(minute >= 30)
//	        nextMinute = 30;
//	    else if(minute >= 15)
//	        nextMinute = 15;
//	    else if(minute > 0)
//	        nextMinute = 0;
//	    else {          
//	        nextMinute = 45;
//	    }
//
//	    // remove ontimechangedlistener to prevent stackoverflow/infinite loop
//	    timePicker.setOnTimeChangedListener(mNullTimeChangedListener);
//
//	    // set minute
//	    timePicker.setCurrentMinute(nextMinute);
//
//	    // hook up ontimechangedlistener again
//	    timePicker.setOnTimeChangedListener(mStartTimeChangedListener); 
//	}
    
    
    private CustomTimePickerDialog.OnTimeSetListener timeSetListener = new CustomTimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute){

        }
    };
    	
}
