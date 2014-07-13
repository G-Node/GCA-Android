package com.g_node.gca.schedule;

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
		
		View vi = arg1;
		ScheduleItemRecord y = scheduleItemsGeneralList.get(arg0);
		
		if(y.getSchedule_item_type().equals(SCHEDULE_ITEMTYPE_EVENT)) {
			
			vi = inflater.inflate(R.layout.schedule_list_events_general, null);
			
			if(vi==null ){
				Log.i("error", "null");
			}
			
			TextView x = (TextView) vi.findViewById(R.id.tvTime);
			int indexOfEvent =y.getIndex();
			
			EventScheduleItem tempEvent = eventsRecordsList.get(indexOfEvent);
			
			x.setText(tempEvent.getStart());
			
			TextView xa = (TextView) vi.findViewById(R.id.tvTitle);
			xa.setText(tempEvent.getTitle());
			
			return vi;
		
		} else if (y.getSchedule_item_type().equals(SCHEDULE_ITEMTYPE_TRACK)) {
			
			//display track here now
			vi = inflater.inflate(R.layout.schedule_list_track, null);
			
			if(vi==null ){
				Log.i("error", "null");
			}
			
			TextView x = (TextView) vi.findViewById(R.id.trackTitle);
			
			int indexOfTrack = y.getIndex();
			
			TrackScheduleItem tempTrack = tracksRecordsList.get(indexOfTrack);
			
			x.setText(tempTrack.getTitle());
			
			TextView xa = (TextView) vi.findViewById(R.id.trackSubtitle);
			xa.setText("Chaired By: " + tempTrack.getChair() + " (" + Integer.toString(y.getIndex() ) + ")");
			
			//ListView trackEventsListView = (ListView) vi.findViewById(R.id.trackInsideItemListView);
			
			EventScheduleItem[] tempTrackEvents = tempTrack.getEventsInTrack();
			
			
			TableLayout table = (TableLayout)vi.findViewById(R.id.trackEventsTable);
			
			for(int i=0; i<tempTrackEvents.length; i++) {
				
				TableRow tempRow = (TableRow) LayoutInflater.from(ctx).inflate(R.layout.track_events_table_row, null);
				((TextView)tempRow.findViewById(R.id.attrib_name)).setText(tempTrackEvents[i].getTitle());
				((TextView)tempRow.findViewById(R.id.attrib_value)).setText(tempTrackEvents[i].getStart());
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
			
			vi = inflater.inflate(R.layout.schedule_list_events_general, null);
			
			if(vi==null ){
				Log.i("error", "null");
			}
			
			TextView x = (TextView) vi.findViewById(R.id.tvTime);
			
			int indexOfSession = y.getIndex();
			
			SessionScheduleItem tempSession = sessionsRecordList.get(indexOfSession);
			
			x.setText(tempSession.getSubtitle());
			
			TextView xa = (TextView) vi.findViewById(R.id.tvTitle);
			xa.setText(tempSession.getTitle() + " (" + Integer.toString(y.getIndex() ) + ")");
			
			return vi;
			
		}
	}
	
}