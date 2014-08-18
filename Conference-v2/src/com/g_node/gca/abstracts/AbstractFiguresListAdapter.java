package com.g_node.gca.abstracts;

import java.util.ArrayList;
import java.util.List;

import com.shumail.newsroom.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AbstractFiguresListAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
    private List<AbstractFiguresClass> figuresList;
	
	public AbstractFiguresListAdapter(Context context, List<AbstractFiguresClass> figuresList){
		this.inflater = LayoutInflater.from(context);
		this.figuresList = figuresList;
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
		
		return arg1;
	}
	
}