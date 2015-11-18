package com.daemin.enumclass;

import android.content.Context;

import com.daemin.timetable.R;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by hernia on 2015-06-27.
 */
public enum Dates {
    NOW();
    Dates(){
        dayOfWeek = getDayOfWeek();
        getWeekData();
    }
    public String[] mData;
    public int dayOfWeek,dayOfWeekOfLastMonth,year,month,weekOfMonth; // DrawMode가 월인 상태에서의 month 값
    public int monthOfSun,monthOfMon,monthOfTue,monthOfWed,monthOfThr,monthOfFri,monthOfSat,
            dayOfSun,dayOfMon,dayOfTue,dayOfWed,dayOfThr,dayOfFri,dayOfSat;
    public String mdOfSun,mdOfMon,mdOfTue,mdOfWed,mdOfThr,mdOfFri,mdOfSat;
    public boolean isToday;
    public void setYear(int year) {
        this.year = year;
    }
    public void setMonth(int month) {
        this.month = month;
    }

    public void setMD(int wSun, int wMon, int wTue, int wWed, int wThr, int wFri, int wSat,
                      int mSun, int mMon, int mTue, int mWed, int mThr, int mFri, int mSat){
        dayOfSun=wSun;
        dayOfMon=wMon;
        dayOfTue=wTue;
        dayOfWed=wWed;
        dayOfThr=wThr;
        dayOfFri=wFri;
        dayOfSat=wSat;
        monthOfSun=mSun;
        monthOfMon=mMon;
        monthOfTue=mTue;
        monthOfWed=mWed;
        monthOfThr=mThr;
        monthOfFri=mFri;
        monthOfSat=mSat;
        mdOfSun = String.valueOf(mSun)+"."+String.valueOf(wSun);
        mdOfMon = String.valueOf(mMon)+"."+String.valueOf(wMon);
        mdOfTue = String.valueOf(mTue)+"."+String.valueOf(wTue);
        mdOfWed = String.valueOf(mWed)+"."+String.valueOf(wWed);
        mdOfThr = String.valueOf(mThr)+"."+String.valueOf(wThr);
        mdOfFri = String.valueOf(mFri)+"."+String.valueOf(wFri);
        mdOfSat = String.valueOf(mSat)+"."+String.valueOf(wSat);
    }
    public String[] getWData(){
        String[] wData = new String[7];
        wData[0] = mdOfSun;
        wData[1] = mdOfMon;
        wData[2] = mdOfTue;
        wData[3] = mdOfWed;
        wData[4] = mdOfThr;
        wData[5] = mdOfFri;
        wData[6] = mdOfSat;
        return wData;
    }
    public String getMonthDay(int xth) {
        switch (xth) {
            case 1:
                return mdOfSun;
            case 3:
                return mdOfMon;
            case 5:
                return mdOfTue;
            case 7:
                return mdOfWed;
            case 9:
                return mdOfThr;
            case 11:
                return mdOfFri;
            case 13:
                return mdOfSat;
            default:
                return "";
        }
    }
    public int getXthToDay(int xth) {
        switch (xth) {
            case 1:
                return dayOfSun;
            case 3:
                return dayOfMon;
            case 5:
                return dayOfTue;
            case 7:
                return dayOfWed;
            case 9:
                return dayOfThr;
            case 11:
                return dayOfFri;
            case 13:
                return dayOfSat;
            default:
                return 0;
        }
    }
    public int getXthToMonth(int xth) {
        switch (xth) {
            case 1:
                return dayOfSun;
            case 3:
                return dayOfMon;
            case 5:
                return dayOfTue;
            case 7:
                return dayOfWed;
            case 9:
                return dayOfThr;
            case 11:
                return dayOfFri;
            case 13:
                return dayOfSat;
            default:
                return 0;
        }
    }
    public void setDayOfWeekOfLastMonth(int dayOfWeekOfLastMonth) {
        this.dayOfWeekOfLastMonth = dayOfWeekOfLastMonth;
    }
    public void setmData(String[] mData) {
        this.mData = mData;
    }



