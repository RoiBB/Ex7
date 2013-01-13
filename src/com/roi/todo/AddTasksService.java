package com.roi.todo;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;

public class AddTasksService extends IntentService
{
	public AddTasksService() 
	{
		super("AddTasksService");
	}

	@Override
	protected void onHandleIntent(Intent intent) 
	{
		URL url = null;
		TaskListModel taskListModel = TaskListModel.getInstance(null);
		
		try
		{
			url = new URL(Tools.urlRandomTaskAdress);
			
			String response = Tools.getUrlResponse(url);
			
			JSONObject jsonResponse = new JSONObject(response);
			
			String description = jsonResponse.getString("description");
			
			Task newTask = new Task(taskListModel.getMaxIdTask()+1, description);
    		
    		taskListModel.addTask(newTask);
		}
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	}

}
