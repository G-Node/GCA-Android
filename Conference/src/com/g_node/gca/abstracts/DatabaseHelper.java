/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * Copyright (c) 2013, Yasir Adnan <adnan.ayon@gmail.com> 
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.abstracts;

import java.util.ArrayList;

import org.jsoup.helper.StringUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import static com.g_node.gca.abstracts.SqlStrings.*;

import com.g_node.gca.abstracts.pojo.*;

public class DatabaseHelper extends SQLiteOpenHelper {

	private String gtag = "GCA-DB";

	private static String Database_Name = "gca.db";

	private static int Database_Version = 5;

	private static SQLiteDatabase database;

	private static DatabaseHelper sInstance;

	private DatabaseHelper(Context context) {
		super(context, Database_Name, null, Database_Version);
		}

	public static synchronized DatabaseHelper getInstance( Context context ) {
		if (sInstance == null) {
			sInstance = new DatabaseHelper(context);
			}
		return sInstance;
		}
	

	@Override
	public void onCreate( SQLiteDatabase db ) {

		/*
		 * Creating Tables
		 */
		db.execSQL(CREATE_ABSTRACT_DETAILS);
		db.execSQL(CREATE_AUTHORS_DETAILS);
		db.execSQL(CREATE_ABSTRACT_AUTHOR_POSITION_AFFILIATION);
		db.execSQL(CREATE_AFFILIATION_DETAILS);
		db.execSQL(CREATE_ABSTRACT_AFFILIATION_ID_POSITION);
		db.execSQL(CREATE_ABSTRACT_REFERENCES);
		db.execSQL(CREATE_ABSTRACT_FAVORITES);
		db.execSQL(CREATE_ABSTRACT_NOTES);
		db.execSQL(CREATE_ABSTRACT_FIGURES);
		}

