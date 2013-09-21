
package org.g_node.abstracts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.g_node.gcaa.R;
import org.g_node.utils.JSONReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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

    public static int cursorCount;

    DatabaseHelper dbHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abstract_general);
        listView = (ListView)findViewById(R.id.list);
        searchOption = (EditText)findViewById(R.id.abSearch);
        /*
         * Get Writable Database
         */
        dbHelper.open();
        /*
         * SQL Query to get data
         */
        String query = "select abstracts_item._id,CORRESPONDENCE,title, type, topic, text,af_name,REFS,ACKNOWLEDGEMENTS from abs_affiliation_name,abstract_affiliation,abstracts_item,abstract_author,authors_abstract where abstracts_item._id = authors_abstract.abstractsitem_id and abstract_author._id = authors_abstract.abstractauthor_id and abstract_affiliation._id = abstract_author._id and abs_affiliation_name._id = abstracts_item._id GROUP By abstracts_item._id";
        /*
         * Query execution
         */
        cursor = DatabaseHelper.database.rawQuery(query, null);
        /*
         * Get number of data to check whether database has any data or it's
         * empty
         */
        cursorCount = cursor.getCount();
        /*
         * Check If Database is empty.
         */
        if (cursorCount <= 0) {

            /*
             * Get data by parsing json file.
             */
            datainList();
            /*
             * Query execution
             */
            cursor = DatabaseHelper.database.rawQuery(query, null);
            /*
             * get number of cursor data
             */
            cursorCount = cursor.getCount();
        }

        cursorAdapter = new AbstractCursorAdapter(this, cursor);
        listView.setAdapter(cursorAdapter);
        listView.setTextFilterEnabled(true);
        listView.setFastScrollEnabled(true);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                // TODO Auto-generated method stub

                try {
                    cursor = (Cursor)cursorAdapter.getCursor();
                    /*
                     * Getting data from Cursor
                     */
                    String Text = cursor.getString(cursor.getColumnIndexOrThrow("TEXT"));
                    String Title = cursor.getString(cursor.getColumnIndexOrThrow("TITLE"));
                    String Topic = cursor.getString(cursor.getColumnIndexOrThrow("TOPIC"));
                    String value = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                    String afName = cursor.getString(cursor.getColumnIndexOrThrow("AF_NAME"));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow("CORRESPONDENCE"));
                    String refs = cursor.getString(cursor.getColumnIndexOrThrow("REFS"));
                    String acknowledgements = cursor.getString(cursor
                            .getColumnIndexOrThrow("ACKNOWLEDGEMENTS"));
                    Intent in = new Intent(getApplicationContext(), AbstractContent.class);
                    /*
                     * Passing data by Intent
                     */
                    in.putExtra("abstracts", Text);
                    in.putExtra("Title", Title);
                    in.putExtra("Topic", Topic);
                    in.putExtra("value", value);
                    in.putExtra("afName", afName);
                    in.putExtra("email", email);
                    in.putExtra("refs", refs);
                    in.putExtra("acknowledgements", acknowledgements);
                    startActivity(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /*
         * Searching filter to search data by Keywords, Title, Author Names
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
                /*
                 * Getting topic name
                 */
                String topic = jsonObject.getString("topic");
                /*
                 * Getting correspondence name
                 */
                String correspondence = jsonObject.getString("correspondence");
                /*
                 * Getting url
                 */
                String url = jsonObject.getString("url");
                /*
                 * Getting coi
                 */
                String coi = jsonObject.getString("coi");
                /*
                 * Getting cite
                 */
                String cite = jsonObject.getString("cite");
                /*
                 * Getting type
                 */
                String type = jsonObject.getString("type");
                /*
                 * Getting title
                 */
                String title = jsonObject.getString("title");
                /*
                 * Getting refs
                 */
                String refs = "";
                /*
                 * if refs has any data
                 */
                if (jsonObject.has("refs")) {
                    refs = jsonObject.getString("refs");
                }

                String acknowledgements = "";
                /*
                 * if acknowledgements has any data
                 */
                if (jsonObject.has("acknowledgements")) {
                    acknowledgements = jsonObject.getString("acknowledgements");
                }
                /*
                 * Get abstract
                 */
                String text = jsonObject.getString("abstract");
                /*
                 * Adding data in ABSTRACTS_ITEM table
                 */
                dbHelper.addItems(null, text, topic, correspondence, url, coi, cite, type, title,
                        refs, acknowledgements);

                JSONObject abAfData = jsonArray.getJSONObject(index).getJSONObject("affiliations");
                /*
                 * Removing { } from abAfData
                 */
                String af_name = abAfData.toString().replaceAll("\\{", "").replaceAll("\\}", "");
                /*
                 * Adding data in ABS_AFFILIATION_NAME
                 */
                dbHelper.addAbsAffiliation(af_name, null);
                JSONArray getKeywords = new JSONArray(jsonObject.getString("keywords"));
                /*
                 * Removing [ ] from keywords data
                 */
                String keywordsData = String.valueOf(getKeywords).replaceAll("\\[", "")
                        .replaceAll("\\]", "").toString().replace("\"", "");
                /*
                 * Adding data in ABSTRACT_KEY_WORDS
                 */
                dbHelper.addKeyWord(keywordsData, null);
                /*
                 * Getting data from Authors Array
                 */
                JSONArray getAuthorsArray = new JSONArray(jsonObject.getString("authors"));

                for (int counter = 0; counter < getAuthorsArray.length(); counter++) {

                    JSONObject authjsonObJecthor = getAuthorsArray.getJSONObject(counter);

                    JSONArray getNumbers = new JSONArray(
                            authjsonObJecthor.getString("affiliations"));

                    authorNames = authjsonObJecthor.getString("name");

                    String is_Corrospondence = "";
                    /*
                     * Check if is_Corrospondence = 'true'
                     */
                    if (authjsonObJecthor.has("corresponding")) {

                        is_Corrospondence = authjsonObJecthor.getString("corresponding");
                    }
                    getAfNumber = getNumbers.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                    /*
                     * Adding data in ABSTRACT_AFFILIATION table
                     */
                    dbHelper.addAbstractAffiliation(null, getAfNumber);
                    /*
                     * Check if Author Name already exist in database
                     */
                    if (!dbHelper.Exists(authorNames)) {
                        /*
                         * Adding data in ABSTRACT_AUTHOR table
                         */
                        dbHelper.addAuthors(null, authorNames, is_Corrospondence);
                        /*
                         * Adding associate Abstract Item id, Abstract Author id
                         * and Abstract Affiliation id in AUTHORS_ABSTRACT table
                         */
                        dbHelper.addAuthorsAbstractItems(dbHelper.items_id, dbHelper.authors_id,
                                dbHelper.abstract_affiliation_id);
                        /*
                         * Adding Author Affiliation id and associate Abstract
                         * Authors id in AUTHORS_AFFILIATE table
                         */
                        dbHelper.authorsAffiliation(dbHelper.abstract_affiliation_id,
                                dbHelper.authors_id);
                        /*
                         * Adding Corresponding Authors
                         */
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
