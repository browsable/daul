package com.daemin.timetable.common;

import java.util.*;

public class Convert {

	public static int DayOfWeekToXth(int DayOfWeekIndex) {
		switch (DayOfWeekIndex) {
		case 1:// 일요일
			return Integer.parseInt("13");
		case 2:// 월요일
			return Integer.parseInt("01", 10);
		case 3:// 화요일
			return Integer.parseInt("03", 10);
		case 4:// 수요일
			return Integer.parseInt("05", 10);
		case 5:// 목요일
			return Integer.parseInt("07", 10);
		case 6:// 금요일
			return Integer.parseInt("09", 10);
		case 7:// 토요일
			return Integer.parseInt("11");
		default:
			return 0;
		}
	}

	public static int HourOfDayToYth(String HourOfDay) {
		switch (Integer.parseInt(HourOfDay)) {
		case 8:
			return Integer.parseInt("01", 10);
		case 9:
			return Integer.parseInt("03", 10);
		case 10:
			return Integer.parseInt("05", 10);
		case 11:
			return Integer.parseInt("07", 10);
		case 12:
			return Integer.parseInt("09", 10);
		case 13:
			return Integer.parseInt("11");
		case 14:
			return Integer.parseInt("13");
		case 15:
			return Integer.parseInt("15");
		case 16:
			return Integer.parseInt("17");
		case 17:
			return Integer.parseInt("19");
		case 18:
			return Integer.parseInt("21");
		case 19:
			return Integer.parseInt("23");
		case 20:
			return Integer.parseInt("25");
		case 21:
			return Integer.parseInt("27");
		case 22:
			return Integer.parseInt("29");
		default:
			return 0;
		}
	}

	public static String getxyMerge(int Xth, int Yth) {
		String x, y;
		if (Xth < 10) {
			x =  String.format("%02d",Xth);
		}else{
			x = String.valueOf(Xth);
		}
		if (Yth < 10) {
			y =  String.format("%02d",Yth);
		}else{
			y = String.valueOf(Yth);
		}
		return "P"+x+y;
	}

	public static String IndexToDayOfWeek(int DayOfWeekIndex) {
		switch (DayOfWeekIndex) {
		case 1: // 일요일
			return "일";
		case 2: // 월요일
			return "월";
		case 3: // 화요일
			return "화";
		case 4: // 수요일
			return "수";
		case 5: // 목요일
			return "목";
		case 6: // 금요일
			return "금";
		case 7: // 토요일
			return "토";
		default:
			return "";
		}
	}
	public static int DayOfWeekToIndex(String DayOfWeek) {
		switch (DayOfWeek) {
		case "일": // 일요일
			return 1;
		case "월": // 월요일
			return 2;
		case "화": // 화요일
			return 3;
		case "수": // 수요일
			return 4;
		case "목": // 목요일
			return 5;
		case "금": // 금요일
			return 6;
		case "토": // 토요일
			return 7;
		default:
			return 0;
		}
	}

	public static String IntAddO(int i) {
		if (i < 10) {
			return String.format("%02d",i);
		}
		return String.valueOf(i);
	}
	public static String getDayofWeek(int year, int monthOfYear, int dayOfMonth){
		Calendar cal= Calendar.getInstance ();
	    cal.set(Calendar.YEAR, year);
	    cal.set(Calendar.MONTH, monthOfYear);
	    cal.set(Calendar.DATE, dayOfMonth);
	    cal.get(Calendar.DAY_OF_WEEK);
	    return Convert.IndexToDayOfWeek(cal.get(Calendar.DAY_OF_WEEK));
	}
	public static String XthToDayOfWeek(int Xth) {
		switch (Xth) {
		case 13:// 일요일
			return "일";
		case 1:// 월요일
			return "월";
		case 3:// 화요일
			return "화";
		case 5:// 수요일
			return "수";
		case 7:// 목요일
			return "목";
		case 9:// 금요일
			return "금";
		case 11:// 토요일
			return "토";
		default:
			return "";
		}
	}
	public static String YthToHourOfDay(int Yth){
		switch (Yth) {
		case 1:
			return "8";
		case 3:
			return "9";
		case 5:
			return "10";
		case 7:
			return "11";
		case 9:
			return "12";
		case 11:
			return "13";
		case 13:
			return "14";
		case 15:
			return "15";
		case 17:
			return "16";
		case 19:
			return "17";
		case 21:
			return "18";
		case 23:
			return "19";
		case 25:
			return "20";
		case 27:
			return "21";
		case 29:
			return "22";
		default:
			return "";
		}
	}
}
