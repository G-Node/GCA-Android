package com.yasiradnan.abstracts;

import java.util.Arrays;
import java.util.List;

import com.yasiradnan.conference.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AbstractAdapter extends BaseAdapter {
    Context context;
    private List<AbstractItem> items;
    
    public AbstractAdapter(Context context, List<AbstractItem> items) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.items = items;
    }
    
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return items.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        
        ViewHolder holder = null;
        
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        
        if(convertView == null){
            convertView = inflater.inflate(R.layout.abstract_content, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.abTitle);
            holder.topic = (TextView) convertView.findViewById(R.id.abTopic);
            holder.type = (TextView) convertView.findViewById(R.id.abType);
            holder.authors = (TextView)convertView.findViewById(R.id.SubTitle);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        
        AbstractItem data = (AbstractItem) getItem(position);
        
        holder.title.setText(data.getTitle());
        
        holder.topic.setText(data.getTopic());
        
        holder.type.setText(data.getType());
        
        holder.authors.setText(data.getAuthorName());
        
        
        return convertView;
    }
    
    private class ViewHolder{
        TextView title;
        TextView topic;
        TextView type;
        TextView authors;
    }

}