	@Override
	public void onUpgrade( SQLiteDatabase db, int oldVersion,
			int newVersion ) {

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_DETAILS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHORS_DETAILS);
		db.execSQL("DROP TABLE IF EXISTS "
				+ TABLE_ABSTRACT_AUTHOR_POSITION_AFFILIATION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_AFFILIATION_DETAILS);
		db.execSQL("DROP TABLE IF EXISTS "
				+ TABLE_ABSTRACT_AFFILIATION_ID_POSITION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_REFERENCES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_FAVORITES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_NOTES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_FIGURES);
		onCreate(db);
		}

	/*
	 * Opening Database
	 */
	public void open() {
		database = getWritableDatabase();
	}

	/*
	 * Closing Database
	 */
	public void close( String string ) {
		Log.d("exc", "DESTROYED:" + string);
		getReadableDatabase().close();
		}

	/*
	 * Helper function for Dropping all tables and creating again In case db is
	 * not consistent, it'll drop and build whole db again Note that we only
	 * need to drop Abstracts data related tables We won't dorp Notes and
	 * Favorites table
	 */
	public void dropAllCreateAgain() {
		SQLiteDatabase db = getWritableDatabase() ;
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_DETAILS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHORS_DETAILS);
		db.execSQL("DROP TABLE IF EXISTS "
				+ TABLE_ABSTRACT_AUTHOR_POSITION_AFFILIATION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_AFFILIATION_DETAILS);
		db.execSQL("DROP TABLE IF EXISTS "
				+ TABLE_ABSTRACT_AFFILIATION_ID_POSITION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_REFERENCES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_FIGURES);
		onCreate(db);
		}

	/*
	 * function for adding to ABSTRACT_FAVORITES Table when a user favourites
	 * some abstract
	 */
	public void addInABSTRACT_FAVORITES( String abstract_uuid ) {

		ContentValues values = new ContentValues();
		values.put("ABSTRACT_UUID", abstract_uuid);
		long abs_fav_id = getWritableDatabase().insert(TABLE_ABSTRACT_FAVORITES, null,
				values);
		Log.d("GCA-DB", "abstract favourited - id: " + abs_fav_id);
		}

	/*
	 * function for deleting from ABSTRACT_FAVORITES if user un-favorites some
	 * abstract
	 */
	public void deleteFromABSTRACT_FAVORITES( String abstract_uuid ) {

		long rows_affected = getWritableDatabase().delete(TABLE_ABSTRACT_FAVORITES,
				"ABSTRACT_UUID = ?", new String[] { abstract_uuid });
		Log.d("GCA-DB", "deleted abstract from fav - no: " + rows_affected);
		}

	/*
	 * Function for adding notes for some abstract into Database
	 * TABLE_ABSTRACT_NOTES
	 */
	public void addInABSTRACT_NOTES( String abstractUUID,
			String noteTitle, String NoteText ) {

		ContentValues values = new ContentValues();
		values.put("ABSTRACT_UUID", abstractUUID);
		values.put("NOTE_TITLE", noteTitle);
		values.put("NOTE_TEXT", NoteText);
		long note_id;
		note_id = getWritableDatabase().insert(TABLE_ABSTRACT_NOTES, null, 
				values);
		Log.d("GCA-DB", "Note inserted: id = > " + note_id);
		}

	/*
	 * Function for delete notes for some abstract from Database
	 * TABLE_ABSTRACT_NOTES
	 */
	public void deleteFromABSTRACT_NOTES( long id ) {
		long rows_affected = getWritableDatabase().delete(TABLE_ABSTRACT_NOTES,
				"NOTE_ID = ?", new String[] { String.valueOf(id) });
		Log.d("GCA-DB", "deleted Note from db  - no: " + rows_affected);
		}

	/*
	 * Function for updating the Note if user edits it
	 */
	public void updateNoteABSTRACT_NOTES( String note_id, String noteTitle, 
										  String NoteText ) {
		
		ContentValues values = new ContentValues();
		values.put("NOTE_TITLE", noteTitle);
		values.put("NOTE_TEXT", NoteText);
		long rows_affected = getWritableDatabase().update(TABLE_ABSTRACT_NOTES, values,
				"NOTE_ID = ?", new String[] { note_id });
		Log.d("GCA-DB", "Updated Note from db  - no: " + rows_affected);
		}

	/*
	 * function to check if Abstract is already favorited and exists in table
	 */
	public boolean isFavorite( String UUID ) {
		Cursor cursor = getWritableDatabase().rawQuery(SELECT_1_UUID,
				new String[]{UUID});
		boolean exists = (cursor.getCount() > 0);
		cursor.close();
		Log.d("GCA-DB", "Abstract is Fav: " + exists);
		return exists;
		}

	/*
	 * Main search function for Abstracts
	 */
	public Cursor findAbstractsWithString( String searchString ) {
		String[] searchArray = new String[FIND_ABSTRACTS_WITH_STRING.length()-
		                                  FIND_ABSTRACTS_WITH_STRING
		                                  .replace("?","").length()];
		for(int i = 0; i < searchArray.length; i++) {
		   searchArray[i] ="%"+searchString+"%";
			}
		Cursor cursor = getReadableDatabase()
				.rawQuery(FIND_ABSTRACTS_WITH_STRING, searchArray);
		return cursor;
		}

	/*
	 * Populating ABSTRACT_DETAILS Table from Arraylist
	 * ABSTRACT_DETAILS_POJOS_ARRAY that was populated while Parsing
	 */
	public void populateABSTRACT_DETAILS(
			ArrayList<AbstractDetails> abstractDetailsArray) {
		
		SQLiteStatement statement = getReadableDatabase().compileStatement(
				POPULATE_ABSTRACTS_DETAILS);		
		getReadableDatabase().beginTransaction();
		Log.d(gtag, "Starting - Insert into Abstract Details");
		for ( AbstractDetails item:abstractDetailsArray ) {
			statement.clearBindings();
			statement.bindString(1, item.getUuid());
			statement.bindString(2, item.getTopic());
			statement.bindString(3, item.getTitle());
			statement.bindString(4, item.getText());
			statement.bindString(5, item.getState());
			statement.bindLong(6, item.getSortID());
			statement.bindString(7, item.getReasonForTalk());
			statement.bindString(8, item.getMtime());
			statement.bindString(9, item.getAbstractType());
			statement.bindString(10, item.getDoi());
			statement.bindString(11, item.getCoi());
			statement.bindString(12, item.getAcknowledgements());
			long y = statement.executeInsert();
			Log.d(gtag, "INSERTED - Abstract " + item.getUuid()
					+ " in Abstract Details. ID: " + y);
			}
		statement.close();
		getReadableDatabase().setTransactionSuccessful();
		getReadableDatabase().endTransaction();
		}

	/*
	 * Populating ABSTRACT_AFFILIATION_ID_POSITION Table from Arraylist
	 * ABSTRACT_AFFILIATION_ID_POSITION_POJOS_ARRAY that was populated while
	 * Parsing
	 */
	public void populateABSTRACT_AFFILIATION_ID_POSITION(
			ArrayList<AbstractAffiliationIdPosition> 
			abstractAffiliationIdPositionArray) {
		
		SQLiteStatement statement = getReadableDatabase().compileStatement(
				POPULATE_ABSTRACT_AFFILIATION_ID_POSITION);
		getReadableDatabase().beginTransaction();
		Log.d(gtag, "Starting - Insert into ABSTRACT_AFFILIATION_ID_POSITION");
		for (AbstractAffiliationIdPosition item: 
			 abstractAffiliationIdPositionArray) {

			statement.clearBindings();
			statement.bindString(1, item.getAbstract_UUID());
			statement.bindString(2, item.getAffiliation_UUID());
			statement.bindLong(3, item.getAffiliation_position());
			long y = statement.executeInsert();
			Log.d(gtag, "INSERTED - ABSTRACT_AFFILIATION_ID_POSITION " 
					+ item.getAbstract_UUID()
					+ " in ABSTRACT_AFFILIATION_ID_POSITION. ID: " + y);
			}
		statement.close();
		getReadableDatabase().setTransactionSuccessful();
		getReadableDatabase().endTransaction();
		}

	/*
	 * Populating ABSTRACT_AUTHOR_POSITION_AFFILIATION Table from Arraylist
	 * ABSTRACT_AUTHOR_POSITION_AFFILIATION_POJOS_ARRAY that was populated while
	 * Parsing
	 */
	public void populateABSTRACT_AUTHOR_POSITION_AFFILIATION(
			ArrayList<AbsractAuthorPositionAffiliation> 
			absractAuthorPositionAffiliationArray ) {

		SQLiteStatement statement = getReadableDatabase().compileStatement(
				POPULATE_ABSTRACT_AUTHOR_POSITION_AFFILIATION);
		getReadableDatabase().beginTransaction();
		Log.d(gtag,
				"Starting - Insert into ABSTRACT_AUTHOR_POSITION_AFFILIATION");
		for (AbsractAuthorPositionAffiliation item:
			 absractAuthorPositionAffiliationArray) {

			statement.clearBindings();
			statement.bindString(1, item.getAbstract_uuid());
			statement.bindString(2, item.getAuthor_uuid());
			statement.bindLong(3, item.getAuthor_position());
			statement.bindString(4, item.getAuthor_affiliation());
			long y = statement.executeInsert();
			Log.d(gtag, "INSERTED - ABSTRACT_AUTHOR_POSITION_AFFILIATION"
					+ item.getAbstract_uuid()
					+ " in ABSTRACT_AUTHOR_POSITION_AFFILIATION. ID: " + y);
			}
		statement.close();
		getReadableDatabase().setTransactionSuccessful();
		getReadableDatabase().endTransaction();

	}

	/*
	 * Populating ABSTRACT_FIGURES Table from Arraylist
	 * ABSTRACT_FIGURES_POJOS_ARRAY that was populated while Parsing
	 */
	public void populateABSTRACT_FIGURES(
			ArrayList<AbstractFigures> abstractFiguresArray) {
		
		SQLiteStatement statement = getReadableDatabase().compileStatement(
				POPULATE_ABSTRACTS_FIGUERS);
		getReadableDatabase().beginTransaction();
		Log.d(gtag, "Starting - Insert into ABSTRACT_FIGURES");
		for (AbstractFigures item:abstractFiguresArray) {
			statement.clearBindings();
			statement.bindString(1, item.getAbstract_uuid());
			statement.bindString(2, item.getFigure_uuid());
			statement.bindString(3, item.getFigure_caption());
			statement.bindString(4, item.getFigure_URL());
			statement.bindString(5, item.getFigure_position());
			long y = statement.executeInsert();
			Log.d(gtag, "INSERTED - ABSTRACT_FIGURES" + item.getAbstract_uuid()
					+ " in ABSTRACT_FIGURES. ID: " + y);
			}
		statement.close();
		getReadableDatabase().setTransactionSuccessful();
		getReadableDatabase().endTransaction();

		}

	/*
	 * Populating ABSTRACT_REFERENCES Table from Arraylist
	 * ABSTRACT_REFERENCES_POJOS_ARRAY that was populated while Parsing
	 */
	public void populateABSTRACT_REFERENCES(
			ArrayList<AbsractReferences> absractReferencesArray) {

		SQLiteStatement statement = getReadableDatabase().compileStatement(
				POPULATE_ABSTRACT_REFERENCES);		
		getReadableDatabase().beginTransaction();
		Log.d(gtag, "Starting - Insert into ABSTRACT_REFERENCES");
		for (AbsractReferences temp:absractReferencesArray) {
			statement.clearBindings();
			statement.bindString(1, temp.getAbstract_uuid());
			statement.bindString(2, temp.getReference_uuid());
			statement.bindString(3, temp.getReference_text());
			statement.bindString(4, temp.getReference_link());
			statement.bindString(5, temp.getReference_doi());
			long y = statement.executeInsert();
			Log.d(gtag, "INSERTED - ABSTRACT_REFERENCES" + temp.getAbstract_uuid()
					+ " in ABSTRACT_REFERENCES. ID: " + y);
			}
		statement.close();
		getReadableDatabase().setTransactionSuccessful();
		getReadableDatabase().endTransaction();
		}

	/*
	 * Populating AFFILIATION_DETAILS Table from Arraylist
	 * ABSTRACT_REFERENCES_POJOS_ARRAY that was populated while Parsing
	 */
	public void populateAFFILIATION_DETAILS(
			ArrayList<AffiliationDetails> affiliationDetailsArray) {

		SQLiteStatement statement = getReadableDatabase().compileStatement(
				POPULATE_AFFILIATION_DETAILS);
		
		getReadableDatabase().beginTransaction();
		Log.d(gtag, "Starting - Insert into AFFILIATION_DETAILS");
		for (AffiliationDetails item:affiliationDetailsArray) {
			statement.clearBindings();
			statement.bindString(1, item.getAffiliation_uuid());
			statement.bindString(2, item.getAffiliation_address());
			statement.bindString(3, item.getAffiliation_country());
			statement.bindString(4, item.getAffiliation_department());
			statement.bindString(5, item.getAffiliation_section());
			long y = statement.executeInsert();
			Log.d(gtag, "INSERTED - AFFILIATION_DETAILS" + item.getAffiliation_uuid()
					+ " in AFFILIATION_DETAILS. ID: " + y);
			}	
		statement.close();
		getReadableDatabase().setTransactionSuccessful();
		getReadableDatabase().endTransaction();
		}

	/*
	 * Populating AUTHORS_DETAILS Table from Arraylist
	 * AUTHORS_DETAILS_POJOS_ARRAY that was populated while Parsing
	 */
	public void populateAUTHORS_DETAILS(
			ArrayList<AuthorsDetails> authorsDetailsArray) {

		SQLiteStatement statement = getReadableDatabase().compileStatement(
				POPULATE_AUTHORS_DETAILS);
		
		getReadableDatabase().beginTransaction();
		Log.d(gtag, "Starting - Insert into AUTHORS_DETAILS");
		for ( AuthorsDetails item:authorsDetailsArray) {

			statement.clearBindings();
			statement.bindString(1, item.getAuthor_uuid());
			statement.bindString(2, item.getAuthor_fName());
			statement.bindString(3, item.getAuthor_middleName());
			statement.bindString(4, item.getAuthor_lName());
			statement.bindString(5, item.getAuthor_email());
			long y = statement.executeInsert();
			Log.d(gtag, "INSERTED - AUTHORS_DETAILS" + item.getAuthor_uuid()
					+ " in AUTHORS_DETAILS. ID: " + y);
			}
		statement.close();
		getReadableDatabase().setTransactionSuccessful();
		getReadableDatabase().endTransaction();
		}

	public ArrayList<String> fetchAbstractsUUIDs() {	
		ArrayList<String> abstractsUUIDs = new ArrayList<String>();
		Cursor cursor = getReadableDatabase().rawQuery(SELECT_ABSTRACTS, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String temp = cursor
					.getString(cursor.getColumnIndexOrThrow("UUID"));
			abstractsUUIDs.add(temp);
			cursor.moveToNext();
			}
		return abstractsUUIDs;
		}

	public ArrayList<String> fetchAffiliationsUUIDs() {
		ArrayList<String> affiliationUUIDs = new ArrayList<String>();
		Cursor cursor = getReadableDatabase().rawQuery(SELECT_AFFILIATIONS, 
													   null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String temp = cursor.getString(cursor
					.getColumnIndexOrThrow("AFFILIATION_UUID"));
			affiliationUUIDs.add(temp);
			cursor.moveToNext();
			}
		return affiliationUUIDs;
	}

	public ArrayList<String> fetchAuthhorsUUIDs() {
		ArrayList<String> existingAuthorsUUID = new ArrayList<String>();
		Cursor cursor = getReadableDatabase().rawQuery(SELECT_AUTHORS, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String temp = cursor.getString(cursor
					.getColumnIndexOrThrow("AUTHOR_UUID"));
			existingAuthorsUUID.add(temp);
			cursor.moveToNext();
		}
		return existingAuthorsUUID;
	}
	
	public Cursor fetchNotesByUUID(String noteUUID){
		return getReadableDatabase().rawQuery(SELCCECT_NOTE, 
											  new String[]{noteUUID});
	}
	
	public Cursor fetchAbtractDetailsByUUID(String abstractUUID){
		return getReadableDatabase().rawQuery(SELECT_ABSTRACT_BY_UUID, 
											  new String[]{abstractUUID});
	}

	public Cursor fetchNextAbtractsDetails(int sortId){
		return getReadableDatabase().rawQuery(SELECT_NEXT_ABSTRACTS, 
									 new String[]{String.valueOf(sortId)});
	}

	public Cursor fetchPreviousAbtractsDetails(int sortId){
		return getReadableDatabase().rawQuery(SELECT_PREVIOUS_ABSTRACTS, 
									 new String[]{String.valueOf(sortId)});
	}
	
	public Cursor fetchAuthorsByAbsId(String abstractUUID){
		return getReadableDatabase().rawQuery(SELECT_AUTHORS_BY_ABSID, 
									 new String[]{abstractUUID, abstractUUID});
	}
	
	public Cursor fetchAffiliationsByAbsId(String abstractUUID){
		return getReadableDatabase().rawQuery(SELECT_AFFILIATION_BY_ABSID, 
									 new String[]{abstractUUID});
	}
	
	public Cursor fetchReferencesByAbsId(String abstractUUID){
		return getReadableDatabase().rawQuery(SELECT_REFERENCES_BY_ABSID, 
									 new String[]{abstractUUID});
	}
	
	public Cursor fetchFiguresByAbsId(String abstractUUID){
		return getReadableDatabase().rawQuery(SELECT_FIGURES_BY_ABSID, 
									 new String[]{abstractUUID});
	}

	public Cursor fetchNotesByAbsId(String abstractUUID){
		return getReadableDatabase().rawQuery(SELECT_NOTES_BY_ABSID, 
									 new String[]{abstractUUID});
	}
	
	public Cursor fetchFavoriteAbs(){
		return getReadableDatabase().rawQuery(SELECT_FAV_ABSTRACT, null);
	}

	public void deleteAbstract(String abstractUUID){
		SQLiteDatabase db = getWritableDatabase();
		db.delete("ABSTRACT_DETAILS", "UUID = ?", 
				new String[] { abstractUUID });		
		db.delete("ABSTRACT_FIGURES", "ABSTRACT_UUID = ?", 
				new String[] { abstractUUID });
		db.delete("ABSTRACT_REFERENCES", "ABSTRACT_UUID = ?", 
				new String[] { abstractUUID });
		db.delete("ABSTRACT_AFFILIATION_ID_POSITION", "ABSTRACT_UUID = ?", 
				new String[] { abstractUUID });		
		db.delete("ABSTRACT_AUTHOR_POSITION_AFFILIATION", "ABSTRACT_UUID = ?", 
				new String[] { abstractUUID });
	}
	
	public void deleteAuthor(String authorUUID){
		getWritableDatabase().delete("AUTHORS_DETAILS", "AUTHOR_UUID = ?", 
		new String[] { authorUUID });
	}
	
	public void deleteAffiliation(String affiliationUUID){
		getWritableDatabase().delete("AFFILIATION_DETAILS", 
				"AFFILIATION_UUID = ?",	new String[] { affiliationUUID });
		}

	/*
	 * just a helper function to empty database, if required
	 */
	public void emptyDb() {
		getReadableDatabase().execSQL("DELETE FROM " + TABLE_ABSTRACT_DETAILS);
	}

	public int getCountOfRowsInAbstractDetailsTable() {
		String sql = "SELECT * FROM ABSTRACT_DETAILS";
		Cursor cursor = getReadableDatabase().rawQuery(sql, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}
}

