
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
import com.yasiradnan.conference.AbstractAuthor;
import com.yasiradnan.conference.AbstractAuthorDao;
import com.yasiradnan.conference.AbstractsItem;
import com.yasiradnan.conference.AbstractsItemDao;
import com.yasiradnan.conference.AuthorsAbstract;
import com.yasiradnan.conference.AuthorsAbstractDao;
import com.yasiradnan.conference.DaoMaster;
import com.yasiradnan.conference.DaoMaster.DevOpenHelper;
import com.yasiradnan.conference.DaoSession;
import com.yasiradnan.conference.R;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import de.greenrobot.*;

/**
 * @author Adnan
 */
public class AbstractActivity extends Activity {

    private List<AbstractItem> addData = new ArrayList<AbstractItem>();

    AbstractCursorAdapter cursorAdapter;

    ListView listView;

    SQLiteDatabase database;

    DevOpenHelper helper;

    DaoSession daoSession;

    DaoMaster daoMaster;

    AbstractsItemDao itemsDao;
    
    AbstractAuthorDao authorDao;
    
    AuthorsAbstractDao authAbstractsDao;

    Cursor cursor;

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        helper = new DaoMaster.DevOpenHelper(this, "Abstract-Database", null);

        database = helper.getWritableDatabase();

        daoMaster = new DaoMaster(database);

        daoSession = daoMaster.newSession();

        itemsDao = daoSession.getAbstractsItemDao();
        
        authorDao = daoSession.getAbstractAuthorDao();
        
        authAbstractsDao = daoSession.getAuthorsAbstractDao();
        

        setContentView(R.layout.abstract_general);

        datainList();

        listView = (ListView)findViewById(R.id.list);

        cursor = database.query(itemsDao.getTablename(), itemsDao.getAllColumns(), null, null, null, null, null);

        cursorAdapter = new AbstractCursorAdapter(this, cursor);

        listView.setAdapter(cursorAdapter);

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

                String correspondence = jsonObject.getString("correspondence");

                String url = jsonObject.getString("url");

                String coi = jsonObject.getString("coi");

                String cite = jsonObject.getString("cite");

                String type = jsonObject.getString("type");

                String title = jsonObject.getString("title");

                Log.e("title", title);

                String text = jsonObject.getString("abstract");

                AbstractsItem items = new AbstractsItem(null, correspondence, title, url, text, type, topic, coi, cite);
                
                itemsDao.insert(items);

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
                
                AbstractAuthor authorInfo = new AbstractAuthor(null, formattedString);
                authorDao.insert(authorInfo);
                
                AuthorsAbstract authAbs = new AuthorsAbstract(items.getId(), authorInfo.getId());
                authAbstractsDao.insert(authAbs);
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
