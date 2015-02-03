package com.example.kickr;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

public class CommonContribute extends Base_Activity 
{
	
	//fixture string
	String fixture_id;
	
	TextView longitude;
	TextView latitude;

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
		
		//long and lat coordinates changes
		longitude = (TextView) findViewById(R.id.textView1);
		latitude = (TextView) findViewById(R.id.textView2);
		
		LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		LocationListener listener = new myLocationListener();
		
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
		contribute.setText(fixture_id);
}

	class myLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) 
		{
			Log.e("tag", "nope");
			
			if (location != null) {
				double dlong = location.getLongitude();
				double dlat = location.getLatitude();
				
				Log.e("tag", "dlong");

				longitude.setText(Double.toString(dlong));
				latitude.setText(Double.toString(dlat));
			}

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

	}
}
