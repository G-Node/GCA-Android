package com.g_node.gca.schedule;

import java.util.ArrayList;
import java.util.List;

import com.shumail.newsroom.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


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
	private Activity activity;
	List<String> diffDates = new ArrayList<String>() ;
	List<Integer> viewsRendered = new ArrayList<Integer>();
	
	
	public scheduleAdapter(Activity act, List<ScheduleItemRecord> _items, List<EventScheduleItem> _eventsList, List<TrackScheduleItem> _tracksList, List<SessionScheduleItem> _sessionsList, Context _Ctx ) {
		inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		scheduleItemsGeneralList = _items;
		eventsRecordsList = _eventsList;
		tracksRecordsList = _tracksList;
		sessionsRecordList = _sessionsList;
		ctx = _Ctx;
		activity = act;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return scheduleItemsGeneralList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return scheduleItemsGeneralList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Log.i("GCA-B-Schedule", "Line 70");
		View vi = arg1;
		ScheduleItemRecord y = scheduleItemsGeneralList.get(arg0);
		
		if(y.getSchedule_item_type().equals(SCHEDULE_ITEMTYPE_EVENT)) {
			Log.i("GCA-B-Schedule", "GetView called for EVENT");
			
			vi = inflater.inflate(R.layout.schedule_list_events_general, null);
			
			Log.i("Conflict", "abi event date: " + y.getEvent_date());
			Log.i("Conflict", "abi DiffList : " + diffDates.toString());
			
			if(diffDates.contains( y.getEvent_date() )) { //if date is already present
				
				if(!viewsRendered.contains(arg0)) {  //check if view is not already rendered
					//if date is already present
					TextView x = (TextView) vi.findViewById(R.id.date_header);
					x.setVisibility(View.GONE);
				} else {
					;
				}
				
			} else {
				if(!viewsRendered.contains(arg0)) {
					diffDates.add(y.getEvent_date());
					//set header
					TextView x = (TextView) vi.findViewById(R.id.date_header);
					x.setText(y.getEvent_date());
				} else {
					;
				}
			}
			
			if(vi==null ){
				Log.i("error", "null");
			}
			
			TextView x = (TextView) vi.findViewById(R.id.event_start_time);
			int indexOfEvent =y.getIndex();
			
			EventScheduleItem tempEvent = eventsRecordsList.get(indexOfEvent);
			
			x.setText(tempEvent.getStart());
			
			TextView xa = (TextView) vi.findViewById(R.id.event_title);
			xa.setText(tempEvent.getTitle());
			
			((TextView)vi.findViewById(R.id.event_end_time)).setText(tempEvent.getEnd());
			((TextView)vi.findViewById(R.id.event_location)).setText(tempEvent.getLocation());
			
			if(!viewsRendered.contains(arg0)) {
				viewsRendered.add(arg0);
			}
			
			return vi;
		
		} else if (y.getSchedule_item_type().equals(SCHEDULE_ITEMTYPE_TRACK)) {
			
			Log.i("GCA-B-Schedule", "GetView called for TRACK");
			
			//display track here now
			vi = inflater.inflate(R.layout.schedule_list_track, null);
			
			if(diffDates.contains( y.getEvent_date() )) {
				//if date is already present
				TextView x = (TextView) vi.findViewById(R.id.track_date_header);
				x.setVisibility(View.GONE);
				
			} else {
				diffDates.add(y.getEvent_date());
				//set header
				TextView x = (TextView) vi.findViewById(R.id.track_date_header);
				x.setText(y.getEvent_date());
				
			}
			
			if(vi==null ){
				Log.i("error", "null");
			}
			
			TextView x = (TextView) vi.findViewById(R.id.trackTitle);
			
			int indexOfTrack = y.getIndex();
			
			TrackScheduleItem tempTrack = tracksRecordsList.get(indexOfTrack);
			
			x.setText(tempTrack.getTitle());
			
			TextView xa = (TextView) vi.findViewById(R.id.trackSubtitle);
			xa.setText("Chaired By: " + tempTrack.getChair() );
			
			//ListView trackEventsListView = (ListView) vi.findViewById(R.id.trackInsideItemListView);
			
			EventScheduleItem[] tempTrackEvents = tempTrack.getEventsInTrack();
			
			
			TableLayout table = (TableLayout)vi.findViewById(R.id.trackEventsTable);
			
			for(int i=0; i<tempTrackEvents.length; i++) {
				
				TableRow tempRow = (TableRow) LayoutInflater.from(ctx).inflate(R.layout.track_events_table_row, null);
				((TextView)tempRow.findViewById(R.id.track_event_start)).setText(tempTrackEvents[i].getStart());
				((TextView)tempRow.findViewById(R.id.track_event_title)).setText(tempTrackEvents[i].getTitle());
				((TextView)tempRow.findViewById(R.id.track_event_location)).setText(tempTrackEvents[i].getLocation());
				((TextView)tempRow.findViewById(R.id.track_Event_end)).setText(tempTrackEvents[i].getEnd());
				table.addView(tempRow);
			}
			table.requestLayout();
//			trackEventsAdapter tempTrackEventsAdapterl = new trackEventsAdapter(activity, tempTrackEvents);
//			
//			trackEventsListView.setAdapter(tempTrackEventsAdapterl);
//			
//			int contentHeight = trackEventsListView.getHeight();
//			Log.i("GCA-Events", "Height of Listview of Tracks: " + contentHeight);
//			
//			ListAdapter LvAdapter = trackEventsListView.getAdapter();
//		    int listviewElementsheight = 0;
//		    for (int i = 0; i < LvAdapter.getCount(); i++) {
//		        View mView = LvAdapter.getView(i, null, trackEventsListView);
//		        mView.measure(
//		                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
//		                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//		        listviewElementsheight += mView.getMeasuredHeight();
//		    }
//		    
//
//			Log.i("GCA-Events", "Height of Listview of Tracks: " + listviewElementsheight);
//			
//			contentHeight = listviewElementsheight;
//			
//			LayoutParams lp = trackEventsListView.getLayoutParams();
//			lp.height = contentHeight + 30;
//			
//			trackEventsListView.setLayoutParams(lp);
//			
			return vi;
			
		} else {
			Log.i("GCA-B-Schedule", "GetView called for SESSION");
			Log.i("GCA-A-Schedule", "Line 164");
			
			vi = inflater.inflate(R.layout.schedule_list_session, null);
			
			if(diffDates.contains( y.getEvent_date() )) {
				//if date is already present
				TextView x = (TextView) vi.findViewById(R.id.session_date_header);
				x.setVisibility(View.GONE);
				
			} else {
				diffDates.add(y.getEvent_date());
				//set header
				TextView x = (TextView) vi.findViewById(R.id.session_date_header);
				x.setText(y.getEvent_date());
				
			}
			
			if(vi==null ){
				Log.i("error", "null");
			}
			Log.i("GCA-A-Schedule", "Line 170");
			TextView sessionTitle = (TextView) vi.findViewById(R.id.sessionTitle);
			
			int indexOfSession = y.getIndex();
			
			SessionScheduleItem tempSession = sessionsRecordList.get(indexOfSession);
			
			sessionTitle.setText(tempSession.getTitle());
			
			TextView sessionSubtitle = (TextView) vi.findViewById(R.id.sessionSubtitle);
			sessionSubtitle.setText(tempSession.getSubtitle() );
			
			TrackScheduleItem[] tempSessionTracks = tempSession.getTracksInSession();
			
			//EventScheduleItem[] tempS = tempTrack.getEventsInTrack();
			
			TableLayout table = (TableLayout)vi.findViewById(R.id.sessionTracksTable);
			
			Log.i("GCA-A-Schedule", "Session Tracks Count: " + tempSessionTracks.length);
			
			//add tracks to table
			for(int i=0; i<tempSessionTracks.length; i++) {
				
				Log.i("GCA-A-Schedule", "in outer Loop no: " + i);
				
				TableRow tempRow = (TableRow) inflater.inflate(R.layout.session_track_table_row, null);
				((TextView)tempRow.findViewById(R.id.session_track_name)).setText(tempSessionTracks[i].getTitle());
				((TextView)tempRow.findViewById(R.id.session_track_chair)).setText("Chaired by: " + tempSessionTracks[i].getChair());
				
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
					((TextView)tempEventRowForTrackEventsTable.findViewById(R.id.session_track_event_start)).setText(eventsInCurrentTrack[j].getStart());
					Log.i("GCA-A-Schedule", "Event title shit: " + eventsInCurrentTrack[j].getTitle());
					((TextView)tempEventRowForTrackEventsTable.findViewById(R.id.session_track_Event_end)).setText(eventsInCurrentTrack[j].getEnd());
					((TextView)tempEventRowForTrackEventsTable.findViewById(R.id.session_track_event_title)).setText(eventsInCurrentTrack[j].getTitle());
					((TextView)tempEventRowForTrackEventsTable.findViewById(R.id.session_track_event_location)).setText(eventsInCurrentTrack[j].getLocation());
					
					//Adding the event row to Tracks
					trackEventstable.addView(tempEventRowForTrackEventsTable);
					trackEventstable.requestLayout();
				}
				
			//	Log.i("GCA-Schedule", "Count of Child in inside table:" + trackEventstable.getChildCount());
				
				//adding the final track row
				table.addView(tempRow);
			}
			table.requestLayout();
			
			
			return vi;
			
		} //end last else
		
	}
	
}