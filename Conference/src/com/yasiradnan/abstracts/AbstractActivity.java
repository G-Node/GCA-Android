

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
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleAdapter;

/**
 * @author Adnan
 */
public class AbstractActivity extends ListActivity {
    
    static final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

    static final String[] from = new String[] {
            "topic", "type", "title", "type", "abstract"
    };

    public static final String KEY_TITLE = "title";

    public static final String KEY_TOPIC = "topic";

    public static final String KEY_ABSTRACT = "abstract";

    public static final String KEY_TYPE = "type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abstract_general);
        datainList();
    }

    private void datainList() {
        try {
           
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
                
                HashMap<String, String> temp = new HashMap<String, String>();

                temp.put(KEY_TOPIC, topic);
                temp.put(KEY_TYPE, type);
                temp.put(KEY_TITLE, title);
                temp.put(KEY_ABSTRACT, absData);

                list.add(temp);

            }
            SimpleAdapter sm = new SimpleAdapter(this, list, R.layout.abstract_content, from,
                    new int[] {
                            R.id.abTopic, R.id.abType, R.id.abTitle, R.id.SubTitle
                    });
            setListAdapter(sm);

        } catch (FileNotFoundException e) {
            Log.e("jsonFile", "file not found");
        } catch (IOException e) {
            Log.e("jsonFile", "ioerror");
        } catch (JSONException e) {
            Log.e("jsonFile", "error while parsing json");
        }
    }
}
