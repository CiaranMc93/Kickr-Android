package com.example.kickr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class AddHomeTeam extends Fragment 
{
	
	private View myFragmentView;
	
	int playerNums = 1;
	
	JSONArray playerList;
	JSONObject player;
	
	Button addPlayer;
	EditText playerPos;
	
	String pos;
	String player_name;

	public AddHomeTeam() 
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
			
			myFragmentView = inflater.inflate(R.layout.add_team_players, container, false);
			
		    playerPos = (EditText) myFragmentView.findViewById(R.id.playerPos);
		    
		    //player_name = playerPos.getText().toString();
		    
		    playerPos.setHint("Hello");
		    
		    addPlayer = (Button) myFragmentView.findViewById(R.id.addPlayer); 
		    
		    addPlayer.setOnClickListener(new OnClickListener() 
		    {
				@Override
				public void onClick(View v) 
				{
					//method
					createPlayers(playerPos.getText().toString());
					
				}

			}); 
		    

		    return myFragmentView;	
	}
	
	private void createPlayers(String players) 
	{
		String play = players;
		
		try 
	    {
			player.put("Player", play);
			
			playerList.put(0,player.toString());
			
			Log.e("JSON",player_name);
		} 
	    catch (Exception e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
