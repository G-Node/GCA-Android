
package com.yasiradnan.abstracts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.yasiradnan.conference.R;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * @author Adnan
 */
public class AbstractActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.abstract_general);
        
        datainList();

        /*
         * Serach Filter
         */
        
        EditText searchOption = (EditText)findViewById(R.id.abSearch);
        
        searchOption.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                AbstractActivity.this.simpleAdapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void datainList() {
        try {
            if (listitems.isEmpty()) {
                BufferedReader jsonReader = new BufferedReader(new InputStreamReader(this
                        .getResources().openRawResource(R.raw.abstracts)));
                StringBuilder jsonBuilder = new StringBuilder();
                for (String line = null; (line = jsonReader.readLine()) != null;) {
                    jsonBuilder.append(line).append("\n");
                }

                JSONTokener tokener = new JSONTokener(jsonBuilder.toString());
                JSONArray jsonArray = new JSONArray(tokener);

                for (int index = 0; index < jsonArray.length(); index++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(index);

                    String topic = jsonObject.getString("topic");

                    Log.e("topic", topic);

                    String type = jsonObject.getString("type");

                    String title = jsonObject.getString("title");

                    String absData = jsonObject.getString("abstract");

                    

                }
            }
            simpleAdapter = new SimpleAdapter(this, listitems, R.layout.abstract_content, from,
                    new int[] {
                            R.id.abTopic, R.id.abType, R.id.abTitle, R.id.SubTitle
                    });
            setListAdapter(simpleAdapter);

            ListView listView = getListView();

            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    // TODO Auto-generated method stub
                    // Get Abstract Data
                    
                    String abstracts_content = listitems.get(position).get(KEY_ABSTRACT).toString();
                    
                    // Create a new Intent
                    
                    Intent in = new Intent(getApplicationContext(), AbstractContent.class);
                    
                    // Getting Abstract for another activity
                    
                    in.putExtra("abstracts", abstracts_content);
                    
                    // StratActivity
                    
                    startActivity(in);
                }
            });

        } catch (FileNotFoundException e) {
            Log.e("jsonFile", "file not found");
        } catch (IOException e) {
            Log.e("jsonFile", "ioerror");
        } catch (JSONException e) {
            Log.e("jsonFile", "error while parsing json");
        }
    }
}
