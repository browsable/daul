package com.daemin.event;

/**
 * Created by hernia on 2015-10-16.
 */
public class EditAlarmEvent {
    String time;
    int position;

    public String getTime() {
        return time;
    }

    public int getPosition() {
        return position;
    }

    public EditAlarmEvent(String time, int position) {
        this.time = time;
        this.position = position;
    }
}
