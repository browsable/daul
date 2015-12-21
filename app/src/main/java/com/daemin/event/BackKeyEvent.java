package com.daemin.event;

import android.widget.ImageButton;

/**
 * Created by hernia on 2015-10-16.
 */
public class BackKeyEvent {
    String fragName;
    String[] visibleBt,goneBt;

    public String[] getVisibleBt() {
        return visibleBt;
    }
    public String[] getGoneBt() {
        return goneBt;
    }
    public String getFragName() {
        return fragName;
    }

    public BackKeyEvent(String fragName, String[] visibleBt, String[] goneBt) {
        this.fragName = fragName;
        this.visibleBt=visibleBt;
        this.goneBt=goneBt;
    }
}
