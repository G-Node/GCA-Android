package com.g_node.gca.schedule;



public class ScheduleItemRecord {
	
	private String schedule_item_type;
	
	private int index;
	
	public ScheduleItemRecord(String schedule_item_type, int index) {
		this.schedule_item_type = schedule_item_type;
		this.index = index;
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
	
	
}