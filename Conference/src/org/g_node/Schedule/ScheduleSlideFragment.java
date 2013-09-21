/*
 * Copyright (c) 2013, Ivan Mylyanyk
 * License: BSD-2 (see LICENSE)
 * 
 * This code was modified with Author permission.
 * Modifications:
 * Added 3 ArrayList
 * Defined Pages = 3
 * Added JSON parsing function
 * Implement switch statement for showing page specific list
 * 
 * */

package org.g_node.schedule;

import java.io.Serializable;
import java.util.List;

import org.g_node.gcaa.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ScheduleSlideFragment extends Fragment {

    final static String ARG_DATA = "data";

    private ScheduleItem[] ScheduleInformation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.schedule, container, false);

        final ListView list = (ListView)rootView.findViewById(R.id.list);
        BinderData bindingData = new BinderData(this.getActivity(), ScheduleInformation);
        list.setAdapter(bindingData);

        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                ScheduleItem item = ScheduleInformation[position];
                /*
                 * item Type 0,2 & 3 don't have content
                 */
                if (item.getItemType() == 0 || item.getItemType() == 3 || item.getItemType() == 2)
                    return;

                Intent intent = new Intent(ScheduleSlideFragment.this.getActivity(),
                        ContentExtended.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("content", item.getContent());
                startActivity(intent);
            }
        });

        return rootView;
    }

    public static Fragment create(ScheduleItem[] data) {
        
        Fragment fragment = new ScheduleSlideFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("schedule", "onCreate()");
        super.onCreate(savedInstanceState);
        Serializable serializable = getArguments().getSerializable(ARG_DATA);
        ScheduleInformation = (ScheduleItem[]) serializable;
    }

    public static Fragment create(int position, ViewPager mPager, List<ScheduleItem>[] data) {
        // TODO Auto-generated method stub
        return null;
    }

}