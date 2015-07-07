/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	
	public String get_formated_date() {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-M-dd").parse(group_date);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date.toString().substring(0, 10);
	}
	
}