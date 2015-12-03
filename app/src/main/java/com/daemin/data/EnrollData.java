package com.daemin.data;

import android.view.View;

/**
 * Created by HOME on 2015-09-11.
 */
public class EnrollData {
    private String title,memo,place,timeCode,timeType,credit,time;
    private long _id;

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getMemo() {
        return memo;
    }

    public String getPlace() {
        return place;
    }

    public long get_id() {
        return _id;
    }

    public String getTimeCode() {
        return timeCode;
    }

    public String getTimeType() {
        return timeType;
    }

    public String getCredit() {
        return credit;
    }

    public EnrollData(String time,String title,String memo,String timeCode,String timeType,String credit,String place,long _id) {
        this.time = time;
        this.title = title;
        this.memo = memo;
        this.place = place;
        this.timeCode = timeCode;
        this.timeType = timeType;
        this.credit = credit;
        this._id = _id;
    }
}