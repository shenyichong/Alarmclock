package com.firstapp.alarmclock;


import java.io.IOException;
import java.net.URI;

import android.R.integer;
import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;



public class ringActivity extends Activity{
	
	private static final String STATE_BUTTON = "BUTTON";
	private static final String GLO_SETTINGS="GLOBAL_SETTINGS";
	private static final String ALARM_CHECKBOX = "ALARM_CHECKBOX_";
	private static MediaPlayer mediaPlayer;
	private static Vibrator vibrator;
	private static AudioManager volMgr;
	private static int RINGERMODE; 
	
	private static int cur_AlarmRingNum = 0;
	
	private Uri ring_Uri;
	private boolean ring_Vibrate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_ring);
		
		//set different ringing type and vibration.
		AppData tempData;
		tempData = MainActivity.AlarmToRing_Datas.get(cur_AlarmRingNum);
		ring_Uri = tempData.ringtone_Uri;
		ring_Vibrate = tempData.buttonVibrate;
		
		//set RingerMode to play music and vibrate
		volMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		switch (volMgr.getRingerMode()){
			case AudioManager.RINGER_MODE_SILENT:
				RINGERMODE=1;
				volMgr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
				RINGERMODE=2;
				volMgr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				break;
			case AudioManager.RINGER_MODE_NORMAL:
				RINGERMODE=3;
				volMgr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				break;
		}
		
		//set Vibration
		vibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		if(ring_Vibrate){
			vibrator.vibrate(new long[]{1000,1000},0);
		}
		
		//play music
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setLooping(true);
		try {
			mediaPlayer.setDataSource(getApplicationContext(), ring_Uri);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaPlayer.start();
		
		//wake and unlock the screen
		PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		Window w = this.getWindow(); 
		if(!pm.isScreenOn() ){
			w.setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD, WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
			
			PowerManager.WakeLock wl = pm.newWakeLock(PowerManager. FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,"LOCK_TAG");
			wl.acquire();
			// ... do work...
			
			/*wl.release();  */ 
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	   if (keyCode == KeyEvent.KEYCODE_BACK){
		   return true;
	   }
	return true;
	}
	
	public void ringCancel(View v){

		//stop playing music 
		mediaPlayer.pause();
		mediaPlayer=null;
		//stop vibration if any
		if(ring_Vibrate){
			vibrator.cancel();
		}
		vibrator=null;
		//recover the RingerMode
		switch (RINGERMODE){
			case 1:
				volMgr.setRingerMode(AudioManager.RINGER_MODE_SILENT);
				break;
			case 2:
				volMgr.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
				break;
			case 3:
				volMgr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				break;
		}
		volMgr=null;
		//set the corresponding AlarmContentFragment's set_Ring button to false
		AppData tempData;
		tempData = MainActivity.AlarmToRing_Datas.get(cur_AlarmRingNum);
		String preferencesNameString = "Pref_"+String.valueOf(tempData.FragmentNum);
        SharedPreferences settings = this.getSharedPreferences(preferencesNameString,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(STATE_BUTTON, false);
        editor.commit();
        
        SharedPreferences globalSettings =  getSharedPreferences(GLO_SETTINGS,Context.MODE_PRIVATE);
        SharedPreferences.Editor globalEditor = globalSettings.edit();
        globalEditor.putBoolean(ALARM_CHECKBOX+String.valueOf(cur_AlarmRingNum), false);
        globalEditor.commit();
        
        
        
        AlarmService runningService = AlarmService.getServiceInstance();
        if (cur_AlarmRingNum+1 < MainActivity.AlarmToRing_Datas.size()) {
        	//set new notification for the next alarm
        	runningService.notebuilder.setContentText(getResources().getString(R.string.notificationText1)+ String.valueOf(MainActivity.AlarmToRing_Datas.get(cur_AlarmRingNum+1).Hour) + getResources().getString(R.string.notificationText2) + String.valueOf(MainActivity.AlarmToRing_Datas.get(cur_AlarmRingNum+1).Minute) + getResources().getString(R.string.notificationText3));
        	Notification note = runningService.notebuilder.build();
        	runningService.startForeground(1, note);
		}else {
			//stop serivce
			this.stopService(new Intent(this,AlarmService.class));
		}
        cur_AlarmRingNum = cur_AlarmRingNum+1;
		this.finish();
	}
}
