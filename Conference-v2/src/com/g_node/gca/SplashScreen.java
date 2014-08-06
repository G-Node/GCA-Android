package com.g_node.gca;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import com.g_node.gca.schedule.ScheduleJSONParse;
import com.shumail.newsroom.R;

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
