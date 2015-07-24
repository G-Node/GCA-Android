/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com> (2014 Version)
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import com.g_node.gca.schedule.ScheduleJSONParse;
import com.g_node.gcaa.R;

public class SplashScreen extends Activity {
	
	int SPLASH_TIME_OUT;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Splash screen timer
    	SPLASH_TIME_OUT = Integer.parseInt(getResources().getString(R.string.splashTiming));
        
        //removing the action bar of splash screen
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
        
        //start separate thread for parsing Schedule JSON
        PrepareScheduleJSONTask scheduleJSONParseAsyncTask = new PrepareScheduleJSONTask();
        scheduleJSONParseAsyncTask.execute();
        
    
    } //end onCreate
 
    
private class PrepareScheduleJSONTask extends AsyncTask<Void, Void, Void>{
		
		
		@Override
		protected void onPreExecute()
	    {

	    }
		
		@Override
		protected Void doInBackground(Void... arg0) {
			
			Log.i("GCA-Performance", "before Schedule JSON Parsing and populating lists - Time: " + System.currentTimeMillis());
	        
	        ScheduleJSONParse getScheduleJSONandParse = new ScheduleJSONParse(SplashScreen.this.getResources().openRawResource(R.raw.schedule));        
	        
	        getScheduleJSONandParse.getScheduleJSONData();
	        getScheduleJSONandParse.groupEventsByDate();
	        getScheduleJSONandParse.setScheduleData();
	        
	        Log.i("GCA-Performance", "After Schedule JSON Parsing and populating lists - Time: " + System.currentTimeMillis());
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result){
			
			new Handler().postDelayed(new Runnable() {
	        	//Handler for for showing splash screen for a little time
	        	
	            @Override
	            public void run() {

	            	//Start activity Tabs
	                Intent mainActivity = new Intent(SplashScreen.this, MainActivity.class);
	                startActivity(mainActivity);
	                
	                // close this activity
	                finish();
	            }
	        }, SPLASH_TIME_OUT);
			
		} //end OnPostExecute
	} //end AsyncTask class
    
} //end main class
