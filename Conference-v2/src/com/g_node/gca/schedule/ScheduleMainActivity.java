package com.g_node.gca.schedule;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.shumail.newsroom.R;
import com.shumail.newsroom.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ScheduleMainActivity extends Activity {
	
	String LOG_TAG = "GCA-Schedule";
	
	private String SCHEDULE_ITEMTYPE_EVENT = "event";
	private String SCHEDULE_ITEMTYPE_TRACK = "track";
	private String SCHEDULE_ITEMTYPE_SESSION = "session";
	
	private SessionScheduleItem tempSession;
	
	List<ScheduleItemRecord> scheduleRecordsArray = new ArrayList<ScheduleItemRecord>() ;
	
	List<EventScheduleItem> eventsRecordsArray = new ArrayList<EventScheduleItem>() ;
	
	List<TrackScheduleItem> tracksRecordsArray = new ArrayList<TrackScheduleItem>() ;
	
	List<SessionScheduleItem> sessionRecordsArray = new ArrayList<SessionScheduleItem>() ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_main);
		
		getScheduleJSONData();
		
		TextView x = (TextView) findViewById(R.id.schedulemain);
		x.setText("Schedule Mian");
		x.setVisibility(View.GONE);
		
		ListView ScheduleList = (ListView) findViewById(R.id.ScheduleMainList);
		Log.i(LOG_TAG, "ScheduleList id got - layout got");
		scheduleAdapter adapter = new scheduleAdapter(this, scheduleRecordsArray, eventsRecordsArray, tracksRecordsArray, sessionRecordsArray, this);
		Log.i(LOG_TAG, "Adapter set - constructor initialized");
		ScheduleList.setAdapter(adapter);
		
		//List Item Click Listener
		
		ScheduleList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				// TODO Auto-generated method stub
				
				Log.i("GCA-Schedule-List", "Clicked Item - int position: " + position);
				Log.i("GCA-Schedule-List", "Clicked Item - Long ID: " + id);
				
				if(scheduleRecordsArray.get(position).getSchedule_item_type().equals(SCHEDULE_ITEMTYPE_EVENT)) {
					Log.i("GCA-Schedule-List", "Event Clicked");
					
					ScheduleItemRecord scheduleItemRecordAtCurrentPosition = scheduleRecordsArray.get(position);
					EventScheduleItem eventAtListPosition = eventsRecordsArray.get(scheduleItemRecordAtCurrentPosition.getIndex() );
					
					//ScheduleItemExtended scheduleDetailObject = new ScheduleItemExtended(eventAtListPosition);
					
					Intent intent = new Intent(ScheduleMainActivity.this, ScheduleItemExtended.class);
					
					Bundle bundle = new Bundle();
					bundle.putSerializable("dEvent", eventAtListPosition);
					
					//bundle.putString("type", SCHEDULE_ITEMTYPE_SESSION);
					bundle.putString("type", scheduleRecordsArray.get(position).getSchedule_item_type());
					
					intent.putExtras(bundle);
					startActivity(intent);
					
				} else if (scheduleRecordsArray.get(position).getSchedule_item_type().equals(SCHEDULE_ITEMTYPE_TRACK)) {
					
					Log.i("GCA-Schedule-List", "Track Clicked");
					
					ScheduleItemRecord scheduleItemRecordAtCurrentPosition = scheduleRecordsArray.get(position);
					TrackScheduleItem trackAtListPosition = tracksRecordsArray.get(scheduleItemRecordAtCurrentPosition.getIndex() );
					
					//ScheduleItemExtended scheduleDetailObject = new ScheduleItemExtended(eventAtListPosition);
					
					Intent intent = new Intent(ScheduleMainActivity.this, ScheduleItemExtended.class);
					
					Bundle bundle = new Bundle();
					bundle.putSerializable("dTrack", trackAtListPosition);
					
					//bundle.putString("type", SCHEDULE_ITEMTYPE_SESSION);
					bundle.putString("type", scheduleRecordsArray.get(position).getSchedule_item_type());
					
					intent.putExtras(bundle);
					startActivity(intent);
					
				} else {
					Log.i("GCA-Schedule-List", "shit Clicked");
				}
				
			}
		});
		
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
                		scheduleRecordsArray.add(new ScheduleItemRecord(SCHEDULE_ITEMTYPE_EVENT, eventAddedIndex));
                		
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
                		
                		//add it now to main records array
                		scheduleRecordsArray.add(new ScheduleItemRecord(SCHEDULE_ITEMTYPE_SESSION, addedSessionIndex));                		
                		
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

		//add it now to main records array
		scheduleRecordsArray.add(new ScheduleItemRecord(SCHEDULE_ITEMTYPE_TRACK, addedTrackIndex));
		
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
	
	
	//Function for parsing Tracks
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
					
					String track_event_authors = trackEventObject.getJSONArray("authors").toString();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.schedule_main, menu);
		return true;
	}

}
