package com.example.kickr;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateMatchStats extends Base_Activity implements OnDoubleTapListener, OnGestureListener, SensorEventListener {

	private GestureDetectorCompat mDetector;
	private static int TAKE_PICTURE = 1;
	private Uri imageUri;
	
	Boolean sendTrue = false;
	
	Sensor accelerate;
	SensorManager sm;
	
	JSONArray updateMatch;
	JSONObject matchStats;
	
	//image view of the pitch
	ImageView pitch1;
	
	//text views to hold the team names
	TextView homeTeam1;
	TextView awayTeam1;
	
	int counter;
	
	JSONObject createMatch = new JSONObject();
	
	//get rid of this eventually
	TextView homePoints;
	TextView awayPoints;
	
	String fixtureResult;
	
	//count swipe functionality
	int countRightSwipes;
	int countLeftSwipes;
	
	//make boolean for home and away team stats
	Boolean homeTeamStats;
	Boolean awayTeamStats;
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
	
	//count variable
	int count = 0;
	
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
	
	//create variables in order to hold the information of each team in each match
	//home team
	int teamAPoints;
	int teamAGoals;
	int yellowACards;
	int redACards;
	int blackACards;
	
	//away team
	int teamBPoints;
	int teamBGoals;
	int yellowBCards;
	int redBCards;
	int blackBCards;
	
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
		
		homeAttack = false;
		awayAttack = false;
		
		counter = 0;
		
		//default home team to have posession
		homeTeamStats = true;
		
		swipeUpdate = (Button) findViewById(R.id.updateUser);
		
		//textviews used to set the home and away team names
		homeTeam1 = (TextView) findViewById(R.id.team1);
		awayTeam1 = (TextView) findViewById(R.id.team2);
		
		//get the id of the image view
		pitch1 = (ImageView) findViewById(R.id.pitchUpdate);
		
		//textviews to set the teams points
		homePoints = (TextView) findViewById(R.id.homePoints);
		awayPoints = (TextView) findViewById(R.id.awayPoints);
		
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
		    //awayTeam1.setText(awayTeam);
		    
		}
		
		createMatch(fixture_id);
		
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
			
			if(secs == 45)
			{
				sendTrue = false;
			}
			
			if(sendTrue == false && secs == 30)
			{
				sendData(secs);
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
				pitch1.setImageResource(R.drawable.home_pos);
				swipeUpdate.setText(homeTeam + " in possession");
				awayTeamStats = false;
				homeTeamStats = true;
				//reset the attack bool
				homeAttack = false;
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
				pitch1.setImageResource(R.drawable.away_goal_kick);
				swipeUpdate.setText(homeTeam + " scored");
				teamAPoints++;
				homePoints.setText(" " + teamAPoints);
				//away team gets posession because home team just scored.
				awayTeamStats = true;
				homeTeamStats = false;
				swipeUpdate.setText("Goal kick to " + awayTeam);
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
				swipeUpdate.setText(awayTeam + " in possession");
				pitch1.setImageResource(R.drawable.away_pos);
				awayTeamStats = true;
				homeTeamStats = false;
				//reset the attack bools also
				homeAttack = false;
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
				pitch1.setImageResource(R.drawable.home_goal_kick);
				swipeUpdate.setText(awayTeam + " scored");
				teamBPoints++;
				awayPoints.setText(" " + teamBPoints);
				awayTeamStats = false;
				homeTeamStats = true;
				swipeUpdate.setText("Goal kick to " + homeTeam);			
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
			
		} 
		else 
		{
			//update home and away goals where the condition is true
			if (homeAttack == true) 
			{
				teamAGoals++;
				pitch1.setImageResource(R.drawable.away_goal_kick);
				swipeUpdate.setText(awayTeam + " goal scored");
			}

			if (awayAttack == true) 
			{
				teamBGoals++;
				pitch1.setImageResource(R.drawable.home_goal_kick);
				swipeUpdate.setText(awayTeam + "goal scored");
			}
		}
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
	public void createMatch(String fixture_id) 
	{
		JSONArray json = new JSONArray();
		try 
		{
			createMatch.put("FixtureID", fixture_id);

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
			
			nVP.add(new BasicNameValuePair("json", json.toString()));


			post.setEntity(new UrlEncodedFormEntity(nVP));
			response = client.execute(post);
			
			if (response != null) 
			{
				
				InputStream in = response.getEntity().getContent();

				BufferedReader reader = new BufferedReader(new InputStreamReader(in, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) 
				{
					sb.append(line + "\n");
				}
				in.close();


				fixtureResult = sb.toString();
				
				awayTeam1.setText(fixtureResult);
			}

		} catch (Exception e) {
			Log.e("log tag", "Error in Http connection" + e.toString());
		}
	}
	
	public void sendData(int secs)
	{
		
		JSONObject obj = new JSONObject();
		try 
		{
			obj.put("teamAPoints", teamAPoints);
			obj.put("teamBPoints", teamBPoints);
			obj.put("teamAGoals", teamAGoals);
			obj.put("teamBGoals", teamBGoals);
			obj.put("homehandPasses", homehandPasses);
			obj.put("awayhandPasses", awayhandPasses);
		} 
		catch (JSONException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JSONArray json = new JSONArray();

		try 
		{
			json.put(0, obj);
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.e("JSON", json.toString());
		
		
		
		/*
		try 
		{
			createMatch.put("FixtureID", fixture_id);

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
			HttpPost post = new HttpPost("http://ciaranmcmanus.server2.eu/insertInto.php");
			List<NameValuePair> nVP = new ArrayList<NameValuePair>(2);
			
			nVP.add(new BasicNameValuePair("json", json.toString()));


			post.setEntity(new UrlEncodedFormEntity(nVP));
			response = client.execute(post);
			
			if (response != null) 
			{
				
				InputStream in = response.getEntity().getContent();

				BufferedReader reader = new BufferedReader(new InputStreamReader(in, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) 
				{
					sb.append(line + "\n");
				}
				in.close();


				fixtureResult = sb.toString();		
			}

		} catch (Exception e) {
			Log.e("log tag", "Error in Http connection" + e.toString());
		}
		
		*/
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
		
		if(event.values[0] > xVal && event.values[1] > yVal && counter == 0)
		{
			counter++;
			//takePhoto();
		}
		
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}
