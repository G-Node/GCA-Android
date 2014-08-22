/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.newsroom;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.g_node.gcaa.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;


/*
 * Custom Adapter class that is responsible for holding the list of news after they
 * get parsed out of XML and building row views to display them on the screen.
 */
public class NewsAdapter extends ArrayAdapter<NewsItemSingle> {

	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	
	public NewsAdapter(Context ctx, int textViewResourceId, List<NewsItemSingle> sites) {
		super(ctx, textViewResourceId, sites);
		
		//Setup the ImageLoader, 
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ctx).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        
        //enabling caching for ImageLoader.
        options = new DisplayImageOptions.Builder()
		.cacheInMemory()
		.cacheOnDisc()
		.build();


	}
	
	
	/*
	 * (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 * 
	 * This method is responsible for creating row views out of a newslist object that can be put
	 * into our ListView
	 */
	@Override
	public View getView(int pos, View convertView, ViewGroup parent){
		RelativeLayout row = (RelativeLayout)convertView;
		Log.i("incf-rss", "getView pos = " + pos);
		if(null == row){
			//No recycled View, we have to inflate one.
			LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = (RelativeLayout)inflater.inflate(R.layout.row_site, null);
		}
		
		//Get our View References
		final ImageView iconImg = (ImageView)row.findViewById(R.id.iconImg);
		TextView nameTxt = (TextView)row.findViewById(R.id.nameTxt);
		TextView aboutTxt = (TextView)row.findViewById(R.id.aboutTxt);
		TextView dateText = (TextView) row.findViewById(R.id.dateTxt);
		
		final ProgressBar indicator = (ProgressBar)row.findViewById(R.id.progress);
		
		//Initially we want the progress indicator visible, and the image invisible
		indicator.setVisibility(View.VISIBLE);
		iconImg.setVisibility(View.INVISIBLE);

		//Setup a listener we can use to swtich from the loading indicator to the Image once it's ready
		ImageLoadingListener listener = new ImageLoadingListener(){



			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				indicator.setVisibility(View.INVISIBLE);
				iconImg.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		//Load the image and use our options so caching is handled.
		imageLoader.displayImage(getItem(pos).getImgUrl(), iconImg,options, listener);
		
		//Set the relavent text in our TextViews
		nameTxt.setText(getItem(pos).getName());
		aboutTxt.setText(getItem(pos).getAbout());
		
		DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy");

		String parsedPubDate = "";
		try {
			Date date = formatter.parse(getItem(pos).getPubDate());
			parsedPubDate = formatter.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dateText.setText(parsedPubDate);
		return row;
				
				
	}

}
