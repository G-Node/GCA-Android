/**
 * Copyright (c) 2013, Yasir Adnan <adnan.ayon@gmail.com>
 * License: BSD-3 (See LICENSE)
 */

package com.shumail.gca.abstracts;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.shumail.newsroom.R;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
//import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AbstractContent extends Activity {
	
	String gtag = "GCA-Abs-Con";
	
	boolean isFav;
	
	MenuItem starG;

    TextView content;

    TextView title;

    TextView topic;

    TextView afName;

    TextView emailField;

    TextView authorNames;

    TextView ConRefs;

    TextView ConAck;

    Button btn;

    private String affiliation_ID;

    private String getName;

    private String value;

    private String sqlQueryOne;

    private String sqlQueryTwo;

    private String sqlQueryThree;

    private String affiliationName;

    Cursor cursor, cursorOne, cursorTwo, referenceCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abstracts_show);
        /*
         * Initializing fields
         */
        initial_UI();
        /*
         * Getting data from Intent
         */
        Bundle getData = getIntent().getExtras();
        String abstracts = getData.getString("abstracts");
        String Title = getData.getString("Title");
        String Topic = getData.getString("Topic");
        value = getData.getString("value");
        affiliationName = getData.getString("afName");
        String email = getData.getString("email");
        String refs; 	//= getData.getString("refs");
        String acknowledgments = getData.getString("acknowledgements");
       
//        /*
//         * Executing SQL Queries to get data
//         */
//        sqlQueries();
        /*
         * Show Author names for that Abstract
         */
        authorName();
        
        

        
        
        
        //Test set affiliations name
        //afName.setText("AFF name 1 \nAFF Name 2");
        
//        /*
//         * Get Affiliation Name for associate abstracts
//         */
        affiliationName();
        title.setText(Title);
        /*
         * Set Title to BOLD
         */
        title.setTypeface(null, Typeface.BOLD);
        topic.setText(Topic);
        /*
         * Getting email address from CORRESPONDENCE(ABSTRACTS_ITEM table)
         */
        int index = email.lastIndexOf(",");
        String emailText = email.substring(index + 1, email.length());
        emailField.append(Html.fromHtml("*<a href= mailto:" + emailText + ">" + emailText
                + "</a><br/>"));
        content.setText(abstracts);
        /*
         * If acknowledgments contain any data
         */
        if (acknowledgments.length() > 0) {

            ConAck.append(Html.fromHtml("<b>Acknowledgments</b><br/>"));
            ConAck.append(acknowledgments + "\n" );
        }
        
        getRefs();
        
        
