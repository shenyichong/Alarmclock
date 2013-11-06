package com.firstapp.alarmclock;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class clockNameDialog extends DialogFragment{
	
	
	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. 
	public interface NameDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
	
	// Use this instance of the interface to deliver action events
	NameDialogListener mListener;*/
	public interface clockNameChangeListener{
		public void onClockNameChange(String str);
	}
	clockNameChangeListener mclocknameChange;
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        	mclocknameChange = (clockNameChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement clockNameChangeListener");
        }
    }
	
	public static String alarmName;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder nameBuilder = new AlertDialog.Builder(getActivity());
		
		// Get the layout inflater
		final LayoutInflater inflater = getActivity().getLayoutInflater();
		
		final View clockNameDialogView = inflater.inflate(R.layout.dialog_alarm_name, null);
		final EditText nameAlarmText = (EditText) clockNameDialogView.findViewById(R.id.alarm_name);
		nameAlarmText.setText(AlarmContentFragment.alarm_name);
		
		nameBuilder.setView(clockNameDialogView);
		nameBuilder.setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
					   @Override    
					   public void onClick(DialogInterface dialog, int id) {
			                   // User confirmed the dialog
						   	   alarmName=nameAlarmText.getText().toString();
			               }
			           });
		nameBuilder.setNegativeButton(getResources().getString(R.string.CANCEL), new DialogInterface.OnClickListener() {
					       public void onClick(DialogInterface dialog, int id) {
					           // User cancelled the dialog
					    	   alarmName=AlarmContentFragment.alarm_name;
					    	   clockNameDialog.this.getDialog().cancel();
					       }
					    });
		return nameBuilder.create();
	}
	
	public void onDestroyView (){
		TextView v = (TextView)getActivity().findViewById(R.id.ringcontent_blank);
		AlarmContentFragment.alarm_name=alarmName;
		v.setText(alarmName);
		super.onDestroyView();
		TextView hour_minute_View = (TextView)getActivity().findViewById(R.id.ringHourMinuteShow);
		mclocknameChange.onClockNameChange(hour_minute_View.getText()+" "+alarmName);
	}
	
}
