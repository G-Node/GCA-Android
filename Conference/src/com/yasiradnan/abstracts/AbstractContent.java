package com.yasiradnan.abstracts;

import java.util.Arrays;

import org.w3c.dom.Text;

import com.yasiradnan.conference.R;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AbstractContent extends Activity {
    TextView content;
    TextView title;
    TextView topic;
    TextView afName;
    ListView list;
    String getName;
    TextView authorNames;
    String affiliation_ID;
    RelativeLayout rel;
    //String[] newText;
    Cursor cursor;
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
            
            String value = getData.getString("value");
            
            String affiliationName = getData.getString("afName");
            
            Log.e("Value", value);
            
            String sqlQuery = "select abstract_author.NAME AS NAME ,ABSTRACT_AFFILIATION.AFFILIATION_NUMBER AS NUMBER "
                    + "from abstracts_item,abstract_author,authors_abstract,ABSTRACT_AFFILIATION "
                    + "where abstracts_item._id = authors_abstract.abstractsitem_id "
                    + "and abstract_author._id = authors_abstract.abstractauthor_id "
                    + "and ABSTRACT_AFFILIATION._id = authors_abstract.ABSTRACTAFFILIATION_ID "
                    + "and abstracts_item._id = " + value ;
            
            cursor = DatabaseHelper.database.rawQuery(sqlQuery, null);
            
            if (cursor !=null && cursor.moveToFirst()) {
                do {
                    getName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                    affiliation_ID = cursor.getString(cursor.getColumnIndexOrThrow("NUMBER"));
                    authorNames.append(Html.fromHtml("\n"+getName+"<sup><small>"+affiliation_ID+"</small></sup><br/>"));
                    authorNames.append("\n"); 

                } while (cursor.moveToNext());
            }
            
           
            
           title.setText(Title);
            
           title.setTypeface(null, Typeface.BOLD);
            
           topic.setText(Topic);
           
          // afName.setText(affiliationName.replace("\"", "").replace("," , " ").replace(":", ". "));
           /*afName.setText(affiliationName.replace("\",\"","\n").replace("\"", "").replace(":", ". "));
           Log.e("A", String.valueOf(affiliationName.split("\",\"")));*/
           
           String[] newAfName = affiliationName.split("\",\""); //it returns an array of strings
         
           for (int i=0; i<newAfName.length; i++){
               Log.e("", String.valueOf(newAfName.length));
               newAfName[i] = newAfName[i].replace("\"", "").replace(":", ". ");
               
           }
          
           Arrays.sort(newAfName);
         
           String FormattedText = "";
           for (String string : newAfName){
              
               FormattedText = FormattedText+string+"\n";
           }
           afName.setText((FormattedText));
            
            
            //((TextView)findViewById(R.id.text)).setText(Html.fromHtml("<sup><small>1</small></sup>"));
            
            //content.setText(abstracts);
            
            //content.setMovementMethod(new ScrollingMovementMethod());
            
        }
        
        private void initial_UI(){
            //content = (TextView)findViewById(R.id.absContent);
            title  = (TextView)findViewById(R.id.ConTitle);
            topic = (TextView)findViewById(R.id.ConTopic);
            list = (ListView)findViewById(R.id.list);
            authorNames = (TextView)findViewById(R.id.ConAuthor);
            afName = (TextView)findViewById(R.id.ConAfName);
            
        }
        
}
