/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.newsroom;
/*
 * Data object that holds all of our information about a single rss item.
 */
public class NewsItemSingle {

	private String title;
	private String link;
	private String description;
	private String imgUrl;
	private String pubDate;
	
	public String getName() {
		return title;
	}
	public void setName(String name) {
		this.title = name;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getAbout() {
		return description;
	}
	public void setAbout(String about) {
		this.description = about;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	
	
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	@Override
	public String toString() {
		return "StackSite [name=" + title + ", link=" + link + ", about="
				+ description + ", imgUrl=" + imgUrl + ", PubDate=" + pubDate + "]";
	}
}
