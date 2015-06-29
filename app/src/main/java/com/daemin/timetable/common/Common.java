package com.daemin.timetable.common;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.daemin.main.SubMainActivity;
import com.daemin.timetable.R;

public class Common {

	/* BroadCastReceiver Filter */
	//public static final String ACTION_EVENT 			= "com.daemin.widget.widget.ACTION_EVENT";
	public static final String ACTION_CALL_ACTIVITY 	= "com.daemin.widget.widget.ACTION_CALL_ACTIVITY";
	public static final String ACTION_UPDATE = "com.daemin.widget.widget.ACTION_UPDATE";
	//public static final String ACTION_DIALOG 			= "com.daemin.widget.widget.ACTION_DIALOG";


	public static final String MAIN_COLOR = SubMainActivity.getInstance().getResources().getString(R.color.maincolor);
	// File url to download
	public static final String UNIVDB_URL = "http://browsable.cafe24.com/timetable/schedule/";


	public static boolean checkTableStateIsNothing = true;
}
