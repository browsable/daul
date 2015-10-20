package com.daemin.timetable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daemin.common.BasicFragment;


public class InitDayFragment extends BasicFragment {

	public InitDayFragment() {
		super(R.layout.fragment_initday, "InitDayFragment");
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
