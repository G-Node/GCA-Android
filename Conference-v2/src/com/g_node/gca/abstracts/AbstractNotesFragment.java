package com.g_node.gca.abstracts;

import com.googlecode.androidannotations.annotations.res.StringArrayRes;
import com.shumail.newsroom.R;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class AbstractNotesFragment extends Fragment {
	
	private String value;
	Cursor notesCursor;
	ListView notesList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("GCA-Abs-Frag", "Notes Fragment - onCreateView");
		View rootView = inflater.inflate(R.layout.fragment_abstract_notes, container, false);
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("GCA-Abs-Frag", "Notes Fragment - onViewCreated");
        value = TabsPagerAdapter.getValue();
        //value = getArguments().getString("value");
        
        TextView sampleNote = (TextView)getView().findViewById(R.id.notesText);
        sampleNote.setText(value);
        //DatabaseHelper.addInABSTRACT_NOTES(value, "1st", "lorem desputm atera i oqwe oiqwe aw");
        //DatabaseHelper.addInABSTRACT_NOTES(value, "2nd", "asdq 123 asdasd desputm atera i oqwe oiqwe aw");
        
        String getNotesQuery = "SELECT ABSTRACT_UUID AS _id, NOTE_TITLE FROM ABSTRACT_NOTES WHERE ABSTRACT_UUID = '" + value + "';";
        notesCursor = DatabaseHelper.database.rawQuery(getNotesQuery, null);
        
        notesList = (ListView) getView().findViewById(R.id.noteslist);
        
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.noteslist_item_layout, notesCursor, new String[] {"NOTE_TITLE"}, new int[] { R.id.noteTitle}, 0);
        
        //ArrayAdapter adapter = new ArrayAdapter<String>(this,  R.layout.noteslist_item_layout, StringArray);
        notesList.setAdapter(adapter);
        
        Button addNoteBtn = (Button) getView().findViewById(R.id.addNotebtn);
        addNoteBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AddNoteDialogFragment addNoteFragment = AddNoteDialogFragment.newInstance(value);
				addNoteFragment.show(getFragmentManager(), "fragment_add_note");
			}
		});
        
	}
	
	@Override
	public void onDestroy() {
        super.onDestroy();
        Log.i("GCA-Abs-Frag", "AbstractNotes Fragment - on Destroy");
    }
}
