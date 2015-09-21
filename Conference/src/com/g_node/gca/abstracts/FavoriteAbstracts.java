/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.abstracts;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.g_node.gcaa.R;

public class FavoriteAbstracts extends Activity {
	
	Cursor cursor;
	public static int cursorCount;
	EditText searchOption;
	ListView listView;
	AbstractCursorAdapter cursorAdapter;
	
	String gTag = "GCA-fav-Abstracts";
	final DatabaseHelper mDbHelper = DatabaseHelper.getInstance(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite_abstracts);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		listView = (ListView)findViewById(R.id.favAbsListView);
		
		/*
         * Get Writable Database
         */
        mDbHelper.open();
		//function to load favourite abstracts
        loadFavAbstracts();

	}

	
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDbHelper.close();
        // The activity is about to be destroyed.
    }
    
    @Override
    protected void onResume() {
    	Log.i(gTag, "in on resume");
    	super.onResume();
    	//refreshes the listview
    	listView.setAdapter(null);
    	loadFavAbstracts();
    }
    
    //function for loading favorite abstracts
    private void loadFavAbstracts() {
    	
    	Log.i(gTag, "Loading Fav Abstract function");
        cursor = mDbHelper.fetchFavoriteAbs();
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
        	//NO FAVORITES
        	TextView noAbstractInFav = (TextView)findViewById(R.id.noFavAbsText);
        	noAbstractInFav.setText("You don't have any Favorite Abstract");
        	cursor.close();
        	
        } else {
        	
        	TextView belowFav = (TextView)findViewById(R.id.noFavAbsText);
        	belowFav.setText("Your Favorited Abstracts");
        	
        	cursorAdapter = new AbstractCursorAdapter(this, cursor,
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
                        String value = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                        String afName = "TEST AF"; //cursor.getString(cursor.getColumnIndexOrThrow("AF_NAME"));
                        String email = "test@foo.bar"; //cursor.getString(cursor.getColumnIndexOrThrow("CORRESPONDENCE"));
                        String refs = "REF 1 Test"; // cursor.getString(cursor.getColumnIndexOrThrow("REFS"));
                        String acknowledgements = cursor.getString(cursor.getColumnIndexOrThrow("ACKNOWLEDGEMENTS"));
                        Intent in = new Intent(getApplicationContext(), AbstractContent.class);
                        /*
                         * Passing data by Intent
                         */
                        in.putExtra("abstracts", Text);
                        in.putExtra("Title", Title);
                        in.putExtra("Topic", Topic);
                        in.putExtra("value", value);
                        in.putExtra("afName", afName);
                        in.putExtra("email", email);
                        in.putExtra("refs", refs);
                        in.putExtra("acknowledgements", acknowledgements);
                        startActivity(in);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


}
