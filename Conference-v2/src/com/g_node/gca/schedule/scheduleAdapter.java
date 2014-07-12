package com.g_node.gca.schedule;

import java.util.List;

import com.shumail.newsroom.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class scheduleAdapter extends BaseAdapter {
	
	private String SCHEDULE_ITEMTYPE_EVENT = "event";
	private String SCHEDULE_ITEMTYPE_TRACK = "track";
	private String SCHEDULE_ITEMTYPE_SESSION = "session";
	
	private LayoutInflater inflater;
	private List<ScheduleItemRecord> scheduleItemsGeneralList;
	private List<EventScheduleItem> eventsRecordsList;
	
	public scheduleAdapter(Activity act, List<ScheduleItemRecord> _items, List<EventScheduleItem> _eventsList) {
		inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		scheduleItemsGeneralList = _items;
		eventsRecordsList = _eventsList;
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
		} else {
			
			vi = inflater.inflate(R.layout.schedule_list_events_general, null);
			
			if(vi==null ){
				Log.i("error", "null");
			}
			
			TextView x = (TextView) vi.findViewById(R.id.tvTime);
			x.setText(Integer.toString(y.getIndex() ));
			
			TextView xa = (TextView) vi.findViewById(R.id.tvTitle);
			xa.setText(y.getSchedule_item_type());
			
			return vi;
			
		}
	}
	
}