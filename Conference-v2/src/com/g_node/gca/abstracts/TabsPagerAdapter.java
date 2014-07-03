package com.g_node.gca.abstracts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	
	String value;

	public String getValue() {
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

}
