package com.shumail.gca.abstracts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.shumail.gca.utils.JSONReader;
import org.json.JSONArray;
import org.json.JSONException;

import com.shumail.newsroom.R;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class Abstracts extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_abstracts);
		try {
			//get json file from raw 
			InputStream inStream = this.getResources().openRawResource(R.raw.abstracts);
			JSONArray jsonArray = JSONReader.parseStream(inStream);
		
		} catch (FileNotFoundException e) {
            Log.e("jsonFile", "file not found");
        } catch (IOException e) {
            Log.e("jsonFile", "ioerror");
        } catch (JSONException e) {
            e.printStackTrace();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.abstracts, menu);
		return true;
	}

}
