package com.daemin.event;

/**
 * Created by hernia on 2015-10-16.
 */
public class EditCheckEvent {
    boolean isTimeChanged;

    public boolean isTimeChanged() {
        return isTimeChanged;
    }

    public EditCheckEvent(boolean isTimeChanged) {

        this.isTimeChanged = isTimeChanged;
    }
}