    static LocalDate fst; //해당날짜의 첫째 date
    public String getToday(){
        return LocalDate.now().getMonthOfYear()+"."+LocalDate.now().getDayOfMonth();
    }
    public void setToday(){
        if(getToday().equals(getMonthDay(2 * dayOfWeek + 1))) isToday = true;
        else isToday = false;
    }
    public long getNowMillis() {
        return new DateTime().getMillis();
    }
    public long getDateMillis(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour) {
        DateTime dt = new DateTime(year,monthOfYear,dayOfMonth,hourOfDay,minuteOfHour);
        return dt.getMillis();
    }
    public long getMidnight(){
        return new DateTime().plusDays(1).withTimeAtStartOfDay().getMillis();
    }
    public String getDatefromMillis(long millis){
        DateTime now = new DateTime(millis);
        String pattern = "yyyy-MM-dd hh.mm.ss";
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        String formatted = formatter.print(now);
        return formatted;
    }
    public String getMD(){
        DateTime dt = DateTime.now();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("MM.dd HH:mm");
        return fmt.print(dt);
    }
    public int getDayOfWeek() {
        int dayOfWeek = LocalDate.now().getDayOfWeek();
        if(dayOfWeek==7) dayOfWeek=0;
        return dayOfWeek;
    }
    public int getDayOfMonth(){ return LocalDate.now().getDayOfMonth();}
    public LocalDate getLastDayOfWeek(){
        if(LocalDate.now().getDayOfWeek()==7) return LocalDate.now().plusDays(1).withDayOfWeek(6);
        else return LocalDate.now().withDayOfWeek(6);
    }

    //이달의 첫 날이 속한 토요일
    public LocalDate getFirstDayOfWeekYear(){
        if(getLastDayOfWeek().withDayOfMonth(1).getDayOfWeek()==7) return getLastDayOfWeek().withDayOfMonth(1).plusDays(1).withDayOfWeek(6);
        return getLastDayOfWeek().withDayOfMonth(1).withDayOfWeek(6);
    }

    public LocalDate getPlusDayOfWeek(int plus){
        if(LocalDate.now().getDayOfWeek()==7)  return LocalDate.now().plusDays(1).plusWeeks(plus).withDayOfWeek(6);
        else return LocalDate.now().plusWeeks(plus).withDayOfWeek(6);
    }
    public LocalDate getPlusFirstDayOfWeek(int plus){
        if(getPlusDayOfWeek(plus).withDayOfMonth(1).getDayOfWeek()==7) return getPlusDayOfWeek(plus).withDayOfMonth(1).plusDays(1).withDayOfWeek(6);
        else return getPlusDayOfWeek(plus).withDayOfMonth(1).withDayOfWeek(6);
    }

