package com.daemin.common;


import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.daemin.enumclass.Dates;
import com.daemin.enumclass.DayOfMonthPos;
import com.daemin.enumclass.DayOfMonthPosState;
import com.daemin.enumclass.PosState;
import com.daemin.enumclass.TimePos;
import com.daemin.enumclass.User;
import com.daemin.repository.MyTimeRepo;

import java.util.ArrayList;
import java.util.Hashtable;

import timedao.MyTime;


public class Common {

	/* BroadCastReceiver Filter */
	public static final String ACTION_HOME5_5 = "com.daemin.widget.ACTION_HOME5_5";
	public static final String ACTION_DIAL5_5 = "com.daemin.widget.ACTION_DIAL5_5";
	public static final String ACTION_WEEK5_5 = "com.daemin.widget.ACTION_WEEK5_5";
	public static final String ACTION_MONTH5_5 = "com.daemin.widget.ACTION_MONTH5_5";
	public static final String ACTION_BACK5_5 = "com.daemin.widget.ACTION_BACK5_5";
	public static final String ACTION_FORWARD5_5 = "com.daemin.widget.ACTION_FORWARD5_5";
	public static final String ACTION_HOME4_4 = "com.daemin.widget.ACTION_HOME4_4";
	public static final String ACTION_WEEK4_4 = "com.daemin.widget.ACTION_WEEK4_4";
	public static final String ACTION_MONTH4_4 = "com.daemin.widget.ACTION_MONTH4_4";
	public static final String ACTION_BACK4_4 = "com.daemin.widget.ACTION_BACK4_4";
	public static final String ACTION_FORWARD4_4 = "com.daemin.widget.ACTION_FORWARD4_4";
	public static final String ACTION_DIAL4_4 = "com.daemin.widget.ACTION_DIAL4_4";
	//public static final String CAPTURE = Environment.getExternalStorageDirectory().toString() + "/.TimeDAO/timetable.jpg";
	public static final String MAIN_COLOR = "#73C8BA";

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
	public static void fetchWeekData(){
		for (TimePos ETP : TimePos.values()) {
			ETP.setPosState(PosState.NO_PAINT);
			ETP.setMin(0, 60);
			ETP.setInitTitle();
		}
		int week_startMonth = Dates.NOW.monthOfSun;
		int week_startDay = Dates.NOW.dayOfSun;
		int week_endMonth = Dates.NOW.monthOfSat;
		int week_endDay = Dates.NOW.dayOfSat;
		int week_startYear;
		int week_endYear;
		if(week_startMonth==12&&week_endMonth==1){
			week_endYear= Dates.NOW.year;
			week_startYear=week_endYear-1;
		}else
			week_endYear=week_startYear=Dates.NOW.year;
		long week_startMillies = Dates.NOW.getDateMillis(week_startYear, week_startMonth, week_startDay, 8, 0);
		long week_endMillies = Dates.NOW.getDateMillis(week_endYear, week_endMonth, week_endDay, 23, 0);
		User.INFO.weekData.clear();
		User.INFO.weekData.addAll(MyTimeRepo.getWeekTimes(AppController.getInstance(), week_startMillies, week_endMillies));

		for(MyTime mt :User.INFO.weekData){
			addWeek(mt.getName(),mt.getDayofweek(), mt.getStarthour(), mt.getEndhour(), mt.getEndmin());
		}
	}
	public static void fetchMonthData(){
		/*for (TimePos ETP : TimePos.values()) {
			ETP.setPosState(PosState.NO_PAINT);
			ETP.setMin(0, 60);
			ETP.setInitTitle();
		}
		int week_startMonth = Dates.NOW.monthOfSun;
		int week_startDay = Dates.NOW.dayOfSun;
		int week_endMonth = Dates.NOW.monthOfSat;
		int week_endDay = Dates.NOW.dayOfSat;
		int week_startYear;
		int week_endYear;
		if(week_startMonth==12&&week_endMonth==1){
			week_endYear= Dates.NOW.year;
			week_startYear=week_endYear-1;
		}else
			week_endYear=week_startYear=Dates.NOW.year;
		long week_startMillies = Dates.NOW.getDateMillis(week_startYear, week_startMonth, week_startDay, 8, 0);
		long week_endMillies = Dates.NOW.getDateMillis(week_endYear, week_endMonth, week_endDay, 23, 0);
		User.INFO.weekData.clear();
		User.INFO.weekData.addAll(MyTimeRepo.getWeekTimes(AppController.getInstance(), week_startMillies, week_endMillies));

		for(MyTime mt :User.INFO.weekData){
			addWeek(mt.getName(),mt.getDayofweek(), mt.getStarthour(), mt.getEndhour(), mt.getEndmin());
		}*/
	}
	public static void addWeek(String title, int xth, int startHour,int endHour, int endMin){
		if(endMin!=0) ++endHour;
		TimePos[] tp = new TimePos[endHour - startHour];
		int j = 0;
		for (int i = startHour; i < endHour; i++) {
			tp[j] = TimePos.valueOf(Convert.getxyMerge(xth, Convert.HourOfDayToYth(i)));
			tp[j].setPosState(PosState.ENROLL);
			if(i==startHour)tp[j].setTitle(title);
			++j;
		}
	}
	public static boolean isTableEmpty(){
		boolean empty = true;
		for (TimePos ETP : TimePos.values()) {
			if(ETP.getPosState()!=PosState.NO_PAINT) empty =false;
		}
		return empty;
	}
	static ArrayList<String> tempTimePos=new ArrayList<>();
	public static ArrayList<String> getTempTimePos(){
		return tempTimePos;
	}
	public static void stateFilter(int viewMode){
		if(tempTimePos!=null) {
			switch(viewMode){
				case 0:
					for(String t : tempTimePos){
						TimePos tp= TimePos.valueOf(t);
						tp.setMin(0, 60);
						if(tp.getPosState()==PosState.PAINT) {
							tp.setPosState(PosState.NO_PAINT);
						}else if(tp.getPosState()==PosState.OVERLAP){
							tp.setPosState(PosState.ENROLL);
						}
					}
					break;
				case 1:
					for(String t : tempTimePos){
						DayOfMonthPos.valueOf(t).setPosState(DayOfMonthPosState.NO_PAINT);
					}
					break;
			}
		}
		Common.getTempTimePos().clear();
		return;
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
}
