package com.daemin.data;

/**
 * Created by hernia on 2015-07-14.
 */
public class BottomNormalData {
    private String YMD,startTime,endTime;
    public String getYMD() {
        return YMD;
    }
    public String getStartTime() {
        return startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public BottomNormalData(String YMD, String startTime, String endTime) {
        this.YMD = YMD;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
