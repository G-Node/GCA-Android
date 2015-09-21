package com.g_node.gca.abstracts.pojo;

public class AffiliationDetails {

	
	private String affiliation_uuid;
	private String affiliation_address;
	private String affiliation_country;
	private String affiliation_department;
	private String affiliation_section;
	

	public AffiliationDetails(String affiliation_uuid,
			String affiliation_address, String affiliation_country,
			String affiliation_department, String affiliation_section) {
		super();
		this.affiliation_uuid = affiliation_uuid;
		this.affiliation_address = affiliation_address;
		this.affiliation_country = affiliation_country;
		this.affiliation_department = affiliation_department;
		this.affiliation_section = affiliation_section;
	}


	public String getAffiliation_uuid() {
		return affiliation_uuid;
	}

	public void setAffiliation_uuid(String affiliation_uuid) {
		this.affiliation_uuid = affiliation_uuid;
	}


	public String getAffiliation_address() {
		return affiliation_address;
	}


	public void setAffiliation_address(String affiliation_address) {
		this.affiliation_address = affiliation_address;
	}


	public String getAffiliation_country() {
		return affiliation_country;
	}


	public void setAffiliation_country(String affiliation_country) {
		this.affiliation_country = affiliation_country;
	}


	public String getAffiliation_department() {
		return affiliation_department;
	}


	public void setAffiliation_department(String affiliation_department) {
		this.affiliation_department = affiliation_department;
	}

	public String getAffiliation_section() {
		return affiliation_section;
	}


	public void setAffiliation_section(String affiliation_section) {
		this.affiliation_section = affiliation_section;
	}


}
