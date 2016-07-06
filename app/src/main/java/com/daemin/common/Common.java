package com.daemin.common;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.daemin.enumclass.Dates;
import com.daemin.enumclass.DayOfMonthPos;
import com.daemin.enumclass.DayOfMonthPosState;
import com.daemin.enumclass.User;
import com.daemin.repository.MyTimeRepo;
import com.daemin.enumclass.PosState;
import com.daemin.enumclass.TimePos;

import java.util.ArrayList;

import timedao.MyTime;


public class Common {

	/* BroadCastReceiver Filter */
	public static final String ACTION_HOME5_5 = "com.daemin.widget.ACTION_HOME5_5";
	public static final String ACTION_DUMMY5_5 = "com.daemin.widget.ACTION_DUMMY5_5";
	public static final String ACTION_WEEK5_5 = "com.daemin.widget.ACTION_WEEK5_5";
	public static final String ACTION_MONTH5_5 = "com.daemin.widget.ACTION_MONTH5_5";
	public static final String ACTION_BACK5_5 = "com.daemin.widget.ACTION_BACK5_5";
	public static final String ACTION_FORWARD5_5 = "com.daemin.widget.ACTION_FORWARD5_5";
	public static final String ACTION_HOME4_4 = "com.daemin.widget.ACTION_HOME4_4";
	public static final String ACTION_DUMMY4_4 = "com.daemin.widget.ACTION_DUMMY4_4";
	public static final String ACTION_WEEK4_4 = "com.daemin.widget.ACTION_WEEK4_4";
	public static final String ACTION_MONTH4_4 = "com.daemin.widget.ACTION_MONTH4_4";
	public static final String ACTION_BACK4_4 = "com.daemin.widget.ACTION_BACK4_4";
	public static final String ACTION_FORWARD4_4 = "com.daemin.widget.ACTION_FORWARD4_4";
	public static final String ALARM_PUSH = "com.daemin.common.ALARM_PUSH";
	public static final String MAIN_COLOR = "#73C8BA";
	public static final String TRANS_COLOR = "#00000000";
	public static boolean firstEnrollFlag = true;
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
		}
		int week_startMonth = Dates.NOW.getMonthWithDayIndex(User.INFO.getStartDay());
		int week_endMonth = Dates.NOW.getMonthWithDayIndex(User.INFO.getEndDay());
		int week_startYear;
		int week_endYear;
		if(week_startMonth==12&&week_endMonth==1){
			week_endYear= Dates.NOW.year;
			week_startYear=week_endYear-1;
		}else
			week_endYear=week_startYear=Dates.NOW.year;

		int week_startDay = Dates.NOW.getDayWithDayIndex(User.INFO.getStartDay());
		int week_endDay = Dates.NOW.getDayWithDayIndex(User.INFO.getEndDay());
		long week_startMillies = Dates.NOW.getDateMillis(week_startYear, week_startMonth, week_startDay, User.INFO.getStartTime(), 0);
		long week_endMillies = Dates.NOW.getDateMillis(week_endYear, week_endMonth, week_endDay, User.INFO.getEndTime(), 0);
		User.INFO.monthData.clear();
		User.INFO.weekData.clear();
		User.INFO.weekData.addAll(MyTimeRepo.getWeekTimes(AppController.getInstance(), week_startMillies, week_endMillies));
		for(MyTime mt :User.INFO.weekData){
			addWeek(mt.getTimetype(),mt.getDayofweek(),mt.getStarthour(),mt.getStartmin(), mt.getEndhour(), mt.getEndmin());
		}
	}
	public static void addWeek(int timeType, int xth, int startHour,int startMin, int endHour, int endMin){
		firstEnrollFlag=true;
		if(endMin!=0) ++endHour;
		else endMin = 60;
		TimePos[] tp = new TimePos[endHour - startHour];
		int j = 0;
		for (int i = startHour; i < endHour; i++) {
			int yth = 0;
			try {
				yth = Convert.HourOfDayToYth(i);

			tp[j] = TimePos.valueOf(Convert.getxyMerge(xth,yth));
			if (tp[j].getPosState() == PosState.NO_PAINT) {
				if (i == startHour) {/*
						if(xth>=2*User.INFO.getStartDay()+1&&xth<=2*User.INFO.getEndDay()+1)
							tp[j].setText(title, place);*/
					if (startMin != 0) tp[j].setMin(startMin, 60);
				}
				if (i == endHour - 1) {
					tp[j].setMin(0, endMin);
				}
				tp[j].setPosState(PosState.ENROLL);
			}else if(tp[j].getPosState() == PosState.ENROLL) {
					if (i == startHour) {
						if (firstEnrollFlag||timeType!=1) {
							//tp[j].setText(title, place);
							if (startMin != 0) tp[j].setMin(startMin, 60);
							firstEnrollFlag = false;
						}
					}
					if (i == endHour - 1) {
						tp[j].setMin(0, endMin);
					} else if (i != startHour) {
						tp[j].setMin(startMin, endMin);
					}
			}
			++j;
			}catch (IllegalArgumentException e){

			}
			catch (NotInException e) {
			}
		}

	}
	public static void fetchMonthData(){
		for (DayOfMonthPos DOMP : DayOfMonthPos.values()) {
			DOMP.setPosState(DayOfMonthPosState.NO_PAINT);
			DOMP.setInitTitle();
		}
		int year = Dates.NOW.year;
		int month = Dates.NOW.month;
		long month_startMillies = Dates.NOW.getDateMillis(year, month, 1, 8, 0);
		long month_endMillies = Dates.NOW.getDateMillis(year, month, Dates.NOW.dayNumOfMonth, 23, 0);
		User.INFO.weekData.clear();
		User.INFO.monthData.clear();
		User.INFO.monthData.addAll(MyTimeRepo.getMonthTimes(AppController.getInstance(), month_startMillies, month_endMillies));
		for (MyTime mt :User.INFO.monthData){
			try {
				addMonth(mt.getName(), mt.getColor(), mt.getDayofweek(), mt.getDayofmonth());
			}catch (NullPointerException e){
				continue;
			}
		}
	}
	public static void addMonth(String title, String color, int xth, int dayOfMonth){
		int dayCnt = dayOfMonth + Dates.NOW.dayOfWeek;
		int yth = dayCnt/7+1;
		xth = Convert.wXthTomXth(xth);
		DayOfMonthPos DOMP = DayOfMonthPos.valueOf(Convert.getxyMergeForMonth(xth, yth));
		if (DOMP.getPosState() == DayOfMonthPosState.NO_PAINT) {
			DOMP.setPosState(DayOfMonthPosState.ENROLL);
			DOMP.setTitleAndColor(title, color);
			DOMP.setEnrollCnt();
		}else if(DOMP.getPosState() == DayOfMonthPosState.ENROLL){
			DOMP.setTitleAndColor(title,color);
			DOMP.setEnrollCnt();
		}
	}
	public static boolean isTableEmpty(){
		boolean empty = true;
		for (TimePos ETP : TimePos.values()) {
			if(ETP.getPosState()!= PosState.NO_PAINT) empty =false;
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
						if(tp.getPosState()== PosState.PAINT) {
							tp.setPosState(PosState.NO_PAINT);
						}else if(tp.getPosState()== PosState.OVERLAP){
							tp.setPosState(PosState.ENROLL);
						}
					}
					break;
				case 1:
					for(String t : tempTimePos){
						DayOfMonthPos.valueOf(t).setPosState(DayOfMonthPosState.NO_PAINT);
					}
					break;
				default:
					break;
			}
		}
		Common.getTempTimePos().clear();
		return;
	}
	public static void registerAlarm(Context context, long requestCode, Long triggerTime, String title,String place, String memo, int timeType)
	{
		Log.i("test registerAlarm", requestCode + "");
		Intent intent = new Intent(context, NotificationReceiver.class);
		intent.setAction(ALARM_PUSH);
		intent.putExtra("title", title);
		intent.putExtra("place", place);
		intent.putExtra("memo",memo);
		PendingIntent sender
				= PendingIntent.getBroadcast(context, (int) requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager manager
				= (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		if(timeType==0)
			manager.set(AlarmManager.RTC_WAKEUP, triggerTime, sender);
		else
			manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, AlarmManager.INTERVAL_DAY*7, sender);
	}
	public static void unregisterAlarm(Context context,long requestCode)
	{
		try {
			Intent intent = new Intent();
			PendingIntent sender
					= PendingIntent.getBroadcast(context, (int) requestCode, intent, 0);
			AlarmManager manager =
					(AlarmManager) context
							.getSystemService(Context.ALARM_SERVICE);
			manager.cancel(sender);
		}
		catch (Exception e){
		}
	}

}