/**
 * Class to hold the SQL Strings for the app
 * They are used in the DatabaseHelper and statically imported  *
 */
final class SqlStrings {
	private SqlStrings() {
		// this prevents even the native class from
		// calling this ctor as well :
		throw new AssertionError();
	}

	/*
	 * Tables Name
	 */
	public static final String TABLE_ABSTRACT_DETAILS = "ABSTRACT_DETAILS";

	public static final String TABLE_AUTHORS_DETAILS = "AUTHORS_DETAILS";

	public static final String TABLE_ABSTRACT_AUTHOR_POSITION_AFFILIATION = 
			"ABSTRACT_AUTHOR_POSITION_AFFILIATION";

	public static final String TABLE_AFFILIATION_DETAILS = "AFFILIATION_DETAILS";

	public static final String TABLE_ABSTRACT_AFFILIATION_ID_POSITION = 
			"ABSTRACT_AFFILIATION_ID_POSITION";

	public static final String TABLE_ABSTRACT_REFERENCES = "ABSTRACT_REFERENCES";

	public static final String TABLE_ABSTRACT_FIGURES = "ABSTRACT_FIGURES";

	public static final String TABLE_ABSTRACT_FAVORITES = "ABSTRACT_FAVORITES";

	public static final String TABLE_ABSTRACT_NOTES = "ABSTRACT_NOTES";

