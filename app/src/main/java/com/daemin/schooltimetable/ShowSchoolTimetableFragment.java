package com.daemin.schooltimetable;

import com.daemin.timetable.*;
import com.daemin.timetable.R.*;

import android.app.*;
import android.os.Bundle;
import android.util.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.*;
import android.widget.*;

public class ShowSchoolTimetableFragment extends Fragment {
	
	
	
	Button showtableBtn;
	Fragment fragment = null;	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_showschooltimetable, container, false);
        
       
	
        
        showtableBtn = (Button) rootView.findViewById(R.id.btnShowtimetable);
        showtableBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        	
        		fragment = new SchoolTimetableFragment();
        		
        		if (fragment != null) {
        			FragmentManager fragmentManager = getFragmentManager();
        			fragmentManager.beginTransaction()
        					.replace(R.id.frame_container, fragment).commit();
        			
        		} else {
        			// error in creating fragment
        			Log.e("MainActivity", "Error in creating fragment");
        		}
        		
        	}
        });
        
        return rootView;
    }
}
