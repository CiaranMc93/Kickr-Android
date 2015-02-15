package com.example.kickr;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CreateTeamAdapter extends FragmentPagerAdapter {

	public CreateTeamAdapter(FragmentManager fm) 
	{
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		switch(arg0)
		{
		case 0: 
			return new AddHomeTeam();
		case 1:
			return new AddAwayTeam();
		default:
			break;
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

}




