package com.example.kickr;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.content.ClipData.Item;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LiveMatches extends Base_Activity 
{
	//show the results 
	TextView resultView;
	
	LinearLayout il;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fixtures);
		
		StrictMode.enableDefaults();
		
		il = (LinearLayout) findViewById(R.id.layoutButtons);
		
		final SwipeRefreshLayout swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipe.setColorSchemeColors(Color.RED, Color.CYAN, Color.GREEN, Color.MAGENTA);
		swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh() {
				
				swipe.setRefreshing(true);
				(new Handler()).postDelayed(new Runnable() {
					
					@Override
					public void run() {
						
						swipe.setRefreshing(false);	
						//get the data again
						il.removeAllViews();
						getData();
					}
				},3000);
			}
		});
		
		getData();
	}
	
	public void getData()
	{
		GetAndPostDataToServer getMatches = new GetAndPostDataToServer();
		
		String jsonArrayString = getMatches.getData("getAllMatches.php").toString();
		//parse the JSON data that returns information needed
		try
		{
			String s = "";
			
			JSONArray jArray = new JSONArray(jsonArrayString);
			//loop through the array
			for(int i=0; i<jArray.length(); i++)
			{
				JSONObject json = jArray.getJSONObject(i);
				String mins = json.getString("match_mins");
				String home = json.getString("teamA");
				String pointsHome = json.getString("teamApoints");
				String goalsHome = json.getString("teamAgoals");
				String away = json.getString("teamB");
				String pointsAway = json.getString("teamBpoints");
				String goalsAway = json.getString("teamBgoals");
				
				// set up the dynamic buttons
				Button btn = new Button(this);
				btn.setLayoutParams((new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)));
				btn.setPadding(0, 0, 0, 0);
				btn.setMaxLines(1);
				btn.setId(i);
				btn.setText(mins + "min" + " " + home + " " + goalsHome + "-" + pointsHome + " | " + goalsAway + "-" + pointsAway + " " + away);
				btn.setBackgroundColor(Color.TRANSPARENT);
				il.addView(btn);
				
				//add a divider to show the buttons
				TextView divider = new TextView(this);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				divider.setLayoutParams(lp);
				divider.setBackgroundColor(Color.parseColor("#B2C5D1"));
				il.addView(divider);
				

				btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) 
					{
						//need to pass the match information to the match information screen
						
					}
				});
			}
			
		}
		catch(Exception e)
		{
			//handle the exception
			Log.e("Log tag", "Error parsing data " + e.toString());
		}
	}
}
