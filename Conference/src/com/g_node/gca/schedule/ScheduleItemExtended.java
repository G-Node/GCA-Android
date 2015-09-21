/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.schedule;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.g_node.gca.abstracts.AbstractContent;
import com.g_node.gca.abstracts.DatabaseHelper;
import com.g_node.gcaa.R;

public class ScheduleItemExtended extends Activity {
	
	private String SCHEDULE_ITEMTYPE_EVENT = "event";
	private String SCHEDULE_ITEMTYPE_TRACK = "track";
	private String SCHEDULE_ITEMTYPE_SESSION = "session";
	
	DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
	
	String event_abstract_uuid;
	
	ActionBar actionbar;
	
	String LOG_TAG = "GCA-Sch-Extend";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		dbHelper.open();
		actionbar = getActionBar();
		actionbar.setIcon(getResources().getDrawable(R.drawable.icon_schedule));
		actionbar.setHomeButtonEnabled(true);
 		actionbar.setDisplayHomeAsUpEnabled(true);
		
		Bundle bundle = getIntent().getExtras();
		String eType = bundle.getString("type");
		
		if(eType.equals(SCHEDULE_ITEMTYPE_EVENT)) {
			
			setContentView(R.layout.activity_schedule_item_extended_event);
			
			EventScheduleItem eventToDisplay =  (EventScheduleItem) bundle.getSerializable("dEvent");
			
			actionbar.setTitle("Event Details");
			
			Log.i("GCA-Schedule-List", "event title: " + eventToDisplay.getTitle());
			
			TextView eventTitleView = (TextView) findViewById(R.id.schedule_event_title);
			eventTitleView.setText(eventToDisplay.getTitle());
			
			((TextView) findViewById(R.id.schedule_event_subtitle)).setText(eventToDisplay.getSubtitle());
			((TextView) findViewById(R.id.schedule_event_author)).setText(eventToDisplay.getAuthors());
			((TextView) findViewById(R.id.schedule_event_time)).setText(eventToDisplay.getStart() + "   -   " + eventToDisplay.getEnd());
			((TextView) findViewById(R.id.schedule_event_location)).setText(eventToDisplay.getLocation());
			((TextView) findViewById(R.id.schedule_event_date)).setText(eventToDisplay.getDate());
			((TextView) findViewById(R.id.schedule_event_type)).setText(eventToDisplay.getType().toUpperCase());
			
			if (eventToDisplay.getLocation().equals("")) {
				findViewById(R.id.schedule_event_location_icon).
				setVisibility(View.INVISIBLE);
			}
			
			event_abstract_uuid = eventToDisplay.getEventAbstract();
			event_abstract_uuid = event_abstract_uuid.substring(event_abstract_uuid.lastIndexOf("/")+1, event_abstract_uuid.length());
			Log.i(LOG_TAG, "Abstract ID of event: " + event_abstract_uuid);
			
			Cursor abstractForEventCursor = dbHelper.fetchAbtractDetailsByUUID(
													 event_abstract_uuid);
			
			Button btnOpenAbstract = (Button) findViewById(R.id.btn_launch_Abstract_from_event);
			
			if(abstractForEventCursor.getCount() < 1) {
				btnOpenAbstract.setEnabled(false);
				btnOpenAbstract.setVisibility(View.GONE);
				//btnOpenAbstract.setVisibility(View.GONE);
			} else {
				btnOpenAbstract.setVisibility(View.VISIBLE);
				btnOpenAbstract.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
						Intent intent = new Intent(ScheduleItemExtended.this, AbstractContent.class);
						intent.putExtra("value", event_abstract_uuid);
						startActivity(intent);
					}
				});
				
			}
			
			
		} else if (eType.equals(SCHEDULE_ITEMTYPE_TRACK)) {
		
			setContentView(R.layout.activity_schedule_item_extended_track);
			
			TrackScheduleItem trackToDisplay =  (TrackScheduleItem) bundle.getSerializable("dTrack");
			
			actionbar.setTitle("Track Details");
			
			Log.i("GCA-Schedule-List", "Track title: " + trackToDisplay.getTitle());
			
			((TextView) findViewById(R.id.schedule_track_title)).setText(trackToDisplay.getTitle());
			((TextView) findViewById(R.id.schedule_track_subtitle)).setText(trackToDisplay.getSubtitle());			
			((TextView) findViewById(R.id.schedule_track_chair)).setText("Chair:   " + trackToDisplay.getChair());
			
			TableLayout trackEventsTableLayout = (TableLayout) findViewById(R.id.schedule_track_events_detail_table);
			
			EventScheduleItem[] tempTrackEvents = trackToDisplay.getEventsInTrack();
			
			for(int i=0; i < tempTrackEvents.length; i++) {
				
				TableRow tempEventRow = (TableRow) LayoutInflater.from(this).inflate(R.layout.schedule_extended_track_events_table_row, null);
				Button btn = (Button) tempEventRow.findViewById(R.id.btn_schedule_track_event_launch);
				btn.setText(tempTrackEvents[i].getTitle());
			
				trackEventsTableLayout.addView(tempEventRow);
				final EventScheduleItem eventToPass = tempTrackEvents[i];
				btn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent launchEventDetail = new Intent(ScheduleItemExtended.this, ScheduleItemExtended.class);

						Bundle bundle = new Bundle();
						
						bundle.putSerializable("dEvent", eventToPass);
						
						//bundle.putString("type", SCHEDULE_ITEMTYPE_SESSION);
						bundle.putString("type", SCHEDULE_ITEMTYPE_EVENT);
						
						launchEventDetail.putExtras(bundle);
						
						startActivity(launchEventDetail);
						
					}
				});
			}
			
			trackEventsTableLayout.requestLayout();
			
			
			
		
		} else {
			setContentView(R.layout.activity_schedule_item_extended_session);
			
			SessionScheduleItem sessionToDisplay = (SessionScheduleItem) bundle.getSerializable("dSession");
			
			actionbar.setTitle("Session Details");
			
			((TextView) findViewById(R.id.schedule_session_title)).setText(sessionToDisplay.getTitle());
			((TextView) findViewById(R.id.schedule_session_subtitle)).setText(sessionToDisplay.getSubtitle());
			
			TableLayout sessionTracksTrable = (TableLayout) findViewById(R.id.schedule_session_track_detail_table);
			
			TrackScheduleItem[] tempSessionTracks = sessionToDisplay.getTracksInSession();
			
			for(int i=0; i<tempSessionTracks.length; i++ ) {
				TableRow tempTrackInRow = (TableRow) LayoutInflater.from(this).inflate(R.layout.schedule_extended_track_events_table_row, null);
				
				Button btn = (Button) tempTrackInRow.findViewById(R.id.btn_schedule_track_event_launch);
				
				btn.setText(tempSessionTracks[i].getTitle());
				sessionTracksTrable.addView(tempTrackInRow);
				
				final TrackScheduleItem trackToPass = tempSessionTracks[i];
				
				btn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
						Intent launchEventDetail = new Intent(ScheduleItemExtended.this, ScheduleItemExtended.class);

						Bundle bundle = new Bundle();
						
						bundle.putSerializable("dTrack", trackToPass);
						
						bundle.putString("type", SCHEDULE_ITEMTYPE_TRACK);
						
						launchEventDetail.putExtras(bundle);
						
						startActivity(launchEventDetail);
						
					}
				});

			}
			
			sessionTracksTrable.requestLayout();
			
		}
		
		
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int theId = item.getItemId();
        if (theId == android.R.id.home) {
            finish();
        }
        return true;
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		dbHelper.close();
		super.onDestroy();
	}
	


}
