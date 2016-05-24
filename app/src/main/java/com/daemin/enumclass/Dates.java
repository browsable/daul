package com.daemin.enumclass;

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
        mData = new String[42];
        dayOfWeek = getDayOfWeek();
        dayOfMonth = getDayOfMonth();
        setWeekData(0);

    }
    public String[] mData;
    public int dayOfWeek,dayOfMonth,year,month,weekOfMonth,dayNumOfMonth,todayIndex;
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
    public int getMonthWithDayIndex(int dayIndex){
        switch (dayIndex) {
            case 0:
                return monthOfSun;
            case 1:
                return monthOfMon;
            case 2:
                return monthOfTue;
            case 3:
                return monthOfWed;
            case 4:
                return monthOfThr;
            case 5:
                return monthOfFri;
            case 6:
                return monthOfSat;
            default:
                return 0;
        }
    }
    public int getDayWithDayIndex(int dayIndex){
        switch (dayIndex) {
            case 0:
                return dayOfSun;
            case 1:
                return dayOfMon;
            case 2:
                return dayOfTue;
            case 3:
                return dayOfWed;
            case 4:
                return dayOfThr;
            case 5:
                return dayOfFri;
            case 6:
                return dayOfSat;
            default:
                return 0;
        }
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
    public String[] getMData(){
        String[] MData = new String[dayNumOfMonth];
        for(int i=0; i<dayNumOfMonth; i++){
            MData[i] =month+"."+mData[i+dayOfWeek+1];
        }
        return MData;
    }
    public String[] getMonthDay(){
        int lastDay = LocalDate.now().dayOfMonth().withMaximumValue().getDayOfMonth();
        String[] MData = new String[lastDay];
        for(int i=0; i<lastDay; i++){
            MData[i] =month+"."+String.valueOf(i+1);
        }
        return MData;
    }
    public String getwMonthDay(int xth) {
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
    public String getmMonthDay(int... index) {
        return mData[index[0]+index[1]];
    }

    static LocalDate fst; //해당날짜의 첫째 date
    public int getYear() {
        return getLastDayOfWeek().getYear();
    }
    public int getDayOfMonth(){ return LocalDate.now().getDayOfMonth();}
    public void setToday(){
        if(todayIndex==0) isToday = true;
        else isToday = false;
    }
    public long getNowMillis() {
        return new DateTime().getMillis();
    }
    public long getDateMillis(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour) {
        DateTime dt = new DateTime(year,monthOfYear,dayOfMonth,hourOfDay,minuteOfHour);
        return dt.getMillis();
    }
    public DateTime getDateTimeMillis(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour) {
        DateTime dt = new DateTime(year,monthOfYear,dayOfMonth,hourOfDay,minuteOfHour);
        return dt;
    }
    public DateTime getDateMillisWithRepeat(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int repeatType, int repeatPeriod) {
        LocalDate ld = new LocalDate(year, monthOfYear, dayOfMonth);
        switch (repeatType){
            case 2: //주
                ld = ld.plusWeeks(repeatPeriod);
                break;
            case 3: //개월
                ld = ld.plusMonths(repeatPeriod);
                break;
            case 4: //년
                ld = ld.plusYears(repeatPeriod);
                break;
        }
        DateTime dt = new DateTime(ld.getYear(),ld.getMonthOfYear(),ld.getDayOfMonth(),hourOfDay,minuteOfHour);
        return dt;
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
    //week
    public int getDayOfWeek() {
        int dayOfWeek = LocalDate.now().getDayOfWeek();
        if(dayOfWeek==7) dayOfWeek=0;
        return dayOfWeek;
    }

    public void setWeekData(int index) {
        todayIndex = index;
        year = getLastDayOfWeek().plusWeeks(index).getYear();
        int lastDayofWeek = getPlusDayOfWeek(index).getWeekOfWeekyear(); //이번주의 토요일의 년대비 주차
        int firstDayOfMonth = getPlusFirstDayOfWeek(index).getWeekOfWeekyear();
        int calWeekOfMonth = lastDayofWeek-firstDayOfMonth+1;
        if(calWeekOfMonth < 0) calWeekOfMonth = lastDayofWeek+1;
        month = getPlusDayOfWeek(index).getMonthOfYear();
        weekOfMonth = calWeekOfMonth;
        if(LocalDate.now().getDayOfWeek()==7) fst = LocalDate.now().plusWeeks(1).weekOfWeekyear().roundFloorCopy().minusDays(1);
        else fst = LocalDate.now().weekOfWeekyear().roundFloorCopy().minusDays(1);
        LocalDate pre = fst.plusWeeks(index);
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
    //month
    public int getDayOfWeekOfLastMonth(){
        int dayOfWeek = LocalDate.now().minusMonths(1).dayOfMonth().withMaximumValue().getDayOfWeek();
        return dayOfWeek;
    }
    public void setMonthData(int index) {
        todayIndex = index;
        year = LocalDate.now().plusMonths(index).getYear();
        month = LocalDate.now().plusMonths(index).getMonthOfYear();
        LocalDate firstDay = LocalDate.now().plusMonths(index).minusMonths(1).dayOfMonth().withMaximumValue().withDayOfWeek(1).minusDays(1);
        for(int i =0; i<42; i++){
            mData[i] = String.valueOf(firstDay.plusDays(i).getDayOfMonth());
        }
        dayOfWeek = LocalDate.now().plusMonths(index).minusMonths(1).dayOfMonth().withMaximumValue().getDayOfWeek();
        dayNumOfMonth = LocalDate.now().plusMonths(index).dayOfMonth().withMaximumValue().getDayOfMonth();
        setToday();
    }
}
