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
        return dt.getDayOfMonth()%7;
    }

    //~월 ~째주
    public static String getTitleMonthWeek(Context context) {
        int firstWeekOfWeekyear = now.dayOfMonth().withMinimumValue().getWeekOfWeekyear();
        int calWeekOfMonth = now.getWeekOfWeekyear()-firstWeekOfWeekyear+1;
        if(calWeekOfMonth<0) calWeekOfMonth=now.getWeekOfWeekyear()+1;
        return  " "+now.getMonthOfYear()+ context.getString(R.string.month)+" "
                +  calWeekOfMonth + context.getString((R.string.weekofmonth));
    }
    public static Integer preTitleYear(int indexForTitle) {
        return now.plusWeeks(indexForTitle).getYear();
    }
    public static Integer backTitleYear(int indexForTitle) {
        return now.minusWeeks(indexForTitle).getYear();
    }
    public static String preTitleMonthWeek(Context context, int indexForTitle) {
        int firstWeekOfWeekyear = now.plusWeeks(indexForTitle).dayOfMonth().withMinimumValue().getWeekOfWeekyear();
        int calWeekOfMonth = now.plusWeeks(indexForTitle).getWeekOfWeekyear()-firstWeekOfWeekyear+1;
        if(calWeekOfMonth<0) calWeekOfMonth=now.plusWeeks(indexForTitle).getWeekOfWeekyear()+1;
        return  " "+now.plusWeeks(indexForTitle).getMonthOfYear()+ context.getString(R.string.month)+" "
                +  calWeekOfMonth + context.getString((R.string.weekofmonth));
    }
    public static String backTitleMonthWeek(Context context, int indexForTitle) {
        int firstWeekOfWeekyear = now.minusWeeks(indexForTitle).dayOfMonth().withMinimumValue().getWeekOfWeekyear();
        int calWeekOfMonth = now.minusWeeks(indexForTitle).getWeekOfWeekyear()-firstWeekOfWeekyear+1;
        if(calWeekOfMonth<0) calWeekOfMonth=now.minusWeeks(indexForTitle).getWeekOfWeekyear()+1;
        return  " "+now.minusWeeks(indexForTitle).getMonthOfYear()+ context.getString(R.string.month)+" "
                +  calWeekOfMonth + context.getString((R.string.weekofmonth));
    }
    public static String getYMD() {
        org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("d MMMM, yyyy");
        return now.toString(fmt);
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
