package com.daemin.data;

/**
 * Created by hernia on 2015-07-14.
 */
public class BottomNormalData {
    private String YMD;
    private String startHour;
    private String startMin;
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

    private String endHour;

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

    private String endMin;
    private int xth;
    public int getXth() {
        return xth;
    }
    public String getYMD() {
        return YMD;
    }
    public BottomNormalData(String YMD, String startHour, String startMin , String endHour, String endMin, int xth) {
        this.YMD = YMD;
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
        this.xth = xth;
    }
}
