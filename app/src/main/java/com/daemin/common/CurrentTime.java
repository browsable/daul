package com.daemin.common;


import android.content.Context;

import com.daemin.timetable.R;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by hernia on 2015-08-07.
 */
public class CurrentTime {
    static LocalDate now = LocalDate.now();
    //년도
    public static Integer getYear() {

        return now.getYear();
    }

    public static Integer getMonthOfYear() {
        return now.getMonthOfYear();
    }

    //일
    public static Integer getDayOfWeek() {
        return now.getDayOfWeek();
    }

    //주
    public static Integer getWeekOfMonth() {
        DateTime dt = new DateTime();
        return dt.getDayOfMonth() % 7;
    }

    //~월 ~째주
    public static String getTitleMonthWeek(Context context) {
        int wow = now.getWeekOfWeekyear();
        int firstWeekOfWeekyear = now.dayOfMonth().withMinimumValue().getWeekOfWeekyear();
        int calWeekOfMonth = wow - firstWeekOfWeekyear + 1;
        if (calWeekOfMonth < 0) calWeekOfMonth = wow + 1;
        else if (now.dayOfMonth().withMinimumValue().getDayOfWeek() == 7 && now.getDayOfWeek()!=7) --calWeekOfMonth;
        return " " + now.getMonthOfYear() + context.getString(R.string.month) + " "
                + calWeekOfMonth + context.getString((R.string.weekofmonth));
    }

    public static Integer preTitleYear(int indexForTitle) {
        return now.plusWeeks(indexForTitle).getYear();
    }

    public static Integer backTitleYear(int indexForTitle) {
        return now.minusWeeks(indexForTitle).getYear();
    }

    public static String preTitleMonthWeek(Context context, int indexForTitle) {
        LocalDate pre = now.plusWeeks(indexForTitle);
        int firstWeekOfWeekyear = pre.dayOfMonth().withMinimumValue().getWeekOfWeekyear();
        int calWeekOfMonth = pre.getWeekOfWeekyear() - firstWeekOfWeekyear + 1;
        if (calWeekOfMonth < 0) calWeekOfMonth = pre.getWeekOfWeekyear() + 1;
        else if (pre.dayOfMonth().withMinimumValue().getDayOfWeek() == 7 && pre.getDayOfWeek()!=7) --calWeekOfMonth;
        return " " + pre.getMonthOfYear() + context.getString(R.string.month) + " "
                + calWeekOfMonth + context.getString((R.string.weekofmonth));
    }

    public static String backTitleMonthWeek(Context context, int indexForTitle) {
        LocalDate back = now.minusWeeks(indexForTitle);
        int firstWeekOfWeekyear = back.dayOfMonth().withMinimumValue().getWeekOfWeekyear();
        int calWeekOfMonth = back.getWeekOfWeekyear() - firstWeekOfWeekyear + 1;
        if (calWeekOfMonth < 0) calWeekOfMonth = back.getWeekOfWeekyear() + 1;
        else if (back.dayOfMonth().withMinimumValue().getDayOfWeek() == 7 && back.getDayOfWeek()!=7) --calWeekOfMonth;
        return " " + back.getMonthOfYear() + context.getString(R.string.month) + " "
                + calWeekOfMonth + context.getString((R.string.weekofmonth));
    }

    public static String getYMD() {
        org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("d MMMM, yyyy");
        return now.toString(fmt);
    }

}
