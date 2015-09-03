package com.g_node.gca.map;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebView.FindListener;

import com.g_node.bc15.R;

public class FloorplanFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.fragment_floorplan, container, false);
    	WebView floorplansView = (WebView) view.findViewById(
    			                           R.id.floorplan_web_view);
    	floorplansView.loadDataWithBaseURL("file:///android_asset/", 
    			"<img src=\"file:///android_res/drawable/bc15eg.jpg\"/>" +
    			"<img src=\"file:///android_res/drawable/bc151og.jpg\"/>" +
    			"<img src=\"file:///android_res/drawable/bc152og.jpg\"/>",
    			"text/html", "utf-8", null);
    	floorplansView.getSettings().setBuiltInZoomControls(true);
    	floorplansView.getSettings().setDisplayZoomControls(false);
        return view ;
    }
}