	/*
	 * Query for Creating Tables
	 */
	public static final String CREATE_ABSTRACT_DETAILS = 
			"CREATE TABLE IF NOT EXISTS ABSTRACT_DETAILS"
			+ "(UUID VARCHAR PRIMARY KEY, TOPIC TEXT NOT NULL, "
			+ "TITLE TEXT NOT NULL, ABSRACT_TEXT TEXT NOT NULL,"
			+ "STATE TEXT NOT NULL, SORTID INTEGER NOT NULL, REASONFORTALK TEXT,"
			+ "MTIME TEXT NOT NULL, TYPE TEXT NOT NULL, DOI TEXT, COI TEXT,"
			+ "ACKNOWLEDGEMENTS TEXT );";

	public static final String CREATE_AUTHORS_DETAILS = 
			"CREATE TABLE IF NOT EXISTS AUTHORS_DETAILS"
			+ "( AUTHOR_UUID VARCHAR PRIMARY KEY, AUTHOR_FIRST_NAME TEXT NOT NULL, AUTHOR_MIDDLE_NAME TEXT, "
			+ "AUTHOR_LAST_NAME TEXT NOT NULL, AUTHOR_EMAIL TEXT);";

	public static final String CREATE_ABSTRACT_AUTHOR_POSITION_AFFILIATION = 
			"CREATE TABLE IF NOT EXISTS ABSTRACT_AUTHOR_POSITION_AFFILIATION"
			+ "( ABSTRACT_UUID VARCHAR NOT NULL, AUTHOR_UUID VARCHAR NOT NULL, "
			+ "AUTHOR_POSITION INTEGER NOT NULL, AUTHOR_AFFILIATION VARCHAR NOT NULL);";

