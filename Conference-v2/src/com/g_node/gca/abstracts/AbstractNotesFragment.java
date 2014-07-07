package com.g_node.gca.abstracts;

import com.shumail.newsroom.R;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AbstractNotesFragment extends Fragment {
	
	private String value;
	static Cursor notesCursor;
	ListView notesList;
	static SimpleCursorAdapter adapter;
	
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
        
        populateListView();
        
        Button addNoteBtn = (Button) getView().findViewById(R.id.addNotebtn);
        addNoteBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AddNoteDialogFragment addNoteFragment = AddNoteDialogFragment.newInstance(value);
				addNoteFragment.show(getFragmentManager(), "fragment_add_note");
			}
		});
     
        notesList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Builder x = new AlertDialog.Builder(getActivity());
				String noteTitle = notesCursor.getString(notesCursor.getColumnIndexOrThrow("NOTE_TITLE"));
				String noteText = notesCursor.getString(notesCursor.getColumnIndexOrThrow("NOTE_TEXT"));
			    x.setTitle(noteTitle)
			    .setMessage(noteText)
			    .setNeutralButton(android.R.string.ok,
			            new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int id) {
			            dialog.cancel();
			        }
			    }).setIcon(android.R.drawable.ic_dialog_info)
			     .show();
			}
		
        });
        
	}
	
	public void populateListView() {
		String getNotesQuery = "SELECT ABSTRACT_UUID AS _id, NOTE_TITLE, NOTE_TEXT FROM ABSTRACT_NOTES WHERE ABSTRACT_UUID = '" + value + "';";
        notesCursor = DatabaseHelper.database.rawQuery(getNotesQuery, null);
        
        notesList = (ListView) getView().findViewById(R.id.noteslist);
        
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.noteslist_item_layout, notesCursor, new String[] {"NOTE_TITLE"}, new int[] { R.id.noteTitle}, 0);
        //adapter.notifyDataSetChanged();
        //ArrayAdapter adapter = new ArrayAdapter<String>(this,  R.layout.noteslist_item_layout, StringArray);
        notesList.setAdapter(adapter);
	}
	
	@Override
	public void onDestroy() {
        super.onDestroy();
        Log.i("GCA-Abs-Frag", "AbstractNotes Fragment - on Destroy");
    }
	
	@Override
	public void onResume() {
        super.onResume();
        Log.i("GCA-Abs-Frag", "AbstractNotes Fragment - on Resume");
    }
	
}
