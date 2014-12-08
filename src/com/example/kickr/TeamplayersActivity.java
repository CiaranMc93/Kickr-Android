package com.example.kickr;

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

public class TeamplayersActivity extends ActionBarActivity {
	
	//create edit texts
	EditText goalie;
	EditText player;
	EditText player2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teamplayers);
		
		getPlayerName();
		
	}

	private void getPlayerName() 
	{	
		Button createPlayers = (Button)findViewById(R.id.createPlayers);
		goalie = (EditText)findViewById(R.id.player1);
		player = (EditText)findViewById(R.id.player2);
		player2 = (EditText)findViewById(R.id.player3);
		
		createPlayers.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				Toast.makeText(TeamplayersActivity.this, "Players Entered = " + goalie.getText() + " " + player.getText() + " " + player2.getText(), Toast.LENGTH_LONG).show();
				
				JSONObject playersData = new JSONObject();
				
				//add the text to a JSON file
				try 
				{
					playersData.put("Goalkeeper", goalie.getText());
					playersData.put("Corner Back", player.getText());
					playersData.put("Midfielder" , player2.getText());
				} 
				catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println(playersData);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teamplayers, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
