package com.example.kickr;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TeamplayersActivity extends Base_Activity {
	
	//create edit texts
	EditText goalie;
	EditText player;
	EditText player2;
	EditText player3;
	EditText player4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teamplayers);
		
		//call the method that gets the edit texts
		getPlayerName();
		
	}

	//emethod to use the players name
	private void getPlayerName() 
	{	
		//button 
		Button createPlayers = (Button)findViewById(R.id.createPlayers);
		
		//on click
		createPlayers.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				Toast.makeText(TeamplayersActivity.this, "Players Entered = " + goalie.getText() + " " + player.getText() + " " + player2.getText() + " " + player3.getText() + " " + player4.getText(), Toast.LENGTH_LONG).show();
				
				JSONObject playersData = new JSONObject();
				
				//add the text to a JSON file
				try 
				{
					playersData.put("Goalkeeper", goalie.getText());
					playersData.put("Corner Back", player.getText());
					playersData.put("Midfielder" , player2.getText());
					playersData.put("Right Wing Forward", player3.getText());
					playersData.put("Full Forward" , player4.getText());
				} 
				catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
