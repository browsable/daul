package com.daemin.main;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.daemin.common.MyRequest;
import com.daemin.repository.GroupListFromServerRepository;
import com.daemin.timetable.R;

import java.util.ArrayList;

import timedao_group.GroupListFromServer;

public class SplashActivity extends Activity {
	static SplashActivity singleton;
	public ArrayList<String> groupListFomServer = new ArrayList<>();

	public ArrayList<String> getGroupListFomServer() {
		return groupListFomServer;
	}
	public static SplashActivity getInstance() {
		return singleton;
	}
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			singleton = this;
			setContentView(R.layout.activity_splash);

			MyRequest.getGroupList();
			// 로컬 sqlite 상에 저장된 그룹리스트 가져와 보여줌
			for( GroupListFromServer GLFS : GroupListFromServerRepository.getAllGroupListFromServer(this)){
				groupListFomServer.add(GLFS.getName());
			}
			//EnumDialog.BOTTOMDIAL.setUnivName(groupListFomServer);
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
