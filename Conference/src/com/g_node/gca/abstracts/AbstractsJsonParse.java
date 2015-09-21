/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.abstracts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.database.Cursor;
import android.util.Log;

import com.g_node.gca.abstracts.pojo.*;
import com.g_node.gca.utils.JSONReader;

public class AbstractsJsonParse {
	
	String gTag = "GCA-Abstracts";
	int noOfRecords = 0;
	
	DatabaseHelper dbHelper;
	InputStream jsonStream;
	
	/*
	 * POJOs Arraylists to hold data after parsing
	 * Lateron, we do bulk insert and save all the data in db
	 */
	private ArrayList<AbstractAffiliationIdPosition> ABSTRACT_AFFILIATION_ID_POSITION_POJOS_ARRAY = new ArrayList<AbstractAffiliationIdPosition>();
	private ArrayList<AbsractAuthorPositionAffiliation> ABSTRACT_AUTHOR_POSITION_AFFILIATION_POJOS_ARRAY = new ArrayList<AbsractAuthorPositionAffiliation>(); 
	private ArrayList<AbstractDetails> ABSTRACT_DETAILS_POJOS_ARRAY = new ArrayList<AbstractDetails>();
	private ArrayList<AbstractFigures> ABSTRACT_FIGURES_POJOS_ARRAY = new ArrayList<AbstractFigures>();
	private ArrayList<AbsractReferences> ABSTRACT_REFERENCES_POJOS_ARRAY = new ArrayList<AbsractReferences>(); 
	private ArrayList<AffiliationDetails> AFFILIATION_DETAILS_POJOS_ARRAY = new ArrayList<AffiliationDetails>();
	private ArrayList<AuthorsDetails> AUTHORS_DETAILS_POJOS_ARRAY = new ArrayList<AuthorsDetails>();	
	
	/*
	 * These 2 arraylists are just to hold UUIDs of parsed Affiliations and Authors. 
	 * Helper for checking if an Affiliation/Author is already parsed and saved or not.
	 * Avoids duplication
	 */
	private ArrayList<String> PARSED_Affiliation_UUIDs = new ArrayList<String>();
	private ArrayList<String> PARSED_Author_UUIDs = new ArrayList<String>();
	
	/*
	 * Constructor
	 */
	public AbstractsJsonParse(InputStream jsonStream, DatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
		this.jsonStream = jsonStream;
	}

