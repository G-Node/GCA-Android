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
        Schema newSchema = new Schema(1, "com.yasiradnan.conference");
        addAbstract(newSchema);
        try {
            new DaoGenerator().generateAll(newSchema, "../Conference/src-gen/");
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
        
        Entity absData = schema.addEntity("AbstractsItem");
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
        Property abstracstitemId = absKeyWord.addLongProperty("abstractsitemId").notNull().getProperty();
        absKeyWord.addToOne(absData, abstracstitemId);
        
        /*
         * Abstract Authors
         */
        
        Entity absAuthor = schema.addEntity("AbstractAuthor");
        absAuthor.addIdProperty();
        absAuthor.addStringProperty("name").notNull();
        
        
        /*
         * AFFILIATION TABLE
         */
        
        Entity absAffiliation = schema.addEntity("AbstractAffiliation");
        absAffiliation.addIdProperty();
        absAffiliation.addIntProperty("affiliationNumber");
        
        /*
         * AuthorsAffiliation
         */
        

        /*
         * AFFILIATION_ONE
         */
        
        Entity absAffiliationName = schema.addEntity("AbsAffiliationName");
        absAffiliationName.addIdProperty();
        absAffiliationName.addStringProperty("af_name");
              
        /*
         * Relations
         */
        Property AuthorIdProperty =absData.addLongProperty("absAuthorId").getProperty();
        absData.addToOne(absAuthor, AuthorIdProperty);
        
        Property abstracstitemAuthor = absAuthor.addLongProperty("abstractsitemId").getProperty();
        absAuthor.addToOne(absData, abstracstitemAuthor);
        
        Property absAffiliationNameProperty = absData.addLongProperty("absAffiliationNameId").getProperty();
        absData.addToMany(absAffiliationName, absAffiliationNameProperty);
        
        Property abs_AF_Author = absAffiliation.addLongProperty("absAuthorId").getProperty();
        absAffiliation.addToOne(absAuthor, abs_AF_Author);
       
        Property AffiliationIdProperty =absAuthor.addLongProperty("absAffiliationId").getProperty();
        absAuthor.addToOne(absAffiliation, AffiliationIdProperty);
        
        
        
        
    }
    
    
}
