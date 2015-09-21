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

import com.g_node.gcaa.R;


public class AddNoteDialogFragment extends DialogFragment {

	private final DatabaseHelper mDbHelper = DatabaseHelper.getInstance(this
			.getActivity());
	
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
            	 mDbHelper.addInABSTRACT_NOTES(absUUID, noteTitle, y);
                 getActivity().getFragmentManager().findFragmentById(R.layout.noteslist_item_layout);
            	 Toast.makeText(getActivity(), "Note Added", Toast.LENGTH_SHORT).show();
            	 getActivity().startActivityForResult(getActivity().getIntent(), 10);
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