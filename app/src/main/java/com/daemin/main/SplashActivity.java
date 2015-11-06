package com.daemin.main;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.daemin.common.Common;
import com.daemin.enumclass.MyPreferences;
import com.daemin.enumclass.User;
import com.daemin.timetable.R;

public class SplashActivity extends Activity {
	static SplashActivity singleton;

	public static SplashActivity getInstance() {
		return singleton;
	}
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			singleton = this;
			setContentView(R.layout.activity_splash);
			initialize();

		}

	
	private void initialize() {
		if(Common.isOnline()) {
			if (User.USER.isGroupListDownloadState()) {
				Toast.makeText(this, "그룹리스트다운로드되있는상태", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, "그룹리스트다운로드해야함", Toast.LENGTH_SHORT).show();
				//MyRequest.getGroupList();
			}
		}
		else{
			Toast.makeText(this, this.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
		}
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int deviceWidth = displayMetrics.widthPixels;
		int deviceHeight =displayMetrics.heightPixels;
		SharedPreferences.Editor wEditor = MyPreferences.USERINFO.getEditor();
		wEditor.putInt("deviceWidth", deviceWidth);
		wEditor.putInt("deviceHeight", deviceHeight);
		wEditor.commit();
		//MyRequest.test(singleton);
		Handler handler = new Handler() {

			public void handleMessage(Message msg) {
				finish();

				Intent i = new Intent(SplashActivity.this, MainActivity.class);
			    startActivity(i);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
		};
		handler.sendEmptyMessageDelayed(0, 1000);
	}

}
