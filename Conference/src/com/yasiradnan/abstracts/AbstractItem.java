package com.yasiradnan.abstracts;

import java.util.ArrayList;
import java.util.List;

public class AbstractItem {
    int id;
    String title, topic, type;
    List<String> names;
    public int getID;
    
    public AbstractItem(int id, String title, String topic, String type){
        this.id = id;
        this.title = title;
        this.topic = topic;
        this.type = type;
        names = new ArrayList<String>();
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    public int getGetID() {
        return getID;
    }
    public void setGetID(int getID) {
        this.getID = getID;
    }
    public String getTopic() {
        return topic;
    }
    
    public void setTopic(String topic) {
        this.topic = topic;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    public List<String> getNames() {
        return names;
    }
    public void setNames(List<String> names) {
        this.names = names;
    }
}
