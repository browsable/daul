package com.daemin.data;

/**
 * Created by hernia on 2015-07-14.
 */
public class DialogNormalData {
    private String startYear,startMonthOfYear,startDayOfMonth,endYear,endMonthOfYear,endDayOfMonth,startHour,startMinute,endHour,endMinute;
    private int AMPM;

    public String getStartYear() {
        return startYear;
    }

    public DialogNormalData(String startYear, String startMonthOfYear, String startDayOfMonth, String endYear, String endMonthOfYear, String endDayOfMonth, String startHour, String startMinute, String endHour, String endMinute, int AMPM) {
        this.startYear = startYear;
        this.startMonthOfYear = startMonthOfYear;
        this.startDayOfMonth = startDayOfMonth;
        this.endYear = endYear;
        this.endMonthOfYear = endMonthOfYear;
        this.endDayOfMonth = endDayOfMonth;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.AMPM = AMPM;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getStartMonthOfYear() {
        return startMonthOfYear;
    }

    public void setStartMonthOfYear(String startMonthOfYear) {
        this.startMonthOfYear = startMonthOfYear;
    }

    public String getStartDayOfMonth() {
        return startDayOfMonth;
    }

    public void setStartDayOfMonth(String startDayOfMonth) {
        this.startDayOfMonth = startDayOfMonth;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public String getEndMonthOfYear() {
        return endMonthOfYear;
    }

    public void setEndMonthOfYear(String endMonthOfYear) {
        this.endMonthOfYear = endMonthOfYear;
    }

    public String getEndDayOfMonth() {
        return endDayOfMonth;
    }

    public void setEndDayOfMonth(String endDayOfMonth) {
        this.endDayOfMonth = endDayOfMonth;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(String startMinute) {
        this.startMinute = startMinute;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(String endMinute) {
        this.endMinute = endMinute;
    }

    public int getAMPM() {
        return AMPM;
    }

    public void setAMPM(int AMPM) {
        this.AMPM = AMPM;
    }
}
