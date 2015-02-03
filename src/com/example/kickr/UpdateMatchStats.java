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
					+ String.format("%03d", milliseconds));

			customHandler.postDelayed(this, 0);	
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
			}
			else if(countRightSwipes == 2)
			{
				swipeUpdate.setText(team1 + " is attacking");
			}
		}
		else if(velocityX < 0 && velocityY < 0)
		{
			countRightSwipes = 0;
			
			countLeftSwipes++;
			
			if(countLeftSwipes == 1)
			{
				swipeUpdate.setText(team2 + " in possession");
			}
			else if(countLeftSwipes ==2)
			{
				swipeUpdate.setText(team2 + " is attacking");
			}
		}
		//Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
		return true;
	}

	@Override
	public void onLongPress(MotionEvent event) {
		Toast.makeText(UpdateMatchStats.this,"Handle Subs",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onShowPress(MotionEvent event) {
		//Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
	}
	
	int handPasses = 0;

	@Override
	public boolean onSingleTapUp(MotionEvent event) 
	{
		
		handPasses++;
		if(handPasses == 20)
		{
			Toast.makeText(UpdateMatchStats.this,"Hand Passes: " + handPasses,Toast.LENGTH_SHORT).show();
			
		}
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent event) {
		Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
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
