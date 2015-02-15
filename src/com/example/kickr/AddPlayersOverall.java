package com.example.kickr;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class AddPlayersOverall extends FragmentActivity implements ActionBar.TabListener 
{

	ActionBar actionbar;
	ViewPager viewpager;
	CreateTeamAdapter ft;
	
	String fixture_id;
	
	String homeTeam;
	String awayTeam;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_overall);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) 
		{
		    homeTeam = extras.getString("Home");
		    awayTeam = extras.getString("Away");    
		}
		
		viewpager = (ViewPager) findViewById(R.id.pager);
		ft = new CreateTeamAdapter(getSupportFragmentManager());
		
		actionbar = getActionBar();
		
		viewpager.setAdapter(ft);
		
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionbar.addTab(actionbar.newTab().setText(homeTeam).setTabListener(this));
		actionbar.addTab(actionbar.newTab().setText(awayTeam).setTabListener(this));
		
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
	
	public AddPlayersOverall() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

}
