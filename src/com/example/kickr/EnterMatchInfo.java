package com.example.kickr;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

public class EnterMatchInfo extends Base_Activity {

	//username
	String username = "";
	//display the username
	TextView user;
	
	public EnterMatchInfo() {

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.match_create);
		StrictMode.enableDefaults();
		
		user = (TextView) findViewById(R.id.username);
		
		getInfo();
	}
	
	public void getInfo()
	{
		Bundle extras = getIntent().getExtras();
		if (extras != null) 
		{
			//get the values from the previous activity
		    username = extras.getString("Username");
		    
		    //set the string
		    user.setText(username);
		}
	}
}
