/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * License: BSD-3 (See LICENSE)
 */
package com.g_node.gca.schedule;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.g_node.ni17.R;


public class trackEventsAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<EventScheduleItem> eventsRecordsList;
	
	
	public trackEventsAdapter(Activity act, EventScheduleItem[] eventItems) {
		inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		eventsRecordsList = Arrays.asList(eventItems);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		Log.i("GCA-Events", "Size of EventsRecordsList: " + eventsRecordsList.size());
		return eventsRecordsList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return eventsRecordsList.get(arg0);
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
		
		EventScheduleItem tempEvent = eventsRecordsList.get(arg0);
		
		vi = inflater.inflate(R.layout.schedule_list_events_general, null);
		
		TextView x = (TextView) vi.findViewById(R.id.event_start_time);
		x.setText(tempEvent.getTitle() + tempEvent.getAuthors());
		
//		TextView xa = (TextView) vi.findViewById(R.id.tvTitle);
//		xa.setText(tempEvent.getAuthors());
		
		
		return vi;
	}
	
	
}