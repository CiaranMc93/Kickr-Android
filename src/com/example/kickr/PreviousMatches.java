package com.example.kickr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PreviousMatches extends Base_Activity 
{

	// id for the layout
	TableLayout homeEvents;
	TableLayout awayEvents;
		
	JSONArray jArray = new JSONArray();
	JSONObject obj = new JSONObject();
	String homeTeam;
	String awayTeam;
	String matchID;
	
	String homeID;
	String awayID;
	String eventID;
	String minutes;
	
	//textview of teams
	TextView home;
	TextView away;
	
	public PreviousMatches() 
	{
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.previous_match_timeline);
		
		home = (TextView) findViewById(R.id.home);
		away = (TextView) findViewById(R.id.away);
		
		//a bundle with all the variables from the previous intent
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// get the values from the previous activity
			homeTeam = extras.getString("Home");
			awayTeam = extras.getString("Away");
			matchID = extras.getString("MatchID");
		}
		
		//set the team name text
		home.setText(homeTeam);
		away.setText(awayTeam);
		
		try 
		{
			obj.put("teamA", homeTeam);
			obj.put("teamB", awayTeam);
			obj.put("MatchID", matchID);
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		getData();

	}
	
	public void getData()
	{
		GetAndPostDataToServer sendMatchData = new GetAndPostDataToServer();
		
		String jsonArrayString = sendMatchData.doInBackground(obj, "getEvents.php").toString();
		
		if(jsonArrayString.equals("No Connection"))
		{
			Toast.makeText(PreviousMatches.this, "No Connection, refresh", Toast.LENGTH_SHORT).show();
		}
		else
		{
			//parse the JSON data that returns information needed
			try
			{
				jArray = new JSONArray(jsonArrayString);
				
				//loop through the array
				for(int i=0; i<jArray.length(); i++)
				{
					
					//get the specific object in the JSON
					JSONObject json = jArray.getJSONObject(i);
					eventID = json.getString("event_id");
					minutes = json.getString("minutes");
					
					Log.e("Events",eventID);
					
				}
			}
			catch(Exception e)
			{
				//handle the exception
				Log.e("Log tag", "Error parsing data " + e.toString());
			}
		}
	}
}
