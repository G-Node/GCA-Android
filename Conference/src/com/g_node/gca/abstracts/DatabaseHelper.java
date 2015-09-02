/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * Copyright (c) 2013, Yasir Adnan <adnan.ayon@gmail.com> 
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.abstracts;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.g_node.gca.abstracts.pojo.*;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private String gtag = "GCA-DB";
	
	private static String Database_Name = "gca.db";

    private static int Database_Version = 5;

    public static SQLiteDatabase database;
    
    /*
     * Tables Name
     */
    public static final String TABLE_ABSTRACT_DETAILS = "ABSTRACT_DETAILS";
    
    public static final String TABLE_AUTHORS_DETAILS = "AUTHORS_DETAILS";
    
    public static final String TABLE_ABSTRACT_AUTHOR_POSITION_AFFILIATION = "ABSTRACT_AUTHOR_POSITION_AFFILIATION";
    
    public static final String TABLE_AFFILIATION_DETAILS = "AFFILIATION_DETAILS";
    
    public static final String TABLE_ABSTRACT_AFFILIATION_ID_POSITION = "ABSTRACT_AFFILIATION_ID_POSITION";
    
    public static final String TABLE_ABSTRACT_REFERENCES = "ABSTRACT_REFERENCES";
    
    public static final String TABLE_ABSTRACT_FIGURES = "ABSTRACT_FIGURES";
    
    public static final String TABLE_ABSTRACT_FAVORITES = "ABSTRACT_FAVORITES";
    
    public static final String TABLE_ABSTRACT_NOTES = "ABSTRACT_NOTES";
    
    /*
     * Query for Creating Tables
     */    
    public static final String CREATE_ABSTRACT_DETAILS = "CREATE TABLE IF NOT EXISTS ABSTRACT_DETAILS"
            + "(UUID VARCHAR PRIMARY KEY, TOPIC TEXT NOT NULL, "
            + "TITLE TEXT NOT NULL, ABSRACT_TEXT TEXT NOT NULL,"
            + "STATE TEXT NOT NULL, SORTID INTEGER NOT NULL, REASONFORTALK TEXT," 
            + "MTIME TEXT NOT NULL, TYPE TEXT NOT NULL, DOI TEXT, COI TEXT,"
            + "ACKNOWLEDGEMENTS TEXT );";
    
    public static final String CREATE_AUTHORS_DETAILS = "CREATE TABLE IF NOT EXISTS AUTHORS_DETAILS"
            + "( AUTHOR_UUID VARCHAR PRIMARY KEY, AUTHOR_FIRST_NAME TEXT NOT NULL, AUTHOR_MIDDLE_NAME TEXT, " 
    		+ "AUTHOR_LAST_NAME TEXT NOT NULL, AUTHOR_EMAIL TEXT);";
    
    public static final String CREATE_ABSTRACT_AUTHOR_POSITION_AFFILIATION = "CREATE TABLE IF NOT EXISTS ABSTRACT_AUTHOR_POSITION_AFFILIATION"
            + "( ABSTRACT_UUID VARCHAR NOT NULL, AUTHOR_UUID VARCHAR NOT NULL, " 
    		+ "AUTHOR_POSITION INTEGER NOT NULL, AUTHOR_AFFILIATION VARCHAR NOT NULL);";
    
    public static final String CREATE_AFFILIATION_DETAILS = "CREATE TABLE IF NOT EXISTS " + TABLE_AFFILIATION_DETAILS
            + "(AFFILIATION_UUID VARCHAR PRIMARY KEY, AFFILIATION_ADDRESS TEXT NOT NULL, AFFILIATION_COUNTRY TEXT NOT NULL, " 
    		+ "AFFILIATION_DEPARTMENT TEXT NOT NULL, AFFILIATION_SECTION TEXT);";
    
    public static final String CREATE_ABSTRACT_AFFILIATION_ID_POSITION = "CREATE TABLE IF NOT EXISTS " + TABLE_ABSTRACT_AFFILIATION_ID_POSITION 
            + "( ABSTRACT_UUID VARCHAR NOT NULL, AFFILIATION_UUID VARCHAR NOT NULL, " 
    		+ "AFFILIATION_POSITION INTEGER NOT NULL);";
    
    
    public static final String CREATE_ABSTRACT_REFERENCES = "CREATE TABLE IF NOT EXISTS " + TABLE_ABSTRACT_REFERENCES
    			+ "( ABSTRACT_UUID VARCHAR NOT NULL, REF_UUID VARCHAR NOT NULL, " 
    			+ "REF_TEXT TEXT, REF_LINK TEXT, REF_DOI TEXT);"; 
    
    public static final String CREATE_ABSTRACT_FAVORITES = "CREATE TABLE IF NOT EXISTS " + TABLE_ABSTRACT_FAVORITES
    		+ "( ABSTRACT_UUID VARCHAR NOT NULL); " ;
    		
    public static final String CREATE_ABSTRACT_NOTES = "CREATE TABLE IF NOT EXISTS " + TABLE_ABSTRACT_NOTES
    		+ "(NOTE_ID INTEGER PRIMARY KEY,  ABSTRACT_UUID VARCHAR NOT NULL, NOTE_TITLE TEXT, NOTE_TEXT TEXT); " ;
    
    public static final String CREATE_ABSTRACT_FIGURES = "CREATE TABLE IF NOT EXISTS " + TABLE_ABSTRACT_FIGURES
			+ "( ABSTRACT_UUID VARCHAR NOT NULL, FIG_UUID VARCHAR NOT NULL, " 
			+ "FIG_CAPTION TEXT, FIG_URL TEXT, FIG_POSITION TEXT);";
    
    public DatabaseHelper(Context context) {
        super(context, Database_Name, null, Database_Version);
    }

	@Override
	public void onCreate(SQLiteDatabase database) {
		
		/*
         * Creating Tables
         */
		database.execSQL(CREATE_ABSTRACT_DETAILS);
		database.execSQL(CREATE_AUTHORS_DETAILS);
		database.execSQL(CREATE_ABSTRACT_AUTHOR_POSITION_AFFILIATION);
		database.execSQL(CREATE_AFFILIATION_DETAILS);
		database.execSQL(CREATE_ABSTRACT_AFFILIATION_ID_POSITION);
		database.execSQL(CREATE_ABSTRACT_REFERENCES);
		database.execSQL(CREATE_ABSTRACT_FAVORITES);
		database.execSQL(CREATE_ABSTRACT_NOTES);
		database.execSQL(CREATE_ABSTRACT_FIGURES);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_DETAILS);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHORS_DETAILS);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_AUTHOR_POSITION_AFFILIATION);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_AFFILIATION_DETAILS);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_AFFILIATION_ID_POSITION);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_REFERENCES);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_FAVORITES);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_NOTES);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_FIGURES);
		onCreate(database);
		
	}

	/*
     * Opening Database
     */
	public void open() {
        
        database = this.getWritableDatabase();
    }
	
	/*
     * Closing Database
     */
	public void close(String string) {
		Log.d("exc", "DESTROYED:" + string);
        database.close();
    }
	
	/*
	 * Helper function for Dropping all tables and creating again
	 * In case db is not consistent, it'll drop and build whole db again
	 * Note that we only need to drop Abstracts data related tables
	 * We won't dorp Notes and Favorites table
	 */
	public void dropAllTablesAndCreateAgain() {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_DETAILS);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHORS_DETAILS);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_AUTHOR_POSITION_AFFILIATION);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_AFFILIATION_DETAILS);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_AFFILIATION_ID_POSITION);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_REFERENCES);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_FIGURES);
		onCreate(database);
		
	}
	
	/*
	 * function for adding to ABSTRACT_FAVORITES Table when a user favourites some abstract
	 */
	public static void addInABSTRACT_FAVORITES (String abstract_uuid) {
			
		ContentValues values = new ContentValues();
		
		values.put("ABSTRACT_UUID", abstract_uuid);
		
		long abs_fav_id = database.insert(TABLE_ABSTRACT_FAVORITES, null, values);
		Log.d("GCA-DB", "abstract favourited - id: " + abs_fav_id);
	}
	
	/*
	 * function for deleting from ABSTRACT_FAVORITES if user un-favorites some abstract
	 */
	public static void deleteFromABSTRACT_FAVORITES (String abstract_uuid) {
		
		long rows_affected = database.delete(TABLE_ABSTRACT_FAVORITES, "ABSTRACT_UUID = ?", new String[] { abstract_uuid });
		Log.d("GCA-DB", "deleted abstract from fav - no: " + rows_affected);
	}
	
	/*
	 * Function for adding notes for some abstract into Database TABLE_ABSTRACT_NOTES
	 */
	public static void addInABSTRACT_NOTES(String abstractUUID, String noteTitle, String NoteText) {
		
		ContentValues values = new ContentValues();
		
		values.put("ABSTRACT_UUID", abstractUUID);
		
		values.put("NOTE_TITLE", noteTitle);
		
		values.put("NOTE_TEXT", NoteText);
		
		long note_id;
		note_id = database.insert(TABLE_ABSTRACT_NOTES, null, values);
		Log.d("GCA-DB", "Note inserted: id = > " + note_id);
	}
	
	/*
	 * Function for delete notes for some abstract from Database TABLE_ABSTRACT_NOTES
	 */
	public static void deleteFromABSTRACT_NOTES(long id) {
		long rows_affected = database.delete(TABLE_ABSTRACT_NOTES, "NOTE_ID = ?", new String[] { String.valueOf(id)});
		Log.d("GCA-DB", "deleted Note from db  - no: " + rows_affected);
	}
	
	/*
	 * Function for updating the Note if user edits it
	 */
	public static void updateNoteABSTRACT_NOTES(String note_id, String noteTitle, String NoteText) {
		
		ContentValues values = new ContentValues();
		
		values.put("NOTE_TITLE", noteTitle);
		
		values.put("NOTE_TEXT", NoteText);
		
		long rows_affected = database.update(TABLE_ABSTRACT_NOTES, values, "NOTE_ID = ?", new String[] { note_id} );
		Log.d("GCA-DB", "Updated Note from db  - no: " + rows_affected);
	}
	
	
	/*
	 * function to check if Abstract is already favorited and exists in table
	 */
	public static boolean abstractIsFavorite(String UUID) {
        Cursor cursor = database.rawQuery("select 1 from " + TABLE_ABSTRACT_FAVORITES + " where ABSTRACT_UUID like '%" + UUID
                + "%'", null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        Log.d("GCA-DB", "Abstract is Fav: " + exists);
        return exists;
    }
	
	/*
	 * Main search function for Abstracts
	 */
	public Cursor fetchDataByName(String string) {

        Cursor cursor = database
                .rawQuery(
                        "SELECT UUID AS _id , TOPIC, TITLE, ABSRACT_TEXT, STATE, SORTID, " +
                        "REASONFORTALK, MTIME, TYPE,DOI, COI, ACKNOWLEDGEMENTS " +
                        "FROM ABSTRACT_DETAILS WHERE ABSRACT_TEXT like '%" + string + "%' OR TITLE like '%" + string + "%' OR " +
                        " _id in (SELECT ABSTRACT_UUID FROM ABSTRACT_AFFILIATION_ID_POSITION " +
                        		"WHERE AFFILIATION_UUID IN (SELECT AFFILIATION_UUID FROM AFFILIATION_DETAILS " +
                        		"WHERE AFFILIATION_SECTION LIKE '%" + string + "%' OR " +
                        			   "AFFILIATION_DEPARTMENT LIKE '%" + string + "%' OR " +
                        			   "AFFILIATION_ADDRESS LIKE '%" + string + "%' OR " +
                        			   "AFFILIATION_COUNTRY LIKE '%" + string + "%')) OR " +
                        " _id in (SELECT ABSTRACT_UUID FROM ABSTRACT_AUTHOR_POSITION_AFFILIATION " +
                        		"WHERE AUTHOR_UUID IN (SELECT AUTHOR_UUID FROM AUTHORS_DETAILS " +
                        		"WHERE AUTHOR_FIRST_NAME || ' ' || AUTHOR_LAST_NAME LIKE '%" + string + "%' OR AUTHOR_LAST_NAME LIKE '%" + string + "%' OR AUTHOR_FIRST_NAME LIKE '%" + string + "%')) ;", null);

        return cursor;
    }

	/*
	 * Populating ABSTRACT_DETAILS Table from Arraylist ABSTRACT_DETAILS_POJOS_ARRAY that was populated while Parsing
	 */
	public void populateABSTRACT_DETAILS(ArrayList<ABSTRACT_DETAILS_POJO> ABSTRACT_DETAILS_POJOS_ARRAY) {
		
		String sql = "INSERT INTO " + TABLE_ABSTRACT_DETAILS + " (UUID, TOPIC, TITLE, ABSRACT_TEXT, STATE, SORTID, REASONFORTALK, MTIME, TYPE, DOI, COI, ACKNOWLEDGEMENTS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";
		SQLiteStatement statement = database.compileStatement(sql);		
		database.beginTransaction();
		
		Log.d(gtag, "Starting - Insert into Abstract Details");
		
		for(int i=0; i<ABSTRACT_DETAILS_POJOS_ARRAY.size(); i++){
			
			ABSTRACT_DETAILS_POJO tempAbstractDetailsToSave = ABSTRACT_DETAILS_POJOS_ARRAY.get(i); 
						
			statement.clearBindings();
			
			statement.bindString(1, tempAbstractDetailsToSave.getUuid());
			statement.bindString(2, tempAbstractDetailsToSave.getTopic());
			statement.bindString(3, tempAbstractDetailsToSave.getTitle());
			statement.bindString(4, tempAbstractDetailsToSave.getText());
			statement.bindString(5, tempAbstractDetailsToSave.getState());
			statement.bindLong(6, tempAbstractDetailsToSave.getSortID());
			statement.bindString(7, tempAbstractDetailsToSave.getReasonForTalk());
			statement.bindString(8, tempAbstractDetailsToSave.getMtime());
			statement.bindString(9, tempAbstractDetailsToSave.getAbstractType());
			statement.bindString(10, tempAbstractDetailsToSave.getDoi());
			statement.bindString(11, tempAbstractDetailsToSave.getCoi());
			statement.bindString(12, tempAbstractDetailsToSave.getAcknowledgements());
			
			long y = statement.executeInsert();
			Log.d(gtag, "INSERTED - Abstract " + i + " in Abstract Details. ID: " + y);
			tempAbstractDetailsToSave = null;
		}
		statement.close();
		database.setTransactionSuccessful();
		database.endTransaction();
	}
	
	
	/*
	 * Populating ABSTRACT_AFFILIATION_ID_POSITION Table 
	 * from Arraylist ABSTRACT_AFFILIATION_ID_POSITION_POJOS_ARRAY that was populated while Parsing
	 */
	public void populateABSTRACT_AFFILIATION_ID_POSITION(
			ArrayList<ABSTRACT_AFFILIATION_ID_POSITION_POJO> ABSTRACT_AFFILIATION_ID_POSITION_POJOS_ARRAY) {
		
		String sql = "INSERT INTO " + TABLE_ABSTRACT_AFFILIATION_ID_POSITION + " (ABSTRACT_UUID, AFFILIATION_UUID, AFFILIATION_POSITION) VALUES(?,?,?);";
		SQLiteStatement statement = database.compileStatement(sql);		
		database.beginTransaction();
		
		Log.d(gtag, "Starting - Insert into ABSTRACT_AFFILIATION_ID_POSITION");
		
		for(int i=0; i<ABSTRACT_AFFILIATION_ID_POSITION_POJOS_ARRAY.size(); i++){
			
			ABSTRACT_AFFILIATION_ID_POSITION_POJO temp = ABSTRACT_AFFILIATION_ID_POSITION_POJOS_ARRAY.get(i); 
						
			statement.clearBindings();
			
			statement.bindString(1, temp.getAbstract_UUID());
			statement.bindString(2, temp.getAffiliation_UUID());
			statement.bindLong(3, temp.getAffiliation_position());
			
			long y = statement.executeInsert();
			Log.d(gtag, "INSERTED - ABSTRACT_AFFILIATION_ID_POSITION " + i + " in ABSTRACT_AFFILIATION_ID_POSITION. ID: " + y);
			temp = null;
		}
		statement.close();
		database.setTransactionSuccessful();
		database.endTransaction();
		
	}
	
	/*
	 * Populating ABSTRACT_AUTHOR_POSITION_AFFILIATION Table 
	 * from Arraylist ABSTRACT_AUTHOR_POSITION_AFFILIATION_POJOS_ARRAY that was populated while Parsing
	 */
	public void populateABSTRACT_AUTHOR_POSITION_AFFILIATION(
			ArrayList<ABSTRACT_AUTHOR_POSITION_AFFILIATION_POJO> ABSTRACT_AUTHOR_POSITION_AFFILIATION_POJOS_ARRAY) {
		
		String sql = "INSERT INTO " + TABLE_ABSTRACT_AUTHOR_POSITION_AFFILIATION + " (ABSTRACT_UUID, AUTHOR_UUID, AUTHOR_POSITION, AUTHOR_AFFILIATION) VALUES(?,?,?,?);";
		SQLiteStatement statement = database.compileStatement(sql);		
		database.beginTransaction();
		
		Log.d(gtag, "Starting - Insert into ABSTRACT_AUTHOR_POSITION_AFFILIATION");
		
		for(int i=0; i<ABSTRACT_AUTHOR_POSITION_AFFILIATION_POJOS_ARRAY.size(); i++){
			
			ABSTRACT_AUTHOR_POSITION_AFFILIATION_POJO temp = ABSTRACT_AUTHOR_POSITION_AFFILIATION_POJOS_ARRAY.get(i); 
						
			statement.clearBindings();
			
			statement.bindString(1, temp.getAbstract_uuid());
			statement.bindString(2, temp.getAuthor_uuid());
			statement.bindLong(3, temp.getAuthor_position());
			statement.bindString(4, temp.getAuthor_affiliation());
			
			long y = statement.executeInsert();
			Log.d(gtag, "INSERTED - ABSTRACT_AUTHOR_POSITION_AFFILIATION" + i + " in ABSTRACT_AUTHOR_POSITION_AFFILIATION. ID: " + y);
			temp = null;
		}
		statement.close();
		database.setTransactionSuccessful();
		database.endTransaction();
		
	}
	
	
	/*
	 * Populating ABSTRACT_FIGURES Table 
	 * from Arraylist ABSTRACT_FIGURES_POJOS_ARRAY that was populated while Parsing
	 */
	public void populateABSTRACT_FIGURES(
			ArrayList<ABSTRACT_FIGURES_POJO> ABSTRACT_FIGURES_POJOS_ARRAY2) {
		
		String sql = "INSERT INTO " + TABLE_ABSTRACT_FIGURES + " (ABSTRACT_UUID, FIG_UUID, FIG_CAPTION, FIG_URL, FIG_POSITION) VALUES(?,?,?,?,?);";
		SQLiteStatement statement = database.compileStatement(sql);		
		database.beginTransaction();
		
		Log.d(gtag, "Starting - Insert into ABSTRACT_FIGURES");
		
		for(int i=0; i<ABSTRACT_FIGURES_POJOS_ARRAY2.size(); i++){
			
			ABSTRACT_FIGURES_POJO temp = ABSTRACT_FIGURES_POJOS_ARRAY2.get(i); 
						
			statement.clearBindings();
			statement.bindString(1, temp.getAbstract_uuid());
			statement.bindString(2, temp.getFigure_uuid());
			statement.bindString(3, temp.getFigure_caption());
			statement.bindString(4, temp.getFigure_URL());
			statement.bindString(5, temp.getFigure_position());
			
			long y = statement.executeInsert();
			Log.d(gtag, "INSERTED - ABSTRACT_FIGURES" + i + " in ABSTRACT_FIGURES. ID: " + y);
			temp = null;
		}
		statement.close();
		database.setTransactionSuccessful();
		database.endTransaction();
			
	}

	
	/*
	 * Populating ABSTRACT_REFERENCES Table 
	 * from Arraylist ABSTRACT_REFERENCES_POJOS_ARRAY that was populated while Parsing
	 */
	public void populateABSTRACT_REFERENCES(
			ArrayList<ABSTRACT_REFERENCES_POJO> ABSTRACT_REFERENCES_POJOS_ARRAY2) {
		
		String sql = "INSERT INTO " + TABLE_ABSTRACT_REFERENCES + " (ABSTRACT_UUID, REF_UUID, REF_TEXT, REF_LINK, REF_DOI) VALUES(?,?,?,?,?);";
		SQLiteStatement statement = database.compileStatement(sql);		
		database.beginTransaction();
		
		Log.d(gtag, "Starting - Insert into ABSTRACT_REFERENCES");
		
		for(int i=0; i<ABSTRACT_REFERENCES_POJOS_ARRAY2.size(); i++){
			
			ABSTRACT_REFERENCES_POJO temp = ABSTRACT_REFERENCES_POJOS_ARRAY2.get(i); 
						
			statement.clearBindings();
			
			statement.bindString(1, temp.getAbstract_uuid());
			statement.bindString(2, temp.getReference_uuid());
			statement.bindString(3, temp.getReference_text());
			statement.bindString(4, temp.getReference_link());
			statement.bindString(5, temp.getReference_doi());
			
			long y = statement.executeInsert();
			Log.d(gtag, "INSERTED - ABSTRACT_REFERENCES" + i + " in ABSTRACT_REFERENCES. ID: " + y);
			temp = null;
		}
		statement.close();
		database.setTransactionSuccessful();
		database.endTransaction();
		
	}

	/*
	 * Populating AFFILIATION_DETAILS Table 
	 * from Arraylist ABSTRACT_REFERENCES_POJOS_ARRAY that was populated while Parsing
	 */
	public void populateAFFILIATION_DETAILS(
			ArrayList<AFFILIATION_DETAILS_POJO> AFFILIATION_DETAILS_POJOS_ARRAY2) {
		
		String sql = "INSERT INTO " + TABLE_AFFILIATION_DETAILS + " (AFFILIATION_UUID, AFFILIATION_ADDRESS, AFFILIATION_COUNTRY, AFFILIATION_DEPARTMENT, AFFILIATION_SECTION) VALUES(?,?,?,?,?);";
		SQLiteStatement statement = database.compileStatement(sql);		
		database.beginTransaction();
		
		Log.d(gtag, "Starting - Insert into AFFILIATION_DETAILS");
		
		for(int i=0; i<AFFILIATION_DETAILS_POJOS_ARRAY2.size(); i++){
			
			AFFILIATION_DETAILS_POJO temp = AFFILIATION_DETAILS_POJOS_ARRAY2.get(i); 
						
			statement.clearBindings();
			
			statement.bindString(1, temp.getAffiliation_uuid());
			statement.bindString(2, temp.getAffiliation_address());
			statement.bindString(3, temp.getAffiliation_country());
			statement.bindString(4, temp.getAffiliation_department());
			statement.bindString(5, temp.getAffiliation_section());
			
			long y = statement.executeInsert();
			Log.d(gtag, "INSERTED - AFFILIATION_DETAILS" + i + " in AFFILIATION_DETAILS. ID: " + y);
			temp = null;
		}
		statement.close();
		database.setTransactionSuccessful();
		database.endTransaction();		
		
	}

	/*
	 * Populating AUTHORS_DETAILS Table 
	 * from Arraylist AUTHORS_DETAILS_POJOS_ARRAY that was populated while Parsing
	 */	
	public void populateAUTHORS_DETAILS(
			ArrayList<AUTHORS_DETAILS_POJO> AUTHORS_DETAILS_POJOS_ARRAY2) {
		
		String sql = "INSERT INTO " + TABLE_AUTHORS_DETAILS + " (AUTHOR_UUID, AUTHOR_FIRST_NAME, AUTHOR_MIDDLE_NAME, AUTHOR_LAST_NAME, AUTHOR_EMAIL) VALUES(?,?,?,?,?);";
		SQLiteStatement statement = database.compileStatement(sql);		
		database.beginTransaction();
		
		Log.d(gtag, "Starting - Insert into AUTHORS_DETAILS");
		
		for(int i=0; i<AUTHORS_DETAILS_POJOS_ARRAY2.size(); i++){
			
			AUTHORS_DETAILS_POJO temp = AUTHORS_DETAILS_POJOS_ARRAY2.get(i); 
						
			statement.clearBindings();
			
			statement.bindString(1, temp.getAuthor_uuid());
			statement.bindString(2, temp.getAuthor_fName());
			statement.bindString(3, temp.getAuthor_middleName());
			statement.bindString(4, temp.getAuthor_lName());
			statement.bindString(5, temp.getAuthor_email());
			
			long y = statement.executeInsert();
			Log.d(gtag, "INSERTED - AUTHORS_DETAILS" + i + " in AUTHORS_DETAILS. ID: " + y);
			temp = null;
		}
		statement.close();
		database.setTransactionSuccessful();
		database.endTransaction();
		
	}
	
	public ArrayList<String> fetchExistingAbstractsUUID() {
		
		ArrayList<String> existingAbstractsUUID = new ArrayList<String>();
		
		Cursor cursor = database.rawQuery("SELECT * FROM ABSTRACT_DETAILS;", null);
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()) {
			
			String temp = cursor.getString(cursor.getColumnIndexOrThrow("UUID"));
			existingAbstractsUUID.add(temp);
			cursor.moveToNext();
			
		}
		
		return existingAbstractsUUID;
	}
	

	public ArrayList<String> fetchExistingAffiliationsUUID() {

		ArrayList<String> existingAffiliationUUID = new ArrayList<String>();
		
		Cursor cursor = database.rawQuery("SELECT * FROM AFFILIATION_DETAILS;", null);
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()) {
			
			String temp = cursor.getString(cursor.getColumnIndexOrThrow("AFFILIATION_UUID"));
			existingAffiliationUUID.add(temp);
			cursor.moveToNext();
		}
		
		return existingAffiliationUUID;
	}
	
	public ArrayList<String> fetchExistingAuthhorsUUID() {
		
		ArrayList<String> existingAuthorsUUID = new ArrayList<String>();
		
		Cursor cursor = database.rawQuery("SELECT * FROM AUTHORS_DETAILS;", null);
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()) {
			
			String temp = cursor.getString(cursor.getColumnIndexOrThrow("AUTHOR_UUID"));
			existingAuthorsUUID.add(temp);
			cursor.moveToNext();
		}
		
		return existingAuthorsUUID;
		
	}

	/*
	 * just a helper function to empty database, if required
	 */
	public void emptyDb() {
		database.execSQL("DELETE FROM " + TABLE_ABSTRACT_DETAILS);
	}
	
	public int getCountOfRowsInAbstractDetailsTable() {
		String sql = "SELECT * FROM ABSTRACT_DETAILS";
		Cursor cursor = database.rawQuery(sql, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}

	
	
}
