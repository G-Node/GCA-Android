/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.abstracts;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.g_node.gcaa.R;

public class AbstractFiguresListAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
    private List<AbstractFiguresClass> figuresList;
    
    ImageLoader imageLoader;
	DisplayImageOptions options;
	
	public AbstractFiguresListAdapter(Context context, List<AbstractFiguresClass> figuresList){
		this.inflater = LayoutInflater.from(context);
		this.figuresList = figuresList;
		
		//Setup the ImageLoader, 
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
        		.Builder(context).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        
        //enabling caching for ImageLoader.
        options = new DisplayImageOptions.Builder()
		.cacheInMemory()
		.cacheOnDisc()
		.build();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.figuresList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.figuresList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		//first inflate, then set image, caption etc
		arg1 = inflater.inflate(R.layout.abstract_figure_listview_row, null);
		
		AbstractFiguresClass currentFigure = figuresList.get(arg0);
		
		TextView figCaption = (TextView) arg1.findViewById(R.id.absListFigCaption);
		figCaption.setText(currentFigure.getCaption());
		
		final ProgressBar downloadingIndicator = (ProgressBar)arg1.findViewById(R.id.absListFigProgress);
		final ImageView figureImage = (ImageView)arg1.findViewById(R.id.absListFigImage);
		
		downloadingIndicator.setVisibility(View.VISIBLE);
		figureImage.setVisibility(View.INVISIBLE);
		
		ImageLoadingListener progressListener = new ImageLoadingListener(){

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				downloadingIndicator.setVisibility(View.GONE);
				figureImage.setVisibility(View.VISIBLE);
				
			}

			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		imageLoader.displayImage(currentFigure.getURL(), figureImage, options, progressListener);
		
		return arg1;
	}
	
}