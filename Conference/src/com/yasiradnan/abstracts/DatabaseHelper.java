/**
 * 
 */
package com.yasiradnan.abstracts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Adnan
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static String Database_Name = "TestData";
    
    private static int Database_Version = 1;
    
    SQLiteDatabase database;
    
    /*
     * Tables Name
     */
    
    public static final String TABLE_ABS_AFFILIATION_NAME = "ABS_AFFILIATION_NAME";
    
    public static final String TABLE_ABSTRACT_AFFILIATE_NAME = "ABSTRACT_AFFILIATE_NAME";
    
    public static final String TABLENAME_ABSTRACT_AFFILIATION = "ABSTRACT_AFFILIATION";
    
    
    public static final String TABLENAME_ABSTRACT_AUTHOR = "ABSTRACT_AUTHOR";
    
    public static final String TABLENAME_ABSTRACT_KEY_WORDS = "ABSTRACT_KEY_WORDS";
    
    public static final String TABLENAME_ABSTRACTS_ITEM = "ABSTRACTS_ITEM";
    
    
    public static final String TABLE_AUTHORS_ABSTRACT = "AUTHORS_ABSTRACT";
    
    public static final String TABLE_AUTHORS_AFFILIATE = "AUTHORS_AFFILIATE";
    
    /*
     * Query for Creating Tables
     */
    
    private String CREATE_ABS_AFFILIATION_NAME = "CREATE TABLE IF NOT EXISTS ABS_AFFILIATION_NAME "
            + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,AF_NAME TEXT);";
    
    
    private String CREATE_ABSTRACT_AFFILIATE_NAME = "CREATE TABLE IF NOT EXISTS ABSTRACT_AFFILIATE_NAME"
            + "(ABSTRACTSITEM_ID INTEGER NOT NULL,ABSAFFILIATIONNAME_ID INTEGER NOT NULL);";
    
    
    private String CREATE_ABSTRACT_AFFILIATION = "CREATE TABLE IF NOT EXISTS ABSTRACT_AFFILIATION ("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT,AFFILIATION_NUMBER TEXT);";
    
    
    
    private String CREATE_ABSTRACT_AUTHOR = "CREATE TABLE IF NOT EXISTS  ABSTRACT_AUTHOR "
            + "(_id INTEGER PRIMARY KEY AUTOINCREMENT ,NAME TEXT NOT NULL,IS__CORRESPONDING TEXT);";
    
    
    
    private String CREATE_ABSTRACT_KEY_WORDS = "CREATE TABLE IF NOT EXISTS ABSTRACT_KEY_WORDS"
            + "( KEYWORDS TEXT,_id INTEGER PRIMARY KEY AUTOINCREMENT );";
    
    
    
    
    private String CREATE_ABSTRACTS_ITEM = "CREATE TABLE IF NOT EXISTS ABSTRACTS_ITEM"
            + "(_id INTEGER PRIMARY KEY AUTOINCREMENT , CORRESPONDENCE TEXT NOT NULL,"
            + "TITLE TEXT NOT NULL ,URL TEXT NOT NULL,"
            + "TEXT TEXT NOT NULL,TYPE TEXT NOT NULL, TOPIC TEXT NOT NULL," 
            +"COI TEXT NOT NULL,CITE TEXT NOT NULL,REFS TEXT NOT NULL);";
    
    
    
    private String CREATE_AUTHORS_ABSTRACT = "CREATE TABLE IF NOT EXISTS AUTHORS_ABSTRACT"
            + "( ABSTRACTSITEM_ID INTEGER NOT NULL,ABSTRACTAUTHOR_ID INTEGER NOT NULL);";
    
    
    
    
    private String CREATE_AUTHORS_AFFILIATE = "CREATE TABLE IF NOT EXISTS AUTHORS_AFFILIATE"
            + "(ABSTRACTAUTHOR_ID INTEGER NOT NULL,ABSTRACTAFFILIATION_ID INTEGER NOT NULL );";
    
    /*
     * End
     */
    
    public DatabaseHelper(Context context) {
        super(context, Database_Name, null, Database_Version);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase arg0) {
        // TODO Auto-generated method stub
        
        /*
         * Creating Tables
         */
        
        database.execSQL(CREATE_ABS_AFFILIATION_NAME);
        
        database.execSQL(CREATE_ABSTRACT_AFFILIATE_NAME);
        
        database.execSQL(CREATE_ABSTRACT_AFFILIATION);
        
        database.execSQL(CREATE_ABSTRACT_AUTHOR);
        
        database.execSQL(CREATE_ABSTRACT_KEY_WORDS);
        
        database.execSQL(CREATE_ABSTRACTS_ITEM);
        
        database.execSQL(CREATE_AUTHORS_ABSTRACT);
        
        database.execSQL(CREATE_AUTHORS_AFFILIATE);
    
    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

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

}
