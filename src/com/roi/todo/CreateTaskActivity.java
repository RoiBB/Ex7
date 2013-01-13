package com.roi.todo;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.roi.todo.DatePickerFragment.MyOnDateSetListener;
import com.roi.todo.R;
import com.roi.todo.TimePickerFragment.MyOnTimeSetListener;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

@TargetApi(11)
public class CreateTaskActivity extends Activity implements MyOnTimeSetListener, MyOnDateSetListener
{
	private static TextView timeTextView;
	private static TextView dateTextView;
	private static EditText taskDescriptionEditText;
	
	public final static String TASK_ID = "com.roi.todo.TASK_ID";
	public final static String TASK_DESCRIPTION = "com.roi.todo.TASK_DESCRIPTION";
	public final static String TASK_ADDRESS = "com.roi.todo.TASK_ADDRESS";
	
	public final static String YEAR = "com.roi.todo.YEAR";
	public final static String MONTH = "com.roi.todo.MONTH";
	public final static String DAY = "com.roi.todo.DAY";
	public final static String HOUR_OF_DAY = "com.roi.todo.HOUR_OF_DAY";
	public final static String MINUTE = "com.roi.todo.MINUTE";
	
	private static int year;
	private static int month;
	private static int day;
	private static int hourOfDay;
	private static int minute;
	
	private static boolean isTimeSelected = false; 
	private static boolean isDateSelected = false; 
	
	@Override
	public void onCreate(Bundle savedInstanceStase)
	{
		super.onCreate(savedInstanceStase);
        setContentView(R.layout.create_task_layout);
        
        timeTextView = (TextView) findViewById(R.id.timeView);
        dateTextView = (TextView) findViewById(R.id.dateView);
        taskDescriptionEditText = (EditText) findViewById(R.id.task_description);
	}
	
	public void addNewTask(View view)
    {
		Intent intent = new Intent(this, MainActivity.class);
		
		String taskDescriptionStr = taskDescriptionEditText.getText().toString();
		
		if (taskDescriptionStr.isEmpty())
		{
			setResult(RESULT_CANCELED,intent);
		}
		else
		{
			if (isTimeSelected && isDateSelected)
			{
				intent.putExtra(YEAR, String.valueOf(year));
				intent.putExtra(MONTH, String.valueOf(month));
				intent.putExtra(DAY, String.valueOf(day));
				intent.putExtra(HOUR_OF_DAY, String.valueOf(hourOfDay));
				intent.putExtra(MINUTE, String.valueOf(minute));
				
				isTimeSelected = false;
				isDateSelected = false;
			}
			
			intent.putExtra(TASK_DESCRIPTION, taskDescriptionStr);
			
			timeTextView.setText("HH : MM");
			dateTextView.setText("DD / MM / YYYY");
			
			EditText taskAddressEditText = (EditText) findViewById(R.id.task_address);
			
			if( taskAddressEditText.getText().length() != 0 )
			{
				intent.putExtra(TASK_ADDRESS, taskAddressEditText.getText().toString());
			}
			
			setResult(RESULT_OK,intent);
		}
		
		finish();
    }
	
	public void fetchNewRandomTask(View view)
	{
		URL url = null;
		
		try 
		{
			url = new URL(Tools.urlRandomTaskAdress);
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		
		new GetFromWebTask().execute(url);
	}
	
	public void showTimePickerDialog(View v)
	{
	    DialogFragment newFragment = new TimePickerFragment();
	    newFragment.show(getFragmentManager(), "timePicker");
	}
	
	public void showDatePickerDialog(View v) 
	{
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
	}

	public void myOnTimeSet(int hourOfDay, int minute) 
	{
		CreateTaskActivity.hourOfDay = hourOfDay;
		CreateTaskActivity.minute = minute;
		isTimeSelected = true;
		
		timeTextView.setText(String.valueOf(hourOfDay) + " : " + String.valueOf(minute));
	}

	public void myOnDateSet(int year, int month, int day) 
	{
		CreateTaskActivity.year = year;
		CreateTaskActivity.month = month;
		CreateTaskActivity.day = day;
		isDateSelected = true;
		
		dateTextView.setText(String.valueOf(day) + " / " + String.valueOf(month+1) + " / " + String.valueOf(year));
	}
	
	public class GetFromWebTask extends AsyncTask<URL, Integer, String>
	{	
		@Override
		protected String doInBackground(URL... urls)
		{
			return Tools.getUrlResponse(urls[0]);
		}
		
		@Override 
		protected void onPostExecute(String result)
		{
			try 
			{
				JSONObject jsonResponse = new JSONObject(result);
				
				String description = jsonResponse.getString("description");
				
				taskDescriptionEditText.setText(description);
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
