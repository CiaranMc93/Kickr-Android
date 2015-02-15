package com.example.kickr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MatchInfoActivity extends Base_Activity 
{

	//strings to hold information
	String homeTeam;
	String awayTeam;
	String loc;
	String dateTime;
	String ref;
	String comp;
	String minutes;
	String pointsHome;
	String pointsAway;
	String goalsHome;
	String goalsAway;
	
	//texviews and buttons to be displayed
	TextView location;
	TextView dateAndTime;
	TextView competition;
	TextView referee;
	TextView mins;
	Button home;
	Button away;
	
	public MatchInfoActivity() 
	{
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.match_info_activity);	
		
		//TextView fixture = (TextView) findViewById(R.id.matchTeams);
		location = (TextView) findViewById(R.id.venue);
		dateAndTime = (TextView) findViewById(R.id.matchDate_Time);
		competition = (TextView) findViewById(R.id.league_Info);
		referee = (TextView) findViewById(R.id.Referee);
		mins = (TextView) findViewById(R.id.mins);

		// create the buttons
		home = (Button) findViewById(R.id.homeBtn);
		away = (Button) findViewById(R.id.awayBtn);
		
		//a bundle with all the variables from the previous intent
		Bundle extras = getIntent().getExtras();
		if (extras != null) 
		{
			// get the values from the previous activity
			homeTeam = extras.getString("Home");
			awayTeam = extras.getString("Away");
			loc = extras.getString("Venue");
			dateTime = extras.getString("Date and Time");
			ref = extras.getString("Referee");
			comp = extras.getString("Competition");
			minutes = extras.getString("Minutes");
			pointsHome = extras.getString("HomePoints");
			pointsAway = extras.getString("AwayPoints");
			goalsHome = extras.getString("HomeGoals");
			goalsAway = extras.getString("AwayGoals");

			// set the text views to be equal to the previous activities values
			location.setText("Venue: " + loc);
			competition.setText("Competition: " + comp);
			referee.setText("Referee: " + ref);
			// set the text of the buttons and text views
			mins.setText(minutes + "'" + " ");
			home.setText(homeTeam + " " + goalsHome + "-" + pointsHome + " ");
			away.setText(" " + goalsAway + "-" + pointsAway + " " + awayTeam);
		}

		home.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) {
				// Perform action on click

				Intent i = new Intent(getApplicationContext(),TeamOverall.class);
				i.putExtra("Home", homeTeam);
				startActivity(i);

			}
		});

		away.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) {
				// Perform action on click

				Intent i = new Intent(getApplicationContext(),TeamOverall.class);
				i.putExtra("Away", awayTeam);
				startActivity(i);

			}
		});
	}

}
