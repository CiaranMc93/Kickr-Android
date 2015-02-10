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
import android.os.StrictMode;
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
		setContentView(R.layout.activity_main);
		
		StrictMode.enableDefaults();
		
		il = (LinearLayout) findViewById(R.id.live_matches);
		
		getData();
	}
	
	public void getData()
	{
		String loginResult = "";
		
		InputStream input = null;
		
		//try catch hhtp client request
		try
		{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://ciaranmcmanus.server2.eu/getAllMatches.php");
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			input = entity.getContent();
			
		}
		catch(Exception e)
		{
			Log.e("log tag","Error in Http connection" + e.toString());
			resultView.setText("Couldnt connect to database");
		}
		//convert response to string
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(input,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
			input.close();
			
			loginResult = sb.toString();
		}
		catch(Exception e)
		{
			Log.e("log tag","Error converting result" + e.toString());
		}
		
		//parse the JSON data that returns information needed
		try
		{
			String s = "";
			
			JSONArray jArray = new JSONArray(loginResult);
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
				btn.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)));
				btn.setId(i);
				btn.setText(mins + " " + home + " " + goalsHome + "-" + pointsHome + " " + goalsAway + "-" + pointsAway + " " + away);
				btn.setBackgroundColor(Color.TRANSPARENT);
				il.addView(btn);
				
				//add a divider to show the buttons
				TextView divider = new TextView(this);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
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
