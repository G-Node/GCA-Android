package com.g_node.gca.schedule;

import com.g_node.gca.abstracts.AbstractContent;
import com.g_node.gca.abstracts.DatabaseHelper;
import com.shumail.newsroom.R;
import com.shumail.newsroom.R.layout;
import com.shumail.newsroom.R.menu;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ScheduleItemExtended extends Activity {
	
	private String SCHEDULE_ITEMTYPE_EVENT = "event";
	private String SCHEDULE_ITEMTYPE_TRACK = "track";
	private String SCHEDULE_ITEMTYPE_SESSION = "session";
	
	DatabaseHelper dbHelper = new DatabaseHelper(this);;
	
	String event_abstract_uuid;
	
	ActionBar actionbar;
	
	String LOG_TAG = "GCA-Sch-Extend";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		dbHelper.open();
		
		Bundle bundle = getIntent().getExtras();
		String eType = bundle.getString("type");
		if(eType.equals(SCHEDULE_ITEMTYPE_EVENT)) {
			
			setContentView(R.layout.activity_schedule_item_extended_event);
			
			EventScheduleItem eventToDisplay =  (EventScheduleItem) bundle.getSerializable("dEvent");
			
			actionbar = getActionBar();
			actionbar.setTitle(eventToDisplay.getTitle());
			
			Log.i("GCA-Schedule-List", "event title: " + eventToDisplay.getTitle());
			
			TextView eventTitleView = (TextView) findViewById(R.id.schedule_event_title);
			eventTitleView.setText(eventToDisplay.getTitle());
			
			((TextView) findViewById(R.id.schedule_event_subtitle)).setText(eventToDisplay.getSubtitle());
			((TextView) findViewById(R.id.schedule_event_author)).setText(eventToDisplay.getAuthors());
			((TextView) findViewById(R.id.schedule_event_time)).setText(eventToDisplay.getStart() + "   -   " + eventToDisplay.getEnd());
			((TextView) findViewById(R.id.schedule_event_location)).setText(eventToDisplay.getLocation());
			((TextView) findViewById(R.id.schedule_event_date)).setText(eventToDisplay.getDate());
			((TextView) findViewById(R.id.schedule_event_type)).setText(eventToDisplay.getType().toUpperCase());
			
			
			event_abstract_uuid = eventToDisplay.getEventAbstract();
			event_abstract_uuid = event_abstract_uuid.substring(event_abstract_uuid.lastIndexOf("/")+1, event_abstract_uuid.length());
			Log.i(LOG_TAG, "Abstract ID of event: " + event_abstract_uuid);
			
	        String query = "SELECT UUID , TOPIC, TITLE, ABSRACT_TEXT, STATE, SORTID, REASONFORTALK, MTIME, TYPE, DOI, COI, ACKNOWLEDGEMENTS FROM ABSTRACT_DETAILS where UUID = '" + event_abstract_uuid + "';";
			
			Cursor abstractForEventCursor = DatabaseHelper.database.rawQuery(query, null);
			
			Button btnOpenAbstract = (Button) findViewById(R.id.btn_launch_Abstract_from_event);
			
			if(abstractForEventCursor.getCount() < 1) {
				btnOpenAbstract.setEnabled(false);
				btnOpenAbstract.setText("No Abstract Found");
				//btnOpenAbstract.setVisibility(View.GONE);
			} else {
				
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
			
			actionbar = getActionBar();
			actionbar.setTitle(trackToDisplay.getTitle());
			
			Log.i("GCA-Schedule-List", "Track title: " + trackToDisplay.getTitle());
			
			((TextView) findViewById(R.id.schedule_track_title)).setText(trackToDisplay.getTitle());
			((TextView) findViewById(R.id.schedule_track_subtitle)).setText(trackToDisplay.getSubtitle());			
			((TextView) findViewById(R.id.schedule_track_chair)).setText("Chair:   " + trackToDisplay.getChair());
//			
		
		} else {
			setContentView(R.layout.activity_schedule_item_extended_event);
//			TextView eventTitleView = (TextView) findViewById(R.id.scheduleDetailTitle);
//			eventTitleView.setText("asdasdasdasdas123");
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.schedule_item_extended, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		dbHelper.close();
		super.onDestroy();
	}
	


}
