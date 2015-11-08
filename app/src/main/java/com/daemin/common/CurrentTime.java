package com.daemin.common;


import android.content.Context;

import com.daemin.data.DayOfWeekData;
import com.daemin.timetable.R;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by hernia on 2015-08-07.
 */
public class CurrentTime {
    static LocalDate now = LocalDate.now();
    static int titleMonth = now.getMonthOfYear();
    //static LocalDate now = new LocalDate(2015,11,29);
    static DayOfWeekData dowd = new DayOfWeekData();
    static LocalDate fst; //해당날짜의 첫째 date
    public static int getYear() {
        return getLastDayOfWeek().getYear();
    }
    public static LocalDate getNow() {
        return now;
    }
    public static String getMonth() {
        return String.valueOf(now.getMonthOfYear());
    }
    public static String getToday(){
        return now.getMonthOfYear()+"/"+now.getDayOfMonth();
    }
    public static long getNowMillis() {
        return new DateTime().getMillis();
    }
    public static long getMidnight(){
        return new DateTime().plusDays(1).withTimeAtStartOfDay().getMillis();
    }
    public static void setTitleMonth(int titleMonth) {
        CurrentTime.titleMonth = titleMonth;

    }
    public static String getMD(){
        DateTime dt = DateTime.now();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("MM.dd HH:mm");
        return fmt.print(dt);
    }
    public static int getTitleMonth() {
        return titleMonth;
    }
    public static int getDayOfWeek() {
        int dayOfWeek = now.getDayOfWeek();
        if(dayOfWeek==7) dayOfWeek=0;
        return dayOfWeek;
    }
    public static int getDayOfMonth(){ return now.getDayOfMonth();}
    public static LocalDate getLastDayOfWeek(){
        if(now.getDayOfWeek()==7) return now.plusDays(1).withDayOfWeek(6);
        else return now.withDayOfWeek(6);
    }

    //이달의 첫 날이 속한 토요일
    public static LocalDate getFirstDayOfWeekYear(){
        if(getLastDayOfWeek().withDayOfMonth(1).getDayOfWeek()==7) return getLastDayOfWeek().withDayOfMonth(1).plusDays(1).withDayOfWeek(6);
        return getLastDayOfWeek().withDayOfMonth(1).withDayOfWeek(6);
    }

    public static LocalDate getPlusDayOfWeek(int plus){
        if(now.getDayOfWeek()==7)  return now.plusDays(1).plusWeeks(plus).withDayOfWeek(6);
        else return now.plusWeeks(plus).withDayOfWeek(6);
    }
    public static LocalDate getPlusFirstDayOfWeek(int plus){
        if(getPlusDayOfWeek(plus).withDayOfMonth(1).getDayOfWeek()==7) return getPlusDayOfWeek(plus).withDayOfMonth(1).plusDays(1).withDayOfWeek(6);
        else return getPlusDayOfWeek(plus).withDayOfMonth(1).withDayOfWeek(6);
    }

