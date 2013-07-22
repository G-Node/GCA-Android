/**
 * 
 */
package com.yasiradnan.ConferenceGenerator;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * @author Adnan
 *
 */
public class Generator {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Schema schema = new Schema(1, "com.yasiradnan.conference");
    }
    
    private void addAbstract(Schema schema){
        Entity absData = schema.addEntity("AbstractItem");
        absData.addIdProperty();
        absData.addStringProperty("Correspondence").notNull();
        absData.addStringProperty("title").notNull();
        absData.addStringProperty("url").notNull();
        absData.addStringProperty("text").notNull();
        absData.addStringProperty("type").notNull();
        absData.addStringProperty("topic").notNull();
        absData.addStringProperty("coi").notNull();
        absData.addStringProperty("cite").notNull();
        
        /*
         * Abstract Keywords
         * */
        
        Entity absKeyWord = schema.addEntity("AbstractKeyWords");
        absKeyWord.addStringProperty("keywords");
        Property abstractitemId = absKeyWord.addLongProperty("abstractitemId").notNull().getProperty();
        absKeyWord.addToOne(absData, abstractitemId);
        
        /*
         * Abstract Authors
         */
        
        Entity absAuthor = schema.addEntity("AbstractAuthor");
        absAuthor.addIdProperty();
        absAuthor.addStringProperty("name").notNull();
        absAuthor.addBooleanProperty("Is_Corresponding");
        
        /*
         * AFFILIATION TABLE
         */
        
        Entity absAffiliation = schema.addEntity("AbstractAffiliation");
        absAffiliation.addIdProperty();
        absAffiliation.addIntProperty("affiliationNumber");
        
        /*
         * AuthorsAffiliation
         */
        
        
        
        
    }
    
    
}
