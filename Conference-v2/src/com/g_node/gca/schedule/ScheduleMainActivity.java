package com.g_node.gca.schedule;

import java.util.ArrayList;
import java.util.List;

import com.shumail.newsroom.R;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ScheduleMainActivity extends FragmentActivity implements ActionBar.TabListener {
	
	String LOG_TAG = "GCA-Schedule";
	
	public static List<ScheduleItemRecord> scheduleRecordsArray = new ArrayList<ScheduleItemRecord>() ;
	
	public static List<EventScheduleItem> eventsRecordsArray = new ArrayList<EventScheduleItem>() ;
	
	public static List<TrackScheduleItem> tracksRecordsArray = new ArrayList<TrackScheduleItem>() ;
	
	public static List<SessionScheduleItem> sessionRecordsArray = new ArrayList<SessionScheduleItem>() ;
	
	public static List<DateWiseEventsRecord> dateWiseEventsRecordList = new ArrayList<DateWiseEventsRecord>();
	
	private ActionBar actionBar;
	ViewPager viewPager;
	SchedulePagerAdapter mAdapter;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.i("GCA-Performance", "In Oncreate of ScheduleMain");
		Log.i("GCA-Performance", "in on Create - Time: " + System.currentTimeMillis());
		setContentView(R.layout.activity_schedule_main_viewpager);
		
		//ArrayLists are already populated during splash screen activity so let's move to initialization
		
		// Initilization
 		viewPager = (ViewPager) findViewById(R.id.schedulePager);
 		actionBar = getActionBar();
 		actionBar.setIcon(getResources().getDrawable(R.drawable.icon_schedule_actionbar));
 		mAdapter = new SchedulePagerAdapter(getSupportFragmentManager());
 		mAdapter.setLists(scheduleRecordsArray, eventsRecordsArray, tracksRecordsArray, sessionRecordsArray, dateWiseEventsRecordList);
 		viewPager.setAdapter(mAdapter);
 		actionBar.setHomeButtonEnabled(false);
 		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
 		for(int i=0; i<dateWiseEventsRecordList.size(); i++) {
 			DateWiseEventsRecord temp = dateWiseEventsRecordList.get(i);
 			actionBar.addTab(actionBar.newTab().setText(temp.getGroup_date()).setTabListener(this) );
 		}
 		
 		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				
				// TODO Auto-generated method stub
				Log.i(LOG_TAG, "at 100 of schedule activity");
//				actionBar.setSelectedNavigationItem(1);
				Log.i(LOG_TAG, "at 102 of schedule activity");
				//supportInvalidateOptionsMenu();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				actionBar.setSelectedNavigationItem(arg0);
			}
 			
 		});
 		
		
	}//end oncreate
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("GCA-Performance", "in on Resume of Schedule main");
		Log.i("GCA-Performance", "in on Resume - Time: " + System.currentTimeMillis());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.schedule_main, menu);
		return true;
	}


	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(arg0.getPosition());
		
	}


	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

}
