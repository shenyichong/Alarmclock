package com.firstapp.alarmclock;

//import java.util.Calendar;
import java.util.Vector;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
//import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
//import android.provider.MediaStore;
//import android.support.v4.content.CursorLoader;
import android.app.Activity;
//import android.app.DialogFragment;
import android.content.Context;
//import android.content.Intent;
import android.content.SharedPreferences;
//import android.database.Cursor;
import android.view.Menu;
//import android.view.View;
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.ToggleButton;


public class MainActivity extends Activity {
	
	private SlidingMenu slidingmenu;
	
	//private static final String STATE_HOUR = "HOUR";
	//private static final String STATE_MINUTE = "MINUTE";
	//private static final String STATE_URI = "URI";
	//private static final String PREFS_NAME="SETTINGS";
	
	private static final String GLO_SETTINGS="GLOBAL_SETTINGS";
	private static final String MENU_NUM = "MENU_NUMBER";
	private static final String MENU_FLAG = "MENU_FLAG";
	private static final String CUR_MENU_NUM = "CURRENT_MENU_NUMBER";
	
	//private static final String STATE_MUSIC = "MUSIC";
	//private static final String STATE_BUTTON = "BUTTON";
	//private static final String STATE_VIBRATE= "VIBRATE";
	//public static final String TIMETOSEND = "TIME";
	//private static final String ALARM_NAME = "NAME";
	
	static int hourIntoflag = 0;
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
	
	static int menu_number;
	static int cur_menu_number;
	
	
/*	private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener1 = new SeekBar.OnSeekBarChangeListener(){

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
			SeekBar HourSet = (SeekBar)findViewById(R.id.seekBar1); 
			
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
			SeekBar HourSet = (SeekBar)findViewById(R.id.seekBar1); 
			SeekBar MinuteSet =  (SeekBar)findViewById(R.id.seekBar2); 
			Float progressFloat=progressfFloat/60/24*100;
			Float progressShowFloat = ((float)tempHourInteger)/24*100 ;
			HourSet.setProgress(progressShowFloat.intValue()+progressFloat.intValue());
			
			//change the indicator once the thumb moves.
			TextView hour_minute_View = (TextView)findViewById(R.id.ringHourMinuteShow);
			
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
	};*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_frame);
		//check whether there are globalSettings existing.
		SharedPreferences globalSettings =  getSharedPreferences(GLO_SETTINGS,Context.MODE_PRIVATE);
		if (globalSettings.getString(MENU_FLAG, null) == null) {
			menu_number=3;
			cur_menu_number=1;
		}else {
			menu_number=globalSettings.getInt(MENU_NUM, 3);
			cur_menu_number=globalSettings.getInt(CUR_MENU_NUM, 1);
		}
		//create the vector preferenceVector to store different sharedPreference file names.
		Vector<String> preferenceVector = new Vector<String>();
		String defaultString = "Pref_";
		String eachString;
		for (int i = 0; i < menu_number; i++) {
			eachString=defaultString+String.valueOf(i+1);
			preferenceVector.add(eachString);
		}
		
		/*
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
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
		TextView hour_minute_View = (TextView)findViewById(R.id.ringHourMinuteShow);
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
		
		//set the alarm name
		TextView alarmNameView = (TextView)findViewById(R.id.ringcontent_blank);
		alarmNameView.setText(alarm_name);
		
		TextView ringname=(TextView)findViewById(R.id.ringname);
		ToggleButton buttonflag=(ToggleButton)findViewById(R.id.Ring_set);
		ToggleButton buttonVflag=(ToggleButton)findViewById(R.id.Vibrate_set);
		
		ringname.setText(music_name);
		buttonflag.setChecked(buttonOn);
		buttonVflag.setChecked(buttonVibrate);*/
		
