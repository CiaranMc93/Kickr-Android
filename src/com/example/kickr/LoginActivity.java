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

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Base_Activity 
{
	//edit text variables
	EditText username;
	EditText password;
	
	Boolean loginTrue = false;
	
	//get the information of the fixture to be contributed to and send on
	String fixture_id;
	String homeTeam;
	String awayTeam;
	String referee;
	String competition;
	String venue;
	
	private boolean isSignedIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		StrictMode.enableDefaults();
		
		//get the editText ids
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
	
		// login button pressed
		Button button = (Button) findViewById(R.id.loginButton);
		
		//get the fixture id from the previous activity
		//a bundle with all the variables from the previous intent
		Bundle extras = getIntent().getExtras();
		if (extras != null) 
		{
			//get the values from the previous activity
			fixture_id = extras.getString("FixtureID");
		    homeTeam = extras.getString("Home");
		    awayTeam = extras.getString("Away");
		    venue = extras.getString("Venue");
		    referee = extras.getString("Referee");
		    competition = extras.getString("Competition");
		}
		
		
		
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click

				// get the text from the edit texts
				String usernameField = username.getText().toString();
				String passwordField = password.getText().toString();

				// login with this information
				login(usernameField, passwordField);
				
			}
		});
		
		Button common = (Button) findViewById(R.id.common);
		common.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				Intent i = new Intent(getApplicationContext(), CommonContribute.class);
				i.putExtra("FixtureID", fixture_id);
				startActivity(i);
			}
		});
		
		
	}
	
	public void login(String username, String password)
	{
		LinearLayout il = (LinearLayout) findViewById(R.id.loginSucceed);
		
		try{
            
			String link = "http://ciaranmcmanus.server2.eu/login.php?username="+ username + "&password=" + password;
			
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
			JSONArray jsonRoot = new JSONArray(sb.toString());
			//get the first object of the array
			JSONObject rootOBJ = jsonRoot.getJSONObject(0);

			isSignedIn = true;
			
			
			Intent i = new Intent(getApplicationContext(), UpdateMatchStats.class);
			i.putExtra("FixtureID", fixture_id);
			i.putExtra("isSignedIn", isSignedIn);
			i.putExtra("Home", homeTeam);
			i.putExtra("Away", awayTeam);
			i.putExtra("Venue", venue);
			i.putExtra("Competition", competition);
			i.putExtra("Referee", referee);
			startActivity(i);
			
		}
		catch (Exception e) 
		{
			String loginFail = "Unsuccessful Login, Register?";
			// set up the dynamic button
			
			if(loginTrue == false)
			{
				TextView btn = new TextView(this);
				btn.setLayoutParams((new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)));
				btn.setText(loginFail);
				il.addView(btn);
				
				btn.setOnClickListener(new View.OnClickListener() 
				{
					public void onClick(View v) 
					{
						
					}
				});
			}
			
		}
	}
}
