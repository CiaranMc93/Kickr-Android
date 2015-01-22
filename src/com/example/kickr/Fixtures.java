package com.example.kickr;

import android.os.Bundle;
import android.os.StrictMode;

public class Fixtures extends Base_Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fixtures);
		
		
		StrictMode.enableDefaults();
	}
	
}
