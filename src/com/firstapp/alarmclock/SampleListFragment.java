package com.firstapp.alarmclock;

import java.util.ArrayList;

import com.firstapp.alarmclock.R;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SampleListFragment extends ListFragment{ 
	
	public int ListNum;
	public ArrayList<String> AlarmNames = new ArrayList<String>();
	
	public interface OnItemClickedListener {
		public void onItemClicked(int position);
	}
	OnItemClickedListener mListener;
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnItemClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnItemClickedListener");
        }
    }
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
        return inflater.inflate(R.layout.list, null); 
    }  
	public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        SampleAdapter adapter = new SampleAdapter(getActivity());  
        for (int i = 0; i < ListNum; i++) { 
            adapter.add(new SampleItem(AlarmNames.get(i), R.drawable.notify_button));  
        }  
        setListAdapter(adapter); 
    }
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id){
		
		// Implement an interface to interact with MainActivity.
		mListener.onItemClicked(position);
	}
	
	private class SampleItem {  
        public String tag;  
        public int iconRes;  
        public SampleItem(String tag, int iconRes) {  
            this.tag = tag;   
            this.iconRes = iconRes;  
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
            
            return convertView;  
        } 
    } 
}
