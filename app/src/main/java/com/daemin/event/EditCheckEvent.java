package com.daemin.event;

/**
 * Created by hernia on 2015-10-16.
 */
public class EditCheckEvent {
    boolean reStart;

    public boolean isReStart() {
        return reStart;
    }

    public EditCheckEvent(boolean reStart) {

        this.reStart = reStart;
    }
}
