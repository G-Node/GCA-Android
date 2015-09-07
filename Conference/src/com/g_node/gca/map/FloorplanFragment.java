package com.g_node.gca.map;


import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebView.FindListener;

import com.g_node.bc15.R;

public class FloorplanFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.fragment_floorplan, container, false);
    	
    	int screen_width =  (int)(view.getResources().getDisplayMetrics()
    			.widthPixels/view.getResources().getDisplayMetrics()
    			.density);
    	WebView floorplansView = (WebView) view.findViewById(
    			                           R.id.floorplan_web_view);
    	String html_string = String.format(
    			"<H3>Floor Plan Neue Universität (ground floor)</H3>" +
    			"<img src=\"file:///android_res/drawable/bc15eg.jpg\" width=\"%d\"/>" +
    			"<H3>first floor</H3>" +
    			"<img src=\"file:///android_res/drawable/bc151og.jpg\" width=\"%d\"/>" +
    			"<H3>second floor</H3>" +
    			"<img src=\"file:///android_res/drawable/bc152og.jpg\" width=\"%d\"/>",
    			screen_width,screen_width,screen_width);
    	floorplansView.loadDataWithBaseURL("file:///android_asset/", 
    			html_string, "text/html", "utf-8", null);
    	floorplansView.getSettings().setBuiltInZoomControls(true);
    	floorplansView.getSettings().setDisplayZoomControls(false);
    	//floorplansView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        return view ;
    }
}

