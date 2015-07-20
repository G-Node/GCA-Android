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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
		
		int connectivityFlag = 0;
		int notificationFlag = 0;
		
		private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
		
		@Override
		protected void onPreExecute() {
			Dialog.setMessage("Please wait while app synchrinizes with Server...");
	        Dialog.setCancelable(false);
	        Dialog.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
	
			if(!isNetworkAvailable()) {	//if no internet access
				connectivityFlag = -1;
			} else {
				/*
				 * internet is available
				 */
				InputStream in = null;
	
				try {
					Log.d("GCA-Sync", "Connecting...");
					URL url = new URL(getResources().getString(R.string.sync_url));
					HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
					Log.d("GCA-Sync", "Connection opened");
					httpConnection.setRequestMethod("GET");
					Log.d("GCA-Sync", "Method Set");
					httpConnection.connect();
					Log.d("GCA-Sync", "connected");
					Log.d("GCA-Sync", "Response Code: " + httpConnection.getResponseCode());
					
					if(httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
						in = httpConnection.getInputStream();
						Log.d("GCA-Sync", "stream xx: " + in + " -- " + in.available());
						
						if(in.available() <=10) { // 10 because even a single abstract object is returned; it'll be much more than 5
							//Notify user that it's already upto date
							notificationFlag = -1;
							
						} else {
							/*
							 * Some valid response. Need to synchronize
							 */
							dbHelpeer.open();
							
							//in = MainActivity.this.getResources().openRawResource(R.raw.abstracts_up);
							Log.d("GCA-Sync", "stream yy: " + in + " -- " + in.available());
							SyncAbstracts sync = new SyncAbstracts(dbHelpeer, in);
							sync.doSync();
						}
						
					} else {	//response from HTTP not 200
						
						// some error in connecting - inform user.
						Toast toast = Toast.makeText(getApplicationContext(), "Unable to Synchronize with Server. Try later", Toast.LENGTH_LONG);
						toast.show();
					}
					
					httpConnection.disconnect();
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				
				catch (IOException e) {
			         e.printStackTrace();
				}
			}// end else
			
			return null;
			
		}//end doInBackground
		
		@Override
		protected void onPostExecute(Void result){
			
			if(connectivityFlag == -1) {

				Dialog.dismiss();
				connectivityFlag = 0;
				
				Builder x = new AlertDialog.Builder(MainActivity.this);
			    x.setTitle("ERROR")
			    .setMessage("Unable to connect to Internet. Please ensure internet connectivity")
			    .setNeutralButton(android.R.string.ok,
			            new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int id) {
			            dialog.cancel();
			        }
			    }).setIcon(android.R.drawable.ic_dialog_alert).create().show();
			    
			} else {
				Dialog.dismiss();
				
				if(notificationFlag == -1) {
					notificationFlag = 0;
					Toast toast = Toast.makeText(getApplicationContext(), "Already up to date!", Toast.LENGTH_LONG);
					toast.show();
				} else {
					dbHelpeer.close();
					//notify that it's updated
					Toast toast = Toast.makeText(getApplicationContext(), "Synchronized with server successfully.", Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		}
		
		private boolean isNetworkAvailable() {
		    ConnectivityManager connectivityManager 
		          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
		}

	}
}



