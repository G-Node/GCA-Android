/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.schedule;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class SchedulePagerAdapter extends FragmentStatePagerAdapter {
	
	List<ScheduleItemRecord> scheduleRecordsArray;
	List<EventScheduleItem> eventsRecordsArray;
	List<TrackScheduleItem> tracksRecordsArray;
	List<SessionScheduleItem> sessionRecordsArray;
	List<DateWiseEventsRecord> dateWiseEventsRecordList;

	public SchedulePagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public void setLists(List<ScheduleItemRecord> _items, List<EventScheduleItem> _eventsList, List<TrackScheduleItem> _tracksList, List<SessionScheduleItem> _sessionsList, List<DateWiseEventsRecord> _dateWiseEventsRecordList) {
		this.scheduleRecordsArray = _items;
		this.eventsRecordsArray = _eventsList;
		this.tracksRecordsArray = _tracksList;
		this.sessionRecordsArray = _sessionsList;
		this.dateWiseEventsRecordList = _dateWiseEventsRecordList;
	}

	@Override
	public Fragment getItem(int arg0) {
		Log.i("GCA-Schedule-Fragment", "getItem called for tab: " + arg0);
		
		DateWiseEventsRecord groupedEventRecordForThisFragment = dateWiseEventsRecordList.get(arg0);
		
		List<Integer> indexesOfEventsForThisDateTab = groupedEventRecordForThisFragment.getEvents_for_this_date();

		
		List<ScheduleItemRecord> eventsForThisFragment = new ArrayList<ScheduleItemRecord>();
		
		for(int k=0; k<indexesOfEventsForThisDateTab.size(); k++){
			int indexToGetData = indexesOfEventsForThisDateTab.get(k);
			ScheduleItemRecord eventObjectGot = scheduleRecordsArray.get(indexToGetData);
			eventsForThisFragment.add(eventObjectGot);
		}
		 
		fragment_schedule.setLists(eventsRecordsArray, tracksRecordsArray, sessionRecordsArray);
	
		Bundle bundle = new Bundle();
		bundle.putInt("tab", arg0);
		
		fragment_schedule x = new fragment_schedule();
		x.setEventsForThisFragment(eventsForThisFragment);
		x.setArguments(bundle);
		
		return x;
	}

	@Override
	public int getCount() {
		Log.i("GCA-Schedule-Fragment", "getcount called");
		// TODO Auto-generated method stub
		return dateWiseEventsRecordList.size();
	}
	
}