package com.daemin.event;

/**
 * Created by hernia on 2015-10-29.
 */
public class SetBtUnivEvent {
    boolean setVisable;

    public SetBtUnivEvent(boolean setVisable) {
        this.setVisable = setVisable;
    }
    public boolean isSetVisable() {
        return setVisable;
    }
}
