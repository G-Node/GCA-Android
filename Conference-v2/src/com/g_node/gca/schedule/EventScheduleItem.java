package com.g_node.gca.schedule;

import java.io.Serializable;


public class EventScheduleItem implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String title;

    private String subtitle;

    private String start;

	private String end;

    private String location;
    
    private String date;
    
    private String authors;
    
    private String type;
    
    private String eventAbstract;
    
    public EventScheduleItem(String title, String subtitle, String start, String end,
			String location, String date, String authors, String type,
			String eventAbstract) {

		this.title = title;
		this.subtitle = subtitle;
		this.start = start;
		this.end = end;
		this.location = location;
		this.date = date;
		this.authors = authors;
		this.type = type;
		this.eventAbstract = eventAbstract;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEventAbstract() {
		return eventAbstract;
	}

	public void setEventAbstract(String eventAbstract) {
		this.eventAbstract = eventAbstract;
	}
    	
    
}