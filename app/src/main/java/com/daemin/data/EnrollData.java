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
    String md;
    String startHour, startMin, endHour, endMin;
    String repeat;
    String color;
    long _id;
    boolean memoEmpty, placeEmpty;

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

    public String getMd() {
        return md;
    }

    public void setMd(String md) {
        this.md = md;
    }

    public void setMemo(String memo) {
        if(memo.equals("")||memo.equals(null))memoEmpty=true;
        else memoEmpty=false;
        this.memo = memo;
    }
    public void setPlace(String place) {
        if(place.equals("")||place.equals(null))placeEmpty=true;
        else placeEmpty=false;
        this.place = place;
    }

    public boolean isMemoEmpty() {
        return memoEmpty;
    }

    public boolean isPlaceEmpty() {
        return placeEmpty;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public void setStartMin(String startMin) {
        this.startMin = startMin;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public void setEndMin(String endMin) {
        this.endMin = endMin;
    }

    public EnrollData(String md,String startHour,String startMin, String endHour, String endMin,String title,String memo,String timeCode,String timeType,String color, String place,String repeat, long _id) {
        if(memo.equals("")||memo.equals(null))memoEmpty=true;
        else memoEmpty=false;
        if(place.equals("")||memo.equals(null))placeEmpty=true;
        else memoEmpty=false;
        this.md=md;
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