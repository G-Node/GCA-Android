package com.g_node.gca.schedule;



public class ScheduleItemRecord {
	
	private String schedule_item_type;
	
	private int index;
	
	private String event_date;
	
	public ScheduleItemRecord(String schedule_item_type, int index, String event_date) {
		this.schedule_item_type = schedule_item_type;
		this.index = index;
		this.event_date = event_date;
	}

	public String getSchedule_item_type() {
		return schedule_item_type;
	}

	public void setSchedule_item_type(String schedule_item_type) {
		this.schedule_item_type = schedule_item_type;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getEvent_date() {
		return event_date;
	}

	public void setEvent_date(String event_date) {
		this.event_date = event_date;
	}
	
	
}