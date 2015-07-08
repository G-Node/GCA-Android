package com.g_node.gca.abstracts.pojo;

public class ABSTRACT_AFFILIATION_ID_POSITION_POJO {
	
	private String Abstract_UUID;
	private String Affiliation_UUID;
	private int Affiliation_position;
	
	
	
	public ABSTRACT_AFFILIATION_ID_POSITION_POJO(String abstract_UUID,
			String affiliation_UUID, int affiliation_position) {
		super();
		Abstract_UUID = abstract_UUID;
		Affiliation_UUID = affiliation_UUID;
		Affiliation_position = affiliation_position;
	}



	public String getAbstract_UUID() {
		return Abstract_UUID;
	}



	public void setAbstract_UUID(String abstract_UUID) {
		Abstract_UUID = abstract_UUID;
	}



	public String getAffiliation_UUID() {
		return Affiliation_UUID;
	}



	public void setAffiliation_UUID(String affiliation_UUID) {
		Affiliation_UUID = affiliation_UUID;
	}



	public int getAffiliation_position() {
		return Affiliation_position;
	}



	public void setAffiliation_position(int affiliation_position) {
		Affiliation_position = affiliation_position;
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
