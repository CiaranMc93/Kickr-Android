package com.example.kickr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GetAndPostDataToServer extends AsyncTask<Void, Void, String>
{

	String team;
	
	int match;
	int player;
	
	boolean p;
	boolean g;
	
	int minute;
	String url;
	
	boolean isMatchOver = false;
	
	public static boolean hasActiveInternetConnection() 
	{
		try 
		{
			HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
			urlc.setRequestProperty("User-Agent", "Test");
			urlc.setRequestProperty("Connection", "close");
			urlc.setConnectTimeout(1500);
			urlc.connect();
			return (urlc.getResponseCode() == 200);
		} 
		catch (IOException e) 
		{
			Log.e("Error checking internet connection", e.toString());
		}

	    return false;
	}
	
	public GetAndPostDataToServer(String matchID, String playerID, boolean point, boolean goal,int mins,String teamName) 
	{
		// TODO Auto-generated constructor stub
		match = Integer.parseInt(matchID);
		player = Integer.parseInt(playerID);
		p = point;
		g = goal;
		minute = mins;
		team = teamName;
		
	}
	
	public GetAndPostDataToServer(String matchID, String playerID, boolean point, boolean goal,int mins,boolean isOver) 
	{
		// TODO Auto-generated constructor stub
		match = Integer.parseInt(matchID);
		player = Integer.parseInt(playerID);
		p = point;
		g = goal;
		minute = mins;
		isMatchOver = isOver;
		
	}
	
	public GetAndPostDataToServer(String matchID, String playerID, int point, int goal,String url) 
	{
		// TODO Auto-generated constructor stub
	}
	
	public GetAndPostDataToServer() 
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
			obj.put("TeamName", team);
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
	
	public String sendJson(JSONObject obj,String php)
	{
		JSONArray json = new JSONArray();

		try {
			json.put(0, obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpClient client = new DefaultHttpClient();
		HttpResponse response;

		try {
			HttpPost post = new HttpPost(
					"http://ciaranmcmanus.server2.eu/" + php);
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

		} catch (Exception e) {
			Log.e("log tag", "Error in Http connection" + e.toString());
		}

		return "";
	}
	
	//method used for getting information from specific php files using a string passed in from other classes
	public String getData(String http)
	{
		String fixtureResult = "";
		
		InputStream input = null;
		
		//try catch hhtp client request
		try
		{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://ciaranmcmanus.server2.eu/" + http);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			input = entity.getContent();
			
		}
		catch(Exception e)
		{
			Log.e("log tag","Error in Http connection" + e.toString());
		}
		//convert response to string
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(input,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
			input.close();
			
			fixtureResult = sb.toString();
			
			return fixtureResult;
		}
		catch(Exception e)
		{
			Log.e("log tag","Error converting result" + e.toString());
		}
		
		return "";
	}
	
	
	public String getTeamStats(String teamName)
	{
		
		try {

			String link = "http://ciaranmcmanus.server2.eu/getTeamStats.php?teamName="
					+ teamName;
			
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
			
			return sb.toString();

		} 
		catch (Exception e) 
		{
			Log.e("JSON", e.toString());
		}
		
		String v = "";
		return v;

	}

	protected String doInBackground(String params) 
	{
		// TODO Auto-generated method stub
		if(hasActiveInternetConnection())
		{
			return getData(params).toString();
		}
		else
		{
			return "No Connection";
		}
		
	}
	
	protected String doInBackground(JSONObject params,String php) 
	{
		if (hasActiveInternetConnection()) 
		{
			// TODO Auto-generated method stub
			String json = sendJson(params, php).toString();
			return json;
		} 
		else 
		{
			return "No Connection";
		}
	}

	@Override
	protected String doInBackground(Void... params) 
	{
		if(hasActiveInternetConnection())
		{
			// TODO Auto-generated method stub
			return createEvent().toString();
		} 
		else 
		{
			return "No Connection";
		}
		
	}
}
