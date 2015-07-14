package com.daemin.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CurrentTime{
	// 현재 시간을 msec으로 구한다.
			private long now = System.currentTimeMillis();
			// 현재 시간을 저장 한다.
			private Date date = new Date(now);

			// 시간 포맷 지정
			private SimpleDateFormat CurYearFormat = new SimpleDateFormat("yyyy");
			private SimpleDateFormat CurMonthFormat = new SimpleDateFormat("MM");
			private SimpleDateFormat CurDayFormat = new SimpleDateFormat("dd");
			private SimpleDateFormat CurHourFormat = new SimpleDateFormat("HH");
			private SimpleDateFormat CurMinFormat = new SimpleDateFormat("mm");

			private String CurYear =CurYearFormat.format(date);
			private String CurMonth = CurMonthFormat.format(date);
			private String CurDay = CurDayFormat.format(date);
			private String CurHour = CurHourFormat.format(date); 
			private String CurMin =  CurMinFormat.format(date);
			private Calendar oCalendar = Calendar.getInstance();
			private int DayOfWeekIndex = oCalendar.get(Calendar.DAY_OF_WEEK);
			private int CurAMPM = oCalendar.get(Calendar.AM_PM);
			private String DayOfWeek = "";//요일

			public int getCurAMPM() {return CurAMPM;}
			public SimpleDateFormat getCurYearFormat() {
				return CurYearFormat;
			}
			public SimpleDateFormat getCurMonthFormat() {
				return CurMonthFormat;
			}
			public SimpleDateFormat getCurDayFormat() {
				return CurDayFormat;
			}
			public String getCurYear() {
				return CurYear;
			}
			public String getCurMonth() {
				return CurMonth;
			}
			public String getCurDay() {
				return CurDay;
			}
			public int getDayOfWeekIndex() {
				return DayOfWeekIndex;
			}
			public String getCurHour() {
				return CurHour;
			}
			public String getCurMin() {
				return CurMin;
			}
}
