package com.daemin.main;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.daemin.common.MyRequest;
import com.daemin.dialog.DialDefault;
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

public class SplashActivity extends Activity{
	static SplashActivity singleton;
	int SPLASH_TIME=1000;
	private final int REQUEST_PHONESTATE = 100;
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
		checkPhoneStatePermission();
		//MyRequest.test(singleton);
	}

	//앱 설치시 맨 처음 한 번만 셋팅
	public void firstSetting(){
		//기기 해상도 너비,높이
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int deviceWidth = displayMetrics.widthPixels;
		int deviceHeight = displayMetrics.heightPixels;

		TelephonyManager systemService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNum;
		//폰번호
        try{
			phoneNum = systemService.getLine1Number();
			if (phoneNum != null) {
				/*phoneNum = phoneNum.substring(phoneNum.length() - 10, phoneNum.length());
				phoneNum = "0" + phoneNum;*/
				phoneNum = PhoneNumberUtils.formatNumber(phoneNum);
			} else {
				phoneNum = "00000000000";
			}
		}catch (StringIndexOutOfBoundsException e){
			e.printStackTrace();
			phoneNum = systemService.getLine1Number();
		}catch(NullPointerException e){
			e.printStackTrace();
			phoneNum = systemService.getLine1Number();
		}catch(Exception e){
			e.printStackTrace();
			phoneNum = systemService.getLine1Number();
		}
		//MAC Address
		WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		String macAddress = info.getMacAddress();
		if (macAddress == null) {
			macAddress = "00:00:00:00:00";//device has no macaddress or wifi is disabled
		}
		SharedPreferences.Editor editor = User.INFO.getEditor();
		editor.putBoolean("firstFlag", false);
		editor.putInt("deviceWidth", deviceWidth);
		editor.putInt("deviceHeight", deviceHeight);
		try {
			MyHash myHash = new MyHash();
			editor.putString("userPK", myHash.encrypt(phoneNum + macAddress));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		editor.commit();
	}
	public void Splash(){
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				finish();
				Intent i = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(i);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
		}, SPLASH_TIME);
	}

	/**
	 * Permission check.
	 */
	private void checkPhoneStatePermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                    // Explain to the user why we need to write the permission.
                    Toast.makeText(this, getString(R.string.permission_phonestate), Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                                REQUEST_PHONESTATE);
            } else {
                // 권한 항상 허용일
				if(User.INFO.getFirstFlag())firstSetting();
				Splash();
            }
		}else {
			// 권한 항상 허용일 경우
			if(User.INFO.getFirstFlag())firstSetting();
			Splash();
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case REQUEST_PHONESTATE:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
					firstSetting();
					Splash();
				} else {
					//권한 거부시
					DialDefault dd = new DialDefault(this,
							getString(R.string.permission_request),
							getString(R.string.permission_phonestate)+
							getString(R.string.permission_deny),
							9);
					dd.show();
				}
				break;
		}
	}
}
