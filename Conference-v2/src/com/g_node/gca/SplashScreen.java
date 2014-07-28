package com.g_node.gca;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.g_node.gca.schedule.DateWiseEventsRecord;
import com.g_node.gca.schedule.EventScheduleItem;
import com.g_node.gca.schedule.ScheduleItemRecord;
import com.g_node.gca.schedule.ScheduleMainActivity;
import com.g_node.gca.schedule.SessionScheduleItem;
import com.g_node.gca.schedule.TrackScheduleItem;
import com.shumail.newsroom.R;
import com.shumail.newsroom.R.layout;
import com.shumail.newsroom.R.menu;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.Window;

public class SplashScreen extends Activity {
	
	String LOG_TAG = "GCA-Schedule";
	
	private String SCHEDULE_ITEMTYPE_EVENT = "event";
	private String SCHEDULE_ITEMTYPE_TRACK = "track";
	private String SCHEDULE_ITEMTYPE_SESSION = "session";
	
	private SessionScheduleItem tempSession;
	
	List<ScheduleItemRecord> scheduleRecordsArray = new ArrayList<ScheduleItemRecord>() ;
	
	List<EventScheduleItem> eventsRecordsArray = new ArrayList<EventScheduleItem>() ;
	
	List<TrackScheduleItem> tracksRecordsArray = new ArrayList<TrackScheduleItem>() ;
	
	List<SessionScheduleItem> sessionRecordsArray = new ArrayList<SessionScheduleItem>() ;
	
	List<DateWiseEventsRecord> dateWiseEventsRecordList = new ArrayList<DateWiseEventsRecord>();
	
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Splash screen timer
    	int SPLASH_TIME_OUT = Integer.parseInt(getResources().getString(R.string.splashTiming));
        
        //removing the action bar of splash screen
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
        
        getScheduleJSONData();
        groupEventsByDate();
        setScheduleData();
        
