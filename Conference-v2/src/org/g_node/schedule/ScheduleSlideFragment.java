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

import java.util.List;
import org.g_node.gcaa.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ScheduleSlideFragment extends Fragment {

    final static String ARG_PAGE = "page";

    private static ViewPager pager;

    private static int pageNumber;

    final static int totalPages = ScheduleMainActivity.totalPages;

    private static List<ScheduleItem>[] ScheduleInformation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.schedule, container, false);
        /**
         * Set header date
         */
        ((TextView)rootView.findViewById(R.id.tvDay)).setText(ScheduleInformation[pageNumber]
                .get(pageNumber).getDate().toString());
        final ListView list = (ListView)rootView.findViewById(R.id.list);
        BinderData bindingData = new BinderData(this.getActivity(), ScheduleInformation[pageNumber]);
        list.setAdapter(bindingData);

        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                /*
                 * Event Type 0,3 & 2 don't contains any information
                 */
                if (ScheduleInformation[pager.getCurrentItem()].get(position).getItemType() == 0
                        || ScheduleInformation[pager.getCurrentItem()].get(position).getItemType() == 3
                        || ScheduleInformation[pager.getCurrentItem()].get(position).getItemType() == 2)
                    return;
                Intent intent = new Intent(ScheduleSlideFragment.this.getActivity(),
                        ContentExtended.class);
                intent.putExtra("title", ScheduleInformation[pager.getCurrentItem()].get(position)
                        .getTitle());
                intent.putExtra("content", ScheduleInformation[pager.getCurrentItem()]
                        .get(position).getContent());
                startActivity(intent);
            }
        });

        ImageButton ibLeft = (ImageButton)rootView.findViewById(R.id.ibLeft);
        if (pageNumber == 0)
            ibLeft.setVisibility(View.INVISIBLE);

        else
            ibLeft.setVisibility(View.VISIBLE);

        ibLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem() > 0)
                    pager.setCurrentItem(pager.getCurrentItem() - 1, true);
            }
        });

        ImageButton ibRight = (ImageButton)rootView.findViewById(R.id.ibRight);
        /*
         * Check if the page is the last page. If it is last, then right button
         * will be invisible
         */
        if (pageNumber + 1 == totalPages)
            ibRight.setVisibility(View.INVISIBLE);
        else
            ibRight.setVisibility(View.VISIBLE);

        ibRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem() < totalPages)
                    pager.setCurrentItem(pager.getCurrentItem() + 1, true);
            }
        });

        return rootView;
    }

    public static Fragment create(int position) {
        Fragment fragment = new ScheduleSlideFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, position);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment create(int position, ViewPager _pager, List<ScheduleItem>[] data) {
        pageNumber = position;
        pager = _pager;
        ScheduleInformation = data;
        Fragment fragment = new ScheduleSlideFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARG_PAGE);
    }

}
