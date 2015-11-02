package com.daemin.event;

/**
 * Created by hernia on 2015-10-29.
 */
public class SetBtPlusEvent {
    boolean setVisable;

    public SetBtPlusEvent(boolean setVisable) {
        this.setVisable = setVisable;
    }
    public boolean isSetVisable() {
        return setVisable;
    }
}
