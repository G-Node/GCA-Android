package com.yasiradnan.abstracts;

public class AbstractModel {
    public String title;
    public String topic;
    public String type;
    public String abstractContent;
    
    public AbstractModel(String abTitle, String abTopic,String abContent,String abType ){
        title = abTitle;
        topic = abTopic;
        abstractContent = abContent;
        type = abType;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
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
    
    public String getAbstractContent() {
        return abstractContent;
    }
    
    public void setAbstractContent(String abstractContent) {
        this.abstractContent = abstractContent;
    }

}