	/*
	 * Main Parsing function that takes the JSON, parsed it, builds relevant Arraylists, and sends signal 
	 * for saving the parsed stuff ito database by migrating it from Arraylists to DB.
	 */
	public int jsonParse() {
		try {
			Log.d(gTag, "in JSON PARSING FUNCTION");
			
			//get json file from raw 
			InputStream inStream = this.jsonStream;
			JSONArray abstractArray = JSONReader.parseStream(inStream);	//read json file and put in JSONarray
			
			 for (int index = 0; index < abstractArray.length(); index++) {
				 Log.d("GCA-Profile", "in for loop - parsing obj " + index);

				 //get first abstract item object
				 JSONObject abstractJson = abstractArray.getJSONObject(index);
				 Log.d(gTag, "json got: " + abstractJson);	             
				 
				 //iterate over the object got, to extract required keys and values
				 
				 //abstract UUID
				 String abs_uuid = abstractJson.getString("uuid");
	             Log.d(gTag, "abstract uuid: " + abs_uuid);
	             
				 //abstract topic (this is a workaround for the stupid 'null') behaviour
	             String topic = "";
	             if (!abstractJson.isNull("topic")){
					 topic = abstractJson.getString("topic");
		             Log.d(gTag, "topic: " + topic);
	             }
	             //abstract title
	             String title = abstractJson.getString("title");
	             Log.d(gTag, "title: " + title);
	             
	             //abstract text
	             String text = abstractJson.getString("text");
	             Log.d(gTag, "text: " + text);
	             
	             //abstract state
	             //String state = jsonObject.getString("state");
	             //Log.d(gTag, "state: " + state);
	             String state = "";
	             //abstract sortID
	             int sortID = abstractJson.optInt("sortId");
	             Log.d(gTag, "sortID: " + sortID);
	             
	             
	             //abstract reasonForTalk
	             //String reasonForTalk = jsonObject.getString("reasonForTalk");
	             //Log.d(gTag, "reasonForTalk: " + reasonForTalk);
	             String reasonForTalk = "";
	             
	             //abstract mtime
	             //String mtime = jsonObject.getString("mtime");
	             //Log.d(gTag, "mtime: " + mtime);
	             String mtime = "";
	             
	             //abstract isTalk
	             //Boolean isTalk = jsonObject.getBoolean("isTalk");
	             Boolean isTalk = false;
	             String abstractType;
	             Log.d(gTag, "isTalk: " + isTalk);	             
	             if(!isTalk) {	//if isTalk is false, then type is poster
	            	abstractType = "poster"; 
	             } else {
	            	 abstractType = "Talk";
	             }
	             Log.d(gTag, "abstract type: " + abstractType);
	             
	             //abstract DOI
	             String doi = abstractJson.optString("doi");
		         Log.d(gTag, "doi: " + doi);

	             //Abstract conflictOfInterest
	             //String coi = jsonObject.getString("conflictOfInterest");
	             //Log.d(gTag, "conflictOfInterest: " + coi);
	             String coi = "No";
	             
	             //Abstract acknowledgements
	             String acknowledgements = abstractJson.
	            		 optString("acknowledgements");
	             Log.d(gTag, "acknowledgements: " + acknowledgements);

	             
	             /*
	              * Insertion of parsed Abstract in Arraylist
	              */            
	             AbstractDetails tempAbstractDetails = new AbstractDetails(abs_uuid, topic, title, text, state, sortID, reasonForTalk, mtime, abstractType, doi, coi, acknowledgements);
	             ABSTRACT_DETAILS_POJOS_ARRAY.add(tempAbstractDetails);
	             
	             tempAbstractDetails = null;
	             
	             //Abstract affiliations JSONarray
	             JSONArray abs_Aff_Array = abstractJson.getJSONArray("affiliations");
	             
	             //now iterate over this array for extracting each affiliation 
	             for (int j=0; j<abs_Aff_Array.length(); j++) {
	            	 //get affiliation object
	            	 JSONObject affiliationJSONObject = abs_Aff_Array.getJSONObject(j);
	            	 
	            	 //affiliation UUID
	            	 String affiliation_uuid = affiliationJSONObject.getString("uuid");
	            	 Log.d(gTag, "aff uuid: " + affiliation_uuid);
	            	 
	            	 //affiliation section
	            	 String affiliation_section = "";
	            	 if (affiliationJSONObject.has("section")){
		            	 affiliation_section = affiliationJSONObject.getString("section");
		            	 Log.d(gTag, "aff section: " + affiliation_section);
	            	 }
	            	 //affiliation department
	            	 String affiliation_department = "";
	            	 if (affiliationJSONObject.has("department")){
	            		 affiliation_department = affiliationJSONObject.getString("department");
	            		 Log.d(gTag, "aff department: " + affiliation_department);
	            	 }
	            	 
	            	 //affiliation country
	            	 String affiliation_country = "";
	            	 if (affiliationJSONObject.has("country")){
	            		 affiliation_country = affiliationJSONObject.getString("country");
		            	 Log.d(gTag, "aff country: " + affiliation_country);
	            	 }
	            	 
	            	 //affiliation address	            	 
	            	 String affiliation_address ="";
	            	 if (affiliationJSONObject.has("address")){
		            	 affiliation_address = affiliationJSONObject.getString("address");
		            	 Log.d(gTag, "aff address: " + affiliation_address);
	            	 }
	            	 //affiliation position - (different for each abstract)
	            	 //int affiliation_position = affiliationJSONObject.getInt("position");
	            	 //Log.d(gTag, "aff position: " + affiliation_position);
	            	 int affiliation_position = j;
	            	 
	            	 /*
	            	  * Check if affiliation is already parsed. If not, save it else skip
	            	  */
	            	 if(!PARSED_Affiliation_UUIDs.contains(affiliation_uuid)) {
	            		 PARSED_Affiliation_UUIDs.add(affiliation_uuid);
	            		 AffiliationDetails tempAffiliationDetails = new AffiliationDetails(affiliation_uuid, affiliation_address, affiliation_country, affiliation_department, affiliation_section);
	            		 
	            		 /*
	    	              * Insertion of parsed Affiliation in Arraylist
	    	              */
	            		 AFFILIATION_DETAILS_POJOS_ARRAY.add(tempAffiliationDetails);
	            		 tempAffiliationDetails = null;
	            	 }
	            	 
	            	 AbstractAffiliationIdPosition tempABSTRACT_AFFILIATION_ID_POSITION_POJO = new AbstractAffiliationIdPosition(abs_uuid, affiliation_uuid, affiliation_position);
	            	 
	            	 /*
		              * Insertion into Arraylist of ABSTRACT_AFFILIATION_ID_POSITION_POJO
		              * to maintain affiliation position against particular abstract in ABSTRACT_AFFILIATION_ID_POSITION table
		              */
	            	 ABSTRACT_AFFILIATION_ID_POSITION_POJOS_ARRAY.add(tempABSTRACT_AFFILIATION_ID_POSITION_POJO);
	            	 
	             }//loop end for each affiliation object
				 
	             //Abstract Authors JSONarray
	             JSONArray abs_authors_Array = abstractJson.getJSONArray("authors");
	             
	             //now iterate over authors array to extract each author information
	             for (int j=0; j<abs_authors_Array.length(); j++) {
	            	 //get author object
	            	 
	            	 JSONObject authorJSONObject = abs_authors_Array.getJSONObject(j);
	            	 
	            	 if(!authorJSONObject.isNull("lastName") ){
		            	 //author UUID
		            	 String author_uuid = authorJSONObject.getString("uuid");
		            	 Log.d(gTag, "auth uuid: " + author_uuid);
		            	 
		            	 //author first Name
		            	 String author_fName = authorJSONObject.getString("firstName");
		            	 Log.d(gTag, "auth first name: " + author_fName);
		            	 
		            	 //author last Name
		            	 String author_lName = authorJSONObject.getString("lastName");
		            	 Log.d(gTag, "auth last name: " + author_lName);
		            	 
		            	 //author middle Name
		            	 String author_middleName = authorJSONObject.getString("middleName");
		            	 Log.d(gTag, "auth middle name: " + author_middleName);
		            	 
		            	 //author mail
		            	 String author_mail = "";
		            	 if (authorJSONObject.has("mail")){
		            		author_mail = authorJSONObject.getString("mail");
		            	 	Log.d(gTag, "auth mail: " + author_mail);	            	 
		            	 }
		            	 
		            	 //author position (unique for each abstract)
		            	 //int author_position = authorJSONObject.getInt("position");
		            	 //Log.d(gTag, "auth position: " + author_position);
		            	 int author_position = j;
		            	 
		            	 //now get affiliations of a particular author for an abstract for example
		            	 // "affiliations": [0,1]
		            	 JSONArray authorAffiliationsArray = authorJSONObject.getJSONArray("affiliations");
		            	 Log.d(gTag, "auth affiliations: " + authorAffiliationsArray.toString());
		            	 
		            	 if(!PARSED_Author_UUIDs.contains(author_uuid)) {
		            		 PARSED_Author_UUIDs.add(author_uuid);
		            		 
		            		 /*
				              * Insertion into Arraylist of AUTHORS_DETAILS_POJO
				              */
		            		 AuthorsDetails tempAuthorDetails = new AuthorsDetails(author_uuid, author_fName, author_lName, author_middleName, author_mail);
		            		 AUTHORS_DETAILS_POJOS_ARRAY.add(tempAuthorDetails);
		            		 tempAuthorDetails = null;
		            	 }
		            	 
		            	 //Remove brackets from author affiliation that's to be written in super script
		            	 String authorAffiliationsWithoutBraces = authorAffiliationsArray.toString().replaceAll("\\[", "").replaceAll("\\]", "");
		            	 
		            	 /*
			              * Insertion into Arraylist of ABSTRACT_AUTHOR_POSITION_AFFILIATION_POJO
			              */
		            	 AbsractAuthorPositionAffiliation tempAbsAuthPosAff = new AbsractAuthorPositionAffiliation(abs_uuid, author_uuid, author_position, authorAffiliationsWithoutBraces);
		            	 ABSTRACT_AUTHOR_POSITION_AFFILIATION_POJOS_ARRAY.add(tempAbsAuthPosAff);
	            	 }
	             } //end authors array loop
	             
	             //Abstract Figures JSONArray
	             //JSONArray abs_fugures_array = jsonObject.getJSONArray("figures");
	             JSONArray abs_figures_array = abstractJson.getJSONArray("figures");
	             //now iterate over this array for extracting each figure detail, if it's length is greater than 0
	             if(abs_figures_array.length() > 0){
	            	 
	            	 for(int j=0; j<abs_figures_array.length(); j++){
	            		 //get figure json object
	            		 JSONObject figureJSONObject = abs_figures_array.getJSONObject(j);
	            		 
	            		 //Figure UUID
	            		 String figure_uuid = figureJSONObject.getString("uuid");
	            		 Log.d(gTag, "Fig uuid: " + figure_uuid);
	            		 
	            		//Figure Caption
	            		String figure_caption = figureJSONObject.getString("caption");
	            		Log.d(gTag, "Fig caption: " + figure_caption);
	            		
	            		//Figure URL
	            		String figure_URL = figureJSONObject.getString("URL");
	            		Log.d(gTag, "Fig URL: " + figure_URL);
	            		
	            		//Figure position
	            		String figure_position = figureJSONObject.getString("position");
	            		Log.d(gTag, "Fig position: " + figure_position);
	            		
	            		/*
			              * Insertion into Arraylist of ABSTRACT_FIGURES_POJO
			              */
	            		AbstractFigures tempAbsFig = new AbstractFigures(abs_uuid, figure_uuid, figure_caption, figure_URL, figure_position);
	            		ABSTRACT_FIGURES_POJOS_ARRAY.add(tempAbsFig);
	            	 
	            	 } //end figures array loop
	             } //end if
	             
	             //Abstract references JSONarray
	             if(abstractJson.has("references")){
		             JSONArray abs_References_Array = abstractJson.getJSONArray("references");
		             
		             //now iterate over this array for extracting each reference
			             for (int j=0; j<abs_References_Array.length(); j++) {
			            	 //get reference object
			            	 JSONObject referenceJSONObject = abs_References_Array.getJSONObject(j);
			            	 
			            	 //Reference UUID
			            	 String reference_uuid = referenceJSONObject.getString("uuid");
			            	 Log.d(gTag, "ref uuid: " + reference_uuid);
			            	 
			            	 //Reference text
			            	 String reference_text = referenceJSONObject.getString("text");
			            	 Log.d(gTag, "ref text: " + reference_text);
			            	 
			            	 //Reference link
			            	 String reference_link = referenceJSONObject.getString("link");
			            	 Log.d(gTag, "ref link: " + reference_link);
			            	 
			            	 
			            	 //Reference DOI
			            	 String reference_doi = referenceJSONObject.getString("doi");
			            	 Log.d(gTag, "ref DOI: " + reference_doi);
			            	 
			            	 /*
				              * Insertion of reference into Arraylist of ABSTRACT_REFERENCES_POJO
				              */
			            	 AbsractReferences tempAbsRef = new AbsractReferences(abs_uuid, reference_uuid, reference_text, reference_link, reference_doi);
			            	 ABSTRACT_REFERENCES_POJOS_ARRAY.add(tempAbsRef);
			             	             
			             }
		            }//end references array loop
	             
			 }//end abstracts array parsing
		
		} catch (FileNotFoundException e) {
			Log.e("AbtractJsonParse", Log.getStackTraceString( e ));
        } catch (IOException e) {
        	Log.e("AbtractJsonParse", Log.getStackTraceString( e ));
        } catch (JSONException e) {
            Log.e("AbtractJsonParse", Log.getStackTraceString( e ));
        }
		
		Log.d(gTag, "Size: - ABSTRACT_AFFILIATION_ID_POSITION_POJOS_ARRAY : " + ABSTRACT_AFFILIATION_ID_POSITION_POJOS_ARRAY.size());
		Log.d(gTag, "Size: - ABSTRACT_AUTHOR_POSITION_AFFILIATION_POJOS_ARRAY : " + ABSTRACT_AUTHOR_POSITION_AFFILIATION_POJOS_ARRAY.size());
		Log.d(gTag, "Size: - ABSTRACT_DETAILS_POJOS_ARRAY : " + ABSTRACT_DETAILS_POJOS_ARRAY.size());
		Log.d(gTag, "Size: - ABSTRACT_FIGURES_POJOS_ARRAY : " + ABSTRACT_FIGURES_POJOS_ARRAY.size());
		Log.d(gTag, "Size: - ABSTRACT_REFERENCES_POJOS_ARRAY : " + ABSTRACT_REFERENCES_POJOS_ARRAY.size());
		Log.d(gTag, "Size: - AFFILIATION_DETAILS_POJOS_ARRAY : " + AFFILIATION_DETAILS_POJOS_ARRAY.size());
		Log.d(gTag, "Size: - AUTHORS_DETAILS_POJOS_ARRAY : " + AUTHORS_DETAILS_POJOS_ARRAY.size());

		
	/*
	 * It just gets number of abstracts from it's table and returns. 
	 * Not really used, but good to return - helper for debugging	
	 */
	
    noOfRecords = dbHelper.fetchAbstractsUUIDs().size();
	
	return noOfRecords;
	}	//end json parsing

	
	/*
	 * After the parsing is complete and ArrayLists are loaded, this helper function migrates the data from
	 * arraylists to relevant tables. For populating each table from it's relevant arraylist, there's respective funciton
	 * in DatabaseHelper class
	 */
	public void saveFromArrayListtoDB() {
		
		/*
		 * Populating all tables respectively
		 */
		dbHelper.populateABSTRACT_AFFILIATION_ID_POSITION(ABSTRACT_AFFILIATION_ID_POSITION_POJOS_ARRAY);
		dbHelper.populateABSTRACT_AUTHOR_POSITION_AFFILIATION(ABSTRACT_AUTHOR_POSITION_AFFILIATION_POJOS_ARRAY);
		dbHelper.populateABSTRACT_DETAILS(ABSTRACT_DETAILS_POJOS_ARRAY);
		dbHelper.populateABSTRACT_FIGURES(ABSTRACT_FIGURES_POJOS_ARRAY);
		dbHelper.populateABSTRACT_REFERENCES(ABSTRACT_REFERENCES_POJOS_ARRAY);
		dbHelper.populateAFFILIATION_DETAILS(AFFILIATION_DETAILS_POJOS_ARRAY);
		dbHelper.populateAUTHORS_DETAILS(AUTHORS_DETAILS_POJOS_ARRAY);
	}
	

