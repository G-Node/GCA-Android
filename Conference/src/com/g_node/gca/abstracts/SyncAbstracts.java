 package com.g_node.gca.abstracts;

import java.io.InputStream;
import java.util.ArrayList;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.g_node.gca.abstracts.pojo.AbstractAffiliationIdPosition;
import com.g_node.gca.abstracts.pojo.AbsractAuthorPositionAffiliation;
import com.g_node.gca.abstracts.pojo.AbstractDetails;
import com.g_node.gca.abstracts.pojo.AbstractFigures;
import com.g_node.gca.abstracts.pojo.AbsractReferences;
import com.g_node.gca.abstracts.pojo.AffiliationDetails;
import com.g_node.gca.abstracts.pojo.AuthorsDetails;
import com.google.android.gms.internal.dd;

public class SyncAbstracts {
	
	String gTag = "GCA-Sync";
	
	DatabaseHelper dbHelper;
	InputStream jsonStreamFromServer;
	
	/*
	 * POJOs Arraylists to hold data after parsing 
	 * Later on, we do bulk insert and save all the data in db
	 */
	private ArrayList<AbstractAffiliationIdPosition> ABSTRACT_AFFILIATION_ID_POSITION_POJOS_ARRAY = new ArrayList<AbstractAffiliationIdPosition>();
	private ArrayList<AbsractAuthorPositionAffiliation> ABSTRACT_AUTHOR_POSITION_AFFILIATION_POJOS_ARRAY = new ArrayList<AbsractAuthorPositionAffiliation>(); 
	private ArrayList<AbstractDetails> ABSTRACT_DETAILS_POJOS_ARRAY = new ArrayList<AbstractDetails>();
	private ArrayList<AbstractFigures> ABSTRACT_FIGURES_POJOS_ARRAY = new ArrayList<AbstractFigures>();
	private ArrayList<AbsractReferences> ABSTRACT_REFERENCES_POJOS_ARRAY = new ArrayList<AbsractReferences>(); 
	private ArrayList<AffiliationDetails> AFFILIATION_DETAILS_POJOS_ARRAY = new ArrayList<AffiliationDetails>();
	private ArrayList<AuthorsDetails> AUTHORS_DETAILS_POJOS_ARRAY = new ArrayList<AuthorsDetails>();	
	
	/*
	 * These arraylists are for fetching existing stuff from database
	 * helper for determining if it's an new INSERT case or a UPDATE case
	 */
	private ArrayList<String> ExistingAbstracts_UUIDs = new ArrayList<String>();
	private ArrayList<String> ExistingAffiliation_UUIDs = new ArrayList<String>();
	private ArrayList<String> ExistingAuthor_UUIDs = new ArrayList<String>();
	
	
	/*
	 * Constructor
	 */
	public SyncAbstracts(DatabaseHelper dbHelpeer, InputStream jsonStreamFromServer) {
		this.dbHelper = dbHelpeer;
		this.jsonStreamFromServer = jsonStreamFromServer;
		
	}
	
	/*
	 * Main synchronization function
	 * it calls respective sub functions for performing sync
	 */
	public void doSync() {
		int no = -1;
		
		/*
		 * Populate by fetching existing stuff. 
		 * This function will fillup 3 arraylists defined above
		 * of existing uuids
		 */
		populateExistingDataFromDB();
		
		/*
		 * Parse the incoming JSON.
		 * The server will return the JSON which we need to parse
		 */
		parseJSONStreamAndUpdateArrayLists();
		
		no = dbHelper.getCountOfRowsInAbstractDetailsTable();
		Log.d(gTag, "No. of records in ABSTRACT_DETAILS table BEFORE deletion: " + no);
		
		/*
		 * Process the populated array lists of Abstract
		 * Delete existing
		 */
		processAbstracts();
		
		no = dbHelper.getCountOfRowsInAbstractDetailsTable();
		Log.d(gTag, "No. of records in ABSTRACT_DETAILS table after deletion: " + no);
		
		/*
		 * As the deletions are already performed, now do insertion in 5 tables 
		 * related to Abstracts
		 */
		doAbstractRelatedInsertions();
		
		no = dbHelper.getCountOfRowsInAbstractDetailsTable();
		Log.d(gTag, "No. of records in ABSTRACT_DETAILS table after INSERTION AGAIN: " + no);
		
		/*
		 * Process Authors now
		 * Delete Existing authors
		 */
		processAuthors();
		
		/*
		 * As authors are processed, just add now
		 */
		doAuthorInsertions();
		 
		 /*
		  * Process Affiliations
		  * Delete Existing
		  */
		processAffiliations();
		 
		 /*
		  * As affiliations are processed,
		  * just add now
		  */
		 doAffiliationInsertions();
	
	} //end do sync
	
	
	/*
	 * This function checks for an Abstract if it's already present in database
	 * if it is: it deletes it's entry from 5 tables, related to Abstract
	 * we are doing this because we need to update Abstract
	 */
	public void processAbstracts() {		
		for(AbstractDetails abstractPojo:ABSTRACT_DETAILS_POJOS_ARRAY) {			
			String uuid = abstractPojo.getUuid();			
			if(ExistingAbstracts_UUIDs.contains(uuid)){	//update case - perform deletions
				dbHelper.deleteAbstract(uuid);				
			}
		}			
	}
	
