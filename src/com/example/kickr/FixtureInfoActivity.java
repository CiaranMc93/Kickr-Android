package com.example.kickr;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FixtureInfoActivity extends Base_Activity 
{
	
	//strings for the team information
	String homeTeam;
	String awayTeam;
	String loc;
	String dateTime;
	String ref;
	String comp;
	String fixID;
	
	//buttons for home and away
	Button home;
	Button away;
	
	//enter match information button
	Button contribute;
	
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
		
		//create the buttons
		home = (Button) findViewById(R.id.homeBtn);
		away = (Button) findViewById(R.id.awayBtn);
		
		//create contribute button
		contribute = (Button) findViewById(R.id.contribute);
		
		//a bundle with all the variables from the previous intent
		Bundle extras = getIntent().getExtras();
		if (extras != null) 
		{
			//get the values from the previous activity
			fixID = extras.getString("FixtureID");
		    homeTeam = extras.getString("Home");
		    awayTeam = extras.getString("Away");
		    loc = extras.getString("Venue");
		    dateTime = extras.getString("Date and Time");
		    ref = extras.getString("Referee");
		    comp = extras.getString("Competition");
		    
		    //set the text views to be equal to the previous acitivities values
		    location.setText("Venue: " + loc);
		    dateAndTime.setText("Date and Time: " + dateTime);
		    competition.setText("Competition: " + comp);
		    referee.setText("Referee: " + ref);
		    //set the text of the buttons
		    home.setText(homeTeam);
		    away.setText(awayTeam);
		}
		
		home.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) {
				// Perform action on click

				Intent i = new Intent(getApplicationContext(), TeamOverall.class);
				i.putExtra("Home",homeTeam);
				startActivity(i);
				finish();
				
				
			}
		});
		
		away.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) {
				// Perform action on click

				Intent i = new Intent(getApplicationContext(), TeamOverall.class);
				i.putExtra("Away", awayTeam);
				startActivity(i);
				finish();
				
			}
		});
		
		contribute.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) {
				// Perform action on click

				Intent i = new Intent(getApplicationContext(), LoginActivity.class);
				i.putExtra("FixtureID", fixID);
				i.putExtra("Home", homeTeam);
				i.putExtra("Away", awayTeam);
				i.putExtra("Venue", loc);
				i.putExtra("Competition", comp);
				i.putExtra("Referee", ref);
				startActivity(i);
				finish();
				
			}
		});
		
	}
	
}
