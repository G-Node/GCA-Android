/*
 * Copyright (c) 2013, Ivan Mylyanyk
 * License: BSD-2 (see LICENSE)
 * 
 * */
package com.yasiradnan.Schedule;

import com.yasiradnan.conference.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class ContentExtended extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_extended);
		TextView title = (TextView)findViewById(R.id.textView1);
		TextView info = (TextView)findViewById(R.id.textView2);
		Bundle bd = getIntent().getExtras();
		String Title = bd.getString("title");
		String Abstract = bd.getString("content");
		
		title.setText(Title);
		info.setText(Abstract);
		info.setMovementMethod(new ScrollingMovementMethod());
	}
}
