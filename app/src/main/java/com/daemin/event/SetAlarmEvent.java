package com.daemin.event;

/**
 * Created by hernia on 2015-10-16.
 */
public class SetAlarmEvent {
    String time;

    public String getTime() {
        return time;
    }

    public SetAlarmEvent(String time) {

        this.time = time;
    }
}
