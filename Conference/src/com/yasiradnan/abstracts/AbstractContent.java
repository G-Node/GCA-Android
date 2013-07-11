package com.yasiradnan.abstracts;

import com.yasiradnan.conference.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class AbstractContent extends Activity {
    TextView content;
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
            
            content.setText(abstracts);
            
            content.setMovementMethod(new ScrollingMovementMethod());
            
        }
        
        private void initial_UI(){
            content = (TextView)findViewById(R.id.absContent);
        }
        
}
