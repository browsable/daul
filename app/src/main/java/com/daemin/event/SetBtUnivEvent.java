package com.daemin.event;

/**
 * Created by hernia on 2015-10-29.
 */
public class SetBtUnivEvent {
    boolean setVisible;
    public SetBtUnivEvent(boolean setVisible) {
        this.setVisible = setVisible;

    }
    public boolean isSetVisible() {
        return setVisible;
    }
}
