package com.daemin.common;


import android.content.Context;

import com.daemin.data.DayOfWeekData;
import com.daemin.enumclass.User;
import com.daemin.timetable.R;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by hernia on 2015-08-07.
 */
public class CurrentTime {
    //static LocalDate now = new LocalDate(2015,11,29);
    static LocalDate fst; //해당날짜의 첫째 date
    public static String getToday(){
        return LocalDate.now().getMonthOfYear()+"/"+LocalDate.now().getDayOfMonth();
    }
    public static long getNowMillis() {
        return new DateTime().getMillis();
    }
    public static long getDateMillis(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour) {
        DateTime dt = new DateTime(year,monthOfYear,dayOfMonth,hourOfDay,minuteOfHour);
        return dt.getMillis();
    }
    public static long getMidnight(){
        return new DateTime().plusDays(1).withTimeAtStartOfDay().getMillis();
    }
    public static String getDatefromMillis(long millis){
        DateTime now = new DateTime(millis);
        String pattern = "yyyy-MM-dd hh.mm.ss";
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        String formatted = formatter.print(now);
        return formatted;
    }
    public static String getMD(){
        DateTime dt = DateTime.now();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("MM.dd HH:mm");
        return fmt.print(dt);
    }
    public static int getDayOfWeek() {
        int dayOfWeek = LocalDate.now().getDayOfWeek();
        if(dayOfWeek==7) dayOfWeek=0;
        return dayOfWeek;
    }
    public static int getDayOfMonth(){ return LocalDate.now().getDayOfMonth();}
    public static LocalDate getLastDayOfWeek(){
        if(LocalDate.now().getDayOfWeek()==7) return LocalDate.now().plusDays(1).withDayOfWeek(6);
        else return LocalDate.now().withDayOfWeek(6);
    }

    //이달의 첫 날이 속한 토요일
    public static LocalDate getFirstDayOfWeekYear(){
        if(getLastDayOfWeek().withDayOfMonth(1).getDayOfWeek()==7) return getLastDayOfWeek().withDayOfMonth(1).plusDays(1).withDayOfWeek(6);
        return getLastDayOfWeek().withDayOfMonth(1).withDayOfWeek(6);
    }

    public static LocalDate getPlusDayOfWeek(int plus){
        if(LocalDate.now().getDayOfWeek()==7)  return LocalDate.now().plusDays(1).plusWeeks(plus).withDayOfWeek(6);
        else return LocalDate.now().plusWeeks(plus).withDayOfWeek(6);
    }
    public static LocalDate getPlusFirstDayOfWeek(int plus){
        if(getPlusDayOfWeek(plus).withDayOfMonth(1).getDayOfWeek()==7) return getPlusDayOfWeek(plus).withDayOfMonth(1).plusDays(1).withDayOfWeek(6);
        else return getPlusDayOfWeek(plus).withDayOfMonth(1).withDayOfWeek(6);
    }

