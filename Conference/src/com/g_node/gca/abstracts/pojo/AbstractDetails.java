package com.g_node.gca.abstracts.pojo;

public class AbstractDetails {

	private String uuid;
	private String topic;
	private String title;
	private String text;
	private String state;
	private int sortID;
	private String reasonForTalk;
	private String mtime;
	private String abstractType;
	private String doi;
	private String coi;
	private String acknowledgements;
	
	
	
	public AbstractDetails(String uuid, String topic, String title,
			String text, String state, int sortID, String reasonForTalk,
			String mtime, String abstractType, String doi,
			String coi, String acknowledgements) {
		super();
		this.uuid = uuid;
		this.topic = topic;
		this.title = title;
		this.text = text;
		this.state = state;
		this.sortID = sortID;
		this.reasonForTalk = reasonForTalk;
		this.mtime = mtime;
		this.abstractType = abstractType;
		this.doi = doi;
		this.coi = coi;
		this.acknowledgements = acknowledgements;
	}



	public String getUuid() {
		return uuid;
	}



	public void setUuid(String uuid) {
		this.uuid = uuid;
	}



	public String getTopic() {
		return topic;
	}



	public void setTopic(String topic) {
		this.topic = topic;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getText() {
		return text;
	}



	public void setText(String text) {
		this.text = text;
	}



	public String getState() {
		return state;
	}



	public void setState(String state) {
		this.state = state;
	}



	public int getSortID() {
		return sortID;
	}



	public void setSortID(int sortID) {
		this.sortID = sortID;
	}



	public String getReasonForTalk() {
		return reasonForTalk;
	}



	public void setReasonForTalk(String reasonForTalk) {
		this.reasonForTalk = reasonForTalk;
	}



	public String getMtime() {
		return mtime;
	}



	public void setMtime(String mtime) {
		this.mtime = mtime;
	}



	public String getAbstractType() {
		return abstractType;
	}



	public void setAbstractType(String abstractType) {
		this.abstractType = abstractType;
	}



	public String getDoi() {
		return doi;
	}



	public void setDoi(String doi) {
		this.doi = doi;
	}



	public String getCoi() {
		return coi;
	}



	public void setCoi(String coi) {
		this.coi = coi;
	}



	public String getAcknowledgements() {
		return acknowledgements;
	}



	public void setAcknowledgements(String acknowledgements) {
		this.acknowledgements = acknowledgements;
	}


}