//        /*
//         * If refs contain any data
//         */
//        if (refs.length() > 0) {
//
//            ConRefs.append(Html.fromHtml("<b>Reference</b><br/>"));
//            ConRefs.append("\n" + refs);
//        }

    }

    private void initial_UI() {
        /*
         * TextView for Abstract Text
         */
        content = (TextView)findViewById(R.id.Content);
        /*
         * TextView for Abstract Title
         */
        title = (TextView)findViewById(R.id.ConTitle);
        /*
         * TextView for Abstract Topic
         */
        topic = (TextView)findViewById(R.id.ConTopic);
        /*
         * TextView for Abstract Author
         */
        authorNames = (TextView)findViewById(R.id.ConAuthor);
        /*
         * TextView for Affiliation Name
         */
        afName = (TextView)findViewById(R.id.ConAfName);
        /*
         * TextView for email
         */
        emailField = (TextView)findViewById(R.id.email);
        /*
         * TextView for Reference
         */
        ConRefs = (TextView)findViewById(R.id.Conrefs);
        /*
         * TextView for Acknowledgments
         */
        ConAck = (TextView)findViewById(R.id.ConACK);
    }

    private void sqlQueries() {

//        sqlQueryOne = "select authors_abstract.abstractauthor_id AS AUTH_ID,abstract_author.NAME AS NAME,abstract_author.IS__CORRESPONDING,ABSTRACT_AFFILIATION.AFFILIATION_NUMBER AS NUMBER "
//                + "from abstracts_item,abstract_author,authors_abstract,ABSTRACT_AFFILIATION "
//                + "where abstracts_item._id = authors_abstract.abstractsitem_id "
//                + "and abstract_author._id = authors_abstract.abstractauthor_id "
//                + "and ABSTRACT_AFFILIATION._id = authors_abstract.ABSTRACTAFFILIATION_ID "
//                + "and abstracts_item._id = " + value;
//
//        sqlQueryTwo = "select CORRESPONDING_AUTHOR_ID AS ID from ABSTRACT_AUTHOR_CORRESPONDENCE where abstractsItem_id = "
//                + value;
        
        String nextAbstractData = "SELECT UUID AS _id , TOPIC, TITLE, " +
        		"ABSRACT_TEXT, STATE, SORTID, REASONFORTALK, MTIME, TYPE,DOI, COI, ACKNOWLEDGEMENTS " +
        		"FROM ABSTRACT_DETAILS WHERE _id = '" + value + "';";
        
//        sqlQueryThree = "select abstracts_item._id AS ID,CORRESPONDENCE,title, type, topic, text,af_name as af,REFS,ACKNOWLEDGEMENTS "
//                + "from abs_affiliation_name,abstract_affiliation,abstracts_item,abstract_author,authors_abstract "
//                + "where ID = "
//                + value
//                + " and abstracts_item._id = authors_abstract.abstractsitem_id "
//                + "and abstract_author._id = authors_abstract.abstractauthor_id "
//                + "and abstract_affiliation._id = abstract_author._id "
//                + "and abs_affiliation_name._id = abstracts_item._id GROUP By abstracts_item._id";

//        cursor = DatabaseHelper.database.rawQuery(sqlQueryOne, null);
//        cursorOne = DatabaseHelper.database.rawQuery(sqlQueryTwo, null);
        
        //Cursor with next Abstract Data
        cursorTwo = DatabaseHelper.database.rawQuery(nextAbstractData, null);
    }

    private void authorName() {

        /*
         * Author Names
         */
        //Query for getting author name, email, position, affiliation data for the particular Abstract
        String authorSQLQuery = "SELECT DISTINCT AUTHORS_DETAILS.AUTHOR_FIRST_NAME, " +
        								"AUTHOR_MIDDLE_NAME, AUTHOR_LAST_NAME, AUTHOR_EMAIL, " +
        								"ABSTRACT_AUTHOR_POSITION_AFFILIATION.AUTHOR_AFFILIATION, " +
        								"ABSTRACT_AUTHOR_POSITION_AFFILIATION.AUTHOR_POSITION " +
        						"FROM AUTHORS_DETAILS JOIN ABSTRACT_AUTHOR_POSITION_AFFILIATION USING (AUTHOR_UUID) " +
        						"WHERE AUTHORS_DETAILS.AUTHOR_UUID IN " +
        								"(SELECT AUTHOR_UUID FROM ABSTRACT_AUTHOR_POSITION_AFFILIATION WHERE ABSTRACT_UUID = '" + value + "') " +
        							"AND ABSTRACT_AUTHOR_POSITION_AFFILIATION.AUTHOR_POSITION IN " +
        								"(SELECT AUTHOR_POSITION FROM ABSTRACT_AUTHOR_POSITION_AFFILIATION WHERE ABSTRACT_UUID = '" + value + "') " +
        						"ORDER BY AUTHOR_POSITION ASC;"; 
        
        cursor = DatabaseHelper.database.rawQuery(authorSQLQuery, null);
        Log.i(gtag, "Auth executed query: rows = " + cursor.getCount());
        //cursor.moveToFirst();
        if (cursor != null && cursor.moveToFirst()) {
	        do {
	        	Log.i(gtag, "in DO WHILE");
	        	String authEmail = cursor.getString(cursor.getColumnIndexOrThrow("AUTHOR_EMAIL"));
	        	Log.i(gtag, "author email => " + authEmail);
	        	String authorName = cursor.getString(cursor.getColumnIndexOrThrow("AUTHOR_FIRST_NAME")) + ", " + cursor.getString(cursor.getColumnIndexOrThrow("AUTHOR_LAST_NAME")) ;
	        	String authAffiliation = cursor.getString(cursor.getColumnIndexOrThrow("AUTHOR_AFFILIATION"));
	        	if (authEmail == null || authEmail.equals("null")) {
	        		Log.i(gtag, "in author check - IF NULL");
	        		authorNames.append(Html.fromHtml("<b>" + authorName + "<sup><small>"
                        + authAffiliation + "</small></sup><br/></b>"));

	        	} else {
	        		Log.i(gtag, "in author check - ELSE ");
	        		authorNames.append(Html.fromHtml("<b><a href=\"mailto:" + authEmail + "\">" + authorName + "</a>"  + "<sup><small>"
	                        + authAffiliation + "</small></sup><br/></b>"));
	        		authorNames.setMovementMethod(LinkMovementMethod.getInstance());
	        		
	        	}
	        } while (cursor.moveToNext());
        }
    	
    }

    private void affiliationName() {
    	
    	//SQL Query for getting affiliation data, position for the particular abstract
        String affiliationsSQLQuery = 	"SELECT AFFILIATION_ADDRESS, AFFILIATION_COUNTRY, " +
        										"AFFILIATION_DEPARTMENT, AFFILIATION_SECTION, AFFILIATION_POSITION " +
        								"FROM AFFILIATION_DETAILS JOIN ABSTRACT_AFFILIATION_ID_POSITION USING (AFFILIATION_UUID) " +
        								"WHERE AFFILIATION_UUID IN " +
        									"(SELECT AFFILIATION_UUID FROM ABSTRACT_AFFILIATION_ID_POSITION " +
        										"WHERE ABSTRACT_UUID = '" + value + "')  " +
        								"ORDER BY AFFILIATION_POSITION ASC;";
        
        cursorOne = DatabaseHelper.database.rawQuery(affiliationsSQLQuery, null);
        Log.i(gtag, "Affiliation executed query: rows = " + cursorOne.getCount());
        
        if (cursorOne != null && cursorOne.moveToFirst()) {
	        do {
	        	Log.i(gtag, "in DO WHILE aff");
	        	String affName = cursorOne.getString(cursorOne.getColumnIndexOrThrow("AFFILIATION_SECTION")) + 
	        					", " + cursorOne.getString(cursorOne.getColumnIndexOrThrow("AFFILIATION_DEPARTMENT")) + 
	        					", " + cursorOne.getString(cursorOne.getColumnIndexOrThrow("AFFILIATION_ADDRESS")) + 
	        					", " + cursorOne.getString(cursorOne.getColumnIndexOrThrow("AFFILIATION_COUNTRY")) ;
	        	int affPos = cursorOne.getInt(cursorOne.getColumnIndexOrThrow("AFFILIATION_POSITION"));
	        	afName.append(Html.fromHtml(affPos + ": " + "<b>" + affName + "</b><br/>" ));
	        } while (cursorOne.moveToNext());
        }
        
        
    }

    private void getAbsTitle() {

        cursorTwo.moveToFirst();

        do {

            String getTitle = cursorTwo.getString(cursorTwo.getColumnIndexOrThrow("TITLE"));
            title.setText(getTitle);

        } while (cursorTwo.moveToNext());
    }

    private void getAbsTopic() {

        cursorTwo.moveToFirst();
        do {

            String getTopic = cursorTwo.getString(cursorTwo.getColumnIndexOrThrow("TOPIC"));
            topic.setText(getTopic);

        } while (cursorTwo.moveToNext());

    }