	public static final String CREATE_AFFILIATION_DETAILS = 
			"CREATE TABLE IF NOT EXISTS "
			+ TABLE_AFFILIATION_DETAILS
			+ "(AFFILIATION_UUID VARCHAR PRIMARY KEY, AFFILIATION_ADDRESS TEXT NOT NULL, AFFILIATION_COUNTRY TEXT NOT NULL, "
			+ "AFFILIATION_DEPARTMENT TEXT NOT NULL, AFFILIATION_SECTION TEXT);";

	public static final String CREATE_ABSTRACT_AFFILIATION_ID_POSITION = 
			"CREATE TABLE IF NOT EXISTS "
			+ TABLE_ABSTRACT_AFFILIATION_ID_POSITION
			+ "( ABSTRACT_UUID VARCHAR NOT NULL, AFFILIATION_UUID VARCHAR NOT NULL, "
			+ "AFFILIATION_POSITION INTEGER NOT NULL);";

	public static final String CREATE_ABSTRACT_REFERENCES = 
			"CREATE TABLE IF NOT EXISTS "
			+ TABLE_ABSTRACT_REFERENCES
			+ "( ABSTRACT_UUID VARCHAR NOT NULL, REF_UUID VARCHAR NOT NULL, "
			+ "REF_TEXT TEXT, REF_LINK TEXT, REF_DOI TEXT);";

