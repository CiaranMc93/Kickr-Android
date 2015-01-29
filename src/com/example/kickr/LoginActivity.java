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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Base_Activity 
{
	//edit text variables
	EditText username;
	EditText password;
	
	Boolean loginTrue = false;

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
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click

				//startActivity(new Intent(LoginActivity.this,AttendeeActivity.class));

				// get the text from the edit texts
				String usernameField = username.getText().toString();
				String passwordField = password.getText().toString();

				// login with this information
				login(usernameField, passwordField);
				
			}
		});
	}
	
	public void login(String username, String password)
	{
		LinearLayout il = (LinearLayout) findViewById(R.id.loginSucceed);
		
		try{
            
			String link = "http://ciaranmcmanus.hostei.com/login.php?username="+ username + "&password=" + password;
			
			URL url = new URL(link);
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(link));

			Log.e("Tag","Gets Here!");
			HttpResponse response = client.execute(request);
			Log.e("Tag","Gets Here!!!");

			BufferedReader in = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			
			Log.e("Tag","Gets Here!!!!!!");

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
			
			//add username to the variable
			String user = rootOBJ.getString("Username");

			String loginSuccess = "Logged in Successfully";
	
			if(sb.toString().length() > 0)
			{
				loginTrue = true;
			}
			
			if(loginTrue = true)
			{
				TextView btn1 = new TextView(this);
				btn1.setLayoutParams((new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)));
				btn1.setText(loginSuccess);
				il.addView(btn1);
				loginTrue = false;
			}

			// start the attendee acitivity intent and send in the username
			// pass the username to the match information page
			Intent i = new Intent(getApplicationContext(), EnterMatchInfo.class);
			i.putExtra("Username", user);
			startActivity(i);
			Log.e("Tag","Errorrrrrr");
			
		}
		catch (Exception e) 
		{
			Log.e("Tag", e.getMessage());
			String loginFail = "Unsuccessful Login";
			// set up the dynamic button
			
			if(loginTrue == false)
			{
				TextView btn = new TextView(this);
				btn.setLayoutParams((new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)));
				btn.setText(loginFail);
				il.addView(btn);
			}
			
		}
	}
	
}
