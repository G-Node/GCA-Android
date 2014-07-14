package com.g_node.gca.schedule;

import com.shumail.newsroom.R;
import com.shumail.newsroom.R.layout;
import com.shumail.newsroom.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class ScheduleItemExtended extends Activity {
	
	private String SCHEDULE_ITEMTYPE_EVENT = "event";
	private String SCHEDULE_ITEMTYPE_TRACK = "track";
	private String SCHEDULE_ITEMTYPE_SESSION = "session";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle bundle = getIntent().getExtras();
		String eType = bundle.getString("type");
		if(eType.equals(SCHEDULE_ITEMTYPE_EVENT)) {
			
			setContentView(R.layout.activity_schedule_item_extended);
			
			EventScheduleItem eventToDisplay =  (EventScheduleItem) bundle.getSerializable("dEvent");
			
			Log.i("GCA-Schedule-List", "event title: " + eventToDisplay.getTitle());
			
			TextView eventTitleView = (TextView) findViewById(R.id.scheduleDetailTitle);
			eventTitleView.setText(eventToDisplay.getTitle());
		
		} else if (eType.equals(SCHEDULE_ITEMTYPE_TRACK)) {
		
			setContentView(R.layout.activity_schedule_item_extended);
			
			TrackScheduleItem trackToDisplay =  (TrackScheduleItem) bundle.getSerializable("dTrack");
			
			Log.i("GCA-Schedule-List", "Track title: " + trackToDisplay.getTitle());
			
			TextView eventTitleView = (TextView) findViewById(R.id.scheduleDetailTitle);
			eventTitleView.setText(trackToDisplay.getTitle() + trackToDisplay.getChair());
			
		
		} else {
			setContentView(R.layout.activity_schedule_item_extended);
			TextView eventTitleView = (TextView) findViewById(R.id.scheduleDetailTitle);
			eventTitleView.setText("asdasdasdasdas123");
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.schedule_item_extended, menu);
		return true;
	}

}
