/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.abstracts;

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

import com.g_node.gca.SyncAbstracts;
import com.g_node.gcaa.R;

public class Abstracts extends Activity {
	
	Cursor cursor;
	public static int cursorCount;
	EditText searchOption;
	ListView listView;
	AbstractCursorAdapter cursorAdapter;
	
	String query = "";
	
	String gTag = "GCA-Abstracts";
	DatabaseHelper dbHelper = new DatabaseHelper(this);
	DatabaseHelper syncDbHelpaer = new DatabaseHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_abstracts);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		listView = (ListView)findViewById(R.id.AbsListView);
		searchOption = (EditText)findViewById(R.id.abstractSearch);
		
		/*
         * Get Writable Database
         */
        dbHelper.open();
		
        /*
         * SQL Query to get data
         */
        query = "SELECT UUID AS _id , TOPIC, TITLE, ABSRACT_TEXT, STATE, SORTID, REASONFORTALK, MTIME, TYPE,DOI, COI, ACKNOWLEDGEMENTS " +
        				"FROM ABSTRACT_DETAILS ORDER BY SORTID;";
        
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
		
		private ProgressDialog Dialog = new ProgressDialog(Abstracts.this);
		
		@Override
		protected void onPreExecute()
	    {
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
			
			cursor = DatabaseHelper.database.rawQuery(query, null);
	        int a = cursor.getCount();
	        Log.i(gTag, "data got rows : " + Integer.toString(a));
	        
	        /*
	         * Get number of data to check whether database has any data or it's
	         * empty
	         */
	        cursorCount = cursor.getCount();
	        
	        /*
	         * Check If Database is empty.
	         */
	        if (cursorCount <= 0) {

	           //call jsonParse function to get data from abstracts JSON file
	        	InputStream jsonStream = Abstracts.this.getResources().openRawResource(R.raw.abstracts_raw);
	        	 
	        	AbstractsJsonParse parseAbstractsJson = new AbstractsJsonParse(jsonStream, dbHelper);
	            parseAbstractsJson.jsonParse();
	            parseAbstractsJson.saveFromArrayListtoDB();
	        	
	            /*
	             * Query execution
	             */
	            cursor = DatabaseHelper.database.rawQuery(query, null);
	            /*
	             * get number of cursor data
	             */
	            cursorCount = cursor.getCount();
	        }
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result){
	        
			//set listView
			
	        cursorAdapter = new AbstractCursorAdapter(Abstracts.this, cursor);
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
	                    String value = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
	                    String acknowledgements = cursor.getString(cursor.getColumnIndexOrThrow("ACKNOWLEDGEMENTS"));
	                    Intent in = new Intent(getApplicationContext(), AbstractContent.class);
	                    /*
	                     * Passing data by Intent
	                     */
	                    in.putExtra("abstracts", Text);
	                    in.putExtra("Title", Title);
	                    in.putExtra("Topic", Topic);
	                    in.putExtra("value", value);
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
				    return dbHelper.fetchDataByName(constraint.toString());
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
	
	int connectivityFlag = 0;
	int notificationFlag = 0;
	
	private ProgressDialog Dialog = new ProgressDialog(Abstracts.this);
	
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
					
					if(in.available() <=20) { // 20 because even a single abstract object is returned; it'll be much more than 5
						//Notify user that it's already upto date
						notificationFlag = -1;
						
					} else {
						/*
						 * Some valid response. Need to synchronize
						 */
						
						if(!dbHelper.database.isOpen()) {
							dbHelper.open();
						}
						//in = MainActivity.this.getResources().openRawResource(R.raw.abstracts_up);
						Log.d("GCA-Sync", "stream yy: " + in + " -- " + in.available());
						SyncAbstracts sync = new SyncAbstracts(dbHelper, in);
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
			
			Builder x = new AlertDialog.Builder(Abstracts.this);
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
				cursor = DatabaseHelper.database.rawQuery(query, null);
				cursorAdapter.changeCursor(cursor);
				cursorAdapter.notifyDataSetChanged();
				//syncDbHelper.close("sync");
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

  } //end sync class

}