	public static final String CREATE_ABSTRACT_FAVORITES = 
			"CREATE TABLE IF NOT EXISTS "
			+ TABLE_ABSTRACT_FAVORITES + "( ABSTRACT_UUID VARCHAR NOT NULL); ";

	public static final String CREATE_ABSTRACT_NOTES = 
			"CREATE TABLE IF NOT EXISTS "
			+ TABLE_ABSTRACT_NOTES
			+ "(NOTE_ID INTEGER PRIMARY KEY,  ABSTRACT_UUID VARCHAR NOT NULL, NOTE_TITLE TEXT, NOTE_TEXT TEXT); ";

	public static final String CREATE_ABSTRACT_FIGURES = 
			"CREATE TABLE IF NOT EXISTS "
			+ TABLE_ABSTRACT_FIGURES
			+ "( ABSTRACT_UUID VARCHAR NOT NULL, FIG_UUID VARCHAR NOT NULL, "
			+ "FIG_CAPTION TEXT, FIG_URL TEXT, FIG_POSITION TEXT);";
	/*
	 * Standard queries
	 */
	public static final String FIND_ABSTRACTS_WITH_STRING = 
			"SELECT UUID AS _id , TOPIC, TITLE, ABSRACT_TEXT, STATE, SORTID, "
			+ "REASONFORTALK, MTIME, TYPE,DOI, COI, ACKNOWLEDGEMENTS "
			+ "FROM ABSTRACT_DETAILS WHERE ABSRACT_TEXT like ?"
			+ " OR TITLE like ?"
			+ " OR "
			+ " _id in (SELECT ABSTRACT_UUID FROM ABSTRACT_AFFILIATION_ID_POSITION "
			+ "WHERE AFFILIATION_UUID IN (SELECT AFFILIATION_UUID FROM AFFILIATION_DETAILS "
			+ "WHERE AFFILIATION_SECTION LIKE ?"
			+ " OR "
			+ "AFFILIATION_DEPARTMENT LIKE ?"
			+ " OR "
			+ "AFFILIATION_ADDRESS LIKE ?"
			+ " OR "
			+ "AFFILIATION_COUNTRY LIKE ?"
			+ ")) OR "
			+ " _id in (SELECT ABSTRACT_UUID FROM ABSTRACT_AUTHOR_POSITION_AFFILIATION "
			+ "WHERE AUTHOR_UUID IN (SELECT AUTHOR_UUID FROM AUTHORS_DETAILS "
			+ "WHERE AUTHOR_FIRST_NAME || ' ' || AUTHOR_LAST_NAME LIKE ?"
			+ " OR AUTHOR_LAST_NAME LIKE ?"
			+ " OR AUTHOR_FIRST_NAME LIKE ?"
			+")) ;";
	
