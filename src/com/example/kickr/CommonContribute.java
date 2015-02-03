package com.example.kickr;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

public class CommonContribute extends Base_Activity {
	
	//fixture string
	String fixture_id;

	public CommonContribute() 
	{
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_contribute);
		StrictMode.enableDefaults();
		
		//a bundle with all the variables from the previous intent
		Bundle extras = getIntent().getExtras();
		if (extras != null) 
		{
			fixture_id = extras.getString("FixtureID");
		}
		
		TextView contribute = (TextView) findViewById(R.id.contribute);
		
		contribute.setText(fixture_id);
	}

}
