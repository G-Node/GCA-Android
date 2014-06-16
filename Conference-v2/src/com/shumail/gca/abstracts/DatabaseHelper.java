/**
* Date: Jun 16, 2014
*/

package com.shumail.gca.abstracts;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static String Database_Name = "gca.db";

    private static int Database_Version = 1;

    public static SQLiteDatabase database;
    
    public long items_id;
    
    /*
     * Tables Name
     */
    
    public static final String TABLE_ABSTRACT_DETAILS = "ABSTRACT_DETAILS";
    
    
    /*
     * Query for Creating Tables
     */
    
    public static final String CREATE_ABSTRACT_DETAILS = "CREATE TABLE IF NOT EXISTS ABSTRACT_DETAILS"
            + "(UUID VARCHAR PRIMARY KEY, TOPIC TEXT NOT NULL, "
            + "TITLE TEXT NOT NULL, ABSRACT_TEXT TEXT NOT NULL,"
            + "STATE TEXT NOT NULL, SORTID INTEGER NOT NULL, REASONFORTALK TEXT," 
            + "MTIME TEXT NOT NULL, TYPE TEXT NOT NULL, DOI TEXT, COI TEXT,"
            + "ACKNOWLEDGEMENTS TEXT );";

    
    
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
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		database.execSQL("DROP TABLE IF EXISTS" + TABLE_ABSTRACT_DETAILS);
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
        Log.i("GNODE", Long.toString(items_id));

    }

}
