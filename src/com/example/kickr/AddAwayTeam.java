package com.example.kickr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AddAwayTeam extends Fragment  
{

	public AddAwayTeam() 
	{
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
			return inflater.inflate(R.layout.add_team_players,container,false);
			
			
			
	}

}
