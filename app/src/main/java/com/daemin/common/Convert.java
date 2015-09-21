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
			default:
				return 0;
		}
	}
}
