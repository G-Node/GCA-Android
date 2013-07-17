
package com.yasiradnan.abstracts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.android.gms.internal.da;
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

    private List<AbstractItem> addData = new ArrayList<AbstractItem>();

    AbstractAdapter abAdapter;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.abstract_general);

        datainList();

        listView = (ListView)findViewById(R.id.list);

        abAdapter = new AbstractAdapter(this, addData);

        listView.setAdapter(abAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                // TODO Auto-generated method stub

                String abstracts_content = addData.get(position).getAbstractContent();

                Intent in = new Intent(getApplicationContext(), AbstractContent.class);

                in.putExtra("abstracts", abstracts_content);

                startActivity(in);
            }
        });

        /*
         * Serach Filter
         */

        /*
         * EditText searchOption = (EditText)findViewById(R.id.abSearch);
         * searchOption.addTextChangedListener(new TextWatcher() {
         * @Override public void onTextChanged(CharSequence s, int start, int
         * before, int count) { // TODO Auto-generated method stub
         * AbstractActivity.this.abAdapter.getFilter().filter(s); }
         * @Override public void beforeTextChanged(CharSequence s, int start,
         * int count, int after) { // TODO Auto-generated method stub }
         * @Override public void afterTextChanged(Editable s) { // TODO
         * Auto-generated method stub } });
         */

    }

    private void datainList() {
        try {
            if (addData.isEmpty()) {
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

                    String type = jsonObject.getString("type");

                    String title = jsonObject.getString("title");

                    Log.e("title", title);

                    String absData = jsonObject.getString("abstract");

                    JSONArray getAuthorsArray = new JSONArray(jsonObject.getString("authors"));

                    String[] authorNames = new String[getAuthorsArray.length()];

                    for (int counter = 0; counter < getAuthorsArray.length(); counter++) {

                        authorNames[counter] = getAuthorsArray.getJSONObject(counter).getString(
                                "name");
                        authorNames[counter] = String.valueOf(authorNames[counter].replaceAll(
                                "^(\\w)\\w+", "$1."));

                    }

                    StringBuilder stringBuild = new StringBuilder();

                    for (int value = 0; value < authorNames.length; value++) {

                        stringBuild.append(authorNames[value]);

                        if (value < authorNames.length - 2) {
                            stringBuild.append(",");
                        } else if (value < authorNames.length - 1) {
                            stringBuild.append(" & ");
                        }
                    }

                    String formattedString = stringBuild.toString();

                    addData.add(new AbstractItem(title, topic, absData, type, formattedString));

                }
            }

        } catch (FileNotFoundException e) {
            Log.e("jsonFile", "file not found");
        } catch (IOException e) {
            Log.e("jsonFile", "ioerror");
        } catch (JSONException e) {
            Log.e("jsonFile", "error while parsing json");
        }
    }
}
