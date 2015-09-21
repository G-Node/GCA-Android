/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com> (2014 Version)
 * Copyright (c) 2013, Yasir Adnan <adnan.ayon@gmail.com> 
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

import com.g_node.gcaa.R;


public class AbstractContent extends FragmentActivity implements
	ActionBar.TabListener {
	
	String gtag = "GCA-Abs-Con";
	private String uuid;
	
	boolean isFav;
	MenuItem starG;
	
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	
	// Tab titles
	private String[] tabs = { "Abstract", "Notes" };
	
	final DatabaseHelper mDbHelper = DatabaseHelper.getInstance(this);
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstractcontent_viewpager);
        
        Bundle getData = getIntent().getExtras();		
		uuid = getData.getString("value");
        
        // Initilization
 		viewPager = (ViewPager) findViewById(R.id.pager);
 		actionBar = getActionBar();
 		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
 		
 		mAdapter.setUuid(uuid);	//set current UUID for fragments
 		
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
        
        if(mDbHelper.isFavorite(uuid) ){
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
				mDbHelper.deleteFromABSTRACT_FAVORITES(uuid);
				Toast.makeText(getApplicationContext(), "Removed from Favorites",
	                    Toast.LENGTH_SHORT).show();
	            starG.setIcon(R.drawable.ic_action_important);
	            isFav = false;
	        	
	        }else{
	        	Log.i(gtag, "in else of isFAV");
	        	mDbHelper.addInABSTRACT_FAVORITES(uuid);
	        	Toast.makeText(getApplicationContext(), "Added to Favorites",
	                    Toast.LENGTH_SHORT).show();
	        	starG.setIcon(R.drawable.ic_action_important_selected);
	        	isFav = true;
	        }
			
        	break;
        	
        case R.id.next:
        {
            Cursor abstractCursor = mDbHelper.fetchAbtractDetailsByUUID(uuid);
            abstractCursor.moveToFirst();
            int currentSORTID = abstractCursor.getInt(abstractCursor
            		.getColumnIndex("SORTID"));
            abstractCursor.close();
            Cursor nextAbstracts = mDbHelper.fetchNextAbtractsDetails(
            								 currentSORTID);
            if (nextAbstracts.getCount()>0) {
            	nextAbstracts.moveToFirst();
            	uuid = nextAbstracts.getString(
            			nextAbstracts.getColumnIndexOrThrow("UUID"));
            	nextAbstracts.close();
            	mAdapter.setUuid(uuid);
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
            Cursor abstractCursor = mDbHelper.fetchAbtractDetailsByUUID(uuid);
            abstractCursor.moveToFirst();
            int currentSORTID = abstractCursor.getInt(abstractCursor
            		.getColumnIndex("SORTID"));
            abstractCursor.close();
            Cursor prevAbstracts = mDbHelper.fetchPreviousAbtractsDetails(
            								 currentSORTID);
            if (prevAbstracts.getCount()>0) {
            	prevAbstracts.moveToFirst();
            	uuid = prevAbstracts.getString(
            			prevAbstracts.getColumnIndexOrThrow("UUID"));
            	prevAbstracts.close();
            	mAdapter.setUuid(uuid);
            	mAdapter.notifyDataSetChanged();            	
            	invalidateOptionsMenu();
            	
            } else {
                Toast.makeText(getApplicationContext(), "No more Abstracts Left",
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
