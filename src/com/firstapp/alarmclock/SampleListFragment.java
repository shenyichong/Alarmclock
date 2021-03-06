package com.firstapp.alarmclock;


import com.firstapp.alarmclock.R;
import com.firstapp.alarmclock.R.string;

import android.R.integer;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SampleListFragment extends ListFragment{ 
	
	//ListNum is the number of Alarms,not including the add button.
	public int ListNum;
	
	public interface OnItemClickedListener {
		public void onItemClicked(int position);
	}
	OnItemClickedListener mListener;
	
	public interface CheckBoxListerner{
		public void onCheckBoxClicked(boolean checkbox,int position);
	}
	CheckBoxListerner mCheckBoxListerner;
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnItemClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnItemClickedListener");
        }
        try {
        	mCheckBoxListerner = (CheckBoxListerner)activity;
		} catch (Exception e) {
			throw new ClassCastException(activity.toString() + " must implement CheckBoxListerner");
		}
    }
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
        return inflater.inflate(R.layout.list, null); 
    } 
	public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        SampleAdapter adapter = new SampleAdapter(getActivity());  
        for (int i = 0; i < ListNum; i++) { 
            adapter.add(new SampleItem(MainActivity.AlarmNames.get(i), R.drawable.notify_button,MainActivity.AlarmCheckbox.get(i)));  
        }
        setListAdapter(adapter);
        /*AddAdapter adapter2 = new AddAdapter(getActivity());
        adapter2.add(new addItem(getResources().getString(R.string.add_view), R.drawable.add_icon));*/
    }
	
	private void setCheckBox(View view){
		CheckBox checkboxView = (CheckBox) view;
		int position = (Integer)checkboxView.getTag();
		boolean checkbox = checkboxView.isChecked();
		MainActivity.AlarmCheckbox.set(position,checkbox);
		mCheckBoxListerner.onCheckBoxClicked(checkbox,position);
	}
	
	/*private void onAddAlarm(View view){
		
	}*/
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id){
		
		// Implement an interface to interact with MainActivity.
		mListener.onItemClicked(position);
	}
	
	
	//class for the alarm button.
	public class SampleItem { 
        public String tag;  
        public int iconRes; 
        public boolean checkbox;
        public SampleItem(String tag, int iconRes ,boolean checkbox) {  
            this.tag = tag;   
            this.iconRes = iconRes;  
            this.checkbox = checkbox;
        }  
    }  
	
	public class SampleAdapter extends ArrayAdapter<SampleItem> {  
	  
        public SampleAdapter(Context context) {  
            super(context, 0); 
        } 
        public View getView(int position, View convertView, ViewGroup parent) {  
            if (convertView == null) {  
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);  
            } 
            ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);  
            icon.setImageResource(getItem(position).iconRes);  
            TextView title = (TextView) convertView.findViewById(R.id.row_title);  
            title.setText(getItem(position).tag); 
            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.row_checkbox);
            checkBox.setChecked(getItem(position).checkbox);
            checkBox.setTag(position);
            //register the checkbox click listener
            checkBox.setOnClickListener(new View.OnClickListener() {
      		    @Override
      		    public void onClick(View view) {
      		    	setCheckBox(view);
      		    }
      		});
            return convertView;  
        } 
    } 
	
	/*public class addItem{
		public String addText;
		public int addIcon;
		public addItem(String addtext,int addicon){
			this.addText=addtext;
			this.addIcon=addicon;
		}
	}
	public class AddAdapter extends ArrayAdapter<addItem>{
		public AddAdapter(Context context) {  
            super(context, 0);  
        } 
		public View getView(int position, View convertView, ViewGroup parent){
			if (convertView == null) {  
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_addbutton, null);  
            } 
			ImageView iconView = (ImageView) convertView.findViewById(R.id.add_icon);  
			iconView.setImageResource(getItem(position).addIcon);  
			TextView textView = (TextView) convertView.findViewById(R.id.add_alarm);
			textView.setText(getItem(position).addText); 
			textView.setOnClickListener(new View.OnClickListener() {
      		    @Override
      		    public void onClick(View view) {
      		    	onAddAlarm(view);
      		    }
      		});

			return convertView ;
		}
	}*/
}
