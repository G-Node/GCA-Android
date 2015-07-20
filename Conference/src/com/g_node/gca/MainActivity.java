/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com> (2014 Version)
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.g_node.gca.abstracts.Abstracts;
import com.g_node.gca.abstracts.DatabaseHelper;
import com.g_node.gca.abstracts.FavoriteAbstracts;
import com.g_node.gca.map.MapActivity;
import com.g_node.gca.newsroom.NewsRoomActivity;
import com.g_node.gca.schedule.ScheduleMainActivity;
import com.g_node.gcaa.R;

public class MainActivity extends Activity {
	
	DatabaseHelper dbHelpeer = new DatabaseHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getActionBar().setTitle(R.string.app_name);
		//getActionBar().setIcon(getResources().getDrawable(R.drawable.icon_brain));
		
		Button news = (Button)findViewById(R.id.btn_news);
		news.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent newsIntent = new Intent(MainActivity.this, NewsRoomActivity.class);
				startActivity(newsIntent);
			}
		});
		
		Button absBtn = (Button) findViewById(R.id.btn_abstracts);
		absBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent abstractIntent = new Intent(MainActivity.this, Abstracts.class);
				startActivity(abstractIntent);
				
			}
		});
		
		Button mapBtn = (Button) findViewById(R.id.btn_maps);
		mapBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mapIntent = new Intent(MainActivity.this, MapActivity.class);
				startActivity(mapIntent);
				
			}
		});
		
		Button scheduleBtn = (Button) findViewById(R.id.btn_schedule);
		scheduleBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent scheduleIntent = new Intent(MainActivity.this, ScheduleMainActivity.class);
				startActivity(scheduleIntent);
				
			}
		});

		
		Button favBtn = (Button) findViewById(R.id.btn_favorites);
		favBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent favIntent = new Intent(MainActivity.this, FavoriteAbstracts.class);
				startActivity(favIntent);
				
			}
		});
		
		Button infBtn = (Button) findViewById(R.id.btn_info);
		infBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent infIntent = new Intent(MainActivity.this, GeneralActivity.class);
				startActivity(infIntent);
				
			}
		});
		
		
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.genInfo:
            Intent genInfo = new Intent(this, GeneralActivity.class);
            this.startActivity(genInfo);
            return true;
        
		case R.id.updateAbs:
		{
			
			//start an async task and check with server if there are any new abstracts
			SynchronizeWithServer syncTask = new SynchronizeWithServer();
			syncTask.execute();
			return true;
		}    
            
		case R.id.abtApp:
		{
			Builder aboutDialog = new AlertDialog.Builder(MainActivity.this);
			aboutDialog.setTitle("About the App:")
			.setMessage(Html.fromHtml("The <b>G-Node Conference Application</b> for Android serves as an electronic conference guide for participants with included proceedings. <br><br> &#169; <b>German Neuroinformatics Node</b><br><br>Created By: <b>Shumail Mohy-ud-Din</b><br>(as part of GSoC 2014)"))
			.setNeutralButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			}).setIcon(getResources().getDrawable(R.drawable.launcher_brain_icon))
			 .show();
		}	
	        return true;
		
		}
			
		return false;
		
	}

	private class SynchronizeWithServer extends AsyncTask<Void, Void, Void> {

		private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
		
		@Override
		protected void onPreExecute() {
			Dialog.setMessage("Checking with Server...");
	        Dialog.setCancelable(false);
	        Dialog.show();
			
		}
		
		@Override
		protected Void doInBackground(Void... params) {
	
			InputStream in = null;
			String response = null;
			
			in = MainActivity.this.getResources().openRawResource(R.raw.abstracts_up);
			dbHelpeer.open();
			SyncAbstracts sync = new SyncAbstracts(dbHelpeer, in);
			sync.doSync();
			
			
//			try {
//				Log.d("GCA-Sync", "Connecting...");
//				URL url = new URL("http://192.168.173.1:9000/api/conferences/2311a932-1e89-4817-b767-a18f4a0b879f/abstracts/2015-07-09T08:24:50.833Z");
//				HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
//				Log.d("GCA-Sync", "Connection opened");
//				httpConnection.setRequestMethod("GET");
//				Log.d("GCA-Sync", "Method Set");
//				httpConnection.connect();
//				Log.d("GCA-Sync", "connected");
//				Log.d("GCA-Sync", "Response Code: " + httpConnection.getResponseCode());
//				if(httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//					in = httpConnection.getInputStream();
//					response = convertStreatToString(in);
//					
//					Log.d("GCA-Sync", "Response received from: " + url + ": " + response);
//					Log.d("GCA-Sync", "Response Length: " + response.length());
//					
//					if(response.length() <=2) {
//						//
//						Toast toast = Toast.makeText(getApplicationContext(), "Already up to date!", Toast.LENGTH_SHORT);
//						toast.show();
//					} else {
//						
//						/*
//						 * some valid response. Need to process abstracts - 2 options
//						 * 1) insert 
//						 * 2) update
//						 * 
//						 * but here, first i should build array of existing abstract UUIDs so to 
//						 * compare if an abstract is to be updated or inserted
//						 * 
//						 * POJOs initially, parse all! and then decide what to do,.
//						 * 
//						 * modify abstractsjsonparse with a flag to decide either to update or insert
//						 */
//					}
//					
//				} else {
//					// some error in connecting 
//				}
//				
//				httpConnection.disconnect();
//				
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			}
//			
//			catch (IOException e) {
//		         e.printStackTrace();
//			}
//			
			
//			String response = null;
//			String url = null;
//			
//			
//			try {
//	            // http client
//	            DefaultHttpClient httpClient = new DefaultHttpClient();
//	            HttpEntity httpEntity = null;
//	            HttpResponse httpResponse = null;
//	             
//	            HttpGet httpGet = new HttpGet(url);
//	 
//	            httpResponse = httpClient.execute(httpGet);
//	            
//	            httpEntity = httpResponse.getEntity();
//	            response = EntityUtils.toString(httpEntity);
//	 
//	        } catch (UnsupportedEncodingException e) {
//	            e.printStackTrace();
//	        } catch (ClientProtocolException e) {
//	            e.printStackTrace();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
			
			
			return null;
		}
		
		private String convertStreatToString(InputStream in) {
			java.util.Scanner s = new java.util.Scanner(in).useDelimiter("\\A");
		    return s.hasNext() ? s.next() : "";
		}

		@Override
		protected void onPostExecute(Void result){
			dbHelpeer.close();
			Dialog.dismiss();
			//notify that it's updated
		}

	}
	
}



