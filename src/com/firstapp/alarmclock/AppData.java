package com.firstapp.alarmclock;

import android.net.Uri;

public class AppData {
	
	public int hourIntoflag;
	public String ringtone_name;
	
	public int Year;
	public int Month;
	public int Day;
	
	public int Hour;
	public int Minute;
	public Uri ringtone_Uri;
	public String music_name;
	public boolean buttonOn;
	public boolean buttonVibrate;
	public String alarm_name;
	
	public AppData(){
		hourIntoflag=0;
	}
}
