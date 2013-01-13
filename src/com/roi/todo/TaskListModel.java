package com.roi.todo;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskListModel extends SQLiteOpenHelper
{
	// Database version
	private static final int DATABASE_VERSION = 2;
	// Database name
	private static final String DATABASE_NAME = "tasksManager";
	// Task table name
	private static final String TASK_TABLE_NAME = "task";
	
	// Task table columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_ISCHECKED = "isChecked";
    
    // Task create table
    private static final String TASK_TABLE_CREATE = 
    		"CREATE TABLE " + TASK_TABLE_NAME + " ("
            + KEY_ID + " INTEGER PRIMARY KEY," 
            + KEY_DESCRIPTION + " TEXT,"
            + KEY_ISCHECKED + " INTEGER"+ ")";
    
    // TaskListModel instance
    private static TaskListModel taskListModel = null;
    
    // private C'tor
    private TaskListModel(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    // Getting instance of TaskListModel to initiate singleton pattern
    public static TaskListModel getInstance(Context context)
    {
    	if (taskListModel == null)
    	{
    		taskListModel = new TaskListModel(context);
    	}
    	
    	return taskListModel;
    }
    
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(TASK_TABLE_CREATE);
    }
    
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE_NAME);
 
        // Create table again
        onCreate(db);
    }
    
    // Adding new task
    public void addTask(Task task) 
    {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_DESCRIPTION, task.getDescription());
        values.put(KEY_ISCHECKED, task.isChecked());
        
        db.insert(TASK_TABLE_NAME, null, values);
        db.close();
    }
    
    // Getting single task
    public Task getTask(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor = db.query(TASK_TABLE_NAME, new String[] { KEY_ID,
        		KEY_DESCRIPTION, KEY_ISCHECKED }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        
        if (cursor != null)
            cursor.moveToFirst();
     
        Task task = new Task(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),Integer.parseInt(cursor.getString(2)));
        
        return task;
    }
    
    // Getting max id task
    public int getMaxIdTask()
    {
        SQLiteDatabase db = this.getReadableDatabase();
     
        String query = "SELECT MAX(id) AS max_id FROM " + TASK_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        int id = 0;     
        if (cursor.moveToFirst())
        {
            do
            {           
                id = cursor.getInt(0);                  
            } while(cursor.moveToNext());           
        }
        
        return id;
    }
    
    // Getting all tasks
    public ArrayList<Task> getAllTasks()
    {
    	ArrayList<Task> tasksArrayList = new ArrayList<Task>();
    	
       // Select All Query
       String selectQuery = "SELECT  * FROM " + TASK_TABLE_NAME;
    
       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);
    
       // looping through all rows and adding to list
       if (cursor.moveToFirst()) 
       {
           do {
        	   Task task = new Task();
        	   task.setId(Integer.parseInt(cursor.getString(0)));
        	   task.setDescription(cursor.getString(1));
        	   task.setChecked(Integer.parseInt(cursor.getString(2)));
        	   
               // Adding task to list
        	   tasksArrayList.add(0,task);
           } while (cursor.moveToNext());
       }
    
       return tasksArrayList;
   }
    
    // Getting tasks count
    public int getTasksCount()
    {
        String countQuery = "SELECT  * FROM " + TASK_TABLE_NAME;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        return cursor.getCount();
    }
    
    // Updating single task
    public int updateTask(Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_DESCRIPTION, task.getDescription());
        values.put(KEY_ISCHECKED, task.isChecked());
        
        // updating row
        return db.update(TASK_TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(task.getId()) });
    }
    
    // Deleting single task
    public void deleteTask(Task task) 
    {
        SQLiteDatabase db = this.getWritableDatabase();
        
        db.delete(TASK_TABLE_NAME, KEY_ID + " = ?",
        		new String[] { String.valueOf(task.getId()) });
        db.close();
    }
}
