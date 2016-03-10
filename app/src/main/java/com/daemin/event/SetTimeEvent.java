package com.daemin.event;

/**
 * Created by hernia on 2015-10-16.
 */
public class SetTimeEvent {
    int position,day,startHour,startMin, endHour, endMin;
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

    public SetTimeEvent(int position,int day, int startHour, int startMin, int endHour, int endMin) {
        this.day = day;
        this.position = position;
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
    }
}
