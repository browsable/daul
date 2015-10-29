package com.daemin.event;

/**
 * Created by hernia on 2015-10-16.
 */
public class SetPlaceEvent {
    String place;

    public SetPlaceEvent(String place) {
        this.place = place;
    }

    public String getPlace() {
        return place;
    }
}
