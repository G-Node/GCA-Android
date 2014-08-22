/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com> (2014 Version)
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca;

import com.g_node.gca.abstracts.Abstracts;
import com.g_node.gca.abstracts.FavoriteAbstracts;
import com.g_node.gca.map.MapActivity;
import com.g_node.gca.newsroom.NewsRoomActivity;
import com.g_node.gca.schedule.ScheduleMainActivity;
import com.g_node.gcaa.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getActionBar().setTitle("G-Node Conference");
		//getActionBar().setIcon(getResources().getDrawable(R.drawable.icon_brain));
		
		Button news = (Button)findViewById(R.id.btn_news);
		news.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent newsIntent = new Intent(MainActivity.this, NewsRoomActivity.class);
				startActivity(newsIntent);
			}
		});
		
		Button absBtn = (Button) findViewById(R.id.btn_abstracts);
		absBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent abstractIntent = new Intent(MainActivity.this, Abstracts.class);
				startActivity(abstractIntent);
				
			}
		});
		
		Button mapBtn = (Button) findViewById(R.id.btn_maps);
		mapBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mapIntent = new Intent(MainActivity.this, MapActivity.class);
				startActivity(mapIntent);
				
			}
		});
		
		Button scheduleBtn = (Button) findViewById(R.id.btn_schedule);
		scheduleBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent scheduleIntent = new Intent(MainActivity.this, ScheduleMainActivity.class);
				startActivity(scheduleIntent);
				
			}
		});

		
		Button favBtn = (Button) findViewById(R.id.btn_favorites);
		favBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent favIntent = new Intent(MainActivity.this, FavoriteAbstracts.class);
				startActivity(favIntent);
				
			}
		});
		
		
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.genInfo:
            Intent genInfo = new Intent(this, GeneralActivity.class);
            this.startActivity(genInfo);
            return true;
		}
			
		return false;
		
	}

}
