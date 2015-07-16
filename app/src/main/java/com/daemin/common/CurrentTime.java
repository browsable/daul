package com.daemin.common;

import android.content.Context;

import com.daemin.timetable.R;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.WeekFields;

import java.util.Locale;

public class CurrentTime{
	//년도
	public static Integer getYear() {
		LocalDate now = LocalDate.now();
		return now.getYear();
	}
	//월
	public static Integer getMonthOfYear() {
		LocalDate now = LocalDate.now();
		return now.getMonth().getValue();
	}
	//일
	public static Integer getDayOfWeek() {
		LocalDate now = LocalDate.now();
		return now.getDayOfWeek().getValue();
	}
	//주
	public static Integer getWeekOfMonth() {
		LocalDate now = LocalDate.now();
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		return now.get(weekFields.weekOfMonth());
	}
	//~월 ~째주
	public static String getTitleMonthWeek(Context context) {
		LocalDate now = LocalDate.now();
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		return " "+String.valueOf(now.getMonth().getValue())+ context.getString(R.string.month)+" "
				+ String.valueOf(now.get(weekFields.weekOfMonth()))+ context.getString((R.string.weekofmonth));
	}
	public static Integer preTitleYear(Context context, int indexForTitle) {
		LocalDate now = LocalDate.now();
		return now.plusWeeks(indexForTitle).getYear();
	}
	public static Integer backTitleYear(Context context, int indexForTitle) {
		LocalDate now = LocalDate.now();
		return now.minusWeeks(indexForTitle).getYear();
	}
	public static String preTitleMonthWeek(Context context, int indexForTitle) {
		LocalDate now = LocalDate.now();
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		return " "+String.valueOf(now.plusWeeks(indexForTitle).getMonth().getValue())+ context.getString(R.string.month)+" "
				+ String.valueOf(now.plusWeeks(indexForTitle).get(weekFields.weekOfMonth()))+ context.getString((R.string.weekofmonth));
	}
	public static String backTitleMonthWeek(Context context, int indexForTitle) {
		LocalDate now = LocalDate.now();
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		return " "+String.valueOf(now.minusWeeks(indexForTitle).getMonth().getValue())+ context.getString(R.string.month)+" "
				+ String.valueOf(now.minusWeeks(indexForTitle).get(weekFields.weekOfMonth()))+ context.getString((R.string.weekofmonth));
	}
	public static String getYMD() {
		LocalDate now = LocalDate.now();
		return DateTimeFormatter.ofPattern("yyyy.MM.dd").format(now);
	}

}
