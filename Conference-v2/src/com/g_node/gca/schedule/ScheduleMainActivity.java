package com.g_node.gca.schedule;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
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
                	
                	//closing if	
                	} else if(scheduleItemJsonObject.has("chair")) {	// 'chair' key is only in track
                		
                		//parse the track
                		parseScheduleTrackJSON(counter, scheduleItemJsonObject);
                	
                	//closing elseif
                	} else if(scheduleItemJsonObject.has("tracks")) {  // 'tracks' key is only in session
                		
                		String session_title = scheduleItemJsonObject.getString("title");
                		Log.i(LOG_TAG, "SESSION - title: " + session_title);
                		
                		String session_subtitle = scheduleItemJsonObject.getString("subtitle");
                		Log.i(LOG_TAG, "SESSION - subtitle: " + session_subtitle);
                		
                		Log.i(LOG_TAG, "SESSION - Start parsing Tracks");
                		
                		JSONArray sessionTracksArray = scheduleItemJsonObject.getJSONArray("tracks");
                		
                		for (int trackCounter = 0; trackCounter < sessionTracksArray.length(); trackCounter++) {
                			
                			//get Track object 
                			JSONObject sessionTrackObject = sessionTracksArray.getJSONObject(trackCounter);
                			//parse the track object
                			parseScheduleTrackJSON(trackCounter, sessionTrackObject);
                		}
                		
                		Log.i(LOG_TAG, "SESSION - End parsing Tracks");
                	}
            }
            
		} catch (Exception e) {
            // TODO: handle exception
            Log.getStackTraceString(e);
        }	
	}
	
	//Function for parsing Tracks
	void parseScheduleTrackJSON(int _counter, JSONObject _scheduleItemJsonObject) throws JSONException {
		
		String track_title = _scheduleItemJsonObject.getString("title");
		Log.i(LOG_TAG, "Track - title: " + track_title);
		
		String track_subtitle = _scheduleItemJsonObject.getString("subtitle");
		Log.i(LOG_TAG, "Track - subtitle: " + track_subtitle);
		
		String track_chair = _scheduleItemJsonObject.getString("chair");
		Log.i(LOG_TAG, "Track - Chair: " + track_chair);
		
		Log.i(LOG_TAG, "Tracks - Start parsing Events");
		
		JSONArray trackEventsArray = _scheduleItemJsonObject.getJSONArray("events");
		
		for (int counter = 0; counter < trackEventsArray.length(); counter++) {
			Log.i(LOG_TAG, "Track Event - Number: " + counter);
			
			JSONObject trackEventObject = trackEventsArray.getJSONObject(counter);
			
				String track_event_title = trackEventObject.getString("title");
				Log.i(LOG_TAG, "Track Event - title: " + track_event_title);
				
				String track_event_subtitle = trackEventObject.getString("subtitle");
				Log.i(LOG_TAG, "Track Event - subtitle: " + track_event_subtitle);
				
				String track_event_start_time = trackEventObject.getString("start");
				Log.i(LOG_TAG, "Track Event - Start: " + track_event_start_time);
				
				String track_event_end_time = trackEventObject.getString("end");
				Log.i(LOG_TAG, "Track Event - end : " + track_event_end_time);
				
				String track_event_location = trackEventObject.getString("location");
				Log.i(LOG_TAG, "Track Event - Location: " + track_event_location);
				
				String track_event_date = trackEventObject.getString("date");
				Log.i(LOG_TAG, "Track Event - date: " + track_event_date);
				
				String track_event_authors = trackEventObject.getJSONArray("authors").toString();
				Log.i(LOG_TAG, "Track Event - Authors: " + track_event_authors);
				
				String track_event_type = trackEventObject.getString("type");
				Log.i(LOG_TAG, "Track Event - Type: " + track_event_type);
				
				String track_event_abstract = trackEventObject.getString("abstract");
				Log.i(LOG_TAG, "Track Event - Abstract: " + track_event_abstract);
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.schedule_main, menu);
		return true;
	}

}
