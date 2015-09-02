/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.abstracts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.g_node.bc15.R;


public class AddNoteDialogFragment extends DialogFragment {

	
	public AddNoteDialogFragment(){
	    // Empty constructor required for DialogFragment
	}
	
	public static AddNoteDialogFragment newInstance(String uuid) {
		AddNoteDialogFragment frag = new AddNoteDialogFragment();
        Bundle args = new Bundle();
        args.putString("uuid", uuid);
        frag.setArguments(args);
        return frag;
    }
	
//	 @Override
//	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//	            Bundle savedInstanceState) {
//		 
//				 
		 
//		 
//	        View view = inflater.inflate(R.layout.add_note_dialog_fragment, container);
//	        EditText mEditText = (EditText) view.findViewById(R.id.txt_your_name);
//	        String uuid = getArguments().getString("uuid", "uuid not valid");
//	        getDialog().setTitle("Add New Note:");
//	        // Show soft keyboard automatically
//	        mEditText.requestFocus();
//	        
//	        return view;
//	    }
	 
	 @Override
     public Dialog onCreateDialog(Bundle savedInstanceState) {
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		 
		 final View view = getActivity().getLayoutInflater().inflate(R.layout.add_note_dialog_fragment, null);
		 final String uuid = getArguments().getString("uuid", "uuid not valid");
         alertDialogBuilder.setView(view);
         alertDialogBuilder.setTitle("Add New Note: ");
         alertDialogBuilder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
            	 
            	 EditText noteTextEditText = (EditText) view.findViewById(R.id.noteText);
            	 EditText noteTitleEditText = (EditText) view.findViewById(R.id.noteTitle2);

            	 String absUUID = uuid;
            	 Editable noteText = noteTextEditText.getText();
            	 String y = noteText.toString();
            	 String noteTitle = noteTitleEditText.getText().toString();
            	 
            	 //Adding to the DB
            	 DatabaseHelper.addInABSTRACT_NOTES(absUUID, noteTitle, y);
                 
            	 Toast.makeText(getActivity(), "Note Added - Please Refresh..", Toast.LENGTH_SHORT).show();
                 
//                 String getNotesQuery = "SELECT ABSTRACT_UUID AS _id, NOTE_TITLE FROM ABSTRACT_NOTES WHERE ABSTRACT_UUID = '" + absUUID + "';";
//                 AbstractNotesFragment.notesCursor = DatabaseHelper.database.rawQuery(getNotesQuery, null);
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