/**
 * 
 */
package com.yasiradnan.ConferenceGenerator;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

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
        addAbstract(schema);
        try {
            new DaoGenerator().generateAll(schema, "../Conference/src-gen/");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private static void addAbstract(Schema schema){
        
        /*
         * Abstract 
         * */
        
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
       
        Property AuthorIdProperty =absData.addLongProperty("absAuthorId").getProperty();
        absData.addToOne(absData, AuthorIdProperty);
        
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
        
        absAuthor.addToOne(absAuthor, abstractitemId);
        
        
        /*
         * AFFILIATION TABLE
         */
        
        Entity absAffiliation = schema.addEntity("AbstractAffiliation");
        absAffiliation.addIdProperty();
        absAffiliation.addIntProperty("affiliationNumber");
        
        /*
         * AuthorsAffiliation
         */
        
        absAffiliation.addToOne(absAuthor, AuthorIdProperty);
        Property AffiliationIdProperty =absAuthor.addLongProperty("absAffiliationId").getProperty();
        absAuthor.addToOne(absAuthor, AffiliationIdProperty);

        /*
         * AFFILIATION_ONE
         */
        
        Entity absAffiliationName = schema.addEntity("AbsAffiliationName");
        absAffiliationName.addIdProperty();
        absAffiliationName.addStringProperty("af_name");
        
        Property absAffiliationNameProperty = absData.addLongProperty("absAffiliationNameId").getProperty();
        absData.addToOne(absData, absAffiliationNameProperty);
        
    }
    
    
}
