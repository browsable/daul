package com.daemin.common;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.daemin.enumclass.PosState;
import com.daemin.enumclass.TimePos;
import com.daemin.timetable.R;

import java.util.ArrayList;


public class Common {

	/* BroadCastReceiver Filter */
	//public static final String ACTION_EVENT 			= "com.daemin.widget.widget.ACTION_EVENT";
	public static final String ACTION_CALL_ACTIVITY 	= "com.daemin.widget.widget.ACTION_CALL_ACTIVITY";
	public static final String ACTION_UPDATE = "com.daemin.widget.widget.ACTION_UPDATE";
	//public static final String ACTION_DIALOG 			= "com.daemin.widget.widget.ACTION_DIALOG";

	public static final String MAIN_COLOR = AppController.getInstance().getResources().getString(R.color.maincolor);

	public static boolean checkTableStateIsNothing = true;

	public static boolean isOnline() { // network 연결 상태 확인
		try {
			ConnectivityManager conMan = (ConnectivityManager) AppController.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState(); // wifi
			if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
				return true;
			}
			NetworkInfo.State mobile = conMan.getNetworkInfo(0).getState(); // mobile ConnectivityManager.TYPE_MOBILE
			if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
				return true;
			}
		} catch (NullPointerException e) {
			return false;
		}
		return false;
	}

	static ArrayList<String> tempTimePos=new ArrayList<>();
	public static ArrayList<String> getTempTimePos(){
		return tempTimePos;
	}
	public static void setTempTimePos(ArrayList<String> tempTimePos) {
		Common.tempTimePos = tempTimePos;
	}


	//원하는 상태의 TimePos 객체를 반환
	public static void stateFilter(ArrayList<String> tempTimePos){
		if(tempTimePos!=null) {
			for(String t : tempTimePos){
				TimePos.valueOf(t).setPosState(PosState.NO_PAINT);
			}
		}
		Common.getTempTimePos().clear();
		return;
	}
}
