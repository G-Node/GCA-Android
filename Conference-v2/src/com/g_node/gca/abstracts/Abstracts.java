package com.g_node.gca.abstracts;

import java.io.InputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;

import com.shumail.newsroom.R;

public class Abstracts extends Activity {
	
	Cursor cursor;
	public static int cursorCount;
	EditText searchOption;
	ListView listView;
	AbstractCursorAdapter cursorAdapter;
	
	String query = "";
	
	String gTag = "GCA-Abstracts";
	DatabaseHelper dbHelper = new DatabaseHelper(this);

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
        				"FROM ABSTRACT_DETAILS;";
        
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
		getMenuInflater().inflate(R.menu.abstracts, menu);
		return true;
	}
	
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
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
	        	InputStream jsonStream = Abstracts.this.getResources().openRawResource(R.raw.abstracts);
	        	 
	        	AbstractsJsonParse parseAbstractsJson = new AbstractsJsonParse(jsonStream, dbHelper);
	            parseAbstractsJson.jsonParse();
	        	
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

}
