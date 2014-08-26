/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.abstracts;

public class AbstractFiguresClass {
	
	private String URL;
	private String caption;
	private String Fig_uuid;
	private String position;
	
	
	
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getFig_uuid() {
		return Fig_uuid;
	}
	public void setFig_uuid(String fig_uuid) {
		Fig_uuid = fig_uuid;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	
	
}