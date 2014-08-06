package com.g_node.gca;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.g_node.gca.schedule.DateWiseEventsRecord;
import com.g_node.gca.schedule.EventScheduleItem;
import com.g_node.gca.schedule.ScheduleItemRecord;
import com.g_node.gca.schedule.ScheduleJSONParse;
import com.g_node.gca.schedule.ScheduleMainActivity;
import com.g_node.gca.schedule.SessionScheduleItem;
import com.g_node.gca.schedule.TrackScheduleItem;
import com.shumail.newsroom.R;
import com.shumail.newsroom.R.layout;
import com.shumail.newsroom.R.menu;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.Window;

public class SplashScreen extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Splash screen timer
    	int SPLASH_TIME_OUT = Integer.parseInt(getResources().getString(R.string.splashTiming));
        
        //removing the action bar of splash screen
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
        
        Log.i("GCA-Performance", "before Schedule JSON Parsing and populating lists - Time: " + System.currentTimeMillis());
        
        ScheduleJSONParse getScheduleJSONandParse = new ScheduleJSONParse(this.getResources().openRawResource(R.raw.schedule));        
        
        getScheduleJSONandParse.getScheduleJSONData();
        getScheduleJSONandParse.groupEventsByDate();
        getScheduleJSONandParse.setScheduleData();
        
        Log.i("GCA-Performance", "After Schedule JSON Parsing and populating lists - Time: " + System.currentTimeMillis());
        
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
    }
    
}
