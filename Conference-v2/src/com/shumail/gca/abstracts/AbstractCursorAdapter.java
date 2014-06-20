/**
 * Copyright (c) 2013, Yasir Adnan <adnan.ayon@gmail.com>
 * License: BSD-3 (See LICENSE)
 */

package com.shumail.gca.abstracts;

import com.shumail.newsroom.R;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.widget.CursorAdapter;
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
        /*
         * TextView for showing Title
         */
        TextView title = (TextView)view.findViewById(R.id.abTitle);
        title.setText(cursor.getString(cursor.getColumnIndexOrThrow("TITLE")));
        /*
         * TextView for showing Topic
         */
        TextView topic = (TextView)view.findViewById(R.id.abTopic);
        topic.setText(cursor.getString(cursor.getColumnIndexOrThrow("TOPIC")));
        /*
         * TextView for Showing Type
         */
        TextView type = (TextView)view.findViewById(R.id.abType);
        type.setText(cursor.getString(cursor.getColumnIndexOrThrow("TYPE")));
        /*
         * _id for getting Author Names
         */
        String value = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

        String authorNamesQuery = "SELECT DISTINCT AUTHORS_DETAILS.AUTHOR_UUID, AUTHORS_DETAILS.AUTHOR_FIRST_NAME, AUTHOR_MIDDLE_NAME, AUTHOR_LAST_NAME, AUTHOR_EMAIL FROM AUTHORS_DETAILS WHERE AUTHORS_DETAILS.AUTHOR_UUID IN (SELECT AUTHOR_UUID FROM ABSTRACT_AUTHOR_POSITION_AFFILIATION WHERE ABSTRACT_UUID = '" + value + "');";
        cursorOne =  DatabaseHelper.database.rawQuery(authorNamesQuery, null);
        
      if (cursorOne != null) {
      cursorOne.moveToFirst();
      /*
       * Name format will be like this A, B & C or A,B,C & D. So, if the
       * name is the last name. We should use '&' before the name
       */
      do {

          if (cursorOne.getPosition() == 0) {
              /*
               * First data
               */
              getName = cursorOne.getString(cursorOne.getColumnIndexOrThrow("AUTHOR_FIRST_NAME")) + " " + cursorOne.getString(cursorOne.getColumnIndexOrThrow("AUTHOR_LAST_NAME"));
          } else if (cursorOne.isLast()) {
              /*
               * Last Data
               */
              getName = getName + " & "
                      + cursorOne.getString(cursorOne.getColumnIndexOrThrow("AUTHOR_FIRST_NAME")) + " " + cursorOne.getString(cursorOne.getColumnIndexOrThrow("AUTHOR_LAST_NAME"));
          } else {
              getName = getName + " , "
                      + cursorOne.getString(cursorOne.getColumnIndexOrThrow("AUTHOR_FIRST_NAME")) + " " + cursorOne.getString(cursorOne.getColumnIndexOrThrow("AUTHOR_LAST_NAME"));

          }

      } while (cursorOne.moveToNext());
  }
        
        
//        /*
//         * SQL Query for getting Author Name
//         */
//        String sqlQuery = "select abstracts_item._id AS ID,abstract_author.NAME AS NAME from abstracts_item,abstract_author,authors_abstract where abstracts_item._id = authors_abstract.abstractsitem_id and abstract_author._id = authors_abstract.abstractauthor_id and ID = "
//                + value;
//
//        cursorOne = DatabaseHelper.database.rawQuery(sqlQuery, null);
//
//        if (cursorOne != null) {
//            cursorOne.moveToFirst();
//            /*
//             * Name format will be like this A, B & C or A,B,C & D. So, if the
//             * name is the last name. We should use '&' before the name
//             */
//            do {
//
//                if (cursorOne.getPosition() == 0) {
//                    /*
//                     * First data
//                     */
//                    getName = cursorOne.getString(cursorOne.getColumnIndexOrThrow("NAME"));
//                } else if (cursorOne.isLast()) {
//                    /*
//                     * Last Data
//                     */
//                    getName = getName + " & "
//                            + cursorOne.getString(cursorOne.getColumnIndexOrThrow("NAME"));
//                } else {
//                    getName = getName + " , "
//                            + cursorOne.getString(cursorOne.getColumnIndexOrThrow("NAME"));
//
//                }
//
//            } while (cursorOne.moveToNext());
//        }
        /*
         * TextView for Author Names
         */
        TextView authorNames = (TextView)view.findViewById(R.id.SubTitle);
        /*
         * Get Width
         */
        WindowManager WinMgr = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int displayWidth = WinMgr.getDefaultDisplay().getWidth();
        Paint paint = new Paint();
        Rect bounds = new Rect();
        int text_width = 0;
        paint.getTextBounds(getName, 0, getName.length(), bounds);
        /*
         * Getting Text Width
         */
        text_width = bounds.width();
        /*
         * If Text Width is greater than Display Width Then show First Name et
         * al.
         */
        if (text_width > displayWidth) {
            String output = getName.split(",")[0] + " et al. ";
            authorNames.setText(output);

        } else {
            /*
             * Name Format will be like this If the Name is Yasir Adnan So,It
             * will be Y.Adnan
             */
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
