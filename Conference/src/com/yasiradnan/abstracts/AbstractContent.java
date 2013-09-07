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
    TextView emailField;
    TextView authorNames;
    TextView ConRefs;
    String affiliation_ID;
    String getName;
    Cursor cursor,cursorOne;
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
            
            String email = getData.getString("email");
            
            String refs = getData.getString("refs");
            
            Log.e("Value", value);
            
            String sqlQuery = "select authors_abstract.abstractauthor_id AS AUTH_ID,abstract_author.NAME AS NAME,abstract_author.IS__CORRESPONDING,ABSTRACT_AFFILIATION.AFFILIATION_NUMBER AS NUMBER "
                    + "from abstracts_item,abstract_author,authors_abstract,ABSTRACT_AFFILIATION "
                    + "where abstracts_item._id = authors_abstract.abstractsitem_id "
                    + "and abstract_author._id = authors_abstract.abstractauthor_id "
                    + "and ABSTRACT_AFFILIATION._id = authors_abstract.ABSTRACTAFFILIATION_ID "
                    + "and abstracts_item._id = " + value ;
            
            String sql = "select CORRESPONDING_AUTHOR_ID AS ID from ABSTRACT_AUTHOR_CORRESPONDENCE where abstractsItem_id = "+ value;
            
            cursor = DatabaseHelper.database.rawQuery(sqlQuery, null);
            cursorOne = DatabaseHelper.database.rawQuery(sql, null);  
            
            if ((cursor !=null && cursor.moveToFirst()) && (cursorOne !=null && cursorOne.moveToFirst())) {
                do {
                    
                    //String Corrosponding = cursor.getString(cursor.getColumnIndexOrThrow("IS__CORRESPONDING"));
                   /* getName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                    int getIndex = email.indexOf(",");
                    String getCorsName =email.substring(0, getIndex);
                    int newIndex = getCorsName.indexOf(".");
                    String getFormattedName = getCorsName.substring(newIndex+1,getCorsName.length());
                   
                    Log.e("QW", "");
                    Log.e("AAA", String.valueOf(getFormattedName.trim().equalsIgnoreCase(getName.trim())));
                   
                    if(getFormattedName.trim().equalsIgnoreCase(getName.trim())){
                        affiliation_ID = cursor.getString(cursor.getColumnIndexOrThrow("NUMBER"));
                        authorNames.append(Html.fromHtml("\n"+getName+"<sup><small>"+affiliation_ID+"*</small></sup><br/>"));
                        authorNames.append("\n"); 
                    }else{
                        affiliation_ID = cursor.getString(cursor.getColumnIndexOrThrow("NUMBER"));
                        authorNames.append(Html.fromHtml("\n"+getName+"<sup><small>"+affiliation_ID+"</small></sup><br/>"));
                        authorNames.append("\n"); 
                    }*/
                 String getID = cursor.getString(cursor.getColumnIndexOrThrow("AUTH_ID"));
                 Log.e("A", getID);
                 String Corr_AUTH_ID = cursorOne.getString(cursorOne.getColumnIndexOrThrow("ID"));
                 Log.e("B", Corr_AUTH_ID);
                 if(getID.trim().equalsIgnoreCase(Corr_AUTH_ID.trim())){
                     getName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                     affiliation_ID = cursor.getString(cursor.getColumnIndexOrThrow("NUMBER"));
                     authorNames.append(Html.fromHtml("\n"+getName+"<sup><small>"+affiliation_ID+"*</small></sup><br/>"));
                     authorNames.append("\n"); 
                 }else{
                     getName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                     affiliation_ID = cursor.getString(cursor.getColumnIndexOrThrow("NUMBER"));
                     authorNames.append(Html.fromHtml("\n"+getName+"<sup><small>"+affiliation_ID+"</small></sup><br/>"));
                     authorNames.append("\n"); 
                 }

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
           
           
           int index = email.lastIndexOf(",");
           
           String emailText = email.substring(index+1,email.length());
           
           emailField.setText("*"+emailText);
            
           
           content.setText(abstracts);
           
           Log.e("Length",String.valueOf(refs.length()));
           if(refs.length() > 0){
           ConRefs.setText("Reference\n"+refs);
           
           }
            
            
        }
        
        private void initial_UI(){
            content = (TextView)findViewById(R.id.Content);
            title  = (TextView)findViewById(R.id.ConTitle);
            topic = (TextView)findViewById(R.id.ConTopic);
            authorNames = (TextView)findViewById(R.id.ConAuthor);
            afName = (TextView)findViewById(R.id.ConAfName);
            emailField = (TextView)findViewById(R.id.email);
            ConRefs = (TextView)findViewById(R.id.Conrefs);
            
        }
        
}
