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
			
			if(vi==null ){
				Log.i("error", "null");
			}
			TextView x = (TextView) vi.findViewById(R.id.event_start_time);
			int indexOfEvent = currSchedItem.getIndex();
			
			EventScheduleItem tempEvent = eventsRecordsList.get(indexOfEvent);
			
			x.setText(tempEvent.getStart());
			
			TextView xa = (TextView) vi.findViewById(R.id.event_title);
			xa.setText(tempEvent.getTitle());
			
			((TextView)vi.findViewById(R.id.event_end_time)).setText(tempEvent.getEnd());
			((TextView)vi.findViewById(R.id.event_location)).setText(tempEvent.getLocation());
			
			if(!tempEvent.getAuthors().equals("")) 
				((TextView)vi.findViewById(R.id.event_location)).append(Html.fromHtml("&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;<i>" + tempEvent.getAuthors() + "</i>" ));
			
			if (tempEvent.getType().equals("food")){
				vi.findViewById(R.id.list_item_middle_container).setBackgroundColor(
						ctx.getResources().getColor(R.color.color_food));
			}			
			vi.setOnClickListener(new ScheduleEventOnClickListener(tempEvent));
			return vi;
		
		} else if (currSchedItem.getSchedule_item_type().equals(SCHEDULE_ITEMTYPE_TRACK)) {	
			//if the schedule item is a Track, different layout and approach
			Log.i("GCA-B-Schedule", "GetView called for TRACK");
			
			//display track here now
			vi = inflater.inflate(R.layout.schedule_list_track, null);
			
			if(vi==null ){
				Log.i("error", "null");
			}
			
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
				TableRow tempRow = (TableRow) LayoutInflater.from(ctx).
						inflate(R.layout.track_events_table_row, null);
				((TextView)tempRow.findViewById(R.id.track_event_start)).
				setText(tempTrackEvents[i].getStart());
				((TextView)tempRow.findViewById(R.id.track_event_title)).
				setText(tempTrackEvents[i].getTitle());
				((TextView)tempRow.findViewById(R.id.track_event_location)).
				setText(tempTrackEvents[i].getLocation());
				((TextView)tempRow.findViewById(R.id.track_Event_end)).
				setText(tempTrackEvents[i].getEnd());
				
				if(!tempTrackEvents[i].getAuthors().equals("")) 
					((TextView)tempRow.findViewById(R.id.track_event_location)).
					append(Html.fromHtml("&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;<i>" + 
					tempTrackEvents[i].getAuthors() + "</i>"));
				
				if (tempTrackEvents[i].getType().equals("food")){
					tempRow.findViewById(R.id.list_item_middle_container).setBackgroundColor(
							ctx.getResources().getColor(R.color.color_food));
				}
				tempRow.setOnClickListener(new ScheduleEventOnClickListener(tempTrackEvents[i]));
				table.addView(tempRow);
			}
			table.requestLayout();
			
			return vi;
			
		} else {
			Log.i("GCA-B-Schedule", "GetView called for SESSION");		
			vi = inflater.inflate(R.layout.schedule_list_session, null);
			
			if(vi==null ){
				Log.i("error", "null");
			}
			TextView sessionTitle = (TextView) vi.findViewById(R.id.sessionTitle);
			
			int indexOfSession = currSchedItem.getIndex();
			
			SessionScheduleItem tempSession = sessionsRecordList.get(indexOfSession);
			
			sessionTitle.setText(tempSession.getTitle());
			
			TextView sessionSubtitle = (TextView) vi.findViewById(R.id.sessionSubtitle);
			sessionSubtitle.setText(tempSession.getSubtitle() );
			
			TrackScheduleItem[] tempSessionTracks = tempSession.getTracksInSession();
						
			TableLayout table = (TableLayout)vi.findViewById(R.id.sessionTracksTable);
			
			Log.i("GCA-A-Schedule", "Session Tracks Count: " + tempSessionTracks.length);
			
			//add tracks to table
			for(int i=0; i<tempSessionTracks.length; i++) {				
				Log.i("GCA-A-Schedule", "in outer Loop no: " + i);				
				TableRow tempRow = (TableRow) inflater.inflate(R.layout.session_track_table_row, null);
				((TextView)tempRow.findViewById(R.id.session_track_name)).
				setText(tempSessionTracks[i].getTitle());
				if (tempSessionTracks[i].getChair().length() > 0){
						((TextView)tempRow.findViewById(R.id.session_track_chair)).
						setText(ctx.getResources().getString(R.string.track_chair_label) + tempSessionTracks[i].getChair());
				}
				//here add events of respective track in another table
				EventScheduleItem[] eventsInCurrentTrack = tempSessionTracks[i].getEventsInTrack();
				
				TableLayout trackEventstable = (TableLayout)tempRow.findViewById(R.id.session_track_events_table);
				
				Log.i("GCA-A-Schedule", "Track Events Count: " + eventsInCurrentTrack.length);
				
				for(int j=0; j<eventsInCurrentTrack.length; j++) {
					Log.i("GCA-A-Schedule", "in loop: " + j);
					//adding each event into a this table
					TableRow tempEventRowForTrackEventsTable = (TableRow) inflater.inflate(R.layout.session_track_event_table_row, null);
					if(tempEventRowForTrackEventsTable == null){
						Log.i("GCA-Schedule", "NULL SCENE");
					}
					((TextView)tempEventRowForTrackEventsTable.findViewById(
							R.id.session_track_event_start)).
							setText(eventsInCurrentTrack[j].getStart());
					((TextView)tempEventRowForTrackEventsTable.findViewById(
							R.id.session_track_Event_end)).setText(
									eventsInCurrentTrack[j].getEnd());
					((TextView)tempEventRowForTrackEventsTable.findViewById(
							R.id.session_track_event_title)).setText(
									eventsInCurrentTrack[j].getTitle());
					((TextView)tempEventRowForTrackEventsTable.findViewById(
							R.id.session_track_event_location)).setText(
									eventsInCurrentTrack[j].getLocation());
					
					if(!eventsInCurrentTrack[j].getAuthors().equals("")) 
						((TextView)tempEventRowForTrackEventsTable.findViewById(
								R.id.session_track_event_location)).append(
										Html.fromHtml("&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;<i>" 
								+ eventsInCurrentTrack[j].getAuthors() + "</i>"));
					
					if (eventsInCurrentTrack[j].getType().equals("food")){
						tempRow.findViewById(R.id.list_item_middle_container).setBackgroundColor(
								ctx.getResources().getColor(R.color.color_food));
					}
					tempEventRowForTrackEventsTable.setOnClickListener(
							new ScheduleEventOnClickListener(eventsInCurrentTrack[j]));
					//Adding the event row to Tracks
					trackEventstable.addView(tempEventRowForTrackEventsTable);
					trackEventstable.requestLayout();
				}
								
				//adding the final track row
				table.addView(tempRow);
			}
			table.requestLayout();
			
			
			return vi;
			
		} //end if/else
		
	} //end getView

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

