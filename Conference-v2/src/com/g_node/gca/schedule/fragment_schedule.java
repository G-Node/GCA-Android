package com.g_node.gca.schedule;

import java.util.ArrayList;
import java.util.List;

import com.shumail.newsroom.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class fragment_schedule extends Fragment {

	String LOG_TAG = "GCA-Schedule-Frag";

	private static List<ScheduleItemRecord> scheduleRecordsArray;
	private static List<EventScheduleItem> eventsRecordsArray;
	private static List<TrackScheduleItem> tracksRecordsArray;
	private static List<SessionScheduleItem> sessionRecordsArray;
	
	private List<ScheduleItemRecord> eventsForThisFragment = new ArrayList<ScheduleItemRecord>();
	
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
        
        TextView notificationNote = (TextView)getView().findViewById(R.id.schedulemain);
        
        Bundle bundle = this.getArguments();
        int myInt = bundle.getInt("tab");
        notificationNote.setText("Tab Number " + myInt);
        
        ListView ScheduleList = (ListView) getView().findViewById(R.id.ScheduleMainList);
		Log.i(LOG_TAG, "ScheduleList id got - layout got");
		
		scheduleAdapter adapter = new scheduleAdapter(getActivity(), eventsForThisFragment, eventsRecordsArray, tracksRecordsArray, sessionRecordsArray, getActivity());
		Log.i(LOG_TAG, "Adapter set - constructor initialized");
		ScheduleList.setAdapter(adapter);
           
	}
	
	 public static void setLists(List<ScheduleItemRecord> _items, List<EventScheduleItem> _eventsList, List<TrackScheduleItem> _tracksList, List<SessionScheduleItem> _sessionsList) {
		 Log.i("GCA-Error", "Setting Fragment Satic Lists");
		 scheduleRecordsArray = _items;
		 eventsRecordsArray = _eventsList;
		 tracksRecordsArray = _tracksList;
		 sessionRecordsArray = _sessionsList;
	 }

}