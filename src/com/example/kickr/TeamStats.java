package com.example.kickr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TeamStats extends Fragment 
{

	String awayTeam;
	String homeTeam;
	
	TextView pos;
	TextView point;
	TextView averagePoints;
	TextView averageGoals;
	TextView red;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.team_stats_layout, container, false);
		
		pos = (TextView) view.findViewById(R.id.posession);
		point = (TextView) view.findViewById(R.id.point);
		averagePoints = (TextView) view.findViewById(R.id.red);
		averageGoals = (TextView) view.findViewById(R.id.yellow);
		red = (TextView) view.findViewById(R.id.goal);
		
		
		//text = (TextView) view.findViewById(R.id.teamName);
		Bundle b = getActivity().getIntent().getExtras();
		
		//get either the away team or the home team based off of which is passed in
		if(b.getString("Away") != null)
		{
			awayTeam = b.getString("Away");
			
			getData(awayTeam);
		}
        
		if(b.getString("Home") != null)
		{
	        homeTeam = b.getString("Home");
	        
	        getData(homeTeam);
		}
        
        //Log.e("String",team);
		return view;
		
	}
	
	public void getData(String name)
	{
		GetAndPostDataToServer getTeamStats = new GetAndPostDataToServer();
		
		try 
		{
			JSONArray json = new JSONArray(getTeamStats.getTeamStats(name).toString());
			
			JSONObject jsonOBJ = json.getJSONObject(0);
			int points = Integer.parseInt(jsonOBJ.get("team_points").toString());
			int goals = Integer.parseInt(jsonOBJ.get("team_goals").toString());
			int yellows = Integer.parseInt(jsonOBJ.get("team_yellows").toString());
			int reds = Integer.parseInt(jsonOBJ.get("team_reds").toString());
			int matches = Integer.parseInt(jsonOBJ.get("matchesPlayed").toString());
			
			float avgPoints = points / matches;
			float avgGoals = goals / matches;
			
			pos.setText("Total Points: " + points);
			point.setText("Total Goals: " + goals);
			averagePoints.setText("Average Points per game: " + avgPoints);
			averageGoals.setText("Average Goals per game: " + avgGoals);
			red.setText("Total Red Cards: " + reds);
			
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