	/*
	 * After the existing stuff is deleted, this function will handle
	 * new insertions of Abstracts. 
	 */
	public void doAbstractRelatedInsertions() {
		
		/*
		 * Deletions are already performed. So we just need to insert new stuff now
		 * Will take care of 5 tables. Need to process author and affiliation stuff separately
		 */
		dbHelper.populateABSTRACT_DETAILS(ABSTRACT_DETAILS_POJOS_ARRAY);
		dbHelper.populateABSTRACT_FIGURES(ABSTRACT_FIGURES_POJOS_ARRAY);
		dbHelper.populateABSTRACT_REFERENCES(ABSTRACT_REFERENCES_POJOS_ARRAY);
		dbHelper.populateABSTRACT_AFFILIATION_ID_POSITION(ABSTRACT_AFFILIATION_ID_POSITION_POJOS_ARRAY);
		dbHelper.populateABSTRACT_AUTHOR_POSITION_AFFILIATION(ABSTRACT_AUTHOR_POSITION_AFFILIATION_POJOS_ARRAY);
		
	}

	/*
	 * Like Abstracts, an Author can be updated or there can be a new author
	 * So this function deletes previous entry of author.
	 * Insertion of that author again with updated info will be handled in doAuthorInsertions() func.
	 */
	public void processAuthors() {		
		for(AuthorsDetails author:AUTHORS_DETAILS_POJOS_ARRAY) {
			String currAuthorUUID = author.getAuthor_uuid();
			if(ExistingAuthor_UUIDs.contains(currAuthorUUID)) {
				dbHelper.deleteAuthor(currAuthorUUID);
			} 
		} 
	}
	
	/*
	 * As we have deleted previous entry of existing authors,
	 * this function handles insertion of authors. Includes authors to be updated
	 * and as well as new authors. But doesn't matter as authors to be updated are also
	 * deleted previously
	 */
	public void doAuthorInsertions() {
		dbHelper.populateAUTHORS_DETAILS(AUTHORS_DETAILS_POJOS_ARRAY);
	}

	
	/*
	 * Like Authors, an affiliation can be updated or there can be a new affiliation
	 * So this function deletes previous entry of affiliation.
	 * Insertion of that affiliation again with updated info will be handled in 
	 * dAffiliationInsertions() func.
	 */
	public void processAffiliations() {		
		for(AffiliationDetails affiliation:AFFILIATION_DETAILS_POJOS_ARRAY) {
			String currentAffiliationUUID = affiliation.getAffiliation_uuid();
			if(ExistingAffiliation_UUIDs.contains(currentAffiliationUUID)) {
				dbHelper.deleteAffiliation(currentAffiliationUUID);
			} 
		} //end for
	}
	
	/*
	 * As we have deleted previous entry of existing affiliation,
	 * this function handles insertion of affiliation. Includes affiliation to be updated
	 * and as well as new affiliation. But doesn't matter as affiliations to be updated are also
	 * deleted previously
	 */
	public void doAffiliationInsertions() {
		dbHelper.populateAFFILIATION_DETAILS(AFFILIATION_DETAILS_POJOS_ARRAY);
		
	}

	/*
	 * This function populates existing UUIDs of Abstracts, Authors, Affiliations
	 * into the arraylists defined in class. We'll use these to check if an entry
	 * is to be updated or inserted newly.
	 */
	public void populateExistingDataFromDB() {
		
		this.ExistingAbstracts_UUIDs = dbHelper.fetchAbstractsUUIDs();
		this.ExistingAffiliation_UUIDs = dbHelper.fetchAffiliationsUUIDs();
		this.ExistingAuthor_UUIDs = dbHelper.fetchAuthhorsUUIDs();
	}
	
	/*
	 * This used AbstractJsonParse class to parse the incoming stream of JSON
	 * and then later on it simply updates this class's ArrayLists
	 */
	public void parseJSONStreamAndUpdateArrayLists() {
		
		AbstractsJsonParse parseAbstractsJson = new AbstractsJsonParse(jsonStreamFromServer, dbHelper);
        parseAbstractsJson.jsonParse();
        
        this.ABSTRACT_AFFILIATION_ID_POSITION_POJOS_ARRAY = parseAbstractsJson.getABSTRACT_AFFILIATION_ID_POSITION_POJOS_ARRAY();
        this.ABSTRACT_AUTHOR_POSITION_AFFILIATION_POJOS_ARRAY = parseAbstractsJson.getABSTRACT_AUTHOR_POSITION_AFFILIATION_POJOS_ARRAY();
        this.ABSTRACT_DETAILS_POJOS_ARRAY = parseAbstractsJson.getABSTRACT_DETAILS_POJOS_ARRAY();
        this.ABSTRACT_FIGURES_POJOS_ARRAY = parseAbstractsJson.getABSTRACT_FIGURES_POJOS_ARRAY();
        this.ABSTRACT_REFERENCES_POJOS_ARRAY = parseAbstractsJson.getABSTRACT_REFERENCES_POJOS_ARRAY();
        this.AFFILIATION_DETAILS_POJOS_ARRAY = parseAbstractsJson.getAFFILIATION_DETAILS_POJOS_ARRAY();
        this.AUTHORS_DETAILS_POJOS_ARRAY = parseAbstractsJson.getAUTHORS_DETAILS_POJOS_ARRAY();
        
   }
	
	
}
