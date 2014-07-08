/**
* Date: Jun 16, 2014
*/

package com.g_node.gca.abstracts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private String gtag = "GCA-DB";
	
	private static String Database_Name = "gca.db";

    private static int Database_Version = 5;

    public static SQLiteDatabase database;
    
    public long items_id;
    public long author_id;
    public long abs_auth_pos_id;
    
    /*
     * Tables Name
     */
    
    public static final String TABLE_ABSTRACT_DETAILS = "ABSTRACT_DETAILS";
    
    public static final String TABLE_AUTHORS_DETAILS = "AUTHORS_DETAILS";
    
    public static final String TABLE_ABSTRACT_AUTHOR_POSITION_AFFILIATION = "ABSTRACT_AUTHOR_POSITION_AFFILIATION";
    
    public static final String TABLE_AFFILIATION_DETAILS = "AFFILIATION_DETAILS";
    
    public static final String TABLE_ABSTRACT_AFFILIATION_ID_POSITION = "ABSTRACT_AFFILIATION_ID_POSITION";
    
    public static final String TABLE_ABSTRACT_REFERENCES = "ABSTRACT_REFERENCES";
    
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
    		+ "AUTHOR_LAST_NAME TEXT NOT NULL, AUTHOR_EMAIL TEXT NOT NULL);";
    
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
    
    
    public DatabaseHelper(Context context) {
        super(context, Database_Name, null, Database_Version);
        // TODO Auto-generated constructor stub
    }

	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		
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
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_DETAILS);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHORS_DETAILS);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_AUTHOR_POSITION_AFFILIATION);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_AFFILIATION_DETAILS);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_AFFILIATION_ID_POSITION);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_REFERENCES);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_FAVORITES);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSTRACT_NOTES);
		onCreate(database);
		
	}
	
	public void open() {
        /*
         * Opening Database
         */

        database = this.getWritableDatabase();
    }
	
	public void close() {
        /*
         * Closing Database
         */
		
        database.close();
    }
	
	//function for adding data in ABSTRACT_DETAILS table
	public void addItems(String uuid, String topic, String Title, String text, String STATE, int SortID,
            String reasonsForTalk, String mtime, String type, String DOI, String COI, String acknowledgements) {
        ContentValues cd = new ContentValues();

        cd.put("UUID", uuid);

        cd.put("TOPIC", topic);

        cd.put("TITLE", Title);

        cd.put("ABSRACT_TEXT", text);

        cd.put("STATE", STATE);

        cd.put("SORTID", SortID);

        cd.put("REASONFORTALK", reasonsForTalk);

        cd.put("MTIME", mtime);

        cd.put("TYPE", type);

        cd.put("DOI", DOI);

        cd.put("COI", COI);
        
        cd.put("ACKNOWLEDGEMENTS", acknowledgements);

        items_id = database.insert(TABLE_ABSTRACT_DETAILS, null, cd);
        Log.i(gtag, "Abstract item insert id return: " + Long.toString(items_id));

    }	//end function addItems
	
	//function for adding data in AUTHORS_DETAILS table
	public void addAuthors(String uuid, String first_Name, String middle_Name, String last_Name, String author_mail) {
		
		ContentValues value = new ContentValues();

        value.put("AUTHOR_UUID", uuid);
        
        value.put("AUTHOR_FIRST_NAME", first_Name);
        
        value.put("AUTHOR_MIDDLE_NAME", middle_Name);
        
        value.put("AUTHOR_LAST_NAME", last_Name);
        
        value.put("AUTHOR_EMAIL", author_mail);
	
        author_id = database.insert(TABLE_AUTHORS_DETAILS, null, value);
        Log.i(gtag, "Author inserted - id: " + author_id);
	
	}	//end addAuthors function
	
	//function for adding data in ABSTRACT_AUTHOR_POSITION_AFFILIATION table
	public void addInABSTRACT_AUTHOR_POSITION_AFFILIATION(String abstractUUID, String authorUUID, int authorPosition, String authorAffiliation) {
		ContentValues values = new ContentValues();
		
		values.put("ABSTRACT_UUID", abstractUUID);
		
		values.put("AUTHOR_UUID", authorUUID);
		
		values.put("AUTHOR_POSITION", authorPosition);
		
		values.put("AUTHOR_AFFILIATION", authorAffiliation);
		
		abs_auth_pos_id = database.insert(TABLE_ABSTRACT_AUTHOR_POSITION_AFFILIATION, null, values);
		Log.i(gtag, "Abstract UUID, Auth uuid, position, affiliation inserted: " + abs_auth_pos_id);
		
	}	//end addInABSTRACT_AUTHOR_POSITION_AFFILIATION function
	
	//function for adding new affiliation in AFFILIATION_DETAILS Table
	public void addInAFFILIATION_DETAILS(String uuid, String aff_address, String aff_country, String aff_dept, String aff_section) {
		
		ContentValues values = new ContentValues();
		
		values.put("AFFILIATION_UUID", uuid);
		
		values.put("AFFILIATION_ADDRESS", aff_address);
		
		values.put("AFFILIATION_COUNTRY", aff_country);
		
		values.put("AFFILIATION_DEPARTMENT", aff_dept);
		
		values.put("AFFILIATION_SECTION", aff_section);
		
		long affiliation_id;
		affiliation_id = database.insert(TABLE_AFFILIATION_DETAILS, null, values);
		Log.i(gtag, "Affiliation inserted into directory: " + affiliation_id);
	}
	
	//function to add in ABSTRACT_AFFILIATION_ID_POSITION Table - maintaining affiliation position against each abstract in a separate table
	public void addInABSTRACT_AFFILIATION_ID_POSITION (String abs_uuid, String aff_uuid, int aff_position) {
		
		ContentValues values = new ContentValues();
		
		values.put("ABSTRACT_UUID", abs_uuid);
		
		values.put("AFFILIATION_UUID", aff_uuid);
		
		values.put("AFFILIATION_POSITION", aff_position);
		
		long abs_aff_id_pos;
		abs_aff_id_pos = database.insert(TABLE_ABSTRACT_AFFILIATION_ID_POSITION, null, values);
		Log.i(gtag, "abs uuid, aff uuid, aff pos inserted: id = > " + abs_aff_id_pos);
	}
	
	//function for adding in ABSTRACT_REFERENCES table 
	public void addInABSTRACT_REFERENCES (String ABSTRACT_UUID, String REF_UUID, String REF_TEXT, String REF_LINK, String REF_DOI) {
		
		ContentValues values = new ContentValues();
		
		values.put("ABSTRACT_UUID", ABSTRACT_UUID);
		
		values.put("REF_UUID", REF_UUID);
		
		values.put("REF_TEXT", REF_TEXT);
		
		values.put("REF_LINK", REF_LINK);
		
		values.put("REF_DOI", REF_DOI);
		
		long ref_id;
		ref_id = database.insert(TABLE_ABSTRACT_REFERENCES, null, values);
		Log.i(gtag, "reference inserted: id = > " + ref_id);
		
	}
	
	//function for adding to ABSTRACT_FAVORITES Table when a user favourites some abstract
	public static void addInABSTRACT_FAVORITES (String abstract_uuid) {
			
		ContentValues values = new ContentValues();
		
		values.put("ABSTRACT_UUID", abstract_uuid);
		
		long abs_fav_id = database.insert(TABLE_ABSTRACT_FAVORITES, null, values);
		Log.i("GCA-DB", "abstract favourited - id: " + abs_fav_id);
	}
	
	//function for deleting from ABSTRACT_FAVORITES if user un-favorites some abstract
	public static void deleteFromABSTRACT_FAVORITES (String abstract_uuid) {
		
		long rows_affected = database.delete(TABLE_ABSTRACT_FAVORITES, "ABSTRACT_UUID = ?", new String[] { abstract_uuid });
		Log.i("GCA-DB", "deleted abstract from fav - no: " + rows_affected);
	}
	
	//Function for adding notes for some abstract into Database TABLE_ABSTRACT_NOTES
	public static void addInABSTRACT_NOTES(String abstractUUID, String noteTitle, String NoteText) {
		
		ContentValues values = new ContentValues();
		
		values.put("ABSTRACT_UUID", abstractUUID);
		
		values.put("NOTE_TITLE", noteTitle);
		
		values.put("NOTE_TEXT", NoteText);
		
		long note_id;
		note_id = database.insert(TABLE_ABSTRACT_NOTES, null, values);
		Log.i("GCA-DB", "Note inserted: id = > " + note_id);
	}
	
	//Function for delete notes for some abstract from Database TABLE_ABSTRACT_NOTES
	public static void deleteFromABSTRACT_NOTES(long id) {
		long rows_affected = database.delete(TABLE_ABSTRACT_NOTES, "NOTE_ID = ?", new String[] { String.valueOf(id)});
		Log.i("GCA-DB", "deleted Note from db  - no: " + rows_affected);
	}
	
	//Function for updating the Note if user edits it
	public static void updateNoteABSTRACT_NOTES(String note_id, String noteTitle, String NoteText) {
		
		ContentValues values = new ContentValues();
		
		values.put("NOTE_TITLE", noteTitle);
		
		values.put("NOTE_TEXT", NoteText);
		
		long rows_affected = database.update(TABLE_ABSTRACT_NOTES, values, "NOTE_ID = ?", new String[] { note_id} );
		Log.i("GCA-DB", "Updated Note from db  - no: " + rows_affected);
	}
	
	//function to check if author already exists in directory
	public boolean AuthorExists(String UUID) {
        Cursor cursor = database.rawQuery("select 1 from " + TABLE_AUTHORS_DETAILS + " where AUTHOR_UUID like '%" + UUID
                + "%'", null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
	
	//function to check if affiliation already exists in directory
	public boolean AffiliationExists(String UUID) {
        Cursor cursor = database.rawQuery("select 1 from " + TABLE_AFFILIATION_DETAILS + " where AFFILIATION_UUID like '%" + UUID
                + "%'", null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
	
	//function to check if Abstract is already favorited and exists in table
	public static boolean abstractIsFavorite(String UUID) {
        Cursor cursor = database.rawQuery("select 1 from " + TABLE_ABSTRACT_FAVORITES + " where ABSTRACT_UUID like '%" + UUID
                + "%'", null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        Log.i("GCA-DB", "Abstract UUID: " + UUID);
        Log.i("GCA-DB", "Abstract is Fav: " + exists);
        return exists;
    }
	
	public Cursor fetchDataByName(String string) {
        // TODO Auto-generated method stub

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
	
	

}
