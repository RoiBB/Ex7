package com.roi.todo;

import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

@TargetApi(11)
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener 
{
	private MyOnTimeSetListener listener;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		// Using current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
		DateFormat.is24HourFormat(getActivity()));
	}
	
	public void onTimeSet(TimePicker view, int hourOfDay, int minute)
	{
		listener.myOnTimeSet(hourOfDay, minute);
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
		try 
		{
			listener = (MyOnTimeSetListener) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity+" Must implement MyOnSetTimeListener");
		}
	}

	public interface MyOnTimeSetListener
	{
		public void myOnTimeSet(int hourOfDay, int minute);
	}
}
