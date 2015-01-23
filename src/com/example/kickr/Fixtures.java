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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class Fixtures extends Base_Activity 
{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fixtures);
		
		//get the fixtures from the database
		/*
		try {

			String link = "http://ciaranmcmanus.hostei.com/login.php?username="
					+ username + "&password=" + password;

			URL url = new URL(link);
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(link));

			HttpResponse response = client.execute(request);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";

			while ((line = in.readLine()) != null) {
				sb.append(line);

				break;
			}

			// close the buffer
			in.close();

			// get array of results that come back
			JSONArray jsonRoot = new JSONArray(sb.toString());
			// get the first object of the array
			JSONObject rootOBJ = jsonRoot.getJSONObject(0);
			// get the string of the username
			String user = rootOBJ.getString("Username");
			// set the text

		} catch (Exception e) {
			Log.d("Exception: " + e.getMessage(), "String");
		}
		*/
		
		//id for the layout
		LinearLayout il = (LinearLayout) findViewById(R.id.layoutButtons);
		
		//dynamic button functionality
		for(int i = 0;i < 4;i++)
		{
			Button btn = new Button(this);
			btn.setLayoutParams((new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)));
			btn.setId(i);
			btn.setText("Button " + i);
			il.addView(btn);
			
			btn.setOnClickListener(new OnClickListener(){
				
				@Override
				public void onClick(View v)
				{
					Toast.makeText(Fixtures.this,"Button " + v.getId() + " clicked",Toast.LENGTH_SHORT).show();
					
					if(v.getId() == 1)
					{
						startActivity(new Intent(Fixtures.this,AttendeeActivity.class));
					}
					else if(v.getId() == 2)
					{
						startActivity(new Intent(Fixtures.this,TeamOverall.class));
					}
				}
			});
		}
	}
	
}
