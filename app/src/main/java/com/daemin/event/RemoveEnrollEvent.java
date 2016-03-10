package com.daemin.event;

/**
 * Created by hernia on 2015-10-16.
 */
public class RemoveEnrollEvent {
    int position;

    public RemoveEnrollEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
