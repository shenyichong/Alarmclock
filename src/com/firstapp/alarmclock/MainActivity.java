package com.firstapp.alarmclock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import com.firstapp.alarmclock.R.string;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainActivity extends Activity 
			implements SampleListFragment.OnItemClickedListener
					  ,SampleListFragment.CheckBoxListerner
					  ,AlarmContentFragment.ClockChangeListener
					  ,AlarmContentFragment.CheckboxListener
			          ,clockNameDialog.clockNameChangeListener{
	
	private SlidingMenu slidingmenu;
	
	private static final String GLO_SETTINGS="GLOBAL_SETTINGS";
	private static final String MENU_NUM = "MENU_NUMBER";
	private static final String MENU_FLAG = "MENU_FLAG";
	private static final String CUR_MENU_NUM = "CURRENT_MENU_NUMBER";
	private static final String ALARM_NAME = "ALARM_NAME_";
	private static final String ALARM_CHECKBOX = "ALARM_CHECKBOX_";
	
	private static final String STATE_BUTTON =  "BUTTON";
	private static final String STATE_HOUR = "HOUR";
	private static final String STATE_MINUTE = "MINUTE";
	private static final String STATE_VIBRATE = "VIBRATE";
	private static final String STATE_URI = "URI";
	private static final String STATE_DAY = "DAY";
	private static final String STATE_TIMEINMILLS = "TIMEINMILLS";
	private static final String STATE_MUSIC = "MUSIC";
	
	
	static int menu_number;
	static int cur_menu_number;
	public static ArrayList<String> AlarmNames = new ArrayList<String>();
	public static ArrayList<Boolean> AlarmCheckbox = new ArrayList<Boolean>();
	public static ArrayList<Long> AlarmTimes = new ArrayList<Long>();
	public static ArrayList<AppData> AlarmToRing_Datas = new ArrayList<AppData>();
	public static Vector<String> preferenceVector = new Vector<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_frame);
		
		//check whether there are globalSettings existing.
		SharedPreferences globalSettings =  getSharedPreferences(GLO_SETTINGS,Context.MODE_PRIVATE);
		if (globalSettings.getString(MENU_FLAG, null) == null) {
			menu_number=3;
			cur_menu_number=1;
			//initialize the content of the sliding menu
			for (int i = 0; i < menu_number; i++) {
	        	AlarmNames.add(i,"00:00"+" "+getString(R.string.ringcontent_blank));
	        	AlarmCheckbox.add(i, false);
			}
		}else {
			menu_number=globalSettings.getInt(MENU_NUM, 3);
			cur_menu_number=globalSettings.getInt(CUR_MENU_NUM, 1);
			//initialize the content of the sliding menu
			for (int i = 0; i < menu_number; i++) {
				AlarmNames.add(i,globalSettings.getString(ALARM_NAME+String.valueOf(i), "00:00"+" "+getString(R.string.ringcontent_blank)));
				AlarmCheckbox.add(i, globalSettings.getBoolean(ALARM_CHECKBOX+String.valueOf(i),false));
			}
		}
		
		//create the vector preferenceVector to store different sharedPreference file names.
		String defaultString = "Pref_";
		String eachString;
		for (int i = 0; i < menu_number; i++) {
			eachString=defaultString+String.valueOf(i+1);
			preferenceVector.add(eachString);
		}
		
		 if (AlarmService.getServiceInstance() !=null ) {
				AlarmService instance = AlarmService.getServiceInstance();
				instance.onDestroy();
			}
		
        initSlidingMenu();  
	}
	 
	private void initSlidingMenu() {  
        //setting up the main content View
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new AlarmContentFragment(),"alarmcontentfragment").commit(); 
		
        //setting up the distribute of sliding menu 
        slidingmenu = new SlidingMenu(this);  
        slidingmenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);  
        slidingmenu.setShadowWidthRes(R.dimen.shadow_width);  
        slidingmenu.setShadowDrawable(R.layout.shadow);  
        slidingmenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);  
        slidingmenu.setFadeDegree(0.35f);  
        slidingmenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);  
        
        
        SampleListFragment samplist = new SampleListFragment();
        samplist.ListNum=menu_number;
        //setting up the sliding menu's view  
        slidingmenu.setMenu(R.layout.menu_frame);     
        getFragmentManager().beginTransaction().replace(R.id.menu_frame,samplist,"samplelistfragment").commit();  
    } 
	
	//Implementation of the interface of SampListFragment.
	public void onItemClicked(int position){
		//there is some delay when the AlarmContentFragment slide in.
		cur_menu_number = position+1;
		getFragmentManager().beginTransaction().replace(R.id.content_frame, new AlarmContentFragment()).commit();
		slidingmenu.showContent();
	}
	
	public void onCheckBoxClicked(boolean checkbox,int position){
		if (position+1 == cur_menu_number) {
			AlarmContentFragment.buttonOn = checkbox;
			ToggleButton viewButton = (ToggleButton)findViewById(R.id.Ring_set);
			viewButton.setChecked(checkbox);
		}else{
			SharedPreferences changeCheckbox = getSharedPreferences(preferenceVector.get(position),Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = changeCheckbox.edit();
			if(changeCheckbox.getString(STATE_MUSIC, null) == null)
			//the Item first been clicked,TimeInMills is 0,and need to be calculated here.
			{	final Calendar c = Calendar.getInstance();
				int Day = c.get(Calendar.DAY_OF_MONTH);
				
				editor.putInt(STATE_DAY, Day);
				int Minute=0;
				editor.putInt(STATE_MINUTE, Minute);
				int Hour=0;
				editor.putInt(STATE_HOUR, Hour);
				Uri ringtone_Uri=Uri.parse("content://settings/system/ringtone");
				editor.putString(STATE_URI, ringtone_Uri.toString());
				String music_name=getResources().getString(R.string.defaultRing);
				editor.putString(STATE_MUSIC, music_name);
				boolean buttonVibrate=false;
				editor.putBoolean(STATE_VIBRATE, buttonVibrate);
				String alarm_name=getResources().getString(R.string.ringcontent_blank);
				editor.putString(ALARM_NAME, alarm_name);
				
				editor.putBoolean(STATE_BUTTON, checkbox);
				Long TimeInMills=(long)0;
				if (checkbox) {
					int Year = c.get(Calendar.YEAR);
					int Month = c.get(Calendar.MONTH);
					
					Calendar setTime = Calendar.getInstance(); 
					setTime.set(Year, Month, Day, Hour, Minute,0); 
					Calendar nowTime = Calendar.getInstance();
					long now=nowTime.getTimeInMillis();
					long set=setTime.getTimeInMillis();
					boolean setORnot=(now < set);
					if (!setORnot) {
						Day=Day+1;
						setTime.set(Year, Month, Day, Hour, Minute,0); 
						set=setTime.getTimeInMillis();
					}
					TimeInMills = set;
				}
				editor.putLong(STATE_TIMEINMILLS, TimeInMills);
				editor.commit();
			}else 
			//the Item had been clicked before,TimeInMills mustn't be 0.
			{
				editor.putBoolean(STATE_BUTTON, checkbox);
		        editor.commit();
			}
		}
	}
	
	
	//Implementation of the interface of AlarmContentFragment.
	public void onClockChange(String str){
		SampleListFragment listFrag = (SampleListFragment)getFragmentManager().findFragmentByTag("samplelistfragment");
		SampleListFragment.SampleAdapter adapter = (SampleListFragment.SampleAdapter)listFrag.getListAdapter();
		AlarmNames.set(cur_menu_number-1, str);
		adapter.clear();
		for (int i = 0; i < menu_number; i++) { 
            adapter.add(listFrag.new SampleItem(AlarmNames.get(i), R.drawable.notify_button, AlarmCheckbox.get(i).booleanValue()));  
        }
		listFrag.setListAdapter(adapter);
	}
	
	public void onClockNameChange(String str){
		SampleListFragment listFrag = (SampleListFragment)getFragmentManager().findFragmentByTag("samplelistfragment");
		SampleListFragment.SampleAdapter adapter = (SampleListFragment.SampleAdapter)listFrag.getListAdapter();
		AlarmNames.set(cur_menu_number-1, str);
		adapter.clear();
		for (int i = 0; i < menu_number; i++) { 
            adapter.add(listFrag.new SampleItem(AlarmNames.get(i), R.drawable.notify_button,AlarmCheckbox.get(i).booleanValue()));  
        }
		listFrag.setListAdapter(adapter);
	}
	
	public void onCheckboxChange(boolean checkbox){
		SampleListFragment listFrag = (SampleListFragment)getFragmentManager().findFragmentByTag("samplelistfragment");
		SampleListFragment.SampleAdapter adapter = (SampleListFragment.SampleAdapter)listFrag.getListAdapter();
		AlarmCheckbox.set(cur_menu_number-1, checkbox);
		adapter.clear();
		for (int i = 0; i < menu_number; i++) {
			adapter.add(listFrag.new SampleItem(AlarmNames.get(i), R.drawable.notify_button,AlarmCheckbox.get(i).booleanValue()));
		}
		
		listFrag.setListAdapter(adapter);
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
	    for (int i = 0; i < menu_number; i++) {
			Total_editor.putString(ALARM_NAME+String.valueOf(i), AlarmNames.get(i));
			Total_editor.putBoolean(ALARM_CHECKBOX+String.valueOf(i), AlarmCheckbox.get(i));
		}
	    Total_editor.commit();
	    
	    
	    //store the Fragments' data in AlarmToRing_Datas
	    AlarmToRing_Datas.clear();
	    for (int i = 0; i < menu_number; i++) {
	    	SharedPreferences alarmFragment = getSharedPreferences("Pref_"+String.valueOf(i+1), Context.MODE_PRIVATE);
			if (alarmFragment.getString(STATE_MUSIC, null)!=null) {
				AppData tempData = new AppData();
				tempData.TimeInMills = alarmFragment.getLong(STATE_TIMEINMILLS, (long)0);
				tempData.Day = alarmFragment.getInt(STATE_DAY, 1);
				tempData.Hour = alarmFragment.getInt(STATE_HOUR,0);
				tempData.Minute = alarmFragment.getInt(STATE_MINUTE,0);
				tempData.buttonOn = alarmFragment.getBoolean(STATE_BUTTON, false);
				tempData.buttonVibrate = alarmFragment.getBoolean(STATE_VIBRATE, false);
				tempData.ringtone_Uri = Uri.parse(alarmFragment.getString(STATE_URI, "content://settings/system/ringtone"));
				tempData.FragmentNum = i+1;
				AlarmToRing_Datas.add(tempData);
			}
		}
	    
	    //delete the AlarmToRing_Datas which doesn't ring.
	    if (AlarmToRing_Datas.size() !=0 ) 
	    	for (int i = 0; i < AlarmToRing_Datas.size(); i++)
	    		if (!AlarmToRing_Datas.get(i).buttonOn) 
	    			{
	    				AlarmToRing_Datas.remove(i);
	    				i--;
	    			}
	    
	    //sort the AlarmToRing_Datas according to time sequence.
	    int len = AlarmToRing_Datas.size();
	    if (len != 0) {
			if (len > 1) {
				for (int i = 0; i < len-1; i++) {
					for (int j = 1; j < len-i; j++) {
						AppData tempData ;
						if (AlarmToRing_Datas.get(j).TimeInMills < AlarmToRing_Datas.get(j-1).TimeInMills) {
							tempData = AlarmToRing_Datas.get(j);
							AlarmToRing_Datas.set(j, AlarmToRing_Datas.get(j-1));
							AlarmToRing_Datas.set(j-1, tempData);
						}
					}
				}
			}
			Intent toService = new Intent(this,AlarmService.class);
	    	this.startService(toService);
	    	
	    	//show when the first Alarm will ring. 
	    	Calendar nowTime = Calendar.getInstance();
			long now=nowTime.getTimeInMillis();
			long set=AlarmToRing_Datas.get(0).TimeInMills;
			int hoursLeft=(int)Math.floor((set-now)/1000/3600);
			long mod=(set-now)%(1000*3600);
			int minutesLeft=(int)Math.floor(mod/1000/60);
			if (hoursLeft == 0 && minutesLeft == 0){
				Toast.makeText(this, getResources().getString(R.string.notificationText1)+getResources().getString(R.string.notificationText6)+getResources().getString(R.string.notificationText5), Toast.LENGTH_SHORT).show();
			}
			else if (hoursLeft == 0 ){
				Toast.makeText(this, getResources().getString(R.string.notificationText1)+String.valueOf(minutesLeft)+getResources().getString(R.string.notificationText5), Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(this, getResources().getString(R.string.notificationText1)+String.valueOf(hoursLeft)+getResources().getString(R.string.notificationText4)+String.valueOf(minutesLeft)+getResources().getString(R.string.notificationText5), Toast.LENGTH_SHORT).show();
			}
		}
	    
	}
	
	public static void cancelAlarm(){
		
	}
}
