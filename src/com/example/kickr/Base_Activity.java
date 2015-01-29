package com.example.kickr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

public class Base_Activity extends Activity {

	

	// Called when the activity is first created.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater mif = getMenuInflater();
		mif.inflate(R.menu.main_activity_action,menu);
		return super.onCreateOptionsMenu(menu);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) 
		{
		case R.id.timer_icon:
			startActivity(new Intent(Base_Activity.this, LiveMatches.class));
			return true;
		case R.id.login:
			startActivity(new Intent(Base_Activity.this, LoginActivity.class));
			return true;
		case R.id.fixtures:
			startActivity(new Intent(Base_Activity.this, Fixtures.class));
			return true;
		case R.id.fav:
			startActivity(new Intent(Base_Activity.this, UpdateMatchStats.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
}