    public static LocalDate getMinusDayOfWeek(int plus){
        if(now.getDayOfWeek()==7)  return now.plusDays(1).minusWeeks(plus).withDayOfWeek(6);
        else return now.minusWeeks(plus).withDayOfWeek(6);
    }
    public static LocalDate getMinusFirstDayOfWeek(int minus){
        if(getMinusDayOfWeek(minus).withDayOfMonth(1).getDayOfWeek()==7) return getMinusDayOfWeek(minus).withDayOfMonth(1).plusDays(1).withDayOfWeek(6);
        else return getMinusDayOfWeek(minus).withDayOfMonth(1).withDayOfWeek(6);
    }
    public static DayOfWeekData getDateOfWeek() {
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
    public static DayOfWeekData getPreDateOfWeek(int indexForTitle) {
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
    public static DayOfWeekData getBackDateOfWeek(int indexForTitle) {
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
    public static String getTitleYearMonth(Context context) {
        return " " + getYear() + context.getString(R.string.year) + " "
                + now.getMonthOfYear() + context.getString((R.string.month));
    }
    public static String preTitleYearMonth(Context context, int indexForTitle) {
        titleMonth = now.plusMonths(indexForTitle).getMonthOfYear();
        return " " + getLastDayOfWeek().plusMonths(indexForTitle).getYear() + context.getString(R.string.year) + " "
                + titleMonth + context.getString((R.string.month));
    }
    public static String backTitleYearMonth(Context context, int indexForTitle) {
        titleMonth = now.minusMonths(indexForTitle).getMonthOfYear();
        return " " + getLastDayOfWeek().minusMonths(indexForTitle).getYear() + context.getString(R.string.year) + " "
                + titleMonth + context.getString((R.string.month));
    }
    public static Integer preTitleYear(int indexForTitle) {
        return now.plusWeeks(indexForTitle).getYear();
    }
    public static Integer backTitleYear(int indexForTitle) {
        return now.minusWeeks(indexForTitle).getYear();
    }
    public static String getTitleMonthWeek(Context context) {
        int lastDayofWeek = getLastDayOfWeek().getWeekOfWeekyear(); //이번주의 토요일의 년대비 주차
        int firstDayOfMonth = getFirstDayOfWeekYear().getWeekOfWeekyear();
        int calWeekOfMonth = lastDayofWeek-firstDayOfMonth+1;
        if(calWeekOfMonth<0) calWeekOfMonth = lastDayofWeek+1;
        return " " + getLastDayOfWeek().getMonthOfYear() + context.getString(R.string.month) + " "
                + calWeekOfMonth + context.getString((R.string.weekofmonth));
    }
    public static String preTitleMonthWeek(Context context, int indexForTitle) {
        int lastDayofWeek = getPlusDayOfWeek(indexForTitle).getWeekOfWeekyear(); //이번주의 토요일의 년대비 주차
        int firstDayOfMonth = getPlusFirstDayOfWeek(indexForTitle).getWeekOfWeekyear();
        int calWeekOfMonth = lastDayofWeek-firstDayOfMonth+1;
        if(calWeekOfMonth < 0) calWeekOfMonth = lastDayofWeek+1;
        return " " + getPlusDayOfWeek(indexForTitle).getMonthOfYear() + context.getString(R.string.month) + " "
                + calWeekOfMonth + context.getString((R.string.weekofmonth));
    }
    public static String backTitleMonthWeek(Context context, int indexForTitle) {
        int lastDayofWeek = getMinusDayOfWeek(indexForTitle).getWeekOfWeekyear(); //이번주의 토요일의 년대비 주차
        int firstDayOfMonth = getMinusFirstDayOfWeek(indexForTitle).getWeekOfWeekyear();
        int calWeekOfMonth = lastDayofWeek-firstDayOfMonth+1;
        if(calWeekOfMonth<0) calWeekOfMonth = lastDayofWeek+1;
        return " " + getMinusDayOfWeek(indexForTitle).getMonthOfYear() + context.getString(R.string.month) + " "
                + calWeekOfMonth + context.getString((R.string.weekofmonth));
    }

    //month
    public static String[] getDayOfLastMonth(){
        String monthData[] = new String[42];
        LocalDate firstDay = now.minusMonths(1).dayOfMonth().withMaximumValue().withDayOfWeek(1).minusDays(1);
        for(int i =0; i<42; i++){
            monthData[i] = String.valueOf(firstDay.plusDays(i).getDayOfMonth());
        }
        return monthData;
    }
    public static int getDayOfWeekOfLastMonth(){
        int dayOfWeek = now.minusMonths(1).dayOfMonth().withMaximumValue().getDayOfWeek();
        return dayOfWeek;
    }

    public static int getDayNumOfMonth(){
        return now.dayOfMonth().withMaximumValue().getDayOfMonth();
    }
    //pre
    public static String[] getPreDayOfLastMonth(int indexForTitle){
        String monthData[] = new String[42];
        LocalDate firstDay = now.plusMonths(indexForTitle).minusMonths(1).dayOfMonth().withMaximumValue().withDayOfWeek(1).minusDays(1);
        for(int i =0; i<42; i++){
            monthData[i] = String.valueOf(firstDay.plusDays(i).getDayOfMonth());
        }
        return monthData;
    }
    public static int getPreDayOfWeekOfLastMonth(int indexForTitle){
        int dayOfWeek = now.plusMonths(indexForTitle).minusMonths(1).dayOfMonth().withMaximumValue().getDayOfWeek();
        return dayOfWeek;
    }
    public static int getPreDayNumOfMonth(int indexForTitle){
        return now.plusMonths(indexForTitle).dayOfMonth().withMaximumValue().getDayOfMonth();
    }
    //back
    public static String[] getBackDayOfLastMonth(int indexForTitle){
        String monthData[] = new String[42];
        LocalDate firstDay = now.minusMonths(indexForTitle).minusMonths(1).dayOfMonth().withMaximumValue().withDayOfWeek(1).minusDays(1);
        for(int i =0; i<42; i++){
            monthData[i] = String.valueOf(firstDay.plusDays(i).getDayOfMonth());
        }
        return monthData;
    }
    public static int getBackDayOfWeekOfLastMonth(int indexForTitle){
        int dayOfWeek = now.minusMonths(indexForTitle).minusMonths(1).dayOfMonth().withMaximumValue().getDayOfWeek();
        return dayOfWeek;
    }
    public static int getBackDayNumOfMonth(int indexForTitle){
        return now.minusMonths(indexForTitle).dayOfMonth().withMaximumValue().getDayOfMonth();
    }
}
