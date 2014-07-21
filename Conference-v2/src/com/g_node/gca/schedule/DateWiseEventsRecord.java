package com.g_node.gca.schedule;

import java.util.ArrayList;
import java.util.List;


public class DateWiseEventsRecord {
	
	private String group_date;
	
	List<Integer> events_for_this_date;

	public DateWiseEventsRecord(String group_date) {
		this.group_date = group_date;
		events_for_this_date = new ArrayList<Integer> ();
	}

	public String getGroup_date() {
		return group_date;
	}

	public void setGroup_date(String group_date) {
		this.group_date = group_date;
	}

	public List<Integer> getEvents_for_this_date() {
		return events_for_this_date;
	}

	public void setEvents_for_this_date(List<Integer> events_for_this_date) {
		this.events_for_this_date = events_for_this_date;
	}
	
	public void addEventsForDate(int event_index) {
		this.events_for_this_date.add(event_index);
	}
	
	
	
}