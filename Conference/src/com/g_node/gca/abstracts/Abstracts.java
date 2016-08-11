/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.abstracts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.Toast;

import com.g_node.ni16.R;

public class Abstracts extends Activity {
	
	public static int cursorCount;
	private EditText searchOption;
	private ListView listView;
	private AbstractCursorAdapter cursorAdapter;
	private Cursor cursor;
	
	private String SYNC_TIME_KEY = "com.g_node.gcaa.syncDateTime";
	private String APP_PKG_NAME = "com.g_node.gcaa";
	private String DB_CONSISTENCY_FLAG = "com.g_node.gcaa.dbConsistency";

	private SharedPreferences appPreferences;
	
	private String gTag = "GCA-Abstracts";
	private final DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_abstracts);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		listView = (ListView)findViewById(R.id.AbsListView);
		searchOption = (EditText)findViewById(R.id.abstractSearch);
		appPreferences = Abstracts.this.getSharedPreferences(APP_PKG_NAME, Context.MODE_PRIVATE);
		/*
         * Get Writable Database
         */
        dbHelper.open();
		
        /*
         * AsynchTask to parse the Abstracts JSON on a background thread
         * ListView is also made ready in same AsyncTask's onPostExecute method
         */
        
        AbstractJSONParsingTask jsonParsingAsyncTask = new AbstractJSONParsingTask();
        jsonParsingAsyncTask.execute();  //starts asyncTask and handles all json parsing, listview populating optimally 
        
	}//end onCreate
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.abstracts_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		
			case R.id.updateAbs:
			{
				//start an async task and check with server if there are any new abstracts
				SynchronizeWithServer syncTask = new SynchronizeWithServer();
				syncTask.execute();
				return true;
			}
			default:
				break;
		}
		return false;
	}
	
    @Override
    protected void onDestroy() {
    	Log.i("exc", "on destry called");
        super.onDestroy();
        dbHelper.close("db");
        // The activity is about to be destroyed.
    }

    
private class AbstractJSONParsingTask extends AsyncTask<Void, Void, Void> {
		
		private ProgressDialog Dialog; 
		
		@Override
		protected void onPreExecute()
		
	    {
			Dialog = new ProgressDialog(Abstracts.this);
	        Dialog.setMessage(Abstracts.this.getResources().getString(R.string.loading_abstracts_dialog_text));
	        Dialog.setCancelable(false);
	        Dialog.show();
	    }
		
		/*
         * doInBackgound handles all JSON parsing in background thread to avoid UI thread blockage
         */
		
		@Override
		protected Void doInBackground(Void... arg0) {
			
			/*
	         * Query execution
	         */
			
			
	        int a = dbHelper.getAbsCount();
	        Log.i(gTag, "data got rows : " + Integer.toString(a));
	        
	        /*
	         * Get value of DB_CONSISTENCY_FLAG from sharedPref and check if 
	         * database is consistent or not.
	         */
	        int dbConsitencyFlagVal = appPreferences.getInt(DB_CONSISTENCY_FLAG, -1);
	        Log.d(gTag, "Val of DB_CONSISTENCY_FLAG - checking as Abstracts " +
	        		"activity is opened: " + dbConsitencyFlagVal);
	        
	        /*
	         * Get number of data to check whether database has any data or it's
	         * empty
	         */
	        cursorCount = dbHelper.getAbsCount();
	        /*
	         * Check If Database is empty.
	         */
	        if (cursorCount <= 0 || dbConsitencyFlagVal == -1) {
	        	
	        	if(dbConsitencyFlagVal == -1) {
	        		Log.d(gTag, "Database is inconsitent - flag: " + dbConsitencyFlagVal);
	        		dbHelper.dropAllCreateAgain();
	    			appPreferences.edit().putInt(DB_CONSISTENCY_FLAG, 1).apply();
	        	} else {
	        		Log.d(gTag, "Database is consistent");
	        	}
	        	
	        	//call jsonParse function to get data from abstracts JSON file
	        	InputStream jsonStream = Abstracts.this.getResources().
	        			openRawResource(R.raw.abstracts);
	        	parseJsonAndInsert(jsonStream);				

	            
	            /*
	             * get number of cursor data
	             */
	            cursorCount = dbHelper.getAbsCount();
	        }
			
			return null;
		}

