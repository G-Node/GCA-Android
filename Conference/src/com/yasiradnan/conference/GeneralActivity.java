package com.yasiradnan.conference;

import com.petebevin.markdown.MarkdownProcessor;

import android.app.Activity;
import android.os.Bundle;

public class GeneralActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_general);
		MarkdownProcessor markDownPro = new MarkdownProcessor();
	}

}
