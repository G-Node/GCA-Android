package com.g_node.gca.abstracts;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.shumail.newsroom.R;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AbstractContentTabFragment extends Fragment {
	
	String gtag = "GCA-Abs-Con";

	boolean isFav;
	MenuItem starG;
	
    TextView content;

    TextView title;

    TextView topic;

    TextView afName;

    TextView authorNames;

    TextView ConRefs;

    TextView ConAck;

    Button btn;

    private String value;
    
    Cursor cursor, cursorOne, cursorTwo, referenceCursor;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		setHasOptionsMenu(true);  
		View rootView = inflater.inflate(R.layout.abstracts_show, container, false);
		
			return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(gtag, "Abstract Content Fragment onViewCreated");
        
        /*
         * Initializing fields
         */
        initial_UI();
        
        /*
         * Getting data from Intent
         */
		
		Bundle getData = getActivity().getIntent().getExtras();
		
		String abstracts = getData.getString("abstracts");
        String Title = getData.getString("Title");
        String Topic = getData.getString("Topic");
        String acknowledgments = getData.getString("acknowledgements");
        
        value = getArguments().getString("value");
		
        /*
         * Show Author names for that Abstract
         */
        authorName();
        
        /*
         * Get Affiliation Name for associate abstracts
         */
        affiliationName();
        
        title.setText(Title);
        /*
         * Set Title to BOLD
         */
        title.setTypeface(null, Typeface.BOLD);
        topic.setText(Topic);
        
        /*
         * Set Abstract Text in view
         */
        
        content.setText(abstracts);
        /*
         * If acknowledgments contain any data
         */
        if (acknowledgments.length() > 0) {
        	
            ConAck.append(acknowledgments + "\n" );
        }
        
        //Get References from db and show in view
        getRefs();
    
	}
	
