package com.roi.todo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.roi.todo.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity 
{
	private static TaskListAdapter taskListAdapter;
	private static ArrayList<Task> taskArrayList;
	private static TaskListModel taskListModel;
	
	private static final int ADD_TASK_SIMPLE = 100;
	
	private static final int SERVICE_FIRST_ALARM = 3000;
	
	private Geocoder geocoder;
	private LocationManager locationManager;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        
        ListView listView = (ListView) findViewById(R.id.main_item_list);
        
        taskListModel = TaskListModel.getInstance(this);
        taskArrayList = taskListModel.getAllTasks();
        
        taskListAdapter = new TaskListAdapter(this,taskArrayList);
        listView.setAdapter(taskListAdapter);
        
        /*listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				String item = ((TextView)view).getText().toString();
				System.out.println("aaaaaaaaa");
                Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
			}
        	
		});*/
        
        addDailyTasks();
        
        geocoder = new Geocoder(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }
    
    public void addDailyTasks()
    {
    	Intent intent = new Intent(this, AddTasksService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SERVICE_FIRST_ALARM, AlarmManager.INTERVAL_DAY, pendingIntent);
    }
    
    public void goToCreateTaskActivity(View view)
    {
    	Intent intent = new Intent(this, CreateTaskActivity.class);
    	startActivityForResult(intent, ADD_TASK_SIMPLE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	if (requestCode == ADD_TASK_SIMPLE)
    	{
	    	if (resultCode == RESULT_OK)
	    	{
	    		String taskDescription = data.getStringExtra(CreateTaskActivity.TASK_DESCRIPTION);
	    		String taskAddress = data.getStringExtra(CreateTaskActivity.TASK_ADDRESS);
	    		
	    		String yearStr = data.getStringExtra(CreateTaskActivity.YEAR);
	    		String monthStr = data.getStringExtra(CreateTaskActivity.MONTH);
	    		String dayStr = data.getStringExtra(CreateTaskActivity.DAY);
	    		String hourOfDayStr = data.getStringExtra(CreateTaskActivity.HOUR_OF_DAY);
	    		String minuteStr = data.getStringExtra(CreateTaskActivity.MINUTE);
	    		
	    		Task newTask = new Task(taskListModel.getMaxIdTask()+1, taskDescription);
	    		
	    		taskListModel.addTask(newTask);
	    		taskArrayList.add(0, newTask);
	    		taskListAdapter.notifyDataSetChanged();
	    		
	    		if (yearStr != null && monthStr != null && dayStr != null && hourOfDayStr != null && minuteStr != null)
	    		{
	    			GregorianCalendar dateToRemind = new GregorianCalendar(Integer.parseInt(yearStr),Integer.parseInt(monthStr),Integer.parseInt(dayStr),Integer.parseInt(hourOfDayStr),Integer.parseInt(minuteStr));
	    		
		    		Intent intentBroadcast = new Intent("com.roi.todo.reminder_broadcast");
		    		
		    		intentBroadcast.putExtra(CreateTaskActivity.TASK_ID, String.valueOf(newTask.getId()));
					intentBroadcast.putExtra(CreateTaskActivity.TASK_DESCRIPTION, taskDescription);
					
	    			PendingIntent pendingIntent = PendingIntent.getBroadcast(this, newTask.getId(), intentBroadcast, 0);
	    			
	    			AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
	    			alarmManager.set(AlarmManager.RTC_WAKEUP, dateToRemind.getTimeInMillis(), pendingIntent);
	    		}
	    		
	    		if (taskAddress != null)
	    		{
	    			try 
	    			{
						List<Address> addresses = geocoder.getFromLocationName(taskAddress, 1);
						
						Address address = addresses.get(0);
						
						double latitude = address.getLatitude();
						double longitude = address.getLongitude();
						
						Intent intent = new Intent("com.roi.todo.location_broadcast");
						intent.putExtra(CreateTaskActivity.TASK_ID, String.valueOf(newTask.getId()));
						intent.putExtra(CreateTaskActivity.TASK_DESCRIPTION, taskDescription);
						
						PendingIntent proximityIntent = PendingIntent.getBroadcast(this, newTask.getId(), intent, 0);
						locationManager.addProximityAlert(latitude, longitude, 2000, 5000, proximityIntent);
					} 
	    			catch (IOException e) 
	    			{
						e.printStackTrace();
					}
	    		}
	    	}
	    	
	    	if (resultCode == RESULT_CANCELED)
	    	{
	    		Toast.makeText(this, "Please enter description.", Toast.LENGTH_SHORT).show();
	    	}
    	}
	}

}