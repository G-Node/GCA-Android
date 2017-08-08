/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.schedule;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.g_node.ni12017.R;

public class fragment_schedule extends Fragment {

	String LOG_TAG = "GCA-Schedule-Frag";
	
	private String SCHEDULE_ITEMTYPE_EVENT = "event";
	private String SCHEDULE_ITEMTYPE_TRACK = "track";
	private String SCHEDULE_ITEMTYPE_SESSION = "session";
	
	private static List<EventScheduleItem> eventsRecordsArray;
	private static List<TrackScheduleItem> tracksRecordsArray;
	private static List<SessionScheduleItem> sessionRecordsArray;
	
	private List<ScheduleItemRecord> eventsForThisFragment = new ArrayList<ScheduleItemRecord>();
	
	//this function will update the list of events for this fragment only
	public void setEventsForThisFragment(List<ScheduleItemRecord> temp) {
		eventsForThisFragment = temp;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(LOG_TAG, "Schedule Fragment - onCreateView");
		View rootView = inflater.inflate(R.layout.fragment_schedule_layout, container, false);
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        Log.i("GCA-Error", "onViewCreaed Called");
        
//        TextView notificationNote = (TextView)getView().findViewById(R.id.schedulemain);
//        
        Bundle bundle = this.getArguments();
        int dayNumber = bundle.getInt("tab");
        Log.i(LOG_TAG, "Current Tab: " + ++dayNumber);
        //        notificationNote.setText("Day : " + ++dayNumber);
        
        ListView ScheduleList = (ListView) getView().findViewById(R.id.ScheduleMainList);
		Log.i(LOG_TAG, "ScheduleList id got - layout got");
		
		scheduleAdapter adapter = new scheduleAdapter(getActivity(), eventsForThisFragment, eventsRecordsArray, tracksRecordsArray, sessionRecordsArray, getActivity());
		Log.i(LOG_TAG, "Adapter set - constructor initialized");
		ScheduleList.setAdapter(adapter);
		
		//List Item Click Listener
		
		ScheduleList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});		
           
	}
	
	 public static void setLists(List<EventScheduleItem> _eventsList, List<TrackScheduleItem> _tracksList, List<SessionScheduleItem> _sessionsList) {
		 Log.i("GCA-Error", "Setting Fragment Satic Lists");
		 eventsRecordsArray = _eventsList;
		 tracksRecordsArray = _tracksList;
		 sessionRecordsArray = _sessionsList;
	 }

}