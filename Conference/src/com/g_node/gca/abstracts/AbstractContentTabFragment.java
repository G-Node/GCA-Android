/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com> (2014 Version)
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.abstracts;

import java.util.ArrayList;
import java.util.Arrays;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.g_node.gcaa.R;

public class AbstractContentTabFragment extends Fragment {
	
	String gtag = "GCA-Abs-Con";

	boolean isFav;
	MenuItem starG;
	
    WebView content;

    TextView title;

    TextView topic;

    TextView afName;

    TextView authors;

    TextView ConRefs;

    TextView ConAck;
    
    TextView absSortID;

    Button btnOpenAbstractFig;

    private String uuid;
    
    private Cursor mAuthorCursor, mAffiliationCursor, mAbstractDataCursor, mReferenceCursor;    
    
	private final DatabaseHelper mDbHelper = DatabaseHelper
			.getInstance(this.getActivity());

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		setHasOptionsMenu(true);  
		View rootView = inflater.inflate(R.layout.fragment_abstracts_content, 
				container, false);
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
        
        uuid = TabsPagerAdapter.getUuid();
        Log.i("GCA-Abs-Frag", "new value: " + uuid);
		
        /*
         * Run SQL Queries to fetch data from database.
         */        
        mAbstractDataCursor = mDbHelper.fetchAbtractDetailsByUUID(uuid);
        
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
        content = (WebView) getView().findViewById(R.id.Content);
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
        authors = (TextView)getView().findViewById(R.id.ConAuthor);
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
        btnOpenAbstractFig = (Button) getView().findViewById(
        		R.id.btnOpenAbstractFig);
        //btnOpenAbstractFig.setVisibility(View.GONE);
    
	}	//end intialUI
	
	
   
    /*
     * Function for getting Author Names for the abstract & add to the view
     */
    private void fetchAndUpdateAuthorsDataFromDB() {
        mAuthorCursor = mDbHelper.fetchAuthorsByAbsId(uuid);
        Log.i(gtag, "Auth executed query: rows = " + mAuthorCursor.getCount());        
        List<String> abstractAuthorNames = new ArrayList<String>();
        if (mAuthorCursor != null && mAuthorCursor.moveToFirst()) {
	        do {
	        	Log.i(gtag, "in DO WHILE");
	        	String authEmail = mAuthorCursor.getString(mAuthorCursor
	        			.getColumnIndexOrThrow("AUTHOR_EMAIL"));
	        	Log.i(gtag, "author email => " + authEmail);
	        	String authorName = mAuthorCursor.getString(mAuthorCursor
	        			.getColumnIndexOrThrow("AUTHOR_FIRST_NAME")) + " " +
	        			mAuthorCursor.getString(mAuthorCursor
	        					.getColumnIndexOrThrow("AUTHOR_LAST_NAME")) ;
	        	String authAffiliation = mAuthorCursor.getString(mAuthorCursor
	        			.getColumnIndexOrThrow("AUTHOR_AFFILIATION"));
	        	
	        	//remove unwanted characters from affiliation superscript id's
	        	String [] authAffiliations = authAffiliation.
	        								 replaceAll("[^0-9][,]", "").
	        								 split(",");

	        	if (!authAffiliations[0].equals("")){
		        	int [] authAffiliationsInt = new int[authAffiliations.length];
		        	int i = 0;
	        		for (String affiliation_nr:authAffiliations){
	        			authAffiliationsInt[i] = Integer.parseInt(
	        					affiliation_nr)+1;
	        			Arrays.sort(authAffiliationsInt);
	        		}
	        		i=0;
		        	for (int affiliation_nr:authAffiliationsInt){
		        		authAffiliations[i++] = Integer.toString(affiliation_nr);
		        	}
	        	}
	        	String auth_affiliations_str = Arrays.toString(authAffiliations);
	        	auth_affiliations_str = auth_affiliations_str.substring(1, 
	        			                auth_affiliations_str.length()-1);
	        	
	        	if (abstractAuthorNames.indexOf(authorName) == -1 ) {
	        		abstractAuthorNames.add(authorName);
	        		
	        		if (authEmail == null || authEmail.equals("null")) {
		        		Log.i(gtag, "in author check - IF NULL");
		        		authors.append(Html.fromHtml("<b>" + authorName + "</b><sup><small>"
	                        + auth_affiliations_str + "</small></sup><br/>"));

		        	} else {
		        		Log.i(gtag, "in author check - ELSE ");
		        		//authorNames.append(Html.fromHtml("<b><a href=\"mailto:" + authEmail + "\">" + authorName + "</a>"  + "</b><sup><small>"
		                //        + authAffiliation + "</small></sup><br/>"));
		        		//authorNames.setMovementMethod(LinkMovementMethod.getInstance());
		        		authors.append(Html.fromHtml("<b>" + authorName + "</b><sup><small>"
		                        + auth_affiliations_str + "</small></sup><br/>"));
		        	}
	        	} else {
	        		;
	        	}
	        	
	        } while (mAuthorCursor.moveToNext());
        }
        
    	
    } //end authorName function
    
    
    
    /*
	 *Function for getting affiliation names for that abstract and adding to the view 
	 */
    private void fetchAndUpdateAffiliationNamesFromDB() {
             
        mAffiliationCursor = mDbHelper.fetchAffiliationsByAbsId(uuid);
        Log.i(gtag, "Affiliation executed query: rows = " + mAffiliationCursor.getCount());
        
        if (mAffiliationCursor != null && mAffiliationCursor.moveToFirst()) {
	        do {
	        	Log.i(gtag, "in DO WHILE aff");
	        	String [] aff_array = {mAffiliationCursor.getString(
	        				mAffiliationCursor.getColumnIndexOrThrow(
	        								"AFFILIATION_SECTION")),
		        			mAffiliationCursor.getString(
		        					mAffiliationCursor.getColumnIndexOrThrow(
		        							"AFFILIATION_DEPARTMENT")),	        	
		        			mAffiliationCursor.getString(
		        					mAffiliationCursor.getColumnIndexOrThrow(
		        							"AFFILIATION_ADDRESS")),
		        			mAffiliationCursor.getString(
		        					mAffiliationCursor.getColumnIndexOrThrow(
		        							"AFFILIATION_COUNTRY")) 
		        			};
	        	String affName = "";
	        	for (String txt:aff_array){
	        		if (!txt.equals("null")&&!txt.equals("")){
	        			affName = affName+txt+", ";
	        		}
	        	}
	        	affName = affName.substring(0, affName.length()-2);
	        	int affPos = mAffiliationCursor.getInt(
	        			mAffiliationCursor.getColumnIndexOrThrow(
	        					"AFFILIATION_POSITION"));
	        	affPos++;
	        	afName.append(Html.fromHtml(affPos + ": " + "<b>" + affName + 
	        			"</b><br/>" ));
	        } while (mAffiliationCursor.moveToNext());
        }        
    }  //end affiliationName    
    
    
    /*
	 *Function for getting abstract title and adding to the view 
	 */
    private void getAndUpdateAbstractTitle() {
        mAbstractDataCursor.moveToFirst();
        do {

            String getTitle = mAbstractDataCursor.getString(mAbstractDataCursor
            		.getColumnIndexOrThrow("TITLE"));
            title.setText(getTitle);
        } while (mAbstractDataCursor.moveToNext());
    }
    
    /*
	 *Function for getting abstract topic and adding to the view 
	 */
    private void getAndUpdateAbstractTopic() {
        mAbstractDataCursor.moveToFirst();
        do {
            String getTopic = mAbstractDataCursor.getString(mAbstractDataCursor
            		.getColumnIndexOrThrow("TOPIC"));
            topic.setText(getTopic);
        } while (mAbstractDataCursor.moveToNext());
    }
    
    
    /*
	 *Function for getting abstract referenes and adding to the view 
	 */
    private void getAndUpdateAbstractReferences() {
        mReferenceCursor = mDbHelper.fetchReferencesByAbsId(uuid);
        String referenceName;
        if (mReferenceCursor != null && mReferenceCursor.moveToFirst()) {
        	int refNumber = 1;
        	do {
	        	Log.i(gtag, "in DO WHILE References");
	        	String ref_txt = mReferenceCursor.getString(
	        			mReferenceCursor.getColumnIndexOrThrow("REF_TEXT"));
	        	String ref_link = mReferenceCursor.getString(
	        			mReferenceCursor.getColumnIndexOrThrow("REF_LINK"));
	        	String ref_doi = mReferenceCursor.getString(
	        			mReferenceCursor.getColumnIndexOrThrow("REF_DOI"));
	        	referenceName = "";
	        	if (!ref_txt.equals("null")){
	        		referenceName += ref_txt + " ";
	        	}
	        	if(!ref_link.equals("null")){
	        		referenceName += ref_link + " ";
	        	}
	        	if(!ref_doi.equals("null")){
	        		referenceName += ref_doi + " ";
	        	}
	        	
	        	ConRefs.append(Html.fromHtml(refNumber+ ":"+referenceName + "<br/>" ));
	        	refNumber++;
	        } while (mReferenceCursor.moveToNext());
        }
        if (mReferenceCursor.getCount()==0){
        	ConRefs.setVisibility(View.GONE);
        	getView().findViewById(R.id.ConReferenceheading).
        		setVisibility(View.GONE);
        }
    }
    
    
    /*
	 *Function for getting acknowledgements for that abstract and adding to the view 
	 */
    private void getAndUpdateAbstractAcknowledgements() {

        mAbstractDataCursor.moveToFirst();

        do {

            String acknowledgements = mAbstractDataCursor.getString(
            		mAbstractDataCursor.getColumnIndexOrThrow(
            				"ACKNOWLEDGEMENTS"));

            if (acknowledgements.length() > 0&&!acknowledgements.equals("null")) {
            	ConAck.setVisibility(View.VISIBLE);
            	if(acknowledgements.equals("null")){
            		ConAck.append("");
            	}else{
            		ConAck.append(acknowledgements + "\n" );
            	}
                
            }
            else{
            	ConAck.setVisibility(View.GONE);
            	getView().findViewById(R.id.ConAcknowledgeheading).setVisibility(View.GONE);
            	getView().findViewById(R.id.bar3).setVisibility(View.GONE);            	
            }

        } while (mAbstractDataCursor.moveToNext());
        
    }
    
    /*
	 * Function for getting abstract text & parsing of SortID and adding to the view 
	 */
    private void getAndUpdateAbstractContent() {

        mAbstractDataCursor.moveToFirst();
            do {
                String Text = mAbstractDataCursor.getString(mAbstractDataCursor
                		.getColumnIndexOrThrow("ABSRACT_TEXT"));
                Text = TextUtils.htmlEncode(Text);
                content.getSettings().setJavaScriptEnabled(true);
        		content.getSettings().setBuiltInZoomControls(false);
        		if (Text.contains("$")){
        		//if (true){
	        		content.loadDataWithBaseURL(
	        				"http://bar", "<script type='text/x-mathjax-config'>"
	        				+"MathJax.Hub.Config({ "
	        				+"showMathMenu: false, "
	        				+"jax: ['input/TeX','output/HTML-CSS'], "
	        				+"tex2jax: {inlineMath: [ ['$','$']],displayMath: [ ['$$','$$'] ],processEscapes: true},"
	        				+"extensions: ['tex2jax.js'], "
	        				+"TeX: { extensions: ['AMSmath.js','AMSsymbols.js',"
	        				+"'noErrors.js','noUndefined.js'] }, "
	        				+"});</script>"
	        				+"<script type='text/javascript' "
	        				+"src='file:///android_asset/MathJax/MathJax.js'"
	        				+"></script><span id='math'>"+Text+"</span>","text/html","UTF-8","");        	
	        		content.loadUrl("javascript:MathJax.Hub.Queue(['Typeset',MathJax.Hub]);");
        		}
        		else{
        			content.loadDataWithBaseURL("http://bar",Text,"text/html","UTF-8","");
        			
        		}
                

            } while (mAbstractDataCursor.moveToNext());
            
            //parsing SortID to extract group id & poster number and add it to abstract text body.
            mAbstractDataCursor.moveToFirst();
            int sortID = mAbstractDataCursor.getInt(mAbstractDataCursor
            		.getColumnIndexOrThrow("SORTID"));
            Log.i("GCA-SortID", "Sort ID: " + sortID);
            if(sortID != 0) {	
            	int groupid =  ((sortID & (0xFFFF << 16)) >> 16);
            	int poster_no = sortID & 0xFFFF;
            	Log.i("GCA-groupid", "groupid: " + groupid);
            	Log.i("GCA-posterno", "Poster Nr: " + poster_no);
            	absSortID.append("\r\nSort ID: " + sortID);
            	title.append("   (" + get_groupid_str(groupid));
            	title.append("" + poster_no+")");
            	absSortID.setVisibility(View.GONE);
            
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
    	Cursor absFiguresCursor = mDbHelper.fetchFiguresByAbsId(uuid);
    	if(absFiguresCursor.getCount() > 0) {
    		btnOpenAbstractFig.setVisibility(View.VISIBLE);
    		btnOpenAbstractFig.setText("Show Figures" + "  (" + absFiguresCursor
    				.getCount() + ")");
    		
    		btnOpenAbstractFig.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View arg0) {
					//if Internet is connected
					if(isNetworkAvailable()){
						
						//check if interent is WIFI
						ConnectivityManager connManager = 
								(ConnectivityManager) getActivity()
								.getSystemService(Context.CONNECTIVITY_SERVICE);
						NetworkInfo mWifi = connManager.getNetworkInfo(
								ConnectivityManager.TYPE_WIFI);
						NetworkInfo mMobile = connManager.getNetworkInfo(
								ConnectivityManager.TYPE_MOBILE);
						
						if (mWifi.isConnected()) {
							//Toast.makeText(getActivity(), "Connected via WLAN", Toast.LENGTH_SHORT).show();
							Intent figuresIntent = new Intent(getActivity(), 
									AbstractFiguresActivity.class);
							figuresIntent.putExtra("abs_uuid", uuid);
							startActivity(figuresIntent);
						
						} else if(mMobile.isConnected()) {
							//if connected with mobile data - 2G, 3G, 4G etc
							//Toast.makeText(getActivity(), "Connected via Mobile Internet", Toast.LENGTH_SHORT).show();
							
							AlertDialog.Builder builder = new AlertDialog
									.Builder(getActivity());
							builder.setTitle("Additional Traffic Warning")
							.setMessage("Downloading of Figures over Mobile Internet may create additional Traffic. Do you want to Continue ?")
							       .setPositiveButton("Continue", 
							    		   new DialogInterface.OnClickListener() {
							           public void onClick(DialogInterface dialog, int id) {
							               // if user Agrees to continue
							        	   Intent figuresIntent = new Intent(
							        			   getActivity(), 
							        			   AbstractFiguresActivity.class);
											figuresIntent.putExtra("abs_uuid", uuid);
											startActivity(figuresIntent);
							           }
							       })
							       .setNegativeButton("Cancel", 
							    		   new DialogInterface.OnClickListener() {
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
    		btnOpenAbstractFig.setVisibility(View.GONE);
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
            content.loadData("", "text/html","utf-8");
            ConRefs.setText("");
            afName.setText("");
            authors.setText("");
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