		void parseJsonAndInsert(InputStream jsonStream) {
			AbstractsJsonParse parseAbstractsJson = new AbstractsJsonParse(
					jsonStream, dbHelper);
			try{
				parseAbstractsJson.jsonParse();
			}
        	catch (FileNotFoundException e){
        		Log.e(gTag, e.getMessage());
        		appPreferences.edit().putInt(DB_CONSISTENCY_FLAG, -1);
        		cancel(true);
        		return;
        	} 
        	catch (IOException e) {
        		Log.e(gTag, e.getMessage());
        		appPreferences.edit().putInt(DB_CONSISTENCY_FLAG, -1);
        		cancel(true);
        		return;
			} 
        	catch (JSONException e) {
        		Log.e(gTag, e.getMessage());
        		appPreferences.edit().putInt(DB_CONSISTENCY_FLAG, -1);
        		cancel(true);
        		return;
			}			
			parseAbstractsJson.saveFromArrayListtoDB();
		}
		
		@Override
		protected void onPostExecute(Void result){
			//set listView
			cursor = dbHelper.getAllAbs();
	        cursorAdapter = new AbstractCursorAdapter(Abstracts.this, cursor, 
	        		AbstractCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	        listView.setAdapter(cursorAdapter);
	        listView.setTextFilterEnabled(true);
	        listView.setFastScrollEnabled(true);
	        listView.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
	                // TODO Auto-generated method stub

	                try {
	                    cursor = (Cursor)cursorAdapter.getCursor();
	                    /*
	                     * Getting data from Cursor
	                     */
	                    String Text = cursor.getString(cursor.getColumnIndexOrThrow("ABSRACT_TEXT"));
	                    Log.i(gTag, "ABSTRACT_TEXT => " + Text);
	                    String Title = cursor.getString(cursor.getColumnIndexOrThrow("TITLE"));
	                    String Topic = cursor.getString(cursor.getColumnIndexOrThrow("TOPIC"));
	                    String uuid = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
	                    String acknowledgements = cursor.getString(cursor.getColumnIndexOrThrow("ACKNOWLEDGEMENTS"));
	                    Intent in = new Intent(getApplicationContext(), AbstractContent.class);
	                    /*
	                     * Passing data by Intent
	                     */
	                    in.putExtra("abstracts", Text);
	                    in.putExtra("Title", Title);
	                    in.putExtra("Topic", Topic);
	                    in.putExtra("value", uuid);
	                    in.putExtra("acknowledgements", acknowledgements);
	                    startActivity(in);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        });
			
			Dialog.dismiss();
			
            
			/*
			* Searching filter to search data by Keywords, Title, Author Names, affiliation
			*/
			cursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
				public Cursor runQuery(CharSequence constraint) {
				    return dbHelper.findAbstractsWithString(constraint.toString());
				}
			});
			
			
			searchOption.addTextChangedListener(new TextWatcher() {

	            @Override
	            public void onTextChanged(CharSequence cs, int start, int before, int count) {
	                // TODO Auto-generated method stub
	                Abstracts.this.cursorAdapter.getFilter().filter(cs);
	                Abstracts.this.cursorAdapter.notifyDataSetChanged();

	            }

	            @Override
	            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	                // TODO Auto-generated method stub

	            }

	            @Override
	            public void afterTextChanged(Editable s) {
	                // TODO Auto-generated method stub

	            }
	        });
			
		} //end onPostExecute
	} //end AsyncTask class


private class SynchronizeWithServer extends AsyncTask<Void, Void, Void> {
	
	private int connectivityFlag = 0;
	private int notificationFlag = 0;
	
	private ProgressDialog Dialog;
	
