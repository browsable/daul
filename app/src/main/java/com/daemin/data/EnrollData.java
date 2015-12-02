package com.daemin.data;

/**
 * Created by HOME on 2015-09-11.
 */
public class EnrollData {
    private String title, time;
    private long _id;

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public long get_id() {
        return _id;
    }

    public EnrollData(String time,String title,long _id) {
        this.time = time;
        this.title = title;
        this._id = _id;
    }
}