package com.roi.todo;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

@TargetApi(16)
public class ReminderBroadCastReceiver extends BroadcastReceiver 
{
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		String taskDescription = intent.getStringExtra(CreateTaskActivity.TASK_DESCRIPTION);
		String taskIdStr = intent.getStringExtra(CreateTaskActivity.TASK_ID);
		
		int taskId = Integer.parseInt(taskIdStr);
		
		Intent myIntent = new Intent(context, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, taskId, myIntent, 0);
		
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Notification notification = new Notification.Builder(context)
			.setContentTitle("You Have To Do:")
			.setContentText(taskDescription)
			.setSmallIcon(R.drawable.ic_launcher)
			.setTicker("ToDo's Reminder")
			.setWhen(System.currentTimeMillis())
			.setContentIntent(pendingIntent).build();
		
		notificationManager.notify(taskId, notification);
	}

}
