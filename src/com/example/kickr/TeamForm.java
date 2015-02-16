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
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class TeamForm extends Fragment 
{
	String awayTeam;
	String homeTeam;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
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
		return inflater.inflate(R.layout.team_form,container,false);
		
	}
	
	public void getData(String teamName)
	{
		try{
            
			String link = "http://ciaranmcmanus.server2.eu/getTeamStats.php?teamName=" + teamName;
			
			URL url = new URL(link);
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(link));

			HttpResponse response = client.execute(request);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
		

			StringBuffer sb = new StringBuffer("");
			String line = "";
			
			while ((line = in.readLine()) != null) 
			{
				sb.append(line);
	
				break;
			}
			
			//close the buffer
			in.close();
			//get array of results that come back
			//JSONArray jsonRoot = new JSONArray(sb.toString());
			//get the first object of the array
			//JSONObject rootOBJ = jsonRoot.getJSONObject(0);
			
			Log.e("JSON", sb.toString());

			
		}
		catch (Exception e) 
		{
			Log.e("JSON", e.toString());
		}
			
	}
	
}
