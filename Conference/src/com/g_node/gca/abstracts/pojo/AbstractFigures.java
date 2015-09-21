package com.g_node.gca.abstracts.pojo;

public class AbstractFigures {

	
	private String abstract_uuid;
	private String figure_uuid;
	private String figure_caption;
	private String figure_URL;
	private String figure_position;
	
	
	public AbstractFigures(String abstract_uuid, String figure_uuid,
			String figure_caption, String figure_URL, String figure_position) {
		super();
		this.abstract_uuid = abstract_uuid;
		this.figure_uuid = figure_uuid;
		this.figure_caption = figure_caption;
		this.figure_URL = figure_URL;
		this.figure_position = figure_position;
	}


	public String getAbstract_uuid() {
		return abstract_uuid;
	}


	public void setAbstract_uuid(String abstract_uuid) {
		this.abstract_uuid = abstract_uuid;
	}


	public String getFigure_uuid() {
		return figure_uuid;
	}


	public void setFigure_uuid(String figure_uuid) {
		this.figure_uuid = figure_uuid;
	}


	public String getFigure_caption() {
		return figure_caption;
	}


	public void setFigure_caption(String figure_caption) {
		this.figure_caption = figure_caption;
	}


	public String getFigure_URL() {
		return figure_URL;
	}


	public void setFigure_URL(String figure_URL) {
		this.figure_URL = figure_URL;
	}


	public String getFigure_position() {
		return figure_position;
	}


	public void setFigure_position(String figure_position) {
		this.figure_position = figure_position;
	}


}
