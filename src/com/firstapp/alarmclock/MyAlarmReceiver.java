package com.firstapp.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/*import android.widget.Toast;*/

public class MyAlarmReceiver extends BroadcastReceiver  {
    @Override
    public void onReceive(Context context, Intent intent) {
/*        Toast.makeText(context, "Alarm went off", Toast.LENGTH_SHORT).show();*/
        //start activity
        
    	Intent i = new Intent();
        i.setClassName("com.firstapp.alarmclock", "com.firstapp.alarmclock.ringActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        
    }
}