	public static final String POPULATE_ABSTRACTS_FIGUERS = 
			"INSERT INTO "
			+ TABLE_ABSTRACT_FIGURES
			+ " (ABSTRACT_UUID, FIG_UUID, FIG_CAPTION, FIG_URL, FIG_POSITION) VALUES(?,?,?,?,?);";
	
	public static final String POPULATE_ABSTRACTS_DETAILS =
			"INSERT INTO "
			+ TABLE_ABSTRACT_DETAILS
			+ " (UUID, TOPIC, TITLE, ABSRACT_TEXT, STATE, SORTID, REASONFORTALK, MTIME, TYPE, DOI, COI, ACKNOWLEDGEMENTS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";
	
	public static final String POPULATE_ABSTRACT_AFFILIATION_ID_POSITION = 
			"INSERT INTO "
			+ TABLE_ABSTRACT_AFFILIATION_ID_POSITION
			+ " (ABSTRACT_UUID, AFFILIATION_UUID, AFFILIATION_POSITION) VALUES(?,?,?);";
	
	public static final String POPULATE_ABSTRACT_AUTHOR_POSITION_AFFILIATION = 
			"INSERT INTO "
			+ TABLE_ABSTRACT_AUTHOR_POSITION_AFFILIATION
			+ " (ABSTRACT_UUID, AUTHOR_UUID, AUTHOR_POSITION, AUTHOR_AFFILIATION) VALUES(?,?,?,?);";
	
	public static final String POPULATE_ABSTRACT_REFERENCES = 
			"INSERT INTO "
			+ TABLE_ABSTRACT_REFERENCES
			+ " (ABSTRACT_UUID, REF_UUID, REF_TEXT, REF_LINK, REF_DOI) VALUES(?,?,?,?,?);";
	
	public static final String POPULATE_AFFILIATION_DETAILS = 
			"INSERT INTO "
			+ TABLE_AFFILIATION_DETAILS
			+ " (AFFILIATION_UUID, AFFILIATION_ADDRESS, AFFILIATION_COUNTRY, AFFILIATION_DEPARTMENT, AFFILIATION_SECTION) VALUES(?,?,?,?,?);";

