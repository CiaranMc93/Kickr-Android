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
import android.widget.TextView;
import android.widget.Toast;

public class FixtureInfoActivity extends Base_Activity {

	//create the text view variable
	TextView fixture;
	TextView location;
	TextView dateAndTime;
	TextView competition;
	TextView referee;
	
	String homeTeam;
	String awayTeam;
	String loc;
	String dateTime;
	String ref;
	String comp;
	
	//create JSONarray 
	JSONArray jArray = new JSONArray();
	
	//create string fixtureInfo
	String fixtureInfo;
	
	public FixtureInfoActivity(String Home,String Away, String Venue,String Date,String Referee,String Competition) {
		// TODO Auto-generated constructor stub
		this();
		setMatchId(Home,Away,Venue,Date,Referee,Competition);
	}
	
	public FixtureInfoActivity() {
		// TODO Auto-generated constructor stub
	}
	
	//getter and setter
	public void setMatchId(String home, String away, String venue, String date, String referee, String competition)
	{
		homeTeam = home;
		awayTeam = away;
		loc = venue;
		dateTime = date;
		ref = referee;
		comp = competition;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fixture_info);
		
		//create all the appropriate text views
		fixture = (TextView) findViewById(R.id.matchTeams);
		location = (TextView) findViewById(R.id.venue);
		dateAndTime = (TextView) findViewById(R.id.matchDate_Time);
		competition = (TextView) findViewById(R.id.league_Info);
		referee = (TextView) findViewById(R.id.Referee);	
		
	}
	
}
