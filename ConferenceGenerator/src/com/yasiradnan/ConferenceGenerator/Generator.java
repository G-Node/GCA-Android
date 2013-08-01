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

    private static void addAbstract(Schema schema) {

        /*
         * Abstract
         */

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
        absData.addStringProperty("refs").notNull();

        /*
         * Abstract Keywords
         */

        Entity absKeyWord = schema.addEntity("AbstractKeyWords");
        absKeyWord.addStringProperty("keywords");
        Property abstracstitemId = absKeyWord.addLongProperty("abstractsitemId").notNull()
                .getProperty();
        absKeyWord.addToOne(absData, abstracstitemId);

        /*
         * Abstract Authors
         */

        Entity absAuthor = schema.addEntity("AbstractAuthor");
        absAuthor.addIdProperty();
        absAuthor.addStringProperty("name").notNull();
        absAuthor.addStringProperty("is_Corresponding").notNull();

        /*
         * AFFILIATION TABLE
         */

        Entity absAffiliation = schema.addEntity("AbstractAffiliation");
        absAffiliation.addIdProperty();
        absAffiliation.addStringProperty("affiliationNumber");
        
        /*
         * AFFILIATION_ONE
         */

        Entity absAffiliationName = schema.addEntity("AbsAffiliationName");
        absAffiliationName.addIdProperty();
        absAffiliationName.addStringProperty("af_name");

        /*
         * AUTHORS_ABSTRACT
         */
        
        Entity absAuthorsAbstract = schema.addEntity("AuthorsAbstract");
       
        Property abstracstsItemsId = absAuthorsAbstract.addLongProperty("abstractsitemId")
                .notNull().getProperty();
        absAuthorsAbstract.addToOne(absData, abstracstsItemsId);
        
        Property abstracstAuthorId = absAuthorsAbstract.addLongProperty("abstractauthorId")
                .notNull().getProperty();
        absAuthorsAbstract.addToOne(absAuthor, abstracstAuthorId);

        /*
         * ABSTRACT AND AFFILIATION_ONE
         */
        
        Entity absAffiliateName = schema.addEntity("AbstractAffiliateName");
       
        Property getabstractItemID = absAffiliateName.addLongProperty("abstractsitemId").notNull().getProperty();
        
        absAffiliateName.addToOne(absData, getabstractItemID);
        
        Property abstractaffiliateNameID = absAffiliateName.addLongProperty("absaffiliationnameId").notNull().getProperty();
        
        absAffiliateName.addToOne(absAffiliationName, abstractaffiliateNameID);
        
        /*
         * Authors and Affiliation number
         */
        
        Entity absAuthorsAffiliate = schema.addEntity("AuthorsAffiliate");
       
        Property authorsItemID = absAuthorsAffiliate.addLongProperty("abstractauthorId").notNull().getProperty();
        absAuthorsAffiliate.addToOne(absAuthor, authorsItemID);
        
        Property authorsaffiliateNumber = absAuthorsAffiliate.addLongProperty("abstractaffiliationId").notNull().getProperty();
        absAuthorsAffiliate.addToOne( absAffiliation, authorsaffiliateNumber);
        
        
        
    }
}
