package com.example.kickr;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AttendeeActivity extends ActionBarActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attendee);
		
		setUpMatchButton();
		createTeamButton();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater mif = getMenuInflater();
		mif.inflate(R.menu.attendee,menu);
		return super.onCreateOptionsMenu(menu);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void setUpMatchButton()
	{
		Button nav = (Button) findViewById(R.id.button2);
		nav.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				Toast.makeText(AttendeeActivity.this, "You created a match!", Toast.LENGTH_LONG).show();
				
				startActivity(new Intent(AttendeeActivity.this, MainActivity.class));
				
				finish();
			}
		});
	}
	
	private void createTeamButton()
	{
		Button createPlayer = (Button)findViewById(R.id.createTeam);
		createPlayer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{	
				startActivity(new Intent(AttendeeActivity.this, TeamplayersActivity.class));
				
				finish();
			}
		});
	}
}
