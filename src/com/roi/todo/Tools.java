package com.roi.todo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class Tools 
{
	public static final String urlRandomTaskAdress = "http://mobile1-tasks-dispatcher.herokuapp.com/task/random";
	
	static String getUrlResponse(URL url)
	{
		String response = null;
		
		try 
		{
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			InputStreamReader inReader = new InputStreamReader(in);
			BufferedReader bufferedReader = new BufferedReader(inReader);
			
			StringBuilder responseBuilder = new StringBuilder();
			
			for (String line=bufferedReader.readLine(); line!=null; line=bufferedReader.readLine())
			{
				responseBuilder.append(line);
			}
			
			response = responseBuilder.toString();
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return response;
	}
}
