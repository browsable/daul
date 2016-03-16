package com.daemin.event;

/**
 * Created by hernia on 2015-10-16.
 */
public class SetTimeEvent {
    int position,timeType, day,startHour,startMin, endHour, endMin;
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
    public int getPosition() {
        return position;
    }
    public int getDay() {
        return day;
    }
    public int getTimeType() {
        return timeType;
    }

    public SetTimeEvent(int position, int timeType, int day, int startHour, int startMin, int endHour, int endMin) {
        this.timeType = timeType;
        this.day = day;
        this.position = position;
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
    }
}