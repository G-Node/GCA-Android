/**
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * Copyright (c) 2013, Yasir Adnan <adnan.ayon@gmail.com> - (old version)
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.abstracts;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.shumail.newsroom.R;


public class AbstractContent extends FragmentActivity implements
	ActionBar.TabListener {
	
	String gtag = "GCA-Abs-Con";
	private String value;
	
	boolean isFav;
	MenuItem starG;
	
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	
	// Tab titles
	private String[] tabs = { "Abstract", "Notes" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstractcontent_viewpager);
        
        Bundle getData = getIntent().getExtras();		
		value = getData.getString("value");
        
        // Initilization
 		viewPager = (ViewPager) findViewById(R.id.pager);
 		actionBar = getActionBar();
 		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
 		
 		mAdapter.setValue(value);	//set current UUID for fragments
 		
 		viewPager.setAdapter(mAdapter);
 		actionBar.setHomeButtonEnabled(true);
 		actionBar.setDisplayHomeAsUpEnabled(true);
 		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		

 		// Adding Tabs
 		for (String tab_name : tabs) {
 			actionBar.addTab(actionBar.newTab().setText(tab_name)
 					.setTabListener(this));
 		}
 		
 		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}
	
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
	
			@Override
			public void onPageScrollStateChanged(int arg0) {
			
			}
		});
        
    }	//end onCreate
    
    
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
            	mAdapter.setValue(value);
            	mAdapter.notifyDataSetChanged();
            	
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
            	
            	mAdapter.setValue(value);
            	mAdapter.notifyDataSetChanged();
            	
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
    	
    }	//end onOptionsMenuItemSelected
    
    
    @Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(gtag, "AbstractContent - on Destroy");
    }

}
