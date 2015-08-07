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

    //년도
    public static Integer getYear() {
        LocalDate now = LocalDate.now();
        return now.getYear();
    }
    public static Integer getMonthOfYear() {
        LocalDate now = LocalDate.now();
        return now.getMonthOfYear();
    }
    //일
    public static Integer getDayOfWeek() {
        LocalDate now = LocalDate.now();
        return now.getDayOfWeek();
    }
    //주
    public static Integer getWeekOfMonth() {
        DateTime dt = new DateTime();
        return dt.getDayOfMonth()%7;
    }

    //~월 ~째주
    public static String getTitleMonthWeek(Context context) {
        DateTime dt = new DateTime();
        int firstWeekOfWeekyear = dt.dayOfMonth().withMinimumValue().getWeekOfWeekyear();
        int calWeekOfMonth = dt.getWeekOfWeekyear()-firstWeekOfWeekyear+1;
        if(calWeekOfMonth<0) calWeekOfMonth+=53;
        return  " "+dt.getMonthOfYear()+ context.getString(R.string.month)+" "
                +  calWeekOfMonth + context.getString((R.string.weekofmonth));
    }
    public static Integer preTitleYear(int indexForTitle) {
        LocalDate now = LocalDate.now();
        return now.plusWeeks(indexForTitle).getYear();
    }
    public static Integer backTitleYear(int indexForTitle) {
        LocalDate now = LocalDate.now();
        return now.minusWeeks(indexForTitle).getYear();
    }
    public static String preTitleMonthWeek(Context context, int indexForTitle) {
        DateTime dt = new DateTime();
        int firstWeekOfWeekyear = dt.plusWeeks(indexForTitle).dayOfMonth().withMinimumValue().getWeekOfWeekyear();
        int calWeekOfMonth = dt.plusWeeks(indexForTitle).getWeekOfWeekyear()-firstWeekOfWeekyear+1;
        if(calWeekOfMonth<0) calWeekOfMonth+=53;
        return  " "+dt.plusWeeks(indexForTitle).getMonthOfYear()+ context.getString(R.string.month)+" "
                +  calWeekOfMonth + context.getString((R.string.weekofmonth));
    }
    public static String backTitleMonthWeek(Context context, int indexForTitle) {
        DateTime dt = new DateTime();
        int firstWeekOfWeekyear = dt.minusWeeks(indexForTitle).dayOfMonth().withMinimumValue().getWeekOfWeekyear();
        int calWeekOfMonth = dt.minusWeeks(indexForTitle).getWeekOfWeekyear()-firstWeekOfWeekyear+1;
        if(calWeekOfMonth<0) calWeekOfMonth+=53;
        return  " "+dt.minusWeeks(indexForTitle).getMonthOfYear()+ context.getString(R.string.month)+" "
                +  calWeekOfMonth + context.getString((R.string.weekofmonth));
    }
    public static String getYMD() {
        LocalDate date = LocalDate.now();
        org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("d MMMM, yyyy");
        return date.toString(fmt);
    }

    /*public void shouldGetAfterOneDay() {
        Chronology chrono = GregorianChronology.getInstance();
        LocalDate theDay = new LocalDate(1582, 10, 4, chrono);
        String pattern = "yyyy.MM.dd";

        LocalDate nextDay = theDay.plusDays(1);
    }

    public void shouldGetAfterOneDayWithGJChronology() {
        Chronology chrono = GJChronology.getInstance();
        LocalDate theDay = new LocalDate(1582, 10, 4, chrono);
        String pattern = "yyyy.MM.dd";

        LocalDate nextDay = theDay.plusDays(1);
    }

    public void shouldGetAfterOneHour() {
        DateTimeZone seoul = DateTimeZone.forID("Asia/Seoul");
        DateTime theTime = new DateTime(1988,5,7,23,0, seoul);
        String pattern = "yyyy.MM.dd HH:mm";

        DateTime after1Hour = theTime.plusHours(1);
    }

    public void shouldGetAfterOneMinute() {
        DateTimeZone seoul = DateTimeZone.forID("Asia/Seoul");
        DateTime theTime = new DateTime(1961, 8, 9, 23, 59, seoul);
        String pattern = "yyyy.MM.dd HH:mm";

        DateTime after1Minute = theTime.plusMinutes(1);
    }

    public void shouldGetAfterTwoSecond() {
        DateTimeZone utc = DateTimeZone.forID("UTC");
        DateTime theTime = new DateTime(2012, 6, 30, 23, 59, 59, utc);
        String pattern = "yyyy.MM.dd HH:mm:ss";

        DateTime after2Seconds = theTime.plusSeconds(2);
    }

    public void shouldThrowExceptionWhenWrongTimeZoneId(){
        DateTimeZone.forID("Seoul/Asia");
    }

    public void shouldGetDate() {
        LocalDate theDay = new LocalDate(1999, 12, 31);

    }

    public void shouldNotAcceptWrongMonth() {
        new LocalDate(1999, 13, 31);
    }

    public void shouldGetDayOfWeek() {
        LocalDate theDay = new LocalDate(2014, 1, 1);

        int dayOfWeek = theDay.getDayOfWeek();
    }*/
}
