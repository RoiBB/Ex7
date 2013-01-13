package com.roi.todo;


public class Task 
{
	private int id;
	private String description;
	private int isChecked;
	
	public Task() 
	{
		isChecked = 1;
	}
	
	public Task(int id, String description) {
		this.isChecked = 1;
		this.id = id;
		this.description = description;
	}

	public Task(int id, String description, int isChecked) {
		this.id = id;
		this.description = description;
		this.isChecked = isChecked;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public int isChecked() {
		return isChecked;
	}

	public void setChecked(int isChecked) {
		this.isChecked = isChecked;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other == this) 
		{
            return true;
        }
		
        if (other == null || other.getClass() != this.getClass())
        {
            return false;
        }

        Task otherTask = (Task) other;
		
		return otherTask.id == id;
		
		
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", description=" + description + "]";
	}
	
	
}
