package com.daemin.event;

/**
 * Created by hernia on 2015-10-16.
 */
public class SetCreditEvent {
   String subtitle;

    public String getSubtitle() {
        return subtitle;
    }

    public SetCreditEvent(String subtitle) {

        this.subtitle = subtitle;
    }
}
