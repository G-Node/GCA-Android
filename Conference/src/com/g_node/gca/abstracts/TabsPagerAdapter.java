/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * License: BSD-3 (See LICENSE)
 */
package com.g_node.gca.abstracts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	
	static String uuid;

	public static String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		TabsPagerAdapter.uuid = uuid;
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
			bundle.putString("value", uuid);
			
			AbstractContentTabFragment y = new AbstractContentTabFragment();
			y.setArguments(bundle);
			return y;
		}	
		case 1:
			
			// Notes fragment
			Bundle bundle = new Bundle();
			bundle.putString("value", uuid);
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
