package com.daemin.event;

/**
 * Created by hernia on 2015-10-16.
 */
public class SetTimeEvent {
    String md;
    int position, startHour,startMin, endHour, endMin;
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
    public String getMd() {
        return md;
    }
    public int getPosition() {
        return position;
    }

    public SetTimeEvent(String md, int position, int startHour, int startMin, int endHour, int endMin) {
        this.md = md;
        this.position = position;
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
    }
}
