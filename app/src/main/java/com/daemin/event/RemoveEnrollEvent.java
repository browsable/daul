package com.daemin.event;

/**
 * Created by hernia on 2015-10-16.
 */
public class RemoveEnrollEvent {
    Long id;

    public Long getId() {
        return id;
    }

    public RemoveEnrollEvent(Long id) {

        this.id = id;
    }
}
