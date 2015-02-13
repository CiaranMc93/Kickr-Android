package com.example.kickr;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.Toast;

public class createMatchEvent 
{

	int match;
	int player;
	boolean p;
	boolean g;
	int minute;
	String url;
	
	public createMatchEvent(String matchID, String playerID, boolean point, boolean goal,int mins) 
	{
		// TODO Auto-generated constructor stub
		match = Integer.parseInt(matchID);
		player = Integer.parseInt(playerID);
		p = point;
		g = goal;
		minute = mins;
		
	}
	
	public createMatchEvent(String matchID, String playerID, int point, int goal,String url) 
	{
		// TODO Auto-generated constructor stub
	}
	
	public String createEvent()
	{
		//create the json object that is going to update the database with its contents
		JSONObject obj = new JSONObject();
		try 
		{
			obj.put("MatchID", match);
			obj.put("PlayerID", player);
			obj.put("Point", p);
			obj.put("Goal", g);
			obj.put("Minutes", minute);
		} 
		catch (JSONException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JSONArray json = new JSONArray();

		try 
		{
			json.put(0, obj);
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.e("JSON", json.toString());

		HttpClient client = new DefaultHttpClient();
		HttpResponse response;

		try 
		{
			HttpPost post = new HttpPost("http://ciaranmcmanus.server2.eu/createEvent.php");
			List<NameValuePair> nVP = new ArrayList<NameValuePair>(2);

			nVP.add(new BasicNameValuePair("json", json.toString()));

			post.setEntity(new UrlEncodedFormEntity(nVP));
			response = client.execute(post);

			if (response != null) {

				InputStream in = response.getEntity().getContent();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				in.close();
				
				String note = sb.toString();
				
				return note;
			}

		} 
		catch (Exception e) 
		{
			Log.e("log tag", "Error in Http connection" + e.toString());
		}
		
		return "";

	}
}
