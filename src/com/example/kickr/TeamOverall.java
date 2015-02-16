package com.example.kickr;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class TeamOverall extends FragmentActivity implements ActionBar.TabListener {

	ActionBar actionbar;
	ViewPager viewpager;
	TeamFragmentAdapter ft;
	
	String teamName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_overall);
		
		//a bundle with all the variables from the previous intent
		Bundle extras = getIntent().getExtras();
		if (extras != null) 
		{
			if(extras.getString("Away") != null)
			{
				teamName = extras.getString("Away");
			}
			else
			{
				//get the values from the previous activity
				teamName = extras.getString("Home");
			}
			
		}
		
		Intent i = new Intent(getApplicationContext(), TeamForm.class);
		i.putExtra("TeamName",teamName);
		
		viewpager = (ViewPager) findViewById(R.id.pager);
		ft = new TeamFragmentAdapter(getSupportFragmentManager());
		
		actionbar = getActionBar();
		
		viewpager.setAdapter(ft);
		
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionbar.addTab(actionbar.newTab().setText("Team Form").setTabListener(this));
		actionbar.addTab(actionbar.newTab().setText("Team Statistics").setTabListener(this));
		actionbar.addTab(actionbar.newTab().setText("Line-Up").setTabListener(this));
		
		viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() 
		{
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				actionbar.setSelectedNavigationItem(arg0);
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewpager.setCurrentItem(tab.getPosition());
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
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
			startActivity(new Intent(TeamOverall.this, LiveMatches.class));
			return true;
		case R.id.fixtures:
			startActivity(new Intent(TeamOverall.this, Fixtures.class));
			return true;
		case R.id.fav:
			startActivity(new Intent(TeamOverall.this, UpdateMatchStats.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