	public ArrayList<AbstractAffiliationIdPosition> getABSTRACT_AFFILIATION_ID_POSITION_POJOS_ARRAY() {
		return ABSTRACT_AFFILIATION_ID_POSITION_POJOS_ARRAY;
	}

	public ArrayList<AbsractAuthorPositionAffiliation> getABSTRACT_AUTHOR_POSITION_AFFILIATION_POJOS_ARRAY() {
		return ABSTRACT_AUTHOR_POSITION_AFFILIATION_POJOS_ARRAY;
	}

	public ArrayList<AbstractDetails> getABSTRACT_DETAILS_POJOS_ARRAY() {
		return ABSTRACT_DETAILS_POJOS_ARRAY;
	}

	public ArrayList<AbstractFigures> getABSTRACT_FIGURES_POJOS_ARRAY() {
		return ABSTRACT_FIGURES_POJOS_ARRAY;
	}

	public ArrayList<AbsractReferences> getABSTRACT_REFERENCES_POJOS_ARRAY() {
		return ABSTRACT_REFERENCES_POJOS_ARRAY;
	}

	public ArrayList<AffiliationDetails> getAFFILIATION_DETAILS_POJOS_ARRAY() {
		return AFFILIATION_DETAILS_POJOS_ARRAY;
	}

	public ArrayList<AuthorsDetails> getAUTHORS_DETAILS_POJOS_ARRAY() {
		return AUTHORS_DETAILS_POJOS_ARRAY;
	}

	public ArrayList<String> getPARSED_Affiliation_UUIDs() {
		return PARSED_Affiliation_UUIDs;
	}

	public ArrayList<String> getPARSED_Author_UUIDs() {
		return PARSED_Author_UUIDs;
	}

}