//    private void getAbsEmail() {
//
//        cursorTwo.moveToFirst();
//        do {
//
//            String email = cursorTwo.getString(cursorTwo.getColumnIndexOrThrow("CORRESPONDENCE"));
//
//            int index = email.lastIndexOf(",");
//
//            String emailText = email.substring(index + 1, email.length());
//
//            emailField.append(Html.fromHtml("*<a href= mailto:" + emailText + ">" + emailText
//                    + "</a><br/>"));
//
//        } while (cursorTwo.moveToNext());
//
//    }

    private void getRefs() {

    	String referenceSQLQuery = "SELECT * FROM ABSTRACT_REFERENCES WHERE ABSTRACT_UUID = '" + value +"';";
        referenceCursor = DatabaseHelper.database.rawQuery(referenceSQLQuery, null);
        
        ConRefs.append(Html.fromHtml("<b>References</b><br/>"));
        if (referenceCursor != null && referenceCursor.moveToFirst()) {
        	do {
	        	Log.i(gtag, "in DO WHILE References");
	        	String referenceName = referenceCursor.getString(referenceCursor.getColumnIndexOrThrow("REF_TEXT"));
	        	ConRefs.append(Html.fromHtml("- " + referenceName + "<br/>" ));
	        } while (referenceCursor.moveToNext());
        }

    }

    private void getAcknowledgements() {

        cursorTwo.moveToFirst();

        do {

            String acknowledgements = cursorTwo.getString(cursorTwo
                    .getColumnIndexOrThrow("ACKNOWLEDGEMENTS"));

            if (acknowledgements.length() > 0) {

                ConAck.append(Html.fromHtml("<b>Acknowledgements</b><br />"));
                ConAck.append(acknowledgements + "\n" );
            }

        } while (cursorTwo.moveToNext());
        
        
        
        
    }

    private void getContent() {

        cursorTwo.moveToFirst();

        do {

            String Text = cursorTwo.getString(cursorTwo.getColumnIndexOrThrow("ABSRACT_TEXT"));
            content.setText(Text);

        } while (cursorTwo.moveToNext());
    }

    private void getAfName() {

        affiliationName();

    }

    private void resetAllFields() {

        title.setText("");
        topic.setText("");
        content.setText("");
        ConRefs.setText("");
        emailField.setText("");
        afName.setText("");
        authorNames.setText("");
        ConAck.setText("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.abstract_content_menu, menu);
        starG = menu.findItem(R.id.star);
        
        if(DatabaseHelper.abstractIsFavorite(value) ){
        	isFav = true;
        } else {
        	isFav = false;
        }
        
        if(isFav) {
        	starG.setIcon(R.drawable.ic_action_important_selected);
        } else {
        	starG.setIcon(R.drawable.ic_action_important);
        }
	        
        /*
         * Disable home button
         */
        getActionBar().setDisplayShowHomeEnabled(false);
        /*
         * Hide Application Title
         */
        getActionBar().setDisplayShowTitleEnabled(false);
        /*
         * Set Custom Color in ActionBar
         */
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#003f84")));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
    	
        switch (item.getItemId()) {
        /*
         * Menu Item for switching next and previous data
         */
        
        case R.id.star:
        	Log.i(gtag, "in Onclick of STAR");
        	
			if(isFav){ 
				Log.i(gtag, "in isFAV");
				DatabaseHelper.deleteFromABSTRACT_FAVORITES(value);
				Toast.makeText(getApplicationContext(), "Removed from Favorites",
	                    Toast.LENGTH_SHORT).show();
	            starG.setIcon(R.drawable.ic_action_important);
	            isFav = false;
	        	
	        }else{
	        	Log.i(gtag, "in else of isFAV");
	        	DatabaseHelper.addInABSTRACT_FAVORITES(value);
	        	Toast.makeText(getApplicationContext(), "Added to Favorites",
	                    Toast.LENGTH_SHORT).show();
	        	starG.setIcon(R.drawable.ic_action_important_selected);
	        	isFav = true;
	        }
			
        	break;
        	
        	
            case R.id.next:
            {

                String getCurrentRowIDQuery = "SELECT ROWID FROM ABSTRACT_DETAILS WHERE UUID = '" + value + "';";
                Log.i(gtag, "Current Row ID Query: " + getCurrentRowIDQuery);
                Cursor getRowIdCursor = DatabaseHelper.database.rawQuery(getCurrentRowIDQuery, null);
                Log.i(gtag, "Next Cursor count: " + getRowIdCursor.getCount());
                Log.i(gtag, "Columns:" + getRowIdCursor.getColumnCount() ); 
                Log.i(gtag, "Column Name: " + getRowIdCursor.getColumnName(0));
                Log.i(gtag, "Column Index: " + getRowIdCursor.getColumnIndex("rowid"));
                getRowIdCursor.moveToFirst();
                Log.i(gtag, "Before 483");
                int currentRowID = getRowIdCursor.getInt(0);
                Log.i(gtag, "After 483 & ROW ID = " + currentRowID);
                int nextRecordID = currentRowID + 1;
                Log.i(gtag, "New ROW ID = " + nextRecordID);
                if (nextRecordID <= Abstracts.cursorCount) {

                	//query and get next abstract id 
                	String getNextAbstractUUID = "SELECT UUID FROM ABSTRACT_DETAILS WHERE ROWID = " + nextRecordID + ";";
                    Cursor getNextAbstractCursor = DatabaseHelper.database.rawQuery(getNextAbstractUUID, null);
                    getNextAbstractCursor.moveToFirst();
                	value = getNextAbstractCursor.getString(getNextAbstractCursor.getColumnIndexOrThrow("UUID"));

                    /*
                     * Delete previous data from all field
                     */
                    resetAllFields();

                    /*
                     * Run SQL Quries
                     */

                    sqlQueries();

                    /*
                     * Get Abstract Title
                     */

                    getAbsTitle();

                    /*
                     * Get Abstract Topic
                     */

                    getAbsTopic();

                    /*
                     * Get Email
                     */

                    //getAbsEmail();

                    /*
                     * Get Author Names
                     */

                    authorName();

                    /*
                     * Get Affiliation's Name
                     */

                    getAfName();

                    /*
                     * Get Abstract Content
                     */

                    getContent();

                    /*
                     * get Acknowledgments
                     */

                    getAcknowledgements();

                    /*
                     * Get References
                     */

                    getRefs();
                    
                    invalidateOptionsMenu();
                } else {
                    Toast.makeText(getApplicationContext(), "No more Abstracts Left",
                            Toast.LENGTH_SHORT).show();
                }

                break;
            }

            case R.id.Previous:
            {
            	
            	String getCurrentRowIDQuery = "SELECT ROWID FROM ABSTRACT_DETAILS WHERE UUID = '" + value + "';";
                Log.i(gtag, "Current Row ID Query: " + getCurrentRowIDQuery);
                Cursor getRowIdCursor = DatabaseHelper.database.rawQuery(getCurrentRowIDQuery, null);
                Log.i(gtag, "Prev Cursor count: " + getRowIdCursor.getCount());
                Log.i(gtag, "Columns:" + getRowIdCursor.getColumnCount() ); 
                Log.i(gtag, "Column Name: " + getRowIdCursor.getColumnName(0));
                Log.i(gtag, "Column Index: " + getRowIdCursor.getColumnIndex("rowid"));
                getRowIdCursor.moveToFirst();
                Log.i(gtag, "Before 483");
                int currentRowID = getRowIdCursor.getInt(0);
                Log.i(gtag, "After 483 & ROW ID = " + currentRowID);
                int prevRecordID = currentRowID - 1;
                Log.i(gtag, "New ROW ID = " + prevRecordID);
                
				if (prevRecordID != 0) {

                	//query and get prev abstract id 
                	String getNextAbstractUUID = "SELECT UUID FROM ABSTRACT_DETAILS WHERE ROWID = " + prevRecordID + ";";
                    Cursor getNextAbstractCursor = DatabaseHelper.database.rawQuery(getNextAbstractUUID, null);
                    getNextAbstractCursor.moveToFirst();
                	value = getNextAbstractCursor.getString(getNextAbstractCursor.getColumnIndexOrThrow("UUID"));

                    /*
                     * Delete previous data from all field
                     */
                    resetAllFields();

                    /*
                     * Run SQL Quries
                     */

                    sqlQueries();

                    /*
                     * Get Abstract Title
                     */

                    getAbsTitle();

                    /*
                     * Get Abstract Topic
                     */

                    getAbsTopic();

                    /*
                     * Get Email
                     */

                    //getAbsEmail();

                    /*
                     * Get Author Names
                     */

                    authorName();

                    /*
                     * Get Affiliation's Name
                     */

                    getAfName();

                    /*
                     * Get Abstract Content
                     */

                    getContent();

                    /*
                     * get Acknowledgments
                     */

                    getAcknowledgements();

                    /*
                     * Get References
                     */

                    getRefs();
                    
                    invalidateOptionsMenu();
                    
                } else {
                    Toast.makeText(getApplicationContext(), "This is the first Abstract",
                            Toast.LENGTH_SHORT).show();
                }

                break;
           
            }
            default:

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
