/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com> (2014 Version)
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.abstracts;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.g_node.gcaa.R;

public class AbstractFiguresActivity extends Activity {
	
	AbstractFiguresListAdapter adapter;
	
	private final DatabaseHelper mDbHelper = DatabaseHelper
			.getInstance(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_abstract_figures);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		String uuid = getIntent().getExtras().getString("abs_uuid");
		
    	Cursor absFiguresCursor = mDbHelper.fetchFiguresByAbsId(uuid);
		absFiguresCursor.moveToFirst();		
		List<AbstractFiguresClass> FiguresList = new ArrayList<AbstractFiguresClass>();		
		do{
			AbstractFiguresClass currentFigure = new AbstractFiguresClass();
			currentFigure.setFig_uuid(absFiguresCursor.getString(
					absFiguresCursor.getColumnIndexOrThrow("FIG_UUID")));
			currentFigure.setCaption(absFiguresCursor.getString(
					absFiguresCursor.getColumnIndexOrThrow("FIG_CAPTION")));
			currentFigure.setURL(absFiguresCursor.getString(
					absFiguresCursor.getColumnIndexOrThrow("FIG_URL")));
			currentFigure.setPosition(absFiguresCursor.getString(
					absFiguresCursor.getColumnIndexOrThrow("FIG_POSITION")));			
			FiguresList.add(currentFigure);
		}while(absFiguresCursor.moveToNext());
		
		//set listview of images
		ListView figuresList = (ListView) findViewById(R.id.absFiguresList);
		adapter = new AbstractFiguresListAdapter(this, FiguresList);
		figuresList.setAdapter(adapter);
			
	} //end onCreate

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int theId = item.getItemId();
        if (theId == android.R.id.home) {
            finish();
        }
        return true;
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.adapter.imageLoader.destroy();
	}	
	
	
	
}
