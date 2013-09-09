
package com.yasiradnan.abstracts;

import java.util.Arrays;

import org.w3c.dom.Text;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.yasiradnan.conference.R;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AbstractContent extends SherlockActivity {
    TextView content;

    TextView title;

    TextView topic;

    TextView afName;

    TextView emailField;

    TextView authorNames;

    TextView ConRefs;

    Button btn;

    String affiliation_ID;

    String getName;

    String value;

    String sqlQueryOne;

    String sqlQueryTwo;

    String sqlQueryThree;

    String affiliationName;

    Cursor cursor, cursorOne, cursorTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abstracts_show);
        initial_UI();

        /*
         * Getting abstracts form AbstractActivity
         */
        Bundle getData = getIntent().getExtras();

        String abstracts = getData.getString("abstracts");

        String Title = getData.getString("Title");

        String Topic = getData.getString("Topic");

        value = getData.getString("value");

        affiliationName = getData.getString("afName");

        String email = getData.getString("email");

        String refs = getData.getString("refs");

        Log.e("Value", value);

        sqlQueries();

        click();

        authorName();

        affiliationName();

        title.setText(Title);

        title.setTypeface(null, Typeface.BOLD);

        topic.setText(Topic);

        int index = email.lastIndexOf(",");

        String emailText = email.substring(index + 1, email.length());

        emailField.setText("*" + emailText);

        content.setText(abstracts);

        if (refs.length() > 0) {
            ConRefs.setText("Reference\n" + refs);

        }

    }

    private void initial_UI() {
        content = (TextView)findViewById(R.id.Content);
        title = (TextView)findViewById(R.id.ConTitle);
        topic = (TextView)findViewById(R.id.ConTopic);
        authorNames = (TextView)findViewById(R.id.ConAuthor);
        afName = (TextView)findViewById(R.id.ConAfName);
        emailField = (TextView)findViewById(R.id.email);
        ConRefs = (TextView)findViewById(R.id.Conrefs);
       // btn = (Button)findViewById(R.id.button1);

    }

    private void click() {
    /*    btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int Current = Integer.parseInt(value) + 1;
                value = String.valueOf(Current);
                resetAllFields();

                sqlQueries();

                getAbsTitle();

                getAbsTopic();

                getAbsEmail();

                authorName();

                getAfName();
                
                getContent();
                
                getRefs();
                

            }
        });*/
    }

    private void sqlQueries() {

        sqlQueryOne = "select authors_abstract.abstractauthor_id AS AUTH_ID,abstract_author.NAME AS NAME,abstract_author.IS__CORRESPONDING,ABSTRACT_AFFILIATION.AFFILIATION_NUMBER AS NUMBER "
                + "from abstracts_item,abstract_author,authors_abstract,ABSTRACT_AFFILIATION "
                + "where abstracts_item._id = authors_abstract.abstractsitem_id "
                + "and abstract_author._id = authors_abstract.abstractauthor_id "
                + "and ABSTRACT_AFFILIATION._id = authors_abstract.ABSTRACTAFFILIATION_ID "
                + "and abstracts_item._id = " + value;

        sqlQueryTwo = "select CORRESPONDING_AUTHOR_ID AS ID from ABSTRACT_AUTHOR_CORRESPONDENCE where abstractsItem_id = "
                + value;

        sqlQueryThree = "select abstracts_item._id AS ID,CORRESPONDENCE,title, type, topic, text,af_name as af,REFS "
                + "from abs_affiliation_name,abstract_affiliation,abstracts_item,abstract_author,authors_abstract "
                + "where ID = "
                + value
                + " and abstracts_item._id = authors_abstract.abstractsitem_id "
                + "and abstract_author._id = authors_abstract.abstractauthor_id "
                + "and abstract_affiliation._id = abstract_author._id "
                + "and abs_affiliation_name._id = abstracts_item._id GROUP By abstracts_item._id";

        cursor = DatabaseHelper.database.rawQuery(sqlQueryOne, null);
        cursorOne = DatabaseHelper.database.rawQuery(sqlQueryTwo, null);
        cursorTwo = DatabaseHelper.database.rawQuery(sqlQueryThree, null);
    }

    private void authorName() {
        if ((cursor != null && cursor.moveToFirst())
                && (cursorOne != null && cursorOne.moveToFirst())) {
            do {

                // String Corrosponding =
                // cursor.getString(cursor.getColumnIndexOrThrow("IS__CORRESPONDING"));
                /*
                 * getName =
                 * cursor.getString(cursor.getColumnIndexOrThrow("NAME")); int
                 * getIndex = email.indexOf(","); String getCorsName
                 * =email.substring(0, getIndex); int newIndex =
                 * getCorsName.indexOf("."); String getFormattedName =
                 * getCorsName.substring(newIndex+1,getCorsName.length());
                 * Log.e("QW", ""); Log.e("AAA",
                 * String.valueOf(getFormattedName.
                 * trim().equalsIgnoreCase(getName.trim())));
                 * if(getFormattedName.trim().equalsIgnoreCase(getName.trim())){
                 * affiliation_ID =
                 * cursor.getString(cursor.getColumnIndexOrThrow("NUMBER"));
                 * authorNames
                 * .append(Html.fromHtml("\n"+getName+"<sup><small>"+affiliation_ID
                 * +"*</small></sup><br/>")); authorNames.append("\n"); }else{
                 * affiliation_ID =
                 * cursor.getString(cursor.getColumnIndexOrThrow("NUMBER"));
                 * authorNames
                 * .append(Html.fromHtml("\n"+getName+"<sup><small>"+affiliation_ID
                 * +"</small></sup><br/>")); authorNames.append("\n"); }
                 */
                String getID = cursor.getString(cursor.getColumnIndexOrThrow("AUTH_ID"));
                Log.e("A", getID);
                String Corr_AUTH_ID = cursorOne.getString(cursorOne.getColumnIndexOrThrow("ID"));
                Log.e("B", Corr_AUTH_ID);
                if (getID.trim().equalsIgnoreCase(Corr_AUTH_ID.trim())) {
                    getName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                    affiliation_ID = cursor.getString(cursor.getColumnIndexOrThrow("NUMBER"));
                    authorNames.append(Html.fromHtml("\n" + getName + "<sup><small>"
                            + affiliation_ID + "*</small></sup><br/>"));
                    authorNames.append("\n");
                } else {
                    getName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                    affiliation_ID = cursor.getString(cursor.getColumnIndexOrThrow("NUMBER"));
                    authorNames.append(Html.fromHtml("\n" + getName + "<sup><small>"
                            + affiliation_ID + "</small></sup><br/>"));
                    authorNames.append("\n");
                }

            } while (cursor.moveToNext());
        }
    }

    private void affiliationName() {

        String[] newAfName = affiliationName.split("\",\"");

        for (int i = 0; i < newAfName.length; i++) {
            Log.e("", String.valueOf(newAfName.length));
            newAfName[i] = newAfName[i].replace("\"", "").replace(":", ". ");

        }

        Arrays.sort(newAfName);

        String FormattedText = "";
        for (String string : newAfName) {

            FormattedText = FormattedText + string + "\n";
        }
        afName.setText((FormattedText));

    }

    private void getAbsTitle() {
        
        cursorTwo.moveToFirst();
        
        do {

            String getTitle = cursorTwo.getString(cursorTwo.getColumnIndexOrThrow("TITLE"));
            title.setText(getTitle);

        } while (cursorTwo.moveToNext());
    }

    private void getAbsTopic() {

        cursorTwo.moveToFirst();
        do {

            String getTopic = cursorTwo.getString(cursorTwo.getColumnIndexOrThrow("TOPIC"));
            topic.setText(getTopic);

        } while (cursorTwo.moveToNext());

    }

    private void getAbsEmail() {

        cursorTwo.moveToFirst();
        do {

            String email = cursorTwo.getString(cursorTwo.getColumnIndexOrThrow("CORRESPONDENCE"));

            int index = email.lastIndexOf(",");

            String emailText = email.substring(index + 1, email.length());

            emailField.setText("*" + emailText);

        } while (cursorTwo.moveToNext());

    }
    
    private void getRefs(){
        
        cursorTwo.moveToFirst();
        
        do{
            
            String refs = cursorTwo.getString(cursorTwo.getColumnIndexOrThrow("REFS"));
            if (refs.length() > 0) {
                ConRefs.setText("Reference\n" + refs);

            }
            
        }while(cursorTwo.moveToNext());
        
        
     
    }
    
    private void getContent(){
        
        cursorTwo.moveToFirst();
        
        do{
            
            String Text = cursorTwo.getString(cursorTwo.getColumnIndexOrThrow("TEXT"));
            content.setText(Text);
            
        }while(cursorTwo.moveToNext());
    }
    
    
    private void getAfName(){
        
        cursorTwo.moveToFirst();
        
        do{
            
            affiliationName = cursorTwo.getString(6);
             
        }while(cursorTwo.moveToNext());
        
        
        affiliationName();
        
    }

    private void resetAllFields() {

        title.setText("");

        topic.setText("");

        content.setText("");

        ConRefs.setText("");

        emailField.setText("");

        afName.setText("");

        authorNames.setText("");

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.general, menu);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        return super.onOptionsItemSelected(item);
    }
}
