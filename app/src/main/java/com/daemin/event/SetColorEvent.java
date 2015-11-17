package com.daemin.event;

/**
 * Created by hernia on 2015-10-16.
 */
public class SetColorEvent {
    int resColor;

    public SetColorEvent(int resColor) {
        this.resColor = resColor;
    }

    public int getResColor() {
        return resColor;
    }
}
