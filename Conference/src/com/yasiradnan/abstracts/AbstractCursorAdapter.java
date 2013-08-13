
package com.yasiradnan.abstracts;

import com.yasiradnan.conference.R;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        ;

        TextView topic = (TextView)view.findViewById(R.id.abTopic);

        topic.setText(cursor.getString(cursor.getColumnIndexOrThrow("TOPIC")));

        TextView type = (TextView)view.findViewById(R.id.abType);

        type.setText(cursor.getString(cursor.getColumnIndexOrThrow("TYPE")));

        String value = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

        String sqlQuery = "select abstracts_item._id AS ID,abstract_author.NAME AS NAME from abstracts_item,abstract_author,authors_abstract where abstracts_item._id = authors_abstract.abstractsitem_id and abstract_author._id = authors_abstract.abstractauthor_id and ID = "
                + value + " GROUP BY NAME";

        cursorOne = AbstractActivity.database.rawQuery(sqlQuery, null);

        if (cursorOne != null) {
            cursorOne.moveToFirst();
            do {
                
                if (cursorOne.getPosition() == 0) {

                    getName = cursorOne.getString(cursorOne.getColumnIndexOrThrow("NAME"));

                } else {

                    getName = getName + ","
                            + cursorOne.getString(cursorOne.getColumnIndexOrThrow("NAME"));

                }

            } while (cursorOne.moveToNext());
        }

        TextView authorNames = (TextView)view.findViewById(R.id.SubTitle);

        String formatterNames = "";

        String[] namesArray = getName.split(",");

        Log.e("Length", String.valueOf(namesArray.length));

        if (namesArray.length > 1) {
            for (int i = 0; i < namesArray.length; i++) {

                if (i == namesArray.length - 1) {
                    formatterNames = formatterNames + " & " + namesArray[i];
                }else if (i == 0){
                    formatterNames = formatterNames +namesArray[i];
                }
                else {
                    formatterNames = formatterNames + " , " +namesArray[i];
                }
            }
            authorNames.setText(formatterNames.replaceAll("((?:^|[^A-Z.])[A-Z])[a-z]*\\s(?=[A-Z])",
                    "$1."));
        } else {

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
