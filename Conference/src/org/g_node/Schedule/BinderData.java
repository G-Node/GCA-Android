/*
 * Copyright (c) 2013, Ivan Mylyanyk
 * License: BSD-2 (see LICENSE)
 * Added Switch cases as Integer
 * */

package org.g_node.schedule;

import org.g_node.gcaa.R;
import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BinderData extends BaseAdapter {

    private LayoutInflater inflater;

    private ScheduleItem[] items;

    public BinderData(Activity act, ScheduleItem[] _items) {
        inflater = (LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        items = _items;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.length;
    }

    @Override
    public Object getItem(int arg0) {
        return items[arg0].getTime();
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getItemViewType(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        View vi = arg1;
        ViewHolder holder = null;
        ScheduleItem item = items[arg0];

        switch (item.getItemType()) {
            case 1:
                /*
                 * Item Type 1 Event with Content
                 */
                vi = inflater.inflate(R.layout.list_event_with_content, null);
                holder = new ViewHolder();
                holder.tvTitle = (TextView)vi.findViewById(R.id.tvTitle);
                holder.tvTime = (TextView)vi.findViewById(R.id.tvTime);
                vi.setTag(holder);
                break;
            case 2:
                /*
                 * Event Without Content
                 */
                vi = inflater.inflate(R.layout.list_event_without_content, null);
                holder = new ViewHolder();
                holder.tvTitle = (TextView)vi.findViewById(R.id.tvTitle);
                holder.tvTime = (TextView)vi.findViewById(R.id.tvTime);
                vi.setTag(holder);
                break;
            case 3:
                /*
                 * Food
                 */
                vi = inflater.inflate(R.layout.list_break, null);
                holder = new ViewHolder();
                holder.tvTitle = (TextView)vi.findViewById(R.id.tvTitle);
                holder.tvTime = (TextView)vi.findViewById(R.id.tvTime);
                vi.setTag(holder);
                break;
            case 0:
                /*
                 * Session
                 */
                vi = inflater.inflate(R.layout.list_separator, null);
                holder = new ViewHolder();
                holder.tvTitle = (TextView)vi.findViewById(R.id.tvTitle);
                vi.setTag(holder);
                break;
            default:
                break;
        }

        switch (item.getItemType()) {
            case 1:
                holder.tvTitle.setText(item.getTitle());
                holder.tvTime.setText(item.getTime());
                break;
            case 2:
                holder.tvTitle.setText(item.getTitle());
                holder.tvTime.setText(item.getTime());
                break;
            case 3:
                holder.tvTitle.setText(item.getTitle());
                holder.tvTime.setText(item.getTime());
                break;
            case 0:
                holder.tvTitle.setText(item.getTitle());
                break;
            default:
                break;
        }

        return vi;
    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return items.length;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver arg0) {
        // TODO Auto-generated method stub

    }

    static class ViewHolder {
        TextView tvTitle;
        TextView tvTime;
    }

}
