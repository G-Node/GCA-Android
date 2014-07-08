package com.g_node.gca.abstracts;

import com.shumail.newsroom.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class EditNoteDialogFragment extends DialogFragment {

	
	public EditNoteDialogFragment(){
	    // Empty constructor required for DialogFragment
	}
	
	public static EditNoteDialogFragment newInstance(String uuid, String note_id) {
		EditNoteDialogFragment frag = new EditNoteDialogFragment();
        Bundle args = new Bundle();
        args.putString("uuid", uuid);
        args.putString("note_id", note_id);
        frag.setArguments(args);
        return frag;
    }

	 @Override
     public Dialog onCreateDialog(Bundle savedInstanceState) {
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		 
		 final View view = getActivity().getLayoutInflater().inflate(R.layout.add_note_dialog_fragment, null);
		 final String uuid = getArguments().getString("uuid", "uuid not valid");
		 final String current_note_id = getArguments().getString("note_id", "note_id not valid");
		 alertDialogBuilder.setView(view);
         alertDialogBuilder.setTitle("Edit Note: ");
         
         final EditText noteTextEditText = (EditText) view.findViewById(R.id.noteText);
    	 final EditText noteTitleEditText = (EditText) view.findViewById(R.id.noteTitle2);

         
		//setting previous values
         String getNotesQuery = "SELECT NOTE_ID, ABSTRACT_UUID, NOTE_TITLE, NOTE_TEXT FROM ABSTRACT_NOTES WHERE NOTE_ID = '" + current_note_id  + "';";
         Cursor notesCursor = DatabaseHelper.database.rawQuery(getNotesQuery, null);
         notesCursor.moveToFirst();
         String currentNoteTitle = notesCursor.getString(notesCursor.getColumnIndexOrThrow("NOTE_TITLE"));
         String currentNoteText = notesCursor.getString(notesCursor.getColumnIndexOrThrow("NOTE_TEXT"));
         
         noteTitleEditText.setText(currentNoteTitle);         
         noteTextEditText.setText(currentNoteText);

         
         alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
            	 
            	 
            	 String absUUID = uuid;
            	 Editable noteText = noteTextEditText.getText();
            	 String y = noteText.toString();
            	 String noteTitle = noteTitleEditText.getText().toString();
            	 
            	 DatabaseHelper.updateNoteABSTRACT_NOTES(current_note_id, noteTitle, y);
                 
            	 Toast.makeText(getActivity(), "Updated..", Toast.LENGTH_SHORT).show();
 //                 
                 AbstractNotesFragment.adapter.notifyDataSetChanged();
                 dialog.dismiss();
             }
         });
         alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 Toast.makeText(getActivity(), "Cancelled" , Toast.LENGTH_SHORT).show();
                 dialog.cancel();
             }
         });

         return alertDialogBuilder.create();

	 }

}