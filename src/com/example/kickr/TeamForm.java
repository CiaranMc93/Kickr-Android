package com.example.kickr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TeamForm extends Fragment 
{
	String awayTeam;
	String homeTeam;
	
	TextView text;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.team_form, container, false);
		
		text = (TextView) view.findViewById(R.id.teamName);
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
	
	public void getData(String teamName)
	{
		GetAndPostDataToServer getTeamStats = new GetAndPostDataToServer();
		
		try 
		{
			JSONArray json = new JSONArray(getTeamStats.getTeamStats(teamName).toString());
			
			JSONObject jsonOBJ = json.getJSONObject(0);
			String team = jsonOBJ.get("team_name").toString();
			String loc = jsonOBJ.get("team_location").toString();
			
			Intent i = new Intent(getActivity().getApplicationContext(), TeamOverall.class);
			i.putExtra("Away", team);
			i.putExtra("Away", loc);
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
}
