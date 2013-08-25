
package com.yasiradnan.abstracts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.yasiradnan.conference.AbsAffiliationName;
import com.yasiradnan.conference.AbsAffiliationNameDao;
import com.yasiradnan.conference.AbstractAffiliateNameDao;
import com.yasiradnan.conference.AbstractAffiliation;
import com.yasiradnan.conference.AbstractAffiliationDao;
import com.yasiradnan.conference.AbstractAuthor;
import com.yasiradnan.conference.AbstractAuthorDao;
import com.yasiradnan.conference.AbstractKeyWords;
import com.yasiradnan.conference.AbstractKeyWordsDao;
import com.yasiradnan.conference.AbstractsItem;
import com.yasiradnan.conference.AbstractsItemDao;
import com.yasiradnan.conference.AuthorsAbstract;
import com.yasiradnan.conference.AuthorsAbstractDao;
import com.yasiradnan.conference.AuthorsAffiliate;
import com.yasiradnan.conference.AuthorsAffiliateDao;
import com.yasiradnan.conference.DaoMaster;
import com.yasiradnan.conference.DaoMaster.DevOpenHelper;
import com.yasiradnan.conference.DaoSession;
import com.yasiradnan.conference.R;
import com.yasiradnan.utils.JSONReader;

import de.greenrobot.dao.QueryBuilder;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
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
    
    Cursor cursor;

    ListView lv;

    String authorNames;

    String is_Corrospondence;

    String getAfNumber;
    
    DatabaseHelper dbHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        

        setContentView(R.layout.abstract_general);

        listView = (ListView)findViewById(R.id.list);
        
        dbHelper.open();
        
        String query = "select abstracts_item._id,title, type, topic, text,affiliation_number,af_name from abs_affiliation_name,abstract_affiliation,abstracts_item,abstract_author,authors_abstract where abstracts_item._id = authors_abstract.abstractsitem_id and abstract_author._id = authors_abstract.abstractauthor_id and abstract_affiliation._id = abstract_author._id and abs_affiliation_name._id = abstracts_item._id GROUP By abstracts_item._id";

        cursor = dbHelper.database.rawQuery(query, null);
        
        Boolean isEmpty;

        if (cursor != null && cursor.getCount() > 0) {

            isEmpty = false;

        } else {

            isEmpty = true;

        }

        if (isEmpty) {
            datainList();
            cursor = dbHelper.database.rawQuery(query, null);
        }

        cursorAdapter = new AbstractCursorAdapter(this, cursor);
        
        listView.setAdapter(cursorAdapter);
        
        listView.setTextFilterEnabled(true);
        
        


        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                // TODO Auto-generated method stub

                String Text = cursor.getString(cursor.getColumnIndexOrThrow("Text"));

                Intent in = new Intent(getApplicationContext(), AbstractContent.class);

                in.putExtra("abstracts", Text);

                startActivity(in);
            }
        });

        /*
         * Serach Filter
         */
        listView.setTextFilterEnabled(true);
        
        EditText searchOption = (EditText)findViewById(R.id.abSearch);
        
        searchOption.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                // TODO Auto-generated method stub
                ((CursorAdapter)AbstractActivity.this.cursorAdapter).getFilter().filter(cs);
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

                    Log.e("title", title);

                    String text = jsonObject.getString("abstract");

                    //itemsDao.insert(items);

                    JSONObject abAfData = jsonArray.getJSONObject(index).getJSONObject("affiliations");

                    String af_name = abAfData.toString().replaceAll("\\{", "").replaceAll("\\}", "");


                    //abAfNameDao.insert(abAfName);

                    JSONArray getKeywords = new JSONArray(jsonObject.getString("keywords"));

                    String keywordsData = String.valueOf(getKeywords).replaceAll("\\[", "")
                            .replaceAll("\\]", "").toString().replace("\"", "");


                    //abKeyDao.insert(Keywords);

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

                       
                        //authorDao.insert(absAuth);

                       
                        //abAfDao.insert(ab_af);

                      
                        //absAuthDao.insert(authAbstract);

                        
                        //abAuthAfDao.insert(authAfNumber);

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
