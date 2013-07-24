package com.yasiradnan.abstracts;

import com.yasiradnan.conference.AbstractsItem;
import com.yasiradnan.conference.R;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AbstractCursorAdapter extends CursorAdapter {

    @SuppressWarnings("deprecation")
    public AbstractCursorAdapter(Context context, Cursor c) {
        super(context, c);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO Auto-generated method stub
        TextView title = (TextView)view.findViewById(R.id.abTitle);
        title.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));
        TextView topic = (TextView)view.findViewById(R.id.abTopic);
        topic.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(6))));
        TextView type = (TextView)view.findViewById(R.id.abType);
        type.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(5))));
        TextView SubType = (TextView)view.findViewById(R.id.SubTitle);
        SubType.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(5))));

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewgroup) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = LayoutInflater.from(viewgroup.getContext());
        View returnView = inflater.inflate(R.layout.abstract_content, viewgroup, false);
        return returnView;
    }

}
