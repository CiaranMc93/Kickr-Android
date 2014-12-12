package com.example.kickr;

import java.net.URI;
import java.util.Timer;

import android.support.v7.app.ActionBarActivity;
import android.content.ClipData.Item;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	Item login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setUpMessageButton();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		//return true;
		MenuInflater mif = getMenuInflater();
		mif.inflate(R.menu.main_activity_action,menu);
		return super.onCreateOptionsMenu(menu);
		
	}
	
	private void setUpMessageButton()
	{
		Button messageButton = (Button) findViewById(R.id.button1);
		messageButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				Toast.makeText(MainActivity.this, "You have logged in!", Toast.LENGTH_LONG).show();
				
				startActivity(new Intent(MainActivity.this, AttendeeActivity.class));
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) 
		{
		case R.id.login:
			startActivity(new Intent(MainActivity.this, AttendeeActivity.class));
			return true;
		case R.id.fav:
			startActivity(new Intent(MainActivity.this, AttendeeActivity.class));
			return true;
		case R.id.matches:
			startActivity(new Intent(MainActivity.this, AttendeeActivity.class));
			return true;
		case R.id.settings:
			startActivity(new Intent(MainActivity.this, AttendeeActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	//set up the items menu in the overflow tab
}