	public static final String POPULATE_AUTHORS_DETAILS = 
			"INSERT INTO "
			+ TABLE_AUTHORS_DETAILS
			+ " (AUTHOR_UUID, AUTHOR_FIRST_NAME, AUTHOR_MIDDLE_NAME, AUTHOR_LAST_NAME, AUTHOR_EMAIL) VALUES(?,?,?,?,?);";
	
	public static final String SELECT_ABSTRACTS = "SELECT * FROM ABSTRACT_DETAILS;";
	
	public static final String SELECT_AFFILIATIONS = "SELECT * FROM AFFILIATION_DETAILS;";
	
	public static final String SELECT_AUTHORS = "SELECT * FROM AUTHORS_DETAILS;";
	
	public static final String SELECT_1_UUID = "SELECT 1 FROM "+TABLE_ABSTRACT_FAVORITES+" WHERE ABSTRACT_UUID LIKE ?";
	
	public static final String SELCCECT_NOTE = "SELECT NOTE_ID, ABSTRACT_UUID, NOTE_TITLE, NOTE_TEXT FROM ABSTRACT_NOTES WHERE NOTE_ID = '?';";
	
	public static final String SELECT_ABSTRACT_BY_UUID = "SELECT * FROM ABSTRACT_DETAILS where UUID = ?;";
	
	public static final String SELECT_NEXT_ABSTRACTS = "SELECT * from abstract_details WHERE SORTID>? ORDER BY SORTID";
	
	public static final String SELECT_PREVIOUS_ABSTRACTS = "SELECT * from abstract_details WHERE SORTID<? ORDER BY SORTID";
	
	public static final String SELECT_AUTHORS_BY_ABSID = 
			"SELECT DISTINCT AUTHORS_DETAILS.AUTHOR_FIRST_NAME, " +
			"AUTHOR_MIDDLE_NAME, AUTHOR_LAST_NAME, AUTHOR_EMAIL, " +
			"ABSTRACT_AUTHOR_POSITION_AFFILIATION.AUTHOR_AFFILIATION, " +
			"ABSTRACT_AUTHOR_POSITION_AFFILIATION.AUTHOR_POSITION " +
			"FROM AUTHORS_DETAILS JOIN ABSTRACT_AUTHOR_POSITION_AFFILIATION USING (AUTHOR_UUID) " +
			"WHERE AUTHORS_DETAILS.AUTHOR_UUID IN " +
			"(SELECT AUTHOR_UUID FROM ABSTRACT_AUTHOR_POSITION_AFFILIATION WHERE ABSTRACT_UUID = ?) " +
			"AND ABSTRACT_AUTHOR_POSITION_AFFILIATION.AUTHOR_POSITION IN " +
			"(SELECT AUTHOR_POSITION FROM ABSTRACT_AUTHOR_POSITION_AFFILIATION WHERE ABSTRACT_UUID = ?) " +
			"ORDER BY AUTHOR_POSITION ASC;"; 
	
	public static final String SELECT_AFFILIATION_BY_ABSID =
			"SELECT AFFILIATION_ADDRESS, AFFILIATION_COUNTRY, " +
			"AFFILIATION_DEPARTMENT, AFFILIATION_SECTION, AFFILIATION_POSITION " +
			"FROM AFFILIATION_DETAILS JOIN ABSTRACT_AFFILIATION_ID_POSITION USING (AFFILIATION_UUID) " +
			"WHERE AFFILIATION_UUID IN " +
			"(SELECT AFFILIATION_UUID FROM ABSTRACT_AFFILIATION_ID_POSITION " +
			"WHERE ABSTRACT_UUID = ?)  " +
			"ORDER BY AFFILIATION_POSITION ASC;";
	
	public static final String SELECT_REFERENCES_BY_ABSID = 
			"SELECT * FROM ABSTRACT_REFERENCES WHERE ABSTRACT_UUID = ?;"; 
	
	public static final String SELECT_FIGURES_BY_ABSID =  
			"SELECT * FROM ABSTRACT_FIGURES WHERE ABSTRACT_UUID = ?;";
	
	public static final String SELECT_NOTES_BY_ABSID = 
			"SELECT * FROM ABSTRACT_NOTES WHERE ABSTRACT_UUID = ?;";
	
	public static final String SELECT_FAV_ABSTRACT =  
			"SELECT * FROM ABSTRACT_DETAILS WHERE UUID IN " +
			"(SELECT ABSTRACT_UUID FROM ABSTRACT_FAVORITES);";
	}
