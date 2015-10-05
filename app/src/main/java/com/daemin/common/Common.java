package com.daemin.common;


import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.daemin.enumclass.DayOfMonthPos;
import com.daemin.enumclass.DayOfMonthPosState;
import com.daemin.enumclass.PosState;
import com.daemin.enumclass.TimePos;
import com.daemin.timetable.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Hashtable;


public class Common {

	/* BroadCastReceiver Filter */
	//public static final String ACTION_EVENT 			= "com.daemin.widget.widget.ACTION_EVENT";
	public static final String ACTION_CALL_ACTIVITY = "com.daemin.widget.widget.ACTION_CALL_ACTIVITY";
	public static final String ACTION_UPDATE = "com.daemin.widget.widget.ACTION_UPDATE";
	//public static final String ACTION_DIALOG 			= "com.daemin.widget.widget.ACTION_DIALOG";

	public static final String MAIN_COLOR = AppController.getInstance().getResources().getString(R.color.maincolor);

	public static boolean checkTableStateIsNothing = true;

	public static boolean llIncludeDepIn = false;
	public static boolean isLlIncludeDepIn() {
		return llIncludeDepIn;
	}
	public static void setLlIncludeDepIn(boolean llIncludeDepIn) {
		Common.llIncludeDepIn = llIncludeDepIn;
	}
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
	public static void stateFilter(ArrayList<String> tempTimePos,String viewMode){
		if(tempTimePos!=null) {
			switch(viewMode){
				case "week":
					for(String t : tempTimePos){
						TimePos.valueOf(t).setMin(0,60);
						TimePos.valueOf(t).setPosState(PosState.NO_PAINT);
					}
					break;
				case "month":
					for(String t : tempTimePos){
						DayOfMonthPos.valueOf(t).setPosState(DayOfMonthPosState.NO_PAINT);
					}
					break;
			}
		}
		Common.getTempTimePos().clear();
		return;
	}


	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			listView.setVisibility(View.GONE);
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * TextView의 text를 보기 좋게 줄바꿈해준다.
	 * 출처 - http://blog.naver.com/min_ting/110118117984
	 * @param textPaint      TextView의 Paint 객체
	 * @param strText        문자열
	 * @param breakWidth     줄바꿈 하고 싶은 width값 지정
	 * @return
	 */
	public static String breakText(Paint textPaint, String strText, int breakWidth) {
		StringBuilder sb = new StringBuilder();
		int endValue = 0;
		do{
			endValue = textPaint.breakText(strText, true, breakWidth, null);
			if(endValue > 0) {
				sb.append(strText.substring(0, endValue)).append("\n");
				strText = strText.substring(endValue);
			}
		}while(endValue > 0);
		return sb.toString().substring(0, sb.length()-1);  // 마지막 "\n"를 제거
	}

	private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();
	//폰트 사용 예시 title.setTypeface(Utils.getFont("nexa-bold.ttf", getApplicationContext()));
	public static Typeface getFont(String name, Context context) {
		Typeface tf = fontCache.get(name);
		if(tf == null) {
			try {
				tf = Typeface.createFromAsset(context.getAssets(), name);
			}
			catch (Exception e) {
				return null;
			}
			fontCache.put(name, tf);
		}
		return tf;
	}

	public static final String md5(final String password) {
		try {

			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(password.getBytes());
			byte messageDigest[] = digest.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
}
