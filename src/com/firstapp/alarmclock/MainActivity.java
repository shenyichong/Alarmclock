package com.firstapp.alarmclock;

import java.util.Calendar;

import javax.xml.transform.Templates;

import android.R.integer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
//import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainActivity extends Activity {
	
	private static final String STATE_HOUR = "HOUR";
	private static final String STATE_MINUTE = "MINUTE";
	/*private static final String STATE_YEAR = "YEAR";
	private static final String STATE_MONTH = "MONTH";
	private static final String STATE_DAY = "DAY";*/
	private static final String STATE_URI = "URI";
	private static final String PREFS_NAME="SETTINGS";
	private static final String STATE_MUSIC = "MUSIC";
	private static final String STATE_BUTTON = "BUTTON";
	private static final String STATE_VIBRATE= "VIBRATE";
//	private static final int TIME_PICKER_INTERVAL = 5;
	public static final String TIMETOSEND = "TIME";
	
	
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
	
//	private TimePicker.OnTimeChangedListener mStartTimeChangedListener =
//		    new TimePicker.OnTimeChangedListener() {
//
//		    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//		        updateDisplay(view, hourOfDay, minute);
//		    }
//		};
//
//	private TimePicker.OnTimeChangedListener mNullTimeChangedListener =
//		    new TimePicker.OnTimeChangedListener() {
//	
//		    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//		    
//		    }
//		};
//	
//	private void updateDisplay(TimePicker timePicker, int hourOfDay, int minute) { 
//
//	    // do calculation of next time 
//		int nextMinute = 0;     
//		if(minute % TIME_PICKER_INTERVAL != 0){
//            int minuteFloor = minute - (minute % TIME_PICKER_INTERVAL);
//            minute = minuteFloor + (minute == minuteFloor + 1 ? TIME_PICKER_INTERVAL : 0);
//            if (minute == 60)  minute=0;
//         }
//		nextMinute=minute;
//
//	    // remove ontimechangedlistener to prevent stackoverflow/infinite loop
//	    timePicker.setOnTimeChangedListener(mNullTimeChangedListener);
//
//	    // set minute
//	    timePicker.setCurrentMinute(nextMinute);
//	    
//	    //set the Timepicker's time
//        MainActivity.Hour=hourOfDay;
//        MainActivity.Minute=nextMinute;
//        //Set AlarmClock
//	    this.setRing(this.findViewById(R.id.Ring_set));
//        
//	    // hook up ontimechangedlistener again
//	    timePicker.setOnTimeChangedListener(mStartTimeChangedListener);
//	}
	
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
			TextView hour_minute_View = (TextView)findViewById(R.id.ringHourMinuteShow);
			Integer tempHourInteger = Hour;
			Integer tempMinuteInteger = Minute;
			hour_minute_View.setText(tempHourInteger.toString()+":"+tempMinuteInteger.toString());
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
			SeekBar HourSet = (SeekBar)findViewById(R.id.seekBar1); 
			Float progressFloat=progressfFloat/60/24*100;
			Float progressShowFloat = ((float)tempHourInteger)/24*100 ;
			HourSet.setProgress(progressShowFloat.intValue()+progressFloat.intValue());
			
			//change the indicator once the thumb moves.
			
			TextView hour_minute_View = (TextView)findViewById(R.id.ringHourMinuteShow);
			hour_minute_View.setText(tempHourInteger.toString()+":"+tempMinuteInteger.toString());
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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
		if(settings.getString(STATE_MUSIC, null) == null){
			final Calendar c = Calendar.getInstance();
			Hour = c.get(Calendar.HOUR_OF_DAY);
			Minute = c.get(Calendar.MINUTE)+1;
			if (Minute >= 60) {
				Hour++; Minute-=60;
				if (Hour >= 24){
					Hour=0;
				}
			}
			
			ringtone_Uri=Uri.parse("content://settings/system/ringtone");
			music_name=getResources().getString(R.string.defaultRing);
			buttonOn=false;
			buttonVibrate=false;
		}
		else{
			Hour =settings.getInt(STATE_HOUR,0);
		    Minute = settings.getInt(STATE_MINUTE,0);
		    ringtone_Uri = Uri.parse(settings.getString(STATE_URI, "content://settings/system/ringtone"));
		    music_name=settings.getString(STATE_MUSIC, getResources().getString(R.string.defaultRing));
		    buttonOn=settings.getBoolean(STATE_BUTTON, false);
		    buttonVibrate=settings.getBoolean(STATE_VIBRATE, false);   
		}
		final Calendar c = Calendar.getInstance();
		Year = c.get(Calendar.YEAR);
		Month = c.get(Calendar.MONTH);
		Day = c.get(Calendar.DAY_OF_MONTH);
		
		//set TextView to indicate the value of Hour and Minute.
		TextView hour_minute_View = (TextView)findViewById(R.id.ringHourMinuteShow);
		Integer tempHourInteger = Hour;
		Integer tempMinuteInteger = Minute;
		hour_minute_View.setText(tempHourInteger.toString()+":"+tempMinuteInteger.toString());
		
		//register the listener of seekbars.
		SeekBar HourSet = (SeekBar)findViewById(R.id.seekBar1); 
		Float temphourfFloat = (float) Hour;
		temphourfFloat = temphourfFloat/24*100;
		HourSet.setProgress(temphourfFloat.intValue());
		
		SeekBar MinuteSet = (SeekBar)findViewById(R.id.seekBar2);
		Float tempminuteFloat = (float) Minute;
		tempminuteFloat = tempminuteFloat/60*100;
		MinuteSet.setProgress(tempminuteFloat.intValue());
		
		
		HourSet.setOnSeekBarChangeListener(mSeekBarChangeListener1);
		MinuteSet.setOnSeekBarChangeListener(mSeekBarChangeListener2);
		
		TextView ringname=(TextView)this.findViewById(R.id.ringname);
		ToggleButton buttonflag=(ToggleButton)this.findViewById(R.id.Ring_set);
		ToggleButton buttonVflag=(ToggleButton)this.findViewById(R.id.Vibrate_set);

		ringname.setText(music_name);
		buttonflag.setChecked(buttonOn);
		buttonVflag.setChecked(buttonVibrate);
	}

	
	@Override
	public void onResume(){
		super.onResume();
		//Set AlarmClock
	//	this.setRing(this.findViewById(R.id.Ring_set));
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}  
	
	@Override
	protected void onStop() {
	    super.onStop();  // Always call the superclass method first
	    //store datas
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putInt(STATE_HOUR, Hour);
	    editor.putInt(STATE_MINUTE, Minute);
	    /*editor.putInt(STATE_YEAR, Year);
	    editor.putInt(STATE_MONTH, Month);
	    editor.putInt(STATE_DAY, Day);*/
	    editor.putString(STATE_URI, ringtone_Uri.toString());
	    editor.putString(STATE_MUSIC, music_name);
	    ToggleButton buttonflag=(ToggleButton)this.findViewById(R.id.Ring_set);
	    ToggleButton buttonVflag=(ToggleButton)this.findViewById(R.id.Vibrate_set);
	    editor.putBoolean(STATE_BUTTON, buttonflag.isChecked());
	    editor.putBoolean(STATE_VIBRATE, buttonVflag.isChecked());
	    editor.commit();
	    
	    //show how long will take before the alarm goes off.
	    Calendar c= Calendar.getInstance();
		long now=c.getTimeInMillis();
		Calendar setTime = Calendar.getInstance(); 
		setTime.set(Year, Month, Day, Hour, Minute,0); 
		long set=setTime.getTimeInMillis();
		int hoursLeft=(int)Math.floor((set-now)/1000/3600);
		long mod=(set-now)%(1000*3600);
		int minutesLeft=(int)Math.floor(mod/1000/60);
		if (minutesLeft == 0 && ((ToggleButton)this.findViewById(R.id.Ring_set)).isChecked() && now<set){
			Toast.makeText(this, getResources().getString(R.string.notificationText1)+getResources().getString(R.string.notificationText6)+getResources().getString(R.string.notificationText5), Toast.LENGTH_SHORT).show();
		}
		else if (hoursLeft == 0 && ((ToggleButton)this.findViewById(R.id.Ring_set)).isChecked() && now<set){
			Toast.makeText(this, getResources().getString(R.string.notificationText1)+String.valueOf(minutesLeft)+getResources().getString(R.string.notificationText5), Toast.LENGTH_SHORT).show();
		}
		else if(((ToggleButton)this.findViewById(R.id.Ring_set)).isChecked() && now<set){
			Toast.makeText(this, getResources().getString(R.string.notificationText1)+String.valueOf(hoursLeft)+getResources().getString(R.string.notificationText4)+String.valueOf(minutesLeft)+getResources().getString(R.string.notificationText5), Toast.LENGTH_SHORT).show();
		}
		
		this.setRing(this.findViewById(R.id.Ring_set));
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK) {
			if(data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI) != null){
				ringtone_Uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
			}
			if(!ringtone_Uri.equals(Uri.parse("content://settings/system/ringtone"))){
				//set the selected ringtone name
				String[] proj = {MediaStore.Audio.Media.DATA};
			    CursorLoader loader = new CursorLoader(this, ringtone_Uri, proj, null, null, null);
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
			    TextView activity_edittext = (TextView)this.findViewById(R.id.ringname);
			    activity_edittext.setText(music_name);
		    }
		}
	}
	
	public void setVibrate(View view){
		boolean on = ((ToggleButton) view).isChecked();
		buttonVibrate=on;
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
		Intent toService = new Intent(this,AlarmService.class);
		
		if (on && setORnot) {
		    // turn on the alarm 
			toService.putExtra(TIMETOSEND, setTime.getTimeInMillis());	
			this.startService(toService);
		} else {
	        // turn off the alarm
	    	this.stopService(toService);
		}
	}
	
	public static void cancelAlarm(){
		
	}
}
