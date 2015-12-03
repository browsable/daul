package com.daemin.event;

/**
 * Created by hernia on 2015-10-16.
 */
public class RemoveEnrollEvent {
    String timeCode;

    public String getTimeCode() {
        return timeCode;
    }

    public RemoveEnrollEvent(String timeCode) {

        this.timeCode = timeCode;
    }
}
