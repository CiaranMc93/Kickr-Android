package com.example.kickr;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AttendeeActivity extends Base_Activity{
	
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
				Toast.makeText(AttendeeActivity.this,"15 Secs",Toast.LENGTH_LONG).show();
				
			}
			
			customHandler.postDelayed(this, 0);	
		}

	};
	
}
