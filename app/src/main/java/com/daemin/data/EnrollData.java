package com.daemin.data;

/**
 * Created by HOME on 2015-09-11.
 */
public class EnrollData {
    String title;
    String memo;
    String place;
    String timeCode;
    String timeType;
    String startHour, startMin, endHour, endMin;
    String repeat;
    String color;
    long _id;

    public String getTitle() {
        return title;
    }

    public String getMemo() {
        return memo;
    }
    public String getRepeat() {
        return repeat;
    }

    public String getStartHour() {
        return startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public String getStartMin() {
        return startMin;
    }

    public String getEndMin() {
        return endMin;
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
    public String getColor() {
        return color;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public EnrollData(String startHour,String startMin, String endHour, String endMin,String title,String memo,String timeCode,String timeType,String color, String place,String repeat, long _id) {
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
        this.title = title;
        this.memo = memo;
        this.place = place;
        this.timeCode = timeCode;
        this.timeType = timeType;
        this.color = color;
        this._id = _id;
        this.repeat = repeat;
    }
}