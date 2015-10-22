package com.daemin.event;

/**
 * Created by hernia on 2015-10-16.
 */
public class SetShareEvent {
    String share;
    public String getShare() {
        return share;
    }
    public SetShareEvent(String share) {

        this.share = share;
    }
}
