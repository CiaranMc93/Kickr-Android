package com.example.kickr;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
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
	
	//count swipe functionality
	int countRightSwipes;
	int countLeftSwipes;
	
	//make boolean for home and away team stats
	Boolean homeTeamStats;
	Boolean awayTeamStats;
	
	//fixture string
	String fixture_id;
	
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
	
	private long startTimePos = 0L;
	//handle the time
	private Handler posessionHandler = new Handler();
	//time logic
	long timeInMillisecondsPos = 0L;
	long timeSwapBuffPos = 0L;
	long updatedTimePos = 0L;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_match_stats);
		
		
		timerValue = (TextView) findViewById(R.id.timerValue);
		swipeUpdate = (Button) findViewById(R.id.updateUser);
		
		//a bundle with all the variables from the previous intent
		Bundle extras = getIntent().getExtras();
		if (extras != null) 
		{
			fixture_id = extras.getString("FixtureID");
		}
		
		TextView fixture = (TextView) findViewById(R.id.fixtureId);
		fixture.setText(fixture_id);
		
		//change the image
		setUpMessageButton();
		
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
	
	//functionality to control activities based on gesture count
	private void setUpMessageButton()
	{
		
		//button functionality 
		final Button playButton = (Button) findViewById(R.id.startMatch);
		playButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{			
				startTime = SystemClock.uptimeMillis();
				playButton.setVisibility(View.INVISIBLE);
				customHandler.postDelayed(updateTimerThread, 0);
				startTimePos = SystemClock.uptimeMillis();
				posessionHandler.postDelayed(updateMatchPosession, 0);
			}
		});
		
		pauseButton = (Button) findViewById(R.id.pauseButton);

		pauseButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) 
			{

				timeSwapBuff += timeInMilliseconds;
				customHandler.removeCallbacks(updateTimerThread);

			}
		});
	}
	
	//change the text for each team home and away
	public void setText(String team1, String team2)
	{
		//change the text to the specified name of each team.
		TextView homeTeam = (TextView) findViewById(R.id.team1);
	    homeTeam.setText(team1);
	    
	    TextView awayTeam = (TextView) findViewById(R.id.team2);
	    awayTeam.setText(team2);
	    
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
			int milliseconds = (int) (updatedTime % 1000);
			//update the display string
			timerValue.setText("" + mins + ":"
					+ String.format("%02d", secs) + ":"
					+ String.format("%02d", milliseconds));

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
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.mDetector.onTouchEvent(event);
		// Be sure to call the superclass implementation
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent event) {
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
	
	int homehandPasses = 0;
	int awayhandPasses = 0;

	@Override
	public boolean onSingleTapUp(MotionEvent event) 
	{
		if(homeTeamStats)
		{
			homehandPasses++;
			if(homehandPasses == 5)
			{
				Toast.makeText(UpdateMatchStats.this,"Hand Passes: " + homehandPasses,Toast.LENGTH_SHORT).show();
			
			}
			
			if(homehandPasses == 15)
			{
				startTime = 0L;
				startTime = SystemClock.uptimeMillis();
                customHandler.removeCallbacks(updateTimerThread);
                customHandler.postDelayed(updateTimerThread, 0);
				Log.e("Passes","Number: " + homehandPasses);
			}
		}
		
		
		if(awayTeamStats)
		{
			awayhandPasses++;
			if(awayhandPasses == 5)
			{
				Toast.makeText(UpdateMatchStats.this,"Hand Passes: " + awayhandPasses,Toast.LENGTH_SHORT).show();
				awayhandPasses = 0;
			
			}
		}
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent event) {
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
	
}
