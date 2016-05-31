package com.daemin.data;

/**
 * Created by hernia on 2015-07-14.
 */
public class BottomNormalData {
    private String year;
    private String MD;
    private String startHour;
    private String startMin;
    private String endHour;
    private String endMin;
    private int xth;
    public BottomNormalData(String year,String MD, String startHour, String startMin, String endHour, String endMin, int xth) {
        this.year = year;
        this.MD = MD;
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
        this.xth = xth;
    }

    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public void setEndMin(String endMin) {
        this.endMin = endMin;
    }
    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }
    public void setStartMin(String startMin) {
        this.startMin = startMin;
    }
    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }
    public String getEndMin() {
        return endMin;
    }
    public String getEndHour() {
        return endHour;
    }
    public String getStartMin() {
        return startMin;
    }
    public String getStartHour() {
        return startHour;
    }
    public int getXth() {
        return xth;
    }
    public String getMD() {
        return MD;
    }
}