	@Override
	protected void onPreExecute() {
		Dialog = new ProgressDialog(Abstracts.this);
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
				
				/*
	             * Read the last saved time and append to URL
	             * 1 - first we will read the last saved timestamp from sharedpreferences
	             * 2 - we'll formulate the URL by appending the timestamp at end
	             * 		- the structure of URL would be usually like
	             * 		http://127.0.0.1:9000/api/conferences/2311a932-1e89-4817-b767-a18f4a0b879f/abstracts/2015-07-09T08:24:50.833Z
	             * 		- note the timestamp in end. that's what we have stored in db and will append
	             * 		- The previous part of url before timestamp is read from strings.xml
	             * 		- so for formulating we'll read url from strings.xml and concatenate timestamp at end
	             */
	    	    long lastSyncTime = appPreferences.getLong(SYNC_TIME_KEY,0);
	    	    Log.d("GCA-Sync", "SYNC: Previous sync time for URL appending: " + lastSyncTime);
	            
	    	    String urlString = getResources().getString(R.string.sync_url) + lastSyncTime;
	    	    
	    	    Log.d("GCA-Sync", "SYNC: URL: " + urlString);
				
	    	    /*
	    	     * connecting with server
	    	     */
	    	    URL url = new URL(urlString);				
				HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
				Log.d("GCA-Sync", "Connection opened");
				httpConnection.setRequestMethod("GET");
				Log.d("GCA-Sync", "Method Set");
				httpConnection.connect();
				Log.d("GCA-Sync", "connected");
				Log.d("GCA-Sync", "Response Code: " + httpConnection.getResponseCode());
				
				if(httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					in = httpConnection.getInputStream();
					Log.d("GCA-Sync", "Received Stream size: " + httpConnection.getContentLength() + " -- " + in.available());
					
					if(httpConnection.getContentLength() <=20) { // 20 because even a single abstract object is returned; it'll be much more than 5
						//Notify user that it's already upto date
						notificationFlag = -1;
						
					} else {
						/*
						 * Some valid response. Need to synchronize
						 */
						
						
						/*
						 * set value of DB_CONSISTENCY_FLAG to -1 here
						 * Which we'll update back to 1 after synchronizing with server
						 */
						appPreferences.edit().putInt(DB_CONSISTENCY_FLAG, -1).apply();
						Log.d(gTag, "DB_CONSISTENCY_FLAG set to -1 before syncing");
						
						dbHelper.dropAllCreateAgain();						
						AbstractJSONParsingTask jsonParsingAsyncTask = 
								new AbstractJSONParsingTask();
						jsonParsingAsyncTask.parseJsonAndInsert(in);
						 
						/*
						 * As synchronizing has been done here, update the value
						 * of DB_CONSISTENCY_FLAG back to 1 here
						 * If it's not updated back to 1 here, (because of some
						 * interuption like if device gets powered off while updating)
						 * the next time user opens Abstracts activity, it will check if
						 * value of flag is not 1, it will drop all db and build it again
						 * and set a old sync date, because with old sync date the next
						 * time it tries to sync with server, it'll fetch everything to sync again
						 * as the db is built again
						 */
						appPreferences.edit().putInt(DB_CONSISTENCY_FLAG, 1).apply();
						long newSyncTime = appPreferences.getLong(SYNC_TIME_KEY, 0) + 1;
						appPreferences.edit().putLong(SYNC_TIME_KEY, newSyncTime).apply();
						Log.d(gTag, "DB_CONSISTENCY_FLAG value after syncing: " + appPreferences.getInt(DB_CONSISTENCY_FLAG, -1));
					}
					
				} else {	//response from HTTP not 200
					
					notificationFlag = -1;
				}
				
				httpConnection.disconnect();
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
				connectivityFlag = -1;
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
			
			Builder x = new AlertDialog.Builder(Abstracts.this);
		    x.setTitle("ERROR")
		    .setMessage("Unable to connect to Server")
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
				Toast toast = Toast.makeText(getApplicationContext(), "Already up to date!", 
						Toast.LENGTH_LONG);
				toast.show();
			} else {
				cursor = dbHelper.getAllAbs();
				cursorAdapter.changeCursor(cursor);
				cursorAdapter.notifyDataSetChanged();
				//syncDbHelper.close("sync");
				//notify that it's updated
				Toast toast = Toast.makeText(getApplicationContext(), "Synchronized with server successfully.", 
						Toast.LENGTH_SHORT);
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

  } //end sync class

}
