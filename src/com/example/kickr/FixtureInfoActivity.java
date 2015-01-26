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
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class FixtureInfoActivity extends Base_Activity {
	
	String homeTeam;
	String awayTeam;
	String loc;
	String dateTime;
	String ref;
	String comp;
	
	Button home;
	Button away;
	
	//create JSONarray 
	JSONArray jArray = new JSONArray();
	
	//create string fixtureInfo
	String fixtureInfo;
	
	public FixtureInfoActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fixture_info);	
		
		//TextView fixture = (TextView) findViewById(R.id.matchTeams);
		TextView location = (TextView) findViewById(R.id.venue);
		TextView dateAndTime = (TextView) findViewById(R.id.matchDate_Time);
		TextView competition = (TextView) findViewById(R.id.league_Info);
		TextView referee = (TextView) findViewById(R.id.Referee);
		
		home = (Button) findViewById(R.id.homeBtn);
		away = (Button) findViewById(R.id.awayBtn);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) 
		{
			//get the values from the previous activity
		    homeTeam = extras.getString("Home");
		    awayTeam = extras.getString("Away");
		    loc = extras.getString("Venue");
		    dateTime = extras.getString("Date and Time");
		    ref = extras.getString("Referee");
		    comp = extras.getString("Competition");
		    
		    //set the text views to be equal to the previous acitivities values
		    location.setText(loc);
		    dateAndTime.setText(dateTime);
		    competition.setText(comp);
		    referee.setText(ref);
		    //set the text of the buttons
		    home.setText(homeTeam);
		    away.setText(awayTeam);
		}
		
	}
	
}
