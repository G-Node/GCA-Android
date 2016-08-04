/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.schedule;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.g_node.gcaa.R;


public class scheduleAdapter extends BaseAdapter {
	
	private String SCHEDULE_ITEMTYPE_EVENT = "event";
	private String SCHEDULE_ITEMTYPE_TRACK = "track";
	private String SCHEDULE_ITEMTYPE_SESSION = "session";
	
	private LayoutInflater inflater;
	private List<ScheduleItemRecord> scheduleItemsGeneralList;
	private List<EventScheduleItem> eventsRecordsList;
	private List<TrackScheduleItem> tracksRecordsList;
	private List<SessionScheduleItem> sessionsRecordList;
	private Context ctx;
	
	
	public scheduleAdapter(Activity act, List<ScheduleItemRecord> _items, List<EventScheduleItem> _eventsList, List<TrackScheduleItem> _tracksList, List<SessionScheduleItem> _sessionsList, Context _Ctx ) {
		inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		scheduleItemsGeneralList = _items;
		eventsRecordsList = _eventsList;
		tracksRecordsList = _tracksList;
		sessionsRecordList = _sessionsList;
		ctx = _Ctx;
	}

	@Override
	public int getCount() {
		return scheduleItemsGeneralList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return scheduleItemsGeneralList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View vi = arg1;
		ScheduleItemRecord currSchedItem = scheduleItemsGeneralList.get(arg0);
		if(currSchedItem.getSchedule_item_type().equals(SCHEDULE_ITEMTYPE_EVENT)) {
			Log.i("GCA-B-Schedule", "GetView called for EVENT");
			
			vi = inflater.inflate(R.layout.schedule_list_events_general, null);
			EventScheduleItem tempEvent = eventsRecordsList.get(
					currSchedItem.getIndex());
			
			inserEventData(vi, tempEvent);			
			return vi;
		
		} else if (currSchedItem.getSchedule_item_type().equals(SCHEDULE_ITEMTYPE_TRACK)) {	
			//if the schedule item is a Track, different layout and approach
			Log.i("GCA-B-Schedule", "GetView called for TRACK");
			
			//display track here now
			vi = inflater.inflate(R.layout.schedule_list_track, null);
			TextView x = (TextView) vi.findViewById(R.id.trackTitle);			
			int indexOfTrack = currSchedItem.getIndex();			
			TrackScheduleItem tempTrack = tracksRecordsList.get(indexOfTrack);			
			x.setText(tempTrack.getTitle());			
			if (tempTrack.getChair().length()>0){
				TextView xa = (TextView) vi.findViewById(R.id.trackSubtitle);
				xa.setText(ctx.getResources().getString(R.string.track_chair_label)
						+ tempTrack.getChair() );
			}
			EventScheduleItem[] tempTrackEvents = tempTrack.getEventsInTrack();
			TableLayout table = (TableLayout)vi.findViewById(R.id.trackEventsTable);
			
			for(int i=0; i<tempTrackEvents.length; i++) {				
				View tempRow = inflater.inflate(R.layout.schedule_list_events_general, null);
				inserEventData(tempRow, tempTrackEvents[i]);
				table.addView(tempRow);
			}
			table.requestLayout();
			
			return vi;
			
		} else {
			Log.i("GCA-B-Schedule", "GetView called for SESSION");		
			vi = inflater.inflate(R.layout.schedule_list_session, null);
			SessionScheduleItem currSession = sessionsRecordList.get(
					currSchedItem.getIndex());			
			((TextView) vi.findViewById(R.id.sessionTitle)).setText(
					currSession.getTitle());
			((TextView) vi.findViewById(R.id.sessionSubtitle)).
			setText(currSession.getSubtitle() );
			
			TrackScheduleItem[] currSessionTracks = currSession.getTracksInSession();						
			TableLayout table = (TableLayout)vi.findViewById(R.id.sessionTracksTable);			
			
			Log.i("GCA-A-Schedule", "Session Tracks Count: " + currSessionTracks.length);
			//add tracks to table
			for(int i=0; i<currSessionTracks.length; i++) {				
				TableRow sessionRow = (TableRow) inflater.inflate(
						R.layout.session_track_table_row, null);
				((TextView)sessionRow.findViewById(R.id.session_track_name)).
				setText(currSessionTracks[i].getTitle());
				if (currSessionTracks[i].getChair().length() > 0){
						((TextView)sessionRow.findViewById(R.id.session_track_chair)).
						setText(ctx.getResources().getString(
								R.string.track_chair_label) + 
								currSessionTracks[i].getChair());
				}
				
				//here add events of respective track in another table
				EventScheduleItem[] eventsInCurrentTrack = currSessionTracks[i].
						getEventsInTrack();
				TableLayout trackEventsTable = (TableLayout)sessionRow.
						findViewById(R.id.session_track_events_table);				
				Log.i("GCA-A-Schedule", "Track Events Count: " + 
						eventsInCurrentTrack.length);				
				for(int j=0; j<eventsInCurrentTrack.length; j++) {
					//adding each event into a this table
					View trackRow = inflater.inflate(
							R.layout.schedule_list_events_general, null);
					inserEventData(trackRow, eventsInCurrentTrack[j]);
					trackEventsTable.addView(trackRow);
					trackEventsTable.requestLayout();
				}
								
				//adding the final track row
				table.addView(sessionRow);
			}
			table.requestLayout();
			
			
			return vi;
			
		} //end if/else
		
	} //end getView

	private void inserEventData(View currView, EventScheduleItem currEvent) {
		((TextView)currView.findViewById(R.id.event_start_time)).
		setText(currEvent.getStart());
		((TextView)currView.findViewById(R.id.event_end_time)).
		setText(currEvent.getEnd());
		((TextView)currView.findViewById(R.id.event_location)).
		setText(currEvent.getLocation());
		((TextView)currView.findViewById(R.id.event_title)).
		setText(currEvent.getTitle());
		if(!currEvent.getAuthors().equals("")) 
			((TextView)currView.findViewById(R.id.event_location)).
			append(Html.fromHtml("&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;<i>" + 
			currEvent.getAuthors() + "</i>" ));
		
		if (currEvent.getType().equals("food")){
			currView.findViewById(R.id.list_item_middle_container).setBackgroundColor(
					ctx.getResources().getColor(R.color.color_food));
		}
		
		if (currEvent.getType().equals("keynote")){
			TextView eventTypeTV = (TextView) currView.findViewById(R.id.event_type);
			eventTypeTV.setVisibility(View.VISIBLE);
			eventTypeTV.setText(currView.getResources().getString(
					R.string.schedule_event_keynote));				
		}
		currView.setOnClickListener(new ScheduleEventOnClickListener(currEvent));
	}

	private class ScheduleEventOnClickListener implements OnClickListener
	{

	  EventScheduleItem event ;
	  public ScheduleEventOnClickListener(EventScheduleItem event ) {
	       this.event = event;
	  }

	  @Override
	  public void onClick(View v){		  
		  Intent intent = new Intent(v.getContext(), ScheduleItemExtended.class);		
		  Bundle bundle = new Bundle();
		  bundle.putSerializable("dEvent", event);
		  bundle.putString("type", "event");		
		  intent.putExtras(bundle);
		  v.getContext().startActivity(intent);
	  }

	};
}//end class

