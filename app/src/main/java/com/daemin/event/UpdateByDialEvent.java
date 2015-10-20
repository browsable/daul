package com.daemin.event;

/**
 * Created by hernia on 2015-10-16.
 */
public class UpdateByDialEvent {
    String startHour;
    String startMin;
    String endHour;
    String endMin;
    int xth;
    int position;

    public UpdateByDialEvent(String startHour, String startMin, String endHour, String endMin, int xth, int position) {
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
        this.xth = xth;
        this.position = position;
    }

    public String getStartHour() {
        return startHour;
    }

    public String getStartMin() {
        return startMin;
    }

    public String getEndHour() {
        return endHour;
    }

    public String getEndMin() {
        return endMin;
    }

    public int getXth() {
        return xth;
    }

    public int getPosition() {
        return position;
    }
}
