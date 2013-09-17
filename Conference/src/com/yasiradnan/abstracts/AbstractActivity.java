
package com.yasiradnan.abstracts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yasiradnan.conference.R;
import com.yasiradnan.utils.JSONReader;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;

/**
 * @author Adnan
 */
public class AbstractActivity extends Activity {

    AbstractCursorAdapter cursorAdapter;

    ListView listView;

    EditText searchOption;

    Cursor cursor;

    ListView lv;

    String authorNames;

    String is_Corrospondence;

    String getAfNumber;

    String getAuthorID;

    DatabaseHelper dbHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abstract_general);

        listView = (ListView)findViewById(R.id.list);

        searchOption = (EditText)findViewById(R.id.abSearch);

        dbHelper.open();

        String query = "select abstracts_item._id,CORRESPONDENCE,title, type, topic, text,af_name,REFS,ACKNOWLEDGEMENTS from abs_affiliation_name,abstract_affiliation,abstracts_item,abstract_author,authors_abstract where abstracts_item._id = authors_abstract.abstractsitem_id and abstract_author._id = authors_abstract.abstractauthor_id and abstract_affiliation._id = abstract_author._id and abs_affiliation_name._id = abstracts_item._id GROUP By abstracts_item._id";

        cursor = DatabaseHelper.database.rawQuery(query, null);

        Log.e("Cursor Count", String.valueOf(cursor.getCount()));
        
        /*
         * Check If Database is empty
         */
        if (cursor.getCount() <= 0) {

            datainList();

            cursor = DatabaseHelper.database.rawQuery(query, null);
        }

        cursorAdapter = new AbstractCursorAdapter(this, cursor);

        listView.setAdapter(cursorAdapter);

        listView.setTextFilterEnabled(true);
        
        listView.setFastScrollEnabled(true);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                // TODO Auto-generated method stub

                try{
                cursor = (Cursor)cursorAdapter.getCursor();

                String Text = cursor.getString(cursor.getColumnIndexOrThrow("TEXT"));

                String Title = cursor.getString(cursor.getColumnIndexOrThrow("TITLE"));

                String Topic = cursor.getString(cursor.getColumnIndexOrThrow("TOPIC"));

                String value = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

               String afName = cursor.getString(cursor.getColumnIndexOrThrow("AF_NAME"));

               String email = cursor.getString(cursor.getColumnIndexOrThrow("CORRESPONDENCE"));

                String refs = cursor.getString(cursor.getColumnIndexOrThrow("REFS"));
                
               String acknowledgements = cursor.getString(cursor.getColumnIndexOrThrow("ACKNOWLEDGEMENTS"));

                Log.e("Position", String.valueOf(position));

                int itemNumber = cursor.getCount();

                Intent in = new Intent(getApplicationContext(), AbstractContent.class);

                in.putExtra("abstracts", Text);

                in.putExtra("Title", Title);

                in.putExtra("Topic", Topic);

                in.putExtra("value", value);

                in.putExtra("afName", afName);

                in.putExtra("email", email);

                in.putExtra("refs", refs);

                in.putExtra("itemNumber", itemNumber);
                
                in.putExtra("acknowledgements",acknowledgements);

                startActivity(in);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        /*
         * Serach Filter
         */
        cursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchDataByName(constraint.toString());
            }
        });

        searchOption.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                // TODO Auto-generated method stub
                AbstractActivity.this.cursorAdapter.getFilter().filter(cs);
                AbstractActivity.this.cursorAdapter.notifyDataSetChanged();

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

            InputStream inStream = this.getResources().openRawResource(R.raw.abstracts);
            JSONArray jsonArray = JSONReader.parseStream(inStream);

            for (int index = 0; index < jsonArray.length(); index++) {

                JSONObject jsonObject = jsonArray.getJSONObject(index);

                String topic = jsonObject.getString("topic");

                String correspondence = jsonObject.getString("correspondence");

                String url = jsonObject.getString("url");

                String coi = jsonObject.getString("coi");

                String cite = jsonObject.getString("cite");

                String type = jsonObject.getString("type");

                String title = jsonObject.getString("title");

                String refs = "";

                if (jsonObject.has("refs")) {

                    refs = jsonObject.getString("refs");
                }

                String acknowledgements = "";
                
                if(jsonObject.has("acknowledgements")){
                    acknowledgements = jsonObject.getString("acknowledgements");
                }

                String text = jsonObject.getString("abstract");

                dbHelper.addItems(null, text, topic, correspondence, url, coi, cite, type, title, refs, acknowledgements);

                JSONObject abAfData = jsonArray.getJSONObject(index).getJSONObject("affiliations");

                String af_name = abAfData.toString().replaceAll("\\{", "").replaceAll("\\}", "");

                dbHelper.addAbsAffiliation(af_name, null);

                JSONArray getKeywords = new JSONArray(jsonObject.getString("keywords"));

                String keywordsData = String.valueOf(getKeywords).replaceAll("\\[", "")
                        .replaceAll("\\]", "").toString().replace("\"", "");

                dbHelper.addKeyWord(keywordsData, null);

                JSONArray getAuthorsArray = new JSONArray(jsonObject.getString("authors"));

                for (int counter = 0; counter < getAuthorsArray.length(); counter++) {

                    JSONObject authjsonObJecthor = getAuthorsArray.getJSONObject(counter);

                    JSONArray getNumbers = new JSONArray(
                            authjsonObJecthor.getString("affiliations"));

                    authorNames = authjsonObJecthor.getString("name");

                    String is_Corrospondence = "";

                    if (authjsonObJecthor.has("corresponding")) {

                        is_Corrospondence = authjsonObJecthor.getString("corresponding");
                    }

                    getAfNumber = getNumbers.toString().replaceAll("\\[", "").replaceAll("\\]", "");

                    dbHelper.addAbstractAffiliation(null, getAfNumber);

                    if (!dbHelper.Exists(authorNames)) {

                        dbHelper.addAuthors(null, authorNames, is_Corrospondence);

                        dbHelper.addAuthorsAbstractItems(dbHelper.items_id, dbHelper.authors_id,
                                dbHelper.abstract_affiliation_id);

                        dbHelper.authorsAffiliation(dbHelper.abstract_affiliation_id,
                                dbHelper.authors_id);

                        if (is_Corrospondence.equalsIgnoreCase("True")) {
                            dbHelper.addCorrespondingAuthor(dbHelper.items_id, dbHelper.authors_id);
                        }

                    } else {
                        cursor = dbHelper.database.rawQuery(
                                "select _id from abstract_author where NAME like '%" + authorNames
                                        + "%'", null);
                        try {

                            if (cursor.moveToFirst()) {
                                getAuthorID = cursor.getString(0);
                            }
                        } finally {
                            cursor.close();
                        }

                        if (is_Corrospondence.equalsIgnoreCase("True")) {
                            dbHelper.addCorrespondingAuthor(dbHelper.items_id,
                                    Integer.parseInt(getAuthorID));
                        }
                        dbHelper.addAuthorsAbstractItems(dbHelper.items_id,
                                Integer.parseInt(getAuthorID), dbHelper.abstract_affiliation_id);
                        dbHelper.authorsAffiliation(dbHelper.abstract_affiliation_id,
                                Integer.parseInt(getAuthorID));

                    }
                }

            }

        } catch (FileNotFoundException e) {
            Log.e("jsonFile", "file not found");
        } catch (IOException e) {
            Log.e("jsonFile", "ioerror");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
