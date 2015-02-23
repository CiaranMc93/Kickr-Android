package com.example.kickr;

import java.io.BufferedReader;
import java.io.File;
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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateMatchStats extends Activity implements OnDoubleTapListener, OnGestureListener, SensorEventListener {

	private GestureDetectorCompat mDetector;
	private static int TAKE_PICTURE = 1;
	private Uri imageUri;
	
	//create a notification
	/*
	NotificationCompat.Builder mBuilder =
		    new NotificationCompat.Builder(this)
		    .setSmallIcon(R.drawable.ic_launcher)
		    .setContentTitle("No Activity in 3 minutes")
		    .setContentText("Update Match");
		    */
	
	//boolean to make sure handpasses isnt updated with taps when not allowed
	Boolean isDialog;
	
	Boolean sendTrue = false;
	int matchID;
	
	//make sure the play button is pressed
	boolean update = false;
	
	//create variables to be used in order to get the chosen player by name
	int getEvent;
	JSONArray jArray;
	
	String created;
	private AlertDialog.Builder dialogBuilder;
	
	Boolean matchInProgress;
	
	Sensor accelerate;
	SensorManager sm;
	
	JSONArray updateMatch;
	JSONObject matchStats;
	
	//image view of the pitch
	ImageView pitch1;
	
	//text views to hold the team names
	TextView homeTeam1;
	TextView awayTeam1;

	int countPassesToScore;
	
	JSONObject createMatch = new JSONObject();
	
	//get rid of this eventually
	TextView homePoints;
	TextView awayPoints;
	TextView homeGoals;
	TextView awayGoals;
	TextView time;
	
	Button endMatch;
	
	String fixtureResult;
	
	//count swipe functionality
	int countRightSwipes;
	int countLeftSwipes;
	
	//make boolean for home and away team stats
	Boolean homeTeamStats;
	Boolean awayTeamStats;
	
	//initialize handpass variables
	int homehandPasses = 0;
	int awayhandPasses = 0;
	
	//boolean values for if the home team and away team are attacking of not
	Boolean homeAttack;
	Boolean awayAttack;
	
	//fixture string
	String fixture_id;
	String homeTeam;
	String awayTeam;
	String venue;
	String competition;
	String referee;
	
	//swipe count image
	Button swipeUpdate;
	
	//main timer
	private long startTime = 0L;
	//handle the time
	private Handler customHandler = new Handler();
	//time logic
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	int minutes;
	
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
	
	//create variables in order to hold the information of each team in each match
	//home team
	int teamAPoints;
	int teamAGoals;
	int yellowACards;
	int redACards;
	int blackACards;
	int teamAWides;
	int fortyFiveA;
	int freeKicksA;
	int penaltyA;
	int subA;
	
	//away team
	int teamBPoints;
	int teamBGoals;
	int yellowBCards;
	int redBCards;
	int blackBCards;
	int teamBWides;
	int fortyFiveB;
	int freeKicksB;
	int penaltyB;
	int subB;
	
	//is match over?
	Boolean isOver = false;
	
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
		
		//when stats are created initialise to 0
		teamAPoints = 0;
		teamBPoints = 0;
		teamAGoals = 0;
		teamBGoals = 0;
		teamAWides = 0;
		teamBWides = 0;
		countPassesToScore = 0;
		
		swipeUpdate = (Button) findViewById(R.id.updateUser);
		
		//textviews used to set the home and away team names
		homeTeam1 = (TextView) findViewById(R.id.team1);
		awayTeam1 = (TextView) findViewById(R.id.team2);
		
		//get the id of the image view
		pitch1 = (ImageView) findViewById(R.id.pitchUpdate);
		
		//textviews to set the teams points
		homePoints = (TextView) findViewById(R.id.homePoints);
		awayPoints = (TextView) findViewById(R.id.awayPoints);
		
		//set text view of the goals
		homeGoals = (TextView) findViewById(R.id.textView2);
		awayGoals = (TextView) findViewById(R.id.textView1);
		
		//set the time to be displayed
		time = (TextView) findViewById(R.id.time);
		endMatch = (Button) findViewById(R.id.endmatch);
		
		//text views for points
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerate = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sm.registerListener(this, accelerate,SensorManager.SENSOR_DELAY_NORMAL);		
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) 
		{
			//get the values from the previous activity
			fixture_id = extras.getString("FixtureID");
		    homeTeam = extras.getString("Home");
		    awayTeam = extras.getString("Away");
		    
		    //set the home team strings
		    homeTeam1.setText(homeTeam);
		    awayTeam1.setText(awayTeam);
		    
		}
		//get the integer value of the match id
		matchID = Integer.parseInt(fixture_id);
		
		//call to the method
		createMatch();
		
		endMatch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if(minutes < 2)
				{
					Toast.makeText(UpdateMatchStats.this, "Match is only " + minutes + " in", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(UpdateMatchStats.this, "Match is over", Toast.LENGTH_SHORT).show();
					isOver = true;	
					sendData(minutes);
				}	
			}
		});
		
		// change the image
		// button functionality
		final Button playButton = (Button) findViewById(R.id.playMatch);
		playButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				startTime = SystemClock.uptimeMillis();
				playButton.setVisibility(View.INVISIBLE);
				customHandler.postDelayed(updateTimerThread, 0);
				
				//posession timer
				startTimePos = SystemClock.uptimeMillis();
				posessionHandler.postDelayed(updateMatchPosession, 0);
				
				//notification timer
				startTimeNotification = SystemClock.uptimeMillis();
				noteHandler.postDelayed(updateMatchNotifications, 0);
				
				update = true;
			}
		});
		
		// Instantiate the gesture detector with the
		// application context and an implementation of
		// GestureDetector.OnGestureListener
		mDetector = new GestureDetectorCompat(this, this);
		// Set the gesture detector as the double tap
		// listener.
		mDetector.setOnDoubleTapListener(this);
		
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
			
			minutes = mins;
			
			time.setText(mins + "'");
			
			if(secs == 45)
			{
				sendTrue = false;
			}
			
			if(sendTrue == false && secs == 30)
			{
				sendData(mins);
				sendTrue = true;
			}
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
			//update the display string

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
			
			//if(mins == 3)
			//{
				//mBuilder.notify();
			//}

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
			if(update == false)
			{
			}
			else
			{
				countLeftSwipes = 0;
				countRightSwipes++;
				
				if(countRightSwipes == 1)
				{
					pitch1.setImageResource(R.drawable.home_pos);
					swipeUpdate.setText(homeTeam + " in possession");
					awayTeamStats = false;
					homeTeamStats = true;
					//reset the attack bool
					homeAttack = false;
					
					//if there is a change in the match then reset the notification timer
					updatedTimeNote = 0;
					timeInMillisecondsNote = 0;
				}
				
				if(countRightSwipes == 2)
				{
					pitch1.setImageResource(R.drawable.home_attack);
					swipeUpdate.setText(homeTeam + " is attacking");
					//set the boolean for this team to be true eg attacking
					homeAttack = true;
				}
				
				if(countRightSwipes == 3)
				{
					//send this data to the dialog so an event can be created
					createAcceptDialogBox(homeTeam);
					
				}
			}
		}
		else if(velocityX < 0 && velocityY < 0)
		{
			if(update == false)
			{
			}
			else
			{
				//swipe is when user moves finger across touch pad right or left
				//reset the swipes for home team
				countRightSwipes = 0;
				//count swipes for left team
				countLeftSwipes++;
				
				if(countLeftSwipes == 1)
				{
					swipeUpdate.setText(awayTeam + " in possession");
					pitch1.setImageResource(R.drawable.away_pos);
					awayTeamStats = true;
					homeTeamStats = false;
					//reset the attack bools also
					awayAttack = false;
					
					//if there is a change in the match then reset the notification timer
					updatedTimeNote = 0;
					timeInMillisecondsNote = 0;
				}
				
				if(countLeftSwipes == 2)
				{
					pitch1.setImageResource(R.drawable.away_attack);
					swipeUpdate.setText(awayTeam + " is attacking");
					
					//attacking team
					awayAttack = true;
				}
				
				if(countLeftSwipes == 3)
				{
					//send this data to the dialog so an event can be created
					createAcceptDialogBox(awayTeam);
				}	
			}
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent event) 
	{
		//update teams goals when there is a double tap
		//make sure there is a condition that doesnt allow goals to be updated
		if (homeAttack == false && awayAttack == false) 
		{
			if(awayTeamStats)
			{
				selectMatchOption(awayTeam,false);
			}
			else
			{
				selectMatchOption(homeTeam,false);
			}
			
		} 
		else 
		{
			//update home and away goals where the condition is true
			if (homeAttack == true) 
			{
				//have different options for a long press
				selectMatchOption(homeTeam,true);
			}
			else
			{
				//have different options for a long press
				selectMatchOption(awayTeam,true);
			}
		}
	}

	@Override
	public void onShowPress(MotionEvent event) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent event) 
	{	
		//make sure handpasses cant be counted until a team is in posession
		if(countRightSwipes == 0 && countLeftSwipes == 0)
		{
			
		}
		else
		{
			// count the handpasses for each team
			if (homeTeamStats == true) {
				Log.e("Got into home passes", "YUSS");
				// count all passes for home team
				homehandPasses++;
				// count the passes it takes to score
				countPassesToScore++;
			}

			if (awayTeamStats == true) {
				Log.e("Got into away passes", "YUSS");
				// count all passes for away team
				awayhandPasses++;
				// count the passes it takes to score
				countPassesToScore++;
			}

		};
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent event) 
	{
		
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
	public void createMatch() 
	{
		try 
		{
			createMatch.put("FixtureID", fixture_id);
			createMatch.put("MatchID", matchID);
		} 
		catch (JSONException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		GetAndPostDataToServer sendMatchData = new GetAndPostDataToServer();
		
		String note = sendMatchData.doInBackground(createMatch, "insertInto.php").toString();
		
		JSONArray checkMatch;
		try 
		{
			checkMatch = new JSONArray(note);
			
			JSONObject json = checkMatch.getJSONObject(0);
			
			if(json.getString("error").equals("1"))
			{
				Log.e("Got here","YAY");
			}
			
		} 
		catch (JSONException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*
		if(note.length() > 20)
		{
			timeSwapBuff += timeInMilliseconds;
			customHandler.removeCallbacks(updateTimerThread);
			
			//pass the match id to the fixture information
			Intent i = new Intent(getApplicationContext(), PreviousMatches.class);
			startActivity(i);
			//destroy the acitivity
			finish();
		}
		*/
		
	}
	
	public void sendData(int mins)
	{
		if(isOver == false)
		{
			//create the json object that is going to update the database with its contents
			JSONObject obj = new JSONObject();
			try 
			{
				obj.put("FixtureID", fixture_id);
				obj.put("MatchID", matchID);
				obj.put("teamAPoints", teamAPoints);
				obj.put("teamBPoints", teamBPoints);
				obj.put("teamAGoals", teamAGoals);
				obj.put("teamBGoals", teamBGoals);
				obj.put("homehandPasses", homehandPasses);
				obj.put("awayhandPasses", awayhandPasses);
				obj.put("matchMins", mins);
				obj.put("isOver", isOver);
				obj.put("teamAWides", teamAWides);
				obj.put("teamBWides", teamBWides);
				//add these to php
				obj.put("yellowCardsA", yellowACards);
				obj.put("redCardsA", redACards);
				obj.put("blackACards", blackACards);
				obj.put("blackBCards", blackBCards);
				obj.put("yellowCardsB", yellowBCards);
				obj.put("redCardsB", redBCards);
				obj.put("subsA", subA);
				obj.put("fortyFiveA", fortyFiveA);
				obj.put("subsB", subB);
				obj.put("fortyFiveB", fortyFiveB);
				obj.put("penaltyA", penaltyA);
				obj.put("penaltyB", penaltyB);
				obj.put("freeKicksA", freeKicksA);
				obj.put("freeKicksB", freeKicksB);
				
			} 
			catch (JSONException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			GetAndPostDataToServer sendMatchData = new GetAndPostDataToServer();
			
			String note = sendMatchData.doInBackground(obj, "updateMatches.php").toString();
			
			Toast.makeText(UpdateMatchStats.this, note, Toast.LENGTH_SHORT).show();
		}
		else
		{
			//create the json object that is going to update the database with its contents
			JSONObject obj = new JSONObject();
			try 
			{
				obj.put("FixtureID", fixture_id);
				obj.put("MatchID", matchID);
				obj.put("teamAPoints", teamAPoints);
				obj.put("teamBPoints", teamBPoints);
				obj.put("teamAGoals", teamAGoals);
				obj.put("teamBGoals", teamBGoals);
				obj.put("homehandPasses", homehandPasses);
				obj.put("awayhandPasses", awayhandPasses);
				obj.put("matchMins", mins);
				obj.put("isOver", isOver);
				obj.put("teamAWides", teamAWides);
				obj.put("teamBWides", teamBWides);
				//add these to php
				obj.put("yellowCardsA", yellowACards);
				obj.put("redCardsA", redACards);
				obj.put("blackACards", blackACards);
				obj.put("blackBCards", blackBCards);
				obj.put("yellowCardsB", yellowBCards);
				obj.put("redCardsB", redBCards);
				obj.put("subsA", subA);
				obj.put("fortyFiveA", fortyFiveA);
				obj.put("subsB", subB);
				obj.put("fortyFiveB", fortyFiveB);
				obj.put("penaltyA", penaltyA);
				obj.put("penaltyB", penaltyB);
				obj.put("freeKicksA", freeKicksA);
				obj.put("freeKicksB", freeKicksB);
				
			} 
			catch (JSONException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			GetAndPostDataToServer sendMatchData = new GetAndPostDataToServer();
			
			sendMatchData.doInBackground(obj, "updateMatches.php");
			
			timeSwapBuff += timeInMilliseconds;
			customHandler.removeCallbacks(updateTimerThread);
			
			//pass the match id to the fixture information
			Intent i = new Intent(getApplicationContext(), PreviousMatches.class);
			i.putExtra("Home",homeTeam);
			i.putExtra("Away",awayTeam);
			i.putExtra("MatchID", matchID);
			startActivity(i);
			//destroy the activity
			finish();
		}
	}
	
	public void takePhoto()
	{
		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "picture.jpg");
		imageUri = Uri.fromFile(photo);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		
		startActivityForResult(intent,TAKE_PICTURE);
	}
	
	/*
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == Activity.RESULT_OK)
		{
			Uri selectedImage = imageUri;
			getContentResolver().notifyChange(selectedImage, null);
			
			ContentResolver cr = getContentResolver();
			Bitmap bitmap;
			
			try
			{
				bitmap = MediaStore.Images.Media.getBitmap(cr, selectedImage);
				Toast.makeText(UpdateMatchStats.this, selectedImage.toString(), Toast.LENGTH_SHORT).show();
				
			}catch(Exception e)
			{
				
			}
		}

	}
	*/
	
	@Override
	public void onSensorChanged(SensorEvent event) 
	{
		// TODO Auto-generated method stub
		
		float xVal = 8.0f;
		float yVal = 8.0f;
		
		//if(event.values[0] > xVal && event.values[1] > yVal && counter == 0)
		//{
			//counter++;
			//takePhoto();
		//}
		
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
		

	@SuppressLint("NewApi")
	private boolean createAcceptDialogBox(String teamName)
	{
		isDialog = true;
		
		final ArrayList<String> selectEventList = new ArrayList<String>();
		
		selectEventList.add("Point");
		selectEventList.add("Wide");
		selectEventList.add("Goal");
		
		
		//create the alert dialog
		dialogBuilder = new AlertDialog.Builder(this);
		//create the layout that the alert will eventually get
		LayoutInflater inflater = this.getLayoutInflater();
		//create the view for the layout
		View v = inflater.inflate(R.layout.spinner_layout, null);
		//set title, add the view to the alert, set the positive button to have ok and cancel.
		dialogBuilder.setTitle("Select Event");
		dialogBuilder.setView(v);
		dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
						if (homeTeamStats == true) 
						{
							
							//switch statement based off of what the user selected
							switch(getEvent)
							{
							case 0:
								teamAPoints++;
								pitch1.setImageResource(R.drawable.away_goal_kick);
								swipeUpdate.setText(homeTeam + " scored");
								homePoints.setText(" " + teamAPoints);
								
								Boolean point = true;
								Boolean goal = false;
								
								GetAndPostDataToServer create = new GetAndPostDataToServer(fixture_id, point, goal, minutes, homeTeam,countPassesToScore);

								String success = create.doInBackground();
								
								Toast.makeText(UpdateMatchStats.this, success, Toast.LENGTH_SHORT).show();
								
								//reset variables
								homeAttack = false;
								awayAttack = false;
								awayTeamStats = false;
								homeTeamStats = false;
								
								swipeUpdate.setText("Goal kick to " + awayTeam);
								
								//reset the score pass counter to be 0
								countPassesToScore = 0;
								
								//reset the swipe counters for both left and right sides
								countRightSwipes = 0;
								countLeftSwipes = 0;
								
								
							case 1:
								Log.e("Got into home wides","YUSS");
								teamAWides++;
								pitch1.setImageResource(R.drawable.away_goal_kick);
								
								//reset variables
								homeAttack = false;
								awayAttack = false;
								awayTeamStats = false;
								homeTeamStats = false;
								
								swipeUpdate.setText("Goal kick to " + awayTeam);
								
								//reset the score pass counter to be 0
								countPassesToScore = 0;
								
								//reset the swipe counters for both left and right sides
								countRightSwipes = 0;
								countLeftSwipes = 0;
								break;
							case 2:
								teamAGoals++;
								pitch1.setImageResource(R.drawable.away_goal_kick);
								swipeUpdate.setText(homeTeam + " goal scored");		
								
								homeGoals.setText("" + teamAGoals);

								point = false;
								goal = true;
								
								create = new GetAndPostDataToServer(fixture_id, point, goal, minutes, homeTeam,countPassesToScore);

								success = create.doInBackground().toString();
								
								Toast.makeText(UpdateMatchStats.this, success, Toast.LENGTH_SHORT).show();
							
								
								//reset swipe count to be 0
								countRightSwipes = 0;
								homeAttack = false;
								awayAttack = false;
								awayTeamStats = false;
								homeTeamStats = false;
								
								//reset the score pass counter to be 0
								countPassesToScore = 0;
								break;
								
								default:
									break;
								
							}
						} 
						else 
						{
							switch(getEvent)
							{
							case 0:
								teamBPoints++;
								pitch1.setImageResource(R.drawable.home_goal_kick);
								swipeUpdate.setText(awayTeam + " scored");
								awayPoints.setText(" " + teamBPoints);
								
								Boolean point = true;
								Boolean goal = false;
								
								GetAndPostDataToServer create = new GetAndPostDataToServer(fixture_id, point, goal, minutes, awayTeam,countPassesToScore);

								String success = create.doInBackground();
								
								Toast.makeText(UpdateMatchStats.this, success, Toast.LENGTH_SHORT).show();
								
								
								//reset variables
								homeAttack = false;
								awayAttack = false;
								awayTeamStats = false;
								homeTeamStats = false;
								
								swipeUpdate.setText("Goal kick to " + homeTeam);
								
								//reset the score pass counter to be 0
								countPassesToScore = 0;
								//reset the swipe counters for both left and right sides
								countRightSwipes = 0;
								countLeftSwipes = 0;
								
								break;
							case 1:
								teamBWides++;
								pitch1.setImageResource(R.drawable.home_goal_kick);
								
								//reset variables
								homeAttack = false;
								awayAttack = false;
								awayTeamStats = false;
								homeTeamStats = false;
								
								swipeUpdate.setText("Goal kick to " + homeTeam);
								
								//reset the score pass counter to be 0
								countPassesToScore = 0;
								//reset the swipe counters for both left and right sides
								countRightSwipes = 0;
								countLeftSwipes = 0;
								break;
							case 2:
								teamBGoals++;

								pitch1.setImageResource(R.drawable.home_goal_kick);
								swipeUpdate.setText(awayTeam + " goal scored");
								awayGoals.setText("" + teamBGoals);
								
								point = false;
								goal = true;
								
								create = new GetAndPostDataToServer(fixture_id, point, goal, minutes, awayTeam,countPassesToScore);

								success = create.doInBackground();
								
								Toast.makeText(UpdateMatchStats.this, success, Toast.LENGTH_SHORT).show();
								
								
								//reset swipe count to be 0
								countLeftSwipes = 0;
								awayAttack = false;
								homeAttack = false;
								awayTeamStats = false;
								homeTeamStats = false;

								//reset the score pass counter to be 0
								countPassesToScore = 0;
								
								break;
							default:
								break;
								
							}
						}
			}
			
		});
		
		//cancel button
		dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				// TODO Auto-generated method stub
				Toast.makeText(UpdateMatchStats.this, "You cancelled an event", Toast.LENGTH_SHORT).show();
			}
		});
		
		//create a dialog box
		AlertDialog dialogCreate = dialogBuilder.create();
		dialogCreate.show();
		
		//create a new spinner to hold the players information
		Spinner spin1 = (Spinner)v.findViewById(R.id.spinner);
		
		
		//create the string adapter to hold the list of names coming from the database
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, selectEventList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spin1.setAdapter(adapter);
		
		spin1.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//get which player has been attributed to an event
				getEvent = (int)id;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return false;
		
	}

	String name = "";
	
	@SuppressLint("NewApi")
	private boolean selectMatchOption(String teamName,final Boolean inAttack)
	{
		isDialog = true;
		
		
		final ArrayList<String> selectEventList = new ArrayList<String>();

		if(inAttack == true)
		{
			selectEventList.add("Forty Five");
			selectEventList.add("Free Kick");
			selectEventList.add("Penalty");
			selectEventList.add("Yellow Card");
			selectEventList.add("Red Card");
			selectEventList.add("Black Card");
			selectEventList.add("Substitution");
		}
		else
		{
			selectEventList.add("Free Kick");
			selectEventList.add("Yellow Card");
			selectEventList.add("Red Card");
			selectEventList.add("Black Card");
			selectEventList.add("Substitution");
		}
		
		
		//create the alert dialog
		dialogBuilder = new AlertDialog.Builder(this);
		//create the layout that the alert will eventually get
		LayoutInflater inflater = this.getLayoutInflater();
		//create the view for the layout
		View v = inflater.inflate(R.layout.spinner_layout, null);
		//set title, add the view to the alert, set the positive button to have ok and cancel.
		dialogBuilder.setTitle("Select Event");
		dialogBuilder.setView(v);
		dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				if (homeTeamStats) 
				{
					//switch statement based off of what the user selected
					switch(name)
					{
					case "Forty Five":
						fortyFiveA++;
						createAcceptDialogBox(homeTeam);
						break;
					case "Free Kick":
						freeKicksA++;
						//if the team is in a dangerous attack, home team could score a point,goal or it could go wide.
						if(inAttack == true)
						{
							createAcceptDialogBox(homeTeam);
						}
						break;
					case "Penalty":
						penaltyA++;
						//reset the posession stats, goal kick switch
						createAcceptDialogBox(homeTeam);
						break;
					case "Yellow Card":
						yellowACards++;
						break;
					case "Red Card":
						redACards++;
						break;
					case "Black Card":
						blackACards++;
						break;
					case "Substitution":
						subA++;
						break;
					default:
						break;
					}
				}
				else
				{
					//switch statement based off of what the user selected
					//switch statement based off of what the user selected
					switch(name)
					{
					case "Forty Five":
						fortyFiveB++;
						
						createAcceptDialogBox(awayTeam);
						
						break;
					case "Free Kick":
						freeKicksB++;
						//if the team is in a dangerous attack, home team could score a point,goal or it could go wide.
						if(inAttack == true)
						{
							createAcceptDialogBox(awayTeam);
						}
						break;
					case "Penalty":
						penaltyB++;
						//reset the posession stats, goal kick switch
						createAcceptDialogBox(awayTeam);
						break;
					case "Yellow Card":
						yellowBCards++;
						break;
					case "Red Card":
						redBCards++;
						break;
					case "Black Card":
						blackBCards++;
						break;
					case "Substitution":
						subB++;
						break;
					default:
						break;
					}
				}
			}
			
		});
		
		//cancel button
		dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				// TODO Auto-generated method stub
				Toast.makeText(UpdateMatchStats.this, "You cancelled an event", Toast.LENGTH_SHORT).show();
			}
		});
		
		//create a dialog box
		AlertDialog dialogCreate = dialogBuilder.create();
		dialogCreate.show();
		
		//create a new spinner to hold the players information
		Spinner spin1 = (Spinner)v.findViewById(R.id.spinner);
		
		//create the string adapter to hold the list of names coming from the database
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, selectEventList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spin1.setAdapter(adapter);
		
		spin1.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//get which player has been attributed to an event
				getEvent = (int)id;
				
				name = selectEventList.get((int)id);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return false;
		
	}
}
