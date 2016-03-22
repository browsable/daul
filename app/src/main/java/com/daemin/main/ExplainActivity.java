package com.daemin.main;


import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.daemin.enumclass.User;
import com.daemin.timetable.R;

public class ExplainActivity extends Activity {
	static ExplainActivity singleton;

	public static ExplainActivity getInstance() {
		return singleton;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		User.INFO.getEditor().putBoolean("explain1",false).commit();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		singleton = this;
		setContentView(R.layout.activity_explain);
		/*ImageButton ibCancel = (ImageButton)findViewById(R.id.ibCancel);
		ibCancel.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				finish();
				return true;
			}
		});*/
		ImageView ivCancel = (ImageView)findViewById(R.id.ivCancel);
		ivCancel.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				finish();
				return true;
			}
		});

	}
}