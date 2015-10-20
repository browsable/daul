package com.daemin.event;

/**
 * Created by hernia on 2015-10-16.
 */
public class ChangeFragEvent {
    Class cl;
    String titleName;

    public Class getCl() {
        return cl;
    }

    public String getTitleName() {
        return titleName;
    }

    public ChangeFragEvent(Class cl, String titleName) {

        this.cl = cl;
        this.titleName = titleName;
    }
}
