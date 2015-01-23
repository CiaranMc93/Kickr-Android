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
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Base_Activity 
{
	//edit text variables
	EditText username;
	EditText password;
	
	//textview results
	TextView result;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		
		StrictMode.enableDefaults();
		
		//get the editText ids
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		
		result = (TextView) findViewById(R.id.result);
	
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
		try{
            
			String link = "http://ciaranmcmanus.hostei.com/login.php?username="+ username + "&password=" + password;
			
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
			//get the string of the username
			String user = rootOBJ.getString("Username");
			//set the text
			if(sb.length() > 0)
			{
				result.setText(user);
			}
			else
			{
				result.setText("Incorrect Login");
			}

      }catch(Exception e)
		{
         Log.d("Exception: " + e.getMessage(),"String");
      }
	}
	
}