        new Handler().postDelayed(new Runnable() {
        	//Handler for for showing splash screen for a little time
        	
            @Override
            public void run() {
                //Start activity Tabs
                Intent mainActivity = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(mainActivity);
                
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
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
                		
                		String event_authors = scheduleItemJsonObject.getJSONArray("authors").toString().replace("\"", "").replace("[", "").replace("]", "");
                		Log.i(LOG_TAG, "Basic Event - Authors: " + event_authors);
                		
                		String event_type = scheduleItemJsonObject.getString("type");
                		Log.i(LOG_TAG, "Basic Event - Type: " + event_type);
                		
                		String event_abstract = scheduleItemJsonObject.getString("abstract");
                		Log.i(LOG_TAG, "Basic Event - Abstract: " + event_abstract);
                		
                		String scheduleItemType = "event";
                		               		
                		//adding the event to events arraylist
                		eventsRecordsArray.add( new EventScheduleItem(event_title, event_subtitle, event_start_time, event_end_time, event_location, event_date, event_authors, event_type, event_abstract) );
                		
                		//get index of this added item now
                		int eventAddedIndex = eventsRecordsArray.size() - 1;
                		
                		//add it now to main records array
                		scheduleRecordsArray.add(new ScheduleItemRecord(SCHEDULE_ITEMTYPE_EVENT, eventAddedIndex, event_date));
                		
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
                		
                		//add the session to sessions list and initialize the tracks array to size of tracks in session
                		sessionRecordsArray.add(new SessionScheduleItem(session_title, session_subtitle, sessionTracksArray.length()));
                		
                		//get index of added session in the list
                		int addedSessionIndex = sessionRecordsArray.size() - 1;
                		
                		String tempSessionDate = sessionTracksArray.getJSONObject(0).getJSONArray("events").getJSONObject(0).getString("date");
                		Log.i(LOG_TAG, "Session - " + session_title + " - Date: " + tempSessionDate);
                		//add it now to main records array
                		scheduleRecordsArray.add(new ScheduleItemRecord(SCHEDULE_ITEMTYPE_SESSION, addedSessionIndex, tempSessionDate));                		
                		
                		//get the session added at this index
                		tempSession = sessionRecordsArray.get(addedSessionIndex);
                		
                		for (int trackCounter = 0; trackCounter < sessionTracksArray.length(); trackCounter++) {
                			
                			//get Track object 
                			JSONObject sessionTrackObject = sessionTracksArray.getJSONObject(trackCounter);
                			//parse the track object
                			parseSessionTrackJSON(trackCounter, sessionTrackObject);
                		}
                		
                		//add session back - update
                		sessionRecordsArray.set(addedSessionIndex, tempSession);
                		
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
		
		//add tracks to track array list and initialize the respective events array for that track
		tracksRecordsArray.add(new TrackScheduleItem(track_title, track_subtitle, track_chair, trackEventsArray.length()) );
		
		//get index of that added track and add events at that index
		int addedTrackIndex = tracksRecordsArray.size() - 1;
		
		String tempTrackDate = _scheduleItemJsonObject.getJSONArray("events").getJSONObject(0).getString("date");
		Log.i(LOG_TAG, "Date for Track - " + track_title + "is: " + tempTrackDate);
		
		//add it now to main records array
		scheduleRecordsArray.add(new ScheduleItemRecord(SCHEDULE_ITEMTYPE_TRACK, addedTrackIndex, tempTrackDate));
		
		//get track that's added to add events now after parsing events
		TrackScheduleItem tempTrack =  tracksRecordsArray.get(addedTrackIndex);
		
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
				
				String track_event_authors = trackEventObject.getJSONArray("authors").toString().replace("\"", "").replace("[", "").replace("]", "");
				Log.i(LOG_TAG, "Track Event - Authors: " + track_event_authors);
				
				String track_event_type = trackEventObject.getString("type");
				Log.i(LOG_TAG, "Track Event - Type: " + track_event_type);
				
				String track_event_abstract = trackEventObject.getString("abstract");
				Log.i(LOG_TAG, "Track Event - Abstract: " + track_event_abstract);
				
				//add the event to the events array in track in order.
				tempTrack.setEventsInTrack(counter, 
						new EventScheduleItem(track_event_title, track_event_subtitle, 
								track_event_start_time, track_event_end_time, track_event_location, track_event_date, 
								track_event_authors, track_event_type, track_event_abstract)
				);	
		}
		
		tracksRecordsArray.set(addedTrackIndex, tempTrack);
	}
	
	
	//Function for parsing Tracks in Session
	void parseSessionTrackJSON(int _counter, JSONObject _scheduleItemJsonObject) throws JSONException {
			
			String track_title = _scheduleItemJsonObject.getString("title");
			Log.i(LOG_TAG, "Track - title: " + track_title);
			
			String track_subtitle = _scheduleItemJsonObject.getString("subtitle");
			Log.i(LOG_TAG, "Track - subtitle: " + track_subtitle);
			
			String track_chair = _scheduleItemJsonObject.getString("chair").replace("\"", "").replace("[", "").replace("]", "");
			Log.i(LOG_TAG, "Track - Chair: " + track_chair);
			
			Log.i(LOG_TAG, "Tracks - Start parsing Events");
			
			JSONArray trackEventsArray = _scheduleItemJsonObject.getJSONArray("events");
			
			//initialize track and add to that respective location
			tempSession.setTracksInSession(_counter, new TrackScheduleItem(track_title, track_subtitle, track_chair, trackEventsArray.length()));
			
			//ab kisi tarah Track jo add hwa ha wo get ker k us jaga events add krney han
			TrackScheduleItem trackAddedForSession = tempSession.getTracksInSession(_counter);
			
			
//			//add tracks to track array list and initialize the respective events array for that track
//			tracksRecordsArray.add(new TrackScheduleItem(track_title, track_subtitle, track_chair, trackEventsArray.length()) );
//			
//			//get index of that added track and add events at that index
//			int addedTrackIndex = tracksRecordsArray.size() - 1;
//
//			//add it now to main records array
//			scheduleRecordsArray.add(new ScheduleItemRecord(SCHEDULE_ITEMTYPE_TRACK, addedTrackIndex));
//			
//			//get track that's added to add events now after parsing events
//			TrackScheduleItem tempTrack =  tracksRecordsArray.get(addedTrackIndex);
			
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
					
					String track_event_authors = trackEventObject.getJSONArray("authors").toString().replace("\"", "").replace("[", "").replace("]", "");
					Log.i(LOG_TAG, "Track Event - Authors: " + track_event_authors);
					
					String track_event_type = trackEventObject.getString("type");
					Log.i(LOG_TAG, "Track Event - Type: " + track_event_type);
					
					String track_event_abstract = trackEventObject.getString("abstract");
					Log.i(LOG_TAG, "Track Event - Abstract: " + track_event_abstract);
					
					trackAddedForSession.setEventsInTrack(counter, new EventScheduleItem(track_event_title, track_event_subtitle, 
									track_event_start_time, track_event_end_time, track_event_location, track_event_date, 
									track_event_authors, track_event_type, track_event_abstract)
					);
				
			}
			
			//update the track back in list from tempTrack
			tempSession.setTracksInSession(_counter, trackAddedForSession);
		}	

