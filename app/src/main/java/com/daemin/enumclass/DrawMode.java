package com.daemin.enumclass;

/**
 * Created by hernia on 2015-07-06.
 */
public enum DrawMode {
    CURRENT(0);

    int mode;  // 0 : 일반, 1 : 대학

    DrawMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
