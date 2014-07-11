package com.g_node.gca.schedule;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.shumail.newsroom.R;
import com.shumail.newsroom.R.layout;
import com.shumail.newsroom.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class ScheduleMainActivity extends Activity {
	
	String LOG_TAG = "GCA-Schedule";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_main);
		
		getScheduleJSONData();
	}
	
	
	//parsing JSON to get the Schedule Information's
	void getScheduleJSONData(){
		
		try {
            BufferedReader jsonReader = new BufferedReader(new InputStreamReader(this
                    .getResources().openRawResource(R.raw.schedule)));
            StringBuilder jsonBuilder = new StringBuilder();
            for (String line = null; (line = jsonReader.readLine()) != null;) {
                jsonBuilder.append(line).append("\n");
            }
            JSONTokener tokener = new JSONTokener(jsonBuilder.toString());
            JSONArray jsonArray = new JSONArray(tokener);
            
            //totalPages = jsonArray.length();
            for (int counter = 0; counter < jsonArray.length(); counter++) {
            	
//            	data[counter] = new ArrayList<ScheduleItem>();
                JSONObject scheduleItemJsonObject = jsonArray.getJSONObject(counter);	//get the event object - can be event, session or track
                	
                	if(scheduleItemJsonObject.has("start")) {	// 'start' key is only in event
                		
                		String event_title = scheduleItemJsonObject.getString("title");
                		Log.i(LOG_TAG, "Basic Event - title: " + event_title);
                		
                		String event_subtitle = scheduleItemJsonObject.getString("subtitle");
                		Log.i(LOG_TAG, "Basic Event - subtitle: " + event_subtitle);
                		
                		String event_start_time = scheduleItemJsonObject.getString("start");
                		Log.i(LOG_TAG, "Basic Event - Start: " + event_start_time);
                		
                		String event_end_time = scheduleItemJsonObject.getString("end");
                		Log.i(LOG_TAG, "Basic Event - end : " + event_end_time);
                		
                		String event_location = scheduleItemJsonObject.getString("location");
                		Log.i(LOG_TAG, "Basic Event - Location: " + event_location);
                		
                		String event_date = scheduleItemJsonObject.getString("date");
                		Log.i(LOG_TAG, "Basic Event - date: " + event_date);
                		
                		String event_authors = scheduleItemJsonObject.getJSONArray("authors").toString();
                		Log.i(LOG_TAG, "Basic Event - Authors: " + event_authors);
                		
                		String event_type = scheduleItemJsonObject.getString("type");
                		Log.i(LOG_TAG, "Basic Event - Type: " + event_type);
                		
                		String event_abstract = scheduleItemJsonObject.getString("abstract");
                		Log.i(LOG_TAG, "Basic Event - Abstract: " + event_abstract);
                		
                		String scheduleItemType = "event";
                		
                	}
            	
            }
            
		} catch (Exception e) {
            // TODO: handle exception
            Log.getStackTraceString(e);
        }
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.schedule_main, menu);
		return true;
	}

}
