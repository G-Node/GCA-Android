package com.g_node.gca.schedule;

import java.io.Serializable;


public class SessionScheduleItem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String title;

    private String subtitle;
    
    private TrackScheduleItem[] tracksInSession;
    
   
    //Constructor
	public SessionScheduleItem(String title, String subtitle, int tracksInSession) {
		this.title = title;
		this.subtitle = subtitle;
		this.tracksInSession = new TrackScheduleItem[tracksInSession];
	}

	//Setters & Getters 
	
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

	public TrackScheduleItem[] getTracksInSession() {
		return tracksInSession;
	}
	
	public TrackScheduleItem getTracksInSession(int index) {
		return tracksInSession[index];
	}

	public void setTracksInSession(TrackScheduleItem[] tracksInSession) {
		this.tracksInSession = tracksInSession;
	}
	
	public void setTracksInSession(int index, TrackScheduleItem trackToAdd) {
		this.tracksInSession[index] = trackToAdd;
	}
    
    
	
}