//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//	    inflater.inflate(R.menu.abstract_content_menu, menu);
//	    
//	    starG = menu.findItem(R.id.star);
//        
//        if(DatabaseHelper.abstractIsFavorite(value) ){
//        	isFav = true;
//        } else {
//        	isFav = false;
//        }
//        
//        if(isFav) {
//        	starG.setIcon(R.drawable.ic_action_important_selected);
//        } else {
//        	starG.setIcon(R.drawable.ic_action_important);
//        }
//	        
//        /*
//         * Disable home button
//         */
//        getActionBar().setDisplayShowHomeEnabled(false);
//        /*
//         * Hide Application Title
//         */
//        getActionBar().setDisplayShowTitleEnabled(false);
//        /*
//         * Set Custom Color in ActionBar
//         */
//
//	    
//	    super.onCreateOptionsMenu(menu,inflater);
//	}
//	
//	@Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // TODO Auto-generated method stub
//    
//    	switch (item.getItemId()) {
//        /*
//         * Menu Item for switching next and previous data
//         */
//        
//        case R.id.star:
//        	Log.i(gtag, "in Onclick of STAR");
//        	
//			if(isFav){ 
//				Log.i(gtag, "in isFAV");
//				DatabaseHelper.deleteFromABSTRACT_FAVORITES(value);
//				Toast.makeText(getActivity().getApplicationContext(), "Removed from Favorites",
//	                    Toast.LENGTH_SHORT).show();
//	            starG.setIcon(R.drawable.ic_action_important);
//	            isFav = false;
//	        	
//	        }else{
//	        	Log.i(gtag, "in else of isFAV");
//	        	DatabaseHelper.addInABSTRACT_FAVORITES(value);
//	        	Toast.makeText(getActivity().getApplicationContext(), "Added to Favorites",
//	                    Toast.LENGTH_SHORT).show();
//	        	starG.setIcon(R.drawable.ic_action_important_selected);
//	        	isFav = true;
//	        }
//			
//        	break;
//        
//        default:
//
//            break;
//    	}
//    
//    	return super.onOptionsItemSelected(item);
//    	
//    }	//end onOptionsMenuItemSelected
	
	
	
	private void initial_UI() {
        /*
         * TextView for Abstract Text
         */
        content = (TextView)getView().findViewById(R.id.Content);
        /*
         * TextView for Abstract Title
         */
        title = (TextView)getView().findViewById(R.id.ConTitle);
        /*
         * TextView for Abstract Topic
         */
        topic = (TextView)getView().findViewById(R.id.ConTopic);
        /*
         * TextView for Abstract Author
         */
        authorNames = (TextView)getView().findViewById(R.id.ConAuthor);
        /*
         * TextView for Affiliation Name
         */
        afName = (TextView)getView().findViewById(R.id.ConAfName);
        /*
         * TextView for Reference
         */
        ConRefs = (TextView)getView().findViewById(R.id.Conrefs);
        /*
         * TextView for Acknowledgments
         */
        ConAck = (TextView)getView().findViewById(R.id.ConACK);
    
	}	//end intialUI
	
	//Query for fetching next/prev abstract data
    private void sqlQueries() {
      
        String nextAbstractData = "SELECT UUID AS _id , TOPIC, TITLE, " +
        		"ABSRACT_TEXT, STATE, SORTID, REASONFORTALK, MTIME, TYPE,DOI, COI, ACKNOWLEDGEMENTS " +
        		"FROM ABSTRACT_DETAILS WHERE _id = '" + value + "';";
        
        //Cursor with next Abstract Data
        cursorTwo = DatabaseHelper.database.rawQuery(nextAbstractData, null);
    }
    
    private void authorName() {
        /*
         * Function for getting Author Names & addint to the view
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
        
        List<String> abstractAuthorNames = new ArrayList<String>();
        
        if (cursor != null && cursor.moveToFirst()) {
	        do {
	        	Log.i(gtag, "in DO WHILE");
	        	String authEmail = cursor.getString(cursor.getColumnIndexOrThrow("AUTHOR_EMAIL"));
	        	Log.i(gtag, "author email => " + authEmail);
	        	String authorName = cursor.getString(cursor.getColumnIndexOrThrow("AUTHOR_FIRST_NAME")) + ", " + cursor.getString(cursor.getColumnIndexOrThrow("AUTHOR_LAST_NAME")) ;
	        	String authAffiliation = cursor.getString(cursor.getColumnIndexOrThrow("AUTHOR_AFFILIATION"));
	        	
	        	String authAffiliationINTs = authAffiliation.replaceAll("[^0-9][,]", "");
	        	
	        	Pattern digitPattern = Pattern.compile("(\\d)"); // EDIT: Increment each digit.

	        	Matcher matcher = digitPattern.matcher(authAffiliationINTs);
	        	StringBuffer result = new StringBuffer();
	        	while (matcher.find())
	        	{
	        	    matcher.appendReplacement(result, String.valueOf(Integer.parseInt(matcher.group(1)) + 1));
	        	}
	        	matcher.appendTail(result);
	        	authAffiliation = result.toString();
	        	
	        	
	        	if (abstractAuthorNames.indexOf(authorName) == -1 ) {
	        		abstractAuthorNames.add(authorName);
	        		
	        		if (authEmail == null || authEmail.equals("null")) {
		        		Log.i(gtag, "in author check - IF NULL");
		        		authorNames.append(Html.fromHtml("<b>" + authorName + "</b><sup><small>"
	                        + authAffiliation + "</small></sup><br/>"));

		        	} else {
		        		Log.i(gtag, "in author check - ELSE ");
		        		authorNames.append(Html.fromHtml("<b><a href=\"mailto:" + authEmail + "\">" + authorName + "</a>"  + "</b><sup><small>"
		                        + authAffiliation + "</small></sup><br/>"));
		        		authorNames.setMovementMethod(LinkMovementMethod.getInstance());
		        		
		        	}
	        	} else {
	        		;
	        	}
	        	
	        } while (cursor.moveToNext());
        }
    	
    } //end authorName function
    
    private void affiliationName() {
    	/*
    	 *Function for getting affiliation names for that abstract and adding to the view 
    	 */
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
	        	affPos++;
	        	afName.append(Html.fromHtml(affPos + ": " + "<b>" + affName + "</b><br/>" ));
	        } while (cursorOne.moveToNext());
        }        
    }  //end affiliationName    
    
    
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

    private void getRefs() {

    	String referenceSQLQuery = "SELECT * FROM ABSTRACT_REFERENCES WHERE ABSTRACT_UUID = '" + value +"';";
        referenceCursor = DatabaseHelper.database.rawQuery(referenceSQLQuery, null);
        
        if (referenceCursor != null && referenceCursor.moveToFirst()) {
        	int refNumber = 1;
        	do {
	        	Log.i(gtag, "in DO WHILE References");
	        	String referenceName = referenceCursor.getString(referenceCursor.getColumnIndexOrThrow("REF_TEXT"));
	        	ConRefs.append(Html.fromHtml(refNumber + ": " + referenceName + "<br/>" ));
	        	refNumber++;
	        } while (referenceCursor.moveToNext());
        }
    }
    
    private void getAcknowledgements() {

        cursorTwo.moveToFirst();

        do {

            String acknowledgements = cursorTwo.getString(cursorTwo
                    .getColumnIndexOrThrow("ACKNOWLEDGEMENTS"));

            if (acknowledgements.length() > 0) {

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
            afName.setText("");
            authorNames.setText("");
            ConAck.setText("");

        }
        
    
    
    
    
}


