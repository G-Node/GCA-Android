package com.g_node.gca.schedule;


public class SessionScheduleItem {
	
	private String title;

    private String subtitle;
    
    private TrackScheduleItem[] tracksInSession;
    
   
    //Constructor
	public SessionScheduleItem(String title, String subtitle) {
		this.title = title;
		this.subtitle = subtitle;
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

	public void setTracksInSession(TrackScheduleItem[] tracksInSession) {
		this.tracksInSession = tracksInSession;
	}
    
    
	
}