package com.roi.todo;

import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

@TargetApi(11)
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener 
{
	private MyOnDateSetListener listener;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}
	
	public void onDateSet(DatePicker view, int year, int month, int day) 
	{
		listener.myOnDateSet(year, month, day);
	}
	
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		
		try 
		{
			listener = (MyOnDateSetListener) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity+" must implement MyOnSetDateListener");
		}
	}
	
	public interface MyOnDateSetListener
	{
		public void myOnDateSet(int year, int month, int day);
	}
}
