package com.example.kickr;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Base_Activity extends Activity 
{
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
			startActivity(new Intent(Base_Activity.this, Favourites.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
