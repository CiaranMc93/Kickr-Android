package com.example.kickr;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Menu;
import android.view.MenuItem;

public class TeamFragmentAdapter extends FragmentPagerAdapter {
	
	public TeamFragmentAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		switch(arg0)
		{
		case 0: 
			return new TeamForm();
		case 1:
			return new TeamStats();
		case 2: 
			return new LineUpInformation();
		default:
			break;
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

}