    public static LocalDate getMinusDayOfWeek(int plus){
        if(LocalDate.now().getDayOfWeek()==7)  return LocalDate.now().plusDays(1).minusWeeks(plus).withDayOfWeek(6);
        else return LocalDate.now().minusWeeks(plus).withDayOfWeek(6);
    }
    public static LocalDate getMinusFirstDayOfWeek(int minus){
        if(getMinusDayOfWeek(minus).withDayOfMonth(1).getDayOfWeek()==7) return getMinusDayOfWeek(minus).withDayOfMonth(1).plusDays(1).withDayOfWeek(6);
        else return getMinusDayOfWeek(minus).withDayOfMonth(1).withDayOfWeek(6);
    }
    public static DayOfWeekData getDateOfWeek() {
        if(LocalDate.now().getDayOfWeek()==7) fst = LocalDate.now().plusWeeks(1).weekOfWeekyear().roundFloorCopy().minusDays(1);
        else fst = LocalDate.now().weekOfWeekyear().roundFloorCopy().minusDays(1);
        DayOfWeekData dowd = new DayOfWeekData();
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
    public static DayOfWeekData getPreDateOfWeek(int indexForTitle) {
        LocalDate pre = fst.plusWeeks(indexForTitle);
        DayOfWeekData dowd = new DayOfWeekData();
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
    public static DayOfWeekData getBackDateOfWeek(int indexForTitle) {
        LocalDate back = fst.minusWeeks(indexForTitle);
        DayOfWeekData dowd = new DayOfWeekData();
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
    public static String getTitleYearMonth(Context context) {
        int year = getYear();
        int month = LocalDate.now().getMonthOfYear();
        User.INFO.setYear(year);
        User.INFO.setMonth(month);
        return " " + year + context.getString(R.string.year) + " "
                + month + context.getString((R.string.month));
    }
    public static String preTitleYearMonth(Context context, int indexForTitle) {
        int year = getLastDayOfWeek().plusMonths(indexForTitle).getYear();
        int titleMonth = LocalDate.now().plusMonths(indexForTitle).getMonthOfYear();
        User.INFO.setYear(year);
        User.INFO.setMonth(titleMonth);
        return " " + year + context.getString(R.string.year) + " "
                + titleMonth + context.getString((R.string.month));
    }
    public static String backTitleYearMonth(Context context, int indexForTitle) {
        int year = getLastDayOfWeek().minusMonths(indexForTitle).getYear();
        int titleMonth = LocalDate.now().minusMonths(indexForTitle).getMonthOfYear();
        User.INFO.setYear(year);
        User.INFO.setMonth(titleMonth);
        return " " + year + context.getString(R.string.year) + " "
                + titleMonth + context.getString((R.string.month));
    }
    public static int getYear() {
        int year = getLastDayOfWeek().getYear();
        User.INFO.setYear(year);
        return year;
    }
    public static Integer preTitleYear(int indexForTitle) {
        int year = getLastDayOfWeek().plusWeeks(indexForTitle).getYear();
        User.INFO.setYear(year);
        return year;
    }
    public static Integer backTitleYear(int indexForTitle) {
        int year = getLastDayOfWeek().minusWeeks(indexForTitle).getYear();
        User.INFO.setYear(year);
        return year;
    }
    public static String getTitleMonthWeek(Context context) {
        int lastDayofWeek = getLastDayOfWeek().getWeekOfWeekyear(); //이번주의 토요일의 년대비 주차
        int firstDayOfMonth = getFirstDayOfWeekYear().getWeekOfWeekyear();
        int calWeekOfMonth = lastDayofWeek-firstDayOfMonth+1;
        if(calWeekOfMonth<0) calWeekOfMonth = lastDayofWeek+1;
        int month = getLastDayOfWeek().getMonthOfYear();
        User.INFO.setMonth(month);
        return " " + month + context.getString(R.string.month) + " "
                + calWeekOfMonth + context.getString((R.string.weekofmonth));
    }
    public static String preTitleMonthWeek(Context context, int indexForTitle) {
        int lastDayofWeek = getPlusDayOfWeek(indexForTitle).getWeekOfWeekyear(); //이번주의 토요일의 년대비 주차
        int firstDayOfMonth = getPlusFirstDayOfWeek(indexForTitle).getWeekOfWeekyear();
        int calWeekOfMonth = lastDayofWeek-firstDayOfMonth+1;
        if(calWeekOfMonth < 0) calWeekOfMonth = lastDayofWeek+1;
        int month = getPlusDayOfWeek(indexForTitle).getMonthOfYear();
        User.INFO.setMonth(month);
        return " " + month + context.getString(R.string.month) + " "
                + calWeekOfMonth + context.getString((R.string.weekofmonth));
    }
    public static String backTitleMonthWeek(Context context, int indexForTitle) {
        int lastDayofWeek = getMinusDayOfWeek(indexForTitle).getWeekOfWeekyear(); //이번주의 토요일의 년대비 주차
        int firstDayOfMonth = getMinusFirstDayOfWeek(indexForTitle).getWeekOfWeekyear();
        int calWeekOfMonth = lastDayofWeek-firstDayOfMonth+1;
        if(calWeekOfMonth<0) calWeekOfMonth = lastDayofWeek+1;
        int month = getMinusDayOfWeek(indexForTitle).getMonthOfYear();
        User.INFO.setMonth(month);
        return " " + month + context.getString(R.string.month) + " "
                + calWeekOfMonth + context.getString((R.string.weekofmonth));
    }

    //month
    public static String[] getDayOfLastMonth(){
        String monthData[] = new String[42];
        LocalDate firstDay = LocalDate.now().minusMonths(1).dayOfMonth().withMaximumValue().withDayOfWeek(1).minusDays(1);
        for(int i =0; i<42; i++){
            monthData[i] = String.valueOf(firstDay.plusDays(i).getDayOfMonth());
        }
        return monthData;
    }
    public static int getDayOfWeekOfLastMonth(){
        int dayOfWeek = LocalDate.now().minusMonths(1).dayOfMonth().withMaximumValue().getDayOfWeek();
        return dayOfWeek;
    }

    public static int getDayNumOfMonth(){
        return LocalDate.now().dayOfMonth().withMaximumValue().getDayOfMonth();
    }
    //pre
    public static String[] getPreDayOfLastMonth(int indexForTitle){
        String monthData[] = new String[42];
        LocalDate firstDay = LocalDate.now().plusMonths(indexForTitle).minusMonths(1).dayOfMonth().withMaximumValue().withDayOfWeek(1).minusDays(1);
        for(int i =0; i<42; i++){
            monthData[i] = String.valueOf(firstDay.plusDays(i).getDayOfMonth());
        }
        return monthData;
    }
    public static int getPreDayOfWeekOfLastMonth(int indexForTitle){
        int dayOfWeek = LocalDate.now().plusMonths(indexForTitle).minusMonths(1).dayOfMonth().withMaximumValue().getDayOfWeek();
        return dayOfWeek;
    }
    public static int getPreDayNumOfMonth(int indexForTitle){
        return LocalDate.now().plusMonths(indexForTitle).dayOfMonth().withMaximumValue().getDayOfMonth();
    }
    //back
    public static String[] getBackDayOfLastMonth(int indexForTitle){
        String monthData[] = new String[42];
        LocalDate firstDay = LocalDate.now().minusMonths(indexForTitle).minusMonths(1).dayOfMonth().withMaximumValue().withDayOfWeek(1).minusDays(1);
        for(int i =0; i<42; i++){
            monthData[i] = String.valueOf(firstDay.plusDays(i).getDayOfMonth());
        }
        return monthData;
    }
    public static int getBackDayOfWeekOfLastMonth(int indexForTitle){
        int dayOfWeek = LocalDate.now().minusMonths(indexForTitle).minusMonths(1).dayOfMonth().withMaximumValue().getDayOfWeek();
        return dayOfWeek;
    }
    public static int getBackDayNumOfMonth(int indexForTitle){
        return LocalDate.now().minusMonths(indexForTitle).dayOfMonth().withMaximumValue().getDayOfMonth();
    }
}
