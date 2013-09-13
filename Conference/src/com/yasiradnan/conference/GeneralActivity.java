package com.yasiradnan.conference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



import com.petebevin.markdown.MarkdownProcessor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.widget.TextView;

public class GeneralActivity extends Activity {
	TextView text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_general);
		/*initial UI*/
		initialUI();
		
		/*Read file*/
		String raw=readRawTextFile(this, R.raw.information);
		
		/**/
		MarkdownProcessor markDownPro = new MarkdownProcessor();
		
		/**/
		String getMarkDown = markDownPro.markdown(raw);
		
		/**/
		CharSequence cs = Html.fromHtml(getMarkDown);
		
		/**/
		text.setText(cs);
		
	}
	
	private void initialUI(){
		text = (TextView)findViewById(R.id.text);
	}
	
	/**/
	
	public static String readRawTextFile(Context ctx, int resId) {
	    InputStream inputStream=ctx.getResources().openRawResource(resId);
	    InputStreamReader inputreader=new InputStreamReader(inputStream);
	    BufferedReader buffreader=new BufferedReader(inputreader);
	    String line;
	    StringBuilder text=new StringBuilder();

	    try {
	      while ((line=buffreader.readLine())!=null) {
	        text.append(line);
	        text.append('\n');
	      }
	    }
	    catch (IOException e) {
	      return null;
	    }
	    return text.toString();
	  }
	
}
