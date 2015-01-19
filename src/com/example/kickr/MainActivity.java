package com.example.kickr;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Timer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.content.ClipData.Item;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	Item login;
	TextView resultView;
	
	//login variable
	EditText userName; 
	EditText passWord;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setUpMessageButton();
		
		StrictMode.enableDefaults();
		
		resultView = (TextView)findViewById(R.id.result);
		
		getData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		//return true;
		MenuInflater mif = getMenuInflater();
		mif.inflate(R.menu.main_activity_action,menu);
		return super.onCreateOptionsMenu(menu);
		
	}
	
	private void setUpMessageButton()
	{
		Button messageButton = (Button) findViewById(R.id.button1);
		messageButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				//String to holf username and password
				String loginUser;
				String loginPass;
				
				//get the login ids
				userName = (EditText)findViewById(R.id.username);
				passWord = (EditText)findViewById(R.id.password);
				
				//get the strings of the edit texts and display them.
				loginUser = userName.getText().toString();
				loginPass = passWord.getText().toString();
				
				
				Toast.makeText(MainActivity.this, "You have logged in!", Toast.LENGTH_LONG).show();
				Toast.makeText(MainActivity.this,"Username : " + loginUser + " Password : " + loginPass, Toast.LENGTH_LONG).show();
				
				//startActivity(new Intent(MainActivity.this, AttendeeActivity.class));
				startActivity(new Intent(MainActivity.this, TeamOverall.class));
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) 
		{
		case R.id.login:
			startActivity(new Intent(MainActivity.this, AttendeeActivity.class));
			return true;
		case R.id.fav:
			startActivity(new Intent(MainActivity.this, TeamOverall.class));
			return true;
		case R.id.matches:
			startActivity(new Intent(MainActivity.this, TeamplayersActivity.class));
			return true;
		case R.id.settings:
			startActivity(new Intent(MainActivity.this, Statistics.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void getData()
	{
		String loginResult = "";
		
		InputStream input = null;
		
		//try catch hhtp client request
		try
		{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://ciaranmcmanus.hostei.com/getAllCustomers.php");
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			input = entity.getContent();
			
		}
		catch(Exception e)
		{
			Log.e("log tag","Error in Http connection" + e.toString());
			resultView.setText("Couldnt connect to database");
		}
		//convert response to string
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(input,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
			input.close();
			
			loginResult = sb.toString();
		}
		catch(Exception e)
		{
			Log.e("log tag","Error converting result" + e.toString());
		}
		
		//parse the JSON data that returns information needed
		try{
			String s = "";
			JSONArray jArray = new JSONArray(loginResult);
			
			//loop through the array
			for(int i=0; i<jArray.length(); i++)
			{
				JSONObject json = jArray.getJSONObject(i);
				s = s + "Name : " + json.getString("Username") + "\nPassword : " + json.getString("Password") + "\n";
				
				
			}
			
			resultView.setText(s);
			
		}
		catch(Exception e)
		{
			//handle the exception
			Log.e("Log tag", "Error parsing data " + e.toString());
		}
	}
}
	//set up the items menu in the overflow tab
