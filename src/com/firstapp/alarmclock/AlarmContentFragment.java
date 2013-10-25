package com.firstapp.alarmclock;

import java.util.Calendar;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AlarmContentFragment extends Fragment{
	
	public static final String TIMETOSEND = "TIME";
	
	int contentFragNum;
	
	int hourIntoflag;
	String ringtone_name;
	int Hour;
	int Minute;
	int Year;
	int Month;
	int Day;
	Uri ringtone_Uri;
	String music_name;
	boolean buttonOn;
	boolean buttonVibrate;
	String alarm_name;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_main, container, false);
    }
	
	public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        //initialize the layouts for this fragment 
      
        
        //set TextView to indicate the value of Hour and Minute.
  		TextView hour_minute_View = (TextView)getActivity().findViewById(R.id.ringHourMinuteShow);
  		Integer tempHourInteger = Hour;
  		Integer tempMinuteInteger = Minute;
  		if (tempHourInteger < 10 && tempMinuteInteger < 10) {
  			hour_minute_View.setText("0"+tempHourInteger.toString() +":"+ "0"+tempMinuteInteger.toString());
  		}else if (tempMinuteInteger < 10) {
  			hour_minute_View.setText(tempHourInteger.toString()+":"+ "0"+tempMinuteInteger.toString());
  		}else if( tempHourInteger < 10){
  			hour_minute_View.setText("0"+tempHourInteger.toString() +":"+tempMinuteInteger.toString());
  		}else {
  			hour_minute_View.setText(tempHourInteger.toString()+":"+ tempMinuteInteger.toString());
  		}
  		
  		//register the listener of seekbars.
  		SeekBar HourSet = (SeekBar)getActivity().findViewById(R.id.seekBar1); 
  		Float temphourfFloat = (float) Hour;
  		temphourfFloat = temphourfFloat/24*100;
  		HourSet.setProgress(temphourfFloat.intValue());
  		
  		SeekBar MinuteSet = (SeekBar)getActivity().findViewById(R.id.seekBar2);
  		Float tempminuteFloat = (float) Minute;
  		tempminuteFloat = tempminuteFloat/60*100;
  		MinuteSet.setProgress(tempminuteFloat.intValue());
  		
  		HourSet.setOnSeekBarChangeListener(mSeekBarChangeListener1);
  		MinuteSet.setOnSeekBarChangeListener(mSeekBarChangeListener2);
  		
  		//set the alarm name
  		TextView alarmNameView = (TextView)getActivity().findViewById(R.id.ringcontent_blank);
  		alarmNameView.setText(alarm_name);
  		
  		TextView ringname=(TextView)getActivity().findViewById(R.id.ringname);
  		ToggleButton buttonflag=(ToggleButton)getActivity().findViewById(R.id.Ring_set);
  		ToggleButton buttonVflag=(ToggleButton)getActivity().findViewById(R.id.Vibrate_set);
  		
  		ringname.setText(music_name);
  		buttonflag.setChecked(buttonOn);
  		buttonVflag.setChecked(buttonVibrate);
      	
    }
	
	public void fillinName(View view){
		DialogFragment newFragment = new clockNameDialog();
	    newFragment.show(getFragmentManager(), "Alarm_Name"); 
	}
	
	/*select ring*/
	public void selectRingtone(View v){
		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, getResources().getString(R.string.ringselectTitle));
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_RINGTONE);
		this.startActivityForResult(intent , 1);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == android.app.Activity.RESULT_OK ) {
			if(data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI) != null){
				ringtone_Uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
			}
			if(!ringtone_Uri.equals(Uri.parse("content://settings/system/ringtone"))){
				//set the selected ringtone name
				String[] proj = {MediaStore.Audio.Media.DATA};
			    CursorLoader loader = new CursorLoader(getActivity(), ringtone_Uri, proj, null, null, null);
			    Cursor cursor = loader.loadInBackground();
			    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
			    cursor.moveToFirst();
			    ringtone_name=cursor.getString(column_index);
			    
			    int start;int end;
			    start=ringtone_name.lastIndexOf("/");
			    end=ringtone_name.lastIndexOf(".");
			    music_name = ringtone_name.substring(start+1,end);
			}
			else{
				music_name = getResources().getString(R.string.defaultRing);
			}
		    if (ringtone_Uri != null) {
			    TextView activity_edittext = (TextView)getActivity().findViewById(R.id.ringname);
			    activity_edittext.setText(music_name);
		    }
		}
	}
	
	public void setVibrate(View view){
		boolean on = ((ToggleButton) view).isChecked();
		buttonVibrate=on;
	}
	
	@Override
	public void onStop() {
		// need to test the sequence of Activity's onStop and this onStop
		super.onStop();
		//store data in the class AppData
		
		setRing(getActivity().findViewById(R.id.Ring_set));
	}
	
	
	/*start the alarm*/
	public void setRing(View view){
		
		boolean on = ((ToggleButton) view).isChecked();
		
		Calendar setTime = Calendar.getInstance(); 
		setTime.set(Year, Month, Day, Hour, Minute,0); 
		Calendar nowTime = Calendar.getInstance();
		long now=nowTime.getTimeInMillis();
		long set=setTime.getTimeInMillis();
		boolean setORnot=(now < set);
		Intent toService = new Intent(getActivity(),AlarmService.class);
		
		if (on) {
			if (setORnot) {
				 // turn on the alarm 
				toService.putExtra(TIMETOSEND, setTime.getTimeInMillis());	
				getActivity().startService(toService);
			}else {
				setTime.set(Year, Month, Day+1, Hour, Minute,0); 
				toService.putExtra(TIMETOSEND, setTime.getTimeInMillis());	
				getActivity().startService(toService);
			}
		}
		else {
	        // turn off the alarm
	    	getActivity().stopService(toService);
		}
	}

    
	private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener1 = new SeekBar.OnSeekBarChangeListener(){

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			if (fromUser) {
				Float progressfFloat = (float) progress;
				progressfFloat = progressfFloat/100 * 24;
				Hour = progressfFloat.intValue();
			}
		
			//change the indicator once the thumb moves.
			TextView hour_minute_View = (TextView)getActivity().findViewById(R.id.ringHourMinuteShow);
			Integer tempHourInteger = Hour;
			Integer tempMinuteInteger = Minute;
			SeekBar HourSet = (SeekBar)getActivity().findViewById(R.id.seekBar1); 
			
			if (tempHourInteger==24) {
				tempHourInteger=0;
				HourSet.setProgress(0);
			}
			
			if (tempHourInteger==24) {
				tempHourInteger=0;
			}
			if (tempMinuteInteger==60) {
				tempMinuteInteger=0;
			}
			
			if (tempHourInteger < 10 && tempMinuteInteger < 10) {
				hour_minute_View.setText("0"+tempHourInteger.toString() +":"+ "0"+tempMinuteInteger.toString());
			}else if (tempMinuteInteger < 10) {
				hour_minute_View.setText(tempHourInteger.toString()+":"+ "0"+tempMinuteInteger.toString());
			}else if( tempHourInteger < 10){
				hour_minute_View.setText("0"+tempHourInteger.toString() +":"+tempMinuteInteger.toString());
			}else {
				hour_minute_View.setText(tempHourInteger.toString()+":"+ tempMinuteInteger.toString());
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener2 = new SeekBar.OnSeekBarChangeListener(){

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			Float progressfFloat = (float) progress;
			progressfFloat = progressfFloat/100 * 60;
			Minute = progressfFloat.intValue();
			
			//set the effect that the hour seekbar will move when the minute seekbar moves
			Integer tempHourInteger = Hour;Integer tempMinuteInteger = Minute;
			SeekBar HourSet = (SeekBar)getActivity().findViewById(R.id.seekBar1); 
			SeekBar MinuteSet =  (SeekBar)getActivity().findViewById(R.id.seekBar2); 
			Float progressFloat=progressfFloat/60/24*100;
			Float progressShowFloat = ((float)tempHourInteger)/24*100 ;
			HourSet.setProgress(progressShowFloat.intValue()+progressFloat.intValue());
			
			//change the indicator once the thumb moves.
			TextView hour_minute_View = (TextView)getActivity().findViewById(R.id.ringHourMinuteShow);
			
			if (tempMinuteInteger==60) {
				tempMinuteInteger=0;
				if (hourIntoflag == 0) {
					Hour = Hour+1;
					hourIntoflag = hourIntoflag+1;
					if (Hour > 23) {
						Hour=0;
					}
				}
				MinuteSet.setProgress(0);
			}

			if (tempHourInteger < 10 && tempMinuteInteger < 10) {
				hour_minute_View.setText("0"+tempHourInteger.toString() +":"+ "0"+tempMinuteInteger.toString());
			}else if (tempMinuteInteger < 10) {
				hour_minute_View.setText(tempHourInteger.toString()+":"+ "0"+tempMinuteInteger.toString());
			}else if( tempHourInteger < 10){
				hour_minute_View.setText("0"+tempHourInteger.toString() +":"+tempMinuteInteger.toString());
			}else {
				hour_minute_View.setText(tempHourInteger.toString()+":"+ tempMinuteInteger.toString());
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			hourIntoflag=0;
		}
	};
	
}
