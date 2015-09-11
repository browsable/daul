package com.daemin.data;

/**
 * Created by HOME on 2015-09-11.
 */
public class EventlistData {
    private String title, time, user;

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getUser() {
        return user;
    }

    public EventlistData(String title, String time, String user) {
        this.title = title;
        this.time = time;
        this.user = user;
    }
}