package com.firstapp.alarmclock;

import java.util.Calendar;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class AlarmContentFragment extends Fragment{
	

	private static final String STATE_HOUR = "HOUR";

	private static final String STATE_MUSIC = "MUSIC";

	private static final String STATE_MINUTE = "MINUTE";

	private static final String STATE_URI = "URI";

	private static final String STATE_BUTTON = "BUTTON";

	private static final String STATE_VIBRATE = "VIBRATE";

	private static final String ALARM_NAME = "NAME";
	
	static int contentFragNum;
	static String preferencesNameString;
	
	static int hourIntoflag=0;
	static String ringtone_name;
	static int Hour;
	static int Minute;
	static int Year;
	static int Month;
	static int Day;
	static Uri ringtone_Uri;
	static String music_name;
	static boolean buttonOn;
	static boolean buttonVibrate;
	static String alarm_name;
	
	public interface ClockChangeListener{
		public void onClockChange(String TimeShown);
	}
	
	ClockChangeListener mClockListener;
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mClockListener = (ClockChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ClockChangeListener");
        }
    }
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_main,container,false);
    }
	
	public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
       
        //initialize the layouts for this fragment 
        contentFragNum=MainActivity.cur_menu_number;
        preferencesNameString = "Pref_"+String.valueOf(contentFragNum);
        SharedPreferences settings = getActivity().getSharedPreferences(preferencesNameString,Context.MODE_PRIVATE);
		if(settings.getString(STATE_MUSIC, null) == null){
			/*final Calendar c = Calendar.getInstance();
			Hour = c.get(Calendar.HOUR_OF_DAY);
			Minute = c.get(Calendar.MINUTE)+1;
			if (Minute >= 60) {
				Hour++; Minute-=60;
				if (Hour >= 24){
					Hour=0;
				}
			}*/
			Minute=0;
			Hour=0;
			
			ringtone_Uri=Uri.parse("content://settings/system/ringtone");
			music_name=getResources().getString(R.string.defaultRing);
			buttonOn=false;
			buttonVibrate=false;
			alarm_name=getResources().getString(R.string.ringcontent_blank);
		}
		else{
			Hour =settings.getInt(STATE_HOUR,0);
		    Minute = settings.getInt(STATE_MINUTE,0);
		    ringtone_Uri = Uri.parse(settings.getString(STATE_URI, "content://settings/system/ringtone"));
		    music_name=settings.getString(STATE_MUSIC, getResources().getString(R.string.defaultRing));
		    buttonOn=settings.getBoolean(STATE_BUTTON, false);
		    buttonVibrate=settings.getBoolean(STATE_VIBRATE, false);
		    alarm_name=settings.getString(ALARM_NAME,getResources().getString(R.string.ringcontent_blank));
		}
		final Calendar c = Calendar.getInstance();
		Year = c.get(Calendar.YEAR);
		Month = c.get(Calendar.MONTH);
		Day = c.get(Calendar.DAY_OF_MONTH);
        
        
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
  		
  		//set the alarm name and register the listeners of buttons.
  		Button ringSelect = (Button)getActivity().findViewById(R.id.button_ringselect);
  		ringSelect.setOnClickListener(new View.OnClickListener() {
  		    @Override
  		    public void onClick(View v) {
  		    	selectRingtone(v);
  		    }
  		});

  		TextView alarmNameView = (TextView)getActivity().findViewById(R.id.ringcontent_blank);
  		alarmNameView.setOnClickListener(new View.OnClickListener() {
  		    @Override
  		    public void onClick(View v) {
  		    	fillinName(v);
  		    }
  		});
  		
  		//register listeners
  		ToggleButton buttonflag=(ToggleButton)getActivity().findViewById(R.id.Ring_set);
  		buttonflag.setOnClickListener(new View.OnClickListener() {
  		    @Override
  		    public void onClick(View v) {
  		    	setRing(v);
  		    }
  		});
  		
  		ToggleButton buttonVflag=(ToggleButton)getActivity().findViewById(R.id.Vibrate_set);
  		buttonVflag.setOnClickListener(new View.OnClickListener() {
  		    @Override
  		    public void onClick(View v) {
  		    	setVibrate(v);
  		    }
  		});
  		
  		TextView ringname=(TextView)getActivity().findViewById(R.id.ringname);
  		ringname.setText(music_name);
  		
  		alarmNameView.setText(alarm_name);
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
		preferencesNameString = "Pref_"+String.valueOf(contentFragNum);
        SharedPreferences settings = getActivity().getSharedPreferences(preferencesNameString,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(STATE_HOUR, Hour);
	    editor.putInt(STATE_MINUTE, Minute);
	    editor.putString(STATE_URI, ringtone_Uri.toString());
	    editor.putString(STATE_MUSIC, music_name);
	    ToggleButton buttonflag=(ToggleButton)getActivity().findViewById(R.id.Ring_set);
	    ToggleButton buttonVflag=(ToggleButton)getActivity().findViewById(R.id.Vibrate_set);
	    TextView alarmNameView = (TextView)getActivity().findViewById(R.id.ringcontent_blank);
	    editor.putString(ALARM_NAME, alarmNameView.getText().toString());
	    editor.putBoolean(STATE_BUTTON, buttonflag.isChecked());
	    editor.putBoolean(STATE_VIBRATE, buttonVflag.isChecked());
	    editor.commit();
	    
	    //setRing(getActivity().findViewById(R.id.Ring_set));
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
		
		if (on) {
			if (!setORnot) {
				setTime.set(Year, Month, Day+1, Hour, Minute,0); 
				set=setTime.getTimeInMillis();
				
			}
			MainActivity.AlarmTimes.add(set);
			int hoursLeft=(int)Math.floor((set-now)/1000/3600);
			long mod=(set-now)%(1000*3600);
			int minutesLeft=(int)Math.floor(mod/1000/60);
			if (minutesLeft == 0 && ((ToggleButton)getActivity().findViewById(R.id.Ring_set)).isChecked()){
				Toast.makeText(getActivity(), getResources().getString(R.string.notificationText1)+getResources().getString(R.string.notificationText6)+getResources().getString(R.string.notificationText5), Toast.LENGTH_SHORT).show();
			}
			else if (hoursLeft == 0 && ((ToggleButton)getActivity().findViewById(R.id.Ring_set)).isChecked()){
				Toast.makeText(getActivity(), getResources().getString(R.string.notificationText1)+String.valueOf(minutesLeft)+getResources().getString(R.string.notificationText5), Toast.LENGTH_SHORT).show();
			}
			else if(((ToggleButton)getActivity().findViewById(R.id.Ring_set)).isChecked()){
				Toast.makeText(getActivity(), getResources().getString(R.string.notificationText1)+String.valueOf(hoursLeft)+getResources().getString(R.string.notificationText4)+String.valueOf(minutesLeft)+getResources().getString(R.string.notificationText5), Toast.LENGTH_SHORT).show();
			}
		}
		else {
	        // turn off the alarm
			
	    	//getActivity().stopService(toService);
		}
	}
    
	private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener1 = new SeekBar.OnSeekBarChangeListener(){
		
		String str = "00:00";
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
				str="0"+tempHourInteger.toString() +":"+ "0"+tempMinuteInteger.toString();
			}else if (tempMinuteInteger < 10) {
				str=tempHourInteger.toString()+":"+ "0"+tempMinuteInteger.toString();
			}else if( tempHourInteger < 10){
				str="0"+tempHourInteger.toString() +":"+tempMinuteInteger.toString();
			}else {
				str=tempHourInteger.toString()+":"+ tempMinuteInteger.toString();
			}
			
			hour_minute_View.setText(str);
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			mClockListener.onClockChange(str+" "+alarm_name);
		}
	};
	
	private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener2 = new SeekBar.OnSeekBarChangeListener(){

		String str = "00:00";
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
				str="0"+tempHourInteger.toString() +":"+ "0"+tempMinuteInteger.toString();
			}else if (tempMinuteInteger < 10) {
				str=tempHourInteger.toString()+":"+ "0"+tempMinuteInteger.toString();
			}else if( tempHourInteger < 10){
				str="0"+tempHourInteger.toString() +":"+tempMinuteInteger.toString();
			}else {
				str=tempHourInteger.toString()+":"+ tempMinuteInteger.toString();
			}
			hour_minute_View.setText(str);
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			hourIntoflag=0;
			mClockListener.onClockChange(str+" "+alarm_name);
		}
	};
	
}
