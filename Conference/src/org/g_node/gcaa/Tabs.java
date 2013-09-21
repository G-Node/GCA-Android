/**
 * 
 */
package org.g_node.gcaa;

import org.g_node. schedule.ScheduleMainActivity;
import org.g_node. schedule.ScheduleSlideFragment;
import org.g_node.abstracts.AbstractActivity;
import org.g_node.location.LocationMarkers;

import com.yasiradnan.conference.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * @author Adnan
 *
 */
@SuppressWarnings("deprecation")
public class Tabs extends TabActivity {
/** Called when the activity is first created. */
	
	public void onCreate(Bundle savedInstanceState) {
		
	    super.onCreate(savedInstanceState);
	 
	    
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);
		
		setTabs() ;
	}
	private void setTabs()
	{
		addTab("Info", R.drawable.tab_home, GeneralActivity.class);
		addTab("Program", R.drawable.tab_search, ScheduleMainActivity.class);
		
		addTab("Abstracts", R.drawable.tab_home,AbstractActivity.class);
		addTab("Map", R.drawable.tab_map,LocationMarkers.class);
	}
	
	private void addTab(String labelId, int drawableId, Class<?> c)
	{
		TabHost tabHost = getTabHost();
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);	
		
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}

}
