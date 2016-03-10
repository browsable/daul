package com.daemin.data;

/**
 * Created by HOME on 2015-09-11.
 */
public class EnrollData {
    String dayOfMonth;
    String title;
    String memo;
    String place;
    String timeCode;
    String timeType;
    String startHour, startMin, endHour, endMin;
    String repeat;
    String color;
    long _id;
    boolean timeChanged, singleEffect;
    public String getDayOfMonth() {
        return dayOfMonth;
    }
    public boolean isTimeChanged() {
        return timeChanged;
    }

    public boolean isSingleEffect() {
        return singleEffect;
    }

    public void setSingleEffect(boolean singleEffect) {
        this.singleEffect = singleEffect;
    }

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
    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }
    public void setDayOfMonth(String dayOfMonth) {
        if(!this.dayOfMonth.equals(dayOfMonth)) timeChanged = true;
        this.dayOfMonth = dayOfMonth;
    }
    public void setStartHour(String startHour) {
        if(!this.startHour.equals(startHour)) timeChanged = true;
        this.startHour = startHour;
    }
    public void setStartMin(String startMin) {
        if(!this.startMin.equals(startMin)) timeChanged = true;
        this.startMin = startMin;
    }
    public void setEndHour(String endHour) {
        if(!this.endHour.equals(endHour)) timeChanged = true;
        this.endHour = endHour;
    }
    public void setEndMin(String endMin) {
        if(!this.endMin.equals(endMin)) timeChanged = true;
        this.endMin = endMin;
    }
    public EnrollData(String dayOfMonth,String startHour,String startMin, String endHour, String endMin,String title,String memo,String timeCode,String timeType,String color, String place,String repeat, long _id) {
        this.dayOfMonth = dayOfMonth;
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
        this.timeChanged = false;
        this.singleEffect = true;
    }
}