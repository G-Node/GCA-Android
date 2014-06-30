package com.g_node.gca;

import com.g_node.gca.abstracts.Abstracts;
import com.g_node.gca.abstracts.FavoriteAbstracts;
import com.g_node.gca.map.MapActivity;
import com.g_node.gca.newsroom.NewsRoomActivity;
import com.shumail.newsroom.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button news = (Button)findViewById(R.id.button1);
		news.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent newsIntent = new Intent(MainActivity.this, NewsRoomActivity.class);
				startActivity(newsIntent);
			}
		});
		
		Button absBtn = (Button) findViewById(R.id.button2);
		absBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent abstractIntent = new Intent(MainActivity.this, Abstracts.class);
				startActivity(abstractIntent);
				
			}
		});
		
		Button favBtn = (Button) findViewById(R.id.button3);
		favBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent favIntent = new Intent(MainActivity.this, FavoriteAbstracts.class);
				startActivity(favIntent);
				
			}
		});
		
		Button mapBtn = (Button) findViewById(R.id.button4);
		mapBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mapIntent = new Intent(MainActivity.this, MapActivity.class);
				startActivity(mapIntent);
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
