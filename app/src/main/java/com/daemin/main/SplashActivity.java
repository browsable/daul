package com.daemin.main;


import android.app.*;
import android.content.*;
import android.os.*;

import com.daemin.timetable.*;

public class SplashActivity extends Activity {
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_splash);
			initialize();

		}
	
	
	private void initialize() {
		Handler handler = new Handler() {

			public void handleMessage(Message msg) {
				finish();
				

				Intent i = new Intent(getApplicationContext(), SubMainActivity.class);
			    startActivity(i);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

			}
		};
		handler.sendEmptyMessageDelayed(0, 500);
	}

}
