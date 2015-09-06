package com.daemin.data;

/**
 * Created by hernia on 2015-08-08.
 */
public class DayOfWeekData {
    String sun,mon,tue,wed,thr,fri,sat;

    public DayOfWeekData() {
    }
    public void setAllDate(String sun, String mon, String tue, String wed, String thr, String fri, String sat) {
        this.sun = sun;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thr = thr;
        this.fri = fri;
        this.sat = sat;
    }
    public String getSun() {
        return sun;
    }
    public String getMon() {
        return mon;
    }
    public String getTue() {
        return tue;
    }
    public String getWed() {
        return wed;
    }
    public String getThr() {
        return thr;
    }
    public String getFri() {
        return fri;
    }
    public String getSat() {
        return sat;
    }
}
