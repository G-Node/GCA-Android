package com.g_node.gca.abstracts;

import com.shumail.newsroom.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AbstractNotesFragment extends Fragment {
	
	private String value;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_abstract_notes, container, false);
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        value = TabsPagerAdapter.getValue();
        //value = getArguments().getString("value");
        
        TextView sampleNote = (TextView)getView().findViewById(R.id.notesText);
        sampleNote.setText(value);
        
	}
	
	@Override
	public void onDestroy() {
        super.onDestroy();
        Log.i("GCA-Abs-Frag", "AbstractNotes Fragment - on Destroy");
    }
}
