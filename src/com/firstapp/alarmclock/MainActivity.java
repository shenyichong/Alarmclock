package com.firstapp.alarmclock;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.widget.ListView;


public class MainActivity extends Activity 
			implements SampleListFragment.OnItemClickedListener
					  ,AlarmContentFragment.ClockChangeListener
			          ,clockNameDialog.clockNameChangeListener{
	
	private SlidingMenu slidingmenu;
	
	private static final String GLO_SETTINGS="GLOBAL_SETTINGS";
	private static final String MENU_NUM = "MENU_NUMBER";
	private static final String MENU_FLAG = "MENU_FLAG";
	private static final String CUR_MENU_NUM = "CURRENT_MENU_NUMBER";
	
	static int menu_number;
	static int cur_menu_number;
	public static ArrayList<String> AlarmNames = new ArrayList<String>();
	public static ArrayList<Long> AlarmTimes = new ArrayList<Long>();
	
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
        	AlarmNames.add("00:00"+" "+getString(R.string.ringcontent_blank));
		}
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
	
	//Implementation of the interface of AlarmContentFragment.
	public void onClockChange(String str){
		SampleListFragment listFrag = (SampleListFragment)getFragmentManager().findFragmentByTag("samplelistfragment");
		SampleListFragment.SampleAdapter adapter = (SampleListFragment.SampleAdapter)listFrag.getListAdapter();
		AlarmNames.set(cur_menu_number-1, str);
		adapter.clear();
		for (int i = 0; i < menu_number; i++) { 
            adapter.add(listFrag.new SampleItem(AlarmNames.get(i), R.drawable.notify_button));  
        }
		listFrag.setListAdapter(adapter);
	}
	
	public void onClockNameChange(String str){
		SampleListFragment listFrag = (SampleListFragment)getFragmentManager().findFragmentByTag("samplelistfragment");
		SampleListFragment.SampleAdapter adapter = (SampleListFragment.SampleAdapter)listFrag.getListAdapter();
		AlarmNames.set(cur_menu_number-1, str);
		adapter.clear();
		for (int i = 0; i < menu_number; i++) { 
            adapter.add(listFrag.new SampleItem(AlarmNames.get(i), R.drawable.notify_button));  
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
	    Total_editor.commit();
	}
	
	public static void cancelAlarm(){
		
	}
}
