package com.example.kickr;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateMatchStats extends Base_Activity implements OnDoubleTapListener, OnGestureListener {

	private static final String DEBUG_TAG = "Gestures";
	private GestureDetectorCompat mDetector;
	
	JSONObject createMatch = new JSONObject();
	
	TextView awayTeamText;
	
	
	String fixtureResult;
	
	
	
	
	//count swipe functionality
	int countRightSwipes;
	int countLeftSwipes;
	
	//make boolean for home and away team stats
	Boolean homeTeamStats;
	Boolean awayTeamStats;
	
	//fixture string
	String fixture_id;
	String homeTeam;
	String awayTeam;
	String venue;
	String competition;
	String referee;
	
	//Set strings to be the home team and the away team
	String team1 = "O'Dempseys";
	String team2 = "Mountmellick";
	
	//swipe count image
	Button swipeUpdate;
	
	//count variable
	int count = 0;
	
	//counter logic
	private Button pauseButton;
	//display the time
	private TextView timerValue;
	private long startTime = 0L;
	//handle the time
	private Handler customHandler = new Handler();
	//time logic
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	
	//timer for posession
	private long startTimePos = 0L;
	//handle the time
	private Handler posessionHandler = new Handler();
	//time logic
	long timeInMillisecondsPos = 0L;
	long timeSwapBuffPos = 0L;
	long updatedTimePos = 0L;
	
	//create timer for notifications
	private long startTimeNotification = 0L;
	//handle the time
	private Handler noteHandler = new Handler();
	//time logic
	long timeInMillisecondsNote = 0L;
	long timeSwapBuffNote = 0L;
	long updatedTimeNote = 0L;
	
	/*
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if (keyCode == KeyEvent.KEYCODE_BACK) {
	     //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
	     return true;
	     }
	     return super.onKeyDown(keyCode, event);    
	}
	*/

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_match_stats);
		
		//default home team to have posession
		homeTeamStats = true;
		
		timerValue = (TextView) findViewById(R.id.timerValue);
		swipeUpdate = (Button) findViewById(R.id.updateUser);
		
		awayTeamText = (TextView) findViewById(R.id.team2);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) 
		{
			//get the values from the previous activity
			fixture_id = extras.getString("FixtureID");
		    homeTeam = extras.getString("Home");
		    awayTeam = extras.getString("Away");
		    venue = extras.getString("Venue");
		    referee = extras.getString("Referee");
		    competition = extras.getString("Competition");
		   
		    setText(homeTeam,awayTeam);
		}
		
		getData(fixture_id,homeTeam,awayTeam,venue,competition,referee);
		
		// change the image
		// button functionality
		final Button playButton = (Button) findViewById(R.id.startMatch);
		playButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startTime = SystemClock.uptimeMillis();
				playButton.setVisibility(View.INVISIBLE);
				customHandler.postDelayed(updateTimerThread, 0);
				
				
				//posession timer
				startTimePos = SystemClock.uptimeMillis();
				posessionHandler.postDelayed(updateMatchPosession, 0);
				
				
				//notification timer
				startTimeNotification = SystemClock.uptimeMillis();
				noteHandler.postDelayed(updateMatchNotifications, 0);
			}
		});

		pauseButton = (Button) findViewById(R.id.pauseButton);

		pauseButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				timeSwapBuff += timeInMilliseconds;
				customHandler.removeCallbacks(updateTimerThread);

			}
		});
		
		//set the text of the teams
		setText(team1,team2);
		
		// Instantiate the gesture detector with the
		// application context and an implementation of
		// GestureDetector.OnGestureListener
		mDetector = new GestureDetectorCompat(this, this);
		// Set the gesture detector as the double tap
		// listener.
		mDetector.setOnDoubleTapListener(this);
		
	}
	
	//change the text for each team home and away
	public void setText(String team1, String team2)
	{
		//change the text to the specified name of each team.
		TextView homeTeam = (TextView) findViewById(R.id.team1);
	    homeTeam.setText(team1);
	    
	    TextView awayTeam = (TextView) findViewById(R.id.team2);
	    //awayTeam.setText(team2);
	    
	}
	
	//run the timer
	private Runnable updateTimerThread = new Runnable() 
	{
		public void run() 
		{

			//milliseconds calculation
			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

			updatedTime = timeSwapBuff + timeInMilliseconds;

			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			//update the display string
			timerValue.setText("" + mins + ":"+ String.format("%02d", secs));

			customHandler.postDelayed(this, 0);	
		}

	};
	
	private Runnable updateMatchPosession = new Runnable() 
	{
		public void run() 
		{

			//milliseconds calculation
			timeInMillisecondsPos = SystemClock.uptimeMillis() - startTimePos;

			updatedTimePos = timeSwapBuffPos + timeInMillisecondsPos;

			int secs = (int) (updatedTimePos / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			int milliseconds = (int) (updatedTimePos % 1000);
			//update the display string
			
			if(mins == 1)
			{
				Toast.makeText(UpdateMatchStats.this, "Yay",Toast.LENGTH_SHORT).show();
			}

			posessionHandler.postDelayed(this, 0);	
		}

	};
	
	private Runnable updateMatchNotifications = new Runnable() 
	{
		public void run() 
		{

			//milliseconds calculation
			timeInMillisecondsNote = SystemClock.uptimeMillis() - startTimeNotification;

			updatedTimeNote = timeSwapBuffNote + timeInMillisecondsNote;

			int secs = (int) (updatedTimeNote / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			//update the display string
			
			if(mins == 2)
			{
				Toast.makeText(UpdateMatchStats.this, "Notification",Toast.LENGTH_SHORT).show();
			}

			noteHandler.postDelayed(this, 0);	
		}

	};
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.mDetector.onTouchEvent(event);
		// Be sure to call the superclass implementation
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent event) 
	{
		//Log.d(DEBUG_TAG, "onDown: " + event.toString());
		return true;
	}

	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2,
			float velocityX, float velocityY) 
	{
		if(velocityX > 0 && velocityY > 0)
		{
			countLeftSwipes = 0;
			countRightSwipes++;
			
			if(countRightSwipes == 1)
			{
				swipeUpdate.setText(team1 + " in possession");
				awayTeamStats = false;
				homeTeamStats = true;
			}
			else if(countRightSwipes == 2)
			{
				swipeUpdate.setText(team1 + " is attacking");
			}
			
			if(countRightSwipes == 3)
			{
				Toast.makeText(UpdateMatchStats.this, "Yay",Toast.LENGTH_SHORT).show();
			}
		}
		else if(velocityX < 0 && velocityY < 0)
		{
			//swipe is when user moves finger across touch pad right or left
			//reset the swipes for home team
			countRightSwipes = 0;
			//count swipes for left team
			countLeftSwipes++;
			
			if(countLeftSwipes == 1)
			{
				swipeUpdate.setText(team2 + " in possession");
				awayTeamStats = true;
				homeTeamStats = false;
			}
			else if(countLeftSwipes ==2)
			{
				swipeUpdate.setText(team2 + " is attacking");
			}
			
			if(countLeftSwipes == 3)
			{
				Toast.makeText(UpdateMatchStats.this, "Yay",Toast.LENGTH_SHORT).show();
			}
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent event) {
		Toast.makeText(UpdateMatchStats.this,"Handle Subs",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onShowPress(MotionEvent event) {
	}
	
	//initialize handpass variables
	int homehandPasses = 0;
	int awayhandPasses = 0;

	@Override
	public boolean onSingleTapUp(MotionEvent event) 
	{	
		//make sure handpasses cant be counted until a team is in posession
		if(countRightSwipes == 0 && countLeftSwipes == 0)
		{
			
		}
		else
		{
			//count the handpasses for each team
			if(homeTeamStats == true)
			{
				homehandPasses++;
			}
			
			if(awayTeamStats == true)
			{
				awayhandPasses++;
			}
		}
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent event) 
	{
		Boolean selectSub = false;
		Boolean selectCard = false;
		
		selectSub = true;
		
		if(selectSub)
		{
			Toast.makeText(UpdateMatchStats.this,"Sub on/off",Toast.LENGTH_SHORT).show();
		}
		else if(selectCard)
		{
			Toast.makeText(UpdateMatchStats.this,"Card yellow/black/red",Toast.LENGTH_SHORT).show();
		}
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent event) {
		//Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
		return true;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent event) {
		//Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
		return true;
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	//gets the fixture data from the database
	public void getData(String fixture_id,String home, String away,String venue, String comp, String ref) 
	{
		InputStream input = null;
		JSONArray json = new JSONArray();
		try 
		{
			createMatch.put("FixtureID", fixture_id);
			createMatch.put("Home",home);
			createMatch.put("Away", away);
			createMatch.put("Venue", venue);
			createMatch.put("Competition", comp);
			createMatch.put("Referee", ref);
			
			
			json.put(0, createMatch);
		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		try 
		{
			HttpPost post = new HttpPost(
					"http://ciaranmcmanus.server2.eu/insertInto.php");
			List<NameValuePair> nVP = new ArrayList<NameValuePair>(2);
			nVP.add(new BasicNameValuePair("json", json.toString())); // studentJson
																		// is
																		// the
																		// JSON
																		// input

			// student.Json.toString() produces the correct JSON
			// [{"studentId":"2","class":"2a","dbname":"testDb"}]

			post.setEntity(new UrlEncodedFormEntity(nVP));
			response = client.execute(post);
			
			if (response != null) 
			{
				
				InputStream in = response.getEntity().getContent(); // Get the
																	// data in
																	// the
																	// entity

				BufferedReader reader = new BufferedReader(new InputStreamReader(in, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) 
				{
					sb.append(line + "\n");
				}
				in.close();


				fixtureResult = sb.toString();

				awayTeamText.setText(fixtureResult);
			}

		} catch (Exception e) {
			Log.e("log tag", "Error in Http connection" + e.toString());
		}
 

		/*
		// try catch hhtp client request
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		
		try
		{
		 HttpPost post = new HttpPost("http://ciaranmcmanus.server2.eu/insertInto.php?name=");
		 List<NameValuePair> nVP = new ArrayList<NameValuePair>(2);  
		 nVP.add(new BasicNameValuePair("json", json.toString()));  //studentJson is the JSON input

		//student.Json.toString() produces the correct JSON [{"studentId":"2","class":"2a","dbname":"testDb"}]

		 post.setEntity(new UrlEncodedFormEntity(nVP));
		 response = client.execute(post);
		 
			 if(response != null)
			 {
				 Log.e("Not Null","Yes");
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
                 
                BufferedReader reader = new BufferedReader(new InputStreamReader(input,"iso-8859-1"),8);
     			StringBuilder sb = new StringBuilder();
     			String line = null;
     			while((line = reader.readLine()) != null)
     			{
     				sb.append(line + "\n");
     			}
     			
     			in.close();
     			
     			fixtureResult = sb.toString();
     			
     			awayTeamText.setText(fixtureResult);
             }

		} 
		catch (Exception e) 
		{
			Log.e("log tag", "Error in Http connection" + e.toString());
		}
		
		*/
	}
	
	public void sendData()
	{
		
	}
}
