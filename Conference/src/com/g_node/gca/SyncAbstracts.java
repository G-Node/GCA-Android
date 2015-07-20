 package com.g_node.gca;

import java.io.InputStream;
import java.util.ArrayList;

import android.util.Log;

import com.g_node.gca.abstracts.AbstractsJsonParse;
import com.g_node.gca.abstracts.DatabaseHelper;
import com.g_node.gca.abstracts.pojo.ABSTRACT_AFFILIATION_ID_POSITION_POJO;
import com.g_node.gca.abstracts.pojo.ABSTRACT_AUTHOR_POSITION_AFFILIATION_POJO;
import com.g_node.gca.abstracts.pojo.ABSTRACT_DETAILS_POJO;
import com.g_node.gca.abstracts.pojo.ABSTRACT_FIGURES_POJO;
import com.g_node.gca.abstracts.pojo.ABSTRACT_REFERENCES_POJO;
import com.g_node.gca.abstracts.pojo.AFFILIATION_DETAILS_POJO;
import com.g_node.gca.abstracts.pojo.AUTHORS_DETAILS_POJO;

public class SyncAbstracts {
	
	String gTag = "GCA-Sync";
	
	DatabaseHelper dbHelper;
	InputStream jsonStreamFromServer;
	
	/*
	 * POJOs Arraylists to hold data after parsing 
	 * Later on, we do bulk insert and save all the data in db
	 */
	private ArrayList<ABSTRACT_AFFILIATION_ID_POSITION_POJO> ABSTRACT_AFFILIATION_ID_POSITION_POJOS_ARRAY = new ArrayList<ABSTRACT_AFFILIATION_ID_POSITION_POJO>();
	private ArrayList<ABSTRACT_AUTHOR_POSITION_AFFILIATION_POJO> ABSTRACT_AUTHOR_POSITION_AFFILIATION_POJOS_ARRAY = new ArrayList<ABSTRACT_AUTHOR_POSITION_AFFILIATION_POJO>(); 
	private ArrayList<ABSTRACT_DETAILS_POJO> ABSTRACT_DETAILS_POJOS_ARRAY = new ArrayList<ABSTRACT_DETAILS_POJO>();
	private ArrayList<ABSTRACT_FIGURES_POJO> ABSTRACT_FIGURES_POJOS_ARRAY = new ArrayList<ABSTRACT_FIGURES_POJO>();
	private ArrayList<ABSTRACT_REFERENCES_POJO> ABSTRACT_REFERENCES_POJOS_ARRAY = new ArrayList<ABSTRACT_REFERENCES_POJO>(); 
	private ArrayList<AFFILIATION_DETAILS_POJO> AFFILIATION_DETAILS_POJOS_ARRAY = new ArrayList<AFFILIATION_DETAILS_POJO>();
	private ArrayList<AUTHORS_DETAILS_POJO> AUTHORS_DETAILS_POJOS_ARRAY = new ArrayList<AUTHORS_DETAILS_POJO>();	
	
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
		
		for(int i=0; i< ABSTRACT_DETAILS_POJOS_ARRAY.size(); i++) {
			
			ABSTRACT_DETAILS_POJO temp = ABSTRACT_DETAILS_POJOS_ARRAY.get(i);
			String currentAbsUUID = temp.getUuid();
			
			if(ExistingAbstracts_UUIDs.contains(temp.getUuid())){	//update case - perform deletions
				long rows_affected;
				
				/*
				 * deletion form 5 tables
				 * Insertions will be performed later in doAbstractRelatedInsertions() function
				 */
				rows_affected = DatabaseHelper.database.delete("ABSTRACT_DETAILS", "UUID = ?", new String[] { currentAbsUUID });
				Log.i(gTag, "Deleted from ABSTRACT_DETAILS: " + rows_affected);
				
				rows_affected = DatabaseHelper.database.delete("ABSTRACT_FIGURES", "ABSTRACT_UUID = ?", new String[] { currentAbsUUID });
				Log.i(gTag, "Deleted from ABSTRACT_FIGURES: " + rows_affected);
				
				rows_affected = DatabaseHelper.database.delete("ABSTRACT_REFERENCES", "ABSTRACT_UUID = ?", new String[] { currentAbsUUID });
				Log.i(gTag, "Deleted from ABSTRACT_REFERENCES: " + rows_affected);
				
				rows_affected = DatabaseHelper.database.delete("ABSTRACT_AFFILIATION_ID_POSITION", "ABSTRACT_UUID = ?", new String[] { currentAbsUUID });
				Log.i(gTag, "Deleted from ABSTRACT_AFFILIATION_ID_POSITION: " + rows_affected);
				
				rows_affected = DatabaseHelper.database.delete("ABSTRACT_AUTHOR_POSITION_AFFILIATION", "ABSTRACT_UUID = ?", new String[] { currentAbsUUID });
				Log.i(gTag, "Deleted from ABSTRACT_AUTHOR_POSITION_AFFILIATION: " + rows_affected);
				
			} else {	
				; //do nothing
			}
		}	//end for		
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
		
		for(int i=0; i<AUTHORS_DETAILS_POJOS_ARRAY.size(); i++) {
			//check for duplication and delete
			AUTHORS_DETAILS_POJO temp = AUTHORS_DETAILS_POJOS_ARRAY.get(i);
			
			String currAuthorUUID = temp.getAuthor_uuid();
			long rows_affected;
			
			if(ExistingAuthor_UUIDs.contains(temp.getAuthor_uuid())) {	//author already exists, delete it, insert again
				
				//query for deleting
				rows_affected = DatabaseHelper.database.delete("AUTHORS_DETAILS", "AUTHOR_UUID = ?", new String[] { currAuthorUUID });
				Log.i(gTag, "Deleted from AUTHORS_DETAILS: " + rows_affected);
				
			} else {
				; //do nothing -  insertions later on
			}
		} //end for
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
		
		for(int i=0; i< AFFILIATION_DETAILS_POJOS_ARRAY.size(); i++) {
			//check for duplication and delete
			
			AFFILIATION_DETAILS_POJO temp = AFFILIATION_DETAILS_POJOS_ARRAY.get(i);
			
			String currentAffiliationUUID = temp.getAffiliation_uuid();
			long rows_affected;
			if(ExistingAffiliation_UUIDs.contains(temp.getAffiliation_uuid())) {
				
				//deletion query
				rows_affected = DatabaseHelper.database.delete("AFFILIATION_DETAILS", "AFFILIATION_UUID = ?", new String[] { currentAffiliationUUID });
				Log.i(gTag, "Deleted from AFFILIATION_DETAILS : " + rows_affected);
				
			} else {
				; //do nothing as insertions will be done later
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
		
		this.ExistingAbstracts_UUIDs = dbHelper.fetchExistingAbstractsUUID();
		this.ExistingAffiliation_UUIDs = dbHelper.fetchExistingAffiliationsUUID();
		this.ExistingAuthor_UUIDs = dbHelper.fetchExistingAuthhorsUUID();
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
