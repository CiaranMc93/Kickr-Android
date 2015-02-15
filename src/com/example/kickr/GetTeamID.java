package com.example.kickr;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class GetTeamID 
{
	String team;

	public GetTeamID(String teamName) 
	{
		// TODO Auto-generated constructor stub
		team = teamName;
		getTeamID(team);
	}
	
	public void getTeamID(String team)
	{
		String fixtureResult = "";
		
		InputStream input = null;
		
		try
		{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://ciaranmcmanus.server2.eu/getAllPlayers.php?team_name=" + team);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			input = entity.getContent();
			
		}
		catch(Exception e)
		{
			Log.e("log tag","Error in Http connection" + e.toString());
		}
		//convert response to string
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(input,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
			input.close();
			
			fixtureResult = sb.toString();
		}
		catch(Exception e)
		{
			Log.e("log tag","Error converting result" + e.toString());
		}
		
	}

}
