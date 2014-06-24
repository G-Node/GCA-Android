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
	
	boolean isFav = true;
	
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

    Cursor cursor, cursorOne, cursorTwo;

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
//        /*
//         * Show Author Names and Corresponding Author Names with a (*) sign
//         */
//        authorName();
        
        
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
        
        
        //Test set affiliations name
        //afName.setText("AFF name 1 \nAFF Name 2");
        
//        /*
//         * Get Affiliation Name for associate abstracts
//         */
//        affiliationName();
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
        
        String referenceSQLQuery = "SELECT * FROM ABSTRACT_REFERENCES WHERE ABSTRACT_UUID = '" + value +"';";
        cursorTwo = DatabaseHelper.database.rawQuery(referenceSQLQuery, null);
        
        ConRefs.append(Html.fromHtml("<b>References</b><br/>"));
        if (cursorTwo != null && cursorTwo.moveToFirst()) {
        	do {
	        	Log.i(gtag, "in DO WHILE References");
	        	String referenceName = cursorTwo.getString(cursorTwo.getColumnIndexOrThrow("REF_TEXT"));
	        	ConRefs.append(Html.fromHtml("- " + referenceName + "<br/>" ));
	        } while (cursorTwo.moveToNext());
        }
        
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

        sqlQueryOne = "select authors_abstract.abstractauthor_id AS AUTH_ID,abstract_author.NAME AS NAME,abstract_author.IS__CORRESPONDING,ABSTRACT_AFFILIATION.AFFILIATION_NUMBER AS NUMBER "
                + "from abstracts_item,abstract_author,authors_abstract,ABSTRACT_AFFILIATION "
                + "where abstracts_item._id = authors_abstract.abstractsitem_id "
                + "and abstract_author._id = authors_abstract.abstractauthor_id "
                + "and ABSTRACT_AFFILIATION._id = authors_abstract.ABSTRACTAFFILIATION_ID "
                + "and abstracts_item._id = " + value;

        sqlQueryTwo = "select CORRESPONDING_AUTHOR_ID AS ID from ABSTRACT_AUTHOR_CORRESPONDENCE where abstractsItem_id = "
                + value;

        sqlQueryThree = "select abstracts_item._id AS ID,CORRESPONDENCE,title, type, topic, text,af_name as af,REFS,ACKNOWLEDGEMENTS "
                + "from abs_affiliation_name,abstract_affiliation,abstracts_item,abstract_author,authors_abstract "
                + "where ID = "
                + value
                + " and abstracts_item._id = authors_abstract.abstractsitem_id "
                + "and abstract_author._id = authors_abstract.abstractauthor_id "
                + "and abstract_affiliation._id = abstract_author._id "
                + "and abs_affiliation_name._id = abstracts_item._id GROUP By abstracts_item._id";

        cursor = DatabaseHelper.database.rawQuery(sqlQueryOne, null);
        cursorOne = DatabaseHelper.database.rawQuery(sqlQueryTwo, null);
        cursorTwo = DatabaseHelper.database.rawQuery(sqlQueryThree, null);
    }

    private void authorName() {

        /*
         * Author Names
         */
        if ((cursor != null && cursor.moveToFirst())
                && (cursorOne != null && cursorOne.moveToFirst())) {
            do {

                // String Corrosponding =
                // cursor.getString(cursor.getColumnIndexOrThrow("IS__CORRESPONDING"));
                /*
                 * getName =
                 * cursor.getString(cursor.getColumnIndexOrThrow("NAME")); int
                 * getIndex = email.indexOf(","); String getCorsName
                 * =email.substring(0, getIndex); int newIndex =
                 * getCorsName.indexOf("."); String getFormattedName =
                 * getCorsName.substring(newIndex+1,getCorsName.length());
                 * Log.e("QW", ""); Log.e("AAA",
                 * String.valueOf(getFormattedName.
                 * trim().equalsIgnoreCase(getName.trim())));
                 * if(getFormattedName.trim().equalsIgnoreCase(getName.trim())){
                 * affiliation_ID =
                 * cursor.getString(cursor.getColumnIndexOrThrow("NUMBER"));
                 * authorNames
                 * .append(Html.fromHtml("\n"+getName+"<sup><small>"+
                 * affiliation_ID +"*</small></sup><br/>"));
                 * authorNames.append("\n"); }else{ affiliation_ID =
                 * cursor.getString(cursor.getColumnIndexOrThrow("NUMBER"));
                 * authorNames
                 * .append(Html.fromHtml("\n"+getName+"<sup><small>"+
                 * affiliation_ID +"</small></sup><br/>"));
                 * authorNames.append("\n"); }
                 */
                String getID = cursor.getString(cursor.getColumnIndexOrThrow("AUTH_ID"));
                String Corr_AUTH_ID = cursorOne.getString(cursorOne.getColumnIndexOrThrow("ID"));
                /*
                 * Compare Author id and Corresponding Author id if Author is a
                 * Corresponding Author show (*) sign
                 */
                if (getID.trim().equalsIgnoreCase(Corr_AUTH_ID.trim())) {
                    getName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                    affiliation_ID = cursor.getString(cursor.getColumnIndexOrThrow("NUMBER"));
                    authorNames.append(Html.fromHtml("<b>" + getName + "<sup><small>"
                            + affiliation_ID + "*</small></sup><br/></b>"));
                } else {
                    getName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                    affiliation_ID = cursor.getString(cursor.getColumnIndexOrThrow("NUMBER"));
                    authorNames.append(Html.fromHtml("<b>" + getName + "<sup><small>"
                            + affiliation_ID + "</small></sup><br/></b>"));
                }

            } while (cursor.moveToNext());
        }
    }

    private void affiliationName() {

        /*
         * Split String
         */
        String[] newAfName = affiliationName.split("\",\"");
        for (int i = 0; i < newAfName.length; i++) {
            newAfName[i] = newAfName[i].replace("\"", "").replace(":", ". ");
        }
        /*
         * Sorting Data
         */
        Arrays.sort(newAfName);
        for (String string : newAfName) {
            /*
             * Count Comma in a String
             */
            int countComma = string.replaceAll("[^,]", "").length();
            /*
             * If String has more than 1 comma
             */
            if (countComma > 1) {
                /**
                 * Arrange String as Number. Institute Name, Department Name ,
                 * Location But Format was Number. Department Name, Institute
                 * Name, Location
                 */
                Pattern pattern = Pattern.compile("(\\d++)\\.([^,]++),\\s*+([^,]++),\\s*+(.*+)");
                Matcher matcher = pattern.matcher("");
                matcher.reset(string);
                String Af_Names = matcher.replaceAll("$1. $3, $2, $4");
                afName.append(Af_Names + "\n");
                afName.setTypeface(null, Typeface.ITALIC);
            } else {
                afName.append(string + "\n");
                afName.setTypeface(null, Typeface.ITALIC);
            }
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

    private void getAbsEmail() {

        cursorTwo.moveToFirst();
        do {

            String email = cursorTwo.getString(cursorTwo.getColumnIndexOrThrow("CORRESPONDENCE"));

            int index = email.lastIndexOf(",");

            String emailText = email.substring(index + 1, email.length());

            emailField.append(Html.fromHtml("*<a href= mailto:" + emailText + ">" + emailText
                    + "</a><br/>"));

        } while (cursorTwo.moveToNext());

    }

    private void getRefs() {

        cursorTwo.moveToFirst();

        do {

            String refs = cursorTwo.getString(cursorTwo.getColumnIndexOrThrow("REFS"));
            if (refs.length() > 0) {

                ConRefs.append(Html.fromHtml("<b>Reference</b><br/>"));
                ConRefs.append("\n" + refs);

            }

        } while (cursorTwo.moveToNext());

    }

    private void getAcknowledgements() {

        cursorTwo.moveToFirst();

        do {

            String acknowledgements = cursorTwo.getString(cursorTwo
                    .getColumnIndexOrThrow("ACKNOWLEDGEMENTS"));

            if (acknowledgements.length() > 0) {

                ConAck.append(Html.fromHtml("<b>Acknowledgements</b><br />"));
                ConAck.append("\n" + acknowledgements);
            }

        } while (cursorTwo.moveToNext());
    }

    private void getContent() {

        cursorTwo.moveToFirst();

        do {

            String Text = cursorTwo.getString(cursorTwo.getColumnIndexOrThrow("TEXT"));
            content.setText(Text);

        } while (cursorTwo.moveToNext());
    }

    private void getAfName() {

        cursorTwo.moveToFirst();

        do {

            affiliationName = cursorTwo.getString(6);

        } while (cursorTwo.moveToNext());

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
        starG.setIcon(R.drawable.ic_action_important_selected);
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
				Toast.makeText(getApplicationContext(), "Removed from Favorites",
	                    Toast.LENGTH_SHORT).show();
	            starG.setIcon(R.drawable.ic_action_important_selected);
	        }else{
	        	Log.i(gtag, "in else of isFAV");
	        	Toast.makeText(getApplicationContext(), "Added to Favorites",
	                    Toast.LENGTH_SHORT).show();
	        	starG.setIcon(R.drawable.ic_action_important);
	        }
			isFav = !isFav; // reverse
	
        	break;
        	
        	
            case R.id.next:

                int currentValue = Integer.parseInt(value) + 1;
                if (currentValue <= Abstracts.cursorCount) {

                    value = String.valueOf(currentValue);

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

                    getAbsEmail();

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
                } else {
                    Toast.makeText(getApplicationContext(), "No more Abstracts Left",
                            Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.Previous:

                int previousValue = Integer.parseInt(value) - 1;
                if (previousValue != 0) {

                    value = String.valueOf(previousValue);

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

                    getAbsEmail();

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
                     * Get Acknowledgments
                     */

                    getAcknowledgements();

                    /*
                     * Get References
                     */

                    getRefs();

                } else {
                    Toast.makeText(getApplicationContext(), "This is the first Abstract",
                            Toast.LENGTH_SHORT).show();
                }

                break;
           

            default:

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
