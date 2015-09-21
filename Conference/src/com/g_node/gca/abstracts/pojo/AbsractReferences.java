package com.g_node.gca.abstracts.pojo;

public class AbsractReferences {
	
	private String abstract_uuid;
	private String reference_uuid;
	private String reference_text;
	private String reference_link;
	private String reference_doi;
	

	public AbsractReferences(String abstract_uuid, String reference_uuid,
			String reference_text, String reference_link, String reference_doi) {
		super();
		this.abstract_uuid = abstract_uuid;
		this.reference_uuid = reference_uuid;
		this.reference_text = reference_text;
		this.reference_link = reference_link;
		this.reference_doi = reference_doi;
	}




	public String getAbstract_uuid() {
		return abstract_uuid;
	}




	public void setAbstract_uuid(String abstract_uuid) {
		this.abstract_uuid = abstract_uuid;
	}




	public String getReference_uuid() {
		return reference_uuid;
	}




	public void setReference_uuid(String reference_uuid) {
		this.reference_uuid = reference_uuid;
	}




	public String getReference_text() {
		return reference_text;
	}




	public void setReference_text(String reference_text) {
		this.reference_text = reference_text;
	}




	public String getReference_link() {
		return reference_link;
	}




	public void setReference_link(String reference_link) {
		this.reference_link = reference_link;
	}




	public String getReference_doi() {
		return reference_doi;
	}




	public void setReference_doi(String reference_doi) {
		this.reference_doi = reference_doi;
	}

}