		//initialize the SlidingMenu menu
        initSlidingMenu();  
	}
	 
	private void initSlidingMenu() {  
        //setting up the main content View
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new AlarmContentFragment()).commit(); 
		
        //setting up the distribute of sliding menu 
        slidingmenu = new SlidingMenu(this);  
        slidingmenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);  
        slidingmenu.setShadowWidthRes(R.dimen.shadow_width);  
        slidingmenu.setShadowDrawable(R.layout.shadow);  
        slidingmenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);  
        slidingmenu.setFadeDegree(0.35f);  
        slidingmenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);  
        
        //initialize the content of the sliding menu
        SampleListFragment samplist = new SampleListFragment();
        samplist.ListNum=3;
        for (int i = 0; i < samplist.ListNum; i++) {
        	samplist.AlarmNames.add("00:00");
		}
        //setting up the sliding menu's view  
        slidingmenu.setMenu(R.layout.menu_frame);     
        getFragmentManager().beginTransaction().replace(R.id.menu_frame,samplist).commit();  
    } 
	@Override  
	public void onBackPressed() {  
       // click return key to return the sliding menu
       if (slidingmenu.isMenuShowing()) {  
    	   slidingmenu.showContent();  
       } else {  
           super.onBackPressed();  
       }  
	} 
	
	//@Override
	//public void onResume(){
	//	super.onResume();
	//	//Set AlarmClock
	////	this.setRing(this.findViewById(R.id.Ring_set));
	//}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}  
	
	@Override
	protected void onStop() {
	    super.onStop();  // Always call the superclass method first
	    
	    //store datas in total.
	    SharedPreferences globalSettings =  getSharedPreferences(GLO_SETTINGS,Context.MODE_PRIVATE);
	    SharedPreferences.Editor Total_editor = globalSettings.edit();
	    Total_editor.putString(MENU_FLAG, "MENU_EXIST");
	    Total_editor.putInt(MENU_NUM, menu_number);
	    Total_editor.putInt(CUR_MENU_NUM, cur_menu_number);
	    Total_editor.commit();
	    
	    /* SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putInt(STATE_HOUR, Hour);
	    editor.putInt(STATE_MINUTE, Minute);
	    editor.putString(STATE_URI, ringtone_Uri.toString());
	    editor.putString(STATE_MUSIC, music_name);
	    ToggleButton buttonflag=(ToggleButton)this.findViewById(R.id.Ring_set);
	    ToggleButton buttonVflag=(ToggleButton)this.findViewById(R.id.Vibrate_set);
	    TextView alarmNameView = (TextView)findViewById(R.id.ringcontent_blank);
	    editor.putString(ALARM_NAME, alarmNameView.getText().toString());
	    editor.putBoolean(STATE_BUTTON, buttonflag.isChecked());
	    editor.putBoolean(STATE_VIBRATE, buttonVflag.isChecked());
	    editor.commit();
	    
	    //show how long will take before the alarm goes off.
	    Calendar c= Calendar.getInstance();
		long now=c.getTimeInMillis();
		Calendar setTime = Calendar.getInstance(); 
		setTime.set(Year, Month, Day, Hour, Minute,0); 
		long set=setTime.getTimeInMillis();
		if (now>set) {
			setTime.set(Year, Month, Day+1, Hour, Minute,0); 
			set=setTime.getTimeInMillis();
		}
		int hoursLeft=(int)Math.floor((set-now)/1000/3600);
		long mod=(set-now)%(1000*3600);
		int minutesLeft=(int)Math.floor(mod/1000/60);
		if (minutesLeft == 0 && ((ToggleButton)findViewById(R.id.Ring_set)).isChecked()){
			Toast.makeText(this, getResources().getString(R.string.notificationText1)+getResources().getString(R.string.notificationText6)+getResources().getString(R.string.notificationText5), Toast.LENGTH_SHORT).show();
		}
		else if (hoursLeft == 0 && ((ToggleButton)findViewById(R.id.Ring_set)).isChecked()){
			Toast.makeText(this, getResources().getString(R.string.notificationText1)+String.valueOf(minutesLeft)+getResources().getString(R.string.notificationText5), Toast.LENGTH_SHORT).show();
		}
		else if(((ToggleButton)findViewById(R.id.Ring_set)).isChecked()){
			Toast.makeText(this, getResources().getString(R.string.notificationText1)+String.valueOf(hoursLeft)+getResources().getString(R.string.notificationText4)+String.valueOf(minutesLeft)+getResources().getString(R.string.notificationText5), Toast.LENGTH_SHORT).show();
		}
		
		this.setRing(findViewById(R.id.Ring_set));*/
	}	
	
	/*public void fillinName(View view){
		DialogFragment newFragment = new clockNameDialog();
	    newFragment.show(getFragmentManager(), "Alarm_Name"); 
	}*/
	
	
	/*select ring*/
/*	public void selectRingtone(View v){
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
	
	start the alarm
	public void setRing(View view){
		
		boolean on = ((ToggleButton) view).isChecked();
		
		Calendar setTime = Calendar.getInstance(); 
		setTime.set(Year, Month, Day, Hour, Minute,0); 
		Calendar nowTime = Calendar.getInstance();
		long now=nowTime.getTimeInMillis();
		long set=setTime.getTimeInMillis();
		boolean setORnot=(now < set);
		Intent toService = new Intent(this,AlarmService.class);
		
		if (on) {
			if (setORnot) {
				 // turn on the alarm 
				toService.putExtra(TIMETOSEND, setTime.getTimeInMillis());	
				this.startService(toService);
			}else {
				setTime.set(Year, Month, Day+1, Hour, Minute,0); 
				toService.putExtra(TIMETOSEND, setTime.getTimeInMillis());	
				this.startService(toService);
			}
		}
		else {
	        // turn off the alarm
	    	this.stopService(toService);
		}
	}*/
	
	public static void cancelAlarm(){
		
	}
}
