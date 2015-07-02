package com.daemin.timetable.common;

import com.daemin.main.SubMainActivity;
import com.daemin.timetable.R;


public class Common {

	/* BroadCastReceiver Filter */
	//public static final String ACTION_EVENT 			= "com.daemin.widget.widget.ACTION_EVENT";
	public static final String ACTION_CALL_ACTIVITY 	= "com.daemin.widget.widget.ACTION_CALL_ACTIVITY";
	public static final String ACTION_UPDATE = "com.daemin.widget.widget.ACTION_UPDATE";
	//public static final String ACTION_DIALOG 			= "com.daemin.widget.widget.ACTION_DIALOG";


	public static final String MAIN_COLOR = SubMainActivity.getInstance().getResources().getString(R.color.maincolor);

	public static boolean checkTableStateIsNothing = true;

	// File url to download
	public static final String UNIVDB_URL = "http://browsable.cafe24.com/timetable/schedule/";



}
