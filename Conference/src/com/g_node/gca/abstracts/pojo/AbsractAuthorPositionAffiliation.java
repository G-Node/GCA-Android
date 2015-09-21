package com.g_node.gca.abstracts.pojo;

public class AbsractAuthorPositionAffiliation {

	
	private String abstract_uuid;
	private String author_uuid;
	private int author_position;
	private String author_affiliation;
	
	
	
	public AbsractAuthorPositionAffiliation(String abstract_uuid,
			String author_uuid, int author_position, String author_affiliation) {
		super();
		this.abstract_uuid = abstract_uuid;
		this.author_uuid = author_uuid;
		this.author_position = author_position;
		this.author_affiliation = author_affiliation;
	}



	public String getAbstract_uuid() {
		return abstract_uuid;
	}



	public void setAbstract_uuid(String abstract_uuid) {
		this.abstract_uuid = abstract_uuid;
	}



	public String getAuthor_uuid() {
		return author_uuid;
	}



	public void setAuthor_uuid(String author_uuid) {
		this.author_uuid = author_uuid;
	}



	public int getAuthor_position() {
		return author_position;
	}



	public void setAuthor_position(int author_position) {
		this.author_position = author_position;
	}



	public String getAuthor_affiliation() {
		return author_affiliation;
	}



	public void setAuthor_affiliation(String author_affiliation) {
		this.author_affiliation = author_affiliation;
	}


}
