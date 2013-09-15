
package com.yasiradnan.abstracts;


import com.yasiradnan.conference.R;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

public class AbstractCursorAdapter extends CursorAdapter {
    Cursor cursorOne;

    String getName;

    @SuppressWarnings("deprecation")
    public AbstractCursorAdapter(Context context, Cursor c) {
        super(context, c);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO Auto-generated method stub
        
        TextView title = (TextView)view.findViewById(R.id.abTitle);

        title.setText(cursor.getString(cursor.getColumnIndexOrThrow("TITLE")));
        

        TextView topic = (TextView)view.findViewById(R.id.abTopic);

        topic.setText(cursor.getString(cursor.getColumnIndexOrThrow("TOPIC")));

        TextView type = (TextView)view.findViewById(R.id.abType);

        type.setText(cursor.getString(cursor.getColumnIndexOrThrow("TYPE")));

        String value = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

        String sqlQuery = "select abstracts_item._id AS ID,abstract_author.NAME AS NAME from abstracts_item,abstract_author,authors_abstract where abstracts_item._id = authors_abstract.abstractsitem_id and abstract_author._id = authors_abstract.abstractauthor_id and ID = "
                + value;

        cursorOne = DatabaseHelper.database.rawQuery(sqlQuery, null);

        if (cursorOne != null) {
            cursorOne.moveToFirst();
            do {

                if (cursorOne.getPosition() == 0) {

                    getName = cursorOne.getString(cursorOne.getColumnIndexOrThrow("NAME"));

                } else if (cursorOne.isLast()) {
                    getName = getName + " & "
                            + cursorOne.getString(cursorOne.getColumnIndexOrThrow("NAME"));
                } else {

                    getName = getName + " , "
                            + cursorOne.getString(cursorOne.getColumnIndexOrThrow("NAME"));

                }

            } while (cursorOne.moveToNext());
        }

        TextView authorNames = (TextView)view.findViewById(R.id.SubTitle);

        /*
         * Get Width
         */

        WindowManager WinMgr = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        int displayWidth = WinMgr.getDefaultDisplay().getWidth();

        Paint paint = new Paint();
        Rect bounds = new Rect();

        int text_height = 0;
        int text_width = 0;

        paint.getTextBounds(getName, 0, getName.length(), bounds);

        text_height = bounds.height();
        text_width = bounds.width();

        if (text_width > displayWidth) {
            //Log.e("Width inside", String.valueOf(text_width)+"--------------"+String.valueOf(displayWidth));
            String output = getName.split(",")[0] + " et al. ";
            authorNames.setText(output);

        } else {
            //Log.e("Width inside", String.valueOf(text_width)+"--------------"+String.valueOf(displayWidth));
            authorNames
            .setText(getName.replaceAll("((?:^|[^A-Z.])[A-Z])[a-z]*\\s(?=[A-Z])", "$1."));
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewgroup) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = LayoutInflater.from(viewgroup.getContext());
        View returnView = inflater.inflate(R.layout.abstract_content, viewgroup, false);
        return returnView;
    }
}
