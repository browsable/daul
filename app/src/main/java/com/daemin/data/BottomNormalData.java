package com.daemin.data;

/**
 * Created by hernia on 2015-07-14.
 */
public class BottomNormalData {
    private String YMD,startTime,endTime;

    public String getYMD() {
        return YMD;
    }

    public void setYMD(String YMD) {
        this.YMD = YMD;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public BottomNormalData(String YMD, String startTime, String endTime) {
        this.YMD = YMD;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