    public LocalDate getMinusDayOfWeek(int plus){
        if(LocalDate.now().getDayOfWeek()==7)  return LocalDate.now().plusDays(1).minusWeeks(plus).withDayOfWeek(6);
        else return LocalDate.now().minusWeeks(plus).withDayOfWeek(6);
    }
    public LocalDate getMinusFirstDayOfWeek(int minus){
        if(getMinusDayOfWeek(minus).withDayOfMonth(1).getDayOfWeek()==7) return getMinusDayOfWeek(minus).withDayOfMonth(1).plusDays(1).withDayOfWeek(6);
        else return getMinusDayOfWeek(minus).withDayOfMonth(1).withDayOfWeek(6);
    }
    public void getWeekData() {
        year = getYear();
        int lastDayofWeek = getLastDayOfWeek().getWeekOfWeekyear(); //이번주의 토요일의 년대비 주차
        int firstDayOfMonth = getFirstDayOfWeekYear().getWeekOfWeekyear();
        int calWeekOfMonth = lastDayofWeek-firstDayOfMonth+1;
        if(calWeekOfMonth<0) calWeekOfMonth = lastDayofWeek+1;
        month = getLastDayOfWeek().getMonthOfYear();
        weekOfMonth = calWeekOfMonth;
        if(LocalDate.now().getDayOfWeek()==7) fst = LocalDate.now().plusWeeks(1).weekOfWeekyear().roundFloorCopy().minusDays(1);
        else fst = LocalDate.now().weekOfWeekyear().roundFloorCopy().minusDays(1);
        setMD(fst.getDayOfMonth(),
                fst.plusDays(1).getDayOfMonth(),
                fst.plusDays(2).getDayOfMonth(),
                fst.plusDays(3).getDayOfMonth(),
                fst.plusDays(4).getDayOfMonth(),
                fst.plusDays(5).getDayOfMonth(),
                fst.plusDays(6).getDayOfMonth(),
                fst.getMonthOfYear(),
                fst.plusDays(1).getMonthOfYear(),
                fst.plusDays(2).getMonthOfYear(),
                fst.plusDays(3).getMonthOfYear(),
                fst.plusDays(4).getMonthOfYear(),
                fst.plusDays(5).getMonthOfYear(),
                fst.plusDays(6).getMonthOfYear()
        );
        setToday();
    }
    public void getPreWeekData(int indexForTitle) {
        year = getLastDayOfWeek().plusWeeks(indexForTitle).getYear();
        int lastDayofWeek = getPlusDayOfWeek(indexForTitle).getWeekOfWeekyear(); //이번주의 토요일의 년대비 주차
        int firstDayOfMonth = getPlusFirstDayOfWeek(indexForTitle).getWeekOfWeekyear();
        int calWeekOfMonth = lastDayofWeek-firstDayOfMonth+1;
        if(calWeekOfMonth < 0) calWeekOfMonth = lastDayofWeek+1;
        month = getPlusDayOfWeek(indexForTitle).getMonthOfYear();
        weekOfMonth = calWeekOfMonth;
        LocalDate pre = fst.plusWeeks(indexForTitle);
        setMD(pre.getDayOfMonth(),
                pre.plusDays(1).getDayOfMonth(),
                pre.plusDays(2).getDayOfMonth(),
                pre.plusDays(3).getDayOfMonth(),
                pre.plusDays(4).getDayOfMonth(),
                pre.plusDays(5).getDayOfMonth(),
                pre.plusDays(6).getDayOfMonth(),
                pre.getMonthOfYear(),
                pre.plusDays(1).getMonthOfYear(),
                pre.plusDays(2).getMonthOfYear(),
                pre.plusDays(3).getMonthOfYear(),
                pre.plusDays(4).getMonthOfYear(),
                pre.plusDays(5).getMonthOfYear(),
                pre.plusDays(6).getMonthOfYear()
        );
        setToday();
    }
    public void getBackWeekData(int indexForTitle) {
        year = getLastDayOfWeek().minusWeeks(indexForTitle).getYear();
        int lastDayofWeek = getMinusDayOfWeek(indexForTitle).getWeekOfWeekyear(); //이번주의 토요일의 년대비 주차
        int firstDayOfMonth = getMinusFirstDayOfWeek(indexForTitle).getWeekOfWeekyear();
        int calWeekOfMonth = lastDayofWeek - firstDayOfMonth + 1;
        if (calWeekOfMonth < 0) calWeekOfMonth = lastDayofWeek + 1;
        month = getMinusDayOfWeek(indexForTitle).getMonthOfYear();
        weekOfMonth = calWeekOfMonth;
        LocalDate back = fst.minusWeeks(indexForTitle);
        setMD(back.getDayOfMonth(),
                back.plusDays(1).getDayOfMonth(),
                back.plusDays(2).getDayOfMonth(),
                back.plusDays(3).getDayOfMonth(),
                back.plusDays(4).getDayOfMonth(),
                back.plusDays(5).getDayOfMonth(),
                back.plusDays(6).getDayOfMonth(),
                back.getMonthOfYear(),
                back.plusDays(1).getMonthOfYear(),
                back.plusDays(2).getMonthOfYear(),
                back.plusDays(3).getMonthOfYear(),
                back.plusDays(4).getMonthOfYear(),
                back.plusDays(5).getMonthOfYear(),
                back.plusDays(6).getMonthOfYear()
        );
        setToday();
    }
    public void setTitleYearMonth() {
        year = getYear();
        month = LocalDate.now().getMonthOfYear();
    }
    public String preTitleYearMonth(Context context, int indexForTitle) {
        int year = getLastDayOfWeek().plusMonths(indexForTitle).getYear();
        int titleMonth = LocalDate.now().plusMonths(indexForTitle).getMonthOfYear();
        setYear(year);
        setMonth(titleMonth);
        return " " + year + context.getString(R.string.year) + " "
                + titleMonth + context.getString((R.string.month));
    }
    public String backTitleYearMonth(Context context, int indexForTitle) {
        int year = getLastDayOfWeek().minusMonths(indexForTitle).getYear();
        int titleMonth = LocalDate.now().minusMonths(indexForTitle).getMonthOfYear();
        setYear(year);
        setMonth(titleMonth);
        return " " + year + context.getString(R.string.year) + " "
                + titleMonth + context.getString((R.string.month));
    }
    public int getYear() {
        return getLastDayOfWeek().getYear();
    }
    //month
    public String[] getDayOfLastMonth(){
        String monthData[] = new String[42];
        LocalDate firstDay = LocalDate.now().minusMonths(1).dayOfMonth().withMaximumValue().withDayOfWeek(1).minusDays(1);
        for(int i =0; i<42; i++){
            monthData[i] = String.valueOf(firstDay.plusDays(i).getDayOfMonth());
        }
        return monthData;
    }
    public int getDayOfWeekOfLastMonth(){
        int dayOfWeek = LocalDate.now().minusMonths(1).dayOfMonth().withMaximumValue().getDayOfWeek();
        return dayOfWeek;
    }

