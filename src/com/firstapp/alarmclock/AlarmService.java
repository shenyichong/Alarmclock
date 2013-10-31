package com.firstapp.alarmclock;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.support.v4.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;


public class AlarmService extends Service{
	
	public NotificationCompat.Builder notebuilder=new NotificationCompat.Builder(this);
	public static int AlarmRingNum;
	
	private static AlarmService instance;
	
	public static AlarmService getServiceInstance(){
		return instance;
	}
	
	@SuppressLint("NewApi")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    // We want this service to continue running until it is explicitly
	    // stopped, so return sticky.
		instance = this;
		Intent i=new Intent(this, MyAlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);
		
		AlarmManager alarmMgr=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
		//cancel all the Alarm services that set before.
		alarmMgr.cancel(pendingIntent);
		
		AlarmRingNum = MainActivity.AlarmToRing_Datas.size();
		for (int j = 0; j < AlarmRingNum; j++) {
			alarmMgr.set(AlarmManager.RTC_WAKEUP,MainActivity.AlarmToRing_Datas.get(j).TimeInMills,pendingIntent);
		}
		
		
		notebuilder.setSmallIcon(R.drawable.notification_bar);
		notebuilder.setContentTitle(getResources().getString(R.string.app_name));
		//set the ringing time
		//indicate the first alarm ringring time.
		notebuilder.setContentText(getResources().getString(R.string.notificationText1)+ String.valueOf(MainActivity.AlarmToRing_Datas.get(0).Hour) + getResources().getString(R.string.notificationText2) + String.valueOf(MainActivity.AlarmToRing_Datas.get(0).Minute) + getResources().getString(R.string.notificationText3));
		
		Intent resultIntent = new Intent(this, MainActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		//set notification to react with touch
		notebuilder.setContentIntent(resultPendingIntent);
		//set expanded zone of notification to react with touch
		//notebuilder.addAction(R.drawable.notify_button, getResources().getString(R.string.notify_button), resultPendingIntent);
		
		Notification note = notebuilder.build();
		this.startForeground(1, note);
		
		return START_STICKY;
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
		instance=null;
		this.stopForeground(true);
    }
}
