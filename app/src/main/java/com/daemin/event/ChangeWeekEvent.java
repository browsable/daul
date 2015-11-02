package com.daemin.event;

/**
 * Created by hernia on 2015-10-29.
 */
public class ChangeWeekEvent {
    boolean btBack;
    public ChangeWeekEvent(boolean btBack) {

        this.btBack = btBack;
    }
    public boolean isBtBack() {
        return btBack;
    }
}
