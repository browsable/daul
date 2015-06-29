package com.daemin.timetable;

import android.os.*;
import android.view.*;

import com.daemin.timetable.common.*;

public class TimetableFragment extends BasicFragment {

	public TimetableFragment() {
		super(R.layout.fragment_timetable, "TimetableFragment");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = super.onCreateView(inflater, container, savedInstanceState);
		if (layoutId > 0) {
		}
		return root;
	}
}
