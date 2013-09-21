/*
 * Copyright (c) 2013, Ivan Mylyanyk
 * License: BSD-2 (see LICENSE)
 * This code was modified with Author permission.
 * Modifications:
 * Added Scrollable info(TextView)
 * */
package org.g_node.schedule;

import org.g_node.gcaa.R;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Window;
import android.widget.TextView;

public class ContentExtended extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*Disable Top Action Bar*/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.content_extended);
		/*
		 * TexView for Showing Title
		 */
		TextView title = (TextView)findViewById(R.id.textView1);
		/*
		 * TextView for showing Information
		 */
		TextView info = (TextView)findViewById(R.id.textView2);
		Bundle bd = getIntent().getExtras();
		String Title = bd.getString("title");
		String Abstract = bd.getString("content");
		title.setText(Title);
		info.setText(Abstract);
		/*
		 * Scrolling in TextView
		 */
		info.setMovementMethod(new ScrollingMovementMethod());
	}
}
