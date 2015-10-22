package com.daemin.event;

/**
 * Created by hernia on 2015-10-16.
 */
public class SetExpandableEvent {
    int groupPosition;
    boolean lastItem;

    public boolean isLastItem() {
        return lastItem;
    }

    public SetExpandableEvent(int groupPosition, boolean lastItem) {

        this.groupPosition = groupPosition;
        this.lastItem = lastItem;
    }

    public SetExpandableEvent() {
    }
    public SetExpandableEvent(int groupPosition) {

        this.groupPosition = groupPosition;
    }
    public int getGroupPosition() {
        return groupPosition;
    }


}
