package com.daemin.event;

/**
 * Created by hernia on 2015-10-16.
 */
public class SetRepeatEvent {
    String repeatType;
    String repeatPeriod;
    String repeatNumber;
    public SetRepeatEvent(String repeatType, String repeatPeriod, String repeatNumber) {
        this.repeatType = repeatType;


        this.repeatPeriod = repeatPeriod;
        this.repeatNumber = repeatNumber;
    }
    public String getRepeatType() {
        return repeatType;
    }

    public String getRepeatPeriod() {
        return repeatPeriod;
    }

    public String getRepeatNumber() {
        return repeatNumber;
    }

    @Override
    public String toString() {
        if(!repeatType.equals("반복 없음")) repeatNumber +="회 : ";
        return repeatNumber+
                repeatPeriod
                + repeatType;
    }
}
