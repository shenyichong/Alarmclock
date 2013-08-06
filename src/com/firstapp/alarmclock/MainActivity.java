package com.firstapp.alarmclock;

import java.util.Calendar;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainActivity extends Activity 
			implements DatePickerFragment.OnAlarmSetListener,TimePickerFragment.OnAlarmSetListener{
	
	private static final String STATE_HOUR = "HOUR";
	private static final String STATE_MINUTE = "MINUTE";
	private static final String STATE_YEAR = "YEAR";
	private static final String STATE_MONTH = "MONTH";
	private static final String STATE_DAY = "DAY";
	private static final String STATE_URI = "URI";
	private static final String PREFS_NAME="SETTINGS";
	private static final String STATE_MUSIC = "MUSIC";
	private static final String STATE_BUTTON = "BUTTON";
	private static final String STATE_VIBRATE= "VIBRATE";
	
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
			Year = c.get(Calendar.YEAR);
			Month = c.get(Calendar.MONTH);
			Day = c.get(Calendar.DAY_OF_MONTH);
			ringtone_Uri=Uri.parse("content://settings/system/ringtone");
			music_name=getResources().getString(R.string.defaultRing);
			buttonOn=false;
			buttonVibrate=false;
			
			TextView ringtime=(TextView)this.findViewById(R.id.ringtime_blank);
			TextView ringdate=(TextView)this.findViewById(R.id.ringdate_blank);
			TextView ringname=(TextView)this.findViewById(R.id.ringname);
			ToggleButton buttonflag=(ToggleButton)this.findViewById(R.id.Ring_set);
			ToggleButton buttonVflag=(ToggleButton)this.findViewById(R.id.Vibrate_set);
			ringtime.setText(String.valueOf(Hour)+":"+ String.valueOf(Minute));
			ringdate.setText(String.valueOf(Month + 1) + "/" + String.valueOf(Day) + "/" + String.valueOf(Year));
			ringname.setText(music_name);
			buttonflag.setChecked(buttonOn);
			buttonVflag.setChecked(buttonVibrate);
		}
		else{
			Hour =settings.getInt(STATE_HOUR,0);
		    Minute = settings.getInt(STATE_MINUTE,0);
		    Year = settings.getInt(STATE_YEAR,0);
		    Month = settings.getInt(STATE_MONTH,0);
		    Day = settings.getInt(STATE_DAY,0);
		    ringtone_Uri = Uri.parse(settings.getString(STATE_URI, "content://settings/system/ringtone"));
		    music_name=settings.getString(STATE_MUSIC, getResources().getString(R.string.defaultRing));
		    buttonOn=settings.getBoolean(STATE_BUTTON, false);
		    buttonVibrate=settings.getBoolean(STATE_VIBRATE, false);
		    
		    TextView ringtime=(TextView)this.findViewById(R.id.ringtime_blank);
		    TextView ringdate=(TextView)this.findViewById(R.id.ringdate_blank);
		    TextView ringname=(TextView)this.findViewById(R.id.ringname);
		    ToggleButton buttonflag=(ToggleButton)this.findViewById(R.id.Ring_set);
		    ToggleButton buttonVflag=(ToggleButton)this.findViewById(R.id.Vibrate_set);
			ringtime.setText(String.valueOf(Hour)+":"+ String.valueOf(Minute));
			ringdate.setText(String.valueOf(Month + 1) + "/" + String.valueOf(Day) + "/" + String.valueOf(Year));
			ringname.setText(music_name);
			buttonflag.setChecked(buttonOn);
			buttonVflag.setChecked(buttonVibrate);
		}
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
	    editor.putInt(STATE_YEAR, Year);
	    editor.putInt(STATE_MONTH, Month);
	    editor.putInt(STATE_DAY, Day);
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
		
	}	
	/*select Time and Date*/
	public void showTimePickerDialog(View v) {
	    DialogFragment newFragment = new TimePickerFragment();
	    newFragment.show(getFragmentManager(), "timePicker");
	}
	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
	}
	public void onAlarmDateSet(){
		//Set AlarmClock
	    this.setRing(this.findViewById(R.id.Ring_set));
	}
	
	public void onAlarmTimeSet(){
		//Set AlarmClock
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
