package com.roi.todo;

import java.util.ArrayList;

import com.roi.todo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class TaskListAdapter extends BaseAdapter
{
	private static Context context;
	private static LayoutInflater inflater;
	private static ArrayList<Task> taskArrayList;
	
	public TaskListAdapter(Context context, ArrayList<Task> results) 
	{
		TaskListAdapter.context = context;
		TaskListAdapter.taskArrayList = results;
		TaskListAdapter.inflater = LayoutInflater.from(context);
	 }
	
	public int getCount()
	{
		return taskArrayList.size();
	}

	public Task getItem(int position)
	{
		return taskArrayList.get(position);
	}

	public long getItemId(int position)
	{
		return getItem(position).getId();
	}
	
	private final OnClickListener removeButtonOnClickListener = new OnClickListener(){
		
		public void onClick(View view)
		{
			int position = (Integer) view.getTag();
			
			Task task = getItem(position);
			
			TaskListModel.getInstance(context).deleteTask(task);
			taskArrayList.remove(task);
			notifyDataSetChanged();
		}
	};
	
	private final OnClickListener checkboxOnClickListener = new OnClickListener(){
		
		public void onClick(View view)
		{
			int position = (Integer) view.getTag();
			
			Task task = getItem(position);
			
			CheckBox checkBox = (CheckBox) view;
			
			if (task.isChecked() == 1)
			{
				checkBox.setChecked(true);
				task.setChecked(0);
			}
			else
			{
				checkBox.setChecked(false);
				task.setChecked(1);
			}
			
			TaskListModel.getInstance(context).updateTask(task);
			notifyDataSetChanged();
		}
	};
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.task_layout, null);
			
			holder = new ViewHolder();
			
			holder.taskDescription = (TextView) convertView.findViewById(R.id.task_description);
			
			holder.taskRemoveButton = (Button) convertView.findViewById(R.id.task_remove_button);
			holder.taskRemoveButton.setOnClickListener(removeButtonOnClickListener);
			
			holder.taskCheckBox = (CheckBox) convertView.findViewById(R.id.task_checkbox);
			holder.taskCheckBox.setOnClickListener(checkboxOnClickListener);
			
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
	    }
		
		holder.taskRemoveButton.setTag(position);
		holder.taskCheckBox.setTag(position);
		
		if (taskArrayList.get(position).isChecked() == 0)
		{
			holder.taskCheckBox.setChecked(true);
		}
		else
		{
			holder.taskCheckBox.setChecked(false);
		}
		
		holder.taskDescription.setText(taskArrayList.get(position).getDescription());
		  
		return convertView;
	}
	
	static class ViewHolder 
	{
		TextView taskDescription;
		Button taskRemoveButton;
		CheckBox taskCheckBox;
	}
}
