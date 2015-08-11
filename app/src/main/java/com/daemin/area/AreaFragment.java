package com.daemin.area;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daemin.common.BasicFragment;
import com.daemin.timetable.R;

public class AreaFragment extends BasicFragment {
	//public static RequestQueue queue;
	public AreaFragment() {
		super(R.layout.fragment_phone, "AreaFragment");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = super.onCreateView(inflater, container, savedInstanceState);
		if (layoutId > 0) {
			//test
			//test3
			//test23423
			//test356565
			//load();
		}
		return root;
	}
}
