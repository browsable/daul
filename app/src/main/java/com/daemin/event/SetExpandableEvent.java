package com.daemin.event;

/**
 * Created by hernia on 2015-10-16.
 */
public class SetExpandableEvent {
    int groupPosition;
    public SetExpandableEvent() {
    }
    public SetExpandableEvent(int groupPosition) {

        this.groupPosition = groupPosition;
    }
    public int getGroupPosition() {
        return groupPosition;
    }


}
