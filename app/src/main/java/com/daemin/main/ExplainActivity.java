package com.daemin.main;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.daemin.common.MyRequest;
import com.daemin.encryption.MyHash;
import com.daemin.enumclass.User;
import com.daemin.timetable.R;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
		ImageButton ibCancel = (ImageButton)findViewById(R.id.ibCancel);
		ibCancel.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				finish();
				return true;
			}
		});
	}
}