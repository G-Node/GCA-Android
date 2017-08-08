/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com> (2014 Version)
 * Copyright (c) 2013, Yasir Adnan <adnan.ayon@gmail.com> 
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.petebevin.markdown.MarkdownProcessor;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.webkit.WebView;

import com.g_node.bc17.R;

public class GeneralActivity extends Activity {
    WebView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        /*
         * initializing UI
         */
        initialUI();
        /*
         * Read file
         */
        String raw = readRawTextFile(this, R.raw.information);
        MarkdownProcessor markDownPro = new MarkdownProcessor();
        /*
         * Get Content as MarkDown
         */
        String getMarkDown = markDownPro.markdown(raw);
        /*
         * Set Content in TextView
         */
        text.loadDataWithBaseURL(null, getMarkDown, "text/html", "utf-8", null);

    }

    private void initialUI() {
        text = (WebView)findViewById(R.id.text);
    }

    public static String readRawTextFile(Context ctx, int resId) {
        InputStream inputStream = ctx.getResources().openRawResource(resId);
        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while ((line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        return text.toString();
    }
    
    @Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int theId = item.getItemId();
        if (theId == android.R.id.home) {
            finish();
        }
        return true;
		
	}

}
