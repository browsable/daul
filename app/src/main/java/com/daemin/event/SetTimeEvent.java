package com.daemin.event;

import android.widget.TextView;

/**
 * Created by hernia on 2015-10-16.
 */
public class SetTimeEvent {
    int timeType, dayIndex, position, startHour,startMin, endHour, endMin;
    public int getTimeType() {
        return timeType;
    }
    public int getPosition() {
        return position;
    }
    public int getDayIndex() {
        return dayIndex;
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
    public SetTimeEvent(int timeType, int position,int dayIndex, int startHour, int startMin, int endHour, int endMin) {
        this.timeType = timeType;
        this.dayIndex = dayIndex;
        this.position = position;
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
    }
}
