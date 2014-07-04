package com.g_node.gca.abstracts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	
	static String value;

	public static String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		Log.i("GCA-Abs-Frag", "getItem of Adapter");
		
		switch (index) {
		case 0:
		{
			// Main Abstract Content Fragment
			Bundle bundle = new Bundle();
			bundle.putString("value", value);
			
			AbstractContentTabFragment y = new AbstractContentTabFragment();
			y.setArguments(bundle);
			return y;
		}	
		case 1:
			
			// Notes fragment
			Bundle bundle = new Bundle();
			bundle.putString("value", value);
			AbstractNotesFragment y = new AbstractNotesFragment();
			y.setArguments(bundle);
			return y;
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}
	
	@Override
	public int getItemPosition(Object object) {
	    return POSITION_NONE;
	}
	
	@Override
	public void notifyDataSetChanged () {
		Log.i("GCA-Abs-Frag", "notifyDataSetChanged called");
		super.notifyDataSetChanged();
	}

}
