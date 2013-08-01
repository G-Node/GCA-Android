
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
import com.yasiradnan.conference.AbsAffiliationName;
import com.yasiradnan.conference.AbsAffiliationNameDao;
import com.yasiradnan.conference.AbstractAffiliateName;
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

    AbstractCursorAdapter cursorAdapter;

    ListView listView;

    SQLiteDatabase database;

    DevOpenHelper helper;

    DaoSession daoSession;

    DaoMaster daoMaster;

    AbstractsItemDao itemsDao;
    
    AbstractAuthorDao authorDao;
    
    AbstractKeyWordsDao abKeyDao;
    
    AbsAffiliationNameDao abAfNameDao;
    
    AbstractAffiliationDao abAfDao;
    
    AbstractAffiliateNameDao abAffiliateDao;
    
    AuthorsAffiliateDao  abAuthAfDao;
    
    AuthorsAbstractDao  absAuthDao;

    Cursor cursor;

    ListView lv;
    
    String authorNames;
    
    String is_Corrospondence;
    
    String getAfNumber;

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
        
        abKeyDao = daoSession.getAbstractKeyWordsDao();
        
        abAfNameDao = daoSession.getAbsAffiliationNameDao();
        
        abAfDao = daoSession.getAbstractAffiliationDao();
        
        abAffiliateDao = daoSession.getAbstractAffiliateNameDao();
        
        abAuthAfDao = daoSession.getAuthorsAffiliateDao();
        
        absAuthDao = daoSession.getAuthorsAbstractDao();
        
        setContentView(R.layout.abstract_general);

        listView = (ListView)findViewById(R.id.list);
        
        String query = "select abstracts_item._id,title,text,type,name,topic from abstracts_item join abstract_author on abstract_author._id = abstracts_item._id";
        
        cursor = database.rawQuery(query, null);
        
        Boolean isEmpty;
        
        if(cursor!=null && cursor.getCount() > 0){
            
            isEmpty = false;
            
        }else{
            
            isEmpty = true;
            
        }
        
        if(isEmpty){
            datainList();
            cursor = database.rawQuery(query, null);
        }
        
        cursorAdapter = new AbstractCursorAdapter(this, cursor);

        listView.setAdapter(cursorAdapter);

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
                
                String refs = jsonObject.getString("refs");

                Log.e("title", title);

                String text = jsonObject.getString("abstract");

                AbstractsItem items = new AbstractsItem(null, correspondence, title, url, text, type, topic, coi, cite,refs);
                
                itemsDao.insert(items);
                
                
                JSONObject abAfData = jsonArray.getJSONObject(index).getJSONObject("affiliations");
                
                String af_name = abAfData.toString().replaceAll("\\{", "").replaceAll("\\}","");
                
                AbsAffiliationName abAfName = new AbsAffiliationName(null, af_name);
                
                abAfNameDao.insert(abAfName);
                
                
                JSONArray getKeywords = new JSONArray(jsonObject.getString("keywords"));
                
                String keywordsData = String.valueOf(getKeywords).replaceAll("\\[", "").replaceAll("\\]","").toString().replace("\"", "");
                
                AbstractKeyWords Keywords= new AbstractKeyWords(keywordsData, items.getId());
                
                abKeyDao.insert(Keywords);
                 
               
                JSONArray getAuthorsArray = new JSONArray(jsonObject.getString("authors"));

                for (int counter = 0; counter < getAuthorsArray.length(); counter++) {
                    
                    JSONObject authjsonObJecthor = getAuthorsArray.getJSONObject(counter);
                    
                    JSONArray getNumbers = new JSONArray(authjsonObJecthor.getString("affiliations"));
                    
                    authorNames = authjsonObJecthor.getString("name");
                    
                    is_Corrospondence = authjsonObJecthor.getString("corresponding");
                    
                    getAfNumber = getNumbers.toString().replaceAll("\\[", "").replaceAll("\\]","");
                    
                    AbstractAuthor absAuth = new AbstractAuthor(null, authorNames, is_Corrospondence);
                    authorDao.insert(absAuth);
                    
                    AbstractAffiliation ab_af = new AbstractAffiliation(null, getAfNumber);
                    abAfDao.insert(ab_af);
                    
                    AuthorsAbstract authAbstract = new AuthorsAbstract(items.getId(), absAuth.getId());
                    absAuthDao.insert(authAbstract);
                    
                    AuthorsAffiliate authAfNumber = new AuthorsAffiliate(absAuth.getId(), ab_af.getId());
                    abAuthAfDao.insert(authAfNumber);
                    
                    
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
