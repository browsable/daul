package com.daemin.common;

public class Convert {

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
	public static String getxyMergeForMonth(int Xth, int Yth) {
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
		return "M"+x+y;
	}

	public static String IntToString(int i) {
		if (i < 10) {
			return String.format("%02d",i);
		}
		return String.valueOf(i);
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
		case 31:
			return "23";
		default:
			return "";
		}
	}
	public static int HourOfDayToYth(int HourOfDay){
		switch (HourOfDay) {
			case 8:
				return 1;
			case 9:
				return 3;
			case 10:
				return 5;
			case 11:
				return 7;
			case 12:
				return 9;
			case 13:
				return 11;
			case 14:
				return 13;
			case 15:
				return 15;
			case 16:
				return 17;
			case 17:
				return 19;
			case 18:
				return 21;
			case 19:
				return 23;
			case 20:
				return 25;
			case 21:
				return 27;
			case 22:
				return 29;
			case 23:
				return 31;
			default:
				return 0;
		}
	}
	public static String indexOfGrade(String grade) {
		String index ="0";
		switch (grade) {
			case "학년 : 전체":
				break;
			case "1학년":
				index="1";
				break;
			case "2학년":
				index="2";
				break;
			case "3학년":
				index="3";
				break;
			case "4학년":
				index="4";
				break;
		}
		return index;
	}
	public static String XthToDayOfWeek(int  xth) {
		switch (xth) {
			case 1:
				return "일요일";
			case 3:
				return "월요일";
			case 5:
				return "화요일";
			case 7:
				return "수요일";
			case 9:
				return "목요일";
			case 11:
				return "금요일";
			case 13:
				return "토요일";
			default:
				return "";
		}
	}
	public static String XthToDayOfWeekInMonth(int  xth) {
		switch (xth) {
			case 1:
				return "일요일";
			case 2:
				return "월요일";
			case 3:
				return "화요일";
			case 4:
				return "수요일";
			case 5:
				return "목요일";
			case 6:
				return "금요일";
			case 7:
				return "토요일";
			default:
				return "";
		}
	}
	public static int dayOfWeekTowXth(int dayOfWeek) {
		int wXth=0;
		switch (dayOfWeek) {
			case 1:
				wXth = 3;
				break;
			case 2:
				wXth = 5;
				break;
			case 3:
				wXth = 7;
				break;
			case 4:
				wXth = 9;
				break;
			case 5:
				wXth = 11;
				break;
			case 6:
				wXth = 13;
				break;
			case 7:
				wXth = 1;
				break;
		}
		return wXth;
	}
	public static int dayOfWeekTomXth(int dayOfWeek) {
		int mXth=0;
		switch (dayOfWeek) {
			case 1:
				mXth = 2;
				break;
			case 2:
				mXth = 3;
				break;
			case 3:
				mXth = 4;
				break;
			case 4:
				mXth = 5;
				break;
			case 5:
				mXth = 6;
				break;
			case 6:
				mXth = 7;
				break;
			case 7:
				mXth = 1;
				break;
		}
		return mXth;
	}
	public static int wXthTomXth(int wXth) {
		int mXth=0;
		switch (wXth) {
			case 1:
				mXth = 1;
				break;
			case 3:
				mXth = 2;
				break;
			case 5:
				mXth = 3;
				break;
			case 7:
				mXth = 4;
				break;
			case 9:
				mXth = 5;
				break;
			case 11:
				mXth = 6;
				break;
			case 13:
				mXth = 7;
				break;
		}
		return mXth;
	}
	public static int Share(String grade) {
		int index=0;
		switch (grade) {
			case "비공개":
				index = 0;
				break;
			case "친구공개":
				index=1;
				break;
			case "전체공개":
				index=2;
				break;
		}
		return index;
	}
	public static String revertShare(int index) {
		String share="";
		switch (index) {
			case 0:
				share = "비공개";
				break;
			case 1:
				share="친구공개";
				break;
			case 2:
				share="전체공개";
				break;
		}
		return share;
	}
	public static long Alarm(long startmillis , String grade) {
		long alarmMillis=0;
		switch (grade) {
			case "알람 없음":
				alarmMillis=0;
				break;
			case "정각":
				alarmMillis=startmillis;
				break;
			case "10분 전":
				alarmMillis=startmillis-10*60*1000;
				break;
			case "20분 전":
				alarmMillis=startmillis-20*60*1000;
				break;
			case "30분 전":
				alarmMillis=startmillis-30*60*1000;
				break;
			case "1시간 전":
				alarmMillis=startmillis-60*60*1000;
				break;
			case "1일 전":
				alarmMillis=startmillis-24*60*60*1000;
				break;
		}
		return alarmMillis;
	}
	public static int onlyNum(String str) {
		if ( str == null ) return 0;

		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < str.length(); i++){
			if( Character.isDigit( str.charAt(i) ) ) {
				sb.append( str.charAt(i) );
			}
		}
		return Integer.parseInt(sb.toString());
	}
}

