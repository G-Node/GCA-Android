/*
 * Copyright (c) 2013, Ivan Mylyanyk
 * License: BSD-2 (see LICENSE)
 * This code was modified with Author permission.
 * Modifications:
 * Added ItemType as Integer and Date
 * */

package org.g_node.schedule;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ScheduleItem implements Serializable {
    private String time;

    private String title;

    private int itemType;

    private String content;

    private String date;

    ScheduleItem(String _time, String _title) {
        time = _time;
        title = _title;
        itemType = 2;
        setContent(null);
    }

    ScheduleItem(String _time, String _title, String _content, int _itemType, String _date) {
        time = _time;
        title = _title;
        itemType = _itemType;
        content = _content;
        date = _date;
    }

    ScheduleItem(String _time, String _title, int _itemType, String _date) {
        time = _time;
        title = _title;
        itemType = _itemType;
        date = _date;
        setContent(null);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
