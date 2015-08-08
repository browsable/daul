package com.daemin.common;


import android.content.Context;

import com.daemin.timetable.DateOfWeekData;
import com.daemin.timetable.R;

import org.joda.time.LocalDate;

/**
 * Created by hernia on 2015-08-07.
 */
public class CurrentTime {
    static LocalDate now = LocalDate.now();
    //static LocalDate now = new LocalDate(2015,8,9);
    static DateOfWeekData dowd = new DateOfWeekData();
    static LocalDate fst; //해당날짜의 첫째 date
    public static Integer getYear() {
        return now.getYear();
    }
    public static LocalDate getLastDayOfWeek(){//토요일
        return now.weekOfWeekyear().roundCeilingCopy().minusDays(2);
    }
    public static DateOfWeekData getDateOfWeek() {
        if(now.getDayOfWeek()==7) fst = now.plusWeeks(1).weekOfWeekyear().roundFloorCopy().minusDays(1);
        else fst = now.weekOfWeekyear().roundFloorCopy().minusDays(1);
       dowd.setAllDate(
               fst.getMonthOfYear()+"/"+fst.getDayOfMonth(),
               fst.plusDays(1).getMonthOfYear() +"/" +fst.plusDays(1).getDayOfMonth(),
               fst.plusDays(2).getMonthOfYear() +"/" +fst.plusDays(2).getDayOfMonth(),
               fst.plusDays(3).getMonthOfYear() +"/" +fst.plusDays(3).getDayOfMonth(),
               fst.plusDays(4).getMonthOfYear() +"/" +fst.plusDays(4).getDayOfMonth(),
               fst.plusDays(5).getMonthOfYear() +"/" +fst.plusDays(5).getDayOfMonth(),
               fst.plusDays(6).getMonthOfYear() +"/" +fst.plusDays(6).getDayOfMonth()
        );
        return dowd;
    }
    public static DateOfWeekData getPreDateOfWeek(int indexForTitle) {
        LocalDate pre = fst.plusWeeks(indexForTitle);
        dowd.setAllDate(
                pre.getMonthOfYear() + "/" + pre.getDayOfMonth(),
                pre.plusDays(1).getMonthOfYear() + "/" + pre.plusDays(1).getDayOfMonth(),
                pre.plusDays(2).getMonthOfYear() + "/" + pre.plusDays(2).getDayOfMonth(),
                pre.plusDays(3).getMonthOfYear() + "/" + pre.plusDays(3).getDayOfMonth(),
                pre.plusDays(4).getMonthOfYear() + "/" + pre.plusDays(4).getDayOfMonth(),
                pre.plusDays(5).getMonthOfYear() + "/" + pre.plusDays(5).getDayOfMonth(),
                pre.plusDays(6).getMonthOfYear() + "/" + pre.plusDays(6).getDayOfMonth()
        );
        return dowd;
    }
    public static DateOfWeekData getBackDateOfWeek(int indexForTitle) {
        LocalDate back = fst.minusWeeks(indexForTitle);
        dowd.setAllDate(
                back.getMonthOfYear() + "/" + back.getDayOfMonth(),
                back.plusDays(1).getMonthOfYear() + "/" + back.plusDays(1).getDayOfMonth(),
                back.plusDays(2).getMonthOfYear() + "/" + back.plusDays(2).getDayOfMonth(),
                back.plusDays(3).getMonthOfYear() + "/" + back.plusDays(3).getDayOfMonth(),
                back.plusDays(4).getMonthOfYear() + "/" + back.plusDays(4).getDayOfMonth(),
                back.plusDays(5).getMonthOfYear() + "/" + back.plusDays(5).getDayOfMonth(),
                back.plusDays(6).getMonthOfYear() + "/" + back.plusDays(6).getDayOfMonth()
        );
        return dowd;
    }
    public static Integer getDayOfWeek() {
        return now.getDayOfWeek();
    }
    public static Integer preTitleYear(int indexForTitle) {
        return now.plusWeeks(indexForTitle).getYear();
    }
    public static Integer backTitleYear(int indexForTitle) {
        return now.minusWeeks(indexForTitle).getYear();
    }
    public static String getTitleMonthWeek(Context context) {
        int lastDayofWeek = getLastDayOfWeek().getWeekOfWeekyear(); //이번주의 토요일의 년대비 주차
        int firstDayOfMonth=0;
        if(now.dayOfMonth().withMinimumValue().getDayOfWeek()==7) firstDayOfMonth = now.dayOfMonth().withMinimumValue().plusDays(2).getWeekOfWeekyear();
        else firstDayOfMonth = now.dayOfMonth().withMinimumValue().getWeekOfWeekyear(); //이번달 첫날 토요일의 주차
        int calWeekOfMonth = lastDayofWeek-firstDayOfMonth+1;
        if(calWeekOfMonth<0) calWeekOfMonth = lastDayofWeek+1;
        return " " + getLastDayOfWeek().getMonthOfYear() + context.getString(R.string.month) + " "
                + calWeekOfMonth + context.getString((R.string.weekofmonth));
    }
    public static String preTitleMonthWeek(Context context, int indexForTitle) {
        int lastDayofWeek = getLastDayOfWeek().plusWeeks(indexForTitle).getWeekOfWeekyear(); //다음 토요일의 년대비 주차
        int firstDayOfMonth=0;
        if(now.plusWeeks(indexForTitle).dayOfMonth().withMinimumValue().getDayOfWeek()==7) firstDayOfMonth = now.plusWeeks(indexForTitle).dayOfMonth().withMinimumValue().plusDays(2).getWeekOfWeekyear();
        else firstDayOfMonth = now.plusWeeks(indexForTitle).dayOfMonth().withMinimumValue().getWeekOfWeekyear(); //이번달 첫날 토요일의 주차
        int calWeekOfMonth = lastDayofWeek-firstDayOfMonth+1;
        if(calWeekOfMonth<0) calWeekOfMonth = lastDayofWeek+1;
        return " " + getLastDayOfWeek().plusWeeks(indexForTitle).getMonthOfYear() + context.getString(R.string.month) + " "
                + calWeekOfMonth + context.getString((R.string.weekofmonth));
    }
    public static String backTitleMonthWeek(Context context, int indexForTitle) {
        int lastDayofWeek = getLastDayOfWeek().minusWeeks(indexForTitle).getWeekOfWeekyear(); //다음 토요일의 년대비 주차
        int firstDayOfMonth=0;
        if(now.minusWeeks(indexForTitle).dayOfMonth().withMinimumValue().getDayOfWeek()==7) firstDayOfMonth = now.minusWeeks(indexForTitle).dayOfMonth().withMinimumValue().plusDays(2).getWeekOfWeekyear();
        else firstDayOfMonth = now.minusWeeks(indexForTitle).dayOfMonth().withMinimumValue().getWeekOfWeekyear();
        int calWeekOfMonth = lastDayofWeek - firstDayOfMonth + 1;
        if(calWeekOfMonth<0) calWeekOfMonth = lastDayofWeek+1;
        return " " + getLastDayOfWeek().minusWeeks(indexForTitle).getMonthOfYear() + context.getString(R.string.month) + " "
                + calWeekOfMonth + context.getString((R.string.weekofmonth));
    }
}
