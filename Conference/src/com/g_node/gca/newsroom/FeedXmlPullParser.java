/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.newsroom;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class FeedXmlPullParser {

	static final String KEY_SITE = "item";
	static final String KEY_TITLE = "title";
	static final String KEY_LINK = "link";
	static final String KEY_ABOUT = "description";
	static final String KEY_IMAGE_URL = "image";
	static final String KEY_DATE = "dc:date";

	public static List<NewsItemSingle> getNewsItemFromFile(Context ctx) {

		// feed items list that we will return
		List<NewsItemSingle> newsItemsList;
		newsItemsList = new ArrayList<NewsItemSingle>();

		// temp holder for current news item while parsing
		NewsItemSingle currentRssItem = null;
		// temp holder for current text value while parsing
		String curText = "";

		try {
			// Get our factory and PullParser
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xpp = factory.newPullParser();

			// Open up InputStream and Reader of our file.
			FileInputStream fis = ctx.openFileInput("news.xml");
			Log.i("incf-file", "before file null check");
			Log.i("incf-file", "FILE Stream: " + fis.toString());
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			if (reader.readLine() == null) {
				Log.i("incf-file", "FILE Buffer NULL" + reader.toString());
				Builder x = new AlertDialog.Builder(ctx);
			    x.setTitle("ERROR")
			    .setMessage("Unable to connect to Internet. Please ensure internet connectivity")
			    .setNeutralButton(android.R.string.ok,
			            new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int id) {
			            dialog.cancel();
			        }
			    }).setIcon(android.R.drawable.ic_dialog_alert)
			     .show();
			}
			
			Log.i("incf-rss", "parsing starts");
			// point the parser to our file.
			xpp.setInput(reader);

			// get initial eventType
			int eventType = xpp.getEventType();
			
			//for inside item managing
			boolean insideitem = false;
			Log.i("incf", "insideItem value: " + insideitem);
			
			// Loop through pull events until we reach END_DOCUMENT
			while (eventType != XmlPullParser.END_DOCUMENT) {
				Log.i("incf", "in While now - insideitem value: " + insideitem);
				// Get the current tag
				String tagname = xpp.getName();
				Log.i("incf", "tagname value: " + tagname);
				
				// React to different event types appropriately
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (tagname.equalsIgnoreCase(KEY_SITE)) {
						// If we are starting a new <item> block we need
						//a new newsItem object to represent it
						insideitem = true;
						currentRssItem = new NewsItemSingle();
						Log.i("incf", "insideItem value: " + insideitem);
					}
					break;

				case XmlPullParser.TEXT:
					if (insideitem) {
						//grab the current text so we can use it in END_TAG event
						curText = xpp.getText();
						Log.i("incf-rss", "curText = " + curText);
						Log.i("incf-rss", "insideItem value: " + insideitem);
					}
					break;

				case XmlPullParser.END_TAG:
				if(insideitem){	
					Log.i("incf-rss", "insideItem value: " + insideitem);
					if (tagname.equalsIgnoreCase(KEY_SITE)) {
						// if </item> then we are done with current Site
						// add it to the list.
						newsItemsList.add(currentRssItem);
					} else if (tagname.equalsIgnoreCase(KEY_DATE)) {
						// if </name> use setName() on curSite
						currentRssItem.setPubDate(curText);
					} else if (tagname.equalsIgnoreCase(KEY_TITLE)) {
						// if </name> use setName() on curSite
						currentRssItem.setName(curText);
					} else if (tagname.equalsIgnoreCase(KEY_LINK)) {
						// if </link> use setLink() on curSite
						currentRssItem.setLink(curText);
					} else if (tagname.equalsIgnoreCase(KEY_ABOUT)) {
						// if </about> use setAbout() on curSite
						//curText = android.text.Html.fromHtml(curText).toString();
						
						Log.i("incf-rss", " parse html before curText = " + curText);
						
						//parsing html content to to get images
						Document doc = Jsoup.parse(curText);
						Element e = doc.select("img[src]").first();
						if(null != e) {
							String imgUrl = e.attr("src");
							Log.i("incf", "Image URL extracted = " + imgUrl);
							currentRssItem.setImgUrl(imgUrl);
						}
						
						//parse html to text
						curText = org.jsoup.Jsoup.parse(curText).text();
						
						Log.i("incf-rss", "parse html after curText = " + curText);
						currentRssItem.setAbout(curText);
					} else if (tagname.equalsIgnoreCase(KEY_IMAGE_URL)) {
						// if </image> use setImgUrl() on curSite
						currentRssItem.setImgUrl(curText);
					}
				}
					break;

				default:
					break;
				}
				//move on to next iteration
				Log.i("incf-rss", "next iteration after case - insideitem:" + insideitem);
				eventType = xpp.next();
			}
			Log.i("incf-rss", "after while finished - ");
		} catch (Exception e) {
			Log.i("incf-file", "no file exists - " + e.getMessage());
			Builder x = new AlertDialog.Builder(ctx);
		    x.setTitle("ERROR")
		    .setMessage("Unable to connect to Internet. Please ensure internet connectivity")
		    .setNeutralButton(android.R.string.ok,
		            new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int id) {
		            dialog.cancel();
		        }
		    }).setIcon(android.R.drawable.ic_dialog_alert)
		     .show();
		}

		// the parsed list is ready for returning here.
		return newsItemsList;
	}
}