	void groupEventsByDate() {
		
		Log.i(LOG_TAG, "in groupEvents func - Line 425");
		List<String> differentDatesForGroups = new ArrayList<String>();
		int indexOfCurrentDate;
		
		for(int i=0; i<scheduleRecordsArray.size(); i++) {
			Log.i(LOG_TAG, "in groupEvents func for loop - Line 430");
			//get current event record from indexes array
			ScheduleItemRecord temperoryScheduleItem = scheduleRecordsArray.get(i);
			
			String dateOfCurrentEventItem = temperoryScheduleItem.getEvent_date();
			
			Log.i(LOG_TAG, "in groupEvents func - Line 436");
			
			if(!differentDatesForGroups.contains(dateOfCurrentEventItem)) {
				//if date is not already in record, add it
				Log.i(LOG_TAG, "in groupEvents func - Line 440");
				differentDatesForGroups.add(dateOfCurrentEventItem);
				Log.i(LOG_TAG, "in groupEvents func - Line 442");
				indexOfCurrentDate = differentDatesForGroups.size() -1;
			} else {
				//get index of the date
				indexOfCurrentDate = differentDatesForGroups.indexOf(dateOfCurrentEventItem);
			}
			Log.i(LOG_TAG, "in groupEvents func - Line 448");
			//now check the List of GroupEvents for this Index
			
			DateWiseEventsRecord tempDateGroup;
			
			try {
				tempDateGroup = dateWiseEventsRecordList.get(indexOfCurrentDate);
			} catch (IndexOutOfBoundsException e) {
				// TODO: handle exception
				dateWiseEventsRecordList.add(new DateWiseEventsRecord(dateOfCurrentEventItem));
				tempDateGroup = dateWiseEventsRecordList.get(indexOfCurrentDate);
			}
			
			
			Log.i(LOG_TAG, "in groupEvents func - Line 451");
			
			if(tempDateGroup == null ) {
				dateWiseEventsRecordList.add(new DateWiseEventsRecord(dateOfCurrentEventItem));
				tempDateGroup = dateWiseEventsRecordList.get(indexOfCurrentDate);
			} //end if condition for null
			
			//add the index of this event item into the group eventsForThisDate
			tempDateGroup.addEventsForDate(i);
			
			//Update the main list
			dateWiseEventsRecordList.set(indexOfCurrentDate, tempDateGroup);
			
			Log.i(LOG_TAG, "in groupEvents func - Line 464");
		} //end outer for loop - iterating for indexes array
		Log.i(LOG_TAG, "in groupEvents func - Line 466");
	} //end function groupEventsByDate
	
	void setScheduleData() {
		ScheduleMainActivity.scheduleRecordsArray = this.scheduleRecordsArray;
		ScheduleMainActivity.eventsRecordsArray = this.eventsRecordsArray;
		ScheduleMainActivity.tracksRecordsArray = this.tracksRecordsArray;
		ScheduleMainActivity.sessionRecordsArray = this.sessionRecordsArray;
		ScheduleMainActivity.dateWiseEventsRecordList = this.dateWiseEventsRecordList;
	}    
    
}