    public int getDayNumOfMonth(){
        return LocalDate.now().dayOfMonth().withMaximumValue().getDayOfMonth();
    }
    //pre
    public String[] getPreDayOfLastMonth(int indexForTitle){
        String monthData[] = new String[42];
        LocalDate firstDay = LocalDate.now().plusMonths(indexForTitle).minusMonths(1).dayOfMonth().withMaximumValue().withDayOfWeek(1).minusDays(1);
        for(int i =0; i<42; i++){
            monthData[i] = String.valueOf(firstDay.plusDays(i).getDayOfMonth());
        }
        return monthData;
    }
    public int getPreDayOfWeekOfLastMonth(int indexForTitle){
        int dayOfWeek = LocalDate.now().plusMonths(indexForTitle).minusMonths(1).dayOfMonth().withMaximumValue().getDayOfWeek();
        return dayOfWeek;
    }
    public int getPreDayNumOfMonth(int indexForTitle){
        return LocalDate.now().plusMonths(indexForTitle).dayOfMonth().withMaximumValue().getDayOfMonth();
    }
    //back
    public String[] getBackDayOfLastMonth(int indexForTitle){
        String monthData[] = new String[42];
        LocalDate firstDay = LocalDate.now().minusMonths(indexForTitle).minusMonths(1).dayOfMonth().withMaximumValue().withDayOfWeek(1).minusDays(1);
        for(int i =0; i<42; i++){
            monthData[i] = String.valueOf(firstDay.plusDays(i).getDayOfMonth());
        }
        return monthData;
    }
    public int getBackDayOfWeekOfLastMonth(int indexForTitle){
        int dayOfWeek = LocalDate.now().minusMonths(indexForTitle).minusMonths(1).dayOfMonth().withMaximumValue().getDayOfWeek();
        return dayOfWeek;
    }
    public int getBackDayNumOfMonth(int indexForTitle){
        return LocalDate.now().minusMonths(indexForTitle).dayOfMonth().withMaximumValue().getDayOfMonth();
    }

}
