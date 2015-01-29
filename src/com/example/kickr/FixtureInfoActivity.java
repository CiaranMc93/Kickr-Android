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
	
	//buttons for home and away
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
		
		//create the buttons
		home = (Button) findViewById(R.id.homeBtn);
		away = (Button) findViewById(R.id.awayBtn);
		
		//a bundle with all the variables from the previous intent
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

				startActivity(new Intent(FixtureInfoActivity.this,TeamOverall.class));
				
			}
		});
		
		away.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) {
				// Perform action on click

				startActivity(new Intent(FixtureInfoActivity.this,TeamOverall.class));
				
			}
		});
		
	}
	
}
