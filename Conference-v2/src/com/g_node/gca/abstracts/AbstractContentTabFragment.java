package com.g_node.gca.abstracts;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shumail.newsroom.R;

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
    
    TextView absSortID;

    Button btnOpenAbstractFig;

    private String value;
    
    Cursor cursor, cursorOne, cursorTwo, referenceCursor;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		setHasOptionsMenu(true);  
		View rootView = inflater.inflate(R.layout.fragment_abstracts_content, container, false);
		Log.i("GCA-Abs-Frag", "Abstract Content Fragment onCreateViews");
			return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("GCA-Abs-Frag", "Abstract Content Fragment onViewCreated");
        
        /*
         * Initializing fields
         */
        initial_UI();
        resetAllFields();
        
        /*
         * Getting UUID of intent that's detail is to be shown.
         */
        
        value = TabsPagerAdapter.getValue();
        Log.i("GCA-Abs-Frag", "new value: " + value);
		
        /*
         * Run SQL Queries to fetch data from database.
         */
        
        fetchBasicAbstractDataFromDB();
        
        /*
         * Fetch Authors name for abstract and update author name fields
         */
        fetchAndUpdateAuthorsDataFromDB();
        
        /*
         * Get Affiliation Name for associate abstracts - and update affiliations view 
         */
        fetchAndUpdateAffiliationNamesFromDB();
                
        /*
         * Getting the title of Abstract - and update abstract title view
         */
        getAndUpdateAbstractTitle();
        
        /*
         * Set Title to BOLD
         */
        title.setTypeface(null, Typeface.BOLD);
        
        /*
         * Getting Topic of the Abstract - and update abstract topic view
         */
        
        getAndUpdateAbstractTopic();
        
        /*
         * Get Abstract Content/Text from Cursor and display - update abstract Text view
         */
        getAndUpdateAbstractContent();
        
        /*
         * Get acknowledgements for Abstract and display - update abstract acknowldegement view
         */
        getAndUpdateAbstractAcknowledgements();
        
        /*
         * Get References from database and display - Update references view
         */
        getAndUpdateAbstractReferences();
        
        /*
         * Get associated Figures from database and set button enabled/disabled - 
         */
        getAndUpdateAbstractFiguresBtn();
        
	} //end onViewCreated
	
	@Override
	public void onDestroy() {
        super.onDestroy();
        Log.i("GCA-Abs-Frag", "AbstractContent Fragment - on Destroy");
    }

	
	
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
        /*
         * TextView for Acknowledgments
         */
        absSortID = (TextView)getView().findViewById(R.id.absSortID);
        /*
         * Clickable for showing images assosiated with Abstract
         */
        btnOpenAbstractFig = (Button) getView().findViewById(R.id.btnOpenAbstractFig);
    
	}	//end intialUI
	
	
	/*
     * Function for executing SQL Queries that fetch next/prev or current abstract data
     */
    private void fetchBasicAbstractDataFromDB() {
    	Log.i(gtag, "SQLQueries function");
        String nextAbstractData = "SELECT UUID AS _id , TOPIC, TITLE, " +
        		"ABSRACT_TEXT, STATE, SORTID, REASONFORTALK, MTIME, TYPE,DOI, COI, ACKNOWLEDGEMENTS " +
        		"FROM ABSTRACT_DETAILS WHERE _id = '" + value + "';";
        
        //Cursor with next Abstract Data
        cursorTwo = DatabaseHelper.database.rawQuery(nextAbstractData, null);
    }
    
    /*
     * Function for getting Author Names for the abstract & add to the view
     */
    private void fetchAndUpdateAuthorsDataFromDB() {
       
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
        
        List<String> abstractAuthorNames = new ArrayList<String>();
        
        if (cursor != null && cursor.moveToFirst()) {
	        do {
	        	Log.i(gtag, "in DO WHILE");
	        	String authEmail = cursor.getString(cursor.getColumnIndexOrThrow("AUTHOR_EMAIL"));
	        	Log.i(gtag, "author email => " + authEmail);
	        	String authorName = cursor.getString(cursor.getColumnIndexOrThrow("AUTHOR_FIRST_NAME")) + ", " + cursor.getString(cursor.getColumnIndexOrThrow("AUTHOR_LAST_NAME")) ;
	        	String authAffiliation = cursor.getString(cursor.getColumnIndexOrThrow("AUTHOR_AFFILIATION"));
	        	
	        	//remove unwanted characters from affiliation superscript id's
	        	String authAffiliationINTs = authAffiliation.replaceAll("[^0-9][,]", "");
	        	
	        	//pattern to get the digits so to increment those by one later so affiliation numbering starts from 1 instead of 0 
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
    
    
    
    /*
	 *Function for getting affiliation names for that abstract and adding to the view 
	 */
    private void fetchAndUpdateAffiliationNamesFromDB() {
    	
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
    
    
    /*
	 *Function for getting abstract title and adding to the view 
	 */
    private void getAndUpdateAbstractTitle() {

        cursorTwo.moveToFirst();

        do {

            String getTitle = cursorTwo.getString(cursorTwo.getColumnIndexOrThrow("TITLE"));
            title.setText(getTitle);

        } while (cursorTwo.moveToNext());
    }
    
    /*
	 *Function for getting abstract topic and adding to the view 
	 */
    private void getAndUpdateAbstractTopic() {

        cursorTwo.moveToFirst();
        do {

            String getTopic = cursorTwo.getString(cursorTwo.getColumnIndexOrThrow("TOPIC"));
            topic.setText(getTopic);

        } while (cursorTwo.moveToNext());
    }
    
    
    /*
	 *Function for getting abstract referenes and adding to the view 
	 */
    private void getAndUpdateAbstractReferences() {

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
    
    
    /*
	 *Function for getting acknowledgements for that abstract and adding to the view 
	 */
    private void getAndUpdateAbstractAcknowledgements() {

        cursorTwo.moveToFirst();

        do {

            String acknowledgements = cursorTwo.getString(cursorTwo
                    .getColumnIndexOrThrow("ACKNOWLEDGEMENTS"));

            if (acknowledgements.length() > 0) {
            	if(acknowledgements.equals("null")){
            		ConAck.append("");
            	}else{
            		ConAck.append(acknowledgements + "\n" );
            	}
                
            }

        } while (cursorTwo.moveToNext());
        
    }
    
    /*
	 * Function for getting abstract text & parsing of SortID and adding to the view 
	 */
    private void getAndUpdateAbstractContent() {

        cursorTwo.moveToFirst();

            do {

                String Text = cursorTwo.getString(cursorTwo.getColumnIndexOrThrow("ABSRACT_TEXT"));
                content.setText(Text);

            } while (cursorTwo.moveToNext());
            
            //parsing SortID to extract group id & poster number and add it to abstract text body.
            cursorTwo.moveToFirst();
            int sortID = cursorTwo.getInt(cursorTwo.getColumnIndexOrThrow("SORTID"));
            Log.i("GCA-SortID", "Sort ID: " + sortID);
            if(sortID != 0) {	
            	int groupid =  ((sortID & (0xFFFF << 16)) >> 16);
            	int poster_no = sortID & 0xFFFF;
            	
            	//absSortID.append("\r\nSort ID: " + sortID);
            	absSortID.append("Group ID: " + get_groupid_str(groupid));
            	absSortID.append("\r\r-\r\rPoster No: " + poster_no);
            
            }else {
            	absSortID.setVisibility(View.GONE);
            }
    }
    
    /*
	 * Method mapping groupid to the corresponding String 
	 */
    private String get_groupid_str(int groupid) {
    	String[] id2str = getResources().getStringArray(R.array.groupid2str);
    	//String[] id2str = {"Talk","Contributed Talk","W","T"};
		return id2str[groupid];
	}

	/*
	 * Function for getting abstract figures and updating the button 
	 */
    private void getAndUpdateAbstractFiguresBtn() {
    	String getFiguresQuery = "SELECT * FROM ABSTRACT_FIGURES WHERE ABSTRACT_UUID = '" + value +"';";
    	Cursor absFiguresCursor = DatabaseHelper.database.rawQuery(getFiguresQuery, null);
    	
    	if(absFiguresCursor.getCount() > 0) {
    		btnOpenAbstractFig.setText("Show Figures" + "  (" + absFiguresCursor.getCount() + ")");
    		
    		btnOpenAbstractFig.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					//if Internet is connected
					if(isNetworkAvailable()){
						
						//check if interent is WIFI
						ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
						NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
						NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
						
						if (mWifi.isConnected()) {
							//Toast.makeText(getActivity(), "Connected via WLAN", Toast.LENGTH_SHORT).show();
							Intent figuresIntent = new Intent(getActivity(), AbstractFiguresActivity.class);
							figuresIntent.putExtra("abs_uuid", value);
							startActivity(figuresIntent);
						
						} else if(mMobile.isConnected()) {
							//if connected with mobile data - 2G, 3G, 4G etc
							//Toast.makeText(getActivity(), "Connected via Mobile Internet", Toast.LENGTH_SHORT).show();
							
							AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
							builder.setTitle("Additional Traffic Warning").setMessage("Downloading of Figures over Mobile Internet may create additional Traffic. Do you want to Continue ?")
							       .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
							           public void onClick(DialogInterface dialog, int id) {
							               // if user Agrees to continue
							        	   Intent figuresIntent = new Intent(getActivity(), AbstractFiguresActivity.class);
											figuresIntent.putExtra("abs_uuid", value);
											startActivity(figuresIntent);
							           }
							       })
							       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							           public void onClick(DialogInterface dialog, int id) {
							               // Handle Cancel
							        	   dialog.cancel();
							           }
							       })
							       .setIcon(android.R.drawable.ic_dialog_alert)
								     .show();
							
						} else {
							;
						}	//end if/else of wlan/mobile
						
					} else {
						Toast.makeText(getActivity(), "Not Connected to Internet - Please connect to Internet first", Toast.LENGTH_SHORT).show();
					} 	//end if/else of isNetworkAvailable
					
				}
			});
    	
    	}else{
    		btnOpenAbstractFig.setText("No Figures Found");
    		btnOpenAbstractFig.setEnabled(false);
    		//btnOpenAbstractFig.setVisibility(View.GONE);
    	}
    }
    
    /*
	 * Function for resetting all the fields 
	 */
    private void resetAllFields() {

            title.setText("");
            topic.setText("");
            content.setText("");
            ConRefs.setText("");
            afName.setText("");
            authorNames.setText("");
            ConAck.setText("");
            absSortID.setText("");

    }
    
  //Helper method to determine if Internet connection is available.
  	private boolean isNetworkAvailable() {
  	    ConnectivityManager connectivityManager 
  	          = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
  	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
  	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  	} 
    
} //end class


