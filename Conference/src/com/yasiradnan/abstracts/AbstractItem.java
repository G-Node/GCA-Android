package com.yasiradnan.abstracts;

public class AbstractItem {
    public String title;
    public String topic;
    public String type;
    public String abstractContent;
    public String authorName;
    
    public AbstractItem(String abTitle, String abTopic,String abContent,String abType,String abAuthor ){
        title = abTitle;
        topic = abTopic;
        abstractContent = abContent;
        type = abType;
        authorName = abAuthor;
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
    
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    
    public String getAuthorName() {
        return authorName;
    }

}
