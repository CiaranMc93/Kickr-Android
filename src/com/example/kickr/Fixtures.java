package com.example.kickr;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class Fixtures extends Base_Activity 
{
	// id for the layout
	LinearLayout il;

	JSONArray jArray;
	// get the first object of the array
	JSONObject fixtureObject;
	
	//Strings to hold the ID,home teams, away team and venue
	String home;
	String away;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fixtures);
		StrictMode.enableDefaults();
		
		il = (LinearLayout) findViewById(R.id.layoutButtons);	
		
		final SwipeRefreshLayout swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipe.setColorSchemeColors(Color.RED, Color.CYAN, Color.GREEN, Color.MAGENTA);
		swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
			
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
	
	//gets the fixture data from the database
	public void getData()
	{
		String fixtureResult = "";
		
		InputStream input = null;
		
		//try catch hhtp client request
		try
		{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://ciaranmcmanus.server2.eu/getAllFixtures.php");
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
		
		//parse the JSON data that returns information needed
		try{
			String s = "";
			jArray = new JSONArray(fixtureResult);
			
			//loop through the array
			for(int i=0; i<jArray.length(); i++)
			{
				
				//get the specific object in the JSON
				JSONObject json = jArray.getJSONObject(i);
				home = json.getString("teamA");
				away = json.getString("teamB");
				
				// set up the dynamic buttons
				Button btn = new Button(this);
				btn.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)));
				btn.setId(i);
				btn.setText(home + " vs. " + away);
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
						//need to pass the match information to the fixture information screen
						try 
						{
							//get all the information needed to be sent to the next acitivity
							JSONObject json = jArray.getJSONObject(v.getId());
							String fixture = (String)json.get("fix_id");
							String home = (String)json.get("teamA");
							String referee = (String)json.get("referee");
							String dateTime = (String)json.get("date");
							String venue = (String)json.get("venue");
							String away = (String)json.get("teamB");
							String comp = (String)json.get("competition");
							
							//pass the match id to the fixture information
							Intent i = new Intent(getApplicationContext(), FixtureInfoActivity.class);
							i.putExtra("FixtureID", fixture);
							i.putExtra("Home",home);
							i.putExtra("Away",away);
							i.putExtra("Referee",referee);
							i.putExtra("Competition",comp);
							i.putExtra("Venue",venue);
							i.putExtra("Date and Time", dateTime);
							//start activity and pass in the bundle of information
							startActivity(i);
						} 
						catch (JSONException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
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
