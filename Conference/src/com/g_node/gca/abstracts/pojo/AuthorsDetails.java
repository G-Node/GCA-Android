package com.g_node.gca.abstracts.pojo;

public class AuthorsDetails {

	private String author_uuid;
	private String author_fName;
	private String author_lName;
	private String author_middleName;
	private String author_email;

	
	
	
	public AuthorsDetails(String author_uuid, String author_fName,
			String author_lName, String author_middleName, String author_email) {
		super();
		this.author_uuid = author_uuid;
		this.author_fName = author_fName;
		this.author_lName = author_lName;
		this.author_middleName = author_middleName;
		this.author_email = author_email;
	}




	public String getAuthor_uuid() {
		return author_uuid;
	}




	public void setAuthor_uuid(String author_uuid) {
		this.author_uuid = author_uuid;
	}




	public String getAuthor_fName() {
		return author_fName;
	}




	public void setAuthor_fName(String author_fName) {
		this.author_fName = author_fName;
	}




	public String getAuthor_lName() {
		return author_lName;
	}




	public void setAuthor_lName(String author_lName) {
		this.author_lName = author_lName;
	}




	public String getAuthor_middleName() {
		return author_middleName;
	}




	public void setAuthor_middleName(String author_middleName) {
		this.author_middleName = author_middleName;
	}




	public String getAuthor_email() {
		return author_email;
	}




	public void setAuthor_email(String author_email) {
		this.author_email = author_email;
	}

}
