package com.firstapp.alarmclock;


import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;



public class ringActivity extends Activity{
	
	private static MediaPlayer mediaPlayer;
	private static Vibrator vibrator;
	private static AudioManager volMgr;
	private static int RINGERMODE; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ring);
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
		if(AlarmContentFragment.buttonVibrate){
			vibrator.vibrate(new long[]{1000,1000},0);
		}
		
		//play music
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setLooping(true);
		try {
			mediaPlayer.setDataSource(getApplicationContext(), AlarmContentFragment.ringtone_Uri);
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
		//stop serivce
		this.stopService(new Intent(this,AlarmService.class));
		//stop playing music 
		mediaPlayer.pause();
		mediaPlayer=null;
		//stop vibration if any
		if(AlarmContentFragment.buttonVibrate){
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
		
		
		this.finish();
	}
}
