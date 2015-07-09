package com.daemin.enumclass;

/**
 * Created by hernia on 2015-07-06.
 */
public enum DrawMode {
    CURRENT(0);

    int mode;  // 0 : 일반, 1 : 대학, 2 : 추천, 3 : 대학화면에서 cancel시 일반모드로 돌아감, +버튼 누르면 대학모드로 다시전환됨

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
