package com.daemin.event;

import android.widget.TextView;

/**
 * Created by hernia on 2015-10-16.
 */
public class SetTimeEvent {
    TextView tvTime,tvMD;
    int position, timeType, day,startHour,startMin, endHour, endMin;
    public int getPosition() {
        return position;
    }
    public int getStartHour() {
        return startHour;
    }
    public int getStartMin() {
        return startMin;
    }
    public int getEndHour() {
        return endHour;
    }
    public int getEndMin() {
        return endMin;
    }
    public int getDay() {
        return day;
    }
    public int getTimeType() {
        return timeType;
    }
    public TextView getTvTime() {
        return tvTime;
    }
    public TextView getTvMD() {
        return tvMD;
    }
    public SetTimeEvent(TextView tvMD,TextView tvTime, int position, int timeType, int day, int startHour, int startMin, int endHour, int endMin) {
        this.position = position;
        this.timeType = timeType;
        this.day = day;
        this.tvTime = tvTime;
        this.tvMD = tvMD;
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
    }
}
