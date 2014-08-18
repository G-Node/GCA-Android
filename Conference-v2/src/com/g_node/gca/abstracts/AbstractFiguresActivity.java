package com.g_node.gca.abstracts;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.shumail.newsroom.R;

public class AbstractFiguresActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_abstract_figures);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		String abs_uuid = getIntent().getExtras().getString("abs_uuid");
		
		String getFiguresQuery = "SELECT * FROM ABSTRACT_FIGURES WHERE ABSTRACT_UUID = '" + abs_uuid +"';";
    	Cursor absFiguresCursor = DatabaseHelper.database.rawQuery(getFiguresQuery, null);
		absFiguresCursor.moveToFirst();
		
		List<AbstractFiguresClass> FiguresList = new ArrayList<AbstractFiguresClass>();
		
		do{
			AbstractFiguresClass currentFigure = new AbstractFiguresClass();
			currentFigure.setFig_uuid(absFiguresCursor.getString(absFiguresCursor.getColumnIndexOrThrow("FIG_UUID")));
			currentFigure.setCaption(absFiguresCursor.getString(absFiguresCursor.getColumnIndexOrThrow("FIG_CAPTION")));
			currentFigure.setURL(absFiguresCursor.getString(absFiguresCursor.getColumnIndexOrThrow("FIG_URL")));
			currentFigure.setPosition(absFiguresCursor.getString(absFiguresCursor.getColumnIndexOrThrow("FIG_POSITION")));
			
			FiguresList.add(currentFigure);
		}while(absFiguresCursor.moveToNext());
		
		TextView x = (TextView) findViewById(R.id.absFiguresText);
		x.setText("");
		for(int i=0; i<FiguresList.size(); i++){
			x.append("\r\n" + FiguresList.get(i).getURL() + "\r\n");
			x.append("\r\n" + FiguresList.get(i).getCaption() + "----" + FiguresList.get(i).getFig_uuid() +  "\r\n");
		}
		
		
	} //end onCreate

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int theId = item.getItemId();
        if (theId == android.R.id.home) {
            finish();
        }
        return true;
		
	}	

}
