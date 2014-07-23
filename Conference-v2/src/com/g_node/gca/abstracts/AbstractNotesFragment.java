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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class AbstractNotesFragment extends Fragment {
	
	private String value;
	static Cursor notesCursor;
	ListView notesList;
	static SimpleCursorAdapter adapter;
	TextView notificationNote;
	
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
        
        notificationNote = (TextView)getView().findViewById(R.id.notesText);
        notificationNote.setText("Your Notes for the Abstract");

        //populate the notes from DB into listview
        populateListView();
        registerForContextMenu(notesList);
        
        Button addNoteBtn = (Button) getView().findViewById(R.id.addNotebtn);
        addNoteBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AddNoteDialogFragment addNoteFragment = AddNoteDialogFragment.newInstance(value);
				addNoteFragment.show(getFragmentManager(), "fragment_add_note");
			}
		});
        
        Button refreshNotesBtn = (Button) getView().findViewById(R.id.NoteRefresh);
        refreshNotesBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				populateListView();
				Toast.makeText(getActivity(), "Notes Refrshed.", Toast.LENGTH_SHORT).show();
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
			    }).setIcon(getResources().getDrawable(R.drawable.notes_icon))
			     .show();
			}
		
        });
        
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;  // ??
		int currentNoteId = (int) info.id;         
		menu.add(0, 2, 0, "Edit");
		menu.add(0, 3, 1, "Delete");   
	}
	
	 @Override
	  public boolean onContextItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	      case 3:
	      {
	        AdapterView.AdapterContextMenuInfo info=
	          (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	        Log.i("GCA-Abs-Frag", "Menu info id: " + info.id);
	        DatabaseHelper.deleteFromABSTRACT_NOTES(info.id);
	        populateListView();
	        return(true);
	      }
	      case 2:
	      {
	        AdapterView.AdapterContextMenuInfo info=
	          (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	        Log.i("GCA-Abs-Frag", "Menu info id: " + info.id);
	        
	        
	        EditNoteDialogFragment editNoteFragment = EditNoteDialogFragment.newInstance(value, String.valueOf(info.id));
			editNoteFragment.show(getFragmentManager(), "fragment_edit_note");
	        //DatabaseHelper.deleteFromABSTRACT_NOTES(info.id);
			//update in dialogefragment positive button method 
	        Log.i("GCA-Abs-Frag", "After EditNote Dialog");
	        //populateListView();
	        return(true);
	      }
	    }

	    return(super.onOptionsItemSelected(item));
	  }
	
	
	public void populateListView() {
		Log.i("GCA-Abs-Frag", "in PopulateListview func");
		String getNotesQuery = "SELECT NOTE_ID AS _id, ABSTRACT_UUID, NOTE_TITLE, NOTE_TEXT FROM ABSTRACT_NOTES WHERE ABSTRACT_UUID = '" + value + "';";
        notesCursor = DatabaseHelper.database.rawQuery(getNotesQuery, null);
        if(notesCursor.getCount() <= 0) {
        	notificationNote.setText("No Notes available for this Abstract");
        } else {
        	notificationNote.setText("You have " + notesCursor.getCount() + " Notes for the Abstract");
        }
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
