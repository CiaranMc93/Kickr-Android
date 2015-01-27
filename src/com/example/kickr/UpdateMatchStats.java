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
		setContentView(R.layout.attendee);
		
		
		timerValue = (TextView) findViewById(R.id.timerValue);
		
		//change the image
		setUpMessageButton();
		
		//Set strings to be the home team and the away team
		String team1 = "O'Dempseys";
		String team2 = "Mountmellick";
		
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
		Button messageButton = (Button) findViewById(R.id.butt);
		messageButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{			
				//get the id of the image
				ImageView imageView = (ImageView) findViewById(R.id.swipeCount);
				
				//count the number of swipes 
				count++;
				
				//counts the number of swipes in play
				if(count == 1)
				{
					imageView.setImageResource(R.drawable.no1);
				}
				else if(count == 2)
				{
					imageView.setImageResource(R.drawable.no2);
				}
				else if(count == 3)
				{
					imageView.setImageResource(R.drawable.no1);
				}
				else if(count == 4)
				{
					imageView.setImageResource(R.drawable.no0);
					count = 0;
				}
			}
		});
		
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
			
			if(secs == 15)
			{
				Toast.makeText(UpdateMatchStats.this,"15 Secs",Toast.LENGTH_LONG).show();
				
			}
			
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
			String plus = "right";
			Log.d(DEBUG_TAG, "onFling: " + plus);
		}
		else if(velocityX < 0 && velocityY < 0)
		{
			String minus = "left";
			Log.d(DEBUG_TAG, "onFling: " + minus);
		}
		//Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
		return true;
	}

	@Override
	public void onLongPress(MotionEvent event) {
		Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
	}

	@Override
	public void onShowPress(MotionEvent event) {
		//Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
	}

	@Override
	public boolean onSingleTapUp(MotionEvent event) {
		//Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
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
